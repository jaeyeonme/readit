package me.jaeyeon.readitdomain.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.common.exception.MemberNotFoundException;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.service.port.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberReadServiceImpl implements MemberReadUseCase {

	private final MemberRepository memberRepository;


	@Override
	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public Member findById(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
}
