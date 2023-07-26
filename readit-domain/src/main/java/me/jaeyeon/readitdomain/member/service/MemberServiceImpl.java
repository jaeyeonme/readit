package me.jaeyeon.readitdomain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.EmailAlreadyExistsException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.domain.MemberCreate;
import me.jaeyeon.readitdomain.member.service.port.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberUseCase {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(MemberCreate memberCreate) {
		if (memberRepository.existsByEmail(memberCreate.getEmail())) {
			throw new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}
		return memberRepository.save(memberCreate.toEntity(passwordEncoder));
	}

	@Override
	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new EmailAlreadyExistsException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public Member findById(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EmailAlreadyExistsException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
}
