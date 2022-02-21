package com.dalc.one.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Data
class UserLikeCoursePK implements Serializable{
	private String userId;
	private int courseId;	
}

@SuppressWarnings("serial")
@Entity
@Table(name="user_like_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserLikeCoursePK.class)
public class UserLikeCourse implements Serializable{
	@Id
	@Column(name="USER_Id")
	private String userId;
	
	@Column(name="COURSE_Id")
	private int courseId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime time;
	
	@OneToOne
    @JoinColumn(name="course_id", insertable = false, updatable = false)
	private Course Course;
}
