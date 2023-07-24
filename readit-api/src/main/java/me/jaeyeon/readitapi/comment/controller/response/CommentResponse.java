package me.jaeyeon.readitapi.comment.controller.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitdomain.comment.domain.Comment;

@Getter
@Builder
public class CommentResponse {

	private Long id;
	private String  content;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public static CommentResponse from(Comment comment) {
		return CommentResponse.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.createdDate(comment.getCreatedDate())
				.modifiedDate(comment.getModifiedDate())
				.build();
	}
}
