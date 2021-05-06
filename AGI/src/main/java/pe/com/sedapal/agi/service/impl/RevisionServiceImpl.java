package pe.com.sedapal.agi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.itextpdf.text.log.SysoCounter;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.dao.IRevisionDAO;
import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Jefes;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequestAdjunto;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.UploadResponse;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.com.sedapal.agi.service.IRevisionService;
import pe.com.sedapal.agi.util.UArchivo;
import pe.com.sedapal.agi.util.UMarcaAgua;
import pe.sedapal.componentes.fs.ClientFS;
import pe.com.sedapal.agi.security.config.UserAuth;
@Service
public class RevisionServiceImpl implements IRevisionService{
	static String rutaAgi ;
	
	@Autowired
	Environment env;	
	private String endpointServidor;
	
	@Autowired
	SessionInfo session;
	
	@Autowired	
	private IRevisionDAO dao;
	
		
	
	@Autowired
	private IFileServerService fileServerService;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}
	private  String carpetaResources;  
	private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();	
	private static  String TOKENS_DIRECTORY_PATH;
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
	private static  String CREDENTIALS_FILE_PATH;		
	private String Indicadorword= "1";
    private String Indicadorexcel= "1";
    private String IndicadorEliminar= "0";  
    boolean existe=false; 

	@Override
	public List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Revision> lista = this.dao.obtenerRevision(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public List<Revision> obtenerRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Revision> lista = this.dao.obtenerRevisionFase(revisionRequest, pageRequest, new Long(((UserAuth)principal).getCodPerfil()));
		return lista;
	}
	
	@Override
	public Boolean eliminarProgramacion(Long id) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.eliminarProgramacion(id, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()));
	}
	
	@Override
	public Boolean eliminarRevision(Long id) {
		return this.dao.eliminarRevision(id);
	}
	
	@Override
	public List<Revision> grabarPrograma(String idProg,String codFichaLogueado, String idEstProg, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Revision> lista = this.dao.grabarPrograma(idProg,codFichaLogueado, idEstProg, revisionSelecc, revisionNoSelecc, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()) );
		return lista;
	}

	@Override
	public List<Revision> grabarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Revision> lista = this.dao.grabarDistribucion(codFichaLogueado, revisionSelecc,revisionNoSelecc , (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()) );
		return lista;
	}
	
	@Override
	public List<Revision> finalizarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Revision> lista = this.dao.finalizarDistribucion(codFichaLogueado, revisionSelecc, revisionNoSelecc, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil()));
		return lista;
	}
	
	@Override
	public Revision crearRevision(Revision revisionRequest){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Revision lista = this.dao.crearRevision(revisionRequest, (((UserAuth)principal).getUsername()), new Long(((UserAuth)principal).getCodPerfil())) ;
		return lista;
	}

	@Override
	public List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest) {
		List<Revision> lista = this.dao.obtenerListaTareaAprobar(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public Revision crearDocumentoGoogleDrive (String idDocGoogle, Long idRevi){
		Revision lista = this.dao.crearDocumentoGoogleDrive(idDocGoogle, idRevi);
		return lista;
	}
	
	@Override
	public Revision rechazarDocumento(Long idDocumento, Revision revisionRechazo) {
		Revision lista = this.dao.rechazarDocumento(idDocumento, revisionRechazo);
		return lista;
	}
	
	@Override
	public List<Revision> consultarProgramacion(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Revision> lista = this.dao.consultarProgramacion(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public List<Revision> consultarProgramacionPorIdProg(RevisionRequest revisionRequest){
		List<Revision> lista = this.dao.consultarProgramacionPorIdProg(revisionRequest);
		return lista;
	}
	
	@Override
	public List<Jefes> obtenerJefes(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Jefes> lista = this.dao.obtenerJefes(revisionRequest, pageRequest);
		return lista;
	}
	
	/**
     * Crea objeto de autenticación.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return objeto de autenticación.
     */
    public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    	RevisionServiceImpl.CREDENTIALS_FILE_PATH= env.getProperty("app.config.paths.carpetaResources");
    	RevisionServiceImpl.TOKENS_DIRECTORY_PATH= env.getProperty("app.config.paths.toke.directory");	
    	InputStream in = new java.io.FileInputStream(new java.io.File(CREDENTIALS_FILE_PATH));	    	
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

	@Override
	public List<Revision> consultarProgramaciones(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Revision> lista = this.dao.consultarProgramaciones(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public List<Revision> consultarDetDistribuciones(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Revision> lista = this.dao.consultarDetDistribuciones(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public Revision guardarDocumentoRevision(Revision revision) {		
		try {
		String IdGoogleDriveTipodoc = revision.getIdDocGoogleDrive();
		//String IdGoogleDriveTipodoc1 = revision.getIdDocGoogleDrive();		
		carpetaResources = env.getProperty("app.config.paths.toke.directory");
		rutaAgi = env.getProperty("app.config.servidor.fileserver");
		String rutaArchivoDocx = "";
    	String rutaArchivoPdf= "";
    	
		UArchivo.crearCarpeta(carpetaResources+"prueba-carpeta");
    	long id = System.currentTimeMillis();
    	String nombreArchivo = "archivo_"+id;    	
    	String idDocumentoGoogle = IdGoogleDriveTipodoc.substring(0, IdGoogleDriveTipodoc.length()-4);
    	String TipodocIdGoogleDrive = IdGoogleDriveTipodoc.substring(IdGoogleDriveTipodoc.length()-4,IdGoogleDriveTipodoc.length());    	
    	System.out.println(idDocumentoGoogle);
    	System.out.println(TipodocIdGoogleDrive);
    	
    	if(TipodocIdGoogleDrive.equals("word")) {
    			try {    				
    				System.out.println("EXISTE ENTRO");    				
    				URL urlWord = new URL("https://docs.google.com/document/d/"+idDocumentoGoogle+"/export?format=docx");
    	    		java.io.File destinationWord = new java.io.File(carpetaResources+"prueba-carpeta/"+nombreArchivo+".docx");        
    	    		 FileUtils.copyURLToFile(urlWord, destinationWord);
    	    		
    	    		InputStream isDocx = new FileInputStream(destinationWord);
    	            OutputStream osDocx = UArchivo.convertirInputStreamAOutputStream(isDocx);       
    	            byte[] byteArrayDocx = ((ByteArrayOutputStream)osDocx).toByteArray();
    	            isDocx.close();            
    	            UploadResponse uploadArchivoDocx = this.fileServerService.cargarArchivoFileServer(byteArrayDocx, nombreArchivo,"docx");    	
    	             rutaArchivoDocx = uploadArchivoDocx.getUrl();
    	      
    	             //PDF             
    	            	 URL urlPdf = new URL("https://docs.google.com/document/d/"+idDocumentoGoogle+"/export?format=pdf");
    	                 java.io.File destinationPdf = new java.io.File(carpetaResources+"prueba-carpeta/"+nombreArchivo+".pdf");
    	                 FileUtils.copyURLToFile(urlPdf, destinationPdf);      	                 
    	                 InputStream isPdf = new FileInputStream(destinationPdf);       
    	                 /* CONVIERTE InputStream en Bytes */
    	                 OutputStream osPdf = UArchivo.convertirInputStreamAOutputStream(isPdf);
    	                 byte[] byteArrayPdf = ((ByteArrayOutputStream)osPdf).toByteArray();
    	                 isPdf.close(); 
    	                 UploadResponse uploadArchivoPdf = this.fileServerService.cargarArchivoFileServer(byteArrayPdf, nombreArchivo,"pdf");
    	             	 rutaArchivoPdf = uploadArchivoPdf.getUrl();    	         	
    	         	InputStream isPdfCopiaNoControlada = new FileInputStream(destinationPdf);
    	         	OutputStream osCopiaNoControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaNoControlada,"COPIA NO CONTROLADA");
    	         	isPdfCopiaNoControlada.close();
    	         	InputStream isPdfCopiaControlada = new FileInputStream(destinationPdf);
    	         	OutputStream osCopiaControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaControlada,"COPIA CONTROLADA");
    	         	isPdfCopiaControlada.close();
    	         	InputStream isPdfCopiaObsoleta = new FileInputStream(destinationPdf);
    	         	OutputStream osCopiaObsoleta = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaObsoleta,"COPIA OBSOLETA");
    	         	isPdfCopiaObsoleta.close();    	         	
    	         	//Cargar Archivos
    	         	UploadResponse uploadCopiaNoControlada = this.fileServerService.cargarArchivoFileServer(
    	         			((ByteArrayOutputStream)osCopiaNoControlada).toByteArray(), nombreArchivo+"_copia_no_controlada","pdf");
    	         	String rutaCopiaNoControlada = uploadCopiaNoControlada.getUrl();
    	         	UploadResponse uploadCopiaControlada = this.fileServerService.cargarArchivoFileServer(
    	         			((ByteArrayOutputStream)osCopiaControlada).toByteArray(), nombreArchivo+"_copia_controlada","pdf");
    	         	String rutaCopiaControlada = uploadCopiaControlada.getUrl();
    	         	UploadResponse uploadCopiaObsoleta = this.fileServerService.cargarArchivoFileServer(
    	         			((ByteArrayOutputStream)osCopiaObsoleta).toByteArray(), nombreArchivo+"_copia_obsoleta","pdf");
    	         	String rutaCopiaObsoleta = uploadCopiaObsoleta.getUrl(); 
    	         	
    	         	rutaArchivoPdf = rutaArchivoPdf.replace(rutaAgi, "");
    	         	rutaArchivoDocx = rutaArchivoDocx.replace(rutaAgi, "");
    	         	rutaCopiaNoControlada = rutaCopiaNoControlada.replace(rutaAgi, "");
    	         	rutaCopiaControlada = rutaCopiaControlada.replace(rutaAgi, "");
    	         	rutaCopiaObsoleta = rutaCopiaObsoleta.replace(rutaAgi, "");
    	         	
    	         	revision.setRutaDocumentoOriginal(rutaArchivoPdf);
    	         	revision.setRutaDocumentoGoogle(rutaArchivoDocx);         	
    	         	revision.setRutaDocumentoCopiaNoConf(rutaCopiaNoControlada);
    	         	revision.setRutaDocumentoCopiaConf(rutaCopiaControlada);
    	         	revision.setRutaDocumentoCopiaObso(rutaCopiaObsoleta);         	
    	         	osCopiaNoControlada.close();
    	         	osCopiaControlada.close();
    	         	osCopiaObsoleta.close();    	         	
    	         	//Eliminar del Google Drive      	    
    	      			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    	                 Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
    	                         .setApplicationName(APPLICATION_NAME)
    	                         .build();
    	            	service.files().delete(idDocumentoGoogle).execute();     	               
    			}catch(MalformedURLException e){
    				System.out.println("URL is invalid");
    				System.out.println(e);
    			}catch(IOException ex) {
    				System.out.println("URL is UNAVAILABLE");    				
    				System.out.println(ex);
    			}
    			
    	}else if(TipodocIdGoogleDrive.equals("exel")) {
            URL urlWord = new URL("https://docs.google.com/spreadsheets/d/"+idDocumentoGoogle+"/export?format=xlsx");
            java.io.File destinationWord = new java.io.File(carpetaResources+"prueba-carpeta/"+nombreArchivo+".xlsx");        
    		FileUtils.copyURLToFile(urlWord, destinationWord);	
    		
    		InputStream isDocx = new FileInputStream(destinationWord);
            OutputStream osDocx = UArchivo.convertirInputStreamAOutputStream(isDocx);       
            byte[] byteArrayDocx = ((ByteArrayOutputStream)osDocx).toByteArray();
            isDocx.close();            
            UploadResponse uploadArchivoDocx = this.fileServerService.cargarArchivoFileServer(byteArrayDocx, nombreArchivo,"xlsx");    	
            rutaArchivoDocx = uploadArchivoDocx.getUrl();  
            //PDF
            URL urlPdf = new URL("https://docs.google.com/spreadsheets/d/"+idDocumentoGoogle+"/export?format=pdf");
            java.io.File destinationPdf = new java.io.File(carpetaResources+"prueba-carpeta/"+nombreArchivo+".pdf");
            FileUtils.copyURLToFile(urlPdf, destinationPdf);
            InputStream isPdf = new FileInputStream(destinationPdf);       
            /* CONVIERTE InputStream en Bytes */
            OutputStream osPdf = UArchivo.convertirInputStreamAOutputStream(isPdf);
            byte[] byteArrayPdf = ((ByteArrayOutputStream)osPdf).toByteArray();
            isPdf.close(); 
            UploadResponse uploadArchivoPdf = this.fileServerService.cargarArchivoFileServer(byteArrayPdf, nombreArchivo,"pdf");
        	rutaArchivoPdf = uploadArchivoPdf.getUrl(); 
        	InputStream isPdfCopiaNoControlada = new FileInputStream(destinationPdf);
        	OutputStream osCopiaNoControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaNoControlada,"COPIA NO CONTROLADA");
        	isPdfCopiaNoControlada.close();
        	InputStream isPdfCopiaControlada = new FileInputStream(destinationPdf);
        	OutputStream osCopiaControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaControlada,"COPIA CONTROLADA");
        	isPdfCopiaControlada.close();
        	InputStream isPdfCopiaObsoleta = new FileInputStream(destinationPdf);
        	OutputStream osCopiaObsoleta = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaObsoleta,"COPIA OBSOLETA");
        	isPdfCopiaObsoleta.close();
        	//Cargar Archivos
        	UploadResponse uploadCopiaNoControlada = this.fileServerService.cargarArchivoFileServer(
        			((ByteArrayOutputStream)osCopiaNoControlada).toByteArray(), nombreArchivo+"_copia_no_controlada","pdf");
        	String rutaCopiaNoControlada = uploadCopiaNoControlada.getUrl();
        	UploadResponse uploadCopiaControlada = this.fileServerService.cargarArchivoFileServer(
        			((ByteArrayOutputStream)osCopiaControlada).toByteArray(), nombreArchivo+"_copia_controlada","pdf");
        	String rutaCopiaControlada = uploadCopiaControlada.getUrl();
        	UploadResponse uploadCopiaObsoleta = this.fileServerService.cargarArchivoFileServer(
        			((ByteArrayOutputStream)osCopiaObsoleta).toByteArray(), nombreArchivo+"_copia_obsoleta","pdf");
        	String rutaCopiaObsoleta = uploadCopiaObsoleta.getUrl(); 
        	
         	rutaArchivoPdf = rutaArchivoPdf.replace(rutaAgi, "");
         	rutaArchivoDocx = rutaArchivoDocx.replace(rutaAgi, "");
         	rutaCopiaNoControlada = rutaCopiaNoControlada.replace(rutaAgi, "");
         	rutaCopiaControlada = rutaCopiaControlada.replace(rutaAgi, "");
         	rutaCopiaObsoleta = rutaCopiaObsoleta.replace(rutaAgi, "");
        	
        	revision.setRutaDocumentoOriginal(rutaArchivoPdf);
        	revision.setRutaDocumentoGoogle(rutaArchivoDocx);
        	revision.setRutaDocumentoCopiaNoConf(rutaCopiaNoControlada);
        	revision.setRutaDocumentoCopiaConf(rutaCopiaControlada);
        	revision.setRutaDocumentoCopiaObso(rutaCopiaObsoleta);        	
        	osCopiaNoControlada.close();
        	osCopiaControlada.close();
        	osCopiaObsoleta.close();
       			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                  Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                          .setApplicationName(APPLICATION_NAME)
                          .build();
              	service.files().delete(idDocumentoGoogle).execute();
              	IndicadorEliminar= "1";
       		} 
    	UArchivo.eliminarCarpeta(carpetaResources+"prueba-carpeta");
    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {			
			ex.printStackTrace();
		}		
		return revision;		
	}
	  

	//capturamos ruta para insertar en la tabla revisiones
	@Override
	public Revision guardarDocumentoRevisionAdjunto(Revision revision) {		
		try {		
		//Creacion de la carpeta
		carpetaResources = env.getProperty("app.config.paths.toke.directory");
		rutaAgi = env.getProperty("app.config.servidor.fileserver");
		String rutaArchivoDocx = "";
    	String rutaArchivoPdf= "";    	
		UArchivo.crearCarpeta(carpetaResources+"prueba-carpeta");
		String nombreArchivo = "archivo_";
    	long id = System.currentTimeMillis();	
    	String RutaDocumento = rutaAgi + revision.getRutaDocumentoOriginal();
    	
    			try {
    	             //PDF             
    	            	 URL urlPdf = new URL(RutaDocumento+"?download=1");    	                 
    	                 java.io.File destinationPdf = new java.io.File(carpetaResources+"prueba-carpeta/"+nombreArchivo+".pdf");
    	                 FileUtils.copyURLToFile(urlPdf, destinationPdf);      	                 
    	                 InputStream isPdf = new FileInputStream(destinationPdf);       
    	                 
    	                 /* CONVIERTE InputStream en Bytes */
    	                 
    	                 OutputStream osPdf = UArchivo.convertirInputStreamAOutputStream(isPdf);
    	                 byte[] byteArrayPdf = ((ByteArrayOutputStream)osPdf).toByteArray();
    	                 isPdf.close(); 
    	                 UploadResponse uploadArchivoPdf = this.fileServerService.cargarArchivoFileServer(byteArrayPdf, nombreArchivo,"pdf");    	                 
    	             	 rutaArchivoPdf = uploadArchivoPdf.getUrl();  
    	             	 
    	         	InputStream isPdfCopiaNoControlada = new FileInputStream(destinationPdf);
    	         	OutputStream osCopiaNoControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaNoControlada,"COPIA NO CONTROLADA");
    	         	isPdfCopiaNoControlada.close();
    	         	InputStream isPdfCopiaControlada = new FileInputStream(destinationPdf);
    	         	OutputStream osCopiaControlada = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaControlada,"COPIA CONTROLADA");
    	         	isPdfCopiaControlada.close();
    	         	InputStream isPdfCopiaObsoleta = new FileInputStream(destinationPdf);
    	         	OutputStream osCopiaObsoleta = UMarcaAgua.agregarTextoMarcaAguaPDF(isPdfCopiaObsoleta,"COPIA OBSOLETA");
    	         	isPdfCopiaObsoleta.close();    	         	
    	         	//Cargar Archivos
    	         	UploadResponse uploadCopiaNoControlada = this.fileServerService.cargarArchivoFileServer(
    	         			((ByteArrayOutputStream)osCopiaNoControlada).toByteArray(), nombreArchivo+"_copia_no_controlada","pdf");
    	         	String rutaCopiaNoControlada = uploadCopiaNoControlada.getUrl();
    	         	UploadResponse uploadCopiaControlada = this.fileServerService.cargarArchivoFileServer(
    	         			((ByteArrayOutputStream)osCopiaControlada).toByteArray(), nombreArchivo+"_copia_controlada","pdf");
    	         	String rutaCopiaControlada = uploadCopiaControlada.getUrl();
    	         	UploadResponse uploadCopiaObsoleta = this.fileServerService.cargarArchivoFileServer(
    	         			((ByteArrayOutputStream)osCopiaObsoleta).toByteArray(), nombreArchivo+"_copia_obsoleta","pdf");
    	         	String rutaCopiaObsoleta = uploadCopiaObsoleta.getUrl(); 
    	         	
    	         	rutaArchivoPdf = rutaArchivoPdf.replace(rutaAgi, "");
    	         	rutaArchivoDocx = rutaArchivoDocx.replace(rutaAgi, "");
    	         	rutaCopiaNoControlada = rutaCopiaNoControlada.replace(rutaAgi, "");
    	         	rutaCopiaControlada = rutaCopiaControlada.replace(rutaAgi, "");
    	         	rutaCopiaObsoleta = rutaCopiaObsoleta.replace(rutaAgi, "");
    	         	
    	         	revision.setRutaDocumentoOriginal(rutaArchivoPdf);
    	         	revision.setRutaDocumentoGoogle(rutaArchivoDocx);         	
    	         	revision.setRutaDocumentoCopiaNoConf(rutaCopiaNoControlada);
    	         	revision.setRutaDocumentoCopiaConf(rutaCopiaControlada);
    	         	revision.setRutaDocumentoCopiaObso(rutaCopiaObsoleta);
    	         	
    	         	osCopiaNoControlada.close();
    	         	osCopiaControlada.close();
    	         	osCopiaObsoleta.close();  
    	         	    	               
    			}catch(MalformedURLException e){
    				System.out.println("URL is invalid");
    				System.out.println(e);
    			}catch(IOException ex) {
    				System.out.println("URL is UNAVAILABLE");    				
    				System.out.println(ex);
    			}
    			
    	
    	
    	
    	UArchivo.eliminarCarpeta(carpetaResources+"prueba-carpeta");
    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {			
			ex.printStackTrace();
		}		
		return revision;		
	}

	@Override
	public boolean registrarDocumentosRevision(Revision revision) {
		// TODO Auto-generated method stub
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.registrarRutasDocumentoRevision(revision, (((UserAuth)principal).getUsername()));
	}
	
	@Override
	public List<RevisionRequest> consultarListaProgramada(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<RevisionRequest> lista = this.dao.consultarListaProgramada(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public List<RevisionRequest> consultarListaDistribucionEjec(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<RevisionRequest> lista = this.dao.consultarListaDistribucionEjec(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public List<Revision> consultarListaEjec(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Revision> lista = this.dao.consultarListaEjec(revisionRequest, pageRequest);
		return lista;
	}
	///cguerra INICIO
		@Override
		@Transactional
		public boolean grabarEjecucionProg(List<RevisionRequestAdjunto> Revision) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			boolean respuesta = false;
			String rutaArchivoDoc ="";
			List<Revision> listaRevision 	= new ArrayList<>();
			Revision revision =null;
			rutaAgi = env.getProperty("app.config.servidor.fileserver");
			for (RevisionRequestAdjunto adjunto : Revision) {
				if (adjunto.getArchivo()!=null) {
				if (adjunto.getRutaDocumt() != null) {
					this.fileServerService.eliminarArchivoFileServerPdfAdjuno(adjunto.getRutaDocumt());
				}				
				UploadResponse uploadArchivoDoc = this.fileServerService.cargarArchivoFileServerPdfAdjunto(adjunto.getArchivo().getBytesArray(), adjunto.getArchivo().getNombreAdjunto());
				rutaArchivoDoc = uploadArchivoDoc.getUrl();
				rutaArchivoDoc = rutaArchivoDoc.replace(rutaAgi, "");						
			}		 
				adjunto.setRutaDocumtNueva(rutaArchivoDoc);
				respuesta = this.dao.guardarEjecucionesProg(adjunto, null, (((UserAuth)principal).getUsername()),
						new Long(((UserAuth)principal).getCodPerfil()));
			}
			return respuesta;
		}
		
		
		@Override
		@Transactional
		public boolean finalizarEjecucion(List<RevisionRequestAdjunto> Revision) {
			boolean respuesta = false;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String rutaArchivoDoc ="";
			List<Revision> listaRevision 	= new ArrayList<>();
			Revision revision =null;
			rutaAgi = env.getProperty("app.config.servidor.fileserver");
			for (RevisionRequestAdjunto adjunto : Revision) {
				if (adjunto.getArchivo()!=null) {
				if (adjunto.getRutaDocumt() != null) {
					this.fileServerService.eliminarArchivoFileServerPdfAdjuno(adjunto.getRutaDocumt());
				}				
				UploadResponse uploadArchivoDoc = this.fileServerService.cargarArchivoFileServerPdfAdjunto(adjunto.getArchivo().getBytesArray(), adjunto.getArchivo().getNombreAdjunto());
				rutaArchivoDoc = uploadArchivoDoc.getUrl();
				rutaArchivoDoc = rutaArchivoDoc.replace(rutaAgi, "");				
				}
				adjunto.setRutaDocumtNueva(rutaArchivoDoc);
				respuesta = this.dao.finalizarEjecuciones(adjunto, null, (((UserAuth)principal).getUsername()),
						new Long(((UserAuth)principal).getCodPerfil()));
				
			}
			return respuesta;
		}
		
		//cguerra FIN		
	
	
	@Override
	public List<Revision> obtenerRevisionesDoc(RevisionRequest revisionRequest, PageRequest pageRequest){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Revision> lista = this.dao.obtenerRevisionesDoc(revisionRequest, pageRequest,new Long(((UserAuth)principal).getCodPerfil()));
		return lista;
	}
	@Override
	public boolean actualizarConocimientoLectura(Conocimiento conocimiento) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.actualizarConocimientoLectura(conocimiento, (((UserAuth)principal).getUsername()));
	}
	@Override
	public boolean grabarEjecucion(List<Revision> revisionEjecucion) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<Revision> grabarEjecucionAdjuntar(MultipartFile[] adjuntos) {
		// TODO Auto-generated method stub
		return null;
	}

}