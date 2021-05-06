package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;
import java.util.List;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IPlanAccionService {
	List<NoConformidad> obtenerNoConformidad(PlanAccionRequest planAccionRequest, PageRequest pageRequest);
	List<PlanAccion> obtenerPlanAccion(Long idNoConformidad, PageRequest pageRequest);	
	PlanAccion actualizarPlanAccion(Long idPlanAccion, PlanAccion planAccion);
	List<PlanAccion> obtenerListaAccionPropuesta(Long idNoConformidad);
	Error getError();
	Paginacion getPaginacion();
}
