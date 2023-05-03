package me.jaeyeon.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeon.blog.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	Page<Comment> findAllByPost_Id(Long postId, Pageable pageable);
}
