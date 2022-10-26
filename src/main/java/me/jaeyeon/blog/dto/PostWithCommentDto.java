package me.jaeyeon.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class PostWithCommentDto {

    private Long id;

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(min = 2, message = "제목은 2자 이상 적어주세요")
    private String title;

    @NotBlank(message = "설명을 입력해 주세요.")
    @Size(min = 5, message = "설명은 5자 이상 적어주세요.")
    private String description;

    @NotBlank(message = "컨텐츠를 입력해 주세요.")
    private String content;
    private Set<CommentDto> comments;
}
