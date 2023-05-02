  <div align="center"><https://user-images.githubusercontent.com/59726665/235715793-e41a73a5-46c2-48aa-b438-f4c6fe1edfa3.png" width="20%"/></div>
  <br>  
</p>

<p align="center"><b>"레딧 스타일의 창의적 커뮤니티 플랫폼"</b><br> <span>사용자들이 다양한 주제와 관심사를 공유하며, 서로 의견을 교환할 수 있는 블로그 서비스를 제공합니다.</p>


## 게시글 API 명세서
|HTTP Method|URL Path|Status Code|Description|
|:-----:|:-----:|:-----:|:-----:|
| POST | /api/posts | 201 (Created) | 새로운 게시글 작성 |
| GET | /api/posts | 200 (OK) | 모든 게시글 가져오기 (키워드 검색 가능) |
| GET | /api/posts/{id} | 200 (OK) | 게시글 ID로 가져오기 |
| PUT | /api/posts/{id} | 200 (OK) | 게시글 ID로 수정 |
| DELETE | /api/posts/{id} | 200 (OK) | 게시글 ID로 삭제 |

<br>

## 댓글 API 명세서
|HTTP Method|URL Path|Status Code|Description|
|:-----:|:-----:|:-----:|:-----:|
| POST | /api/posts/{postId}/comments | 201 (Created) | 새로운 댓글 작성 |
| POST | /api/posts/{postId}/comments/{commentId} | 201 (Created) | 대댓글 |
| GET | /api/posts/{postId}/comments | 200 (OK) | 게시글에 해당하는 댓글 가져오기 |
| GET | /api/posts/{postId}/comments/{commentId} | 200 (OK) | 댓글 ID로 가져오기 |
| PUT | /api/posts/{postId}/comments/{commentId} | 200 (OK) | 댓글 ID로 수정 |
| DELETE | /api/posts/{postId}/comments/{commentId} | 200 (OK) | 댓글 ID로 삭제 |

<br>

## 멤버 API 명세서
|HTTP Method|URL Path|Status Code|Description|
|:-----:|:-----:|:-----:|:-----:|
| POST | /members/register | 201 (Created) | 회원가입 |
| POST | /members/sign-in | 200 (OK) | 로그인 |
| POST | /members/logout | 200 (OK) | 로그아웃 |


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
  - Request, Resonse DTO를 따로 생성하지 않고 -> modelmapper로 DTO 통합 
  - open-session-in-view true가 default 값이므로, Service Layer에서 (Entity -> DTO 변환)

<br>
  
## 5. 연관관계 처리
  - 게시글과 댓글 1:N 연관관계 처리를 하였습니다., 고아객체 와 영속성전이(CASCADE)와 고아 객체(PrphanRemoval)로 세팅해서 부모엔티티가 변경되면 자식 엔티티도 같이 변경될 수 있도록 설정하였습니다.

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
