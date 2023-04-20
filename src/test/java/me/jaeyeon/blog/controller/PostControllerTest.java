package me.jaeyeon.blog.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.jaeyeon.blog.config.SessionConst;
import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.service.PostService;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	private MockHttpSession mockSession;
	private Long postID;
	private Long memberID;

	@BeforeEach
	public void setUp() {
		mockSession = new MockHttpSession();
		mockSession.setAttribute(SessionConst.LOGIN_MEMBER, 1L);
		postID = 1L;
		memberID = 1L;
	}

	@Test
	@DisplayName("게시물 생성 테스트 - 성공")
	void createPostTest_Success() throws Exception {
		// given
		PostReq postReq = new PostReq("Test Title", "Test Content");

		// when
		mockMvc.perform(post("/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postReq))
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("게시물 조회 테스트 - 성공")
	void getAllPostsTest_Success() throws Exception {
		// given
		Member author = TestHelper.createTestMember(1L, "Test User", "test@example.com", "password");
		Post post1 = Post.builder().title("Test Title 1").content("Test Content 1").author(author).build();
		Post post2 = Post.builder().title("Test Title 2").content("Test Content 2").author(author).build();
		PostResponse postResponse1 = new PostResponse(post1);
		PostResponse postResponse2 = new PostResponse(post2);
		List<PostResponse> postResponses = Arrays.asList(postResponse1, postResponse2);

		given(postService.getAllPosts()).willReturn(postResponses);

		// when
		mockMvc.perform(get("/api/posts"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].title", is("Test Title 1")))
			.andExpect(jsonPath("$[0].content", is("Test Content 1")))
			.andExpect(jsonPath("$[1].title", is("Test Title 2")))
			.andExpect(jsonPath("$[1].content", is("Test Content 2")));

		// then
		verify(postService, times(1)).getAllPosts();
	}

	@Test
	@DisplayName("게시물 조회 테스트 - 성공 (딴일 게시글)")
	void getPostByIdTest_Success() throws Exception {
	    // given
		Member author = TestHelper.createTestMember(1L, "Test User", "test@example.com", "password");
		Post post = Post.builder().title("Test Title 1").content("Test Content 1").author(author).build();
		PostResponse postResponse = new PostResponse(post);

		given(postService.getPostById(postID)).willReturn(postResponse);

	    // when
		mockMvc.perform(get("/api/posts/{id}", postID))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title", is("Test Title 1")))
			.andExpect(jsonPath("$.content", is("Test Content 1")));

	    // then
		verify(postService, times(1)).getPostById(postID);
	}

	@Test
	@DisplayName("게시물 수정 테스트 - 성공")
	void updatePostTest_Success() throws Exception {
		// given
		PostReq postReq = new PostReq("Updated Title", "Updated Content");

		// when
		mockMvc.perform(put("/api/posts/{id}", postID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postReq))
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk());

		// then
		verify(postService, times(1)).checkAuthor(memberID, postID);
		verify(postService, times(1)).updatePost(postReq, postID);

	}

	@Test
	@DisplayName("게시물 삭제 테스트 - 성공")
	void deletePostTest_Success() throws Exception {
	    // when
		mockMvc.perform(delete("/api/posts/{id}", postID)
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk());

	    // then
		verify(postService, times(1)).checkAuthor(memberID, postID);
		verify(postService, times(1)).deletePostById(postID);
	}

	static class TestHelper {
		public static Member createTestMember(Long id, String userName, String email, String password) {
			Member member = new Member(userName, email, password);
			member.setId(id);
			return member;
		}
	}
}





