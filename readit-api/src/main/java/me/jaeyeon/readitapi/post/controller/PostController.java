package me.jaeyeon.readitapi.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.annotation.AuthenticationRequired;
import me.jaeyeon.common.annotation.CurrentMember;
import me.jaeyeon.readitapi.post.controller.response.PostResponse;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.domain.PostCreate;
import me.jaeyeon.readitdomain.post.domain.PostUpdate;
import me.jaeyeon.readitdomain.post.service.PostUseCase;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostUseCase postUseCase;

	@PostMapping
	@AuthenticationRequired
	public ResponseEntity<Void> createPost(@RequestBody @Valid PostCreate request, @CurrentMember Member member) {
		postUseCase.createPost(request, member);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Page<PostResponse>> getPostsWithKeyword(
			@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
			@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<Post> postPage = postUseCase.searchPostsWithKeyword(keyword, pageable);
		Page<PostResponse> postResponsePage = postPage.map(PostResponse::from);
		return new ResponseEntity<>(postResponsePage, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable("id") Long id) {
		Post post = postUseCase.getPostById(id);
		PostResponse postResponse = PostResponse.from(post);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@AuthenticationRequired
	public ResponseEntity<Void> updatePost(@RequestBody @Valid PostUpdate request, @PathVariable("id") Long id,
			@CurrentMember Member member) {
		postUseCase.updatePost(request, id, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@AuthenticationRequired
	public ResponseEntity<Void> deletePost(@PathVariable("id") Long id, @CurrentMember Member member) {
		postUseCase.deletePostById(id, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
