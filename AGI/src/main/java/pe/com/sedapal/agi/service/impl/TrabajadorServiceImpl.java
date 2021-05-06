package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.ITrabajadorDAO;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.ITrabajadorService;

@Service
public class TrabajadorServiceImpl implements ITrabajadorService {
	
	@Autowired ITrabajadorDAO dao;

	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}

	@Override
	public List<Trabajador> obtenerTrabajador(AuditorRequest trabajadorRequest, PageRequest paginaRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerTrabajador(trabajadorRequest, paginaRequest);
	}
}
