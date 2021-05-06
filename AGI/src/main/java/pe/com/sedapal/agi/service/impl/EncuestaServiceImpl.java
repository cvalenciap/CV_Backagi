package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IEncuestaDAO;
import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.Encuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.EncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IEncuestaService;

@Service
public class EncuestaServiceImpl implements IEncuestaService{
	@Autowired
	private IEncuestaDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Encuesta> obtenerEncuesta(EncuestaRequest constanteRequest, PageRequest pageRequest) {
		List<Encuesta> listaEncuesta = new ArrayList<>();
		listaEncuesta = this.dao.obtenerEncuesta(constanteRequest, pageRequest);
		return listaEncuesta;
	}
	
	@Override
	public List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest detEncuestaRequest, PageRequest pageRequest) {
		List<DetEncuesta> listaEncuesta = new ArrayList<>();
		listaEncuesta = this.dao.obtenerDetEncuesta(detEncuestaRequest, pageRequest);
		return listaEncuesta;
	}
	
	@Override
	public Boolean eliminarEncuesta(Long id) {
		return this.dao.eliminarEncuestaAll(id);
	}
	
	@Override
	public Encuesta actualizarEncuesta(Long id, Encuesta encuesta) {
		Encuesta item;
		item = this.dao.actualizarEncuestaAll(id, encuesta);
		return item;
	}
	@Override
	public Encuesta insertarEncuesta(Encuesta encuesta) {
		Encuesta item;
		item = this.dao.registrarEncuesta(encuesta);
		return item;
	}
	@Override
	public Encuesta obtenerDatosEncuesta(Long idCurso) {
		return this.dao.obtenerListaEncuestaAll(idCurso);
	}
	@Override
	public Boolean eliminarEncuestaDet(Long id) {
		return this.dao.eliminarEncuestaDet(id);
	}

	

	
}
