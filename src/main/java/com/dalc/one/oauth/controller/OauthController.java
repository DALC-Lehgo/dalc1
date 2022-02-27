package com.dalc.one.oauth.controller;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.oauth.constants.SocialLoginType;
import com.dalc.one.oauth.service.OauthService;
import com.dalc.one.user.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/oauth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    /**
     * 사용자로부터 SNS 로그인 요청을 Social Login Type 을 받아 처리
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
		log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        oauthService.request(socialLoginType);
    }
    
    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어노는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    public void callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code, HttpServletResponse response) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
        response.setHeader("Location", "http://localhost:8081/login?code=" + code + "&socialLoginType=" + socialLoginType);

        response.setStatus(302);
    }
    
    @GetMapping(value = "/{socialLoginType}/login")
    public ResponseEntity<UserVO> login(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code, HttpServletResponse response) {
        String token = ((String) oauthService.requestAccessToken(socialLoginType, code));
        response.setHeader("authorization", token);
        return ResponseEntity.ok(JwtTokenProvider.getUserOf(token));
    }
}