package me.jaeyeon.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.CommentRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogPostService postService;
    private final GeneralMemberService memberService;

    public Long createComment(CommentReq commentReq, Long memberId, Long postId) {
        // 회원 존재 확인
        Member member = memberService.getMember(memberId);

        // 게시글 존재 확인
        Post post = postService.findPostById(postId);

        // CommentReq에서 Comment 엔티티 생성
        Comment comment = commentReq.toEntity(member, post);

        // 댓글을 게시글에 추가
        comment.setPost(post);

        // Comment 저장 및 ID 반환
        Comment savedComment = commentRepository.save(comment);
        log.info("Created comment with ID: {}", savedComment.getId());
        return savedComment.getId();
    }

    public Long saveReplyComment(Long commentId, CommentReq request, Long postId, Long memberId) {
        // 부모 댓글, 회원, 게시글 정보를 가져오기
        Comment parentComment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BlogApiException(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberService.getMember(memberId);
        Post post = postService.findPostById(postId);

        // CommentReq 객체를 이용하여 대댓글 객체를 생성
        Comment childComment = request.toEntity(member, post, parentComment);

        // 생성한 대댓글 객체를 저장하고 반환
        Comment savedComment = commentRepository.save(childComment);
        return savedComment.getId();
    }

    @Transactional(readOnly = true)
    public Page<CommentRes> getCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findAllByPost_Id(postId, pageable).map(CommentRes::new);
    }

    @Transactional(readOnly = true)
    public CommentRes getCommentById(Long commentId) {
        Comment comment = getComment(commentId);
        return new CommentRes(comment);
    }

    public void updateComment(Long id, CommentReq commentReq, Long memberId) {
        Comment comment = getComment(id);
        checkWhetherAuthor(comment, memberId);
        comment.updateComment(commentReq.getContent());
        log.info("Updated comment with ID: {}", comment.getId());
    }

    public void deleteComment(Long id, Long memberId) {
        Comment comment = getComment(id);
        checkWhetherAuthor(comment, memberId);
        commentRepository.delete(comment);
        log.info("Deleted comment with ID: {}", comment.getId());
    }

    private Comment getComment(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new BlogApiException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public void checkWhetherAuthor(Comment comment, Long memberId) {
        if (!comment.checkIsOwner(memberId)) {
            throw new BlogApiException(ErrorCode.IS_NOT_OWNER);
        }
    }
}
