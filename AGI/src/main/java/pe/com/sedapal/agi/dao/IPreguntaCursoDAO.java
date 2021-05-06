package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.PreguntaDetalle;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.request_objects.PreguntaCursoRequest;

public interface IPreguntaCursoDAO {
	
	public Error getError();
	public Paginacion getPaginacion();
	public List<PreguntaCurso> obtenerPregunta(PreguntaCursoRequest preguntaRequest, PageRequest paginaRequest);
	public List<PreguntaCurso> obtenerCurso(PreguntaCursoRequest preguntaRequest, PageRequest paginaRequest);
	
	public PreguntaCurso registrarPregunta(PreguntaCurso preguntaCurso);
	public PreguntaDetalle registrarDetalle(PreguntaDetalle preguntaDetalle);
	public PreguntaCurso guardarPregunta(PreguntaCurso preguntaCurso);
	
	public Boolean eliminarPregunta(Long id);
	public List<ConstanteRequest> obtenerTipoPregunta();
	
	public PreguntaCurso actualizarPregunta(Long id, PreguntaCurso preguntaCurso);
	public PreguntaDetalle actualizarDetalle(PreguntaDetalle preguntaDetalle);
	
	public PreguntaCurso guardarDetalle(PreguntaCurso preguntacurso);
	public List<PreguntaDetalle> obtenerDetalle(Long id);
}
