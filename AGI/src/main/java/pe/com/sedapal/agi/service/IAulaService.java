package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.request_objects.AulaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IAulaService {
	List<Aula> obtenerAula(AulaRequest aulaRequest, PageRequest pageRequest);
	Boolean eliminarAula(Long id);
	Paginacion getPaginacion();
	Aula actualizarAula(Long id, Aula aula);
	Aula insertarAula(Aula aula);
	Error getError();
	
}