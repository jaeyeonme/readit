package me.jaeyeon.blog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@InjectMocks
	private BlogPostService blogPostService;

	@Mock
	private PostRepository postRepository;

	@Mock
	private LoginService loginService;

	private Member member;
	private Post post;
	private PostReq request;

	@BeforeEach
	void before() {
		member = Member.builder()
				.userName("testUser")
				.email("test@email.com")
				.password("P@ssw0rd!")
				.build();
		member.setId(1L);

		post = Post.builder()
				.title("Test Title")
				.content("Test Content")
				.author(member)
				.build();

		request = new PostReq("Test Title", "Test Content");
	}

	@Test
	@DisplayName("게시글 생성 테스트")
	void createPostTest() {
		// when
		blogPostService.createPost(request, member);

		// then
		verify(postRepository, times(1)).save(any(Post.class));
	}

	@Test
	@DisplayName("키워드로 게시글 검색 테스트")
	void searchPostsWithKeywordTest() {
		// given
		PageRequest pageable = PageRequest.of(0, 10);
		when(postRepository.findByTitleContainingOrContentContaining(anyString(), any(Pageable.class)))
				.thenReturn(Page.empty(pageable));

		// when
		blogPostService.searchPostsWithKeyword("Test", pageable);

		// then
		verify(postRepository, times(1)).findByTitleContainingOrContentContaining(anyString(), any(Pageable.class));
	}

	@Test
	@DisplayName("게시글 조회 테스트")
	void getPostByIdTest() {
		// given
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));

		// when
		PostResponse foundPost = blogPostService.getPostById(1L);

		// then
		assertNotNull(foundPost);
		assertEquals(post.getId(), foundPost.getId());
	}

	@Test
	@DisplayName("게시글 수정 테스트")
	void updatePostTest() {
		// given
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
		when(loginService.getLoginMember()).thenReturn(member);

		// when
		blogPostService.updatePost(request, 1L, member);

		// then
		assertEquals(request.getTitle(), post.getTitle());
		assertEquals(request.getContent(), post.getContent());
	}

	@Test
	@DisplayName("게시글 삭제 테스트")
	void deletePostByIdTest() {
		// given
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
		when(loginService.getLoginMember()).thenReturn(member);

		// when
		blogPostService.deletePostById(1L, member);

		// then
		verify(postRepository, times(1)).delete(any(Post.class));
	}

	@Test
	@DisplayName("게시글 작성자가 사용자인지 확인")
	void checkWhetherAuthor() throws Exception {
		// given
		when(loginService.getLoginMember()).thenReturn(member);

		// then
		assertDoesNotThrow(() -> blogPostService.checkWhetherAuthor(post));
	}

	@Test
	@DisplayName("게시글 작성자가 사용자가 아닌 경우")
	void checkWhetherAuthorFail() throws Exception {
		// given
		Member anotherMember = Member.builder()
				.userName("anotherUser")
				.email("another@email.com")
				.password("P@ssw0rd!")
				.build();
		anotherMember.setId(2L);

		// when
		when(loginService.getLoginMember()).thenReturn(anotherMember);

		// then
		assertThrows(BlogApiException.class, () -> blogPostService.checkWhetherAuthor(post));
	}
}
