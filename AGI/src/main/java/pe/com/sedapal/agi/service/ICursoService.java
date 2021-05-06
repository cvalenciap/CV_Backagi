package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.CursoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ICursoService {

	public Error getError();

	public Paginacion getPaginacion();
	
	public List<Curso> obtenerCursos(CursoRequest cursoRequest, PageRequest pageRequest);
	
	public List<Constante> listarTipoCursos(ConstanteRequest constanteRequest);
	
	public Curso registrarCurso(Curso curso);
	
	public Curso actualizarCurso(Curso curso);

	public Curso obtenerDatosCurso(Long idCurso);

	public Map<String, Object> eliminarCurso(Long codigo);

}
