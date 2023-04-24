package me.jaeyeon.blog.controller;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.config.SessionConst;
import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

	@PostMapping
	public ResponseEntity<Void> createComment(@Valid @RequestBody CommentReq commentReq,
		@PathVariable Long postId,
		@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Long memberId) {
		commentService.createComment(commentReq, memberId, postId);
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
	public ResponseEntity<Void> updateComment(@PathVariable Long commentId,
		@Valid @RequestBody CommentReq commentReq,
		@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Long memberId) {
		commentService.updateComment(commentId, commentReq, memberId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
		@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Long memberId) {
		commentService.deleteComment(commentId, memberId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
