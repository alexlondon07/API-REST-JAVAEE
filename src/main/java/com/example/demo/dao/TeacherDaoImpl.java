package com.example.demo.dao;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Teacher;
import com.example.demo.model.TeacherSocialMedia;


@Repository
@Transactional
public class TeacherDaoImpl extends AbstractSession implements TeacherDao {
	
	public void saveTeacher(Teacher teacher) {
		getSession().persist(teacher);
	}

	public void deleteTeacherById(Long idTeacher) {
		Teacher teacher = findById(idTeacher);
		if(idTeacher != null){
			
			/**
			 * Eliminamos todas las socialMedias de un Teacher
			 */
			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();
			while (i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				i.remove();
				getSession().delete(teacherSocialMedia);
			}
			teacher.getTeacherSocialMedias().clear();
			/**
			 * Eliminamos nuestro Teacher
			 */
			getSession().delete(teacher);
		}
	}

	public void updateTeacher(Teacher teacher) {
		getSession().update(teacher);
	}

	public List<Teacher> findAllTeachers() {
		return getSession().createQuery("from Teacher").list();
	}

	public Teacher findById(Long idTeacher) {
		return getSession().get(Teacher.class, idTeacher);
	}

	public Teacher findByName(String name) {
		return (Teacher) getSession().createQuery(
				"from Teacher where name = :name")
				.setParameter("name", name).uniqueResult();
	}

}
