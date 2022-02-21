package com.dalc.one.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.UserLikeCourse;

public interface UserLikeCourseRepository extends CrudRepository<UserLikeCourse, Long>{


	int deleteByUserIdAndCourseId(String id, int cid);

	List<UserLikeCourse> findByUserIdOrderByTimeDesc(String userId);

}
