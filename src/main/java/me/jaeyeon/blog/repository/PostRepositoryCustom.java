package me.jaeyeon.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.blog.model.Post;

public interface PostRepositoryCustom {

	Page<Post> findByTitleContainingOrContentContaining(String keyword, Pageable page);
}
