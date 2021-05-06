package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IDocumentoService {
	List<Documento> obtenerDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest);
	List<Documento> obtenerDocumentoHisto(DocumentoRequest documentoRequest, PageRequest pageRequest);
	//List<Documento> obtenerDocumentoHist(DocumentoRequest documentoRequest, PageRequest pageRequest);
	//List<Documento> obtenerDocumentoRevision(DocumentoRequest documentoRequest, PageRequest pageRequest);
	List<Documento> obtenerDocumentoSolicitud(DocumentoRequest documentoRequest, PageRequest pageRequest);
	Documento obtenerDocumentoDetalle(Long codigo);
	Documento obtenerDocumentoHistorialRev(Long codigo, Long idRevision);
	//Documento obtenerDocumentoDetalleHist(Long codigo, Long idRevision);
	//Documento obtenerDocumentoRuta(Long iddocu,Long idrevi);
	Documento obtenerDocumentoSolicitudDetalle(Long codigo,Long codigoSolicitud,Long codigoRevision);
	Documento obtenerDocumentoRevisionDetalle(Long codigo,Long codigoRevision);
	List<Codigo> obtenerCodigoAnterior(DocumentoRequest documentoRequest, PageRequest pageRequest);
	Documento obtenerDocumentoHistorial(Long codigo);
	//Documento obtenerDocumentoHistorialRev(Long codigo, Long idRevision);
	//Documento obtenerDocumentoHistorial(Long codigo, Long idRevision);
	Error getError();
	Paginacion getPaginacion();
	Documento crearDocumento(Documento documento);
	Documento actualizarDocumento(Documento documento, Long codigo);	
	//actualizarDocumentoTraslado
	Documento actualizarDocumentoTraslado(Documento documento, Long codigo);	
	Boolean eliminarDocumento(Long codigo);
	List<Documento> obtenerDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, Long idjerarquia);	
	String generarCodigoDocumento(Long codigoGerencia, Long codigoTipoDocumento);
	//Excel
	String generarExcelPlazo(DocumentoRequest documentoRequest, PageRequest pageRequest) throws IOException;
	byte[] generarPdfPlazo(DocumentoRequest documentoRequest, PageRequest pageRequest) throws IOException;
	public Map<String,Object> manejarDocumentos(MultipartFile file);
	Flujo bloquearDocumento(Flujo bitacora);
	Flujo desBloquearDocumento();	
	
	public Documento obtenerIdGoogle(Long codigo,Long numero,Long idrevi);
}