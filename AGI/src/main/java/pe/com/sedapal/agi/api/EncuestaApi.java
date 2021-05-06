package pe.com.sedapal.agi.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.Encuesta;
import pe.com.sedapal.agi.model.Encuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.EncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IEncuestaService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class EncuestaApi {
	
	@Autowired
	private IEncuestaService service;
	
	private static final Logger LOGGER = Logger.getLogger(EncuestaApi.class);	
	@GetMapping(path = "/encuesta", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarEncuesta(EncuestaRequest encuestaRequest, PageRequest page)throws Exception{
		ResponseObject response = new ResponseObject();
		try {			
			List<Encuesta> lista = this.service.obtenerEncuesta(encuestaRequest, page);	
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
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/detencuesta", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDetEncuesta(DetEncuestaRequest detEncuestaRequest, PageRequest page)throws Exception{
		ResponseObject response = new ResponseObject();
		try {
			
			List<DetEncuesta> lista = this.service.obtenerDetEncuesta(detEncuestaRequest, page);	
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
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/encuesta/detalle/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerDatoEncuesta(@PathVariable("codigo") Long idEncuesta)throws Exception{		
		ResponseObject response = new ResponseObject();
		try {
			Encuesta encuesta = this.service.obtenerDatosEncuesta(idEncuesta);
			if(this.service.getError() == null) {
				response.setResultado(encuesta);
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

	//Actualiza Encuesta
	@PostMapping(path = "/encuesta/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarEncuesta(@PathVariable("id") Long id,
													     @RequestBody Encuesta encuesta)throws Exception{
		
		ResponseObject response = new ResponseObject();
		
		try {			
			
			Encuesta item = this.service.actualizarEncuesta(id, encuesta);				
			
			if (this.service.getError() == null) {
				response.setResultado(item);
				response.setEstado(Estado.OK);
				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
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
	
	
	//Insertar Encuesta
	@PostMapping(path = "/encuesta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> insertarEncuesta(@RequestBody Encuesta encuesta)throws Exception{
		
		ResponseObject response = new ResponseObject();
		
		try {
			Encuesta item = this.service.insertarEncuesta(encuesta);				
			
			if (this.service.getError() == null) {
				response.setResultado(item);
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
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
		
//Eliminar Encuesta
@DeleteMapping(path = "/encuesta/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ResponseObject> eliminarEncuesta(@PathVariable("id")  Long id) throws Exception{
	ResponseObject response = new ResponseObject();
	try {
		Boolean eliminado = this.service.eliminarEncuesta(id);
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

@DeleteMapping(path = "/encuesta/detalle/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ResponseObject> eliminarEncuestaDetalle(@PathVariable("id")  Long id)throws Exception{
	ResponseObject response = new ResponseObject();
	try {
		Boolean eliminado = this.service.eliminarEncuestaDet(id);
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
	
	

}
