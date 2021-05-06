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

import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.request_objects.AulaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IAulaService;
import pe.com.sedapal.agi.util.UConstante;



@RestController
@RequestMapping("/api")
public class AulaApi {
	
	@Autowired
	private IAulaService service;
	
	private static final Logger LOGGER = Logger.getLogger(AulaApi.class);
	
	@GetMapping(path = "/aula", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarAula(AulaRequest aulaRequest, PageRequest page)throws GmdException{
		ResponseObject response = new ResponseObject();
		try {
			
			List<Aula> lista = this.service.obtenerAula(aulaRequest, page);	
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

	//Actualiza Aula
	@PostMapping(path = "/aula/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarAula(@PathVariable("id") Long id,
													     @RequestBody Aula aula)throws GmdException{
		
		ResponseObject response = new ResponseObject();
		
		try {
			Aula item = this.service.actualizarAula(id, aula);
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
	
	
	//Insertar Aula
	@PostMapping(path = "/aula", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> insertarAula(@RequestBody Aula aula)throws GmdException{
		
		ResponseObject response = new ResponseObject();
		
		try {
			
			
			Aula item = this.service.insertarAula(aula);				
			
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
		
//Eliminar Aula
@DeleteMapping(path = "/aula/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ResponseObject> eliminarFeriado(@PathVariable("id")  Long id) throws GmdException{
	ResponseObject response = new ResponseObject();
	try {
		Boolean eliminado = this.service.eliminarAula(id);
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
