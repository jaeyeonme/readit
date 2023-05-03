package me.jaeyeon.blog.model;

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
import me.jaeyeon.blog.config.BaseTimeEntity;

@Entity
@Getter
@ToString(exclude = {"comments"}, callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

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
	private Member author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@Builder
	private Post(String title, String content, Member author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
