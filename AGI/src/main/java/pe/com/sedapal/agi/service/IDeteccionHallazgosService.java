package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.DeteccionHallazgos;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.request_objects.DeteccionHallazgosRequest;
import pe.com.sedapal.agi.model.request_objects.NormaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IDeteccionHallazgosService {
	

	public List<DeteccionHallazgos> obtenerListaDeteccionHallazgos(DeteccionHallazgosRequest deteccionHallazgosRequest,PageRequest paginaRequest);

	public Map<String, Object> obtenerListaConstantes(Map<String, String> requestParm);		
	
	public Error getError();
	
	public Paginacion getPaginacion();	
	
	public boolean registrarDatosDeteccionHallazgos(DeteccionHallazgos deteccion);

}
