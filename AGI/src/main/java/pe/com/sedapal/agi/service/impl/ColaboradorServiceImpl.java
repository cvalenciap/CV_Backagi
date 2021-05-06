package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IColaboradorDAO;
import pe.com.sedapal.agi.dao.ISolicitudDAO;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IColaboradorService;

@Service
public class ColaboradorServiceImpl implements IColaboradorService{
	@Autowired
	private IColaboradorDAO dao;
	@Autowired
	private ISolicitudDAO daoD;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Colaborador> obtenerColaborador(ColaboradorRequest colaboradorRequest, PageRequest pageRequest){
		List<Colaborador> listaColaborador = new ArrayList<>();
		listaColaborador = this.dao.obtenerColaborador(colaboradorRequest, pageRequest);
		return listaColaborador;
	}
	
	@Override
	public List<Colaborador> obtenerDestinatario(ColaboradorRequest colaboradorRequest, PageRequest pageRequest){
		List<Colaborador> listaColaborador = new ArrayList<>();//////////
		listaColaborador = this.daoD.obtenerDestinatario(colaboradorRequest, pageRequest);
		return listaColaborador;
	}
	
	@Override
	public List<Colaborador> obtenerColaboradorAuditoria(ColaboradorRequest colaboradorRequest, PageRequest pageRequest){
		List<Colaborador> listaColaborador = new ArrayList<>();
		listaColaborador = this.dao.obtenerColaboradorAuditor(colaboradorRequest, pageRequest);
		return listaColaborador;
	}
	
	
		
}
