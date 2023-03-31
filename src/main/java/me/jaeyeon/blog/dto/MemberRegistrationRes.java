package me.jaeyeon.blog.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.jaeyeon.blog.model.Member;

@Data
@NoArgsConstructor
public class MemberRegistrationRes {

	private Long id;
	private String userName;
	private String email;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public MemberRegistrationRes(Member member) {
		this.id = member.getId();
		this.userName = member.getUserName();
		this.email = member.getEmail();
		this.createdDate = member.getCreatedDate();
		this.modifiedDate = member.getModifiedDate();
	}
}
