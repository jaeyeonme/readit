package me.jaeyeon.blog.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.jaeyeon.blog.config.TestMvcConfig;
import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.resolver.TestMemberArgumentResolver;
import me.jaeyeon.blog.service.BlogPostService;
import me.jaeyeon.blog.service.LoginService;
import me.jaeyeon.blog.service.SessionLoginService;
import me.jaeyeon.blog.testHelper.TestHelper;

@Import(TestMvcConfig.class)
@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BlogPostService postService;

	@MockBean
	private LoginService loginService;

	private MockHttpSession mockSession;
	private Long postID;
	private Long memberID;
	private PostReq postReq;
	private Member testMember;
	private Post testPost;
	private PostResponse postResponse;

	@BeforeEach
	public void setUp() {
		mockSession = new MockHttpSession();
		mockSession.setAttribute(SessionLoginService.MEMBER_ID, 1L);
		postID = 1L;
		memberID = 1L;

		createPostController();
	}

	private void createPostController() {
		TestMemberArgumentResolver testMemberArgumentResolver = new TestMemberArgumentResolver();
		PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();

		mockMvc = MockMvcBuilders.standaloneSetup(new PostController(postService))
			.setCustomArgumentResolvers(testMemberArgumentResolver, pageableResolver)
			.build();
	}

	@Test
	@DisplayName("게시물 생성 테스트 - 성공")
	void createPostTest_Success() throws Exception {
		// given
		postReq = new PostReq("Test Title", "Test Content");

		// when
		mockMvc.perform(post("/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postReq))
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("게시물 조회 테스트 - 성공 (단일 게시글)")
	void getPostByIdTest_Success() throws Exception {
		// given
		postReq = new PostReq("Test Title 1", "Test Content 1");
		postResponse = new PostResponse();
		postResponse.setId(postID);
		postResponse.setTitle(postReq.getTitle());
		postResponse.setContent(postReq.getContent());

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
	@DisplayName("게시물 키워드 검색 테스트 - 성공")
	void getPostsWithKeywordTest_Success() throws Exception {
		// given
		String keyword = "Test";
		int page = 0;
		int size = 10;
		Sort.Direction direction = Sort.Direction.DESC;
		String sort = "createdDate";
		PageRequest pageable = PageRequest.of(page, size, direction, sort);

		List<PostResponse> postResponses = new ArrayList<>();
		postResponses.add(new PostResponse(Post.builder().title("Test Title 1").content("Test Content 1").build()));
		postResponses.add(new PostResponse(Post.builder().title("Test Title 2").content("Test Content 2").build()));

		Page<PostResponse> postResponsePage = new PageImpl<>(postResponses, pageable, postResponses.size());

		given(postService.searchPostsWithKeyword(keyword, pageable)).willReturn(postResponsePage);

		// when
		mockMvc.perform(get("/api/posts")
				.param("keyword", keyword)
				.param("page", String.valueOf(page))
				.param("size", String.valueOf(size))
				.param("sort", sort + "," + direction))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(2)))
			.andExpect(jsonPath("$.content[0].title", is("Test Title 1")))
			.andExpect(jsonPath("$.content[1].title", is("Test Title 2")));

		// then
		verify(postService, times(1)).searchPostsWithKeyword(keyword, pageable);

	}

	@Test
	@DisplayName("게시물 수정 테스트 - 성공")
	void updatePostTest_Success() throws Exception {
		// given
		PostReq updatedPostReq = new PostReq("Updated Title", "Updated Content");
		Member testMember =
			TestHelper.createTestMember(memberID, "Test User", "test@example.com", "password");

		// when
		mockMvc.perform(put("/api/posts/{id}", postID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPostReq))
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk());

		// then
		verify(postService, times(1)).updatePost(updatedPostReq, postID, testMember);
	}

	@Test
	@DisplayName("게시물 삭제 테스트 - 성공")
	void deletePostTest_Success() throws Exception {
		Member testMember =
			TestHelper.createTestMember(memberID, "Test User", "test@example.com", "password");

		// when
		mockMvc.perform(delete("/api/posts/{id}", postID)
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk());

		// then
		verify(postService, times(1)).deletePostById(postID, testMember);
	}
}
