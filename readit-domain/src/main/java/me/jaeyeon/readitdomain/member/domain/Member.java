package me.jaeyeon.readitdomain.member.domain;


import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;

@Getter
public class Member {

	private final Long id;
	private final String userName;
	private final String email;
	private final String password;

	@Builder
	public Member(Long id, String userName, String email, String password) {
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public Member updateUserName(String userName) {
		return Member.builder()
				.id(this.id)
				.userName(userName)
				.email(this.email)
				.password(this.password)
				.build();
	}

	public MemberEntity toEntity() {
		return MemberEntity.builder()
				.userName(userName)
				.email(email)
				.password(password)
				.build();
	}
}
