package pe.com.sedapal.agi.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IDocumentoDAO;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.UploadResponse;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IDocumentoService;
import pe.com.sedapal.agi.service.IExcel;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.com.sedapal.agi.util.UConstante;

@Service
public class DocumentoServiceImpl implements IDocumentoService{
    static String rutaAgi ;
    
    private static final Logger LOGGER = Logger.getLogger(DocumentoServiceImpl.class);	
    
    @Autowired
    Environment env;    

	@Autowired
	private IExcel service;	
	
	@Autowired
	private IFileServerService fileServerService;
	
	@Autowired
	SessionInfo session;
	
	@Autowired
	private IDocumentoDAO dao;
	
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
	
	@Override
	public List<Documento> obtenerDocumentoHisto(DocumentoRequest documentoRequest, PageRequest pageRequest){
		List<Documento> lista = this.dao.obtenerDocumentoHisto(documentoRequest, pageRequest);
		return lista;
	}
	
	/*@Override
	public List<Documento> obtenerDocumentoHist(DocumentoRequest documentoRequest, PageRequest pageRequest){
		List<Documento> lista = this.dao.obtenerDocumento(documentoRequest, pageRequest);
		return lista;
	}*/
	
	//Documento Solicitud
		@Override
		public List<Documento> obtenerDocumentoSolicitud(DocumentoRequest documentoRequest, PageRequest pageRequest){
			List<Documento> lista = this.dao.obtenerDocumentoSolicitud(documentoRequest, pageRequest);
			return lista;
		}
	
	//Buscamos documento por ID JERAR
	@Override
	public List<Documento> obtenerDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, Long idjerarquia){
		List<Documento> lista = this.dao.obtenerDocumentoj(documentoRequest, pageRequest,idjerarquia);
		return lista;
	}	
	@Override
	public Documento obtenerDocumentoDetalle(Long codigo){
		Documento objeto = this.dao.obtenerDocumentoDetalle(codigo);
		return objeto;
	}

	@Override
	public Documento obtenerDocumentoHistorialRev(Long codigo, Long idRevision){
		Documento objeto = this.dao.obtenerDocumentoHistorialRev(codigo, idRevision);
		return objeto;
	}
	
	
	@Override
	public Documento obtenerDocumentoSolicitudDetalle(Long codigo,Long codigoSolicitud,Long codigoRevision){
		Documento objeto = this.dao.obtenerDocumentoSolicitudDetalle(codigo,codigoSolicitud,codigoRevision);
		return objeto;
	}
	//revision
	@Override
	public Documento obtenerDocumentoRevisionDetalle(Long codigo,Long codigoRevision){
		Documento objeto = this.dao.obtenerDocumentoRevisionDetalle(codigo,codigoRevision);
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
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		Documento objeto = this.dao.guardarDocumento(documento, null,(((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil())/*new Long(((UserAuth)principal).getCodPerfil()))*/);
		return objeto;
	}
	
	
	@Override
	public Documento actualizarDocumento(Documento documento, Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Documento objeto = this.dao.guardarDocumento(documento, codigo, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()));
		return objeto;
	}
	//actualizarDocumentoTraslado
	@Override
	public Documento actualizarDocumentoTraslado(Documento documento, Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Documento objeto = this.dao.guardarDocumentoTraslado(documento, codigo, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()));
		return objeto;
	}
	@Override
	public Boolean eliminarDocumento(Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.eliminarDocumento(codigo, (((UserAuth)principal).getUsername()));
	}
	
	@Override
	public String generarCodigoDocumento(Long codigoGerencia, Long codigoTipoDocumento) {
		return this.dao.generarCodigoDocumento(codigoGerencia, codigoTipoDocumento);
	}
	
	/*Excel Inicio*/
	public String generarExcelPlazo(DocumentoRequest documentoRequest, PageRequest pageRequest) throws IOException {
		List<Documento> listaDocumentos = new ArrayList<>();
		listaDocumentos = this.dao.obtenerDocumento(documentoRequest, pageRequest);
		return service.escribirExcel(listaDocumentos);
	}
	/*Excel Fin*/
	
	public byte[] generarPdfPlazo(DocumentoRequest documentoRequest, PageRequest pageRequest) throws IOException {
		List<Documento> listaDocumentos = new ArrayList<>();
		listaDocumentos = this.dao.obtenerDocumento(documentoRequest, pageRequest);
		return service.generarPdfDocumento(listaDocumentos);
	}
	
	public Map<String,Object> manejarDocumentos(MultipartFile file){
		Map<String,Object> mapaRespuesta = new HashMap<>();
		rutaAgi = env.getProperty("app.config.servidor.fileserver");
		try {
			InputStream is = file.getInputStream();
			String nombreArchivo = file.getOriginalFilename();
	    	byte[] bytesArchivo = file.getBytes();
	    	UploadResponse uploadArchivo = this.fileServerService.cargarArchivoFileServer(bytesArchivo, nombreArchivo,"pdf");
	    	String rutaArchivo = uploadArchivo.getUrl();


	    	rutaArchivo  =rutaArchivo.replace(rutaAgi,"");
	    	mapaRespuesta.put("rutaArchivo",rutaArchivo);
	    	is.close();

		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			e.printStackTrace();
		}
		return mapaRespuesta;
	}
	
	@Override
	public Flujo bloquearDocumento(Flujo bitacora) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Flujo objeto = this.dao.bloquearDocumento(bitacora, (((UserAuth)principal).getUsername()));
		return objeto;
	}
	@Override
	public Documento obtenerIdGoogle(Long codigo, Long numero, Long idrevi) {
		// TODO Auto-generated method stub
		return this.dao.obtenerIdGoogle(codigo,numero,idrevi);
	}
	@Override
	public Flujo desBloquearDocumento() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Flujo objeto = this.dao.desBloquearDocumento((((UserAuth)principal).getUsername()));
//		Flujo objeto = this.dao.desBloquearDocumento(((UserDetails)principal).getUsername());
		return objeto;
	}

}