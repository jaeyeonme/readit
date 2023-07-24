package me.jaeyeon.readitdomain.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.common.exception.MemberNotFoundException;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.service.port.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationUseCase {


	private final SessionManager sessionManager;
	private final MemberRepository memberRepository;
	private static final String MEMBER_ID = "MEMBER_ID";

	@Override
	public void login(long id) {
		sessionManager.setAttribute(MEMBER_ID, id);
	}

	@Override
	public void logout() {
		sessionManager.removeAttribute(MEMBER_ID);
	}

	@Override
	public Member getLoginMember() {
		Long memberId = (Long) sessionManager.getAttribute(MEMBER_ID);
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public Long getLoginMemberId() {
		return (Long) sessionManager.getAttribute(MEMBER_ID);
	}
}
