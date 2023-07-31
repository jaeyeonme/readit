package me.jaeyeon.readitdomain.member.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;
import me.jaeyeon.readitdomain.member.infrastructure.MemberNicknameHistoryEntity;

public record MemberNicknameHistoryCreate(

		@NotBlank(message = "유저 이름은 필수입니다.")
		@Size(max = 10, message = "최대 길이를 초과했습니다.")
		String userName
) {

	public MemberNicknameHistoryEntity toEntity(MemberEntity memberEntity) {
		return MemberNicknameHistoryEntity.builder()
				.memberEntity(memberEntity)
				.userName(this.userName)
				.build();
	}
}
