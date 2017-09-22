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

import com.example.demo.model.Teacher;
import com.example.demo.service.TeacherService;

@Controller
@RequestMapping(value="/v1")
public class TeacherController {

	@Autowired
	private TeacherService _teacherService;
	
	// ------------------- GET Teacher-----------------------------------------
	@RequestMapping(value = "/teachers", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required = false) String name, @RequestParam(value = "id_teacher", required = false) Long id_teacher){
		
		List<Teacher> teachers = new ArrayList<Teacher>();
		
		if(name != null){
			teachers = _teacherService.findAllTeachers();
			if(teachers.isEmpty()){
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		}
		
		return null;
		
	}
	
	// ------------------- POST Teacher-----------------------------------------
	
	// ------------------- UPDATE Teacher-----------------------------------------
	
	// ------------------- DELETE Teacher-----------------------------------------
	
}
