package me.jaeyeon.blog.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignIn {

	@NotBlank(message = "이메일 입력은 필수입니다.")
	private String email;

	@NotBlank(message = "비밀번호확인 입력은 필수입니다.")
	private String password;
}
