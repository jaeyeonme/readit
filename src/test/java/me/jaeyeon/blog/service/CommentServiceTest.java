package me.jaeyeon.blog.service;

import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	private CommentService commentService;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private PostService postService;

	@Mock
	private MemberService memberService;

	private Member testMember;
	private Post testPost;
	private Comment testComment;
	private Comment testReplyComment;

	@BeforeEach
	void setUp() {
		testMember = Member.builder()
			.userName("testUser")
			.email("test@email.com")
			.password("P@ssw0rd!")
			.build();
		testMember.setId(1L);

		testPost = Post.builder()
			.title("Test Title")
			.content("Test Content")
			.author(testMember)
			.build();

		testComment = Comment.builder()
			.content("Test Comment")
			.member(testMember)
			.post(testPost)
			.build();
		testComment.setId(1L);

		testReplyComment = Comment.builder()
			.content("Test Reply Comment")
			.member(testMember)
			.post(testPost)
			.parent(testComment)
			.build();

	}

	@Test
	@DisplayName("댓글 생성 테스트")
	void createCommentTest() throws Exception {
	    // given
		CommentReq commentReq = new CommentReq("Test Comment");

		when(memberService.getMember(1L)).thenReturn(testMember);
		when(postService.findPostById(1L)).thenReturn(testPost);

		Comment comment = commentReq.toEntity(testMember, testPost);
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);

		// when
		Long commentId = commentService.createComment(commentReq, 1L, 1L);

		// then
		assertEquals(comment.getId(), commentId);
		verify(memberService, times(1)).getMember(1L);
		verify(postService, times(1)).findPostById(1L);
		verify(commentRepository, times(1)).save(any(Comment.class));
	}

	@Test
	@DisplayName("대댓글 생성 테스트")
	void createReplyCommentTest() throws Exception {
	    // given
		CommentReq replyCommentReq = new CommentReq("Test Reply Comment");

		when(memberService.getMember(1L)).thenReturn(testMember);
		when(postService.findPostById(1L)).thenReturn(testPost);
		when(commentRepository.findById(eq(testComment.getId()))).thenReturn(ofNullable(testComment));

		Comment replyComment = replyCommentReq.toEntity(testMember, testPost, testComment);
		when(commentRepository.save(any(Comment.class))).thenReturn(replyComment);

		// when
		Long replyCommentId = commentService.saveReplyComment(testComment.getId(), replyCommentReq, 1L, 1L);

		// then
		assertEquals(replyComment.getId(), replyCommentId);
		verify(memberService, times(1)).getMember(1L);
		verify(postService, times(1)).findPostById(1L);
		verify(commentRepository, times(1)).findById(any(Long.class));
		verify(commentRepository, times(1)).save(any(Comment.class));
	}

	@Test
	@DisplayName("게시글별 댓글 조회 테스트")
	void getCommentsByPostIdTest() {
		// given
		Comment comment1 = Comment.builder().content("Test Comment 1").post(testPost).build();
		Comment comment2 = Comment.builder().content("Test Comment 2").post(testPost).build();
		List<Comment> comments = Arrays.asList(comment1, comment2);

		Page<Comment> commentPage = new PageImpl<>(comments);
		when(commentRepository.findAllByPost_Id(1L, PageRequest.of(0, 10))).thenReturn(commentPage);

		// when
		Page<CommentRes> commentResPage = commentService.getCommentsByPostId(1L, PageRequest.of(0, 10));

		// then
		Assertions.assertEquals(2, commentResPage.getNumberOfElements());
		verify(commentRepository, times(1)).findAllByPost_Id(1L, PageRequest.of(0, 10));
	}

	@Test
	@DisplayName("댓글 아이디로 댓글 조회 테스트")
	void getCommentByIdTest() throws Exception {
	    // given
		when(commentRepository.findById(1L)).thenReturn(ofNullable(testComment));

	    // when
		CommentRes commentRes = commentService.getCommentById(1L);

		// then
		assertEquals(testComment.getContent(), commentRes.getContent());
		verify(commentRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("댓글 수정 테스트")
	void updateCommentTest() throws Exception {
		// given
		CommentReq updateCommentReq = new CommentReq("Updated Comment");
		when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

		// when
		commentService.updateComment(1L, updateCommentReq, testMember.getId());

		// then
		assertEquals(updateCommentReq.getContent(), testComment.getContent());
		verify(commentRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("댓글 삭제 테스트")
	void deleteCommentTest() throws Exception {
	    // given
		when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

	    // when
		commentService.deleteComment(1L, testMember.getId());

	    // then
		verify(commentRepository, times(1)).findById(1L);
		verify(commentRepository, times(1)).delete(testComment);
	}
}

