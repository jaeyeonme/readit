package me.jaeyeon.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.MemberRepository;
import me.jaeyeon.blog.repository.PostRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void createPost(PostReq postReq, Long memberId) {
        Member author = memberRepository.findById(memberId)
            .orElseThrow(() -> new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));
        Post post = postReq.toEntity(author);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
            .map(PostResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
            () -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
        return new PostResponse(post);
    }

    public void updatePost(PostReq postReq, Long id, Long memberId) {
        Post post = getPost(id);
        checkWhetherAuthor(memberId, post);
        post.update(postReq.getTitle(), postReq.getContent());
    }

    public void deletePostById(Long id, Long memberId) {
        Post post = getPost(id);
        checkWhetherAuthor(memberId, post);
        postRepository.delete(post);
    }

    public Post findPostById(Long postId) {
        return getPost(postId);
    }

    private Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(
            () -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
    }

    public void checkWhetherAuthor(Long memberId, Post post) {
        if (!post.isAuthor(memberId)) {
            throw new BlogApiException(ErrorCode.IS_NOT_OWNER);
        }
    }
}
