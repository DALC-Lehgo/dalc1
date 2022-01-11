package com.dalc.one.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dalc.one.domain.User;
import com.dalc.one.service.Facade;


@Controller
public class IndexController {
	private Facade facade;
	
	@Autowired
	public void setFacade(Facade facade) {
		this.facade = facade;
	}
	
	@RequestMapping("/")
	public String index(ModelMap model) { 
		return "index"; 
	}
	
	@ResponseBody
	@RequestMapping("/user")
	public String indexData(ModelMap model) {  
		List<User> userList = facade.getUserList();
		JSONObject vo = new JSONObject();
		JSONArray va = new JSONArray();
		for (User user : userList) {
			JSONObject userObject = new JSONObject();
			userObject.put("id", user.getId());
			userObject.put("name", user.getName());
			va.add(userObject);
		}
		vo.put("user", va);
		return vo.toJSONString(); 
	}
}
