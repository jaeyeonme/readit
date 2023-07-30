package me.jaeyeon.readitdomain.member.service.port;

import java.util.List;

import me.jaeyeon.readitdomain.member.domain.MemberNicknameHistory;

public interface MemberNicknameHistoryRepository {

	MemberNicknameHistory save(MemberNicknameHistory history);
	List<MemberNicknameHistory> findByMemberId(Long memberId);
}
