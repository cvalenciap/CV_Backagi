package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.ConocimientoRevisionDocumento;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IReporteConocimientoDocumentoService {
	public byte[] generarReporteConocimientoDocumento(RevisionRequest request);
	public Error getError();
	public Paginacion getPaginacion();
	public List<ConocimientoRevisionDocumento> consultarDocumentosRevision(DocumentoRequest documentoRequest, PageRequest paginaRequest);
	
}
