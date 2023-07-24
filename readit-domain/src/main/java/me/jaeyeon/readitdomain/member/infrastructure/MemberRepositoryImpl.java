package me.jaeyeon.readitdomain.member.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.service.port.MemberRepository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

	private final MemberJpaRepository memberJpaRepository;

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberJpaRepository.findByEmail(email).map(MemberEntity::toModel);
	}

	@Override
	public boolean existsByEmail(String email) {
		return memberJpaRepository.existsByEmail(email);
	}

	@Override
	public Member save(Member member) {
		MemberEntity savedEntity = memberJpaRepository.save(member.toEntity());
		return savedEntity.toModel();
	}

	@Override
	public Optional<Member> findById(Long id) {
		return memberJpaRepository.findById(id).map(MemberEntity::toModel);
	}
}
