package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IEquipoDAO;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.request_objects.EquipoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.service.IEquipoService;

@Service
public class EquipoServiceImpl implements IEquipoService{
		
	@Autowired
	private IEquipoDAO dao;
	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Equipo> obtenerEquipo(EquipoRequest equipoRequest){
		List<Equipo> listaEquipo = new ArrayList<>();
		listaEquipo = this.dao.obtenerEquipo(equipoRequest);
		return listaEquipo;
	}
	
}