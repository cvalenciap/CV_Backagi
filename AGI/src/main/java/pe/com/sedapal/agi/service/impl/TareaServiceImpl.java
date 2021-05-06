package pe.com.sedapal.agi.service.impl;


import java.io.File;
import pe.com.sedapal.agi.security.config.UserAuth;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import pe.com.sedapal.agi.dao.ITareaDAO;
import pe.com.sedapal.agi.dao.impl.DocumentoDAOImpl;
import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.SolicitudDocumentoComplementario;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IExcel;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.com.sedapal.agi.service.ITareaService;
import pe.com.sedapal.agi.util.UArchivo;
import pe.com.sedapal.agi.util.UConstante;
import pe.sedapal.componentes.fs.ClientFS;

@Service
public class TareaServiceImpl implements ITareaService {

	//Subir documento a Google Drive - Inicio.
		@Autowired
		//Environment env;	
		//private String endpointServidor;	
		private static String carpetaResources;
	    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
	    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();    
	    private static  String TOKENS_DIRECTORY_PATH;
	    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
	    private static  String CREDENTIALS_FILE_PATH;
	    //Subir documento a Google Drive - Fin.
	    
    @Autowired
    ITareaDAO tareaDAO;
   @Autowired
	private IExcel service;	
    @Autowired
    IFileServerService fileServer;
    
    @Autowired
	private IFileServerService fileServerService;
    
    @Autowired
    Environment env;
    
    @Autowired
	SessionInfo session;
  	
    private String endpointServidor;
    private String informaciondocumentada;
    
    private String UArchivocarpetaResources;
    @Override
    public List<Cancelacion> obtenerSolicitudesCancelacion(TareaRequest tareaRequest, PageRequest pageRequest) {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return tareaDAO.obtenerSolicitudesCancelacion(tareaRequest, pageRequest, new Long(((UserAuth)principal).getCodPerfil()));
    }
    
    @Override
    public List<Cancelacion> obtenerSolicitudesCancelacionReporte(TareaRequest tareaRequest, PageRequest pageRequest) {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return tareaDAO.obtenerSolicitudesCancelacionReporte(tareaRequest, pageRequest, new Long(((UserAuth)principal).getCodPerfil()));
    }

    
    //Guardar
    @Override
    public boolean crearActualizarSolicitudCancelacion(Cancelacion cancelacion,Optional<MultipartFile> file) {
    	boolean respuesta = false;
    	try {
    		endpointServidor = env.getProperty("app.config.servidor.fileserver");
    		UArchivocarpetaResources =env.getProperty("app.config.paths.toke.directory");
    		informaciondocumentada=env.getProperty("app.config.servidor.fileserver.agi");
    		
    		ClientFS cliente = new ClientFS();
    		//MultipartFile archivo = null;
        	if(file.isPresent()) {
        		if(cancelacion.getRutaArchivoSustento() != null) {
        			String rutaFileServer= "";
            		rutaFileServer = cancelacion.getRutaArchivoSustento();
            		rutaFileServer = rutaFileServer.replace(endpointServidor,"");     
        			this.fileServerService.eliminarArchivoFileServerPdfAdjuno(rutaFileServer);
        		}
        		UArchivo.crearCarpeta(UArchivocarpetaResources+"carpetagenerador");
        		File archivoObtenido = UArchivo.convert(file.get(),UArchivocarpetaResources+"carpetagenerador/");
        		String rutaSaliente = informaciondocumentada; 
        		String rutaFileServer = cliente.subirArchivo(endpointServidor+"/"+rutaSaliente, archivoObtenido);
        		rutaFileServer = rutaFileServer.replace(endpointServidor, "");
        		cancelacion.setRutaArchivoSustento(rutaFileServer);
        		cancelacion.setNombreArchivoSustento(file.get().getOriginalFilename());
        		UArchivo.eliminarCarpeta(UArchivocarpetaResources+"carpetagenerador");
        	}else {        		
        		String rutaFileServer= "";
        		rutaFileServer = cancelacion.getRutaArchivoSustento();
        		rutaFileServer = rutaFileServer.replace(endpointServidor,"");        		
        		cancelacion.setRutaArchivoSustento(rutaFileServer);
        	}
        	
        	DatosAuditoria datosAuditoria = new DatosAuditoria();
        	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
        	datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
        	cancelacion.setIdColaborador(new Long(((UserAuth)principal).getCodPerfil()));
        	cancelacion.setDatosAuditoria(datosAuditoria);
            respuesta = tareaDAO.crearActualizarDatosSolicitudCancelacion(cancelacion);
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	return respuesta;
    }
    
    @Override
    public String subirDocumentoGoogleDrive(TareaRequest tareaRequest, String idDocGoogle) {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      return tareaDAO.subirDocumentoGoogleDrive(tareaRequest, idDocGoogle, (((UserAuth)principal).getUsername()));
    }    
    
    		
    @Override
    public Error obtenerError() {
        return tareaDAO.obtenerError();
    }

    @Override
    public Paginacion obtenerPaginacion() { 
    	return this.tareaDAO.obtenerPaginacion(); 
    }

	@Override
	public Cancelacion obtenerSolicitudCancelacion(Long idCancelacion) {
		// TODO Auto-generated method stub
		endpointServidor = env.getProperty("app.config.servidor.fileserver");
		Cancelacion cancelacionObtenido = this.tareaDAO.obtenerSolicitudCancelacion(idCancelacion);
		List<SolicitudDocumentoComplementario> listaSolicitudes = this.tareaDAO.obtenerSolicitudesDocumentoComplementario(idCancelacion);
		cancelacionObtenido.setListaSolicitudesDocComp(listaSolicitudes);
		cancelacionObtenido.setRutaArchivoSustento(endpointServidor+cancelacionObtenido.getRutaArchivoSustento());
		return cancelacionObtenido;
	}

	@Override
	public boolean aprobarRechazarSolicitudCancelacion(Cancelacion cancelacion) {
		// TODO Auto-generated method stub
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
    	datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
    	if (cancelacion.getIdColaborador() == null) {
    		cancelacion.setIdColaborador(new Long(((UserAuth)principal).getCodPerfil()));
		}     	
    	cancelacion.setDatosAuditoria(datosAuditoria);
    	if(cancelacion.getTipo() == 1) {
    		cancelacion.setSustentoRechazo(null);
    	}
    	return this.tareaDAO.aprobarRechazarSolicitudCancelacion(cancelacion);
	}

	@Override
	public boolean cancelarDocumento(Cancelacion cancelacion) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
    	datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
    	datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
    	cancelacion.setIdColaborador(new Long(((UserAuth)principal).getCodPerfil()));
    	cancelacion.setDatosAuditoria(datosAuditoria);
		return this.tareaDAO.cancelarDocumento(cancelacion);
	}
	
