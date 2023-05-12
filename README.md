<p align="center"><img src="https://github.com/jaeyeonme/simple-blog/blob/main/picture/Reddit-removebg.png?raw=true" width="30%"></p>

<div align = "center" > 
  <b> "레딧 스타일의 창의적 커뮤니티 플랫폼" </b>
  <p/>
  <p> 사용자들이 다양한 주제와 관심사를 공유하며, 서로 의견을 교환할 수 있는 블로그 서비스를 제공합니다. </p>
</div>

<br>
<br>

## ✏ 무엇을 경험하려고 하는건가요? 
- 테스트 코드 작성과 테스트 커버리지를 높이기 위한 방법들을 적용하기 위해 노력했습니다.
- 코드의 재사용성, 확장성 및 유지 보수성 향상을 위해 OOP원칙을 적용하려고 노력했습니다.

<br>
<br>

## ✏ Project Structure
<p align="left">
  <div align="left"><img src="https://user-images.githubusercontent.com/59726665/237933958-b682fd6e-9a7c-48d2-b392-e8049251ccf3.png" width="75%"/></div>
  <br>  
</p>


<br>
<br>

## ✏ Architecture
<p align="left">
  <div align="left"><img src="https://user-images.githubusercontent.com/59726665/235740199-636ddcf7-0e5f-4155-a6da-82108f5c0d40.png" width="75%"/></div>
  <br>  
</p>

**3-Tier-Architecture** 적용
1. **Separation of Concerns (SoC)**: 각 계층은 명확한 역할과 책임을 가지고 있어, 각 계층의 관심사를 분리할 수 있습니다. 이로 인해 코드의 가독성과 유지 보수성이 향상됩니다.
2. **Scalability**: 각 계층을 독립적으로 확장할 수 있도록 지원합니다. 이는 프로젝트의 성장에 따라 성능과 안정성을 유지할 수 있게 해줍니다.
3. **Modularity**: 계층 간의 느슨한 결합(loose coupling)을 통해, 개별 계층을 독립적으로 개발, 테스트 및 배포할 수 있습니다. 이를 통해 개발 효율성과 개별 기능의 확장성이 향상됩니다.
4. **Reusability**: 계층간의 인터페이스가 명확하게 정의되어 있으므로, 각 계층의 구성 요소를 재사용할 수 있습니다. 이를 통해 코드의 중복을 줄이고 개발 시간을 절약할 수 있습니다.
5. **Maintainability**: 각 계층의 변경 사항이 다른 계층에 최소한의 영향을 미치도록 합니다. 따라서 유지 보수가 용이하며, 프로젝트의 생명 주기가 길어질수록 이점이 더 커집니다.


<br>
<br>


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
