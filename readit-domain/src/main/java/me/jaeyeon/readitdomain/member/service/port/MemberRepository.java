package me.jaeyeon.readitdomain.member.service.port;

import java.util.Optional;

import me.jaeyeon.readitdomain.member.domain.Member;

public interface MemberRepository {

	Optional<Member> findByEmail(String email);
	boolean existsByEmail(String email);
	Member save(Member member);
	Optional<Member> findById(Long id);
}