	///CGUERRA
	@Override
	public boolean cancelarDocumentoGoogleDrive(Cancelacion cancelacion) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
    	datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
    	datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
    	cancelacion.setIdColaborador(new Long(((UserAuth)principal).getCodPerfil()));
    	cancelacion.setDatosAuditoria(datosAuditoria);
		return this.tareaDAO.cancelarDocumentoGoogleDrive(cancelacion);
	}
	///CGUERRA
	
	
	
	
	
	
	

	@Override
	public String obtenerUltimaRutaCopiaControladaDocumento(Long idDocumento) {
		return this.tareaDAO.obtenerUltimaRutaCopiaControladaDocumento(idDocumento);
	}

	@Override
	public List<Documento> obtenerDocumentosJerarquicos(Long idDocumentoHijo) {
		// TODO Auto-generated method stub
		return this.tareaDAO.obtenerDocumentosJerarquicos(idDocumentoHijo);
	}

	@Override
	public Integer obtenerCantidadSolicitudCancelacion(Long idDocumento) {
		// TODO Auto-generated method stub
		return this.tareaDAO.obtenerCantidadSolicitudCancelacion(idDocumento);
	}
	
	/*Excel Inicio*/	
	public String generarExcelPlazo(TareaRequest tareaRequest, PageRequest pageRequest) throws IOException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Cancelacion> listaCancelacion = new ArrayList<>();	
		listaCancelacion = this.tareaDAO.obtenerSolicitudesCancelacionReporte(tareaRequest, pageRequest,new Long(((UserAuth)principal).getCodPerfil()));					
		return service.escribirExcelCancelacion(listaCancelacion);
	}
	/*Excel Fin*/
	
	@Override
    public List<Cancelacion> obtenerCancelacionAprobacion(TareaRequest tareaRequest, PageRequest pageRequest) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return tareaDAO.obtenerCancelacionAprobacion(tareaRequest, pageRequest, new Long(((UserAuth)principal).getCodPerfil()));
    }

}