package me.jaeyeon.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.config.SessionConst;
import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.dto.MemberRegistrationRes;
import me.jaeyeon.blog.dto.MemberSignIn;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody @Valid MemberRegistrationReq request) {
		memberService.register(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<Void> signIn(@RequestBody @Valid MemberSignIn signIn, HttpServletRequest request) {
		Member loginMember = memberService.signIn(signIn);
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.getEmail());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return ResponseEntity.ok().build();
	}
}
