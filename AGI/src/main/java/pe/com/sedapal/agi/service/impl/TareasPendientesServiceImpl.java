package pe.com.sedapal.agi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.ITareasPendientesDAO;
import pe.com.sedapal.agi.model.Dashboard;
import pe.com.sedapal.agi.model.TareasPendientes;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.service.IExcel;
import pe.com.sedapal.agi.service.ITareasPendientesService;

@Service
public class TareasPendientesServiceImpl implements ITareasPendientesService{
	@Autowired
	private ITareasPendientesDAO daoTareasPendientes;
	
	@Autowired
	private IExcel excelService;
	
	public Error getError() {
		return this.daoTareasPendientes.getError();
	}
	
	@Override
	public List<TareasPendientes> obtenerTareasPendientesDocumental(Long idColaborador){
		List<TareasPendientes> listaTareasPendientes = new ArrayList<>();
		listaTareasPendientes = this.daoTareasPendientes.obtenerTareasPendientesDocumental(idColaborador);
		return listaTareasPendientes;
	}
	
	@Override
	public Dashboard obtenerDashboardDocumento(Long anio, Long idTrimestre){
		Dashboard estadistica = this.daoTareasPendientes.obtenerDashboardDocumento(anio, idTrimestre);
		return estadistica;
	}
	
	@Override
	public String obtenerDashboardExcel(Long anio, Long idTrimestre, String trimestre) throws IOException {
		List<Dashboard> lista = this.daoTareasPendientes.obtenerDashboardExcel(anio, idTrimestre);
		return this.excelService.formatoExcelDashboard(lista, anio, trimestre);
	}


}