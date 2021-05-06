package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface INormaService {
	public Error getError();

	public Paginacion getPaginacion();

	public List<Norma> obtenerListaNormas(PageRequest pageRequest);

	public Integer actualizarNorma(Norma norma);

	/*
	 * public List<Norma> obtenerListaNormasGrilla(NormaRequest normaRequest,
	 * PageRequest pageRequest); Paginacion getPaginacion();
	 * 
	 * public List<Requisito> obtenerNormaRequisito(NormaRequest normaRequest);
	 * public void guardarNormaRequisito(Norma norma); public void
	 * actualizarNormaRequisito(Long idNorma, Norma norma); public Norma
	 * obtenerDatosNormaId(Long idNorma); Boolean eliminarNorma(Long id);
	 */
}
