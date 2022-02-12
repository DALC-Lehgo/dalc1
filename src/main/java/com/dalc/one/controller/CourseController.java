package com.dalc.one.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dalc.one.ExceptionEnum;
import com.dalc.one.domain.Course;
import com.dalc.one.domain.User;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.service.LehgoFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController{
	private LehgoFacade lehgo;

	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}
	
	@ResponseBody
	@GetMapping
	public ResponseEntity<Course> getCourse(HttpServletRequest request, @RequestParam("cid") int id) throws Exception {
		return ResponseEntity.ok(lehgo.getCourse(id));
	}
	
	@ResponseBody
	@GetMapping("my")
	public ResponseEntity<Course> getCourseByUserId(HttpServletRequest request, @RequestParam("id") String id, @RequestParam("cid") int courseId) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(id)) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		return ResponseEntity.ok(lehgo.getCourseByUserId(id, courseId));
	}
	
	@ResponseBody
	@GetMapping("list")
	public ResponseEntity<List<Course>> getCourseList(HttpServletRequest request, @RequestParam("user") String userId) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(userId)) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		return ResponseEntity.ok(lehgo.getCourseList(userId));
	}
	
	@ResponseBody
	@PostMapping("new")
	public ResponseEntity<Course> addNewCourse(HttpServletRequest request,
			@Valid @RequestBody User user) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		Course result = lehgo.addCourse(user);
		
		if (result == null) {
			throw new ResponseStatusException
				(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
		}
		return ResponseEntity.ok(result);
	}
	
	@ResponseBody
	@PutMapping("update")
	public ResponseEntity<Course> updateCourse(HttpServletRequest request, @RequestBody Course course) throws Exception {
		Course result = lehgo.updateCourse(course);
		
		if (result == null) {
			throw new ResponseStatusException
				(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
		}
		return ResponseEntity.ok(result);
	}
	
	@ResponseBody
	@DeleteMapping("delete")
	public ResponseEntity<HttpStatus> deleteCourse(HttpServletRequest request, @RequestParam("cid") int cid) throws Exception {
		
		int result = lehgo.deleteCourse(cid);

		if(result > 0) return ResponseEntity.ok(HttpStatus.OK);
		else return ResponseEntity.ok(HttpStatus.CONFLICT);
	}
}