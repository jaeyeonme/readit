package me.jaeyeon.blog.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.jaeyeon.blog.config.SessionConst;
import me.jaeyeon.blog.config.TestMvcConfig;
import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.resolver.TestMemberArgumentResolver;
import me.jaeyeon.blog.service.BlogCommentService;
import me.jaeyeon.blog.service.LoginService;
import me.jaeyeon.blog.testHelper.TestHelper;

@Import(TestMvcConfig.class)
@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BlogCommentService commentService;

	@MockBean
	private LoginService loginService;
	private MockHttpSession mockSession;
	private Long postID;
	private Long memberID;
	private Long commentID;
	private Member member;

	@BeforeEach
	public void setUp() {
		mockSession = new MockHttpSession();
		mockSession.setAttribute(SessionConst.LOGIN_MEMBER, 1L);
		commentID = 1L;
		postID = 1L;
		memberID = 1L;

		// 멤버 초기화
		member = TestHelper.createTestMember(memberID, "Test User", "test@example.com", "password");

		createCommentController();
	}


	private void createCommentController() {
		TestMemberArgumentResolver testMemberArgumentResolver = new TestMemberArgumentResolver();
		PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();

		mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService))
			.setCustomArgumentResolvers(testMemberArgumentResolver, pageableResolver)
			.build();
	}

	@Test
	@DisplayName("댓글 생성 테스트 - 성공")
	void createCommentTest() throws Exception {
		// given
		CommentReq commentReq = new CommentReq("Test Comment");

		// when
		mockMvc.perform(post("/api/posts/{postID}/comments", postID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(commentReq))
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isCreated());

		// then
		verify(commentService, times(1)).createComment(eq(commentReq), eq(postID), any(Member.class));
	}

	@Test
	@DisplayName("대댓글 생성 테스트 - 성공")
	void createReplyCommentTest() throws Exception {
		// given
		CommentReq replyCommentReq = new CommentReq("Test Reply Comment");

		// when
		mockMvc.perform(post("/api/posts/{postID}/comments/{commentID}", postID, commentID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(replyCommentReq))
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isCreated());

		// then
		verify(commentService, times(1)).saveReplyComment(eq(commentID), eq(replyCommentReq), any(Member.class), eq(postID));
	}

	@Test
	@DisplayName("댓글 목록 조회 테스트 - 성공")
	void getAllCommentsTest() throws Exception {
		// given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");

		// Comment 객체 생성
		Comment comment = Comment.builder()
			.content("Test Comment")
			.build();

		// CommentRes 객체 생성
		CommentRes commentRes = new CommentRes(comment);

		Page<CommentRes> commentPage = new PageImpl<>(
			Collections.singletonList(commentRes), pageable, 1);

		when(commentService.getCommentsByPostId(postID, pageable)).thenReturn(commentPage);

		// when
		mockMvc.perform(get("/api/posts/{postId}/comments", postID)
				.contentType(MediaType.APPLICATION_JSON)
				.session(mockSession)
				.param("page", "0")
				.param("size", "10")
				.param("sort", "createdDate,desc"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(commentRes.getId()))
			.andExpect(jsonPath("$.content[0].content").value(commentRes.getContent()));

		// then
		verify(commentService, times(1)).getCommentsByPostId(postID, pageable);
	}

	@Test
	@DisplayName("특정 댓글 조회 테스트 - 성공")
	void getCommentByIdTest() throws Exception {
	    // given
		Comment comment = Comment.builder()
			.content("Test Comment")
			.build();

		CommentRes commentRes = new CommentRes(comment);
		when(commentService.getCommentById(commentID)).thenReturn(commentRes);

	    // when
		mockMvc.perform(get("/api/posts/{postId}/comments/{commentId}", postID, commentID)
				.contentType(MediaType.APPLICATION_JSON)
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(commentRes.getId()))
			.andExpect(jsonPath("$.content").value(commentRes.getContent()));

	    // then
		verify(commentService, times(1)).getCommentById(commentID);
	}

	@Test
	@DisplayName("댓글 수정 테스트 - 성공")
	void updateCommentTest() throws Exception {
		// given
		CommentReq commentReq = new CommentReq("Test Comment");

		// when
		mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}", postID, commentID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(commentReq))
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk());

		// then
		verify(commentService, times(1)).updateComment(commentID, commentReq, member);
	}

	@Test
	@DisplayName("댓글 삭제 테스트 - 성공")
	void deleteCommentTest() throws Exception {
		// when
		mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", postID, commentID)
				.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk());

		// then
		verify(commentService, times(1)).deleteComment(commentID, member);
	}
}
