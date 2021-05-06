package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.PreguntaDetalle;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PreguntaCursoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IPreguntaCursoService {
	
public Error getError();
	
	public Paginacion getPaginacion();
	
	public List<PreguntaCurso> obtenerPregunta(PreguntaCursoRequest preguntaRequest,PageRequest paginaRequest);
	public List<PreguntaCurso> obtenerCurso(PreguntaCursoRequest preguntaRequest,PageRequest paginaRequest);
	public PreguntaCurso registrarPregunta(PreguntaCurso pregunta);
	public Boolean eliminarPregunta(Long id);
	public List<ConstanteRequest> obtenerTipoPregunta();
	public PreguntaCurso actualizarPregunta(PreguntaCurso preguntacurso);
	
}
