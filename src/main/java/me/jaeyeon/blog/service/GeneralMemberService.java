package me.jaeyeon.blog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.EmailAlreadyExistsException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.repository.MemberRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GeneralMemberService implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(MemberRegistrationReq request) {
		validateDuplicate(request.getEmail());
		Member member = request.toEntity(passwordEncoder);
		Member savedMember = memberRepository.save(member);
		log.info("New member registered. Email: {}, ID: {}", savedMember.getEmail(), savedMember.getId());
		return savedMember;
	}

	@Override
	@Transactional(readOnly = true)
	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(() -> new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public Member getMember(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public void validateDuplicate(String email) {
		if (memberRepository.existsByEmail(email)) {
			log.warn("Failed to register. Email: {}, Reason: email already exists", email);
			throw new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}
	}

	@Override
	public void checkPassword(String password, Member member) {
		if (!passwordEncoder.matches(password, member.getPassword())) {
			log.warn("Failed to login. Email: {}, Reason: wrong password", member.getEmail());
			throw new BlogApiException(ErrorCode.WRONG_PASSWORD);
		}
	}
}
