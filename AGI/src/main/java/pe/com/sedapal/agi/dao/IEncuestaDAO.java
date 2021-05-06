package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.Encuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.EncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IEncuestaDAO {
	
	Paginacion getPaginacion();
	Error getError();
	
	List<Encuesta> obtenerEncuesta(EncuestaRequest encuestaRequest, PageRequest pageRequest);
	List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest detEncuestaRequest, PageRequest pageRequest);
	List<DetEncuesta> obtenerListaEncuestaDetXEncu(Long idEncu);
	Encuesta obtenerListaEncuestaAll(Long idCurso);
	Encuesta obtenerListaEncuestaXId(Long idCurso);
	
	Encuesta insertarEncuesta(Encuesta encuesta);
	DetEncuesta insertarDetEncuesta(DetEncuesta detEncuesta, Long idEncu);
	Encuesta registrarEncuesta(Encuesta encuesta);
		
	Encuesta actualizarEncuesta(Long id, Encuesta encuesta);
	DetEncuesta actualizarDetEncuesta(Long id, Long idEncu,DetEncuesta detEncuesta);
	Encuesta actualizarEncuestaAll(Long id, Encuesta encuesta);
	
	Boolean eliminarDetEncuesta(Long id);
	Boolean eliminarEncuesta(Long id);
	Boolean eliminarEncuestaAll(Long id);
	Boolean eliminarEncuestaDet(Long id);
	
	

	
	

}
