package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.CapacitacionDocumentos;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Capacitacion;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ICapacitacionService {
	
	public Error getError();

	public Paginacion getPaginacion();
	
	public List<Capacitacion> obtenerCapacitaciones(Capacitacion capacitacion, PageRequest pageRequest);

	public Capacitacion registrarDatosCapacitacion(Capacitacion capacitacion);

	public boolean eliminarCapacitacion(Capacitacion capacitacion);

	public Capacitacion actualizarCapacitacion(Capacitacion capacitacion);

	public boolean enviarCapacitacionPart(Asistencia asistencia);
	
	public CapacitacionDocumentos cargarDocumento(CapacitacionDocumentos capacitacion,Optional<MultipartFile> file);

	public List<PreguntaCurso> consultarPreguntaCursoId(PreguntaCurso pregunta, PageRequest paginaRequest);

}
