package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.ConocimientoRevisionDocumento;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IReporteConocimientoDocumentoDAO {
	public List<Trabajador> consultarPersonasDesconocenDocumento(RevisionRequest request);
	
	public Error getError();
	
	public Paginacion getPaginacion();
	
	public List<ConocimientoRevisionDocumento> consultarDocumentosRevision(DocumentoRequest documentoRequest, PageRequest paginaRequest);
}
