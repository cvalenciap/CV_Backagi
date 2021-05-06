package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.ITipoNormasDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.ITipoNormasService;

@Service
public class TipoNormasServiceImpl implements ITipoNormasService {
	@Autowired
	private ITipoNormasDAO dao;

	@Autowired
	SessionInfo session;

	@Override
	public Error getError() {
		return dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}

	@Override
	public List<Constante> listaTipoNormas(PageRequest pageRequest) {
		return dao.listaTipoNormas(pageRequest);
	}
}
