package com.dalc.one.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dalc.one.domain.User;
import com.dalc.one.service.LehgoFacade;
import com.dalc.one.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {
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

	@RequestMapping("/")
	public String index(ModelMap model) {
		return "index";
	}

	//adminTest
	@ResponseBody
	@GetMapping("/admin/user") 
	public ResponseEntity<Map<String, Object>> indexData(ModelMap model) {
		List<User> userList = lehgo.getUserList();
		
		Map<String, Object> result = new HashMap<>();
		result.put("data", userList);
		
		return ResponseEntity.ok(result);
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