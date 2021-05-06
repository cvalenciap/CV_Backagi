package pe.com.sedapal.agi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import pe.com.sedapal.agi.dao.IDocumentoDAO;
import pe.com.sedapal.agi.dao.IMigracionDAO;
import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.Historico;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.UploadResponse;
//import pe.com.sedapal.agi.service.IDocumentoService;
import pe.com.sedapal.agi.service.IExcel;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.com.sedapal.agi.service.IMigracionService;
import pe.com.sedapal.agi.util.UArchivo;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UMarcaAgua;
import pe.sedapal.componentes.fs.ClientFS;

@Service
public class MigracionServiceImpl implements IMigracionService{
	
	@Autowired
	private IExcel service;	
	
	@Autowired
	private IFileServerService fileServerService;
	
    @Autowired
    Environment env;
	
	static String iUsuario;	
	
	static Long idUsuario;	
	
	
	private String endpointServidor;
	private String informacionadjunta;
		
	@Autowired
	private IMigracionDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Documento> obtenerDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest){
		List<Documento> lista = this.dao.obtenerDocumento(documentoRequest, pageRequest);
		return lista;
	}
	//Consulta Historica
	@Override
	public List<Documento> obtenerHistoryMigracion(DocumentoRequest documentoRequest, PageRequest pageRequest){
		List<Documento> lista = this.dao.obtenerConsultaHistorica(documentoRequest, pageRequest);
		return lista;
	}
	
	@Override
	public List<Colaborador> obtenerColaboradores(ColaboradorRequest colaboradorRequest, PageRequest pageRequest) {
		List<Colaborador> colaboradores = this.dao.obtenerColaboradores(colaboradorRequest, pageRequest);
		return colaboradores;
	}

	//Buscamos documento por ID JERAR
	@Override
	public List<Documento> obtenerDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, Long idjerarquia){
		List<Documento> lista = this.dao.obtenerDocumentoj(documentoRequest, pageRequest,idjerarquia);
		return lista;
	}
	/*
	public List<Documento> obtenerDocumentoj(Long idjerarquia) {
		List<Documento> listaDocumento = new ArrayList<>();
		List<Documento> documento = new List<Documento>();
		DocumentoRequest documentoRequest = new DocumentoRequest();
		PageRequest pageRequest = new PageRequest();
		documentoRequest.setIdjerarquia(idjerarquia);
		listaDocumento = this.dao.obtenerDocumentoj(documentoRequest, pageRequest,idjerarquia);
				if (listaDocumento.size() > 0) {
					documento = listaDocumento.get(0);
		}
		return documento;
	}
*/
	@Override
	public Documento obtenerDocumentoDetalle(Long codigo){
		Documento objeto = this.dao.obtenerDocumentoDetalle(codigo);
		return objeto;
	}
	
	@Override
	public List<Codigo> obtenerCodigoAnterior(DocumentoRequest documentoRequest, PageRequest pageRequest){
		List<Codigo> lista = this.dao.obtenerCodigoAnterior(documentoRequest, pageRequest);
		return lista;
	}

	@Override
	public Documento obtenerDocumentoHistorial(Long codigo){
		Documento objeto = this.dao.obtenerDocumentoHistorial(codigo);
		return objeto;
	}
    //Registro Documento 
	@Override
	public Documento crearDocumento(Documento documento) {
		Documento objeto = this.dao.guardarDocumento(documento, null, iUsuario,idUsuario);
		return objeto;
	}
	
	@Override
	public Documento actualizarDocumento(Documento documento, Long codigo) {
		Documento objeto = this.dao.guardarDocumento(documento, codigo, iUsuario,idUsuario);
		return objeto;
	}
	
	@Override
	public Boolean eliminarDocumento(Long codigo) {
		return this.dao.eliminarDocumento(codigo, iUsuario);
	}

	@Override
	public String generarCodigoDocumento(Long codigoGerencia, Long codigoTipoDocumento) {
		return this.dao.generarCodigoDocumento(codigoGerencia, codigoTipoDocumento);
	}
	
	/*Excel Inicio*/
	public String generarExcelPlazo(DocumentoRequest documentoRequest, PageRequest pageRequest) throws IOException {
		List<Documento> listaDocumentos = new ArrayList<>();
		// validar permiso 
		//String buscarArea = session.validatePermiso(SessionConstants.OPCION_BANDEJA_ENT_AREA) ? "A" : "U";
		listaDocumentos = this.dao.obtenerDocumento(documentoRequest, pageRequest);
				/*buscarArea,
				((UserAuth)principal).getCodPerfil()),
				session.getUserProfile().getCodArea(),
				(((UserAuth)principal).getUsername()));*/		
		return service.escribirExcel(listaDocumentos);
	}
	/*Excel Fin*/
	@Override
	public List<Historico> obtenerHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		List<Historico> lista = this.dao.obtenerHistorico(documentoRequest, pageRequest);
		return lista;
	}
	
	
	
	/**CGuerra Inicio**/
	@Override
    public boolean crearDocumentoFileMigracion(Documento documento,Optional<MultipartFile> file,Optional<MultipartFile> fileDoc) {
    	boolean respuesta = false;
    	endpointServidor = env.getProperty("app.config.servidor.fileserver");
    	try {
    		ClientFS cliente = new ClientFS();   
    		
    		if(fileDoc.isPresent()){
    			System.out.println("ejemplo de array");
    			System.out.println(fileDoc.get().getBytes());
    			UploadResponse uploadArchivoDoc = this.fileServerService.cargarArchivoFileServerDoc(fileDoc.get().getBytes(), fileDoc.get().getOriginalFilename());
    			System.out.println(fileDoc.get().getOriginalFilename());
    			
    			String rutaArchivoDoc = uploadArchivoDoc.getUrl();
    			rutaArchivoDoc = rutaArchivoDoc.replace(endpointServidor,"");
            	documento.getRevision().setRutaDocumentoGoogle(rutaArchivoDoc);
    		}   	
    		
        	if(file.isPresent()) {
        		if(documento.getRevision().getRutaDocumentoOriginal() != null) {
        			System.out.println("Va a eliminar Archivo");
        			cliente.eliminarArchivo(documento.getRevision().getRutaDocumentoOriginal());
        			System.out.println("Elimino Archivo");
        		}
        		InputStream myInputStream = new ByteArrayInputStream(file.get().getBytes());        		
        		UploadResponse uploadArchivoPdf = this.fileServerService.cargarArchivoFileServer(file.get().getBytes(), file.get().getOriginalFilename(),"pdf");
            	String rutaArchivoPdf = uploadArchivoPdf.getUrl();
            	InputStream isPdfCopiaNoControlada = new ByteArrayInputStream(file.get().getBytes());
            	OutputStream osCopiaNoControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaNoControlada,"COPIA NO CONTROLADA");
            	isPdfCopiaNoControlada.close();
            	InputStream isPdfCopiaControlada = new ByteArrayInputStream(file.get().getBytes());
            	OutputStream osCopiaControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaControlada,"COPIA CONTROLADA");
            	isPdfCopiaControlada.close();
            	InputStream isPdfCopiaObsoleta = new ByteArrayInputStream(file.get().getBytes());
            	OutputStream osCopiaObsoleta = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaObsoleta,"COPIA OBSOLETA");
            	isPdfCopiaObsoleta.close();
            	//Cargar Archivos
            	UploadResponse uploadCopiaNoControlada = this.fileServerService.cargarArchivoFileServer(((ByteArrayOutputStream)osCopiaNoControlada).toByteArray(), file.get().getOriginalFilename()+"_copia_no_controlada","pdf");
            	String rutaCopiaNoControlada = uploadCopiaNoControlada.getUrl();
            	UploadResponse uploadCopiaControlada = this.fileServerService.cargarArchivoFileServer(((ByteArrayOutputStream)osCopiaControlada).toByteArray(), file.get().getOriginalFilename()+"_copia_controlada","pdf");
            	String rutaCopiaControlada = uploadCopiaControlada.getUrl();
            	UploadResponse uploadCopiaObsoleta = this.fileServerService.cargarArchivoFileServer(((ByteArrayOutputStream)osCopiaObsoleta).toByteArray(), file.get().getOriginalFilename()+"_copia_obsoleta","pdf");
            	String rutaCopiaObsoleta = uploadCopiaObsoleta.getUrl();
            	
            	rutaCopiaControlada = rutaCopiaControlada.replace(endpointServidor,"");
            	rutaArchivoPdf = rutaArchivoPdf.replace(endpointServidor,"");
            	rutaCopiaNoControlada = rutaCopiaNoControlada.replace(endpointServidor,"");
            	rutaCopiaObsoleta = rutaCopiaObsoleta.replace(endpointServidor,"");
            	
            	documento.getRevision().setRutaDocumentoCopiaConf(rutaCopiaControlada);
            	documento.getRevision().setRutaDocumentoOriginal(rutaArchivoPdf);
            	documento.getRevision().setRutaDocumentoCopiaNoConf(rutaCopiaNoControlada);
            	documento.getRevision().setRutaDocumentoCopiaObso(rutaCopiaObsoleta);
            	
            	osCopiaNoControlada.close();
            	osCopiaControlada.close();
            	osCopiaObsoleta.close();
        	
        	}
        	
        	DatosAuditoria datosAuditoria = new DatosAuditoria();
        	datosAuditoria.setUsuarioCreacion(iUsuario);
        	datosAuditoria.setUsuarioModificacion(iUsuario);
        	
		  /** Grabación de los tabsMigración **/
		  Documento objeto = this.dao.guardarDocumento(documento, null, iUsuario,idUsuario);
			}catch(IOException e) {
				e.printStackTrace();
			}    	
			return respuesta;
		}
		/**CGuerra Fin**/
	
	
	@Override
	public List<Historico> obtenerDetalleHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		List<Historico> lista = this.dao.obtenerDetalleHistorico(documentoRequest, pageRequest);
		return lista;
	}
}