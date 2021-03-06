package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.SocialMedia;
import com.example.demo.model.Teacher;
import com.example.demo.model.TeacherSocialMedia;

public interface SocialMediaDao {
	
	void saveSocialMedia(SocialMedia socialMedia);
	
	void deleteSocialMediaById(Long IdSocialMedia);
	
	void updateSocialMedia(SocialMedia socialMedia);
	
	List<SocialMedia> findAllSocialMedias();

	SocialMedia findById(long IdSocialMedia);
	
	SocialMedia findByName(String name);
	
	TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname);
	
	TeacherSocialMedia findSocialMediaByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia);

}
