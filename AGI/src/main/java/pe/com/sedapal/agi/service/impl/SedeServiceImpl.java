package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.ISedeDAO;
import pe.com.sedapal.agi.model.Sede;
import pe.com.sedapal.agi.model.request_objects.SedeRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.service.ISedeService;

@Service
public class SedeServiceImpl implements ISedeService{
	@Autowired
	private ISedeDAO dao;
	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Sede> obtenerSede(SedeRequest constanteRequest) {
		List<Sede> listaSede = new ArrayList<>();
		listaSede = this.dao.obtenerSede(constanteRequest);
		return listaSede;
	}
		
}
