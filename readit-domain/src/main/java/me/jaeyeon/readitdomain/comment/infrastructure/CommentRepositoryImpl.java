package me.jaeyeon.readitdomain.comment.infrastructure;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.comment.domain.Comment;
import me.jaeyeon.readitdomain.comment.service.port.CommentRepository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

	private final CommentJpaRepository commentJpaRepository;

	@Override
	public Comment save(Comment comment) {
		CommentEntity savedEntity = commentJpaRepository.save(comment.toEntity());
		return savedEntity.toModel();
	}

	@Override
	public Optional<Comment> findById(Long id) {
		return commentJpaRepository.findById(id).map(CommentEntity::toModel);
	}

	@Override
	public void updateContent(Long id, String content) {
		CommentEntity commentEntity = commentJpaRepository.findById(id)
				.orElseThrow(() -> new BlogApiException(ErrorCode.COMMENT_NOT_FOUND));

		commentEntity.updateComment(content);
		commentJpaRepository.save(commentEntity);
	}

	@Override
	public void delete(Comment comment) {
		commentJpaRepository.delete(comment.toEntity());
	}

	@Override
	public Page<Comment> findAllByPost_Id(Long postId, Pageable pageable) {
		return commentJpaRepository.findAllByPost_Id(postId, pageable).map(CommentEntity::toModel);
	}
}
