package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IPlanAccionDAO {
	List<NoConformidad> obtenerNoConformidad(PlanAccionRequest planAccionRequest, PageRequest pageRequest);
	List<PlanAccion> obtenerPlanAccion(Long idNoConformidad, PageRequest pageRequest);	
	PlanAccion actualizarPlanAccion(Long idPlanAccion, PlanAccion planAccion);
	List<PlanAccion> obtenerListaAccionPropuesta(Long idNoConformidad);
	Paginacion getPaginacion();
	Error getError();
}
