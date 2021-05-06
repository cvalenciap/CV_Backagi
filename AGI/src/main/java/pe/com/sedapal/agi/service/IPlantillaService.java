package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Plantilla;
import pe.com.sedapal.agi.model.request_objects.ConocimientoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlantillaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IPlantillaService {	
	Error getError();
	Paginacion getPaginacion();
	List<Plantilla> obtenerPlantilla(PlantillaRequest plantillaRequest, PageRequest pageRequest);
	List<Conocimiento> obtenerConocimiento(ConocimientoRequest conocimientoRequest, PageRequest pageRequest);
	Plantilla agregarPlantilla(Plantilla plantilla,Optional<MultipartFile> file);
	
	public Boolean eliminarPlantilla(Plantilla plantilla);	
	public Boolean eliminarConocimiento(Conocimiento conocimiento);	
}
