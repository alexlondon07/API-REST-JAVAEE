package com.example.demo.service;

import java.util.List;

import com.example.demo.model.SocialMedia;
import com.example.demo.model.TeacherSocialMedia;

public interface SocialMediaService {

	
	void saveSocialMedia(SocialMedia socialMedia);
	
	void deleteSocialMediaById(Long IdSocialMedia);
	
	void updateSocialMedia(SocialMedia socialMedia);
	
	List<SocialMedia> findAllSocialMedias();

	SocialMedia findById(long IdSocialMedia);
	
	SocialMedia findByName(String name);
	
	TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname);

}
