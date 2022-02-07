package com.dalc.one.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.dalc.one.domain.User;


public interface UserDAO {
	
	List<User> getUserList()  throws DataAccessException;
	User findUserbyUserId(String id) throws DataAccessException;
	String findIdbyEmail(String email) throws DataAccessException;
	void signUp(User user) throws DataAccessException;

	public int checkUserId(String id) throws DataAccessException;
	public int checkUserEmail(String email) throws DataAccessException;
	public int checkUserNickname(String nickname) throws DataAccessException;
	public void updateUserInfo(User user) throws DataAccessException;
	public void deleteUser(String id) throws DataAccessException;
	public User findUserbyIdAndEmail(String id, String email)throws DataAccessException;
	public void resetPw(String password, String id)throws DataAccessException;
}