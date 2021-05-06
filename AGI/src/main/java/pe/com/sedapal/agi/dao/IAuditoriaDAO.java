package pe.com.sedapal.agi.dao;

import java.util.List;
import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.model.AuditorAuditoria;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.ConsideracionPlan;
import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.DetalleAuditoria;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.RequisitoAuditoriaDetalle;
import pe.com.sedapal.agi.model.request_objects.AuditoriaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.RespuestaBean;

public interface IAuditoriaDAO {

	public Auditoria obtenerAuditoriaPorId(Long idAuditoria);

	public List<Auditoria> obtenerListaAuditorias(AuditoriaRequest auditoriaRequest, PageRequest pageRequest);

	public List<Auditoria> obtenerListaAuditoriasPrograma(Long idPrograma);

	public Error getError();

	public Paginacion getPaginacion();

	public boolean registrarNormaAuditoria(Norma norma, Auditoria auditoria);

	public List<Norma> obtenerNormasAuditoria(Long idAuditoria);

	public boolean eliminarAuditoria(Auditoria auditoria);

	public boolean eliminarNormasAuditoria(Auditoria auditoria);

	public void actualizarAuditoriasPrograma(Long idPrograma, String estado, String usuario);

	public Auditoria registrarPlanAuditoria(Auditoria auditoria);

	public Auditoria actualizarAuditoria(Auditoria auditoria);

	public Auditoria registrarDatosPlanAuditoria(Auditoria auditoria);

	public boolean registrarConsideracionesAuditoria(ConsideracionPlan consideracion);

	public boolean registrarCriteriosAuditoria(CriterioResultado criterio);

	public List<ConsideracionPlan> obtenerListaConsideracionesAuditoria(Long idAuditoria);

	public List<CriterioResultado> obtenerListaCriteriosAuditoria(Long idAuditoria);

	public Auditoria actualizarDatosPlanAuditoria(Auditoria auditoria);

	public boolean eliminarConsideracion(ConsideracionPlan consideracion);

	public boolean eliminarCriterio(CriterioResultado criterio);

	public boolean eliminarAuditoriaExterna(Auditoria auditoria);

	public boolean eliminarCriteriosAuditoria(Auditoria auditoria);

	public boolean eliminarConsideracionesAuditoria(Auditoria auditoria);

	public DetalleAuditoria registrarDetalleAuditoria(DetalleAuditoria detalleAuditoria);

	public DetalleAuditoria registrarDatosDetalleAuditoria(DetalleAuditoria detalleAuditoria);

	public boolean registrarRequisitoAuditoriaDetalle(RequisitoAuditoriaDetalle requisitoAuditoria);

	public boolean registrarAuditorAuditoriaDetalle(AuditorAuditoria auditorAuditoria);

	public List<DetalleAuditoria> obtenerDetalleAuditoria(Long idAuditoria);

	public List<DetalleAuditoria> obtenerListaDetalleAuditoria(Long idAuditoria);

	public List<AuditorAuditoria> obtenerAuditoresDetalleAuditoria(Long idDetalleAuditoria);

	public List<RequisitoAuditoriaDetalle> obtenerRequisitosDetalleAuditoria(Long idDetalleAuditoria);

	public DetalleAuditoria actualizarDatosDetalleAuditoria(DetalleAuditoria detalleAuditoria);

	public DetalleAuditoria actualizarDetalleAuditoria(DetalleAuditoria detalleAuditoria);

	public boolean eliminarRequisitoAuditoriaDetalle(RequisitoAuditoriaDetalle requisitoAuditoria);

	public boolean actualizarAuditorAuditoriaDetalle(AuditorAuditoria auditorAuditoria);

	public boolean eliminarAuditorAuditoriaDetalle(AuditorAuditoria auditorAuditoria);

	public boolean eliminarDatosDetalleAuditoria(DetalleAuditoria detalleAuditoria);

	public boolean eliminarDetalleAuditoria(DetalleAuditoria detalleAuditoria);

	public boolean eliminarRequisitosDeDetalleAuditoria(DetalleAuditoria detalleAuditoria);

	public boolean eliminarAuditoresDeDetalleAuditoria(DetalleAuditoria detalleAuditoria);

}
