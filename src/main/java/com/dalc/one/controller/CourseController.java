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
import com.dalc.one.domain.CoursePlace;
import com.dalc.one.domain.User;
import com.dalc.one.domain.UserLikeCourse;
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
		Course course = lehgo.getCourse(id);
		String authorizationHeader = request.getHeader("authorization");

		if(course.getVisibility() == 1) { // 작성자만 view 가능할 때
			if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(course.getUserId())) { // 코스의 ID와 로그인 ID가 일치하지 않으면 오류
				throw new ResponseStatusException
					(ExceptionEnum.PERMISSION_FAIL.getStatus(), ExceptionEnum.PERMISSION_FAIL.getMessage());
			}
			else {
				return ResponseEntity.ok(lehgo.getCourse(id));
			}
		}
			return ResponseEntity.ok(lehgo.getCourse(id));
	}
	
	@ResponseBody
	@GetMapping("all")
	public ResponseEntity<List<Course>> getVisibleCourseList(HttpServletRequest request) throws Exception {
		return ResponseEntity.ok(lehgo.getVisibleCourse());
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
	@GetMapping("detail")
	public ResponseEntity<List<CoursePlace>> getCourseDetail(HttpServletRequest request, @RequestParam("cid") int courseId) throws Exception {	
		Course course = lehgo.getCourse(courseId);
		String authorizationHeader = request.getHeader("authorization");

		if(course.getVisibility() == 1) { // 작성자만 view 가능할 때
			if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(course.getUserId())) { // 코스의 ID와 로그인 ID가 일치하지 않으면 오류
				throw new ResponseStatusException
					(ExceptionEnum.PERMISSION_FAIL.getStatus(), ExceptionEnum.PERMISSION_FAIL.getMessage());
			}
			else // 코스의 작성자면 view
				return ResponseEntity.ok(lehgo.getCourseDetail(courseId));
		} 
		// 접속한 모든 사람들 view 가능
		return ResponseEntity.ok(lehgo.getCourseDetail(courseId));
	}
	
	@ResponseBody
	@GetMapping("detail/new")
	public ResponseEntity<CoursePlace> AddCourseDetail(HttpServletRequest request, 
			@RequestParam("cid") int courseId, @RequestParam("pid") int placeId, @RequestParam("priority") int priority) throws Exception {
		CoursePlace result = lehgo.addCourseDetail(courseId, placeId, priority);
		if (result == null) {
			throw new ResponseStatusException
				(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
		}
		return ResponseEntity.ok(result);
	}
	
	@ResponseBody
	@PostMapping("new")
	public ResponseEntity<Course> addNewCourse(HttpServletRequest request,
			@Valid @RequestBody User user, @RequestParam("name") String courseName) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		Course result = lehgo.addCourse(user, courseName);
		
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
	@PutMapping("detail/update")
	public ResponseEntity<CoursePlace> updateCourseDetail(HttpServletRequest request, @RequestBody CoursePlace coursePlace) throws Exception {
		Course course = lehgo.getCourse(coursePlace.getCourseId());
		CoursePlace result = new CoursePlace();
		String authorizationHeader = request.getHeader("authorization");

		if(course.getEditable() == 1) { // 작성자만 edit 가능할 때
			if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(course.getUserId())) { // 코스의 ID와 로그인 ID가 일치하지 않으면 오류
				throw new ResponseStatusException
					(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_MATCH.getMessage());
			}
			else {
				result = lehgo.updateCoursePlace(coursePlace);
			}
		}
		else { // 로그인한 모두가 edit 가능
			if (authorizationHeader == null) {
				throw new ResponseStatusException
					(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
			}
			else
				result = lehgo.updateCoursePlace(coursePlace);
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
	
	@ResponseBody
	@DeleteMapping("detail/delete")
	public ResponseEntity<HttpStatus> deleteCourseDetail(HttpServletRequest request, @RequestParam("cid") int cid, @RequestParam("pid") int pid) throws Exception {
		Course course = lehgo.getCourse(cid);
		String authorizationHeader = request.getHeader("authorization");

		int result = 0;
		if(course.getEditable() == 1) { // 작성자만 edit 가능할 때
			if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(course.getUserId())) { // 코스의 ID와 로그인 ID가 일치하지 않으면 오류
				throw new ResponseStatusException
					(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_MATCH.getMessage());
			}
			else {
				result = lehgo.deleteCourseDetail(cid, pid);
			}
		}
		else { // 로그인한 모두가 edit 가능
			if (authorizationHeader == null) {
				throw new ResponseStatusException
					(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
			}
			else
				result = lehgo.deleteCourseDetail(cid, pid);
		}
		
		if(result > 0) return ResponseEntity.ok(HttpStatus.OK);
		else return ResponseEntity.ok(HttpStatus.CONFLICT);
	}
	
	/* User Like Course */
	@ResponseBody
	@PostMapping("like")
	public ResponseEntity<UserLikeCourse> likeCourse(HttpServletRequest request,
			@Valid @RequestBody User user, @RequestParam("cid") int courseId) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		UserLikeCourse result = new UserLikeCourse();
		try {
			result = lehgo.addUserLikeCourse(user, courseId);
			if (result == null) {
				throw new ResponseStatusException
					(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
			}
		}
		 catch(Exception e) {
				throw new ResponseStatusException
				(ExceptionEnum.NOT_EXIST.getStatus(), ExceptionEnum.NOT_EXIST.getMessage());
		}
		
		return ResponseEntity.ok(result);
	}
	
	@ResponseBody
	@PostMapping("dislike")
	public ResponseEntity<HttpStatus> deleteUserLikeCourse(HttpServletRequest request, 
			@Valid @RequestBody User user, @RequestParam("cid") int cid) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		int result = lehgo.deleteUserLikeCourse(user, cid);

		if(result > 0) return ResponseEntity.ok(HttpStatus.OK);
		else return ResponseEntity.ok(HttpStatus.CONFLICT);
	}
	
	@ResponseBody
	@GetMapping("like")
	public ResponseEntity<List<UserLikeCourse>> getUserLikeCourse(HttpServletRequest request, @RequestParam("id") String userId) throws Exception {
		return ResponseEntity.ok(lehgo.getUserLikeCourse(userId));
	}
}