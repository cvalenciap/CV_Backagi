package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Instructor;
import pe.com.sedapal.agi.model.request_objects.InstructorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IInstructorDAO {
	List<Instructor> obtenerInstructor(InstructorRequest instructorRequest, PageRequest pageRequest);
	Instructor actualizarInstructor(Long id, Instructor instructor);
	Boolean eliminarInstructor(Long id);
	Paginacion getPaginacion();
	Error getError();
	Instructor insertarInstructor(Instructor instructor);

	
	

}
