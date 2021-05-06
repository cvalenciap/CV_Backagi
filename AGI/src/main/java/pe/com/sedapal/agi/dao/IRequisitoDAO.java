package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Requisito;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IRequisitoDAO {
	public Error getError();

	public Paginacion getPaginacion();

	public List<Requisito> listaNormaRequerimientos(PageRequest pageRequest);
	/*
	 * public List<Requisito> obtenerRequisitos(RequisitoRequest parametros);
	 * 
	 * public Error getError();
	 * 
	 * public Long obtenerNorma(Long idRequisito);
	 */
}
