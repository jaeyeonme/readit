package me.jaeyeon.readitdomain.follow.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaeyeon.readitdomain.config.BaseTimeEntity;
import me.jaeyeon.readitdomain.follow.domain.Follow;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowEntity extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_member_id")
	private MemberEntity fromMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_member_id")
	private MemberEntity toMember;

	@Builder
	public FollowEntity(MemberEntity fromMember, MemberEntity toMember) {
		this.fromMember = fromMember;
		this.toMember = toMember;
	}

	public Follow toModel() {
		return Follow.builder()
				.id(this.id)
				.fromMember(this.fromMember.toModel())
				.toMember(this.toMember.toModel())
				.build();
	}
}
