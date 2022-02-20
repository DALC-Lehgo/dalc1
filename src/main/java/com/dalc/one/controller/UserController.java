package com.dalc.one.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	
	// 로그인
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
				throw new ResponseStatusException
					(ExceptionEnum.LOGIN_FAIL.getStatus(), ExceptionEnum.LOGIN_FAIL.getMessage());
			}
		}
		catch (NullPointerException e) {
			//아이디가 존재하지 않는 경우
			throw new ResponseStatusException
				(ExceptionEnum.LOGIN_FAIL.getStatus(), ExceptionEnum.LOGIN_FAIL.getMessage());
		}
	}
	
	// 회원가입
	@PostMapping("/users/new")
	public ResponseEntity<HttpStatus> signUp(@Valid @RequestBody User user, HttpServletResponse response){
		//기본적인 형식은 프론트에서 1차적으로 검증
		try {
			if (lehgo.checkUserId(user.getId()) > 0) {
				throw new ResponseStatusException
					(ExceptionEnum.EXIST_ID.getStatus(), ExceptionEnum.EXIST_ID.getMessage());
			}
			if (lehgo.checkUserEmail(user.getEmail()) > 0) {
				throw new ResponseStatusException
				(ExceptionEnum.EXIST_EMAIL.getStatus(), ExceptionEnum.EXIST_EMAIL.getMessage());
			}
			if (lehgo.checkUserNickname(user.getNickname()) > 0) {
				throw new ResponseStatusException
				(ExceptionEnum.EXIST_NICKNAME.getStatus(), ExceptionEnum.EXIST_NICKNAME.getMessage());
			}
			userService.signUp(user);
		} catch(NullPointerException e) {
			throw new ResponseStatusException
			(ExceptionEnum.NULL.getStatus(), ExceptionEnum.NULL.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	// 유저 정보 GET
	@GetMapping("users/{id}")
	public ResponseEntity<User> getUserInfo(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("id") String userId) throws IOException {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(userId)) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		User user = lehgo.findUserbyUserId(JwtTokenProvider.getUserOf(authorizationHeader).getUsername());
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("exists/id/{id}")
	public ResponseEntity<Integer> checkUserId(@PathVariable("id") String userId, HttpServletResponse response){
		return ResponseEntity.ok(lehgo.checkUserId(userId));
	}
	@PostMapping("exists/nickname/{nickname}")
	public ResponseEntity<Integer> checkUserNickname(@PathVariable("nickname") String nickname, HttpServletResponse response){
		return ResponseEntity.ok(lehgo.checkUserNickname(nickname));
	}
	@PostMapping("exists/email/{email}")
	public ResponseEntity<Integer> checkUserEmail(@PathVariable("email") String email, HttpServletResponse response){
		return ResponseEntity.ok(lehgo.checkUserEmail(email));
	}
	
	// 아이디 찾기
	@ResponseBody
	@GetMapping("users/findid/{email}")
	public ResponseEntity<String> findUserId(@PathVariable("email") String email) throws Exception {
		String userId = lehgo.findUserID(email);
		
		if (userId == null)
			throw new Exception("userId not found");
				
		return ResponseEntity.ok(userId);
	}
	
	
	// 비밀번호 찾기
	@ResponseBody
	@GetMapping("users/findpw")
	public ResponseEntity<User> findUserPw(@RequestParam(value = "id") String id,
			@RequestParam(value = "email") String email) throws Exception {

		User user = lehgo.findUser(id, email);
		if (user == null)  {
			throw new ResponseStatusException
			(ExceptionEnum.NO_USER.getStatus(), ExceptionEnum.NO_USER.getMessage());
		}

		return ResponseEntity.ok(user);
	}
	
	//비밀번호 재설정
	@ResponseBody
	@PutMapping("users/resetpw")
	public ResponseEntity<HttpStatus> resetPw(@Valid @RequestBody User user, @RequestParam(value = "pw") String pw, 
			HttpServletResponse response) throws Exception {
		user.setPassword(pw);
		try {
			userService.resetPw(user);
		} catch(NullPointerException e) {
			throw new ResponseStatusException
			(ExceptionEnum.NULL.getStatus(), ExceptionEnum.NULL.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	// 유저 정보 수정
	@PostMapping("users")
	public ResponseEntity<HttpStatus> updateUserInfo(HttpServletRequest request,
			@RequestBody User newUserInfo,
			@RequestParam(value = "id") String userId) {
		
		//수정 요청 ID와 로그인 ID의 일치 여부 확인
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(userId)) {
			ExceptionEnum e = ExceptionEnum.NOT_MATCH;
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}

		User pastUserInfo = lehgo.findUserbyUserId(JwtTokenProvider.getUserOf(authorizationHeader).getUsername());
		try {
			if (!pastUserInfo.getEmail().equals(newUserInfo.getEmail()) &&
					lehgo.checkUserEmail(newUserInfo.getEmail()) > 0) {
				throw new ResponseStatusException
				(ExceptionEnum.EXIST_EMAIL.getStatus(), ExceptionEnum.EXIST_EMAIL.getMessage());
			}
			if (!pastUserInfo.getNickname().equals(newUserInfo.getNickname()) &&
					lehgo.checkUserNickname(newUserInfo.getNickname()) > 0) {
				throw new ResponseStatusException
				(ExceptionEnum.EXIST_NICKNAME.getStatus(), ExceptionEnum.EXIST_NICKNAME.getMessage());
			}
			lehgo.updateUserInfo(newUserInfo);
		} catch(NullPointerException e) {
			throw new ResponseStatusException
			(ExceptionEnum.NULL.getStatus(), ExceptionEnum.NULL.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	// 유저 탈퇴
	@DeleteMapping("users/{id}")
	public ResponseEntity<HttpStatus> deleteUser(HttpServletRequest request,
			@PathVariable("id") String userId) {
		//탈퇴 요청 ID와 로그인 ID의 일치 여부 확인
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(userId)) {
			ExceptionEnum e = ExceptionEnum.NOT_MATCH;
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}

		try {
			lehgo.deleteUser(userId);
		} catch(DataAccessException e) {
			throw new ResponseStatusException
			(ExceptionEnum.NOT_DATA_ACCESS.getStatus(), ExceptionEnum.NOT_DATA_ACCESS.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@GetMapping("/checkUser")
	public ResponseEntity<JSONObject> checkUser(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader != null) {
			UserVO user = JwtTokenProvider.getUserOf(authorizationHeader);

			JSONObject userdata = new JSONObject();
			userdata.put("id", user.getUsername());
			userdata.put("auth", user.getUsername());
			return ResponseEntity.ok(userdata);
		}
		return null;
	}
}