package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IBancoPreguntaService {

	public List<Pregunta> ListaBancoPreguntas(Pregunta pregunta, PageRequest pageRequest);

	public Map<String, Object> GuardarBancoPreguntas(Pregunta pregunta);
	
	public Map<String, Object> ActualizarBancoPreguntas(Pregunta pregunta);

	public Boolean EliminarPregunta(Long codigo);

	public Map<String, Object> ObtenerPreguntaDatos(Map<String, String> requestParm);		
	
	public List<Constante> buscarRoles(String descripcion);
	
	public Error getError();
	
	Paginacion getPaginacion();
	
}
