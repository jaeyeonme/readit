package me.jaeyeon.readitdomain.post.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.member.service.AuthenticationUseCase;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.infrastructure.PostCountPerDate;
import me.jaeyeon.readitdomain.post.service.port.PostRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostReadServiceImpl implements PostReadUseCase {

	private final PostRepository postRepository;
	private final AuthenticationUseCase authenticationUseCase;

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
}
