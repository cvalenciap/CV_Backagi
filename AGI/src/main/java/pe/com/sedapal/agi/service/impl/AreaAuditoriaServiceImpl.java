package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.dao.IAreaAuditoriaDAO;
import pe.com.sedapal.agi.model.AreaAlcanceAuditoria;
import pe.com.sedapal.agi.model.AreaAuditoria;
import pe.com.sedapal.agi.model.AreaCargoAuditoria;
import pe.com.sedapal.agi.model.AreaParametros;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.service.IAreaAuditoriaService;

@Service
public class AreaAuditoriaServiceImpl implements IAreaAuditoriaService {
	
	@Autowired
	private IAreaAuditoriaDAO dao;

	@Override
	public AreaParametros obtenerAreaParametros() {
		return this.dao.obtenerAreaParametros();
	}
	
	@Override
	public Error getError() {
		return this.dao.getError();
	}
	
	@Override
	public Integer eliminarAuditoria(AreaAuditoria areaAuditoria) throws GmdException {
		return this.dao.eliminarAuditoria(areaAuditoria);
	}
	
	@Override
	public Integer registrarAuditoria(AreaAuditoria areaAuditoria, List<AreaAlcanceAuditoria> lstAlcance,
			List<AreaCargoAuditoria> lstCargo) throws GmdException {
		return this.dao.registrarAuditoria(areaAuditoria, lstAlcance, lstCargo);
	}
	

}
