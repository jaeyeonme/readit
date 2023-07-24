package me.jaeyeon.readitdomain.comment.infrastructure;

import java.util.ArrayList;
import java.util.List;

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
import me.jaeyeon.readitdomain.comment.domain.Comment;
import me.jaeyeon.readitdomain.config.BaseTimeEntity;
import me.jaeyeon.readitdomain.member.infrastructure.MemberEntity;
import me.jaeyeon.readitdomain.post.infrastructure.PostEntity;

@Entity
@Getter
@ToString(exclude = {"post", "parent", "children"}, callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private PostEntity post;

	@Column(name = "content", nullable = false, length = 500)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private CommentEntity parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> children = new ArrayList<>();

	@Builder
	private CommentEntity(MemberEntity author, PostEntity post, String content, CommentEntity parent) {
		this.author = author;
		this.post = post;
		this.content = content;
		if (parent != null) {
			setParent(parent);
		}
	}

	public void setParent(CommentEntity parent) {
		this.parent = parent;
		if (parent != null) {
			parent.getChildren().add(this);
		}
	}

	public void updateComment(String content) {
		this.content = content;
	}

	public void setPost(PostEntity post) {
		if (this.post != null) {
			this.post.getComments().remove(this);
		}

		this.post = post;
		post.getComments().add(this);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Comment toModel() {
		return Comment.builder()
				.id(this.id)
				.content(this.content)
				.author(this.author.toModel())
				.post(this.post.toModel())
				.parent(this.parent != null ? this.parent.toModel() : null)
				.createdDate(this.getCreatedDate())
				.modifiedDate(this.getModifiedDate())
				.build();
	}
}
