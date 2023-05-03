package me.jaeyeon.blog.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.EmailAlreadyExistsException;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class GeneralMemberServiceTest {

	@InjectMocks
	private GeneralMemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private MemberRegistrationReq request;
	private Member member;

	@BeforeEach
	void setUp() {
		request = new MemberRegistrationReq("test", "test@email.com", "P@ssw0rd!");
		member = Member.builder()
			.userName("test")
			.email("test@email.com")
			.password("P@ssw0rd!")
			.build();
	}

	@Test
	@DisplayName("회원 가입 테스트 - 성공")
	void registerTest_Success() {
		// given
		when(memberRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(memberRepository.save(any(Member.class))).thenReturn(member);

		// when
		Member registeredMember = memberService.register(request);

		// then
		Assertions.assertThat(registeredMember).isEqualTo(member);
		verify(memberRepository, times(1)).existsByEmail(request.getEmail());
		verify(memberRepository, times(1)).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 가입 테스트 - 실패 (이메일 중복)")
	void registerTest_Fail_EmailAlreadyExists() {
		// given
		when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);

		// when
		Throwable throwable = catchThrowable(() -> memberService.register(request));

		// then
		assertThat(throwable)
			.isInstanceOf(EmailAlreadyExistsException.class)
			.hasMessage("이미 사용중인 이메일입니다.");
	}

	@Test
	@DisplayName("이메일로 회원 찾기 테스트 - 성공")
	void findByEmailTest_Success() {
		// given
		when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

		// when
		Member foundMember = memberService.findByEmail(member.getEmail());

		// then
		assertThat(foundMember).isEqualTo(member);
	}

	@Test
	@DisplayName("이메일로 회원 찾기 테스트 - 실패 (회원 없음)")
	void findByEmailTest_Fail_MemberNotFound() {
		// given
		when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());

		// when
		Throwable throwable = catchThrowable(() -> memberService.findByEmail(member.getEmail()));

		// then
		assertThat(throwable)
			.isInstanceOf(BlogApiException.class)
			.hasMessage("회원을 찾을 수 없습니다.");
	}
}
