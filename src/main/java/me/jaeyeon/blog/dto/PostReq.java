package me.jaeyeon.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.model.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReq {

	@NotBlank(message = "제목을 입력해 주세요.")
	@Size(min = 2, message = "제목은 2자 이상 적어주세요")
	private String title;

	@NotBlank(message = "컨텐츠를 입력해 주세요.")
	private String content;

	public Post toEntity(Member author) {
		return Post.builder()
			.title(title)
			.content(content)
			.author(author)
			.build();
	}
}
