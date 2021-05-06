package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Requisito;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IRequisitoService {

	public Error getError();

	public Paginacion getPaginacion();

	public List<Requisito> listaNormaRequerimientos(PageRequest pageRequest);

	// public NodoRequisito obtenerRequisitos(RequisitoRequest parametros);

	// public List<NodoRequisito> obtenerListaRequisitos(RequisitoRequest
	// parametros);

}
