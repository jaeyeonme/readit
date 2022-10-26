package me.jaeyeon.blog.controller;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.PostDto;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final ModelMapper modelMapper;
    static final String DEFAULT_PAGE_NUMBER = "0";
    static final String DEFAULT_PAGE_SIZE = "10";
    static final String DEFAULT_SORT_BY = "id";
    static final String DEFAULT_SORT_DIRECTION = "ASC";

    // http://localhost:8008/api/posts
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto) {
        PostDto newPost = postService.createPost(postDto);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }


    // http://localhost:8008/api/posts&pageNo=0&pageSize=5
    // http://localhost:8080/api/posts?pageNo=2&pageSize=5&sortBy=title&sortDir=DESC
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {

        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    // http://localhost:8080/api/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {
        PostDto postById = postService.getPostById(id);
        return new ResponseEntity<>(postById, HttpStatus.OK);
    }

    // http://localhost:8080/api/posts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody @Valid PostDto postDto,
                                              @PathVariable("id") Long id) {

        PostDto updatedPost = postService.updatePost(postDto, id);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // http://localhost:8080/api/posts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }
}
