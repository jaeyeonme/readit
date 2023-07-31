package me.jaeyeon.readitdomain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.EmailAlreadyExistsException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.common.exception.MemberNotFoundException;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.domain.MemberCreate;
import me.jaeyeon.readitdomain.member.domain.MemberNicknameHistory;
import me.jaeyeon.readitdomain.member.domain.MemberNicknameHistoryCreate;
import me.jaeyeon.readitdomain.member.service.port.MemberNicknameHistoryRepository;
import me.jaeyeon.readitdomain.member.service.port.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberWriteServiceImpl implements MemberWriteUseCase {

	private final MemberRepository memberRepository;
	private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(MemberCreate request) {
		if (memberRepository.existsByEmail(request.email())) {
			throw new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		return memberRepository.save(request.toEntity(passwordEncoder));
	}

	@Override
	public Member updateUserName(Long memberId, String userName) {
		// Find member first
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		// Create DTO for nickname history
		MemberNicknameHistoryCreate historyCreate = new MemberNicknameHistoryCreate(member.getUserName());

		// Create domain object from DTO
		MemberNicknameHistory history = historyCreate.toEntity(member.toEntity()).toModel();

		// Save the history
		memberNicknameHistoryRepository.save(history);

		// Update member's username
		return memberRepository.save(member.updateUserName(userName));
	}
}
