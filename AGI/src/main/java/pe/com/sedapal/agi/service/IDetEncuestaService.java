package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IDetEncuestaService {
	List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest detEncuestaRequest, PageRequest pageRequest);
	Boolean eliminarDetEncuesta(Long id);
	Paginacion getPaginacion();
	DetEncuesta actualizarDetEncuesta(Long id, DetEncuesta detEncuesta);
	DetEncuesta insertarDetEncuesta(DetEncuesta detEncuesta);
	Error getError();
	
}