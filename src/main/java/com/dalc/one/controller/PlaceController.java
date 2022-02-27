package com.dalc.one.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dalc.one.ExceptionEnum;
import com.dalc.one.domain.Place;
import com.dalc.one.domain.User;
import com.dalc.one.domain.UserLikePlace;
import com.dalc.one.domain.UserSearchPlace;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.service.LehgoFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/place")
public class PlaceController{
	private LehgoFacade lehgo;

	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}
	
	@ResponseBody
	@GetMapping("search")
	public ResponseEntity<Place> getPlace(@RequestParam("id") int id) throws Exception {
		return ResponseEntity.ok(lehgo.getPlace(id));
	}
	
	@ResponseBody
	@GetMapping("search/category")
	public ResponseEntity<List<Place>> getPlaceListbyCategory(@RequestParam("category") String category ) throws Exception {
		return ResponseEntity.ok(lehgo.getPlaceListbyCategory(category));
	}

	@ResponseBody
	@GetMapping("search/area")
	public ResponseEntity<List<Place>> getPlaceListbyArea(@RequestParam("area") String area ) throws Exception {
		return ResponseEntity.ok(lehgo.getPlaceListbyArea(area));
	}
	
	@ResponseBody
	@GetMapping("search/content")
	public ResponseEntity<List<Place>> getPlaceListbyContent(@RequestParam("content") String content ) throws Exception {
		return ResponseEntity.ok(lehgo.getPlaceListbyContent(content));
	}
	
	@ResponseBody
	@GetMapping("search/name")
	public ResponseEntity<List<Place>> getPlaceListbyName(@RequestParam("name") String name ) throws Exception {
		return ResponseEntity.ok(lehgo.getPlaceListbyName(name));
	}
	
	@ResponseBody
	@GetMapping("search/all")
	public ResponseEntity<List<Place>> getPlaceListbyAll(@RequestParam("query") String query) throws Exception {
		return ResponseEntity.ok(lehgo.getPlaceListbyAll(query));
	}
	
	/* 사용자 선호 여행지 리스트*/
	@ResponseBody
	@PostMapping("mylist")
	public ResponseEntity<List<UserLikePlace>> getUserPlaceList(HttpServletRequest request, @Valid @RequestBody User user) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		return ResponseEntity.ok(lehgo.getUserPlaceList(user));
	}
	
	@ResponseBody
	@PostMapping("mylist/like")
	public ResponseEntity<HttpStatus> addUserPlace(HttpServletRequest request, @Valid @RequestBody User user, @RequestParam("id") int id ) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		UserLikePlace result = lehgo.addUserplace(user, id);
				
		if (result != null) {
			return ResponseEntity.ok(HttpStatus.OK);

		} else {
			return ResponseEntity.ok(HttpStatus.CONFLICT);
		}
		
	}
	
	@ResponseBody
	@PostMapping("mylist/delete")
	public ResponseEntity<HttpStatus> deleteUserPlace(HttpServletRequest request, @Valid @RequestBody User user, @RequestParam("id") int id ) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		int result = lehgo.deleteUserPlace(user, id);

		if(result > 0) {
			return ResponseEntity.ok(HttpStatus.OK);

		} else {
			return ResponseEntity.ok(HttpStatus.CONFLICT);
		}
	}
	
	/* 사용자가 본 여행지 리스트*/
	@ResponseBody
	@PostMapping("visited")
	public ResponseEntity<List<UserSearchPlace>> getUserVisitedList(@Valid @RequestBody User user) throws Exception {
		return ResponseEntity.ok(lehgo.getUserVisitedList(user));
	}
	
	@ResponseBody
	@PostMapping("visited/add")
	public ResponseEntity<HttpStatus> addUserVisitedPlace(@Valid @RequestBody User user, @RequestParam("id") int id ) throws Exception {
		UserSearchPlace result = lehgo.addUserVisitedPlace(user, id);
				
		if (result != null) {
			return ResponseEntity.ok(HttpStatus.OK);

		} else {
			return ResponseEntity.ok(HttpStatus.CONFLICT);
		}
		
	}
	
	@ResponseBody
	@PostMapping("visited/delete")
	public ResponseEntity<HttpStatus> deleteUserVisitedPlace(@Valid @RequestBody User user, @RequestParam("id") int id ) throws Exception {
		int result = lehgo.deleteUserVisitedPlace(user, id);

		if(result > 0) {
			return ResponseEntity.ok(HttpStatus.OK);

		} else {
			return ResponseEntity.ok(HttpStatus.CONFLICT);
		}
	}
	
	/* UserPlace */
	@ResponseBody
	@PostMapping("new")
	public ResponseEntity<Place> addNewPlace(HttpServletRequest request,
			@RequestBody Place place, @RequestParam("id") String userId) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(userId)) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		Place result = lehgo.addPlace(place);
		
		if (result == null) {
			throw new ResponseStatusException
				(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
		}
		return ResponseEntity.ok(result);
	}
}