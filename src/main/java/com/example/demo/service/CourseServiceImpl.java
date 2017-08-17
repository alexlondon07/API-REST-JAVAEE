package com.example.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CourseDao;
import com.example.demo.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseDao _courseDao;

	@Override
	public void saveCourse(Course course) {
		// TODO Auto-generated method stub
		_courseDao.saveCourse(course);
	}

	@Override
	public void deleteCourseById(Long IdCourse) {
		// TODO Auto-generated method stub
		_courseDao.deleteCourseById(IdCourse);
	}

	@Override
	public void updateCourse(Course socialMedia) {
		// TODO Auto-generated method stub
		_courseDao.updateCourse(socialMedia);
	}

	@Override
	public List<Course> findAllCourses() {
		// TODO Auto-generated method stub
		return _courseDao.findAllCourses();
	}

	@Override
	public Course findById(long IdCourse) {
		// TODO Auto-generated method stub
		return _courseDao.findById(IdCourse);
	}

	@Override
	public Course findByName(String name) {
		// TODO Auto-generated method stub
		return _courseDao.findByName(name);
	}

	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		return _courseDao.findByIdTeacher(idTeacher);
	}

}
