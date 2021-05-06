package pe.com.sedapal.agi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.ISesionDAO;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.ISesionService;

@Service
public class SesionServiceImpl implements ISesionService {
	
	@Autowired ISesionDAO dao;

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
	public Sesion obtenerDatosSesionCurso(Long idCurso, Long idSesion) {
		return this.dao.obtenerDatosSesionCurso(idCurso, idSesion);
	}
	
	
}
