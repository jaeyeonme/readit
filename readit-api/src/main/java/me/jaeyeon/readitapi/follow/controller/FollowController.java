package me.jaeyeon.readitapi.follow.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitapi.follow.controller.response.FollowResponse;
import me.jaeyeon.readitdomain.follow.domain.Follow;
import me.jaeyeon.readitdomain.follow.service.FollowReadUseCase;
import me.jaeyeon.readitdomain.follow.service.FollowWriteUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

	private final FollowReadUseCase followReadUseCase;
	private final FollowWriteUseCase followWriteUseCase;

	@PostMapping("/{fromMemberId}/{toMemberId}")
	public ResponseEntity<Void> followMember(@PathVariable Long fromMemberId, @PathVariable Long toMemberId) {
		followWriteUseCase.follow(fromMemberId, toMemberId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{fromMemberId}/followings")
	public ResponseEntity<List<FollowResponse>> getFollowings(@PathVariable Long fromMemberId) {
		List<Follow> followings = followReadUseCase.getFollowings(fromMemberId);
		return ResponseEntity.ok(followings.stream()
				.map(FollowResponse::from)
				.collect(Collectors.toList()));
	}

	@GetMapping("/{fromMemberId}/followers")
	public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable Long fromMemberId) {
		List<Follow> followers = followReadUseCase.getFollowers(fromMemberId);
		return ResponseEntity.ok(followers.stream()
				.map(FollowResponse::from)
				.collect(Collectors.toList()));
	}
}
