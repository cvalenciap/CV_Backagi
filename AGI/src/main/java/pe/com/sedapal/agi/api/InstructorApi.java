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
import pe.com.sedapal.agi.model.Instructor;
import pe.com.sedapal.agi.model.request_objects.InstructorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IInstructorService;
import pe.com.sedapal.agi.util.UConstante;



@RestController
@RequestMapping("/api")
public class InstructorApi {
	
	@Autowired
	private IInstructorService service;
	private static final Logger LOGGER = Logger.getLogger(InstructorApi.class);	
	
	@GetMapping(path = "/instructor", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarInstructor(InstructorRequest instructorRequest, PageRequest page){
		ResponseObject response = new ResponseObject();
		try {
			
			List<Instructor> lista = this.service.obtenerInstructor(instructorRequest, page);	
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

	//Actualiza Instructor
	@PostMapping(path = "/instructor/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarInstructor(@PathVariable("id") Long id,
													     @RequestBody Instructor instructor){
		
		ResponseObject response = new ResponseObject();
		
		try {
			
			
			Instructor item = this.service.actualizarInstructor(id, instructor);				
			
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
	
	
	//Insertar Instructor
	@PostMapping(path = "/instructor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> insertarInstructor(@RequestBody Instructor instructor){
		
		ResponseObject response = new ResponseObject();
		
		try {
			
			
			Instructor item = this.service.insertarInstructor(instructor);				
			
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
		
//Eliminar Instructor
@DeleteMapping(path = "/instructor/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ResponseObject> eliminarFeriado(@PathVariable("id")  Long id) throws Exception{
	ResponseObject response = new ResponseObject();
	try {
		Boolean eliminado = this.service.eliminarInstructor(id);
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
