package me.jaeyeon.readitapi.comment.controller;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.annotation.AuthenticationRequired;
import me.jaeyeon.common.annotation.CurrentMember;
import me.jaeyeon.readitapi.comment.controller.response.CommentResponse;
import me.jaeyeon.readitdomain.comment.domain.Comment;
import me.jaeyeon.readitdomain.comment.domain.CommentCreate;
import me.jaeyeon.readitdomain.comment.service.CommentUseCase;
import me.jaeyeon.readitdomain.member.domain.Member;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

	private final CommentUseCase commentUseCase;

	@PostMapping
	@AuthenticationRequired
	public ResponseEntity<Void> createComment(@RequestBody @Valid CommentCreate request, @PathVariable Long postId,
			@CurrentMember Member member) {
		commentUseCase.createComment(request, postId, member);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/{commentId}")
	@AuthenticationRequired
	public ResponseEntity<Void> createReplyComment(@RequestBody @Valid CommentCreate request, @PathVariable Long postId,
			@PathVariable Long commentId, @CurrentMember Member member) {
		commentUseCase.replyToComment(request, postId, commentId, member);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Page<CommentResponse>> getAllComment(@PathVariable Long postId,
			@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Comment> comments = commentUseCase.getCommentsByPostId(postId, pageable);
		return new ResponseEntity<>(comments.map(CommentResponse::from), HttpStatus.OK);
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentId) {
		Comment comment = commentUseCase.getCommentById(commentId);
		return new ResponseEntity<>(CommentResponse.from(comment), HttpStatus.OK);
	}

	@PutMapping("/{commentId}")
	@AuthenticationRequired
	public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentCreate request,
			@CurrentMember Member member) {
		commentUseCase.updateComment(commentId, request, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}")
	@AuthenticationRequired
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @CurrentMember Member member) {
		commentUseCase.deleteComment(commentId, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
