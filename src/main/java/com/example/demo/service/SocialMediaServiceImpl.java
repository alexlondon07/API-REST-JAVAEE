package com.example.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.SocialMediaDao;
import com.example.demo.model.SocialMedia;
import com.example.demo.model.TeacherSocialMedia;

@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService {

	@Autowired
	private SocialMediaDao socialMediaDao;
	
	@Override
	public void saveSocialMedia(SocialMedia socialMedia) {
		socialMediaDao.saveSocialMedia(socialMedia);
	}

	@Override
	public void deleteSocialMediaById(Long IdSocialMedia) {
		socialMediaDao.deleteSocialMediaById(IdSocialMedia);
	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		socialMediaDao.updateSocialMedia(socialMedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedias() {
		return socialMediaDao.findAllSocialMedias();
	}

	@Override
	public SocialMedia findById(long IdSocialMedia) {
		return socialMediaDao.findById(IdSocialMedia);
	}

	@Override
	public SocialMedia findByName(String name) {
		return socialMediaDao.findByName(name);
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		return socialMediaDao.findSocialMediaByIdAndName(idSocialMedia, nickname);
	}
}
