package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.io.IOException;
import java.util.List;

import pe.com.sedapal.agi.model.Dashboard;
import pe.com.sedapal.agi.model.TareasPendientes;

public interface ITareasPendientesService {
	List<TareasPendientes> obtenerTareasPendientesDocumental(Long idColaborador);
	Dashboard obtenerDashboardDocumento(Long anio, Long idTrimestre);
	String obtenerDashboardExcel(Long anio, Long idTrimestre, String trimestre) throws IOException;
	Error getError();
	//List<TareasPendientes> obtenerTareasPendientesDocumental(Long idColaborador, Long EstaConsta);
}