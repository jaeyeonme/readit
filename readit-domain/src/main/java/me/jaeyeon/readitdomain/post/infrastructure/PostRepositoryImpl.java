package me.jaeyeon.readitdomain.post.infrastructure;

import static me.jaeyeon.readitdomain.post.infrastructure.QPostEntity.*;

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
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.service.port.PostRepository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository, PostRepositoryCustom {

	private final PostJpaRepository postJpaRepository;
	private final JPAQueryFactory queryFactory;

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
