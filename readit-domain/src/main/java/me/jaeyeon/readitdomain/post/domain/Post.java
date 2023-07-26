package me.jaeyeon.readitdomain.post.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitdomain.comment.domain.Comment;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.post.infrastructure.PostEntity;

@Getter
public class Post {

	private final Long id;
	private final String title;
	private final String content;
	private final Member author;
	private final List<Comment> comments;
	private final LocalDateTime createdDate;
	private final LocalDateTime modifiedDate;

	@Builder
	public Post(Long id, String title, String content, Member author, List<Comment> comments,
			LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.author = author;
		this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public Post update(String title, String content) {
		return Post.builder()
				.id(this.id)
				.title(title)
				.content(content)
				.author(this.author)
				.comments(this.comments)
				.createdDate(this.createdDate)
				.modifiedDate(LocalDateTime.now())
				.build();
	}

	public PostEntity toEntity() {
		PostEntity postEntity = PostEntity.builder()
				.title(title)
				.content(content)
				.author(author.toEntity())
				.build();

		comments.forEach(comment -> postEntity.addComment(comment.toEntity()));

		return postEntity;
	}
}
