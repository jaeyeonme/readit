package me.jaeyeon.readitdomain.member.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaeyeon.readitdomain.config.BaseTimeEntity;
import me.jaeyeon.readitdomain.member.domain.MemberNicknameHistory;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberNicknameHistoryEntity extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private MemberEntity member;
	private String userName;

	@Builder
	public MemberNicknameHistoryEntity(MemberEntity memberEntity, String userName) {
		this.member = memberEntity;
		this.userName = userName;
	}

	public MemberNicknameHistory toModel() {
		return MemberNicknameHistory.builder()
				.id(this.id)
				.memberId(this.member.getId())
				.userName(this.userName)
				.createdDate(this.getCreatedDate())
				.build();
	}
}
