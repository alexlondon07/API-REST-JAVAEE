package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Course;
import com.example.demo.service.CourseService;
import com.example.demo.util.CustomErrorType;


@Controller
@RequestMapping("/v1")
public class CourseController {
	public static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired
	CourseService _courseService;
	
	// ------------------- GET Courses-----------------------------------------
	
	@RequestMapping(value="/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name, @RequestParam(value = "id_teacher", required = false) Long id_teacher){
		
		List<Course> courses = new ArrayList<>();
		
		if(id_teacher !=null){
			courses = _courseService.findByIdTeacher(id_teacher);
			if(courses.isEmpty()){
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		}
		
		if(name != null){
			Course course = _courseService.findByName(name);
			if(course == null){
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			courses.add(course);
		}
		
		if(name == null && id_teacher == null){
			courses = _courseService.findAllCourses();
			if(courses.isEmpty()){
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		}
		
		return new ResponseEntity(courses, HttpStatus.OK);
		
	}
	
	// ------------------- POST Courses-----------------------------------------
	
	
	
	
	
	
	//UPDATE
	
	//DELETE
	

}
