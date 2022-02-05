package com.dalc.one.dao.mybatis;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;

import com.dalc.one.dao.UserDAO;
import com.dalc.one.dao.mybatis.mapper.UserMapper;
import com.dalc.one.domain.User;


@Repository
public class MybatisUserDAO implements UserDAO {

	@Autowired
	private UserMapper userMapper;

	@Override
	public List<User> getUserList() {
		return userMapper.getUserList();
	}
	
	@Override
	public User findUserbyUserId(String id) {
		return userMapper.findUserbyUserId(id);
	}
	@Override
	public void signUp(User user) {
		userMapper.signUp(user);
	}

	@Override
	public int checkUserId(String id){
		return userMapper.checkUserId(id);
	}
	@Override
	public int checkUserEmail(String email){
		return userMapper.checkUserEmail(email);
	}
	@Override
	public int checkUserNickname(String nickname){
		return userMapper.checkUserNickname(nickname);
	}
	@Override
	public void updateUserInfo(User user) {
		userMapper.updateUserInfo(user);
	}
	@Override
	public void deleteUser(String id) {
		userMapper.deleteUser(id);
	}

	@Override
	public String findIdbyEmail(String email) throws DataAccessException {
		return userMapper.findIdbyEmail(email);
	}

	@Override
	public User findUserbyIdAndEmail(String id, String email) throws DataAccessException {
		return userMapper.findUserbyIdAndEmail(id, email);
	}

	@Override
	public void resetPw(String password, String id) throws DataAccessException {
		userMapper.resetPassword(password, id);
	}
	
	
}
