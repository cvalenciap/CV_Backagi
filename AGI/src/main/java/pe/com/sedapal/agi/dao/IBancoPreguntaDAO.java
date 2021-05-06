package pe.com.sedapal.agi.dao;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IBancoPreguntaDAO {
	
	
	public List<Pregunta> ListaBancoPreguntas(Pregunta pregunta, PageRequest pageRequest);

	public Map<String, Object> GuardarBancoPregunta(Pregunta objPregunta);

	//public Map<String, Object> eliminarPregunta(Long codigo);
	public Boolean eliminarPregunta(Long codigo);
	
	public Map<String, Object> ObtenerPregunta(String cod);
		
	public Map<String, Object> ActualizarBancoPregunta(Pregunta pregunta);
	
	public List<Constante> buscarRoles(String descripcion);
	
	public Error getError();  
	
	Paginacion getPaginacion();

	
}

