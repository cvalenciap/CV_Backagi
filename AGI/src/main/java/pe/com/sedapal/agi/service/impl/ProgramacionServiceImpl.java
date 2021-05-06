package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IAuditoriaDAO;
import pe.com.sedapal.agi.dao.IProgramacionDAO;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ProgramaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IProgramacionService;

@Service
public class ProgramacionServiceImpl implements IProgramacionService{
	
	@Autowired
	SessionInfo session;
	
		
	@Autowired IProgramacionDAO dao;
	@Autowired IAuditoriaDAO daoAuditoria;

	@Override
	public List<Programa> obtenerProgramas(ProgramaRequest programaRequest, PageRequest paginaRequest) {
		return this.dao.obtenerProgramas(programaRequest, paginaRequest);
	}
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}
	@Override
	public Boolean eliminarPrograma(Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Auditoria> listaAuditoriasPrograma = this.daoAuditoria.obtenerListaAuditoriasPrograma(codigo);
		return this.dao.eliminarDatosPrograma(codigo, (((UserAuth)principal).getUsername()),listaAuditoriasPrograma);
	}
	
	@Override
	public void registrarDatosPrograma(Programa programa, List<Auditoria> listaAuditorias) {
		this.dao.registrarDatosPrograma(programa, listaAuditorias);
		
	}
	@Override
	public Programa obtenerProgramaPorId(Long idPrograma) {
		return this.dao.obtenerProgramaPorId(idPrograma);
	}
	@Override
	public void actualizarDatosPrograma(Programa programa, List<Auditoria> listaAuditoriasEliminadas,
			List<Auditoria> listaAuditoriasNuevas) {
		this.dao.actualizarDatosPrograma(programa, listaAuditoriasEliminadas,listaAuditoriasNuevas);
		
	}
	

}
