package pe.com.sedapal.agi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.CapacitacionDocumentos;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.dao.ICapacitacionDAO;
import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Capacitacion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.UploadResponse;
import pe.com.sedapal.agi.service.ICapacitacionService;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.sedapal.componentes.fs.ClientFS;

@Service
public class CapacitacionServiceImpl implements ICapacitacionService {
	
	@Autowired ICapacitacionDAO dao;

	@Override
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}

	@Override
	public List<Capacitacion> obtenerCapacitaciones(Capacitacion capacitacion, PageRequest pageRequest) {
		return this.dao.obtenerCapacitaciones(capacitacion, pageRequest);
	}

	@Override
	public Capacitacion registrarDatosCapacitacion(Capacitacion capacitacion) {
		return this.dao.registroCapacitacion(capacitacion);
	}

	@Override
	public boolean eliminarCapacitacion(Capacitacion capacitacion) {
		return this.dao.eliminarCapacitacion(capacitacion);
	}

	@Override
	public Capacitacion actualizarCapacitacion(Capacitacion capacitacion) {
		return this.dao.actualizarCapacitacion(capacitacion);
	}

	@Override
	public boolean enviarCapacitacionPart(Asistencia asistencia) {
		return this.dao.programarCapacitacion(asistencia);
	}

	@Override
	public CapacitacionDocumentos cargarDocumento(CapacitacionDocumentos capacitacion, Optional<MultipartFile> file) {
		return this.dao.cargarDocumentoFS(capacitacion, file);
	}

	@Override
	public List<PreguntaCurso> consultarPreguntaCursoId(PreguntaCurso pregunta, PageRequest paginaRequest) {
		return this.dao.consultarPreguntaCursoId(pregunta, paginaRequest);
	}
}
