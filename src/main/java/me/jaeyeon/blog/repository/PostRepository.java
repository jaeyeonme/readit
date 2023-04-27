package me.jaeyeon.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeon.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}
