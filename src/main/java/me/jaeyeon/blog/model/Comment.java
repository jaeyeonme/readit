package me.jaeyeon.blog.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String body;

    // 여러개(댓글)에 하나의 포스트, 포스트 클래스 : OneToMany 하나의 포스트에 여러개 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
