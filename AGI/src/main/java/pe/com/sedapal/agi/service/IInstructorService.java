package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Instructor;
import pe.com.sedapal.agi.model.request_objects.InstructorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IInstructorService {
	List<Instructor> obtenerInstructor(InstructorRequest instructorRequest, PageRequest pageRequest);
	Boolean eliminarInstructor(Long id);
	Paginacion getPaginacion();
	Instructor actualizarInstructor(Long id, Instructor instructor);
	Instructor insertarInstructor(Instructor instructor);
	Error getError();
	
}