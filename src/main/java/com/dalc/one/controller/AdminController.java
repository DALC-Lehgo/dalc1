package com.dalc.one.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dalc.one.ExceptionEnum;
import com.dalc.one.domain.User;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.service.LehgoFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController{
	private LehgoFacade lehgo;

	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}
	
	@ResponseBody
	@PostMapping()
	public ResponseEntity<List<User>> getUserList(HttpServletRequest request, @Valid @RequestBody User user) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		try {
			if (user.getNickname().equals("관리자")) {
				return ResponseEntity.ok(lehgo.getUserList());
			}
			else {
				throw new ResponseStatusException
					(ExceptionEnum.PERMISSION_FAIL.getStatus(), ExceptionEnum.PERMISSION_FAIL.getMessage());
			}
		}
		catch (NullPointerException e) {
			throw new ResponseStatusException
				(ExceptionEnum.PERMISSION_FAIL.getStatus(), ExceptionEnum.PERMISSION_FAIL.getMessage());
		}
	}
	
	
	
}