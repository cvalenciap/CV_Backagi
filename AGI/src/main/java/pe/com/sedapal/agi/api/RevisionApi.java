package pe.com.sedapal.agi.api;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequestAdjunto;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IRevisionService;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Jefes;
import pe.com.sedapal.agi.model.Revision;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.temporal.ValueRange;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class RevisionApi {	
	
	
	@Autowired
	Environment env;
	
	private  String endpointServidor; 		
	//Creación de documento en Google Drive - Inicio.
	@Autowired
	private IRevisionService service;
		
	private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();    
    private static  String TOKENS_DIRECTORY_PATH;
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static String  CREDENTIALS_FILE_PATH;  
    
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
	
	//Rechazar Revision Solicitud
	@PostMapping(path = "/rechazarDocumento/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> rechazarDocumento(@PathVariable("id") Long idDocumento, @RequestBody Revision revisionRechazo){
		ResponseObject response = new ResponseObject();
		try {
			Revision revision = this.service.rechazarDocumento(idDocumento, revisionRechazo);
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
//Ver Documento 
		@PostMapping(path = "/crearDocGoogleDrive/{idRevi}/{tipoDocumentoCrear}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> crearDocumentoGoogleDrive(@PathVariable("idRevi") Long idRevi, @PathVariable("tipoDocumentoCrear") String tipoDocumentoCrear){
			ResponseObject response = new ResponseObject();
			try {
				//Creación de documento en Google Drive - Inicio.
				final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
		                .setApplicationName(APPLICATION_NAME)
		                .build();
		        FileList result = service.files().list()  
		                //.setPageSize(10)
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
		        
		        //String carpetaResources1 = "/opt/tomcat/webapps/AGI-WebService/WEB-INF/classes/";
		        
		        File fileMetadata = new File();
		        fileMetadata.setName("Nombre Documento");
		        //Id de carpeta SEDAPAL donde se guardarán los documentos creados.
		        fileMetadata.setParents(Collections.singletonList("1GiNU-9y4eRIBmdbP5lcG9if2Txb2gbji"));
		        
		        //Tipo de documento a crear
		        if (tipoDocumentoCrear.equals("word")) { //tipo de Documento a Crear
		        	fileMetadata.setMimeType("application/vnd.google-apps.document");
				}else if(tipoDocumentoCrear.equals("excel")) {					
					fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");					
				}//
		        
		        File file = service.files().create(fileMetadata)
		            .setFields("id")
		            .execute();
		        
		        System.out.println("File ID Creado: " + file.getId());
				//Creación de documento en Google Drive - Fin.
				
				String idDocGoogle = file.getId();
				
				//cguerra
				if(tipoDocumentoCrear.equals("word")) {
					idDocGoogle = file.getId()+"word";
				}else if(tipoDocumentoCrear.equals("excel")) {
					idDocGoogle = file.getId()+"exel";
				}
				//cguerra 
				
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
	    	
	    	RevisionApi.CREDENTIALS_FILE_PATH= env.getProperty("app.config.paths.carpetaResources");
	    	RevisionApi.TOKENS_DIRECTORY_PATH= env.getProperty("app.config.paths.toke.directory");
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
	    
	    
	    @GetMapping(path = "/revision/programaciones", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarProgramacion(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.consultarProgramacion(revisionRequest, pageRequest);	
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
    
		@GetMapping(path = "/revision/listaprogramada", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarListaProgramada(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<RevisionRequest> lista = this.service.consultarListaProgramada(revisionRequest, pageRequest);	
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
		
		@DeleteMapping(path = "/revision/eliminar/programacion/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> eliminarProgramacion(@PathVariable("id")  Long id){
			ResponseObject response = new ResponseObject();
			try {
				Boolean eliminado = this.service.eliminarProgramacion(id);
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
		
		@GetMapping(path = "/revision/editar/programaciones", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarProgramacionPorIdProg(RevisionRequest revisionRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.consultarProgramacionPorIdProg(revisionRequest);	
				if(this.service.getError() == null) {
					response.setResultado(lista);
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
				
		@GetMapping(path = "/revision/listadistribucion", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarListaDistribucionEjec(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<RevisionRequest> lista = this.service.consultarListaDistribucionEjec(revisionRequest, pageRequest);	
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
		
		@PostMapping(path = "/revision/guardarDocumento", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> guardarDocumentoRevision(@RequestBody Revision revision){
			ResponseObject response = new ResponseObject();
			try {
				Revision revisionObtenido = this.service.guardarDocumentoRevision(revision);
				boolean resultado = this.service.registrarDocumentosRevision(revisionObtenido);
				if (resultado) {
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
		
		@PostMapping(path = "/revision/actualizarConocimiento", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> actualizarConocimientoLectura(@RequestBody Conocimiento conocimiento){
			ResponseObject response = new ResponseObject();
			try {
				boolean resultado = this.service.actualizarConocimientoLectura(conocimiento);
				if (resultado) {
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
	    
		@GetMapping(path = "/revision/distribuir", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarDistribuciones(RevisionRequest revisionRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.consultarProgramacionPorIdProg(revisionRequest);	
				if(this.service.getError() == null) {
					response.setResultado(lista);
					//response.setPaginacion(this.service.getPaginacion());
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
		
		@PostMapping(path = "/revision/guardarprogramacion", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> grabarPrograma(@RequestBody Map<String, Object> mapa){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> revisionSelecc = null;
				List<Revision> revisionNoSelecc = null;
				String codFichaLogueado = "";
				String idEstProg = "";
				String idProg = "";
				
				if(mapa.get("listaProgSeleccionada") != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					revisionSelecc = objectMapper.readValue(mapa.get("listaProgSeleccionada").toString(), new TypeReference<List<Revision>>(){});
				}
				if(mapa.get("listaProgNoSeleccionada") != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					revisionNoSelecc = objectMapper.readValue(mapa.get("listaProgNoSeleccionada").toString(), new TypeReference<List<Revision>>(){});
				}
				
				codFichaLogueado = mapa.get("codFichaLogueado").toString();
				idEstProg = mapa.get("idEstProg").toString();
				
				if (mapa.get("idProg") == null) {
					idProg = null;
				} else {
					idProg = mapa.get("idProg").toString();
				}
				
				List<Revision> revisiones = this.service.grabarPrograma(idProg, codFichaLogueado, idEstProg, revisionSelecc, revisionNoSelecc);
				
				if (this.service.getError() == null) {
					response.setResultado(revisiones);
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
		
		@PostMapping(path = "/revision/guardarlistadistribucion", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> grabarDistribucion(@RequestBody Map<String, Object> mapa){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> revisionSelecc = null;
				List<Revision> revisionNoSelecc = null;
				String codFichaLogueado = "";
			
				if(mapa.get("listaProgSeleccionada") != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					revisionSelecc = objectMapper.readValue(mapa.get("listaProgSeleccionada").toString(), new TypeReference<List<Revision>>(){});
				}
				if(mapa.get("listaProgNoSeleccionada") != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					revisionNoSelecc = objectMapper.readValue(mapa.get("listaProgNoSeleccionada").toString(), new TypeReference<List<Revision>>(){});
				}
				
				codFichaLogueado = mapa.get("codFichaLogueado").toString();
				
				List<Revision> revisionDistribuida = this.service.grabarDistribucion(codFichaLogueado, revisionSelecc, revisionNoSelecc);
				
				if (this.service.getError() == null) {
					response.setResultado(revisionDistribuida);
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
		
		@PostMapping(path = "/revision/finalizarlistadistribucion", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> finalizarDistribucion(@RequestBody Map<String, Object> mapa){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> revisionSelecc = null;
				List<Revision> revisionNoSelecc = null;
				String codFichaLogueado = "";
			
				if(mapa.get("listaProgSeleccionada") != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					revisionSelecc = objectMapper.readValue(mapa.get("listaProgSeleccionada").toString(), new TypeReference<List<Revision>>(){});
				}
				if(mapa.get("listaProgNoSeleccionada") != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					revisionNoSelecc = objectMapper.readValue(mapa.get("listaProgNoSeleccionada").toString(), new TypeReference<List<Revision>>(){});
				}
				
				codFichaLogueado = mapa.get("codFichaLogueado").toString();
				
				List<Revision> revisiones = this.service.finalizarDistribucion(codFichaLogueado, revisionSelecc, revisionNoSelecc);
				
				if (this.service.getError() == null) {
					response.setResultado(revisiones);
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
		
		@GetMapping(path = "/revision/listar/programaciones", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarProgramaciones(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.consultarProgramaciones(revisionRequest, pageRequest);	
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
		
		@GetMapping(path = "/revision/detalle/distribuciones", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarDetDistribuciones(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Revision> lista = this.service.consultarDetDistribuciones(revisionRequest, pageRequest);	
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
		
		@PostMapping(path = "/revision/guardar-adjunto", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> grabarAdjunto(@RequestParam("files") MultipartFile[] files,@RequestBody Map<String, Object> mapa) {
			ResponseObject response = new ResponseObject();
	    	MultipartFile archivo = null;
	    	Revision objetoDocumento = null;
	    	List<Revision> listaRevision=null;
 	
			try {
				listaRevision = this.service.grabarEjecucionAdjuntar(files);
				if (this.service.getError() == null) {
					response.setResultado(listaRevision);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
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
		
		@PostMapping(path = "/revision/guardar-ejecucion", produces = MediaType.APPLICATION_JSON_VALUE)
	    //public ResponseEntity<ResponseObject> grabarEjecucion(@RequestPart("file") Optional<MultipartFile> file, @RequestPart("documento") Optional<Revision> revision) {
		public ResponseEntity<ResponseObject> grabarEjecucion(@RequestBody Map<String, Object> mapa) {
			ResponseObject response = new ResponseObject();
	    	List<Revision> documentosEjecucion  = null;
	    	/*if(file.isPresent()) {
	    		archivo = file.get();
	    	}
	    	if(revision.isPresent()) {
	    		objetoDocumento = revision.get();
	    	}	*/    	
			try {
				if(mapa.get("listaDistribucion") != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					documentosEjecucion = objectMapper.readValue(mapa.get("listaRevision").toString(), new TypeReference<List<Revision>>(){});
				}
				boolean resultado = this.service.grabarEjecucion(documentosEjecucion);
				if (this.service.getError() == null) {
					response.setResultado(resultado);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
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
		
		//cguerra  INICIO
			@PostMapping(path = "/revision/guardar-ejecucion1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
			public ResponseEntity<ResponseObject> grabarEjecucion1(@RequestBody List<RevisionRequestAdjunto> revision) {
			ResponseObject response = new ResponseObject();	    	
			try {	
				boolean resultado = this.service.grabarEjecucionProg(revision);
				if (this.service.getError() == null) {
					response.setResultado(resultado);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
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
		
			
			@PostMapping(path = "/revision/guardar-ejecucion-finalizar", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
			public ResponseEntity<ResponseObject> finalizarEjecucion(@RequestBody List<RevisionRequestAdjunto> revision) {
			ResponseObject response = new ResponseObject();	    	
			try {	
				boolean resultado = this.service.finalizarEjecucion(revision);
				if (this.service.getError() == null) {
					response.setResultado(resultado);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
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
			
		//cguerra FIN
		
	
		@GetMapping(path = "/revision/listajefes", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> obtenerJefes(RevisionRequest revisionRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Jefes> lista = this.service.obtenerJefes(revisionRequest, pageRequest);	
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
		

				@GetMapping(path = "/revision/Doc", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> consultarRevisionDoc(RevisionRequest revisionRequest, PageRequest pageRequest){
					ResponseObject response = new ResponseObject();
					try {
						List<Revision> lista = this.service.obtenerRevisionesDoc(revisionRequest, pageRequest);	
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

	@GetMapping(path = "/revision/listaejecucion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarListaEjecucion(RevisionRequest revisionRequest,PageRequest pageRequest) {
		ResponseObject response = new ResponseObject();
		try {
			List<Revision> lista = this.service.consultarListaEjec(revisionRequest, pageRequest);
			if (this.service.getError() == null) {
				response.setResultado(lista);
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			} else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}