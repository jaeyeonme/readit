package me.jaeyeon.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.dto.CommentReq;
import me.jaeyeon.blog.dto.CommentRes;
import me.jaeyeon.blog.exception.BlogApiException;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;
import me.jaeyeon.blog.repository.CommentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    public Long createComment(CommentReq commentReq, Long memberId, Long postId) {
        // 회원 존재 확인
        Member member = memberService.getMember(memberId);

        // 게시글 존재 확인
        Post post = postService.findPostById(postId);

        // CommentReq에서 Comment 엔티티 생성
        Comment comment = commentReq.toEntity(member, post);

        // Comment 저장 및 ID 반환
        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    @Transactional(readOnly = true)
    public Page<CommentRes> getCommentsByPostId(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByPost_Id(postId, pageable);
        return comments.map(CommentRes::new);
    }

    // public void updateComment(Long id, CommentReq commentReq, Long memberId) {
    //     Comment comment = getComment(id);
    //     postService.checkAuthor(memberId, comment.getPost().getId());
    //     comment.updateComment(commentReq.getContent());
    // }
    //
    // public void deleteComment(Long id, Long memberId) {
    //     Comment comment = getComment(id);
    //     postService.checkAuthor(memberId, comment.getPost().getId());
    //     commentRepository.delete(comment);
    // }

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
