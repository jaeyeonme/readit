package me.jaeyeon.readitdomain.follow.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;

public interface FollowJpaRepository extends JpaRepository<FollowEntity, Long> {

	List<FollowEntity> findAllByFromMember(MemberEntity fromMember);
	List<FollowEntity> findAllByToMember(MemberEntity toMember);
	boolean existsByFromMemberAndToMember(MemberEntity fromMember, MemberEntity toMember);
}
