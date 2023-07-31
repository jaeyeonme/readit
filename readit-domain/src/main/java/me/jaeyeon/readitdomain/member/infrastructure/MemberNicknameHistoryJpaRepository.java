package me.jaeyeon.readitdomain.member.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNicknameHistoryJpaRepository extends JpaRepository<MemberNicknameHistoryEntity, Long> {

	List<MemberNicknameHistoryEntity> findByMemberEntity_Id(Long memberId);
}
