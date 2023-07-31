package me.jaeyeon.readitdomain.follow.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.follow.domain.Follow;
import me.jaeyeon.readitdomain.follow.service.port.FollowRepository;
import me.jaeyeon.readitdomain.member.domain.Member;

@Service
@RequiredArgsConstructor
public class FollowWriteServiceImpl implements FollowWriteUseCase {

	private final FollowRepository followRepository;

	@Override
	public Follow follow(Long fromMemberId, Long toMemberId) {
		Member fromMember = Member.builder().id(fromMemberId).build();
		Member toMember = Member.builder().id(toMemberId).build();

		if (followRepository.existsByFromMemberAndToMember(fromMember, toMember)) {
			throw new BlogApiException(ErrorCode.FOLLOW_ALREADY_EXISTS);
		}

		Follow follow = Follow.builder()
				.fromMember(fromMember)
				.toMember(toMember)
				.build();

		return followRepository.save(follow);
	}
}
