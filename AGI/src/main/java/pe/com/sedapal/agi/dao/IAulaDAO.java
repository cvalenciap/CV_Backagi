package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.request_objects.AulaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IAulaDAO {
	List<Aula> obtenerAula(AulaRequest aulaRequest, PageRequest pageRequest);
	Aula actualizarAula(Long id, Aula aula);
	Boolean eliminarAula(Long id);
	Paginacion getPaginacion();
	Error getError();
	Aula insertarAula(Aula aula);

	
	

}
