package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.FichaAuditor;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IFichaAuditorService {
	
	public Paginacion getPaginacion();
	
	public Error getError();

	public Map<String, Object> ListaAuditorBandeja(Map<String, String> requestParm);

	public Map<String, Object> EliminarFicha(Long codigo);

	public Map<String, Object> ListaDatosAuditor(Map<String, String> requestParm);

	public Map<String, Object> ListaConsultaConstante(Map<String, String> requestParm);

	public Map<String, Object> ListaCursosObligatorios(Map<String, String> requestParm);
	
	public List<FichaAuditor> obtenerListaAuditores(AuditorRequest auditoriaRequest, PageRequest paginaRequest);

}
