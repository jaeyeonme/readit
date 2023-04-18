package me.jaeyeon.blog.service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.PostDto;
import me.jaeyeon.blog.dto.PostResponse;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional(readOnly = true)
// public class PostService {
//
//     private final PostRepository postRepository;
//     private final ModelMapper modelMapper;
//
//     @Transactional
//     public PostDto createPost(PostDto postDto) {
//         Post post = modelMapper.map(postDto, Post.class);
//         return modelMapper.map(postRepository.save(post), PostDto.class);
//     }
//
//     // 모든 Posts 가져오기 (Pagination)
//     public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
//         Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
//                 Sort.by(sortBy).ascending() :
//                 Sort.by(sortBy).descending();
//
//         // Pageable 객체 생성
//         Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//         // pageable 을 입력해 원하는 페이지의 사이즈만큼 리스트 리턴
//         Page<Post> posts = postRepository.findAll(pageable);
//
//         // 페이지객체 안의 리스트만 가져오기
//         List<Post> listOfPosts = posts.getContent();
//         // DTO로 변환하려 리턴
//         List<PostDto> content = listOfPosts.stream()
//                 .map(post -> modelMapper.map(post, PostDto.class))
//                 .collect(Collectors.toList());
//
//         return PostResponse.builder()
//                 .content(content)
//                 .pageNo(posts.getNumber())
//                 .pageSize(posts.getSize())
//                 .totalElements(posts.getTotalElements())
//                 .totalPages(posts.getTotalPages())
//                 .last(posts.isLast())
//                 .build();
//     }
//
//     public PostDto getPostById(Long id) {
//         Post post = postRepository.findById(id).orElseThrow(
//                 () -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
//         return modelMapper.map(post, PostDto.class);
//     }
//
//     @Transactional
//     public PostDto updatePost(PostDto postDto, Long id) {
//         Post post = getPost(id);
//
//         post.setTitle(postDto.getTitle());
//         post.setDescription(postDto.getDescription());
//         post.setContent(postDto.getContent());
//         return modelMapper.map(post, PostDto.class);
//     }
//
//     @Transactional
//     public void deletePostById(Long id) {
//         Post post = getPost(id);
//         postRepository.delete(post);
//     }
//
//     private Post getPost(Long id) {
//         return postRepository.findById(id).orElseThrow(
//                 () -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
//     }
// }
