package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.util.List;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;


public interface IConstanteService {
	List<Constante> obtenerConstantes(ConstanteRequest constanteRequest, PageRequest pageRequest, Long codigo);
	Error getError();
	Paginacion getPaginacion();
	Constante actualizarConstante(Long id, Long i_nidpadre, String i_vcampo1, Constante constante);
	Boolean eliminarConstante(Long id);
}
