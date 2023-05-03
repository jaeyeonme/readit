package me.jaeyeon.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;

public interface PostService {

	void createPost(PostReq postReq, Member member);

	Page<PostResponse> searchPostsWithKeyword(String keyword, Pageable pageable);

	PostResponse getPostById(Long id);

	void updatePost(PostReq postReq, Long id, Member member);

	void deletePostById(Long id, Member member);

	Post findPostById(Long postId);

	Post getPost(Long id);

	void checkWhetherAuthor(Post post, Member member);
}
