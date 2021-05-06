package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AsistenciaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IAsistenciaDAO {
	
	public Error getError();
	public Paginacion getPaginacion();
	public List<Asistencia> obtenerAsistencia(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest);
	public List<Sesion> obtenerSesion(Sesion session);
	public List<Trabajador> obtenerEmpleadoSesion(Trabajador trabajador);
	public Asistencia actualizarAsistencia(Asistencia asistencia);
	public Asistencia actualizar(Asistencia asistencia);
	public List<Asistencia> obtenerEvaluacion(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest);
	public List<Trabajador> obtenerEmpleadoEvaluacion(Trabajador trabajador,PageRequest paginaRequest);
	public Asistencia actualizarEvaluacion(Asistencia asistencia);
	public Asistencia guardarEvaluacion(Asistencia asistencia);
}
