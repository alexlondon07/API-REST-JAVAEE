package com.example.demo.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Course;
import com.example.demo.model.SocialMedia;
import com.example.demo.model.TeacherSocialMedia;

@Repository
@Transactional
public class SocialMediaDaoImp extends AbstractSession implements SocialMediaDao {

	public SocialMediaDaoImp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveSocialMedia(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSocialMediaById(Long IdSocialMedia) {
		// TODO Auto-generated method stub
		SocialMedia socialMedia = findById(IdSocialMedia);
		if(IdSocialMedia !=null){
			getSession().delete(socialMedia);
		}
	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
		getSession().update(socialMedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedias() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public SocialMedia findById(long IdSocialMedia) {
		// TODO Auto-generated method stub
		return getSession().get(SocialMedia.class, IdSocialMedia);
	}

	@Override
	public SocialMedia findByName(String name) {
		// TODO Auto-generated method stub
		return (SocialMedia) getSession().createQuery("from SocialMedia where name :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		// TODO Auto-generated method stub
		List<Object[]> objects =  (List<Object[]>) getSession().createQuery(
				"from TeacherSocialMedia tsm join tms.socialMedia sm"
				+ " where t.idSocialMedia = :idSocialMedia and tsm.nickname = :nickname")
				.setParameter("idSocialMedia", idSocialMedia)
				.setParameter("nickname", nickname);
		
		if(objects.size() > 0){
			for(Object[] objects2 :  objects){
				for(Object object :  objects2){
					if(object instanceof TeacherSocialMedia ){
						return (TeacherSocialMedia) object;
					}
				}
			}
		}
		return null;
	}

}
