package pe.com.sedapal.agi.service.impl;

import static java.text.MessageFormat.format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IAuditoriaDAO;
import pe.com.sedapal.agi.dao.IListaVerificacionDAO;
import pe.com.sedapal.agi.dao.IRequisitoDAO;
import pe.com.sedapal.agi.model.AuditorAuditoria;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.ConsideracionPlan;
import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.DetalleAuditoria;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.ListaVerificacionAuditor;
import pe.com.sedapal.agi.model.ListaVerificacionRequisito;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.RequisitoAuditoriaDetalle;
import pe.com.sedapal.agi.model.request_objects.AuditoriaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IAuditoriaService;
import pe.com.sedapal.agi.util.Constantes;
import pe.com.sedapal.agi.util.RespuestaBean;
import pe.com.sedapal.agi.util.RespuestaBean;

@Service
public class AuditoriaServiceImpl implements IAuditoriaService {

	private static final Logger LOGGER = Logger.getLogger(AuditoriaServiceImpl.class);

	RespuestaBean respuesta = new RespuestaBean();

	@Autowired
	IAuditoriaDAO dao;

	@Autowired
	IListaVerificacionDAO daoListaVerificacion;

	@Override
	public List<Auditoria> obtenerListaAuditorias(AuditoriaRequest auditoriaRequest, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerListaAuditorias(auditoriaRequest, pageRequest);
	}

	@Override
	public List<Auditoria> obtenerListaAuditoriasPrograma(Long idPrograma) {
		List<Auditoria> listaAuditoria = this.dao.obtenerListaAuditoriasPrograma(idPrograma);
		for (Auditoria auditoria : listaAuditoria) {
			List<Norma> listaNormas = this.dao.obtenerNormasAuditoria(auditoria.getIdAuditoria());
			auditoria.setListaNormas(listaNormas);
		}
		return listaAuditoria;
	}

	@Override
	public Auditoria obtenerAuditoriaPorId(Long idAuditoria) {
		Auditoria auditoria = this.dao.obtenerAuditoriaPorId(idAuditoria);
		List<Norma> listaNormas = this.dao.obtenerNormasAuditoria(auditoria.getIdAuditoria());

		List<CriterioResultado> listaCriterios = this.dao.obtenerListaCriteriosAuditoria(idAuditoria);
		List<ConsideracionPlan> listaConsideraciones = this.dao.obtenerListaConsideracionesAuditoria(idAuditoria);
		List<DetalleAuditoria> listaDetalleAuditoria = this.dao.obtenerDetalleAuditoria(idAuditoria);
		auditoria.setListaNormas(listaNormas);
		auditoria.setListaCriterios(listaCriterios);
		auditoria.setListaConsideracionesPlan(listaConsideraciones);
		auditoria.setListaDetalle(listaDetalleAuditoria);
		return auditoria;
	}

