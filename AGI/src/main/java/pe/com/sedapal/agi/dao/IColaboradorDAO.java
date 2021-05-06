package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IColaboradorDAO {
	List<Colaborador> obtenerColaborador(ColaboradorRequest colaboradorRequest, PageRequest pageRequest);
	
	List<Colaborador> obtenerColaboradorAuditor(ColaboradorRequest colaboradorRequest, PageRequest pageRequest);
	
	
		Paginacion getPaginacion();
	Error getError();
}