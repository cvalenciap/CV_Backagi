package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.NoConformidadSeguimiento;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.PlanAccionEjecucion;
import pe.com.sedapal.agi.model.request_objects.NoConformidadRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface INoConformidadService {
	List<NoConformidad> obtenerNoConformidad(NoConformidadRequest constanteRequest, PageRequest pageRequest);
	List<NoConformidadSeguimiento> obtenerNoConformidadSeguimiento(NoConformidadRequest noConformidadRequest, PageRequest pageRequest);
	NoConformidad obtenerDatosNoConformidad(NoConformidadRequest noConformidadRequest);
	NoConformidad actualizarNoConformidad(Long idNoConformidad, NoConformidad noConformidad);
	public void actualizarDatosPlanAccion(List<PlanAccion> listaPlanAccion, Long idNoConformidad);
	public void actualizarDatosEjecucion(List<PlanAccionEjecucion> listaEjecucion, Long idNoConformidad);
	NoConformidad insertarNoCoformidadSeguimiento(Long idNoConformidad, NoConformidad noConformidad);
	public List<PlanAccionEjecucion> obtenerEjecucion(Long idNoConformidad, PageRequest pageRequest);
	Error getError();
	Paginacion getPaginacion();
}
