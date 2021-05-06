package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.SolicitudCopia;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface ISolicitudDAO {
	
	
	Documento ActualizaSolicitud(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario/*,Colaborador colaborador*/);
	Documento UpdateSolicitud(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario/*,Colaborador colaborador*/);
	
	Documento guardarSolicitud(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario/*,Colaborador colaborador*/);
	Colaborador guardarSolicitudDetalle(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario,Colaborador colaborador);
	Paginacion getPaginacion();
	Error getError();
	Documento guardarSolicitudParticipantes(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario);
	List<Revision> obtenerSolicitud(RevisionRequest revisionRequest, PageRequest pageRequest, Long iUsuario);//
	List<Colaborador> obtenerDestinatario(ColaboradorRequest colaboradorRequest, PageRequest pageRequest);
	
	
	List<Revision> obtenerSolicitudDocument(RevisionRequest revisionRequest, PageRequest pageRequest, Long iUsuario);//
	//List<Revision> obtenerDestinatarioDocumento(RevisionRequest revisionRequest, PageRequest pageRequest, Long iUsuario);//
	
	
	
	
	
	
}