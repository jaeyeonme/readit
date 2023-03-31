package me.jaeyeon.blog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import me.jaeyeon.blog.model.Member;

@Data
public class MemberRegistrationReq {

	@NotBlank(message = "유저 이름 입력은 필수입니다.")
	private String userName;

	@NotBlank(message = "이메일 입력은 필수입니다.")
	@Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "올바른 이메일 형식으로 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호확인 입력은 필수입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{5,}$",
		message = "비밀번호는 최소5자 이상, 문자1개, 숫자1개, 대문자 포함해서 특수문자 1개 포함입니다.)")
	private String password;

	public Member toEntity(PasswordEncoder passwordEncoder) {
		return Member.builder()
			.userName(userName)
			.email(email)
			.password(passwordEncoder.encode(password))
			.build();
	}
}
