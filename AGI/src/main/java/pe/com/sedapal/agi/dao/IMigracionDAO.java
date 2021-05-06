package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.Historico;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IMigracionDAO {
	List<Documento> obtenerDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest);
	List<Documento> obtenerConsultaHistorica(DocumentoRequest documentoRequest, PageRequest pageRequest);
	
	Documento obtenerDocumentoDetalle(Long codigo);
	List<Codigo> obtenerCodigoAnterior(DocumentoRequest documentoRequest, PageRequest pageRequest);
	Documento obtenerDocumentoHistorial(Long codigo);
	Paginacion getPaginacion();
	Error getError();
	Documento guardarDocumento(Documento documento, Long codigo, String usuario, Long idUsuario);
	Boolean eliminarDocumento(Long codigo, String usuario);
	List<Documento> obtenerDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, Long idjerarquia);
	String generarCodigoDocumento (Long codigoGerencia, Long codigoTipoDocumento);
	List<Colaborador> obtenerColaboradores(ColaboradorRequest colaboradorRequest, PageRequest pageRequest);
	List<Historico> obtenerHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest);
	List<Historico> obtenerDetalleHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest);
}