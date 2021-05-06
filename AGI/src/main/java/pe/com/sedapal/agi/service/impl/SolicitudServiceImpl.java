package pe.com.sedapal.agi.service.impl;

import java.util.List;
import pe.com.sedapal.agi.security.config.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.dao.ISolicitudDAO;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.SolicitudCopia;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.ISolicitudService;

@Service
public class SolicitudServiceImpl implements ISolicitudService{
	
	@Autowired
	SessionInfo session;
	
	
	@Autowired
	private ISolicitudDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}
    //Registro de Solicitud 
	@Override
	public Documento crearSolicitud(SolicitudCopia constante) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//Documento objeto = this.dao.guardarSolicitud( constante, null, iUsuario,idUsuario);
		Documento objeto = this.dao.guardarSolicitudParticipantes(constante,null, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()));
		return objeto;
	}
	//Obetener BITACORA EN SOLICITUD
	@Override
	public List<Revision> obtenerSolicitudBitacora(RevisionRequest revisionRequest, PageRequest pageRequest){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Revision> lista = this.dao.obtenerSolicitud(revisionRequest, pageRequest, new Long(((UserAuth)principal).getCodPerfil()));
		return lista;
	}	
	//Obetener SolicitudDocumento 
		@Override
		public List<Revision> obtenerSolicitudBitacoraDocumento(RevisionRequest revisionRequest, PageRequest pageRequest){
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Revision> lista = this.dao.obtenerSolicitudDocument(revisionRequest, pageRequest, new Long(((UserAuth)principal).getCodPerfil()));
			return lista;
	}
	//Actualizar Solicitud (Aprobado / Rechazado)
	@Override
	public Documento actualizarDocumento(SolicitudCopia constante, Long codigo) {	
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//Documento objeto = this.dao.UpdateSolicitud(constante, codigo, iUsuario,idUsuario);
		Documento objeto = this.dao.ActualizaSolicitud(constante, codigo, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()));
		return objeto;
	}
	//Actualizar Solicitud copia Impresa
	@Override
	public Documento actualizarDocumentoCopiaImpresa(SolicitudCopia constante, Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Documento objeto = this.dao.ActualizaSolicitud(constante, codigo, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()));					
		return objeto;
	}
}