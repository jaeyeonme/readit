package me.jaeyeon.blog.service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.model.Member;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

	private final HttpSession httpSession;
	private final MemberService memberService;
	public static final String MEMBER_ID = "MEMBER_ID";

	@Override
	public void login(String email, String password) {
		Member member = memberService.findByEmail(email);
		memberService.checkPassword(password, member);
		httpSession.setAttribute(MEMBER_ID, member.getId());
	}

	@Override
	public void logout() {
		httpSession.removeAttribute(MEMBER_ID);
	}

	@Override
	public Member getLoginMember() {
		Long memberId = (Long) httpSession.getAttribute(MEMBER_ID);
		return memberService.getMember(memberId);
	}

	@Override
	public Long getLoginMemberId() {
		return (Long) httpSession.getAttribute(MEMBER_ID);
	}
}
