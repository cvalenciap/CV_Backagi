package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.HallazgoRequisito;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.ListaVerificacionAuditado;
import pe.com.sedapal.agi.model.ListaVerificacionAuditor;
import pe.com.sedapal.agi.model.ListaVerificacionRequisito;
import pe.com.sedapal.agi.model.request_objects.ListaVerificacionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IListaVerificacionDAO {
	public Error getError();
	
	public Paginacion getPaginacion();
	
	public ListaVerificacion registrarListaVerificacion(ListaVerificacion listaVerificacion);
	
	public boolean registrarListaVerificacionRequisito(ListaVerificacionRequisito listaVerificacionRequisito);
	
	public boolean registrarListaVerificacionAuditor(ListaVerificacionAuditor listaVerificacionAuditor);
	
	public List<ListaVerificacion> consultarListaVerificacion(ListaVerificacionRequest listaVerificacionRequest,
			PageRequest paginaRequest);
	
	public ListaVerificacion consultarListaVerifcacionPorId(Long idListaVerificacion);
	
	public List<ListaVerificacionRequisito> obtenerRequisitosListaVerificacion(Long idListaVerificacion);
	
	public boolean actualizarRequisitosListaVerificacion(ListaVerificacionRequisito listaVerificacionRequisito,int indicador);
	
	public boolean actualizarListaVerificacion(ListaVerificacion listaVerificacion,int indicador);
	
	public boolean registrarListaVerificacionAuditado(ListaVerificacionAuditado listaVerificacionAuditado);
	
	public List<ListaVerificacionAuditado> obtenerAuditadosListaVerificacion(Long idListaVerificacion);
	
	public boolean eliminarListaVerificacionAuditado(ListaVerificacionAuditado listaVerificacionAuditado);
	
	public boolean aprobarRechazarListaVerificacion(ListaVerificacion listaVerificacion, int indicador);
	
	public List<ListaVerificacion> obtenerRevisionHallazgos(ListaVerificacionRequest listaVerificacionRequest,
			PageRequest paginaRequest);
	
	public List<CriterioResultado> obtenerCriteriosCalificacion(Long idListaVerificacion);
	
	public boolean registrarHallazgoRequisito(HallazgoRequisito hallazgo);
	
	public boolean actualizarHallazgoRequisito(HallazgoRequisito hallazgo);
	
	public boolean eliminarHallazgoRequisito(HallazgoRequisito hallazgo);
	
	public Long buscarHallazgoRequisito(Long idRequisitoLV);
	
	public boolean aprobarRechazarRevisionHallazgos(ListaVerificacion listaVerificacion, int indicador);
	
}
