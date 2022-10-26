package me.jaeyeon.blog.service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.CommentDto;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.CommentRepository;
import me.jaeyeon.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        // retrieve post entity by id
        Post post = getPost(postId);

        // set Post to comment entity
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);

        // comment entity to DB
        Comment newComment = commentRepository.save(comment);
        return modelMapper.map(newComment, CommentDto.class);
    }

    // postId로 댓글 찾기
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

    // id로 댓글 찾기
    public CommentDto getCommentById(Long postId, Long commentId) {
        //ID 로 포스트 찾기 못찾을 경우 예외처리
        Post post = getPost(postId);
        //ID 로 댓글 찾기 못찾을 경우 예외
        Comment comment = getComment(commentId);

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(ErrorCode.COMMENT_NOT_EQUALS_POST);
        }

        // mapToDTO
        return modelMapper.map(comment, CommentDto.class);
    }

    // id로 댓글 업데이트

    @Transactional
    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentDto) {
        //ID 로 포스트 찾기 못찾을 경우 예외처리
        Post post = getPost(postId);
        //ID 로 댓글 찾기 못찾을 경우 예외
        Comment comment = getComment(commentId);

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(ErrorCode.COMMENT_NOT_EQUALS_POST);
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        return modelMapper.map(commentRepository.save(comment), CommentDto.class);
    }

    // id로 댓글 삭제
    @Transactional
    public void deleteCommentById(Long postId, Long commentId) {
        //ID 로 포스트 찾기 못찾을 경우 예외처리
        Post post = getPost(postId);
        //ID 로 댓글 찾기 못찾을 경우 예외
        Comment comment = getComment(commentId);

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(ErrorCode.COMMENT_NOT_EQUALS_POST);
        }

        commentRepository.delete(comment);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new BlogApiException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new BlogApiException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
