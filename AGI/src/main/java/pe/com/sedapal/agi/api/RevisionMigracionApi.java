package pe.com.sedapal.agi.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IRevisionMigracionService;
import pe.com.sedapal.agi.service.IRevisionService;
import pe.com.sedapal.agi.dao.IRevisionMigracionDAO;
import pe.com.sedapal.agi.model.Revision;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.api.services.content.ShoppingContent;
import com.google.common.collect.Lists;
import java.io.BufferedReader;

@RestController
@RequestMapping("/api/migracion")
public class RevisionMigracionApi {
	@Autowired
	private IRevisionMigracionService service;
	//private IRevisionService 
	@Autowired
	Environment env;	
	private String endpointServidor;
	//Creación de documento en Google Drive - Inicio.
	private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();    
    private static  String TOKENS_DIRECTORY_PATH;
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static  String CREDENTIALS_FILE_PATH;// = "/credentials.json";
  //Creación de documento en Google Drive - Fin.
    
	//Lista Documento
		@GetMapping(path = "/revision", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarRevision(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.obtenerRevision(revisionRequest, pageRequest);	
				if(this.service.getError() == null) {
					response.setResultado(lista);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
				}else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				response.setError(1, "Error Interno",e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	// Lista de elaboración de revisión
		@GetMapping(path = "/revisionFase", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.obtenerRevisionFase(revisionRequest, pageRequest);	
				if(this.service.getError() == null) {
					response.setResultado(lista);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
				}else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				response.setError(1, "Error Interno",e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@DeleteMapping(path = "/revision/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> eliminarRevision(@PathVariable("id")  Long id){
			ResponseObject response = new ResponseObject();
			try {
				Boolean eliminado = this.service.eliminarRevision(id);
				if (this.service.getError() == null) {
					response.setResultado(eliminado);
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);		
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);		
				}
			} catch(Exception e) {
				response.setError(1, "Error Interno", e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@DeleteMapping(path = "/revision/",produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> eliminarRevisionTodo(){
			ResponseObject response = new ResponseObject();
			try {
				Boolean eliminado = this.service.eliminarRevision(null);
				if (this.service.getError() == null) {
					response.setResultado(eliminado);
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);		
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);		
				}
			} catch(Exception e) {
				response.setError(1, "Error Interno", e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@PostMapping(path = "/revision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> creaRevision(@RequestBody Revision revisionRequest){
			ResponseObject response = new ResponseObject();
			try {
				Revision revision = this.service.crearRevision(revisionRequest);
				if (this.service.getError() == null) {
					response.setResultado(revision);
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch(Exception e) {
				response.setError(1, "Error Interno", e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	@GetMapping(path = "/tarea-aprobado", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarTarea(RevisionRequest revisionRequest, PageRequest pageRequest){
		ResponseObject response = new ResponseObject();
		try {
			List<Revision> lista = this.service.obtenerListaTareaAprobar(revisionRequest, pageRequest);
			if(this.service.getError() == null) {
				response.setResultado(lista);
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

		@PostMapping(path = "/crearDocGoogleDrive/{idRevi}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> crearDocumentoGoogleDrive(@PathVariable("idRevi") Long idRevi){
			ResponseObject response = new ResponseObject();
			try {
				//Creación de documento en Google Drive - Inicio.
				final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		         Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
		                .setApplicationName(APPLICATION_NAME)
		                .build();
		     
		        FileList result = service.files().list()
		                .setPageSize(10)
		                .setFields("nextPageToken, files(id, name)")
		                .execute();
		        List<File> files = result.getFiles();
		        if (files == null || files.isEmpty()) {
		            System.out.println("No files found.");
		        } else {
		            System.out.println("Files:");
		            for (File file : files) {
		            	
		                System.out.printf("%s (%s)\n", file.getName(), file.getId());
		            }
		        }
		        
		        /** Creación de documento en carpeta específica */
		        File fileMetadata = new File();
		        fileMetadata.setName("Nombre Documento");
		        //Id de carpeta SEDAPAL donde se guardarán los documentos creados.
		        fileMetadata.setParents(Collections.singletonList("1GiNU-9y4eRIBmdbP5lcG9if2Txb2gbji"));
		        //Tipo de documento a crear.
		        fileMetadata.setMimeType("application/vnd.google-apps.document");
		        
		        File file = service.files().create(fileMetadata)
		            .setFields("id")
		            .execute();
		        System.out.println("File ID Creado: " + file.getId());
				//Creación de documento en Google Drive - Fin.
				
				String idDocGoogle = file.getId();
				
				Revision revision = this.service.crearDocumentoGoogleDrive(idDocGoogle, idRevi);
				if (this.service.getError() == null) {
					response.setResultado(revision);
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch(Exception e) {
				response.setError(1, "Error Interno", e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//Creación de documento en Google Drive - Inicio.
		/**
	     * Crea objeto de autenticación.
	     * @param HTTP_TRANSPORT The network HTTP Transport.
	     * @return Un bjeto de autenticación.
	     * @throws IOException si el credentials.json no es encontrado.
	     */
	    private  Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

			RevisionMigracionApi.CREDENTIALS_FILE_PATH= env.getProperty("app.config.paths.carpetaResources");
			RevisionMigracionApi.TOKENS_DIRECTORY_PATH= env.getProperty("app.config.paths.toke.directory");	
	        InputStream in = RevisionMigracionApi.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
	        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
	                .setAccessType("offline")
	                .build();
	        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
	        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	    }

//	    public static void main(String... args) throws IOException, GeneralSecurityException {        
//	    	 final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//	         Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//	                .setApplicationName(APPLICATION_NAME)
//	                .build();
//	     
//	        FileList result = service.files().list()
//	                .setPageSize(10)
//	                .setFields("nextPageToken, files(id, name)")
//	                .execute();
//	        List<File> files = result.getFiles();
//	        if (files == null || files.isEmpty()) {
//	            System.out.println("No files found.");
//	        } else {
//	            System.out.println("Files:");
//	            for (File file : files) {
//	                System.out.printf("%s (%s)\n", file.getName(), file.getId());
//	            }
//	        }
//	        
//	        /** Creación de documento en carpeta específica */
//	        File fileMetadata = new File();
//	        fileMetadata.setName("Prueba - Werner");
//	        //Id de carpeta SEDAPAL donde se guardarán los documentos creados.
//	        fileMetadata.setParents(Collections.singletonList("1GiNU-9y4eRIBmdbP5lcG9if2Txb2gbji"));
//	        //Tipo de documento a crear.
//	        fileMetadata.setMimeType("application/vnd.google-apps.document");
//	        
//	        File file = service.files().create(fileMetadata)
//	            .setFields("id")
//	            .execute();
//	        System.out.println("File ID Creado: " + file.getId());
//	    }
	    //Creación de documento en Google Drive - Fin.
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*@GetMapping(path = "/revisionFase", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.obtenerRevisionFase(revisionRequest, pageRequest);	
				if(this.service.getError() == null) {
					response.setResultado(lista);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
				}else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				response.setError(1, "Error Interno",e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}*/
	
}