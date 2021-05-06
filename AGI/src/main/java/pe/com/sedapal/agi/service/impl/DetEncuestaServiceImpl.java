package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IDetEncuestaDAO;
import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IDetEncuestaService;

@Service
public class DetEncuestaServiceImpl implements IDetEncuestaService{
	@Autowired
	private IDetEncuestaDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest constanteRequest, PageRequest pageRequest) {
		List<DetEncuesta> listaDetEncuesta = new ArrayList<>();
		listaDetEncuesta = this.dao.obtenerDetEncuesta(constanteRequest, pageRequest);
		return listaDetEncuesta;
	}
	
	@Override
	public Boolean eliminarDetEncuesta(Long id) {
		return this.dao.eliminarDetEncuesta(id);
	}
	
	@Override
	public DetEncuesta actualizarDetEncuesta(Long id, DetEncuesta detEncuesta) {
		DetEncuesta item;
		item = this.dao.actualizarDetEncuesta(id, detEncuesta);
		return item;
	}
	@Override
	public DetEncuesta insertarDetEncuesta(DetEncuesta detEncuesta) {
		DetEncuesta item;
		item = this.dao.insertarDetEncuesta(detEncuesta);
		return item;
	}
	

	
}
