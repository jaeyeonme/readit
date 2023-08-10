package me.jaeyeon.readitdomain.post.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.common.utils.CursorRequest;
import me.jaeyeon.common.utils.PageCursor;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.infrastructure.PostCountPerDate;
import me.jaeyeon.readitdomain.post.service.port.PostRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostReadServiceImpl implements PostReadUseCase {

	private final PostRepository postRepository;

	@Override
	public Page<Post> searchPostsWithKeyword(String keyword, Pageable pageable) {
		Page<Post> result = postRepository.findByTitleContainingOrContentContaining(keyword, pageable);
		log.info("Retrieved {} posts with keyword: {}", result.getNumberOfElements(), keyword);
		return result;
	}

	@Override
	public Post getPostById(Long id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
	}

	@Override
	public Page<Post> getPostsByAuthorId(Long memberId, Pageable pageable) {
		return postRepository.findAllByAuthorId(memberId, pageable);
	}

	@Override
	public List<PostCountPerDate> countPostsByMemberAndDateRange(Long memberId, LocalDate startDate,
			LocalDate endDate) {
		return postRepository.countPostsByMemberAndDateRange(memberId, startDate, endDate);
	}

	@Override
	public PageCursor<Post> getPostsByAuthorIdAndCursor(Long memberId, CursorRequest cursorRequest) {
		int limit = cursorRequest.size();
		Long cursor = cursorRequest.hasKey() ? cursorRequest.key() : null;

		List<Post> posts = postRepository.findPostsByAuthorIdAndCursor(memberId, cursor, PageRequest.of(0, limit));


		Long nextCursor = !posts.isEmpty() ? posts.get(posts.size() - 1).getId() : CursorRequest.NONE_KEY;
		CursorRequest nextCursorRequest = new CursorRequest(nextCursor, limit);

		return new PageCursor<>(nextCursorRequest, posts);
	}
}
