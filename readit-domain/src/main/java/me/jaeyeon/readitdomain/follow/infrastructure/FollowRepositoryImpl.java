package me.jaeyeon.readitdomain.follow.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitdomain.follow.domain.Follow;
import me.jaeyeon.readitdomain.follow.service.port.FollowRepository;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {

	private final FollowJpaRepository followJpaRepository;

	@Override
	public List<Follow> findAllByFromMember(Member fromMember) {
		MemberEntity fromMemberEntity = fromMember.toEntity();
		return followJpaRepository.findAllByFromMember(fromMemberEntity).stream()
				.map(FollowEntity::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public List<Follow> findAllByToMember(Member toMember) {
		MemberEntity toMemberEntity = toMember.toEntity();
		return followJpaRepository.findAllByToMember(toMemberEntity).stream()
				.map(FollowEntity::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public boolean existsByFromMemberAndToMember(Member fromMember, Member toMember) {
		MemberEntity fromMemberEntity = fromMember.toEntity();
		MemberEntity toMemberEntity = toMember.toEntity();
		return followJpaRepository.existsByFromMemberAndToMember(fromMemberEntity, toMemberEntity);
	}

	@Override
	public Follow save(Follow follow) {
		FollowEntity savedEntity = followJpaRepository.save(follow.toEntity());
		return savedEntity.toModel();
	}
}
