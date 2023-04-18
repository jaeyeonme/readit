package me.jaeyeon.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeon.blog.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	Optional<Member> findByEmail(String email);
}
