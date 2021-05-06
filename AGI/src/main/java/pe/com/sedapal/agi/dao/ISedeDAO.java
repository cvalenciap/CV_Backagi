package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Sede;
import pe.com.sedapal.agi.model.request_objects.SedeRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.request_objects.PageRequest;


public interface ISedeDAO {
	List<Sede> obtenerSede(SedeRequest sedeRequest);
	Error getError();

}
