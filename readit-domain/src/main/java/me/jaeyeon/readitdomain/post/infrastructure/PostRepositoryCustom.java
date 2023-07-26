package me.jaeyeon.readitdomain.post.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.readitdomain.post.domain.Post;

public interface PostRepositoryCustom {

	Page<Post> findByTitleContainingOrContentContaining(String keyword, Pageable page);
}
