# Devstagram

## 🛠️사용 기술 및 환경

- Spring Boot
- MongoDB
- Docker
- Kubernetes
- Naver Cloud Platform

## 🙌**DevStagram 서비스 소개**

- 개발자에게 사이드 프로젝트, 스터디는 숙명과도 같은 일. 하지만 개인적 인맥으로만 팀을 구하는 것은 한계가 있습니다. 여기저기 찾아보지 마시고 개발자 커뮤니티 서비스 ***DevStagram***에서 쉽게 MeepUp하세요!
- 네이버 카페, 오픈카톡 등에서 스터디를 구하고자 하면 스터디 장은 지원자의 신원을 확인할 수 없어 지원서에 프로필과 포부를 적어달라고 합니다. 그러나 이런 방식은 지원자에게도 부담이고, 스터디 장도 제한된 정보로 사람을 판단하기는 어려운 일입니다.
- 그래서 ***DevStagram***에서는 프로필 기능, 포스팅 기능을 제공합니다. 블로그나 깃허브 보다는 가볍게, 인스타그램에는 차마 올리지 못했던 개발자스러운 이야기들을 자유롭게 올려보세요. 서로의 포스팅 기록만 보고 나랑 잘 맞는 사람인지 확인할 수 있지 않을까요?

## 🔖 화면 구성

