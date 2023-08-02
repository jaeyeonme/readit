package me.jaeyeon.readitdomain.post.service;

import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.post.domain.PostCreate;
import me.jaeyeon.readitdomain.post.domain.PostUpdate;

public interface PostWriteUseCase {
	void createPost(PostCreate postCreate, Member author);
	void updatePost(PostUpdate postUpdate, Long id, Member author);
	void deletePostById(Long id, Member author);
}
