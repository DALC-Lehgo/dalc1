package com.dalc.one.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dalc.one.dao.UserDAO;
import com.dalc.one.domain.User;
import com.dalc.one.service.LehgoFacade;

@Service
@Transactional
public class LehgoImpl implements LehgoFacade{

	@Autowired
	private UserDAO userDao;

	public List<User> getUserList(){
		return userDao.getUserList();
	}
	public User findUserbyUserId(String id) {
		return userDao.findUserbyUserId(id);
	}
	public void signUp(User user) {
		userDao.signUp(user);
	}
	public int checkUserId(String id){
		return userDao.checkUserId(id);
	}
	public int checkUserEmail(String email){
		return userDao.checkUserEmail(email);
	}
	public int checkUserNickname(String nickname){
		return userDao.checkUserNickname(nickname);
	}
	public void updateUserInfo(User user) {
		userDao.updateUserInfo(user);
	}
	public void deleteUser(String id) {
		userDao.deleteUser(id);
	}
}