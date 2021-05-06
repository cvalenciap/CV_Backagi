package pe.com.sedapal.agi.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Documento;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IConstanteService;
import pe.com.sedapal.agi.service.IDocumentoService;
import pe.com.sedapal.agi.service.ITareaService;
import pe.com.sedapal.agi.util.UConstante;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
@RestControllerAdvice
@RequestMapping("/api")
public class TareaApi {
    private ResponseObject responseObject;
    @Autowired
	Environment env;	
	private String endpointServidor;
	
    
    private static String carpetaResources;
    //Subir documento a Google Drive - Inicio.
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();    
    private static String TOKENS_DIRECTORY_PATH;
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static  String CREDENTIALS_FILE_PATH;
    //Subir documento a Google Drive - Fin.
    
    
    @Autowired
	private IDocumentoService serviceDocu;
    
    @Autowired
    ITareaService tareaService;
    
    @Autowired
	private IConstanteService service;

    

    
    @GetMapping(path = "/tarea/cancelacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerListaSolicitudCancelacion(TareaRequest tareaRequest, PageRequest pageRequest) {
    	ResponseObject response = new ResponseObject();
    	try {
        	PageRequest page = new PageRequest();
        	page.setPagina(1);
        	page.setRegistros(1000);
        	ConstanteRequest constanteRequest = new ConstanteRequest();
        	constanteRequest.setPadre(UConstante.NOMBRE_PARAMETRO_ESTADOS_CANCELACION);
        	List<Constante> listaConstantes = this.service.obtenerConstantes(constanteRequest, page,null);
        	String textoConstante = "";
        	int cont = 0;
        	for(Constante constante:listaConstantes) {
        		if(constante.getV_valcons().equals(UConstante.ESTADO_CANCELACION_REGISTRADO) || constante.getV_valcons().equals(UConstante.ESTADO_CANCELACION_RECHAZADO)){
        			if(cont == 0) {
        				textoConstante = textoConstante + constante.getIdconstante();
        			}else if(cont > 0){
        				textoConstante = textoConstante + ", " +constante.getIdconstante();
        			}
        			cont++;
        		}
        	}
        	if(tareaRequest.getEstadoDoc()==null) {
        		tareaRequest.setEstado(textoConstante);}
            	else {
            		tareaRequest.setEstado(null);	
            	}
        	
        	
            List<Cancelacion> solicitudesCancelacion = tareaService.obtenerSolicitudesCancelacion(tareaRequest,pageRequest);
            
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setPaginacion(this.tareaService.obtenerPaginacion());
            	response.setResultado(solicitudesCancelacion);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(path = "/tarea/cancelacionReporte", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerListaSolicitudCancelacionReporte(TareaRequest tareaRequest, PageRequest pageRequest) {
    	ResponseObject response = new ResponseObject();
    	try {
        	PageRequest page = new PageRequest();
        	page.setPagina(1);
        	page.setRegistros(1000);
        	ConstanteRequest constanteRequest = new ConstanteRequest();
        	constanteRequest.setPadre(UConstante.NOMBRE_PARAMETRO_ESTADOS_CANCELACION);
        	List<Constante> listaConstantes = this.service.obtenerConstantes(constanteRequest, page,null);
        	String textoConstante = "";
        	int cont = 0;
        	for(Constante constante:listaConstantes) {
        		if(constante.getV_valcons().equals(UConstante.ESTADO_CANCELACION_REGISTRADO) || constante.getV_valcons().equals(UConstante.ESTADO_CANCELACION_RECHAZADO)){
        			if(cont == 0) {
        				textoConstante = textoConstante + constante.getIdconstante();
        			}else if(cont > 0){
        				textoConstante = textoConstante + ", " +constante.getIdconstante();
        			}
        			cont++;
        		}
        	}
        	if(tareaRequest.getEstadoDoc()==null) {
        		tareaRequest.setEstado(textoConstante);}
            	else {
            		tareaRequest.setEstado(null);	
            	}
        	
        	
            List<Cancelacion> solicitudesCancelacion = tareaService.obtenerSolicitudesCancelacionReporte(tareaRequest,pageRequest);
            
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setPaginacion(this.tareaService.obtenerPaginacion());
            	response.setResultado(solicitudesCancelacion);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/tarea/cancelacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> crearSolicitudCancelacion(@RequestPart("file") Optional<MultipartFile> file ,@RequestPart("cancelacion") Optional<Cancelacion> cancelacion) {
    	ResponseObject response = new ResponseObject();
    	MultipartFile archivo = null;
    	Cancelacion objetoCancelacion = null;
    	if(cancelacion.isPresent()) {
    		objetoCancelacion = cancelacion.get();
    	}
    	try {
        	objetoCancelacion.setTipo(1);
            boolean resultado = tareaService.crearActualizarSolicitudCancelacion(objetoCancelacion,file);
        	response = new ResponseObject();
            if(tareaService.obtenerError() != null){
                responseObject.setEstado(Estado.ERROR);
                responseObject.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
            } else { 
	        	response.setEstado(Estado.OK);
	        	response.setResultado("OK");
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
           }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(path = "/tarea/cancelacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> actualizarSolicitudCancelacion(@RequestPart("file") Optional<MultipartFile> file ,@RequestPart("cancelacion") Optional<Cancelacion> cancelacion) {
    	ResponseObject response = new ResponseObject();
    	MultipartFile archivo = null;
    	Cancelacion objetoCancelacion = null;
    	if(file.isPresent()) {
    		archivo = file.get();
    	}
    	if(cancelacion.isPresent()) {
    		objetoCancelacion = cancelacion.get();
    	}
    	try {
        	objetoCancelacion.setTipo(2);
        	boolean resultado = tareaService.crearActualizarSolicitudCancelacion(objetoCancelacion,file);
        	response = new ResponseObject();
            if(tareaService.obtenerError() != null){
                responseObject.setEstado(Estado.ERROR);
                responseObject.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
            } else { 
        	response.setEstado(Estado.OK);
        	response.setResultado("OK");
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
           }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(path = "/tarea/enviarSolicitudCancelacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> crearEnviarSolicitudCancelacion(@RequestPart("file") Optional<MultipartFile> file ,@RequestPart("cancelacion") Optional<Cancelacion> cancelacion) {
    	ResponseObject response = new ResponseObject();
    	MultipartFile archivo = null;
    	Cancelacion objetoCancelacion = null;
    	if(file.isPresent()) {
    		archivo = file.get();
    	}
    	if(cancelacion.isPresent()) {
    		objetoCancelacion = cancelacion.get();
    	}
    	try {
        	objetoCancelacion.setTipo(4);
        	boolean resultado = tareaService.crearActualizarSolicitudCancelacion(objetoCancelacion,file);
        	response = new ResponseObject();
            if(tareaService.obtenerError() != null){
                responseObject.setEstado(Estado.ERROR);
                responseObject.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
            } else { 
        	response.setEstado(Estado.OK);
        	response.setResultado("OK");
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
           }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(path = "/tarea/enviarSolicitudCancelacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> actualizarEnviarSolicitudCancelacion(@RequestPart("file") Optional<MultipartFile> file ,@RequestPart("cancelacion") Optional<Cancelacion> cancelacion) {
    	ResponseObject response = new ResponseObject();
    	MultipartFile archivo = null;
    	Cancelacion objetoCancelacion = null;
    	if(file.isPresent()) {
    		archivo = file.get();
    	}
    	if(cancelacion.isPresent()) {
    		objetoCancelacion = cancelacion.get();
    	}
    	try {
        	objetoCancelacion.setTipo(3);
        	boolean resultado = tareaService.crearActualizarSolicitudCancelacion(objetoCancelacion,file);
        	response = new ResponseObject();
            if(tareaService.obtenerError() != null){
                responseObject.setEstado(Estado.ERROR);
                responseObject.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
            } else { 
        	response.setEstado(Estado.OK);
        	response.setResultado("OK");
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
           }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(path = "/tarea/cancelacion/obtenerSolicitudCancelacion/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerSolicitudCancelacion(@PathVariable("id") Long idCancelacion) {
    	ResponseObject response = new ResponseObject();
    	try {
        	
            Cancelacion solicitudCancelacion = tareaService.obtenerSolicitudCancelacion(idCancelacion);
            
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setResultado(solicitudCancelacion);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(path = "/tarea/cancelacion/aprobacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerListaAprobacionCancelacion(TareaRequest tareaRequest, PageRequest pageRequest) {
    	ResponseObject response = new ResponseObject();
    	try {
        	PageRequest page = new PageRequest();
        	page.setPagina(1);
        	page.setRegistros(1000);
        	ConstanteRequest constanteRequest = new ConstanteRequest();
        	constanteRequest.setPadre(UConstante.NOMBRE_PARAMETRO_ESTADOS_CANCELACION);
        	List<Constante> listaConstantes = this.service.obtenerConstantes(constanteRequest, page,null);
        	String textoConstante = "";
        	for(Constante constante:listaConstantes) {
        		if(constante.getV_valcons().equals(UConstante.ESTADO_CANCELACION_EN_REVISION)){
        			textoConstante = constante.getIdconstante().toString();
        			break;
        		}
        	}        	
        	tareaRequest.setEstado(textoConstante);
        	
            List<Cancelacion> solicitudesCancelacion = tareaService.obtenerCancelacionAprobacion(tareaRequest,pageRequest);
            
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setPaginacion(this.tareaService.obtenerPaginacion());
            	response.setResultado(solicitudesCancelacion);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @PostMapping(path = "/tarea/cancelacion/aprobarSolicitudCancelacion",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> aprobarSolicitudCancelacion(@RequestBody Cancelacion cancelacion) {
    	ResponseObject response = new ResponseObject();
    	
    	try {
    		cancelacion.setTipo(1);
        	boolean resultado = tareaService.aprobarRechazarSolicitudCancelacion(cancelacion);
        	response = new ResponseObject();
            if(tareaService.obtenerError() != null){
                responseObject.setEstado(Estado.ERROR);
                responseObject.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
            } else { 
            	response.setEstado(Estado.OK);
        		response.setResultado("OK");
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
           }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(path = "/tarea/cancelacion/rechazarSolicitudCancelacion",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> rechazarSolicitudCancelacion(@RequestBody Cancelacion cancelacion) {
    	ResponseObject response = new ResponseObject();
    	
    	try {
    		cancelacion.setTipo(2);
        	boolean resultado = tareaService.aprobarRechazarSolicitudCancelacion(cancelacion);
        	response = new ResponseObject();
            if(tareaService.obtenerError() != null){
                responseObject.setEstado(Estado.ERROR);
                responseObject.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
            } else { 
            	response.setEstado(Estado.OK);
        		response.setResultado("OK");
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
           }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(path = "/tarea/cancelacion/ejecucion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerListaEjecucionCancelacion(TareaRequest tareaRequest, PageRequest pageRequest) {
    	ResponseObject response = new ResponseObject();
    	try {
        	PageRequest page = new PageRequest();
        	page.setPagina(1);
        	page.setRegistros(1000);
        	ConstanteRequest constanteRequest = new ConstanteRequest();
        	constanteRequest.setPadre(UConstante.NOMBRE_PARAMETRO_ESTADOS_CANCELACION);
        	List<Constante> listaConstantes = this.service.obtenerConstantes(constanteRequest, page,null);
        	String textoConstante = "";
        	for(Constante constante:listaConstantes) {
        		if(constante.getV_valcons().equals(UConstante.ESTADO_CANCELACION_APROBADO)){
        			textoConstante = constante.getIdconstante().toString();
        			break;
        		}
        	}
        	tareaRequest.setEstado(textoConstante);
        	
            List<Cancelacion> solicitudesCancelacion = tareaService.obtenerCancelacionAprobacion(tareaRequest,pageRequest);
            
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setPaginacion(this.tareaService.obtenerPaginacion());
            	response.setResultado(solicitudesCancelacion);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    /*ejecutar la cancelacion o el alta del documento ?  */
    @PostMapping(path = "/tarea/cancelacion/cancelarDocumento",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> cancelarDocumento(@RequestBody Cancelacion cancelacion) {
    	ResponseObject response = new ResponseObject();
    	try {
    		//cguerra
    		
    		Documento objeto = this.serviceDocu.obtenerIdGoogle(cancelacion.getIdDocumento(), null, null);    		
    		if(cancelacion.getNumTipoCancelacion()==775) {
    			//Google DRIVE
    			if(objeto.getFechaApro()!=null) {
    				if(objeto.getIndicadorDigital()==0) {
    					cancelacion.setRutaDocumento(objeto.getRutaDocumentoOriginal());
        				boolean resultadoGoogleDrive = tareaService.cancelarDocumentoGoogleDrive(cancelacion);	
    				}else {
    					//carga digital
    		    		  boolean resultado = tareaService.cancelarDocumento(cancelacion);	
    	    		}
        		}
    		}else {
    		  boolean resultado = tareaService.cancelarDocumento(cancelacion);	
    		}
    		
    		
    		
    		//cguerra
        	response = new ResponseObject();
            if(tareaService.obtenerError() != null){
                responseObject.setEstado(Estado.ERROR);
                responseObject.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
            } else { 
            	response.setEstado(Estado.OK);
        		response.setResultado("OK");
                return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
           }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    
    private String String(boolean resultadoGoogleDrive) {
		// TODO Auto-generated method stub
		return null;
	}

	@GetMapping(path = "/tarea/cancelacion/obtenerRutaCopiaControladaDocumento/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerRutaCopiaControladaDocumento(@PathVariable("id") Long idDocumento) {
    	ResponseObject response = new ResponseObject();
    	try {
    		String ruta = this.tareaService.obtenerUltimaRutaCopiaControladaDocumento(idDocumento);
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setResultado(ruta);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping(path = "/tarea/documentosjerarquicos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerDocumentosJerarquicos(@PathVariable("id") Long idDocumentoHijo) {
    	ResponseObject response = new ResponseObject();
    	try {
    		List<Documento> listaDocumentos = this.tareaService.obtenerDocumentosJerarquicos(idDocumentoHijo);
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setResultado(listaDocumentos);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(path = "/tarea/cancelacion/cantidadsolicitudes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerCantidadSolicitudesCancelacion(@PathVariable("id") Long idDocumento) {
    	ResponseObject response = new ResponseObject();
    	try {
    		Integer cantidad = this.tareaService.obtenerCantidadSolicitudCancelacion(idDocumento);
            if(tareaService.obtenerError() != null){
            	response.setEstado(Estado.ERROR);
            	response.setError(tareaService.obtenerError());
                return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
            } else {
            	response.setEstado(Estado.OK);
            	response.setResultado(cantidad);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
        	response.setEstado(Estado.ERROR);
        	response.setError(new Error(exception.hashCode(), exception.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
            return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    
 
  	
    @PostMapping(path = "/subirDocGoogleDrive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> subirDocumentoGoogleDrive(@RequestBody TareaRequest tareaRequest){
		ResponseObject response = new ResponseObject();
		String mensaje = "";
		try {
			//conexión a Google Drive
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		    Drive serviceGD = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
		            .setApplicationName(APPLICATION_NAME)
		            .build();
	    
			//crear carpeta en el proyecto
		    carpetaResources=env.getProperty("app.config.paths.toke.directory");	
			java.io.File carpet = new java.io.File(carpetaResources+"documentos");
			carpet.mkdir();
            
            if(tareaRequest.getRutaDocuSelecc()!=null) {
                 
			  //especifico nombre de documento al crearlo y fomato mismo de GD
			  File fileMetadata = new File();
			  fileMetadata.setName("Nombre Documento");
			  fileMetadata.setMimeType("application/vnd.google-apps.document");
			  fileMetadata.setParents(Collections.singletonList("1GiNU-9y4eRIBmdbP5lcG9if2Txb2gbji"));
			  //descarga documento de GD en carpeta del proyecto
			  //URL urlWord = new URL("http://sedapal.test:8080/fileserver/agi/b4b75bdf-b10b-4e5d-a51f-632fe12f066e.docx");
			  URL urlWord = new URL(tareaRequest.getRutaDocuSelecc());
			  //java.io.File destinationWord = new java.io.File("D:\\PruebaDescarga\\"+file.getName()+c+".doc");
			  java.io.File destinationWord = new java.io.File(carpetaResources+"documentos/"+"Nombre Documento"+".docx");
			  FileUtils.copyURLToFile(urlWord, destinationWord);
			
			  //subo documento descargado a GD
			  java.io.File filePath = new java.io.File("/documentos/"+"Nombre Documento"+".docx");
			  FileContent mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.wordprocessingml.document", filePath);
			  File file = serviceGD.files().create(fileMetadata, mediaContent)
			      .setFields("id")
			      .execute();
			  //System.out.println("File ID: " + file.getId());
			  
			  //elimino carpeta y archivo descargado
			  Path eliminarCarpeta = Paths.get(carpetaResources+"documentos");
			  deleteDirectoryRecursion(eliminarCarpeta);            
			        //Creación de documento en Google Drive - Fin.
			  
			     //registro en BD los datos
			    String idDocGoogle = file.getId();
			    mensaje = this.tareaService.subirDocumentoGoogleDrive(tareaRequest, idDocGoogle);
                        
            }else {
             //creacion de dcumento en GD
             /** Creación de documento en carpeta específica */
              File fileMetadata = new File();
              fileMetadata.setName("Nombre Documento");
              //Id de carpeta SEDAPAL donde se guardarán los documentos creados.
              fileMetadata.setParents(Collections.singletonList("1GiNU-9y4eRIBmdbP5lcG9if2Txb2gbji"));
              //Tipo de documento a crear.
              fileMetadata.setMimeType("application/vnd.google-apps.document");
              
              File file = serviceGD.files().create(fileMetadata)
                  .setFields("id")
                  .execute();
              //System.out.println("File ID Creado: " + file.getId());
                    //Creación de documento en Google Drive - Fin.
             
              //registro en BD los datos
	            String idDocGoogle = file.getId();
	            mensaje = this.tareaService.subirDocumentoGoogleDrive(tareaRequest, idDocGoogle);
            }
                 
		 if(tareaService.obtenerError() != null){
			 response.setError(tareaService.obtenerError());
		     response.setEstado(Estado.ERROR);
		     return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);
		 } else {
			 response.setResultado(mensaje);
			 response.setEstado(Estado.OK);
		     return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
		 }
      } catch(Exception e) {
             response.setError(1, "Error Interno", e.getMessage());
             response.setEstado(Estado.ERROR);
             return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
  		/**
  	     * Crea objeto de autenticación.
  	     * @param HTTP_TRANSPORT The network HTTP Transport.
  	     * @return Un bjeto de autenticación.
  	     * @throws IOException si el credentials.json no es encontrado.
  	     */
  	    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
//  	        InputStream in = RevisionApi.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
			TareaApi.CREDENTIALS_FILE_PATH= env.getProperty("app.config.paths.carpetaResources");
			TareaApi.TOKENS_DIRECTORY_PATH= env.getProperty("app.config.paths.toke.directory");  	    	
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
  	    //Subir documento a Google Drive - Fin.
  	    
  	  private void deleteDirectoryRecursion(Path path) throws IOException {
          if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
              for (Path entry : entries) {
                deleteDirectoryRecursion(entry);
              }
            }
          }
          Files.delete(path);
        }
}
