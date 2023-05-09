package me.jaeyeon.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.dto.MemberSignIn;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.service.LoginService;
import me.jaeyeon.blog.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;
	private final LoginService loginService;

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody @Valid MemberRegistrationReq request) {
		memberService.register(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<Void> signIn(@RequestBody @Valid MemberSignIn signIn) {
		Member member = memberService.signIn(signIn);
		loginService.login(member.getId());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
		loginService.logout();
		return ResponseEntity.ok().build();
	}
}
