package me.jaeyeon.readitdomain.post.service.port;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.readitdomain.post.domain.DailyPostCountRequest;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.infrastructure.PostCountPerDate;

public interface PostRepository {

	Optional<Post> findById(Long id);
	Post save(Post post);
	void deleteById(Long id);
	void delete(Post post);
	Page<Post> findByTitleContainingOrContentContaining(String keyword, Pageable pageable);
	Page<Post> findAllByAuthorId(Long memberId, Pageable pageable);
	List<PostCountPerDate> countPostsByMemberAndDateRange(Long memberId, LocalDate startDate, LocalDate endDate);
}
