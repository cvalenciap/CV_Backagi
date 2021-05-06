package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.util.List;

import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.NodoJerarquia;
import pe.com.sedapal.agi.model.NodoRequisito;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RequisitoRequest;
import pe.com.sedapal.agi.model.request_objects.JerarquiaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IJerarquiaService {
	//List<Jerarquia> obtenerJerarquia(JerarquiaRequest JerarquiaRequest, PageRequest pageRequest);
	List<NodoJerarquia> obtenerJerarquia(JerarquiaRequest parametros);
	List<Jerarquia> obtenerJerarquiaTipoDocumento(JerarquiaRequest parametros, PageRequest pageRequest);
	Error getError();
	Paginacion getPaginacion();
	Jerarquia crearJerarquia(Jerarquia jerarquia);
	Jerarquia actualizarJerarquia(Jerarquia jerarquia, Long codigo);
	Jerarquia actualizarPermiso(Jerarquia jerarquia, Long codigo);
	Boolean eliminarJerarquia(Long codigo);
	public Jerarquia actualizarHijosJerarquia(Jerarquia jerarquia,Long codigo); 
	public  List<Jerarquia> obtenerHijoJerarquia(Jerarquia jerarquia);
	public  List<Jerarquia> obtenerDocJerarquia(Jerarquia jerarquia);
	public List<Jerarquia> obtenerJerarquiaIdPadre(JerarquiaRequest parametros);

}