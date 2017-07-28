package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Teacher;



public class TeacherDaoImpl implements TeacherDao {
	
	private PlatziSession platziSession;
	

	public TeacherDaoImpl() {
		platziSession = new PlatziSession();
	}

	public void saveTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		platziSession.getSession().persist(teacher);
		platziSession.getSession().getTransaction().commit();
	}

	public void deleteTeacherById(Long idTeacher) {
		// TODO Auto-generated method stub
		
	}

	public void updateTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		
	}

	public List<Teacher> findAllTeachers() {
		// TODO Auto-generated method stub
		return platziSession.getSession().createQuery("from Teacher").list();
	}

	public Teacher findById(Long idTeacher) {
		// TODO Auto-generated method stub
		return null;
	}

	public Teacher findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
