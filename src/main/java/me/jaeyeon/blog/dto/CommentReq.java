package me.jaeyeon.blog.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jaeyeon.blog.model.Comment;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReq {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    public Comment toEntity(Member member, Post post) {
        return toEntity(member, post, null);
    }

    public Comment toEntity(Member member, Post post, Comment parent) {
        return Comment.builder()
            .member(member)
            .post(post)
            .content(content)
            .parent(parent)
            .build();
    }
}
