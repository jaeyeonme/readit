package me.jaeyeon.readitdomain.member.service;

import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.domain.MemberCreate;

public interface MemberUseCase {

	Member register(MemberCreate request);
	Member findByEmail(String email);
	Member findById(Long memberId);
	boolean existsByEmail(String email);
}