- `figma`: [피그마 파일 보러가기](https://www.figma.com/file/qOrnqo4o5ZJyKmMKjGKCWa/Devstagram-2%EC%B0%A8?node-id=0%3A1)

![image](https://user-images.githubusercontent.com/56625356/183299638-ce712c13-28ad-473e-9739-7fa575f86412.png) ![image](https://user-images.githubusercontent.com/56625356/183299692-a3317f1a-47ad-4631-ab05-ac3b0a265f79.png) ![image](https://user-images.githubusercontent.com/56625356/183299696-e6455c65-5422-46c6-a103-68d6835d6238.png) ![image](https://user-images.githubusercontent.com/56625356/183299713-bfd796f9-2d59-44e1-8de0-e3f12d6806ce.png) ![image](https://user-images.githubusercontent.com/56625356/183299720-0429cab8-906a-4576-90fe-c59e46ae17b5.png) ![image](https://user-images.githubusercontent.com/56625356/183299727-810feaae-397b-4aef-8ab5-e61f86056065.png)



## 🔖 WorkFlow

![image](https://user-images.githubusercontent.com/56625356/183299774-3f6913c0-2708-47d5-b4ac-ca087df4aa54.png)



## 🔖 DB 설계 (Mongo DB)
![image](https://user-images.githubusercontent.com/56625356/183299783-7b438aa8-50c3-42a7-8f27-2c320b696d49.png)

- 연결 표시되어있는 것은 논리적인 관계를 표시한 것일뿐, 실제로 릴레이션이 설정되어있는 것은 아닙니다.

## 🔖 아키텍처

- 클라우드 아키텍처
![image](https://user-images.githubusercontent.com/56625356/183299798-ed420935-cae5-406d-ada5-9123c0d66295.png)


- 백엔드 아키텍처
![image](https://user-images.githubusercontent.com/56625356/183299839-28016ff1-78cb-43cb-8465-bcbf2c944fc5.png)





## 🙅‍♀️Gateway 정책

| URI Rule | 기능 | 토큰 필요 | Gateway 동작 | routing to |
| --- | --- | --- | --- | --- |
| Path=/api/auth/** | 로그인, 비밀번호 관련 기능 | N | RestTemplate로 내부통신(devsta-user에서 받은 응답으로 JWT 생성 처리) | devsta-users |
| Path=/api/user/profile/** | 내 프로필 정보, 수정 | Y | AuthFilter에서 토큰 인증 후 라우팅 | devsta-users |
| Path=/api/posts/** | 포스팅, 타임라인 관련 기능 | Y | AuthFilter에서 토큰 인증 후 라우팅 | devsta-posts |
| Path=/api/meetup/service/** |  밋업 관련 기능 중 로그인 필요한 기능 | Y | AuthFilter에서 토큰 인증 후 라우팅 | devsta-meetup |
| Path=/api/meetup/read/** | 밋업 관련 기능 중 로그인 필요 없는 기능 | N | uri rewrite만 하고 토큰 인증 없이 라우팅 | devsta-meetup |

## ❓MongoDB 사용 이유

- 1:N 관계가 많기 때문에 Join/정규화/트랜잭션을 사용해야 장점을 잘 활용하게 되는 RDB보다 NoSQL 계열의 DB가 성능이 유리하다고 생각했습니다.
- 고도화된 검색 기능보다 DB 데이터 수정/삭제가 빠른 것이 더 중요하기 때문에 Elastic Search 보다 MongoDB가 적합하다고 판단했습니다.
    - 일반적인 NoSQL DBMS의 기능으로써의 높은 활용도
- 트래픽이 많은 서비스라고 가정하고 개발했기 때문에 MongoDB를 선택했습니다
    - ACID를 위해 Table 단위로 Blocking을 하는 RDBMS는 대용량 트래픽에서 성능이슈가 발생할 수 있습니다. 반면 도큐먼트 단위로 Lock을 할 수 있는 MongoDB가 동시성 이슈를 해결하며 빠른 성능을 낼 수 있다고 생각했습니다.
        - 기존 RDBMS와 같은 트랜잭션이 필요한 경우, 이를 Kafka 등을 통한 메시징 시스템을 통한 해결이나, RDBMS를 인메모리 유효성 데이터만 끌고 와서 처리하는 식의 활용도 가능하기에, 좀 더 범용 기술이 된 MongoDB 이해도를 높여놓는 것이 장점이 많을 거라고 판단하기도 했습니다.
    - Sharding, Clustering을 통해 Scale-Out을 구현할 수 있습니다.

## 👐**브랜치 관리 전략**

- Issue 생성 → Issue 번호를 딴 branch 생성
- 목적에 따라 feat, refact, debug 폴더로 branch 관리

```
keyword
- feat : 새로운 기능이 추가 됨
- refact : 기능 혹은 성능 개선
- debug : 버그 수정
```

- 개발 완료 후 main branch 내용을 current branch로 Merge한 후 Pull Request

## 🌟Issue 및 해결방안

- 밋업, 포스트 리스트 조회할 때 검색 조건, 페이지 길이/ 스크롤 속도 등에 따라 Frontend 에서 유연하게 요청이 가능해야함
    - Pagination 제공, 다양한 조회 조건에 따른 조회 API 제공
- 내가 작성하지 않은 밋업 글에 대해서도 수정/삭제/멤버 수정 등이 가능한 문제
    - Gateway에서 JWT 토큰 decoding 하여 userID를 헤더에 담아 라우팅
    - 헤더의 userId와 밋업 글 작성자(밋업 리더)의 ID를 비교하여 권한이 있는지 확인
- DB에 Data Update시 Lock을 최소화 하면서 수정 필요
    - MongoTemplate 사용하여 원하는 field만 수정
    - FindAndModifyOptions로 수정 후 결과 리턴해주도록 설정 (수정 후 확인 차 조회하지 않아도됨)
- H**ateoas 적용**
    - 리스트 가져오는 API 결과에 각각의 상세 설정 볼 수 있는 링크, 다음 페이지(pagination) 볼 수 있는 링크 자동으로 달아주는 기능
    - 프론트엔드에서 페이지 계산 안하고 클릭으로 다음 페이지를 갈 수 있게 제공하기 위해 도입
    - Assembler이용해 코드 모듈화
- Front-end에서 API Response를 받아 처리할 때 데이터 형식이 제각각이면 일관성 있는 처리가 어려운 문제
    - CommonResponse로 API의 응답 타입 형식화
        - 사례 바로가기
- 일일이 커스텀 예외 클래스를 만들면 지나치케 클래스가 많아지는 문제
    - CustomException과 CommonCode로 에러 메세지 공통 관리
        - CustomException 코드 보러가기
        - CommonCode 보러가기
- Private Subnet에 있는 Service 와 DB 통신문제
    - 보안그룹에서 Inbound 룰 추가
    - 개발 목적으로 접근하는 트래픽은 Bastion 서버를 통해 Local Forwarding 구현
    - [자세한 설명 보러가기(블로그)](https://velog.io/@mdy0102/NCP-%EB%8F%84%EC%BB%A4Kubernetes-%EC%84%A4%EC%A0%95)
