# Spring Data JPA 블로그 프로젝트
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

# :pushpin: goQuality
> 블로그 API
> http://54.225.27.126/swagger-ui/index.html#/

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
  - H2
  - Post Man

#### `AWS`
  - EC2

</br>

## 3. ERD 설계

