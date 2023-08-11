package me.jaeyeon.readitdomain.timeline.infrastructure;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;

public interface TimelineJpaRepository extends JpaRepository<TimelineEntity, Long> {

	List<TimelineEntity> findByMemberOrderByCreatedDateDesc(MemberEntity member, Pageable pageable);
	List<TimelineEntity> findByIdLessThanAndMemberOrderByCreatedDateDesc(Long id, MemberEntity member, Pageable pageable);
}
