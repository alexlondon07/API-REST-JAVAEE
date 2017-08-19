package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Course;
import com.example.demo.service.CourseService;


@Controller
@RequestMapping("/v1")
public class CourseController {
	
	@Autowired
	CourseService _courseService;
	
	//GET
	@RequestMapping(value="/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name){
		
		List<Course> courses = new ArrayList<>();
		
		if(name == null){
			courses = _courseService.findAllCourses();
			
			if(courses.isEmpty()){
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		}else{
			Course course = _courseService.findByName(name);
			if(course == null){
				return new ResponseEntity("Course not found", HttpStatus.NOT_FOUND);
			}
			courses.add(course);
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		}
	}
	
	
	//POST
	
	//UPDATE
	
	//DELETE
	

}
