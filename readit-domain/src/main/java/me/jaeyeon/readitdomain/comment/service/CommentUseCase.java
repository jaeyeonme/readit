package me.jaeyeon.readitdomain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.readitdomain.comment.domain.Comment;
import me.jaeyeon.readitdomain.comment.domain.CommentCreate;
import me.jaeyeon.readitdomain.member.domain.Member;

public interface CommentUseCase {

	Comment createComment(CommentCreate commentCreate, Long postId, Member member);
	Comment replyToComment(CommentCreate commentCreate, Long postId, Long parentCommentId, Member member);
	Page<Comment> getCommentsByPostId(Long postId, Pageable pageable);
	Comment getCommentById(Long id);
	Comment updateComment(Long id, CommentCreate commentCreate, Member member);
	void deleteComment(Long id, Member member);
}
