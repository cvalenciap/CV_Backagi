package pe.com.sedapal.agi.dao;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.FichaAuditor;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;



public interface IFichaAuditorDAO {
	
	public Paginacion getPaginacion();
	
	public Error getError();
	
	Map<String, Object> obtenerListaFichaAuditor(String iavanzada, String iNumFicha, String iNombreAuditor, String iApePaternoAuditor, String iApeMaternoAuditor, PageRequest pageRequest);

	Map<String, Object> eliminarFichaAuditor(Long codigo);

	Map<String, Object> listaConsultaConstate(Long iCodConstante);

	Map<String, Object> cargaCursosObligatorios(Long iCodConstante, Long iObligatorio);
	
	public List<FichaAuditor> obtenerListaAuditores(AuditorRequest auditoriaRequest,PageRequest paginaRequest);
		
}
