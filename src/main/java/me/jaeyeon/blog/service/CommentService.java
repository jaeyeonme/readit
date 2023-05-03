package me.jaeyeon.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Member;

public interface CommentService {
	Long createComment(CommentReq commentReq, Long postId, Member member);

	Long saveReplyComment(Long commentId, CommentReq request, Member member, Long postId);

	Page<CommentRes> getCommentsByPostId(Long postId, Pageable pageable);

	CommentRes getCommentById(Long commentId);

	void updateComment(Long id, CommentReq commentReq, Member member);

	void deleteComment(Long id, Member member);

	Comment getComment(Long id);

	void checkWhetherAuthor(Comment comment, Member member);
}
