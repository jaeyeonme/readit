package me.jaeyeon.readitdomain.post.infrastructure;

import static me.jaeyeon.readitdomain.post.infrastructure.QPostEntity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;
import me.jaeyeon.readitdomain.member.infrastructure.MemberJpaRepository;
import me.jaeyeon.readitdomain.post.domain.DailyPostCountRequest;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.service.port.PostRepository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

	private final PostJpaRepository postJpaRepository;
	private final JPAQueryFactory queryFactory;
	private final MemberJpaRepository memberJpaRepository;

	@Override
	public Optional<Post> findById(Long id) {
		return postJpaRepository.findById(id).map(PostEntity::toModel);
	}

	@Override
	public Post save(Post post) {
		return postJpaRepository.save(post.toEntity()).toModel();
	}

	@Override
	public void deleteById(Long id) {
		postJpaRepository.deleteById(id);
	}

	@Override
	public void delete(Post post) {
		postJpaRepository.delete(post.toEntity());
	}

	@Override
	public Page<Post> findAllByAuthorId(Long memberId, Pageable pageable) {
		MemberEntity memberEntity = memberJpaRepository.findById(memberId)
				.orElseThrow(() -> new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));
		Page<PostEntity> postEntitiesPage = postJpaRepository.findAllByAuthor(memberEntity, pageable);
		List<Post> posts = postEntitiesPage.stream()
				.map(PostEntity::toModel)
				.collect(Collectors.toList());

		return new PageImpl<>(posts, pageable, postEntitiesPage.getTotalElements());
	}

	@Override
	public List<PostCountPerDate> countPostsByMemberAndDateRange(Long memberId, LocalDate startDate,
			LocalDate endDate) {
		return postJpaRepository.countPostsByMemberAndDateRange(memberId, startDate, endDate);
	}

	@Override
	public Page<Post> findByTitleContainingOrContentContaining(String keyword, Pageable pageable) {
		List<PostEntity> postEntities = queryFactory.selectFrom(postEntity)
				.where(titleContains(keyword).or(contentContains(keyword)))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = queryFactory.selectFrom(postEntity)
				.where(titleContains(keyword).or(contentContains(keyword)))
				.fetch().size();

		List<Post> posts = postEntities.stream()
				.map(PostEntity::toModel)
				.collect(Collectors.toList());

		return new PageImpl<>(posts, pageable, total);
	}

	private BooleanExpression titleContains(String keyword) {
		return keyword != null ? postEntity.title.like("%" + keyword + "%") : null;
	}

	private BooleanExpression contentContains(String keyword) {
		return keyword != null ? postEntity.content.like("%" + keyword + "%") : null;
	}
}
