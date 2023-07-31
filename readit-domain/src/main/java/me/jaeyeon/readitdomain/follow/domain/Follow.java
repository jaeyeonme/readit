package me.jaeyeon.readitdomain.follow.domain;

import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitdomain.follow.infrastructure.FollowEntity;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;

@Getter
public class Follow {

	private final Long id;
	private final Member fromMember;
	private final Member toMember;

	@Builder
	public Follow(Long id, Member fromMember, Member toMember) {
		this.id = id;
		this.fromMember = fromMember;
		this.toMember = toMember;
	}

	public FollowEntity toEntity() {
		MemberEntity fromMemberEntity = fromMember.toEntity();
		MemberEntity toMemberEntity = toMember.toEntity();

		return FollowEntity.builder()
				.fromMember(fromMemberEntity)
				.toMember(toMemberEntity)
				.build();
	}
}
