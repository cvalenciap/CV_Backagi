package pe.com.sedapal.agi.api;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PreguntaCursoRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IAsistenciaService;
import pe.com.sedapal.agi.service.IPreguntaCursoService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.model.request_objects.AsistenciaRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Documento;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaApi {
	
	@Autowired IAsistenciaService service;
	
	private static final Logger LOGGER = Logger.getLogger(AsistenciaApi.class);	
	
	@GetMapping(path = "/capacitacion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarAsistencia( AsistenciaRequest asistenciaRequest, PageRequest paginaRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<Asistencia> lista = this.service.obtenerAsistencia(asistenciaRequest, paginaRequest);
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
		}catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(path = "/empleadoSesion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerEmpleadoSesion(Sesion sesion){
		
		ResponseObject response = new ResponseObject();
		try {
			List<Trabajador> lista = this.service.obtenerEmpleadoSesion(sesion);
			if(this.service.getError() == null) {
				response.setResultado(lista);				
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);
				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping(path = "/crearDocumento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> crearDocumento(@RequestPart("file") Optional<MultipartFile> file,@RequestPart("asistencia") Optional<Asistencia> asistencia ){
		
		ResponseObject response = new ResponseObject();
		MultipartFile archivo = null;
    	Asistencia objetoDocumento = null;
    	Trabajador objetotrabajador=null;
    	
    	if(file.isPresent()) {
    		archivo = file.get();
    	}
    	if(asistencia.isPresent()) {
    		objetoDocumento = asistencia.get();
    	}
    	
    	
		
		try {
			this.service.crearDocumento(objetoDocumento,file);				
			
			if (this.service.getError() == null) {
				response.setResultado("Registro actualizado");
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);				
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			} else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping(path = "/actualizar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarAsistencia(@RequestBody Asistencia asistencia){
		
		ResponseObject response = new ResponseObject();
		
		try {			
			
			this.service.actualizarAsistencia(asistencia);			
			
			if (this.service.getError() == null) {
				response.setResultado("Registro actualizado");
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);				
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			} else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);			
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/evaluacion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarEvaluacion( AsistenciaRequest asistenciaRequest, PageRequest paginaRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<Asistencia> lista = this.service.obtenerEvaluacion(asistenciaRequest, paginaRequest);
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
		}catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "/actualizarEvaluacion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarEvaluacion(@RequestBody Asistencia asistencia){
		
		ResponseObject response = new ResponseObject();
		
		try {
			this.service.actualizarEvaluacion(asistencia);
			if (this.service.getError() == null) {
				response.setResultado("Registro actualizado");
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);				
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			} else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(path = "/empleados", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> empleadosEvaluacion( Trabajador trabajador, PageRequest paginaRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<Trabajador> lista = this.service.obtenerEmpleadoEvaluacion(trabajador, paginaRequest);
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
		}catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	//Generador de PDF
		@GetMapping(path = "/generarPDF")
		public ResponseEntity<byte[]> escribirPdf(HttpServletResponse servletResponse, Trabajador trabajador,
				PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			pageRequest.setPagina(0);
			try {
				byte[] bytes = this.service.generarPdf(trabajador, pageRequest);
				response.setResultado(bytes);
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "attachment; filename="+"consulta"+".pdf");
				headers.add("Access-Control-Expose-Headers", "Content-Disposition");
				return ResponseEntity.ok().headers(headers).body(bytes);
			} catch (Exception e) {
				response.setError(1, "Error Interno",e.getMessage());
				response.setEstado(Estado.ERROR);
				String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
				LOGGER.error(error[1], e);
				return null;
			}
		}
}
