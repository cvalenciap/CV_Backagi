package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Area;
import pe.com.sedapal.agi.model.request_objects.AreaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IAreaDAO {
	List<Area> obtenerArea(AreaRequest constanteRequest, PageRequest pageRequest);
	List<Area> obtenerAreaMigracion(AreaRequest constanteRequest, PageRequest pageRequest);
	Paginacion getPaginacion();
	Error getError();
}
