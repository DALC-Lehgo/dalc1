package com.dalc.one.dao.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import com.dalc.one.domain.User;


@Mapper
public interface UserMapper {
	List<User> getUserList();
	User findUserbyUserId(String id);
	void signUp(User user);
}