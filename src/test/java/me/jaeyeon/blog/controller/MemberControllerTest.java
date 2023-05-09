package me.jaeyeon.blog.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.jaeyeon.blog.config.WebConfig;
import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.dto.MemberSignIn;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.EmailAlreadyExistsException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.service.LoginService;
import me.jaeyeon.blog.service.MemberService;

@Import(WebConfig.class)
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MemberService memberService;

	@MockBean
	private LoginService loginService;

	private Member testMember;

	@BeforeEach
	void setUp() {
		testMember = Member.builder()
				.userName("test")
				.email("test@email.com")
				.password("P@ssw0rd!")
				.build();
		testMember.setId(1L);
	}

	@Test
	@DisplayName("회원 가입 API 테스트 - 성공")
	void registerMemberTest() throws Exception {
		// given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test@email.com");
		request.setPassword("P@ssw0rd!");

		given(memberService.register(request)).willReturn(null);

		// when
		mockMvc.perform(post("/members/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("회원 가입 API 테스트 - 실패 (이메일 중복)")
	void registerMemberTest_Fail() throws Exception {
		// given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test@email.com");
		request.setPassword("P@ssw0rd!");

		given(memberService.register(request)).willThrow(
				new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS));

		// when
		mockMvc.perform(post("/members/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage", containsString(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage())));
	}

	@Test
	@DisplayName("로그인 성공")
	void signInSuccess() throws Exception {
		// given
		MemberSignIn signIn = new MemberSignIn("test@email.com", "P@ssw0rd!");

		given(memberService.signIn(signIn)).willReturn(testMember);
		doNothing().when(loginService).login(testMember.getId());

		// when
		mockMvc.perform(post("/members/sign-in")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(signIn)))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("로그인 실패")
	void signInFailure() throws Exception {
		// given
		MemberSignIn signIn = new MemberSignIn("nonexistent@email.com", "wrong_password");
		given(memberService.signIn(signIn)).willThrow(new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));

		// when
		mockMvc.perform(post("/members/sign-in")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(signIn)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorCode", is("M-002")))
				.andExpect(jsonPath("$.errorMessage", is("회원을 찾을 수 없습니다.")));
	}
}
