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
import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.resolver.TestMemberArgumentResolver;
import me.jaeyeon.blog.service.CommentService;
import me.jaeyeon.blog.service.LoginService;
import me.jaeyeon.blog.service.SessionLoginService;

@Import(TestMvcConfig.class)
@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CommentService commentService;

	@MockBean
	private LoginService loginService;

	private MockHttpSession mockSession;
	private Long postId;
	private Long commentId;
	private Long memberId;
	private CommentReq commentReq;
	private Member testMember;
	private Comment testComment;
	private CommentRes commentRes;

	@BeforeEach
	public void setUp() {
		mockSession = new MockHttpSession();
		mockSession.setAttribute(SessionLoginService.MEMBER_ID, 1L);
		postId = 1L;
		commentId = 1L;
		memberId = 1L;

		// Initialize testMember
		testMember = Member.builder()
				.userName("Test User")
				.email("test@example.com")
				.password("password")
				.build();
		testMember.setId(memberId);
		
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
	void createCommentTest_Success() throws Exception {
		// given
		commentReq = new CommentReq("Test Comment Content");

		// when
		mockMvc.perform(post("/api/posts/{postId}/comments", postId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(commentReq))
						.session(mockSession))
				.andDo(print())
				.andExpect(status().isCreated());

		// then
		verify(commentService, times(1)).createComment(commentReq, postId, testMember);
	}

	@Test
	@DisplayName("대댓글 생성 테스트 - 성공")
	void createReplyCommentTest_Success() throws Exception {
		// given
		Long parentCommentId = 1L;
		CommentReq replyCommentReq = new CommentReq("Test Reply Comment");

		// when
		mockMvc.perform(post("/api/posts/{postId}/comments/{commentId}", postId, parentCommentId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(replyCommentReq))
						.session(mockSession))
				.andDo(print())
				.andExpect(status().isCreated());

		// then
		verify(commentService, times(1)).saveReplyComment(parentCommentId, replyCommentReq, postId, testMember);
	}

	@Test
	@DisplayName("댓글 목록 조회 테스트 - 성공")
	void getAllCommentTest_Success() throws Exception {
		// given
		int page = 0;
		int size = 10;
		Sort.Direction direction = Sort.Direction.DESC;
		String sort = "createdDate";
		PageRequest pageable = PageRequest.of(page, size, direction, sort);

		List<CommentRes> commentResponses = new ArrayList<>();
		commentResponses.add(new CommentRes(Comment.builder().content("Test Comment 1").build()));
		commentResponses.add(new CommentRes(Comment.builder().content("Test Comment 2").build()));

		Page<CommentRes> commentResponsePage = new PageImpl<>(commentResponses, pageable, commentResponses.size());

		given(commentService.getCommentsByPostId(postId, pageable)).willReturn(commentResponsePage);

		// when
		mockMvc.perform(get("/api/posts/{postId}/comments", postId)
						.param("page", String.valueOf(page))
						.param("size", String.valueOf(size))
						.param("sort", sort + "," + direction))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].content", is("Test Comment 1")))
				.andExpect(jsonPath("$.content[1].content", is("Test Comment 2")));

		// then
		verify(commentService, times(1)).getCommentsByPostId(postId, pageable);
	}

	@Test
	@DisplayName("댓글 조회 테스트 - 성공")
	void getCommentByIdTest_Success() throws Exception {
		// given
		commentReq = new CommentReq("Test Comment Content");
		commentRes = new CommentRes(Comment.builder().content("Test Comment Content").build());

		given(commentService.getCommentById(commentId)).willReturn(commentRes);

		// when
		mockMvc.perform(get("/api/posts/{postId}/comments/{commentId}", postId, commentId))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", is("Test Comment Content")));

		// then
		verify(commentService, times(1)).getCommentById(commentId);
	}

	@Test
	@DisplayName("댓글 수정 테스트 - 성공")
	void updateCommentTest_Success() throws Exception {
		// given
		CommentReq updatedCommentReq = new CommentReq("Updated Comment Content");

		// when
		mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}", postId, commentId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCommentReq))
						.session(mockSession))
				.andDo(print())
				.andExpect(status().isOk());

		// then
		verify(commentService, times(1)).updateComment(commentId, updatedCommentReq, testMember);
	}

	@Test
	@DisplayName("댓글 삭제 테스트 - 성공")
	void deleteCommentTest_Success() throws Exception {
		// when
		mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
						.session(mockSession))
				.andDo(print())
				.andExpect(status().isOk());

		// then
		verify(commentService, times(1)).deleteComment(commentId, testMember);
	}
}
