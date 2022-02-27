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

import com.dalc.one.domain.PlaceKeyword;
import com.dalc.one.domain.User;
import com.dalc.one.service.LehgoFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class AIController{
	private LehgoFacade lehgo;

	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}
	
	/* AI 여행지 리스트*/
	@ResponseBody
	@PostMapping
	public ResponseEntity<List<PlaceKeyword>> getUserPlaceList(HttpServletRequest request, @Valid @RequestBody User user) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		int keyword = 1;
		
		if (authorizationHeader != null) { // 로그인을 한 상태면
			keyword = lehgo.getUserKeyword(user).getKeywordId();
		}
		return ResponseEntity.ok(lehgo.getAiPlaceList(keyword));
	}
}