package me.jaeyeon.readitdomain.comment.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.post.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreate {

	@NotBlank(message = "댓글 내용을 입력해주세요.")
	private String content;

	public Comment toEntity(Member author, Post post) {
		return Comment.builder()
				.content(this.content)
				.author(author)
				.post(post)
				.build();
	}

	public Comment toEntity(Member author, Post post, Comment parentComment) {
		Comment comment = this.toEntity(author, post);
		comment.setParent(parentComment);
		return comment;
	}
}
