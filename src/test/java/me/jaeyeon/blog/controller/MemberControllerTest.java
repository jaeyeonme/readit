package me.jaeyeon.blog.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import me.jaeyeon.blog.dto.MemberRegistrationReq;
import me.jaeyeon.blog.dto.MemberRegistrationRes;
import me.jaeyeon.blog.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void clear() {
		memberRepository.deleteAll();
	}

	@Test
	@DisplayName("회원 가입 API 테스트 - 성공")
	void registerMemberTest() throws Exception {
		// given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test@email.com");
		request.setPassword("P@ssw0rd!");

		// When
		ResponseEntity<MemberRegistrationRes> response =
			restTemplate.postForEntity("/register", request, MemberRegistrationRes.class);

		// Then
		assertThat(response.getStatusCode() ).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getUserName()).isEqualTo(request.getUserName());
		assertThat(response.getBody().getEmail()).isEqualTo(request.getEmail());
	}

	@Test
	@DisplayName("회원 가입 API 테스트 - 실패 (이메일 형식이 잘못됨)")
	void registerMemberTest_Fail_1() throws Exception {
	    // given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test.123@inValid");
		request.setPassword("P@ssw0rd!");

	    // when
		ResponseEntity<MemberRegistrationRes> response =
			restTemplate.postForEntity("/register", request, MemberRegistrationRes.class);

	    // then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("회원 가입 API 테스트 - 실패 (비밀번호 형식이 잘못됨)")
	void registerMemberTest_Fail_2() throws Exception {
		// given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test@email.com");
		request.setPassword("password");

	    // when
		ResponseEntity<MemberRegistrationRes> response =
			restTemplate.postForEntity("/register", request, MemberRegistrationRes.class);

	    // then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("회원 가입 API 테스트 - 실패 (이메일 중복)")
	void registerMemberTest_Fail_3() throws Exception {
	    // given
		MemberRegistrationReq request = new MemberRegistrationReq();
		request.setUserName("test");
		request.setEmail("test@email.com");
		request.setPassword("P@ssw0rd!");

		restTemplate.postForEntity("/register", request, MemberRegistrationRes.class);

		// when
		ResponseEntity<String> response =
			restTemplate.postForEntity("/register", request, String.class);

	    // then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("이미 사용중인 이메일 주소입니다.");
	}
}
