package com.dalc.one.dao.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dalc.one.domain.User;

/**
 * @author Eduardo Macarron
 *
 */
@Mapper
public interface UserMapper {
	List<User> getUserList();
}
