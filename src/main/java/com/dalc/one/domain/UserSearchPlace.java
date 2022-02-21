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
class UserSearchPlacePK implements Serializable{
	private String userId;
	private int placeId;	
}

@SuppressWarnings("serial")
@Entity
@Table(name="user_search_place")
@IdClass(UserLikePlacePK.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSearchPlace implements Serializable{
	@Id
	@Column(name="user_id")
	private String userId;
	
	@Id
	@Column(name="place_id")
	private int placeId;
	
	@OneToOne
    @JoinColumn(name="place_id", insertable = false, updatable = false)
	private Place place;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime time;
}
