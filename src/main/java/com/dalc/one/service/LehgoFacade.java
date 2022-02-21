package com.dalc.one.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DataAccessException;

import com.dalc.one.domain.Course;
import com.dalc.one.domain.CoursePlace;
import com.dalc.one.domain.Folder;
import com.dalc.one.domain.FolderPlace;
import com.dalc.one.domain.Place;
import com.dalc.one.domain.User;
import com.dalc.one.domain.UserLikeCourse;
import com.dalc.one.domain.UserLikePlace;
import com.dalc.one.domain.UserSearchPlace;


public interface LehgoFacade {

	//USER
	List<User> getUserList() throws DataAccessException;
	User findUserbyUserId(String id) throws DataAccessException;
	void signUp(User user) throws DataAccessException;
	String findUserID(String email) throws DataAccessException;
	User findUser(String id, String email) throws DataAccessException;
	public int checkUserId(String id) throws DataAccessException;
	int checkUserEmail(String email);
	int checkUserNickname(String nickname);
	void updateUserInfo(User newUserInfo);
	void deleteUser(String userId);
	void resetPw(User user);
	
	
	//Place
	Place getPlace(int id);
	List<Place> getPlaceListbyName (String name) throws DataAccessException;
	List<Place> getPlaceListbyCategory (String category) throws DataAccessException;
	List<Place> getPlaceListbyArea(String area) throws DataAccessException;
	List<Place> getPlaceListbyContent(String content) throws DataAccessException;
	List<Place> getPlaceListbyAll(String query);
	//Place-리스트
	UserLikePlace addUserplace(User user, int id) throws DataAccessException;
	int deleteUserPlace(@Valid User user, int id) throws DataAccessException;
	List<UserLikePlace> getUserPlaceList(@Valid User user)throws DataAccessException;
	//Place-visited
	List<UserSearchPlace> getUserVisitedList(@Valid User user) throws DataAccessException;
	UserSearchPlace addUserVisitedPlace(@Valid User user, int id) throws DataAccessException;
	int deleteUserVisitedPlace(@Valid User user, int id) throws DataAccessException;
	/* UserPlace */
	Place addPlace(Place place);	

	/* Folder */
	Folder addFolder(@Valid User user, String folder) throws DataAccessException;
	int deleteFolder(@Valid User user, int id);
	List<Folder> getFolderList(String userId);
	Folder getFolder(int id);
	Folder updateFolder(Folder newFolder, String name);
	/* Folder Place */
	FolderPlace addFolderPlace(int folderId, int placeId);
	List<FolderPlace> getFolderPlaceList(int folderId);
	int deleteFolderPlace(int folderId, int placeId);
	

	/* Course */
	List<Course> getCourseList(String userId);
	List<Course> getVisibleCourse();
	Course getCourse(int id);
	Course getCourseByUserId(String id, int courseId);
	Course addCourse(@Valid User user, String courseName);
	Course updateCourse(Course course);
	int deleteCourse(int cid);
	/* CoursePlace */
	List<CoursePlace> getCourseDetail(int courseId);
	CoursePlace addCourseDetail(int courseId, int placeId, int priority);
	int deleteCourseDetail(int cid, int pid);
	CoursePlace updateCoursePlace(CoursePlace coursePlace);
	

	/* Like Course */
	UserLikeCourse addUserLikeCourse(@Valid User user, int courseId);
	int deleteUserLikeCourse(@Valid User user, int cid);
	List<UserLikeCourse> getUserLikeCourse(String userId);
}