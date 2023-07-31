package me.jaeyeon.readitapi.follow.controller.response;

import lombok.Builder;
import me.jaeyeon.readitdomain.follow.domain.Follow;


@Builder
public record FollowResponse(
		Long id,
		String followerUserName,
		String followerEmail
) {
	public static FollowResponse from(Follow follow) {
		return FollowResponse.builder()
				.id(follow.getId())
				.followerUserName(follow.getFromMember().getUserName())
				.followerEmail(follow.getFromMember().getEmail())
				.build();
	}
}
