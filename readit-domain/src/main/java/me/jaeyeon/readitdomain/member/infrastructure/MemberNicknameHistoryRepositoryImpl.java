package me.jaeyeon.readitdomain.member.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.common.exception.MemberNotFoundException;
import me.jaeyeon.readitdomain.member.domain.MemberNicknameHistory;
import me.jaeyeon.readitdomain.member.service.port.MemberNicknameHistoryRepository;
import me.jaeyeon.readitdomain.member.service.port.MemberRepository;

@Repository
@RequiredArgsConstructor
public class MemberNicknameHistoryRepositoryImpl implements MemberNicknameHistoryRepository {

	private final MemberNicknameHistoryJpaRepository memberNicknameHistoryJpaRepository;
	private final MemberRepository memberRepository;

	@Override
	public MemberNicknameHistory save(MemberNicknameHistory history) {
		MemberEntity memberEntity = memberRepository.findById(history.getMemberId())
				.orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND))
				.toEntity();
		return memberNicknameHistoryJpaRepository.save(history.toEntity(memberEntity)).toModel();
	}

	@Override
	public List<MemberNicknameHistory> findByMemberId(Long memberId) {
		return memberNicknameHistoryJpaRepository.findByMemberEntity_Id(memberId).stream()
				.map(MemberNicknameHistoryEntity::toModel)
				.collect(Collectors.toList());
	}
}
