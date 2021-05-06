package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.util.List;

import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IColaboradorService {
	List<Colaborador> obtenerColaborador(ColaboradorRequest rutaRequest, PageRequest pageRequest);
	List<Colaborador> obtenerColaboradorAuditoria(ColaboradorRequest rutaRequest, PageRequest pageRequest);
	List<Colaborador> obtenerDestinatario(ColaboradorRequest rutaRequest, PageRequest pageRequest);
	//List<Colaborador> obtenerDestinatarioDocumento(ColaboradorRequest rutaRequest, PageRequest pageRequest);	
	Error getError();
	Paginacion getPaginacion();
}