package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.SolicitudCopia;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ISolicitudService {
	Documento crearSolicitud(SolicitudCopia constante);
	List<Revision> obtenerSolicitudBitacora(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> obtenerSolicitudBitacoraDocumento(RevisionRequest revisionRequest, PageRequest pageRequest);
	Documento actualizarDocumento(SolicitudCopia constante, Long codigo);
	Documento actualizarDocumentoCopiaImpresa(SolicitudCopia constante, Long codigo);
	Error getError();
	Paginacion getPaginacion();		
}
