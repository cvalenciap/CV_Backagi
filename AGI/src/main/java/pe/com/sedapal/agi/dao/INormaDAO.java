package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface INormaDAO {

	public Error getError();

	public Paginacion getPaginacion();

	public List<Norma> obtenerListaNormas(PageRequest pageRequest);
	
	public Integer actualizarNorma(Norma norma);


	// lista para la grilla
	// public List<Norma> obtenerListaNormasGrilla(NormaRequest normaRequest,
	// PageRequest pageRequest);
	/*
	 * 
	 * public List<Requisito> obtenerNormaRequisito(NormaRequest normaRequest);
	 * public void guardarNormaRequisito(Norma norma); public Norma
	 * obtenerDatosNormaId(Long idNorma); public void registarNormaReq(Long idNorma,
	 * NormaReq normaReq); void registrarReqRelacionado(RequisitoRelacionado
	 * requisitoRelacionado); void registrarReqDocumento(RequisitoDocumento
	 * requisitoDocumento);
	 * 
	 * void actualizarNormaRequisito(Long idNorma, Norma norma);
	 * 
	 * Norma actualizarNorma(Long id,Norma norma); NormaReq actualizarReq(Long
	 * id,NormaReq normaReq); void actualizaNormaReq(Long id, Long idNorma, NormaReq
	 * normaReq); long actualizarNormaReq(Long id,Long idNorma, NormaReq normaReq);
	 * void actualizarReqRelacionado(Long id,RequisitoRelacionado
	 * requisitoRelacionado); void actualizarReqHijos(Long idNorma,NormaReq
	 * normaReq); void actualizarReqDocumento(Long id, RequisitoDocumento
	 * requisitoDocumento);
	 * 
	 * Boolean eliminarNorma(Long id);
	 */

}
