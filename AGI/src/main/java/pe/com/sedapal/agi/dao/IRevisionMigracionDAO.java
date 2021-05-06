package pe.com.sedapal.agi.dao;

import java.util.Date;
import java.util.List;

import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IRevisionMigracionDAO {
	List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest);
	Boolean eliminarRevision(Long idRevision);
	Revision crearRevision(Revision revision, String iUsuario, Long idUsuario, Date FechaCreaDoc);
	Revision crearDocumentoGoogleDrive(String idDocGoogle, Long idRevi);
	List<Revision> obtenerRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest, Long iUsuario);
	Paginacion getPaginacion();
	Error getError();
	List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest);
}