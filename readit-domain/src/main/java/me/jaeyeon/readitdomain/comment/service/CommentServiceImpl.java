package me.jaeyeon.readitdomain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.comment.domain.Comment;
import me.jaeyeon.readitdomain.comment.domain.CommentCreate;
import me.jaeyeon.readitdomain.comment.service.port.CommentRepository;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.service.AuthenticationUseCase;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.service.PostUseCase;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentUseCase {

	private final CommentRepository commentRepository;
	private final PostUseCase postUseCase;
	private final AuthenticationUseCase authenticationUseCase;

	@Override
	public Comment createComment(CommentCreate commentCreate, Long postId, Member member) {
		Post post = postUseCase.getPostById(postId);
		Comment comment = commentCreate.toEntity(member, post);
		commentRepository.save(comment);
		return comment;
	}

	@Override
	public Comment replyToComment(CommentCreate commentCreate, Long postId, Long parentCommentId, Member member) {
		Comment parentComment = getCommentById(parentCommentId);
		Post post = postUseCase.getPostById(postId);
		Comment childComment = commentCreate.toEntity(member, post, parentComment);
		return commentRepository.save(childComment);
	}

	@Override
	public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
		return commentRepository.findAllByPost_Id(postId, pageable);
	}

	@Override
	public Comment getCommentById(Long id) {
		return commentRepository.findById(id)
				.orElseThrow(() -> new BlogApiException(ErrorCode.COMMENT_NOT_FOUND));
	}

	@Override
	public Comment updateComment(Long id, CommentCreate commentCreate, Member member) {
		Comment comment = getCommentById(id);
		checkWhetherAuthor(comment);
		commentRepository.updateContent(id, commentCreate.getContent());
		return getCommentById(id); // Fetch the updated comment
	}

	@Override
	public void deleteComment(Long id, Member member) {
		Comment comment = getCommentById(id);
		checkWhetherAuthor(comment);
		commentRepository.delete(comment);
	}

	private void checkWhetherAuthor(Comment comment) {
		Member loginMember = authenticationUseCase.getLoginMember();
		if (!comment.getAuthor().equals(loginMember)) {
			throw new BlogApiException(ErrorCode.IS_NOT_OWNER);
		}
	}
}
