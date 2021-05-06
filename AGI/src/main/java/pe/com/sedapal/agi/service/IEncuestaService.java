package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.Encuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.EncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IEncuestaService {
	List<Encuesta> obtenerEncuesta(EncuestaRequest aulaRequest, PageRequest pageRequest);
	List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest detEncuestaRequest, PageRequest pageRequest);
	Encuesta obtenerDatosEncuesta(Long idCurso);
	Boolean eliminarEncuesta(Long id);
	Boolean eliminarEncuestaDet(Long id);	
	Paginacion getPaginacion();
	Encuesta actualizarEncuesta(Long id, Encuesta aula);
	Encuesta insertarEncuesta(Encuesta aula);
	Error getError();
	
}