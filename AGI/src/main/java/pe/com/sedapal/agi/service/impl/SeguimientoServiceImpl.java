package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.ISeguimientoDAO;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.SeguimientoDocumento;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.ISeguimientoService;

@Service
public class SeguimientoServiceImpl implements ISeguimientoService{
	
	@Autowired
	SessionInfo session;
	
	@Autowired
	ISeguimientoDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}
	
	@Override
	public List<SeguimientoDocumento> obtenerSeguimientoDocumento(DocumentoRequest documentoRequest,
			PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerSeguimientoDocumento(documentoRequest, pageRequest);
	}

	@Override
	public List<Flujo> obtenerFlujosDocumento(RevisionRequest revisionRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerFlujosDocumento(revisionRequest);
	}
	
}
