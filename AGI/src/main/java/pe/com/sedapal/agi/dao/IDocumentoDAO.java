package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IDocumentoDAO {
	List<Documento> obtenerDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest);
	Documento obtenerIdGoogle(Long codigo,Long numero,Long idrevi);	
	List<Documento> obtenerDocumentoHisto(DocumentoRequest documentoRequest, PageRequest pageRequest);
	//List<Documento> obtenerDocumentoHist(DocumentoRequest documentoRequest, PageRequest pageRequest);
	List<Documento> obtenerDocumentoSolicitud(DocumentoRequest documentoRequest, PageRequest pageRequest);	
	Documento obtenerDocumentoDetalle(Long codigo);
	//Documento obtenerDocumentoDetalleHist(Long codigo, Long idRevision);
	//Documento obtenerDocumentoRuta(Long iddocu, Long idrevi); //Ruta de Documento Con revision 
	Documento obtenerDocumentoSolicitudDetalle(Long codigo,Long codigoSolicitud,Long codigoRevision);
	Documento obtenerDocumentoRevisionDetalle(Long codigo,Long codigoRevision);
	List<Codigo> obtenerCodigoAnterior(DocumentoRequest documentoRequest, PageRequest pageRequest);
	Documento obtenerDocumentoHistorial(Long codigo);
	Documento obtenerDocumentoHistorialRev(Long codigo, Long idRevision);
	Paginacion getPaginacion();
	Error getError();
	Documento guardarDocumento(Documento documento, Long codigo, String usuario, Long idUsuario);
	Boolean eliminarDocumento(Long codigo, String usuario);
	List<Documento> obtenerDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, Long idjerarquia);
	String generarCodigoDocumento (Long codigoGerencia, Long codigoTipoDocumento);
	List<Documento> obtenerDocumentoRevision(DocumentoRequest documentoRequest,Long codigoRevision, PageRequest pageRequest);
	Flujo bloquearDocumento(Flujo bitacora, String usuario);
	Flujo desBloquearDocumento(String usuario);
	Documento guardarDocumentoTraslado(Documento documento, Long codigo, String iUsuario, Long idUsuario);
}