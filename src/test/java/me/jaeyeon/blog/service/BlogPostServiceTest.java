package me.jaeyeon.blog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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

import me.jaeyeon.blog.dto.PostReq;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.MemberRepository;
import me.jaeyeon.blog.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class BlogPostServiceTest {

	@InjectMocks
	private BlogPostService postService;

	@Mock
	private PostRepository postRepository;
	@Mock
	private LoginService loginService;
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
	@DisplayName("게시글 생성 테스트")
	void createPostTest() {
		// given
		PostReq postReq = new PostReq("Test Title", "Test Content");
		when(postRepository.save(any(Post.class))).thenReturn(testPost);
		when(memberRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));

		// when
		postService.createPost(postReq, testMember.getId());

		// then
		verify(postRepository, times(1)).save(any(Post.class));
	}

	@Test
	@DisplayName("모든 게시글 조회 테스트")
	void getAllPostsTest() {
		// given
		PageRequest pageable = PageRequest.of(0, 10);
		when(postRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(testPost), pageable, 1));

		// when
		Page<PostResponse> result = postService.getAllPosts(pageable);

		// then
		Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
		verify(postRepository, times(1)).findAll(pageable);
	}

	@Test
	@DisplayName("키워드로 게시글 검색 테스트")
	void searchPostsWithKeywordTest() {
		// given
		String keyword = "Test";
		PageRequest pageable = PageRequest.of(0, 10);
		when(postRepository.findByTitleContainingOrContentContaining(keyword, pageable))
			.thenReturn(new PageImpl<>(Collections.singletonList(testPost), pageable, 1));


		// when
		Page<PostResponse> result = postService.searchPostsWithKeyword(keyword, pageable);

		// then
		Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
		verify(postRepository, times(1)).findByTitleContainingOrContentContaining(keyword, pageable);
	}

	@Test
	@DisplayName("게시글 조회 테스트")
	void getPostByIdTest() {
		// given
		when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));

		// when
		PostResponse result = postService.getPostById(testPost.getId());

		// then
		Assertions.assertThat(result.getTitle()).isEqualTo(testPost.getTitle());
		Assertions.assertThat(result.getContent()).isEqualTo(testPost.getContent());
		verify(postRepository, times(1)).findById(testPost.getId());
	}

	@Test
	@DisplayName("게시글 수정 테스트")
	void updatePostTest() {
		// given
		PostReq postReq = new PostReq("Updated Title", "Updated Content");
		when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
		when(loginService.getLoginMember()).thenReturn(testMember);

		// when
		postService.updatePost(postReq, testPost.getId(), testMember.getId());

		// then
		Assertions.assertThat(testPost.getTitle()).isEqualTo(postReq.getTitle());
		Assertions.assertThat(testPost.getContent()).isEqualTo(postReq.getContent());
		verify(postRepository, times(1)).findById(testPost.getId());
		verify(loginService, times(1)).getLoginMember();
	}

	@Test
	@DisplayName("게시글 삭제 테스트")
	void deletePostByIdTest() {
		// given
		when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
		when(loginService.getLoginMember()).thenReturn(testMember);
		doNothing().when(postRepository).delete(testPost);

		// when
		postService.deletePostById(testPost.getId(), testMember.getId());

		// then
		verify(postRepository, times(1)).findById(testPost.getId());
		verify(loginService, times(1)).getLoginMember();
		verify(postRepository, times(1)).delete(testPost);
	}

	@Test
	@DisplayName("게시글 찾기 테스트")
	void findPostByIdTest() {
		// given
		when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));

		// when
		Post result = postService.findPostById(testPost.getId());

		// then
		Assertions.assertThat(result).isEqualTo(testPost);
		verify(postRepository, times(1)).findById(testPost.getId());
	}

	@Test
	@DisplayName("게시글 가져오기 테스트")
	void getPostTest() {
		// given
		when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));

		// when
		Post result = postService.getPost(testPost.getId());

		// then
		Assertions.assertThat(result).isEqualTo(testPost);
		verify(postRepository, times(1)).findById(testPost.getId());
	}

	@Test
	@DisplayName("게시글 작성자 확인 테스트 - 성공")
	void checkWhetherAuthorTest_Success() {
		// given
		when(loginService.getLoginMember()).thenReturn(testMember);

		// when & then
		assertDoesNotThrow(() -> postService.checkWhetherAuthor(testPost));
		verify(loginService, times(1)).getLoginMember();
	}

	@Test
	@DisplayName("게시글 작성자 확인 테스트 - 실패")
	void checkWhetherAuthorTest_Fail() {
		// given
		Member anotherMember = Member.builder()
			.userName("anotherUser")
			.email("another@email.com")
			.password("P@ssw0rd!")
			.build();
		when(loginService.getLoginMember()).thenReturn(anotherMember);

		// when & then
		assertThrows(BlogApiException.class, () -> postService.checkWhetherAuthor(testPost));
		verify(loginService, times(1)).getLoginMember();
	}
}
