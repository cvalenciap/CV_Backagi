package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IListaVerificacionDAO;
import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.HallazgoRequisito;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.ListaVerificacionAuditado;
import pe.com.sedapal.agi.model.ListaVerificacionRequisito;
import pe.com.sedapal.agi.model.request_objects.ListaVerificacionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IRevisionHallazgosService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.security.config.UserAuth;
@Service
public class RevisionHallazgosServiceImpl implements IRevisionHallazgosService {
	
	@Autowired
	SessionInfo session;
	
	private static final Logger LOGGER = Logger.getLogger(RevisionHallazgosServiceImpl.class);	
	
	@Autowired IListaVerificacionDAO dao;
	
	
	
	@Override
	public List<ListaVerificacion> obtenerRevisionHallazgos(ListaVerificacionRequest listaVerificacionRequest,
			PageRequest paginaRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerRevisionHallazgos(listaVerificacionRequest, paginaRequest);
	}

	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}

	@Override
	public List<CriterioResultado> obtenerCriteriosCalificacion(Long idListaVerificacion) {
		// TODO Auto-generated method stub
		return this.dao.obtenerCriteriosCalificacion(idListaVerificacion);
	}

	@Override
	public void actualizarListaVerificacionHallazgos(ListaVerificacion listaVerificacion) {
		try {
			DatosAuditoria datosAuditoria = new DatosAuditoria();
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
			datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
			listaVerificacion.setDatosAuditoria(datosAuditoria);
			boolean registroLV = this.dao.actualizarListaVerificacion(listaVerificacion,2);
			
			if(registroLV) {
				for(ListaVerificacionRequisito requisito:listaVerificacion.getListaRequisitosLV()) {
					boolean registroHallazgo = true;
					requisito.setDatosAuditoria(listaVerificacion.getDatosAuditoria());
					if(requisito.getValorCalificacion()!=null) {
						if(requisito.getValorCalificacion().equals("01")) {
							requisito.setDescripcionCalificacion(null);
						}
						boolean registro = this.dao.actualizarRequisitosListaVerificacion(requisito, 2);
						System.out.println(registro);
						if(!registro) {
							break;
						}else {
							if(requisito.getValorCalificacion().equals("01")) {
								if(requisito.getHallazgo().getIdHallazgo()!=null) {
									HallazgoRequisito hallazgo = requisito.getHallazgo();
									hallazgo.setIdLVRequisito(requisito.getIdLVRequisito());
									hallazgo.setDatosAuditoria(requisito.getDatosAuditoria());
									registroHallazgo = this.dao.actualizarHallazgoRequisito(hallazgo);
								}else {
									Long idHallazgo = this.dao.buscarHallazgoRequisito(requisito.getIdLVRequisito());
									System.out.println(idHallazgo);
									if(!idHallazgo.equals(new Long(0))) {
										HallazgoRequisito hallazgo = requisito.getHallazgo();
										hallazgo.setIdLVRequisito(requisito.getIdLVRequisito());
										hallazgo.setDatosAuditoria(requisito.getDatosAuditoria());
										hallazgo.setIdHallazgo(idHallazgo);
										registroHallazgo = this.dao.actualizarHallazgoRequisito(hallazgo);
									}else {
										HallazgoRequisito hallazgo = requisito.getHallazgo();
										hallazgo.setIdLVRequisito(requisito.getIdLVRequisito());
										hallazgo.setDatosAuditoria(requisito.getDatosAuditoria());
										registroHallazgo = this.dao.registrarHallazgoRequisito(hallazgo);
									}
									
									
								}
								
							}else {
								if(requisito.getHallazgo().getIdHallazgo()!=null) {
									HallazgoRequisito hallazgo = requisito.getHallazgo();
									hallazgo.setDatosAuditoria(requisito.getDatosAuditoria());
									registroHallazgo = this.dao.eliminarHallazgoRequisito(hallazgo);
								}
							}
						}
					}
					if(!registroHallazgo) {
						break;	
					}
					
				}
				
				for(ListaVerificacionAuditado auditado:listaVerificacion.getListaAuditadosLV()) {
					boolean registro = true;
					if(auditado.getEstadoRegistro().equals("1")) {
						if(auditado.getIdListVeriAuditado()==null) {
							auditado.setDatosAuditoria(listaVerificacion.getDatosAuditoria());
							auditado.setIdListVeri(listaVerificacion.getIdListaVerificacion());
							registro = this.dao.registrarListaVerificacionAuditado(auditado);
						}
						
						
					}else if(auditado.getEstadoRegistro().equals("0")) {
						if(auditado.getIdListVeriAuditado()!=null) {
							auditado.setDatosAuditoria(listaVerificacion.getDatosAuditoria());
							registro = this.dao.eliminarListaVerificacionAuditado(auditado);
						}
						
					}
					
					if(!registro) {
						break;
					}
					
					
				}
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionHallazgosServiceImpl.actualizarListaVerificacionHallazgos";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		
	}

	@Override
	public boolean aprobarRechazarRevisionHallazgos(ListaVerificacion listaVerificacion, int indicador) {
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
		listaVerificacion.setDatosAuditoria(datosAuditoria);
		
		return this.dao.aprobarRechazarRevisionHallazgos(listaVerificacion,indicador);
	}
}
