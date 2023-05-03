package me.jaeyeon.blog.repository;

import static me.jaeyeon.blog.model.QPost.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.model.Post;

@RequiredArgsConstructor
public class PostRepositoryIml implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Post> findByTitleContainingOrContentContaining(String keyword, Pageable pageable) {
		List<Post> posts = queryFactory.selectFrom(post)
				.where(matchAgainstTitleAndContent(keyword).gt(0))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = queryFactory.selectFrom(post).where(matchAgainstTitleAndContent(keyword).gt(0)).fetch().size();

		return new PageImpl<>(posts, pageable, total);
	}

	private NumberExpression<Double> matchAgainstTitleAndContent(String keyword) {
		return Expressions.numberTemplate(Double.class, "function('match_against', {0}, {1}, {2})", post.title,
				post.content, keyword);
	}
}
