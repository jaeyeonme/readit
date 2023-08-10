package me.jaeyeon.readitapi.post.controller;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
import me.jaeyeon.common.utils.CursorRequest;
import me.jaeyeon.common.utils.PageCursor;
import me.jaeyeon.readitapi.post.controller.response.DailyPostCount;
import me.jaeyeon.readitapi.post.controller.response.PostResponse;
import me.jaeyeon.readitapi.post.controller.response.PostSummaryResponse;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.domain.PostCreate;
import me.jaeyeon.readitdomain.post.domain.PostUpdate;
import me.jaeyeon.readitdomain.post.infrastructure.PostCountPerDate;
import me.jaeyeon.readitdomain.post.service.PostReadUseCase;
import me.jaeyeon.readitdomain.post.service.PostWriteUseCase;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostWriteUseCase postWriteUseCase;
	private final PostReadUseCase postReadUseCase;

	@PostMapping
	@AuthenticationRequired
	public ResponseEntity<Void> createPost(@RequestBody @Valid PostCreate request, @CurrentMember Member member) {
		postWriteUseCase.createPost(request, member);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Page<PostResponse>> getPostsWithKeyword(
			@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
			@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<Post> postPage = postReadUseCase.searchPostsWithKeyword(keyword, pageable);
		Page<PostResponse> postResponsePage = postPage.map(PostResponse::from);
		return new ResponseEntity<>(postResponsePage, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable("id") Long id) {
		Post post = postReadUseCase.getPostById(id);
		PostResponse postResponse = PostResponse.from(post);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@AuthenticationRequired
	public ResponseEntity<Void> updatePost(@RequestBody @Valid PostUpdate request, @PathVariable("id") Long id,
			@CurrentMember Member member) {
		postWriteUseCase.updatePost(request, id, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@AuthenticationRequired
	public ResponseEntity<Void> deletePost(@PathVariable("id") Long id, @CurrentMember Member member) {
		postWriteUseCase.deletePostById(id, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/daily-post-counts")
	public ResponseEntity<List<DailyPostCount>> getDailyPostsByMemberId(
			@RequestParam Long memberId,
			@RequestParam String firstDate,
			@RequestParam String lastDate) {

		LocalDate start = LocalDate.parse(firstDate);
		LocalDate end = LocalDate.parse(lastDate);

		List<PostCountPerDate> postCountPerDates =
				postReadUseCase.countPostsByMemberAndDateRange(memberId, start, end);

		List<DailyPostCount> dailyPostCounts = postCountPerDates.stream()
				.map(postCountPerDate ->
						DailyPostCount.from(memberId, postCountPerDate.date(), postCountPerDate.count()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(dailyPostCounts);
	}

	@GetMapping("/members/{memberId}/by-cursor")
	public ResponseEntity<PageCursor<PostSummaryResponse>> getPostByCursor(
			@PathVariable Long memberId,
			CursorRequest cursorRequest) {

		PageCursor<Post> postPage = postReadUseCase.getPostsByAuthorIdAndCursor(memberId,
				cursorRequest);

		List<PostSummaryResponse> postResponses = postPage.body().stream()
				.map(PostSummaryResponse::from)
				.collect(Collectors.toList());

		return ResponseEntity.ok(new PageCursor<>(postPage.nextCursorRequest(), postResponses));
	}
}
