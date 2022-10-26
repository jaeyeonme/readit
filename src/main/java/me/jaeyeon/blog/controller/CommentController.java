package me.jaeyeon.blog.controller;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.CommentDto;
import me.jaeyeon.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // http://localhost:8080/api/posts/{postId}/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") Long postId,
                                                    @RequestBody @Valid CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    // http://localhost:8080/api/posts/{postId}/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable("postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // http://localhost:8080/api/posts/{postId}/comments/{commentId}
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") Long postId,
                                                     @PathVariable("commentId") Long commentId) {

        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }

    // http://localhost:8080/api/posts/{postId}/comments/{commentId}
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentsByPostId(@PathVariable("postId") Long postId,
                                                          @PathVariable("commentId") Long commentId,
                                                          @RequestBody @Valid CommentDto commentDto) {

        return new ResponseEntity<>(commentService.updateCommentById(postId, commentId, commentDto), HttpStatus.OK);
    }

    // http://localhost:8080/api/posts/{postId}/comments/{commentId}
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("postId") Long postId,
                                                    @PathVariable("commentId") Long commentId) {

        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }
}
