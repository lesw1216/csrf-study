# CSRF 테스트

CSRF 기법을 실제로 테스트 하기 위한 프로젝트.

```
.
└── src/
    ├── main/
    │   └── java/
    │       └── org/
    │           └── sangwoo/
    │               └── csrfstudy/
    │                   └── AuthController
    └── resources/
        └── static/
            ├── index.html
            └── attck-site/
                └── attack.html
```

* AuthController : 로그인, 로그아웃, GET, POST 테스트를 위한 컨트롤러
* index.html : 로그인, 로그아웃, GET, POST 요청을 위한 HTML
* attack.html : CSRF 공격을 위한 HTML

> 간단한 테스트를 위해 실제 로그인 로직은 넣지 않고, 로그인 요청이 들어오면 쿠키를 생성해서 응답하는 방식으로 구현

# 테스트 방법

1. 스프링 부트를 실행하여 http://localhost:8080/index.html 접속
2. attack.html 독립적으로 실행되어야함.

    Python을 사용하여 html을 서빙할 수 있음

    ```
   python -m htpp.server 4000 // 4000 포트로 열기
   ```
   
    attack.html 파일 위치로 이동하여 터미널로 위 명령어 실행, http://127.0.0.1:4000/attack.html 접속
   (서로 도메인이 달라야 하기 때문에 127.0.0.1:4000 사용)

위 조건에서 테스트를 진행하면 된다. ngrok을 사용하여 스프링 부트를 https로 호출해보는 것도 나쁘지 않은 방법


# 결과

attack.html에 작성한 세 가지 공격 기법은 다음과 같음.

1. 접속 시 img 태그의 src 속성을 사용한 GET 요청
2. 링크 클릭 시 href 속성을 사용한 GET 요청
3. Form 버튼 클릭 시 action 속성을 사용한 POST 요청

## SameSite=Lax

`SameSite=Lax`에서는 top-level navigation인 링크 클릭만 쿠키 전송을 허용.

위 세 가지 방법 중 쿠키가 전송되는 공걱 기법은 2번

## SameSite=None

`SameSite=None`에서는 모든 요청에 쿠키 전송을 허용.

따라서, 위 세 가지 공격 기법 모두 쿠키가 전송 됨.

## SameSite=Strict

`SameStie=Strict`에서는 모든 요청의 쿠키 전송을 막음.

따라서, 위 세가지 공격 기법 모두 쿠키가 전송되지 않음.