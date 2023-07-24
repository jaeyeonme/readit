package me.jaeyeon.readitapi.post.controller.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitapi.comment.controller.response.CommentResponse;
import me.jaeyeon.readitdomain.post.domain.Post;

@Getter
@Builder
public class PostResponse {

	private Long id;
	private String title;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private List<CommentResponse> comments;

	public static PostResponse from(Post post) {
		return PostResponse.builder()
				.id(post.getId())
				.title(post.getTitle())
				.content(post.getContent())
				.createdDate(post.getCreatedDate())
				.modifiedDate(post.getModifiedDate())
				.comments(post.getComments().stream().map(CommentResponse::from).collect(Collectors.toList()))
				.build();
	}
}
