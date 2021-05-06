package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IAreaDAO;
import pe.com.sedapal.agi.model.Area;
import pe.com.sedapal.agi.model.request_objects.AreaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IAreaService;

@Service
public class AreaServiceImpl implements IAreaService{
	@Autowired
	private IAreaDAO dao;

	@Override
	public List<Area> obtenerArea(AreaRequest constanteRequest, PageRequest pageRequest) {
		List<Area> listaArea = new ArrayList<>();
		listaArea = this.dao.obtenerArea(constanteRequest, pageRequest);
		return listaArea;
	}

	@Override
	public List<Area> obtenerAreaMigracion(AreaRequest constanteRequest, PageRequest pageRequest) {
		List<Area> listaArea = this.dao.obtenerAreaMigracion(constanteRequest, pageRequest);
		return listaArea;
	}

	@Override
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}

}
