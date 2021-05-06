package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.request_objects.ListaVerificacionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IRevisionHallazgosService {
	
	public Error getError();
	
	public Paginacion getPaginacion();
	
	public List<ListaVerificacion> obtenerRevisionHallazgos(ListaVerificacionRequest listaVerificacionRequest,PageRequest paginaRequest);
	
	public List<CriterioResultado> obtenerCriteriosCalificacion(Long idListaVerificacion);
	
	public void actualizarListaVerificacionHallazgos(ListaVerificacion listaVerificacion);
	
	public boolean aprobarRechazarRevisionHallazgos(ListaVerificacion listaVerificacion, int indicador);
}
