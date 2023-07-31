package me.jaeyeon.readitdomain.follow.service;

import me.jaeyeon.readitdomain.follow.domain.Follow;
import me.jaeyeon.readitdomain.member.domain.Member;

public interface FollowWriteUseCase {
	Follow follow(Long fromMemberId, Long toMemberId);
}
