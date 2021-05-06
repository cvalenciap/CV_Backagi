package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Area;
import pe.com.sedapal.agi.model.request_objects.AreaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IAreaService {
	List<Area> obtenerArea(AreaRequest constanteRequest, PageRequest pageRequest);
	List<Area> obtenerAreaMigracion(AreaRequest constanteRequest, PageRequest pageRequest);
	Error getError();
	Paginacion getPaginacion();
}