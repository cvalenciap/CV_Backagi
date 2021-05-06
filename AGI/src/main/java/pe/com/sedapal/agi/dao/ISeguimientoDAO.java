package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.SeguimientoDocumento;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ISeguimientoDAO {
	Error getError();
	Paginacion getPaginacion();
	public List<SeguimientoDocumento> obtenerSeguimientoDocumento(DocumentoRequest documentoRequest,PageRequest pageRequest);
	
	public List<Flujo> obtenerFlujosDocumento(RevisionRequest revisionRequest);
}
