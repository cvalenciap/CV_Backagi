package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ITipoNormasDAO {
	public Error getError();

	public Paginacion getPaginacion();

	public List<Constante> listaTipoNormas(PageRequest pageRequest);
}
