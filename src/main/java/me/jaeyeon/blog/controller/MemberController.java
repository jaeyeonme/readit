package me.jaeyeon.blog.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.dto.MemberRegistrationRes;
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
}
