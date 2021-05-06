package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.INormaDAO;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.INormaService;

@Service
public class NormaServiceImpl implements INormaService {
	
	@Autowired
	private INormaDAO dao;
	
	@Autowired
	SessionInfo session;
	
	@Override
	public Error getError() {
		return dao.getError();
	}
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}
		
	@Override
	public List<Norma> obtenerListaNormas(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return dao.obtenerListaNormas(pageRequest);
	}
	
	@Override
	public Integer actualizarNorma(Norma norma) {
		return this.dao.actualizarNorma(norma);
	}
	
	/*@Override
	public List<Norma> obtenerListaNormasGrilla(NormaRequest normaRequest, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return dao.obtenerListaNormasGrilla(normaRequest,  pageRequest);
	}   

	
	*/
	

	/*@Override
	public List<Requisito> obtenerNormaRequisito(NormaRequest normaRequest) {
		// TODO Auto-generated method stub
		return dao.obtenerNormaRequisito(normaRequest);
	}


	@Override
	public void guardarNormaRequisito(Norma norma) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		norma.setDatosAuditoria(datosAuditoria);
		this.dao.guardarNormaRequisito(norma);
	}
	
	@Override
	public void actualizarNormaRequisito(Long idNorma, Norma norma) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		norma.setDatosAuditoria(datosAuditoria);
		this.dao.actualizarNormaRequisito(idNorma, norma);
		
	}
	

	@Override
	public Norma obtenerDatosNormaId(Long idNorma) {
		return this.dao.obtenerDatosNormaId(idNorma);
	}


	@Override
	public Boolean eliminarNorma(Long id) {
		return this.dao.eliminarNorma(id);
	}

*/
	




}
