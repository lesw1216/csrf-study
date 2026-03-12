package org.sangwoo.csrfstudy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    private static final String COOKIE_NAME = "session";
    private static final String COOKIE_VALUE = "abc123";

    @PostMapping("/login")
    public ResponseEntity<String> login() {

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, COOKIE_VALUE)
                .httpOnly(true)
                .path("/")
                .secure(false)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("login success");
    }

    @PostMapping("/login-same-site-none")
    public ResponseEntity<String> loginSameSiteNone() {

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, COOKIE_VALUE)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("login same site none success");
    }

    @PostMapping("/login-same-site-strict")
    public ResponseEntity<String> loginSameSiteStrict() {

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, COOKIE_VALUE)
                .httpOnly(true)
                .path("/")
                .secure(false)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("login same site strict success");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .path("/")
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("logout success");
    }

    @GetMapping("/get-csrf")
    public ResponseEntity<String> getCsrf(@CookieValue(value = COOKIE_NAME, required = false) String session) {

        log.info("GET 요청 받음, session: {}", session);

        if (session == null || !session.equals(COOKIE_VALUE)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("권한이 없음, 쿠키 인증 실패");
        }

        return ResponseEntity.ok("GET Success");
    }

    @PostMapping("/post-csrf")
    public  ResponseEntity<String> postCsrf(@CookieValue(value = COOKIE_NAME, required = false) String session) {

        log.info("POST 요청 받음, session: {}", session);

        if (session == null || !session.equals(COOKIE_VALUE)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("권한이 없음, 쿠키 인증 실패");
        }

        return ResponseEntity.ok("POST Success");
    }
}