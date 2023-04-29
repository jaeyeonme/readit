package me.jaeyeon.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.MemberRepository;
import me.jaeyeon.blog.repository.PostRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BlogPostService implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LoginService loginService;

    @Override
    public void createPost(PostReq postReq, Long memberId) {
        Member author = memberRepository.findById(memberId)
            .orElseThrow(() -> new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));
        Post post = postReq.toEntity(author);
        postRepository.save(post);
        log.info("Post created with id: {}", post.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<PostResponse> result = postRepository.findAll(pageable).map(PostResponse::new);
        log.info("Retrieved {} posts", result.getNumberOfElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> searchPostsWithKeyword(String keyword, Pageable pageable) {
        Page<PostResponse> result = postRepository.findByTitleContainingOrContentContaining(keyword, pageable)
            .map(PostResponse::new);
        log.info("Retrieved {} posts with keyword: {}", result.getNumberOfElements(), keyword);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
            () -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
        return new PostResponse(post);
    }

    @Override
    public void updatePost(PostReq postReq, Long id, Long memberId) {
        Post post = getPost(id);
        checkWhetherAuthor(post);
        post.update(postReq.getTitle(), postReq.getContent());
        log.info("Post updated with id: {}", id);
    }

    @Override
    public void deletePostById(Long id, Long memberId) {
        Post post = getPost(id);
        checkWhetherAuthor(post);
        postRepository.delete(post);
        log.info("Post deleted with id: {}", id);
    }

    @Override
    public Post findPostById(Long postId) {
        return getPost(postId);
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(
            () -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
    }

    @Override
    public void checkWhetherAuthor(Post post) {
        Member loggedInMember = loginService.getLoginMember();
        if (!post.getAuthor().equals(loggedInMember)) {
            throw new BlogApiException(ErrorCode.IS_NOT_OWNER);
        }
    }
}
