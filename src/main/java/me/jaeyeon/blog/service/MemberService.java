package me.jaeyeon.blog.service;

import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.model.Member;

public interface MemberService {
	Member register(MemberRegistrationReq request);

	Member findByEmail(String email);

	Member findById(Long memberId);

	Member signIn(String email, String password);

	void validateDuplicate(String email);

	void checkPassword(String password, Member member);
}
