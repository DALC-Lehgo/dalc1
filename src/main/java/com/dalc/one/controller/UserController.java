package com.dalc.one.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.dalc.one.ExceptionEnum;
import com.dalc.one.domain.User;
import com.dalc.one.user.UserDTO;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.service.LehgoFacade;
import com.dalc.one.user.UserService;
import com.dalc.one.user.UserVO;

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
	public ResponseEntity<String> login(@Valid @RequestBody UserDTO user, 
			HttpServletResponse response) throws IOException {
		try {
			if (userService.checkPassword(user)) {
				String token = JwtTokenProvider.makeJwtToken(userService.loadUserByUsername(user.getId()));
				response.setHeader("authorization", "bearer " + token);
				return ResponseEntity.ok(user.getId());
			}
			else {
				//비밀번호가 맞지 않는 경우
				ExceptionEnum exception = ExceptionEnum.LOGIN_FAIL;
				throw new ResponseStatusException(exception.getStatus(), exception.getMessage());
			}
		}
		catch (NullPointerException e) {
			//아이디가 존재하지 않는 경우
			ExceptionEnum exception = ExceptionEnum.LOGIN_FAIL;
			throw new ResponseStatusException(exception.getStatus(), exception.getMessage());
		}
	}
	
	@PostMapping("/users/new")
	public String signUp(@RequestBody User user, HttpServletResponse response) {
		userService.signUp(user);
		return "signup";
	}
	
	@GetMapping("users/{id}")
	public ResponseEntity<User> getUserInfo(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("id") String userId) throws IOException {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			//리다이렉트
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(userId)) {
			ExceptionEnum e = ExceptionEnum.NOT_MATCH;
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}
		User user = lehgo.findUserbyUserId(JwtTokenProvider.getUserOf(authorizationHeader).getUsername());
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("users/{id}")
	public ResponseEntity<User> editUserInfo(HttpServletRequest request,
			@RequestBody User user,
			@PathVariable("id") String userId) {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader != null && 
				JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			//비밀번호가 일치하면...
			//이메일 유효성 검사
			//닉네임 유효성 검사
			
			return ResponseEntity.ok(user);
		}
		return null;
	}
	
//	@GetMapping("/checkUser")
//	public ResponseEntity<JSONObject> checkUser(HttpServletRequest request) {
//		String authorizationHeader = request.getHeader("authorization");
//		if (authorizationHeader != null) {
//			UserVO user = JwtTokenProvider.getUserOf(authorizationHeader);
//
//			JSONObject userdata = new JSONObject();
//			userdata.put("id", user.getUsername());
//			userdata.put("auth", user.getUsername());
//			return ResponseEntity.ok(userdata);
//		}
//		return null;
//	}
}