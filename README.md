# 블로그 프로젝트
---------
## (게시글) API 명세서
|HTTP Method|URL Path|Status Code|Description|
|:-----:|:-----:|:-----:|:-----:|
| GET | /api/posts | 200 (OK) | 모든 게시글 가져오기 |
| GET | /api/posts/{id} | 200 (OK) | 한 개의 게시글 가져오기 |
| POST | /api/posts | 201 (Created) | 새로운 게시글 작성 |
| PUT | /api/posts/{id} | 200 (OK) | 게시글 ID로 게시글 수정 |
| DELETE | /api/posts/{id} | 200 (OK) | 게시글 ID로 삭제 |
| GET | /api/posts?pageSize=5&pageNo=1&sortBy=firstName | 200 (OK) | 게시글 페이징 |


<br>

## (댓글) API 명세서
|HTTP Method|URL Path|Status Code|Description|
|:-----:|:-----:|:-----:|:-----:|
| GET | /api/posts/{postId}/comments | 200 (OK) | 게시글 ID에 해당하는 댓글 가져오기 |
| GET | /api/posts/{postId}/comments/{id} | 200 (OK) | 게시글 ID에 대항하는 댓글 있으면 가져오기 |
| POST | /api/posts/{postId}/comments | 201 (Created) | 새로운 댓글 작성 |
| PUT | /api/posts/{postId}/comments/{id} | 200 (OK) | 게시글 ID로 댓글이 있다면 댓글 수정 |
| DELETE | /api/posts/{postId}/comments/{id} | 200 (OK) | 게시글 ID로 삭제 |


<br>
<br>

# :pushpin: 배포(수정중)
> 
- AWS EC2 기간이 끝나서 Heroku로 이동중입니다.

</br>

## 1. 제작 기간
- 2022년 10월 10일 ~ 10월 15일
- 개인 프로젝트

</br>

## 2. 사용 기술
#### `Back-end`
  - Java 11
  - Spring Boot 2.5.9
  - Gradle
  - Spring Data JPA
  - MySQL
  - Swagger


#### `Heroku`
  - MySQL

</br>

## 3. ERD 설계
<img src="https://github.com/jaeyeonme/spring-blog/blob/main/picture/erd.png?raw=true" width="600" height="600">

<br>

## 4. DTO
  - Request, Resonse DTO를 따로 생성하지 않고 -> modelmapper로 통합
  - open-session-in-view true가 기본값이므로 Service 레이어에서 Entity -> DTO 변환작업

<br>
  
## 5. 연관관계 처리
  - 게시글과 댓글 연관관계를 고민하다, 고아객체 와 영속성전이로 설정해서 게시글이 지워지면 댓글도 같이 지워지게 설정

```java
@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<Comment> comments = new HashSet<>();

...

// 여러개(댓글)에 하나의 포스트, 포스트 클래스 : OneToMany 하나의 포스트에 여러개 댓글
@ManyToOne(fetch = FetchType.LAZY)
private Post post;
```

<br>

## 6. 추가작업 중
 - JWT
