package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.model.Course;
import com.example.demo.service.CourseService;
import com.example.demo.util.CustomErrorType;


@Controller
@RequestMapping("/v1")
public class CourseController {
	
	public static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired
	CourseService courseService;
	
	
	@RequestMapping(value="/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name, @RequestParam(value = "id_teacher", required = false) Long idTeacher){
		
		List<Course> courses = new ArrayList<>();
		
		if(idTeacher !=null){
			courses = courseService.findByIdTeacher(idTeacher);
			if(courses.isEmpty()){
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
		}
		
		if(name != null){
			Course course = courseService.findByName(name);
			if(course == null){
				return new ResponseEntity(new CustomErrorType("Course name " + name + " not found "), HttpStatus.NOT_FOUND);
			}
			courses.add(course);
		}
		
		if(name == null && idTeacher == null){
			courses = courseService.findAllCourses();
			if(courses.isEmpty()){
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
		}
		
		return new ResponseEntity(courses, HttpStatus.OK);
		
	}
	
	
	// ------------------- POST Courses----------------------------------------------------------------------------------
	@RequestMapping(value="/courses", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriBuilder){
		logger.info("Creating Course : {}", course);
		
		if(course.getName() == null || course.getName().isEmpty()){
			return new ResponseEntity(new CustomErrorType("Course name is required. "), HttpStatus.CONFLICT);
		}
		
		if(isCourseExist(course)){
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Course with name " + course.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		
		courseService.saveCourse(course);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriBuilder.path("/v1/courses/{id}").buildAndExpand(course.getIdCourse()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	

	// ------------------- UPDATE Courses----------------------------------------------------------------------------------
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Course> updateCouse(@PathVariable("id") Long id, @RequestBody Course course){
		
		logger.info("Updating Course with id {}", id);
		
		if (id == null || id <= 0) {
			return new ResponseEntity(new CustomErrorType("idCourse is required"), HttpStatus.CONFLICT);
		}
		
		if(course.getName().equals(null) || course.getName().isEmpty()){
			return new ResponseEntity(new CustomErrorType("Course name is required. "), HttpStatus.CONFLICT);
		}
				
		if(isCourseExist(course)){
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Course with name " + course.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		
		Course currentCourse = courseService.findById(id);
		if(currentCourse == null){
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Course with id " + id + " already exist."),
					HttpStatus.CONFLICT);
		}
		
		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getThemes());
		currentCourse.setProject(course.getProject());
		
		courseService.updateCourse(currentCourse);
		return new ResponseEntity<Course>(currentCourse, HttpStatus.OK);

	}
	
	
	// ------------------- DELETE Courses----------------------------------------------------------------------------------
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id){
		
		logger.info("fetching % Deleting Course with id {} ", id);
		
		if (id == null || id <= 0) {
			return new ResponseEntity(new CustomErrorType("idCourse is required"), HttpStatus.CONFLICT);
		}
		
		Course course = courseService.findById(id);
		
		if(course == null){
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. A Course with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		
		courseService.deleteCourseById(id);
		return new ResponseEntity<Course>(HttpStatus.OK);
		
	}
	
	/**
	 * Validate if exists a Course
	 * @param course
	 * @return
	 */
	public boolean isCourseExist(Course course){
		if(courseService.findByName(course.getName()) !=null){
			logger.error("Unable to create. A Course with name {} already exist", course.getName());
			return true;
		}
		return false;
	}	

}
