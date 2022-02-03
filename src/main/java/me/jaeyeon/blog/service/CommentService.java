package me.jaeyeon.blog.service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.CommentDto;
import me.jaeyeon.blog.entity.Comment;
import me.jaeyeon.blog.entity.Post;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ResourceNotFoundException;
import me.jaeyeon.blog.repository.CommentRepository;
import me.jaeyeon.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        final Post post = getPost(postId);

        // set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        final Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    public List<CommentDto> getCommentsByPostId(Long postId) {
        // retrieve comments by postId
        final List<Comment> comments = commentRepository.findByPostId(postId);

        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        final Post post = getPost(postId);

        // retrieve comment by id
        final Comment comment = getComment(commentId);

        extracted(comment, post, "Comment does not belong to post");

        return mapToDTO(comment);
    }

    @Transactional
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        // retrieve post entity by id
        final Post post = getPost(postId);

        final Comment comment = getComment(commentId);

        extracted(comment, post, "Comment does not belongs to post");

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        final Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {

        // retrieve post entity by id
        final Post post = getPost(postId);

        // retrieve comment by id
        final Comment comment = getComment(commentId);

        extracted(comment, post, "Comment does not belongs to post");

        commentRepository.delete(comment);
    }

    private void extracted(Comment comment, Post post, String message) {
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
    }


    private CommentDto mapToDTO(Comment comment) {
//        final CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
//        final Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return modelMapper.map(commentDto, Comment.class);
    }
}
