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
		member.setId(1L);

		mockHttpSession = new MockHttpSession();

		loginService = new SessionLoginService(mockHttpSession, memberService);
	}

	@Test
	@DisplayName("로그인 테스트 - 성공")
	void loginTest_Success() {
		// when
		loginService.login(member.getId());

		// then
		assertThat(mockHttpSession.getAttribute(SessionLoginService.MEMBER_ID)).isEqualTo(member.getId());
	}

	@Test
	@DisplayName("로그아웃 테스트")
	void loginTest() {
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
		when(memberService.findById(member.getId())).thenReturn(member);

		// when
		Member resultMember = loginService.getLoginMember();

		// then
		assertThat(resultMember).isEqualTo(member);
	}

	@Test
	@DisplayName("로그인 멤버 가져오기 테스트 - 실패 (로그인 상태가 아님)")
	void getLoginMemberTest_Fail_NotLoggedIn() {
		// given
		when(memberService.findById(null)).thenReturn(null);

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
