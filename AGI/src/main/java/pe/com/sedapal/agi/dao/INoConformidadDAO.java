package pe.com.sedapal.agi.dao;

import java.util.List;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.PlanAccionEjecucion;
import pe.com.sedapal.agi.model.NoConformidadSeguimiento;
import pe.com.sedapal.agi.model.request_objects.NoConformidadRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface INoConformidadDAO {
	List<NoConformidad> obtenerNoConformidad(NoConformidadRequest constanteRequest, PageRequest pageRequest);
	List<NoConformidadSeguimiento> obtenerNoConformidadSeguimiento(NoConformidadRequest noConformidadRequest, PageRequest pageRequest);
	NoConformidad obtenerDatosNoConformidad(NoConformidadRequest noConformidadRequest);
	NoConformidad actualizarNoConformidad(Long idNoConformidad, NoConformidad noConformidad);
	NoConformidad insertarNoCoformidadSeguimiento(Long idNoConformidad, NoConformidad noConformidad);
	public void actualizarDatosPlanAccion(List<PlanAccion> listaPlanAccion, Long idNoConformidad);
	public boolean eliminarPlanAccion(PlanAccion planAccion);
	public void agregarPlanAccion(PlanAccion planAccion, Long idNoConformidad);
	public void modificarPlanAccion(PlanAccion planAccion);
	public List<PlanAccionEjecucion> obtenerEjecucion(Long idNoConformidad, PageRequest pageRequest);
	public void actualizarDatosEjecucion(List<PlanAccionEjecucion> listaEjecucion, Long idPlanAccion);
	public void agregarEjecucion(PlanAccionEjecucion ejecucion, Long idPlanAccion);
	public void modificarEjecucion(PlanAccionEjecucion ejecucion);
	Paginacion getPaginacion();
	Error getError();
}
