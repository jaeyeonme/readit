package me.jaeyeon.blog.repository;

import static me.jaeyeon.blog.model.QPost.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.model.Post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Post> findByTitleContainingOrContentContaining(String keyword, Pageable pageable) {
		List<Post> posts = queryFactory.selectFrom(post)
				.where(titleContains(keyword).or(contentContains(keyword)))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = queryFactory.selectFrom(post)
				.where(titleContains(keyword).or(contentContains(keyword)))
				.fetch().size();

		return new PageImpl<>(posts, pageable, total);
	}

	private BooleanExpression titleContains(String keyword) {
		return keyword != null ? post.title.like("%" + keyword + "%") : null;
	}

	private BooleanExpression contentContains(String keyword) {
		return keyword != null ? post.content.like("%" + keyword + "%") : null;
	}
}
