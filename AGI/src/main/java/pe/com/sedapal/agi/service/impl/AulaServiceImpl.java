package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IAulaDAO;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.request_objects.AulaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IAulaService;

@Service
public class AulaServiceImpl implements IAulaService{
	@Autowired
	private IAulaDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Aula> obtenerAula(AulaRequest constanteRequest, PageRequest pageRequest) {
		List<Aula> listaAula = new ArrayList<>();
		listaAula = this.dao.obtenerAula(constanteRequest, pageRequest);
		return listaAula;
	}
	
	@Override
	public Boolean eliminarAula(Long id) {
		return this.dao.eliminarAula(id);
	}
	
	@Override
	public Aula actualizarAula(Long id, Aula aula) {
		Aula item;
		item = this.dao.actualizarAula(id, aula);
		return item;
	}
	@Override
	public Aula insertarAula(Aula aula) {
		Aula item;
		item = this.dao.insertarAula(aula);
		return item;
	}
	

	
}
