package me.jaeyeon.readitdomain.post.infrastructure;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {

	Page<PostEntity> findAllByAuthor(MemberEntity author, Pageable pageable);

	@Query("SELECT PostCountPerDate(DATE(p.createdDate), COUNT(p.id)) " +
			"FROM PostEntity p " +
			"WHERE p.author.id = :memberId AND DATE(p.createdDate) BETWEEN :startDate AND :endDate " +
			"GROUP BY DATE(p.createdDate)")
	List<PostCountPerDate> countPostsByMemberAndDateRange(
			@Param("memberId") Long memberId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
}
