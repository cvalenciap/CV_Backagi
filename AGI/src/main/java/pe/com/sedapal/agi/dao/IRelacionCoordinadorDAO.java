package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.RelacionCoordinador;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IRelacionCoordinadorDAO {
	List<Jerarquia> obtenerArbolJerarquiaPorTipo(Long idJerarquia);
	List<RelacionCoordinador> obtenerRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador, PageRequest pageRequest);
	RelacionCoordinador guardarRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador);
	RelacionCoordinador actualizarRelacionGerenciaAlcance(Long idRelacion, RelacionCoordinador relacionCoordinador);
	boolean eliminarRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador);
	List<RelacionCoordinador> obtenerDatosCoordinador(Long idGerencia, Long idAlcance);
	Long obtenerDatosJefeEquipo(Long idArea);
	Error getError();
	Paginacion getPaginacion();
}
