package com.dalc.one.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.dalc.one.domain.Folder;
import com.dalc.one.domain.FolderPlace;
import com.dalc.one.domain.User;

import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.service.LehgoFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/folder")
public class FolderController{
	private LehgoFacade lehgo;

	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}
	
	@ResponseBody
	@GetMapping()
	public ResponseEntity<Folder> getFolder(HttpServletRequest request, @RequestParam("id") int id) throws Exception {
		return ResponseEntity.ok(lehgo.getFolder(id));
	}
	
	@ResponseBody
	@GetMapping("list")
	public ResponseEntity<List<Folder>> getFolderList(HttpServletRequest request, @RequestParam("user") String userId) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(userId)) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		return ResponseEntity.ok(lehgo.getFolderList(userId));
	}
	
	@ResponseBody
	@PostMapping("new")
	public ResponseEntity<Folder> addNewFolder(HttpServletRequest request,
			@Valid @RequestBody User user, @RequestParam("name") String name ) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		Folder result = lehgo.addFolder(user, name);
		
		if (result == null) {
			throw new ResponseStatusException
				(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
		}
		return ResponseEntity.ok(result);
	}
	
	@ResponseBody
	@PutMapping("update")
	public ResponseEntity<Folder> updateFolder(HttpServletRequest request,
			@Valid @RequestBody User user, @RequestParam("name") String newName, @RequestParam("id") int forderId ) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		Folder folder = lehgo.getFolder(forderId);
		folder.setFolderName(newName);
		Folder result = lehgo.updateFolder(folder, newName);
		
		if (result == null) {
			throw new ResponseStatusException
				(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
		}
		return ResponseEntity.ok(result);
	}
	
	@ResponseBody
	@PostMapping("delete")
	public ResponseEntity<HttpStatus> deleteFolder(HttpServletRequest request, 
			@Valid @RequestBody User user, @RequestParam("id") int id) throws Exception {
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_LOGIN.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		else if (!JwtTokenProvider.getUserOf(authorizationHeader).getUsername().equals(user.getId())) {
			throw new ResponseStatusException
				(ExceptionEnum.NOT_MATCH.getStatus(), ExceptionEnum.NOT_LOGIN.getMessage());
		}
		
		int result = lehgo.deleteFolder(user, id);

		if(result > 0) return ResponseEntity.ok(HttpStatus.OK);
		else return ResponseEntity.ok(HttpStatus.CONFLICT);
	}

	@ResponseBody
	@GetMapping("place/new")
	public ResponseEntity<FolderPlace> newFolderPlace(HttpServletRequest request, 
			@RequestParam("place") int placeId, @RequestParam("folder") int folderId) throws Exception {
		FolderPlace result = lehgo.addFolderPlace(folderId, placeId);
		
		if (result == null) {
			throw new ResponseStatusException
				(ExceptionEnum.INPUT_FAIL.getStatus(), ExceptionEnum.INPUT_FAIL.getMessage());
		}
		return ResponseEntity.ok(result);
	}
	
	@ResponseBody
	@GetMapping("place/delete")
	public ResponseEntity<HttpStatus> deleteFolderPlace(HttpServletRequest request, 
			@RequestParam("place") int placeId, @RequestParam("folder") int folderId) throws Exception {
		
		int result = lehgo.deleteFolderPlace(folderId, placeId);
		
		if(result > 0) return ResponseEntity.ok(HttpStatus.OK);
		else return ResponseEntity.ok(HttpStatus.CONFLICT);
	}
	
	@ResponseBody
	@GetMapping("place/list")
	public ResponseEntity<List<FolderPlace>> GetFolderPlaceList(HttpServletRequest request, 
			@RequestParam("folder") int folderId) throws Exception {
		return ResponseEntity.ok(lehgo.getFolderPlaceList(folderId));
	}
}