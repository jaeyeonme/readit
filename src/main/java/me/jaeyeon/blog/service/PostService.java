package me.jaeyeon.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.model.Post;

public interface PostService {

	void createPost(PostReq postReq, Long memberId);
	Page<PostResponse> getAllPosts(Pageable pageable);
	Page<PostResponse> searchPostsWithKeyword(String keyword, Pageable pageable);
	PostResponse getPostById(Long id);
	void updatePost(PostReq postReq, Long id, Long memberId);
	void deletePostById(Long id, Long memberId);
	Post findPostById(Long postId);
	Post getPost(Long id);
	void checkWhetherAuthor(Post post);
}
