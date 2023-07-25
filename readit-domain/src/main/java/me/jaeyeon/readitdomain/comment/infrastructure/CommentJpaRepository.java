package me.jaeyeon.readitdomain.comment.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

	Page<CommentEntity> findAllByPost_Id(Long postId, Pageable pageable);
}
