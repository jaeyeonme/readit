package me.jaeyeon.readitdomain.post.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.infrastructure.PostCountPerDate;

public interface PostReadUseCase {
	Page<Post> searchPostsWithKeyword(String keyword, Pageable pageable);
	Post getPostById(Long id);
	Page<Post> getPostsByAuthorId(Long memberId, Pageable pageable);
	List<PostCountPerDate> countPostsByMemberAndDateRange(Long memberId, LocalDate startDate, LocalDate endDate);
}
