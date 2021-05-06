package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.util.List;

import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.request_objects.EquipoRequest;

public interface IEquipoService {
	
	List<Equipo> obtenerEquipo(EquipoRequest equipoRequest);
	Error getError();
	
	
	
}