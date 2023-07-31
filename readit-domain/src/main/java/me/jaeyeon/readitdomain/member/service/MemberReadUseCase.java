package me.jaeyeon.readitdomain.member.service;

import me.jaeyeon.readitdomain.member.domain.Member;

public interface MemberReadUseCase {
	Member findByEmail(String email);
	Member findById(Long memberId);
	boolean existsByEmail(String email);
}
