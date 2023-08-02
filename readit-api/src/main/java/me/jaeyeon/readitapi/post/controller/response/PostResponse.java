package me.jaeyeon.readitapi.post.controller.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import me.jaeyeon.readitapi.comment.controller.response.CommentResponse;
import me.jaeyeon.readitdomain.post.domain.Post;

public record PostResponse(
		Long id,
		String title,
		String content,
		LocalDateTime createdDate,
		LocalDateTime modifiedDate,
		List<CommentResponse> comments) {

	public static PostResponse from(Post post) {
		return new PostResponse(
				post.getId(),
				post.getTitle(),
				post.getContent(),
				post.getCreatedDate(),
				post.getModifiedDate(),
				post.getComments().stream().map(CommentResponse::from).collect(Collectors.toList()));
	}
}
