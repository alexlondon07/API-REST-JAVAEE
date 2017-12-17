package com.example.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.model.Course;
import com.example.demo.model.SocialMedia;
import com.example.demo.model.Teacher;
import com.example.demo.model.TeacherSocialMedia;
import com.example.demo.service.SocialMediaService;
import com.example.demo.service.TeacherService;
import com.example.demo.util.CustomErrorType;


@Controller
@RequestMapping(value="/v1")
public class TeacherController {
	
	public static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private SocialMediaService socialMediaService;
	
	
	// ------------------- GET Teacher----------------------------------------------------------------------------------
	@RequestMapping(value = "/teachers", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required=false) String name){
		List<Teacher> teachers = new ArrayList<Teacher>();
		
		if (name == null) {
			teachers = teacherService.findAllTeachers();
	        if (teachers.isEmpty()) {
	            return new ResponseEntity(HttpStatus.NOT_FOUND);
	        }
	        
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
			
		} else {
			
			Teacher teacher = teacherService.findByName(name);
			if (teacher == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			teachers.add(teacher);
			
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}
    }
	
	// ------------------- GET Teacher Find By Id----------------------------------------------------------------------------------
	@RequestMapping(value = "/teachers/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Teacher> getCourseById(@PathVariable("id") Long id){
		
		Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
    }
	
	
	// ------------------- DELETE Teacher-------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/teachers/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id) {
		
		Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            return new ResponseEntity(new CustomErrorType("Unable to delete. teacher with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        
        teacherService.deleteTeacherById(id);
        
        return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
    }
	
	
	
	//POST
	@RequestMapping(value="/teacher", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, UriComponentsBuilder uriBuilder){
		
		logger.info("Creating Teacher : {}", teacher);
		
		if(teacher.getName() == null || teacher.getName().isEmpty()){
			return new ResponseEntity(new CustomErrorType("Teacher name is required. "), HttpStatus.CONFLICT);
		}
		
		if(isTeacherExist(teacher)){
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Teacher with name " + teacher.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		
		teacherService.saveTeacher(teacher);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriBuilder.path("/v1/teachers/{id}").buildAndExpand(teacher.getIdTeacher()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	
	// ------------------- POST Teacher Create Teacher Image----------------------------------------------------------------------------------
	public static final String TEACHER_UPLOADED_FOLDER ="images/teachers/";
	@RequestMapping(value="/teachers/images", method = RequestMethod.POST, headers=("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id_teacher") Long idTeacher, 
			@RequestParam("file") MultipartFile multipartFile, 
			UriComponentsBuilder componentsBuilder){
		
		if (idTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Please set id_teacher"), HttpStatus.NO_CONTENT);
		}
		
		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}
		
		//Buscamos la información del Teacher
		Teacher teacher = teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not dfound"), HttpStatus.NOT_FOUND);
		}
		
		//Validamos si el avatar no esta vacio o no esta null, Eliminamos la imagen
		if (!teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
			
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			
			if (f.exists()) {
				f.delete();
			}
		}
		
		//Se procede a Subir la imagen
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateName = dateFormat.format(date);
			
			String fileName = String.valueOf(idTeacher) + "-pictureTaecher-" + dateName + "." + multipartFile.getContentType().split("/")[1];
			teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			//Se sube la imagen del teacher
			teacherService.updateTeacher(teacher);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload: " + multipartFile.getOriginalFilename()),HttpStatus.CONFLICT);
		}
	}
	

	// ------------------- Get Image---------------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/teachers/{id_teacher}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id_teacher") Long idTeacher){
		
		if (idTeacher == null) {
			 return new ResponseEntity(new CustomErrorType("Please set id_teacher "), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
		}
		
		try {
			
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if (!f.exists()) {
				return new ResponseEntity(new CustomErrorType("Image not found"),HttpStatus.CONFLICT);
			}
			
			byte[] image = Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error to show image"),HttpStatus.CONFLICT);
		}
	
	}

	// ------------------- Delete Image------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/teachers/{id_teacher}/images", method = RequestMethod.DELETE,headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacherImage(@PathVariable("id_teacher") Long idTeacher){
		
		if (idTeacher == null) {
			 return new ResponseEntity(new CustomErrorType("Please set id_teacher "), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getAvatar().isEmpty() || teacher.getAvatar() == null) {
			 return new ResponseEntity(new CustomErrorType("This Teacher dosen't have image assigned"), HttpStatus.NO_CONTENT);
		}
		
		String fileName = teacher.getAvatar();
		Path path = Paths.get(fileName);
		File file = path.toFile();
		if (file.exists()) {
			file.delete();
		}
		
		teacher.setAvatar("");
		teacherService.updateTeacher(teacher);
		
		return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
	}
	
	// ------------------- 	Assign Teacher Social Medias ----------------------------------------------------------------------------------
	@RequestMapping(value="teachers/socialMedias", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> assignTeacherSocialMedia(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder){
		
		if (teacher.getIdTeacher() == null) {
			return new ResponseEntity(new CustomErrorType("We nee almost id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacherSaved = teacherService.findById(teacher.getIdTeacher());
		if (teacherSaved == null) {
			return new ResponseEntity(new CustomErrorType("The id_teacher: " + teacher.getIdTeacher() + " not found"), HttpStatus.NO_CONTENT);
		}
		
		if (teacher.getTeacherSocialMedias().size() == 0) {
			return new ResponseEntity(new CustomErrorType("We nee almost id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
			
		}else{
			
			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();
			while (i.hasNext()) {
				
				TeacherSocialMedia teacherSocialMedia = i.next();
				
				if (teacherSocialMedia.getSocialMedia().getIdSocialMedia() == null || teacherSocialMedia.getNickname() == null) {
					return new ResponseEntity(new CustomErrorType("We nee almost id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
					
				}else{
					
					//Consultamos si la social Media con Id y nombre Existe en nuestra bd
					TeacherSocialMedia tsmAux = socialMediaService.findSocialMediaByIdAndName(
							teacherSocialMedia.getSocialMedia().getIdSocialMedia(), 
							teacherSocialMedia.getNickname());
					
					if (tsmAux != null) {
						return new ResponseEntity(new CustomErrorType("The id social media " 
						+ teacherSocialMedia.getSocialMedia().getIdSocialMedia() 
						+ " witch nickname: " + teacherSocialMedia.getNickname() 
						+ " already exist "), HttpStatus.NO_CONTENT);
					}
					
					SocialMedia socialMedia = socialMediaService.findById(teacherSocialMedia.getSocialMedia().getIdSocialMedia());
					
					if (socialMedia == null) {
						return new ResponseEntity(new CustomErrorType("The id social media " 
								+ teacherSocialMedia.getSocialMedia().getIdSocialMedia() 
								+ " not found "), HttpStatus.NO_CONTENT);
					}
					
					//Seteamos los datos que estamos obteniendo
					teacherSocialMedia.setSocialMedia(socialMedia);
					teacherSocialMedia.setTeacher(teacherSaved);
					
					if (tsmAux == null) {
						//Agregamos el objeto teacher social media
						teacherSaved.getTeacherSocialMedias().add(teacherSocialMedia);
						
					}else{
						
						LinkedList<TeacherSocialMedia> teacherSocialMedias = new LinkedList<>();
						teacherSocialMedias.addAll(teacherSaved.getTeacherSocialMedias());
						
						for (int j = 0; j < teacherSocialMedias.size(); j++) {
							
							TeacherSocialMedia teacherSocialMedia2 = teacherSocialMedias.get(j);
							
							if (teacherSocialMedia.getTeacher().getIdTeacher() == teacherSocialMedia2.getTeacher().getIdTeacher()
									&& teacherSocialMedia.getSocialMedia().getIdSocialMedia() == teacherSocialMedia2.getSocialMedia().getIdSocialMedia()) {

								//Como existe, procedemos a modificarlo
								teacherSocialMedia2.setNickname(teacherSocialMedia.getNickname());
								teacherSocialMedias.set(j, teacherSocialMedia2);
							}else{
								
								//Si no son iguales, lo añadimos a la lista
								teacherSocialMedias.set(j, teacherSocialMedia2);
							}
						}
						
						teacherSaved.getTeacherSocialMedias().clear();
						teacherSaved.getTeacherSocialMedias().addAll(teacherSocialMedias);
						
					}			
				}
			}
		}
		teacherService.updateTeacher(teacherSaved);
		
		return new ResponseEntity<Teacher>(teacherSaved, HttpStatus.OK);
	}
	
	/**
	 * Metodo para validar si un teacher ya existe en la bd
	 * @param teacher
	 * @return
	 */
	public boolean isTeacherExist(Teacher teacher){
		if(teacherService.findByName(teacher.getName()) !=null){
			logger.error("Unable to create. A Teacher with name {} already exist", teacher.getName());
			return true;
		}
		return false;
	}	
}
