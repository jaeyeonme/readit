package me.jaeyeon.readitdomain.post.service.port;

import java.util.Optional;

import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.infrastructure.PostRepositoryCustom;

public interface PostRepository extends PostRepositoryCustom {

	Optional<Post> findById(Long id);
	Post save(Post post);
	void deleteById(Long id);
	void delete(Post post);
}
