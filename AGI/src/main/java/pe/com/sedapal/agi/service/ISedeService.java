package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Sede;
import pe.com.sedapal.agi.model.request_objects.SedeRequest;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface ISedeService {
	List<Sede> obtenerSede(SedeRequest aulaRequest);
	Error getError();
	
}