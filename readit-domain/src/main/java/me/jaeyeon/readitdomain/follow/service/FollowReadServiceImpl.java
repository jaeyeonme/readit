package me.jaeyeon.readitdomain.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitdomain.follow.domain.Follow;
import me.jaeyeon.readitdomain.follow.service.port.FollowRepository;
import me.jaeyeon.readitdomain.member.domain.Member;

@Service
@RequiredArgsConstructor
public class FollowReadServiceImpl implements FollowReadUseCase {

	private final FollowRepository followRepository;

	@Override
	public List<Follow> getFollowings(Long memberId) {
		Member member = Member.builder().id(memberId).build();
		return followRepository.findAllByFromMember(member);
	}

	@Override
	public List<Follow> getFollowers(Long memberId) {
		Member member = Member.builder().id(memberId).build();
		return followRepository.findAllByToMember(member);
	}
}
