package me.jaeyeon.blog.repository;

import me.jaeyeon.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
