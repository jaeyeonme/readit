package me.jaeyeon.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(description = "Post model information")
@Data
public class PostDto {

    @ApiModelProperty(value = "Blog post id")
    private Long id;

    @ApiModelProperty(value = "Blog post title")
    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(min = 2, message = "제목은 2자 이상 적어주세요")
    private String title;

    @ApiModelProperty(value = "Blog post description")
    @NotBlank(message = "설명을 입력해 주세요.")
    @Size(min = 5, message = "설명은 5자 이상 적어주세요.")
    private String description;

    @ApiModelProperty(value = "Blog post content")
    @NotBlank(message = "컨텐츠를 입력해 주세요.")
    private String content;

}
