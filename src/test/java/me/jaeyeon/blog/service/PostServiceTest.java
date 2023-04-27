package me.jaeyeon.blog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.Pageable;

import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.MemberRepository;
import me.jaeyeon.blog.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@InjectMocks
	private PostService postService;

	@Mock
	private PostRepository postRepository;

	@Mock
	private MemberRepository memberRepository;
	private Member testMember;
	private Post testPost;

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
	}

	@Test
	@DisplayName("게시물 생성 - 정상적인 요청으로 게시물을 생성할 때")
	void createPost() throws Exception {
		// given
		PostReq postReq = new PostReq();
		postReq.setTitle("TestTitle");
		postReq.setContent("TestContent");

		given(memberRepository.findById(anyLong())).willReturn(Optional.of(testMember));
		given(postRepository.save(any(Post.class))).willReturn(testPost);

		// when
		postService.createPost(postReq, testMember.getId());

		// then
		then(memberRepository).should(times(1)).findById(anyLong());
		then(postRepository).should(times(1)).save(any(Post.class));
	}

	@Test
	@DisplayName("게시물 생성 - 작성자를 찾을 수 없는 경우")
	void createPost_memberNotFound() throws Exception {
		// given
		PostReq postReq = new PostReq();
		postReq.setTitle("TestTitle");
		postReq.setContent("TestContent");

		given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		assertThrows(BlogApiException.class, () -> postService.createPost(postReq, testMember.getId()));
	}

	@Test
	@DisplayName("게시물 조회 - 정상적인 요청으로 특정 게시물을 조회할 때")
	void getPostById() throws Exception {
		// given
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(testPost));

		// when
		PostResponse result = postService.getPostById(1L);

		// then
		assertEquals(testPost.getId(), result.getId());
		assertEquals(testPost.getTitle(), result.getTitle());
		assertEquals(testPost.getContent(), result.getContent());
		assertEquals(testPost.getCreatedDate(), result.getCreatedDate());
		assertEquals(testPost.getModifiedDate(), result.getModifiedDate());
		verify(postRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("게시물 검색 - 정상적인 요청으로 게시물을 검색할 때")
	void searchPostsWithKeyword() throws Exception {
	    // given
		List<Post> posts = Arrays.asList(testPost);
		String keyword = "Test";
		when(postRepository.findByTitleContainingOrContentContaining(anyString(), any(Pageable.class)))
			.thenReturn(new PageImpl<>(posts));

	    // when
		PageRequest pageable = PageRequest.of(0, 10);
		Page<PostResponse> result = postService.searchPostsWithKeyword(keyword, pageable);

	    // then
		assertEquals(posts.size(), result.getNumberOfElements());
		verify(postRepository, times(1)).findByTitleContainingOrContentContaining(anyString(), any(Pageable.class));
	}

	@Test
	@DisplayName("게시물 수정 - 정상적인 요청으로 게시물을 수정할 때")
	void updatePost() throws Exception {
		// given
		PostReq updateRequest = new PostReq("Updated Title", "Updated Content");
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(testPost));

		// when
		postService.updatePost(updateRequest, 1L, testMember.getId());

		// then
		verify(postRepository, times(1)).findById(anyLong());
		assertEquals("Updated Title", testPost.getTitle());
		assertEquals("Updated Content", testPost.getContent());
	}

	@Test
	@DisplayName("게시물 삭제 - 정상적인 요청으로 게시물을 삭제할 때")
	void deletePostById() throws Exception {
		// given
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(testPost));

		// when
		postService.deletePostById(1L, testMember.getId());

		// then
		verify(postRepository, times(1)).findById(anyLong());
		verify(postRepository, times(1)).delete(testPost);
	}

	@Test
	@DisplayName("게시물 작성자 확인 - 정상적인 요청으로 작성자와 세션 사용자가 동일한지 확인할 때")
	void checkWhetherAuthor() throws Exception {
		// given
		Post post = Post.builder()
			.title(testPost.getTitle())
			.content(testPost.getContent())
			.author(testMember)
			.build();

		// when
		// memberId 1L is the same as the author's ID
		assertDoesNotThrow(() -> postService.checkWhetherAuthor(testMember.getId(), post));
		// memberId 2L is not the same as the author's ID
		assertThrows(BlogApiException.class, () -> postService.checkWhetherAuthor(2L, post));

		// then
		// postRepository.findById()가 호출되지 않는 것을 검증합니다.
		verify(postRepository, times(0)).findById(anyLong());
	}
}
