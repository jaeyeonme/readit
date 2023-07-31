package me.jaeyeon.readitdomain.follow.service;

import java.util.List;

import me.jaeyeon.readitdomain.follow.domain.Follow;

public interface FollowReadUseCase {
	List<Follow> getFollowings(Long memberId);
	List<Follow> getFollowers(Long memberId);
}
