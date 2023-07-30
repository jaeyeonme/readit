package me.jaeyeon.readitdomain.member.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;
import me.jaeyeon.readitdomain.member.infrastructure.MemberNicknameHistoryEntity;

@Getter
public class MemberNicknameHistory {

	private Long id;
	private Long memberId;
	private String userName;
	private LocalDateTime createdDate;

	@Builder
	public MemberNicknameHistory(Long id, Long memberId, String userName, LocalDateTime createdDate) {
		this.id = id;
		this.memberId = memberId;
		this.userName = userName;
		this.createdDate = createdDate;
	}

	public MemberNicknameHistoryEntity toEntity(MemberEntity memberEntity) {
		return MemberNicknameHistoryEntity.builder()
				.memberEntity(memberEntity)
				.userName(this.userName)
				.build();
	}
}
