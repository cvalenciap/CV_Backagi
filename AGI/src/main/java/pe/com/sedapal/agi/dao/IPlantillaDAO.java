package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Plantilla;
import pe.com.sedapal.agi.model.request_objects.ConocimientoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlantillaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IPlantillaDAO {
	List<Plantilla> obtenerPlantilla(PlantillaRequest plantillaRequest, PageRequest pageRequest);	
	List<Conocimiento> obtenerConocimiento(ConocimientoRequest conocimientoRequest, PageRequest pageRequest);	
	Plantilla agregarPlantilla(Plantilla plantilla);
	public Boolean eliminarPlantilla(Plantilla plantilla);
	public Boolean eliminarConocimiento(Conocimiento conocimiento);
	Paginacion getPaginacion();
	Error getError();
	
}