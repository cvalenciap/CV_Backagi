package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.dao.IConstanteDAO;
import pe.com.sedapal.agi.dao.IParametroDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ParametroRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IConstanteService;
import pe.com.sedapal.agi.service.IParametroService;

@Service
public class ParametroServiceImpl implements IParametroService{
	@Autowired
	private IParametroDAO dao;
	
	    
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}
	@Override
	public List<Constante> obtenerConstantes(ParametroRequest parametroRequest, PageRequest pageRequest, Long codigo){
		List<Constante> listaConstante = new ArrayList<>();
		listaConstante = this.dao.obtenerConstantes(parametroRequest, pageRequest, codigo);
		return listaConstante;
	}
	@Override
	public Constante actualizarParametro(Constante constante) {
		return this.dao.actualizarParametro(constante);
	}
		
	@Override
	public Constante actualizarConstante(Long id, Long i_nidpadre, String i_vcampo1, Constante constante) {
		Constante item;
		item = this.dao.actualizarConstante(id, i_nidpadre, i_vcampo1, constante);
		return item;
	}
	
	@Override
	public Boolean eliminarConstante(Long id) {
		return this.dao.eliminarConstante(id);
	}
		
}
