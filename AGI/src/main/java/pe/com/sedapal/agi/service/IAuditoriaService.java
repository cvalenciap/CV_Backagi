package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.request_objects.AuditoriaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.RespuestaBean;

public interface IAuditoriaService {
	public Auditoria obtenerAuditoriaPorId(Long idAuditoria);

	public List<Auditoria> obtenerListaAuditorias(AuditoriaRequest auditoriaRequest, PageRequest pageRequest);

	public List<Auditoria> obtenerListaAuditoriasPrograma(Long idPrograma);

	public Error getError();

	public Paginacion getPaginacion();

	public boolean eliminarAuditoria(Auditoria auditoria);

	public Auditoria registrarPlanAuditoria(Auditoria auditoria);

	public Auditoria actualizarAuditoria(Auditoria auditoria);

	public void procesarAuditoria(Auditoria auditoria);

	public void procesarDatosAuditoria(Auditoria auditoria);

}
