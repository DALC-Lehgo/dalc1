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
			//throws com.dwu.alonealong.dao.DataAccessException {
		return userMapper.getUserList();
	}
}