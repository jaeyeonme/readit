package me.jaeyeon.readitdomain.member.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreate {

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
