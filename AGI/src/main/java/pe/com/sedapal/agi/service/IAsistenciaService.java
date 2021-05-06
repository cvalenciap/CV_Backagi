package pe.com.sedapal.agi.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AsistenciaRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IAsistenciaService {
	
	public Error getError();
	public Paginacion getPaginacion();
	public List<Asistencia> obtenerAsistencia(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest);
	public Asistencia crearDocumento(Asistencia asistencia,Optional<MultipartFile> file);
	public Asistencia actualizarAsistencia(Asistencia asistencia);
	public List<Asistencia> obtenerEvaluacion(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest);
	public Asistencia actualizarEvaluacion(Asistencia asistencia);
	public List<Trabajador> obtenerEmpleadoEvaluacion(Trabajador trabajador,PageRequest paginaRequest);
	byte[] generarPdf(Trabajador trabajador, PageRequest pageRequest) throws IOException;
	public List<Trabajador> obtenerEmpleadoSesion(Sesion sesion);
}
