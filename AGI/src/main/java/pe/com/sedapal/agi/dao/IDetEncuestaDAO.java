package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IDetEncuestaDAO {
	List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest detEncuestaRequest, PageRequest pageRequest);
	DetEncuesta actualizarDetEncuesta(Long id, DetEncuesta detEncuesta);
	Boolean eliminarDetEncuesta(Long id);
	Paginacion getPaginacion();
	Error getError();
	DetEncuesta insertarDetEncuesta(DetEncuesta detEncuesta);

	
	

}
