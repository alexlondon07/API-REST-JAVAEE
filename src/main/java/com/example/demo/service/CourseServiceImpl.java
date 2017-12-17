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
	private CourseDao courseDao;

	@Override
	public void saveCourse(Course course) {
		courseDao.saveCourse(course);
	}

	@Override
	public void deleteCourseById(Long idCourse) {
		courseDao.deleteCourseById(idCourse);
	}

	@Override
	public void updateCourse(Course socialMedia) {
		courseDao.updateCourse(socialMedia);
	}

	@Override
	public List<Course> findAllCourses() {
		return courseDao.findAllCourses();
	}

	@Override
	public Course findById(long idCourse) {
		return courseDao.findById(idCourse);
	}

	@Override
	public Course findByName(String name) {
		return courseDao.findByName(name);
	}

	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		return courseDao.findByIdTeacher(idTeacher);
	}

}
