package me.jaeyeon.readitdomain.comment.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitdomain.comment.infrastructure.CommentEntity;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.post.domain.Post;

@Getter
public class Comment {

	private final Long id;
	private final String content;
	private final Member author;
	private final Post post;
	private Comment parent;
	private final List<Comment> children = new ArrayList<>();
	private final LocalDateTime createdDate;
	private final LocalDateTime modifiedDate;

	@Builder
	public Comment(Long id, String content, Member author, Post post, Comment parent, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.id = id;
		this.content = content;
		this.author = author;
		this.post = post;
		this.parent = parent;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
		if (parent != null) {
			parent.getChildren().add(this);
		}
	}

	public void addChild(Comment child) {
		children.add(child);
		if (child.getParent() != this) {
			child.setParent(this);
		}
	}

	public CommentEntity toEntity() {
		return CommentEntity.builder()
				.author(this.author.toEntity())
				.post(this.post.toEntity())
				.content(this.content)
				.parent(this.parent != null ? parent.toEntity() : null)
				.build();
	}
}
