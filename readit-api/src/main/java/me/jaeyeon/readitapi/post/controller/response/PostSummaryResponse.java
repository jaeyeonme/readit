package me.jaeyeon.readitapi.post.controller.response;

import java.time.LocalDateTime;

import me.jaeyeon.readitdomain.post.domain.Post;

public record PostSummaryResponse(
		Long id,
		String title,
		LocalDateTime createdDate
) {

	public static PostSummaryResponse from(Post post) {
		return new PostSummaryResponse(
				post.getId(),
				post.getTitle(),
				post.getCreatedDate());
	}
}
