package pe.com.sedapal.agi.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Capacitacion;
import pe.com.sedapal.agi.model.CapacitacionDocumentos;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.CursoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PreguntaCursoRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.ICapacitacionService;
import pe.com.sedapal.agi.service.ICursoService;
import pe.com.sedapal.agi.service.ITrabajadorService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UJson;

@RestController
@RequestMapping("/api")
public class CapacitacionApi {
	
	@Autowired ICapacitacionService service;
	
	private static final Logger LOGGER = Logger.getLogger(AreaApi.class);	
	
	@GetMapping(path = "/capacitacion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarCapacitacion(Capacitacion capacitacion, PageRequest paginaRequest)throws GmdException {		
		ResponseObject response = new ResponseObject();
		try {
			List<Capacitacion> lista = this.service.obtenerCapacitaciones(capacitacion, paginaRequest);
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
	
	@PostMapping(path = "/guardarCapacitacion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> registrarCapacitacion(@RequestBody Capacitacion capacitacion)throws Exception{		
		ResponseObject response = new ResponseObject();
		try {
			this.service.registrarDatosCapacitacion(capacitacion);
			if(this.service.getError()==null) {
				response.setResultado("Registro realizado");
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);				
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			} else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(path = "/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarCapacitacion(@PathVariable("codigo") Long idCapacitacion)throws Exception{
		ResponseObject response = new ResponseObject();
		Capacitacion capacitacion = new Capacitacion();
		capacitacion.setIdCapacitacion(idCapacitacion);
		try {
			boolean eliminado = this.service.eliminarCapacitacion(capacitacion);
			if(this.service.getError() == null) {
				if(eliminado) {
					response.setResultado("Registro eliminado");
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);					
					return new ResponseEntity<ResponseObject>(response, HttpStatus.NOT_FOUND);
				}
			} else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> modificarCapacitacion(@RequestBody Capacitacion capacitacion,@PathVariable("codigo") Long codigo)throws Exception{		
		ResponseObject response = new ResponseObject();
		try {
			Capacitacion datosCapacitacion = this.service.actualizarCapacitacion(capacitacion);
			if(this.service.getError() == null) {
				response.setResultado("Actualización realizada");
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);		
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch(Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "/enviarParticipante", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> enviarCapacitacionPart(@RequestBody Asistencia asistencia)throws Exception{		
		ResponseObject response = new ResponseObject();
		try {
			boolean envio = this.service.enviarCapacitacionPart(asistencia);
			if(this.service.getError() == null) {
				if(envio) {
					response.setResultado("Envío Correcto");
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);					
					return new ResponseEntity<ResponseObject>(response, HttpStatus.NOT_FOUND);
				}
			} else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "/cargarDocumento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> cargarDocumento(@RequestPart("file") Optional<MultipartFile> file,@RequestPart("capacitacion") Optional<CapacitacionDocumentos> capacitacion )throws Exception{
		System.out.println("Nombre : " + capacitacion.get().getNombreDocumento());
		ResponseObject response = new ResponseObject();
		MultipartFile archivo = null;
		CapacitacionDocumentos objetoCapacitacion = null;
    	
    	if(file.isPresent()) {
    		archivo = file.get();
    	}
    	if(capacitacion.isPresent()) {
    		objetoCapacitacion = capacitacion.get();
    	}

		try {
			this.service.cargarDocumento(objetoCapacitacion,file);				
			
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
	
	@GetMapping(path = "preguntaCurso", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarPreguntaCursoId(PreguntaCurso pregunta, PageRequest paginaRequest)throws Exception{		
		ResponseObject response = new ResponseObject();
		try {
			List<PreguntaCurso> lista = this.service.consultarPreguntaCursoId(pregunta, paginaRequest);
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

}
