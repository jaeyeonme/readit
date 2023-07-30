package me.jaeyeon.readitdomain.member.infrastructure;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.jaeyeon.readitdomain.config.BaseTimeEntity;
import me.jaeyeon.readitdomain.member.domain.Member;

@Entity
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "user_name", nullable = false, length = 50)
	private String userName;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MemberNicknameHistoryEntity> nicknameHistories = new ArrayList<>();

	@Builder
	public MemberEntity(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public Member toModel() {
		return Member.builder()
				.id(this.id)
				.userName(this.userName)
				.email(this.email)
				.build();
	}
}
