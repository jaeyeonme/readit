package me.jaeyeon.blog.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Member;

@ExtendWith(MockitoExtension.class)
class SessionLoginServiceTest {

	private SessionLoginService loginService;

	@Mock
	private GeneralMemberService memberService;

	private MockHttpSession mockHttpSession;

	private Member member;

	@BeforeEach
	void setUp() {

		member = Member.builder()
			.userName("test")
			.email("test@email.com")
			.password("P@ssw0rd!")
			.build();

		mockHttpSession = new MockHttpSession();

		loginService = new SessionLoginService(mockHttpSession, memberService);
	}


	@Test
	@DisplayName("로그인 테스트 - 성공")
	void loginTest_Success() {
		// given
		String email = "test@email.com";
		String password = "P@ssw0rd!";

		when(memberService.findByEmail(email)).thenReturn(member);
		doNothing().when(memberService).checkPassword(password, member);

		// when
		loginService.login(email, password);

		// then
		verify(memberService, times(1)).findByEmail(email);
		verify(memberService, times(1)).checkPassword(password, member);
		assertThat(mockHttpSession.getAttribute(SessionLoginService.MEMBER_ID)).isEqualTo(member.getId());
	}

	@Test
	@DisplayName("로그인 테스트 - 실패 (이메일 불일치)")
	void loginTest_Fail_EmailMismatch() {
		// given
		String email = "wrong@email.com";
		String password = "P@ssw0rd!";

		when(memberService.findByEmail(email)).thenThrow(new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));

		// when
		Throwable throwable = catchThrowable(() -> loginService.login(email, password));

		// then
		assertThat(throwable)
			.isInstanceOf(BlogApiException.class)
			.hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("로그아웃 테스트")
	void logoutTest() {
		// given
		mockHttpSession.setAttribute(SessionLoginService.MEMBER_ID, member.getId());

		// when
		loginService.logout();

		// then
		assertThat(mockHttpSession.getAttribute(SessionLoginService.MEMBER_ID)).isNull();
	}

	@Test
	@DisplayName("로그인 멤버 가져오기 테스트 - 성공")
	void getLoginMemberTest_Success() {
		// given
		mockHttpSession.setAttribute(SessionLoginService.MEMBER_ID, member.getId());
		when(memberService.getMember(member.getId())).thenReturn(member);

		// when
		Member resultMember = loginService.getLoginMember();

		// then
		assertThat(resultMember).isEqualTo(member);
	}

	@Test
	@DisplayName("로그인 멤버 가져오기 테스트 - 실패 (로그인 상태가 아님)")
	void getLoginMemberTest_Fail_NotLoggedIn() {
		// given
		when(memberService.getMember(null)).thenReturn(null);

		// when
		Member resultMember = loginService.getLoginMember();

		// then
		assertThat(resultMember).isNull();
	}

	@Test
	@DisplayName("로그인 멤버 ID 가져오기 테스트")
	void getLoginMemberIdTest() {
		// given
		mockHttpSession.setAttribute(SessionLoginService.MEMBER_ID, member.getId());

		// when
		Long memberId = loginService.getLoginMemberId();

		// then
		assertThat(memberId).isEqualTo(member.getId());
	}

}
