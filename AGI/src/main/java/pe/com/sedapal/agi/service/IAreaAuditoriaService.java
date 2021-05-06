package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.model.AreaAlcanceAuditoria;
import pe.com.sedapal.agi.model.AreaAuditoria;
import pe.com.sedapal.agi.model.AreaCargoAuditoria;
import pe.com.sedapal.agi.model.AreaParametros;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IAreaAuditoriaService {
	
	public AreaParametros obtenerAreaParametros();
	Integer registrarAuditoria(AreaAuditoria areaAuditoria, List<AreaAlcanceAuditoria> lstAlcance,
			List<AreaCargoAuditoria> lstCargo) throws GmdException;
	Integer eliminarAuditoria(AreaAuditoria areaAuditoria) throws GmdException;
	Error getError();

}
