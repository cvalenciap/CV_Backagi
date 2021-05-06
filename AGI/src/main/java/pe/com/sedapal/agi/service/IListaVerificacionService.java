package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.ListaVerificacionRequisito;
import pe.com.sedapal.agi.model.request_objects.ListaVerificacionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IListaVerificacionService {
	
	public Error getError();
	
	public Paginacion getPaginacion();
	
	public List<ListaVerificacion> consultarListaVerificacion(ListaVerificacionRequest listaVerificacionRequest,PageRequest paginaRequest);
	
	public ListaVerificacion consultarListaVerifcacionPorId(Long idListaVerificacion);
	
	public List<ListaVerificacionRequisito> obtenerRequisitosListaVerificacion(Long idListaVerificacion);
	
	public void actualizarListaVerificacion(ListaVerificacion listaVerificacion);
	
	public boolean actualizarRequisitosListaVerificacion(ListaVerificacionRequisito listaVerificacionRequisito);
	
	public boolean aprobarRechazarListaVerificacion(ListaVerificacion listaVerificacion,int indicador);
}
