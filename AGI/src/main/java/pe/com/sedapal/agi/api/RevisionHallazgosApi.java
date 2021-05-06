package pe.com.sedapal.agi.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.request_objects.ListaVerificacionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IRevisionHallazgosService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api/revisionHallazgos")
public class RevisionHallazgosApi {
	
	private static final Logger LOGGER = Logger.getLogger(RevisionHallazgosApi.class);	
	@Autowired IRevisionHallazgosService service;
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarListaRevisionHallazgos(ListaVerificacionRequest listaVerificacionRequest, PageRequest paginaRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<ListaVerificacion> lista = this.service.obtenerRevisionHallazgos(listaVerificacionRequest, paginaRequest);
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
	
	@GetMapping(path = "/criterios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerCriteriosCalificacion(@PathVariable("id") Long idListaVerificacion){		
		ResponseObject response = new ResponseObject();
		try {
			List<CriterioResultado> lista = this.service.obtenerCriteriosCalificacion(idListaVerificacion);
			if(this.service.getError() == null) {
				response.setResultado(lista);
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
	
	@PostMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarListaVerificacionHallazgos(@RequestBody ListaVerificacion listaVerificacion,@PathVariable("id") Long idListVerificacion){		
		ResponseObject response = new ResponseObject();
		try {
			this.service.actualizarListaVerificacionHallazgos(listaVerificacion);
			if(this.service.getError() == null) {
				response.setResultado("Actualización realizada");
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
	
	@PostMapping(path = "/rechazar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> rechazarRevisionHallazgos(@RequestBody ListaVerificacion listaVerificacion,@PathVariable("id") Long idListVerificacion){		
		ResponseObject response = new ResponseObject();
		try {
			this.service.aprobarRechazarRevisionHallazgos(listaVerificacion, 2);
			if(this.service.getError() == null) {
				response.setResultado("Actualización realizada");
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
	
	@PostMapping(path = "/aprobar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> aprobarRevisionHallazgos(@RequestBody ListaVerificacion listaVerificacion,@PathVariable("id") Long idListVerificacion){		
		ResponseObject response = new ResponseObject();
		try {
			this.service.aprobarRechazarRevisionHallazgos(listaVerificacion, 1);
			if(this.service.getError() == null) {
				response.setResultado("Actualización realizada");
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
}
