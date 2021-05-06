package pe.com.sedapal.agi.dao;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.DeteccionHallazgos;
import pe.com.sedapal.agi.model.request_objects.DeteccionHallazgosRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IDeteccionHallazgosDAO {


	public List<DeteccionHallazgos> obtenerListaDeteccionHallazgos(DeteccionHallazgosRequest deteccionHallazgosRequest,PageRequest paginaRequest);

	public Map<String, Object> ListaConstantes(String listaConstante);
	
	public Error getError();
	
	public Paginacion getPaginacion();
	
	public boolean registrarDatosDeteccionHallazgos(DeteccionHallazgos deteccion);
	
	public DeteccionHallazgos registrarDeteccionHallazgos(DeteccionHallazgos deteccion);
	
	public boolean registrarRequisitoDeteccion(DeteccionHallazgos deteccion);
	
	public boolean registrarDetectorDeteccionHallazgos(DeteccionHallazgos deteccion);
}
