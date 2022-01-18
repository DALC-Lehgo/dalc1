package com.dalc.one.service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.dalc.one.domain.User;


public interface LehgoFacade {

	//USER
	List<User> getUserList() throws DataAccessException;
	
}