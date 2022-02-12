package com.dalc.one.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.Course;
import com.dalc.one.domain.FolderPlace;

public interface CourseRepository extends CrudRepository<Course, Long>{

	List<Course> findByUserId(String userId);

	Course findByCourseId(int id);

	Course findByCourseIdAndUserId(int courseId, String userId);

	int deleteByCourseId(int courseId);


}
