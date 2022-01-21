package com.dalc.one;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ExceptionEnum {
	LOGIN_FAIL(HttpStatus.NOT_FOUND, "아이디 또는 비밀번호가 잘못 입력되었습니다."),
	NO_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	NOT_MATCH(HttpStatus.NOT_FOUND, "로그인한 사용자의 정보와 일치하지 않습니다."),
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003");  

    private final HttpStatus status;
    private String message;

    ExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}