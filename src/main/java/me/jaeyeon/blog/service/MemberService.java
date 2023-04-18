package me.jaeyeon.blog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.dto.MemberSignIn;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.EmailAlreadyExistsException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.repository.MemberRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public Member register(MemberRegistrationReq request) {
		validateDuplicate(request.getEmail());
		Member member = request.toEntity(passwordEncoder);
		Member savedMember = memberRepository.save(member);
		log.info("New member registered. Email: {}, ID: {}", savedMember.getEmail(), savedMember.getId());
		return savedMember;
	}

	@Transactional(readOnly = true)
	public Member signIn(MemberSignIn signIn) {
		String email = signIn.getEmail();
		String password = signIn.getPassword();

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));

		checkPassword(password, member);
		log.info("Member logged in. Email: {}, ID: {}", member.getEmail(), member.getId());
		return member;
	}

	private void checkPassword(String password, Member member) {
		if (!passwordEncoder.matches(password, member.getPassword())) {
			log.warn("Failed to login. Email: {}, Reason: wrong password", member.getEmail());
			throw new BlogApiException(ErrorCode.WRONG_PASSWORD);
		}
	}


	private void validateDuplicate(String email) {
		if (memberRepository.existsByEmail(email)) {
			log.warn("Failed to register. Email: {}, Reason: email already exists", email);
			throw new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}
	}
}
