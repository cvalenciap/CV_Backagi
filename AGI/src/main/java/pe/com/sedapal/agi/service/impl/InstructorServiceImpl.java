package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IInstructorDAO;
import pe.com.sedapal.agi.model.Instructor;
import pe.com.sedapal.agi.model.request_objects.InstructorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IInstructorService;

@Service
public class InstructorServiceImpl implements IInstructorService{
	@Autowired
	private IInstructorDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Instructor> obtenerInstructor(InstructorRequest constanteRequest, PageRequest pageRequest) {
		List<Instructor> listaInstructor = new ArrayList<>();
		listaInstructor = this.dao.obtenerInstructor(constanteRequest, pageRequest);
		return listaInstructor;
	}
	
	@Override
	public Boolean eliminarInstructor(Long id) {
		return this.dao.eliminarInstructor(id);
	}
	
	@Override
	public Instructor actualizarInstructor(Long id, Instructor instructor) {
		Instructor item;
		item = this.dao.actualizarInstructor(id, instructor);
		return item;
	}
	@Override
	public Instructor insertarInstructor(Instructor instructor) {
		Instructor item;
		item = this.dao.insertarInstructor(instructor);
		return item;
	}
	

	
}
