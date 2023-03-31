package me.jaeyeon.blog.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		memberService = new MemberService(memberRepository, passwordEncoder);
	}

	@Test
	@DisplayName("회원 가입 테스트 - 성공")
	void registerMemberTest_Success() throws Exception {
	    // given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test@email.com");
		request.setPassword("P@ssw0rd!");

		Member expectedMember = request.toEntity(passwordEncoder);
		when(memberRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);

		// when
		Member savedMember = memberService.register(request);

		// then
		verify(memberRepository, times(1)).existsByEmail(request.getEmail());
		verify(memberRepository, times(1)).save(any(Member.class));
		assertThat(savedMember.getUserName()).isEqualTo(request.getUserName());
		assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
		assertThat(passwordEncoder.matches(request.getPassword(), savedMember.getPassword()));
	}

	@Test
	@DisplayName("회원 가입 테스트 - 실패 (이메일 중복)")
	void registerMemberTest_Fail() throws Exception {
	    // given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test@email.com");
		request.setPassword("P@ssw0rd!");

		when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);

	    // when
		Throwable throwable = catchThrowable(() -> memberService.register(request));

		// then
		verify(memberRepository, times(1)).existsByEmail(request.getEmail());
		assertThat(throwable)
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("이미 사용중인 이메일 주소입니다.");
	}
}
