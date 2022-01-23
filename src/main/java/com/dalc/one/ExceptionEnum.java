package com.dalc.one;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ExceptionEnum {
	//로그인
	LOGIN_FAIL(HttpStatus.NOT_FOUND, "아이디 또는 비밀번호가 잘못 입력되었습니다."),
	NO_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	NOT_MATCH(HttpStatus.NOT_FOUND, "로그인한 사용자의 정보와 일치하지 않습니다."),
	//회원가입
	EXIST_ID(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
	EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
	EXIST_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
	NOT_DATA_ACCESS(HttpStatus.BAD_REQUEST, "올바르지 않은 정보가 있습니다."),
	NULL(HttpStatus.BAD_REQUEST, "입력되지 않은 정보가 있습니다."),
	//인증
	NOT_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."), 
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");

    private final HttpStatus status;
    private String message;

    ExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}