	@Override
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}

	@Override
	public boolean eliminarAuditoria(Auditoria auditoria) {
		// TODO Auto-generated method stub
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion((((UserAuth) principal).getUsername()));
		auditoria.setDatosAuditoria(datosAuditoria);
		return this.dao.eliminarAuditoriaExterna(auditoria);
	}

	@Override
	public Auditoria registrarPlanAuditoria(Auditoria auditoria) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion(((UserAuth) principal).getUsername());
		auditoria.setDatosAuditoria(datosAuditoria);
		return this.dao.registrarDatosPlanAuditoria(auditoria);
	}

	@Override
	public Auditoria actualizarAuditoria(Auditoria auditoria) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion((((UserAuth) principal).getUsername()));
		datosAuditoria.setUsuarioModificacion((((UserAuth) principal).getUsername()));
		auditoria.setDatosAuditoria(datosAuditoria);

		return this.dao.actualizarDatosPlanAuditoria(auditoria);
	}

	@Override
	public void procesarAuditoria(Auditoria auditoria) {
		auditoria.setCicloAuditoria("3");// Cambio de estado de Auditoria
		Auditoria auditoriaRegistro = this.actualizarAuditoria(auditoria);
		this.procesarDatosAuditoria(auditoria);
	}

	@Override
	public void procesarDatosAuditoria(Auditoria auditoria) {
		if (auditoria.getListaDetalle().size() > 0) {
			for (DetalleAuditoria detalle : auditoria.getListaDetalle()) {
				detalle.setDatosAuditoria(auditoria.getDatosAuditoria());
				ListaVerificacion listaVerificacionRegistro = this.convertirDetalleAuditoria(detalle);
				listaVerificacionRegistro.setTipoLV("1");
				listaVerificacionRegistro.setEstadoListaVerificacion("1");
				listaVerificacionRegistro = this.daoListaVerificacion
						.registrarListaVerificacion(listaVerificacionRegistro);
				if (listaVerificacionRegistro != null) {
					if (detalle.getListaRequisitos().size() > 0) {
						for (RequisitoAuditoriaDetalle requisitoDetalle : detalle.getListaRequisitos()) {
							requisitoDetalle.setDatosAuditoria(detalle.getDatosAuditoria());
							requisitoDetalle.setIdDetalleAuditoria(detalle.getIdDetalleAuditoria());
							ListaVerificacionRequisito listaVerificacionRequisito = convertirRequisitoDetalle(
									requisitoDetalle);
							listaVerificacionRequisito
									.setIdListaVerificacion(listaVerificacionRegistro.getIdListaVerificacion());
							boolean registroRequisito = this.daoListaVerificacion
									.registrarListaVerificacionRequisito(listaVerificacionRequisito);
							if (!registroRequisito) {
								break;
							}
						}
					}

					if (detalle.getListaParticipante().size() > 0) {
						for (AuditorAuditoria auditorAuditoria : detalle.getListaParticipante()) {
							auditorAuditoria.setDatosAuditoria(detalle.getDatosAuditoria());
							ListaVerificacionAuditor listaVerificacionAuditor = convertirAuditorAuditoria(
									auditorAuditoria);
							listaVerificacionAuditor
									.setIdListaVerificacion(listaVerificacionRegistro.getIdListaVerificacion());
							boolean registroAuditor = this.daoListaVerificacion
									.registrarListaVerificacionAuditor(listaVerificacionAuditor);
							if (!registroAuditor) {
								break;

							}
						}
					}

				}
			}
		}

	}

	private ListaVerificacion convertirDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		ListaVerificacion listaVerificacion = new ListaVerificacion();
		listaVerificacion.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
		listaVerificacion.setIdAuditoria(detalleAuditoria.getIdAuditoria());
		listaVerificacion.setFecha(detalleAuditoria.getFecha());

		switch (detalleAuditoria.getValorTipoEntidad()) {
		case "1":
			listaVerificacion.setCodigoGerencia(detalleAuditoria.getValorEntidadGerencia());
			break;
		case "2":
			listaVerificacion.setCodigoEquipo(detalleAuditoria.getValorEntidadEquipo());
			break;
		case "3":
			listaVerificacion.setCodigoCargo(detalleAuditoria.getValorEntidadCargo());
			break;
		case "4":
			listaVerificacion.setCodigoComite(detalleAuditoria.getValorEntidadComite());
			break;
		}
		return listaVerificacion;
	}

	private ListaVerificacionRequisito convertirRequisitoDetalle(RequisitoAuditoriaDetalle requisitoDetalle) {
		ListaVerificacionRequisito listaVerificacionRequisito = new ListaVerificacionRequisito();
		listaVerificacionRequisito.setDatosAuditoria(requisitoDetalle.getDatosAuditoria());
		listaVerificacionRequisito.setIdRequisito(requisitoDetalle.getId());
		listaVerificacionRequisito.setIdNorma(requisitoDetalle.getIdNorma());
		listaVerificacionRequisito.setIdDetalleRequisito(requisitoDetalle.getIdDetalleAuditoria());
		return listaVerificacionRequisito;
	}

	private ListaVerificacionAuditor convertirAuditorAuditoria(AuditorAuditoria auditorAuditoria) {
		ListaVerificacionAuditor listaVerificacionAuditor = new ListaVerificacionAuditor();
		listaVerificacionAuditor.setDatosAuditoria(auditorAuditoria.getDatosAuditoria());
		listaVerificacionAuditor.setIdAuditor(auditorAuditoria.getIdAuditor());
		listaVerificacionAuditor.setIdRolAuditor(auditorAuditoria.getIdRolAuditor());
		return listaVerificacionAuditor;
	}
}
