package me.jaeyeon.readitapi.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitdomain.member.domain.LoginRequest;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.domain.MemberCreate;
import me.jaeyeon.readitdomain.member.service.AuthenticationUseCase;
import me.jaeyeon.readitdomain.member.service.MemberUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberUseCase memberUseCase;
	private final AuthenticationUseCase authenticationUseCase;

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody @Valid MemberCreate request) {
		memberUseCase.register(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<Void> signIn(@RequestBody @Valid LoginRequest signIn) {
		Member member = memberUseCase.findByEmail(signIn.getEmail());
		authenticationUseCase.login(member.getId());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
		authenticationUseCase.logout();
		return ResponseEntity.ok().build();
	}
}
