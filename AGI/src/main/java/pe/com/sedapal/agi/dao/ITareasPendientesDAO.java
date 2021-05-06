package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Dashboard;
import pe.com.sedapal.agi.model.TareasPendientes;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface ITareasPendientesDAO {	
	Dashboard obtenerDashboardDocumento(Long anio, Long idTrimestre);
	List<Dashboard> obtenerDashboardExcel(Long anio, Long idTrimestre);
	Error getError();
	List<TareasPendientes> obtenerTareasPendientesDocumental(Long idColaborador);
}