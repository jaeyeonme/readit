package me.jaeyeon.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(description = "Comment model information")
@Data
public class CommentDto {

    @ApiModelProperty(value = "Blog comment id")
    private Long id;

    @ApiModelProperty(value = "Blog comment name")
    @NotBlank(message = "이름을 입력해 주세요")
    private String name;

    @ApiModelProperty(value = "Blog comment email")
    @NotBlank(message = "이메일을 입력해 주세요")
    @Email(message = "이메일 형식에 맞게 입력해 주세요")
    private String email;

    @ApiModelProperty(value = "Blog comment body")
    @NotBlank(message = "댓글을 입력해 주세요")
    @Size(min=5, message = "댓글은 최소 5자 이상 적어주세요")
    private String body;
}
