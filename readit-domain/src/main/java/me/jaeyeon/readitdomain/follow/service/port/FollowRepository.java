package me.jaeyeon.readitdomain.follow.service.port;

import java.util.List;

import me.jaeyeon.readitdomain.follow.domain.Follow;
import me.jaeyeon.readitdomain.member.domain.Member;

public interface FollowRepository {
	List<Follow> findAllByFromMember(Member fromMember);
	List<Follow> findAllByToMember(Member toMember);
	boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);
	Follow save(Follow follow);
}
