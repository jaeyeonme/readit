package me.jaeyeon.readitdomain.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.service.AuthenticationUseCase;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.domain.PostCreate;
import me.jaeyeon.readitdomain.post.domain.PostUpdate;
import me.jaeyeon.readitdomain.post.service.port.PostRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteServiceImpl implements PostWriteUseCase {

	private final PostRepository postRepository;
	private final AuthenticationUseCase authenticationUseCase;

	@Override
	public void createPost(PostCreate postCreate, Member author) {
		Post post = postCreate.toEntity(author);
		postRepository.save(post);
		log.info("Post created with id: {}", post.getId());
	}

	@Override
	public void updatePost(PostUpdate postUpdate, Long id, Member author) {
		Post post = getPostById(id);
		checkWhetherAuthor(post);
		post.update(postUpdate.title(), postUpdate.content());
		log.info("Post updated with id: {}", id);
	}

	@Override
	public void deletePostById(Long id, Member author) {
		Post post = getPostById(id);
		checkWhetherAuthor(post);
		postRepository.delete(post);
		log.info("Post deleted with id: {}", id);
	}

	private Post getPostById(Long id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
	}

	private void checkWhetherAuthor(Post post) {
		Member loginMember = authenticationUseCase.getLoginMember();
		if (!post.getAuthor().equals(loginMember)) {
			throw new BlogApiException(ErrorCode.IS_NOT_OWNER);
		}
	}
}
