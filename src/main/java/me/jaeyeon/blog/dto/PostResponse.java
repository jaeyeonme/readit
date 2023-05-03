package me.jaeyeon.blog.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.jaeyeon.blog.model.Post;

@Data
@NoArgsConstructor
public class PostResponse {

	private Long id;
	private String title;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private List<CommentRes> comments;

	public PostResponse(Post post) {
		this.id = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.createdDate = post.getCreatedDate();
		this.modifiedDate = post.getModifiedDate();
		this.comments = post.getComments().stream().map(CommentRes::new).collect(Collectors.toList());
	}
}
