package me.jaeyeon.blog.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userName;
	private String email;
	private String password;

	@OneToMany(mappedBy = "member")
	private List<Post> posts = new ArrayList<>();

	@Builder
	public Member(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
}
