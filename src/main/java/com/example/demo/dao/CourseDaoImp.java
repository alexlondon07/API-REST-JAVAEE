package com.example.demo.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Course;

@Repository
@Transactional
public class CourseDaoImp extends AbstractSession implements CourseDao {

	public CourseDaoImp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveCourse(Course course) {
		getSession().persist(course);
	}

	@Override
	public void deleteCourseById(Long IdCourse) {
		Course course = findById(IdCourse);
		if(IdCourse !=null){
			getSession().delete(course);
		}		
	}

	@Override
	public void updateCourse(Course course) {
		getSession().update(course);
	}

	@Override
	public List<Course> findAllCourses() {
		return getSession().createQuery("from Course").list();
	}

	@Override
	public Course findById(long IdCourse) {
		return getSession().get(Course.class, IdCourse);
	}

	@Override
	public Course findByName(String name) {
		return (Course) getSession().createQuery(
				"from Course where name :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		return (List<Course>) getSession().createQuery(
				"from Course c join c.teacher t where t.idTeacher = :idTeacher")
				.setParameter("idTeacher", idTeacher).list();
	}

}
