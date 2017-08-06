package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Course;

public interface CourseService {
	
	void saveCourse(Course course);
	
	void deleteCourseById(Long IdCourse);
	
	void updateCourse(Course socialMedia);
	
	List<Course> findAllCourses();

	Course findById(long IdCourse);
	
	Course findByName(String name);
	
	List<Course> findByIdTeacher(Long idTeacher);

}
