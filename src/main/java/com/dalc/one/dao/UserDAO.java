package com.dalc.one.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.dalc.one.domain.User;


public interface UserDAO {
	
	List<User> getUserList()  throws DataAccessException;
	User findUserbyUserId(String id) throws DataAccessException;
	void signUp(User user) throws DataAccessException;
}