package me.jaeyeon.readitdomain.post.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.jaeyeon.readitdomain.comment.infrastructure.CommentEntity;
import me.jaeyeon.readitdomain.config.BaseTimeEntity;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;
import me.jaeyeon.readitdomain.post.domain.Post;

@Entity
@Getter
@ToString(exclude = {"comments"}, callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "title", nullable = false, length = 10)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT", length = 500)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> comments = new ArrayList<>();

	@Builder
	private PostEntity(String title, String content, MemberEntity author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void addComment(CommentEntity comment) {
		this.comments.add(comment);
		if (comment.getPost() != this) {
			comment.setPost(this);
		}
	}

	public Post toModel() {
		return Post.builder()
				.id(this.id)
				.title(this.title)
				.content(this.content)
				.author(this.author.toModel())
				.comments(comments.stream().map(CommentEntity::toModel).collect(Collectors.toList()))
				.createdDate(this.getCreatedDate())
				.modifiedDate(this.getModifiedDate())
				.build();
	}
}
