package me.jaeyeon.blog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.CommentDto;
import me.jaeyeon.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "CRUD REST API for Comment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // http://localhost:8080/api/posts/{postId}/comments
    @ApiOperation(value = "Create Comment REST API")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") Long postId,
                                                    @RequestBody @Valid CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    // http://localhost:8080/api/posts/{postId}/comments
    @ApiOperation(value = "Get All Comments By Post ID REST API")
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable("postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // http://localhost:8080/api/posts/{postId}/comments/{commentId}
    @ApiOperation(value = "Get Single Comment By ID REST API")
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") Long postId,
                                                     @PathVariable("commentId") Long commentId) {

        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }

    // http://localhost:8080/api/posts/{postId}/comments/{commentId}
    @ApiOperation(value = "Delete Comment By ID REST API")
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
