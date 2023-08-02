package me.jaeyeon.readitdomain.post.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.jaeyeon.readitdomain.member.domain.Member;

public record PostCreate(
		@NotBlank(message = "제목을 입력해 주세요.")
		@Size(min = 2, message = "제목은 2자 이상 적어주세요")
		String title,

		@NotBlank(message = "컨텐츠를 입력해 주세요.")
		String content) {

	public Post toEntity(Member author) {
		return Post.builder()
				.title(this.title)
				.content(this.content)
				.author(author)
				.build();
	}
}
