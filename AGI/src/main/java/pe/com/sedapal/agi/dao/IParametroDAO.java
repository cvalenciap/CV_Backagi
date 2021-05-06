package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ParametroRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IParametroDAO {
	List<Constante> obtenerConstantes(ParametroRequest parametroRequest, PageRequest pageRequest, Long codigo);
	Constante actualizarConstante(Long id, Long i_nidpadre, String i_vcampo1, Constante constante);
	public Constante actualizarParametro(Constante constante);
	Paginacion getPaginacion();
	Error getError();
	Boolean eliminarConstante(Long id);
	Constante agregarParametro(Constante listaParametro);

}
