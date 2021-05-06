package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.INoConformidadDAO;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.NoConformidadSeguimiento;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.PlanAccionEjecucion;
import pe.com.sedapal.agi.model.request_objects.NoConformidadRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.INoConformidadService;

@Service
public class NoConformidadServiceImpl implements INoConformidadService {
	@Autowired
	private INoConformidadDAO dao;
	
	@Override
	public List<NoConformidad> obtenerNoConformidad(NoConformidadRequest constanteRequest, PageRequest pageRequest) {
		List<NoConformidad> listaNoConformidad = new ArrayList<>();
		listaNoConformidad = this.dao.obtenerNoConformidad(constanteRequest, pageRequest);
		return listaNoConformidad;
	}

	@Override
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}

	@Override
	public List<NoConformidadSeguimiento> obtenerNoConformidadSeguimiento(NoConformidadRequest noConformidadRequest, PageRequest pageRequest) {
		List<NoConformidadSeguimiento> listaNoConformidadSeguimiento = new ArrayList<>();
		listaNoConformidadSeguimiento = this.dao.obtenerNoConformidadSeguimiento(noConformidadRequest,pageRequest);
		return listaNoConformidadSeguimiento;
	}
	
	@Override
	public NoConformidad obtenerDatosNoConformidad(NoConformidadRequest noConformidadRequest) {
		NoConformidad datosNoConformidad = new NoConformidad();
		datosNoConformidad = this.dao.obtenerDatosNoConformidad(noConformidadRequest);
		return datosNoConformidad;
	}
	
	@Override
	public NoConformidad actualizarNoConformidad(Long idNoConformidad, NoConformidad noConformidad) {
		NoConformidad noConformidadDatos = new NoConformidad();
		noConformidadDatos = this.dao.actualizarNoConformidad(idNoConformidad, noConformidad);
		return noConformidadDatos;
	}
	
	@Override
	public void actualizarDatosPlanAccion(List<PlanAccion> listaPlanAccion, Long idNoConformidad) {
		this.dao.actualizarDatosPlanAccion(listaPlanAccion, idNoConformidad);
	}
	
	@Override
	public void actualizarDatosEjecucion(List<PlanAccionEjecucion> listaEjecucion, Long idPlanAccion) {
		this.dao.actualizarDatosEjecucion(listaEjecucion, idPlanAccion);
	}
	
	@Override
	public NoConformidad insertarNoCoformidadSeguimiento(Long idNoConformidad, NoConformidad noConformidad) {
		NoConformidad noConformidadDatos = new NoConformidad();
		noConformidadDatos = this.dao.insertarNoCoformidadSeguimiento(idNoConformidad, noConformidad);
		return noConformidadDatos;
	}
	
	@Override
	public List<PlanAccionEjecucion> obtenerEjecucion(Long idNoConformidad, PageRequest pageRequest){
		List<PlanAccionEjecucion> listaEjecucion = new ArrayList<>();
		listaEjecucion = this.dao.obtenerEjecucion(idNoConformidad, pageRequest);
		return listaEjecucion;
	}
}
