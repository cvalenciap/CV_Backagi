package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.response_objects.Error;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Historico;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IMigracionService {
	List<Documento> obtenerDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest);
	List<Documento> obtenerHistoryMigracion(DocumentoRequest documentoRequest, PageRequest pageRequest);
	
	Documento obtenerDocumentoDetalle(Long codigo);
	List<Codigo> obtenerCodigoAnterior(DocumentoRequest documentoRequest, PageRequest pageRequest);
	Documento obtenerDocumentoHistorial(Long codigo);
	Error getError();
	Paginacion getPaginacion();
	Documento crearDocumento(Documento documento);
	Documento actualizarDocumento(Documento documento, Long codigo);
	Boolean eliminarDocumento(Long codigo);
	List<Documento> obtenerDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, Long idjerarquia);
	String generarCodigoDocumento(Long codigoGerencia, Long codigoTipoDocumento);
	//Excel
	String generarExcelPlazo(DocumentoRequest documentoRequest, PageRequest pageRequest) throws IOException;
	List<Colaborador> obtenerColaboradores(ColaboradorRequest colaboradorRequest, PageRequest pageRequest);
	List<Historico> obtenerHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest);
	//cguerra Inicio	
	boolean crearDocumentoFileMigracion(Documento documento,Optional<MultipartFile> file,Optional<MultipartFile> fileDoc); 
	//cguerra Fin
	List<Historico> obtenerDetalleHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest);
}