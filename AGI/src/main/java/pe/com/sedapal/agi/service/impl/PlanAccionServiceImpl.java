package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IPlanAccionDAO;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IPlanAccionService;

@Service
public class PlanAccionServiceImpl implements IPlanAccionService {
	@Autowired
	private IPlanAccionDAO daoPlanAccion;
	
	public Paginacion getPaginacion() {
		return this.daoPlanAccion.getPaginacion();
	}
	
	public Error getError() {
		return this.daoPlanAccion.getError();
	}
	
	@Override
	public List<NoConformidad> obtenerNoConformidad(PlanAccionRequest planAccionRequest, PageRequest pageRequest){
		List<NoConformidad> listaNoConformidad = new ArrayList<>();
		listaNoConformidad = this.daoPlanAccion.obtenerNoConformidad(planAccionRequest, pageRequest);
		return listaNoConformidad;
	}
	
	@Override
	public List<PlanAccion> obtenerPlanAccion(Long idNoConformidad, PageRequest pageRequest){
		List<PlanAccion> listaPlanAccion = new ArrayList<>();
		listaPlanAccion = this.daoPlanAccion.obtenerPlanAccion(idNoConformidad, pageRequest);
		return listaPlanAccion;
	}
	
	@Override
	public PlanAccion actualizarPlanAccion(Long idPlanAccion, PlanAccion planAccion){
		PlanAccion itemPlanAccion = new PlanAccion();
		itemPlanAccion = this.daoPlanAccion.actualizarPlanAccion(idPlanAccion, planAccion);
		return itemPlanAccion;
	}
	
	@Override
	public List<PlanAccion> obtenerListaAccionPropuesta(Long idNoConformidad){
		List<PlanAccion> listaPlanAccion = new ArrayList<>();
		listaPlanAccion = this.daoPlanAccion.obtenerListaAccionPropuesta(idNoConformidad);
		return listaPlanAccion;
	}
}