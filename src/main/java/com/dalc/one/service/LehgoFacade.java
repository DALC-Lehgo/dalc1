package com.dalc.one.service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.dalc.one.domain.User;


public interface LehgoFacade {

	//USER
	List<User> getUserList() throws DataAccessException;
	User findUserbyUserId(String id) throws DataAccessException;
	void signUp(User user) throws DataAccessException;
	
	public int checkUserId(String id) throws DataAccessException;
	public int checkUserEmail(String email) throws DataAccessException;
	public int checkUserNickname(String nickname) throws DataAccessException;
	
	public void updateUserInfo(User user) throws DataAccessException;
	public void deleteUser(String id) throws DataAccessException;
}