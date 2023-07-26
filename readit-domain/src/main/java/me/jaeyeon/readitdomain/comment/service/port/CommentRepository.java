package me.jaeyeon.readitdomain.comment.service.port;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.readitdomain.comment.domain.Comment;

public interface CommentRepository {

	Comment save(Comment comment);
	Optional<Comment> findById(Long id);
	void updateContent(Long id, String content);
	void delete(Comment comment);
	Page<Comment> findAllByPost_Id(Long postId, Pageable pageable);
}
