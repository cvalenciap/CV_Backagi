package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ITrabajadorService {
	
	public Error getError();
	
	public Paginacion getPaginacion();
	
	public List<Trabajador> obtenerTrabajador(AuditorRequest trabajadorRequest,PageRequest paginaRequest);

}
