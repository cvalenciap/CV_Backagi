package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IRevisionMigracionService {
	List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest);
	Boolean eliminarRevision(Long id);
	List<Revision> obtenerRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest);
	Error getError();
	Revision crearRevision(Revision revisionRequest);
	Revision crearDocumentoGoogleDrive(String idDocGoogle, Long idRevi);
	Paginacion getPaginacion();
	List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest);
}