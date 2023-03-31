package me.jaeyeon.blog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public Member register(MemberRegistrationReq request) {
		validateDuplicate(request.getEmail());
		Member member = request.toEntity(passwordEncoder);
		return memberRepository.save(member);
	}

	private void validateDuplicate(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new IllegalStateException("이미 사용중인 이메일 주소입니다.");
		}
	}
}
