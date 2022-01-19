package com.dalc.one.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dalc.one.domain.User;
import com.dalc.one.user.UserDTO;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.service.LehgoFacade;
import com.dalc.one.user.UserService;

import lombok.RequiredArgsConstructor;

//로그인, 로그아웃, 회원가입 등 사용자 서비스 전반
@RequiredArgsConstructor
@Controller
public class UserController{
	private LehgoFacade lehgo;
	private UserService userService;

	@Autowired
	public void setUserDetailServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}

	@PostMapping("/user")
	public ResponseEntity<String> login(@RequestBody UserDTO user, HttpServletResponse response) {
		if (!userService.checkPassword(user)) {
			throw new IllegalArgumentException("아이디 혹은 비밀번호가 잘못되었습니다.");
		}
		String token = JwtTokenProvider.makeJwtToken(userService.loadUserByUsername(user.getId()));
		response.setHeader("authorization", "bearer " + token);
		return ResponseEntity.ok(user.getId());

	}
	
	@PostMapping("/users/new")
	public String signUp(@RequestBody User user, HttpServletResponse response) {
		userService.signUp(user);
		return "signup";
	}
}