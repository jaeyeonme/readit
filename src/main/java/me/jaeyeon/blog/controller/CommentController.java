package me.jaeyeon.blog.controller;

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
import me.jaeyeon.blog.annotation.AuthenticationRequired;
import me.jaeyeon.blog.annotation.CurrentMember;
import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	@AuthenticationRequired
	public ResponseEntity<Void> createComment(@RequestBody @Valid CommentReq commentReq, @PathVariable Long postId,
			@CurrentMember Member member) {
		commentService.createComment(commentReq, postId, member);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/{commentId}")
	@AuthenticationRequired
	public ResponseEntity<Void> createReplyComment(@RequestBody @Valid CommentReq commentReq, @PathVariable Long postId,
			@PathVariable Long commentId, @CurrentMember Member member) {
		commentService.saveReplyComment(commentId, commentReq, postId, member);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Page<CommentRes>> getAllComment(@PathVariable Long postId,
			@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<CommentRes> comments = commentService.getCommentsByPostId(postId, pageable);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<CommentRes> getCommentById(@PathVariable Long commentId) {
		CommentRes comment = commentService.getCommentById(commentId);
		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

	@PutMapping("/{commentId}")
	@AuthenticationRequired
	public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentReq commentReq,
			@CurrentMember Member member) {
		commentService.updateComment(commentId, commentReq, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}")
	@AuthenticationRequired
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @CurrentMember Member member) {
		commentService.deleteComment(commentId, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
