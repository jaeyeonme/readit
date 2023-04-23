package me.jaeyeon.blog.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.jaeyeon.blog.model.Comment;

@Data
@NoArgsConstructor
public class CommentRes {

	private Long id;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public CommentRes(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.createdDate = comment.getCreatedDate();
		this.modifiedDate = comment.getModifiedDate();
	}
}
