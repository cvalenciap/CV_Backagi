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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.NodoJerarquia;
import pe.com.sedapal.agi.model.RelacionCoordinador;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IRelacionCoordinadorService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class RelacionCoordinadorApi {	
	@Autowired
	private IRelacionCoordinadorService serviceRelacion;	
	
	private static final Logger LOGGER = Logger.getLogger(RelacionCoordinadorApi.class);	
	
	@GetMapping(path = "/relacioncoordinador/tipoJerarquia/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerArbolJerarquiaPorTipo(@PathVariable("id") Long idJerarquia){
		ResponseObject response = new ResponseObject();
		try {			
			List<NodoJerarquia> listaJerarquia = this.serviceRelacion.obtenerArbolJerarquiaPorTipo(idJerarquia);
			if(this.serviceRelacion.getError() == null) {
				response.setResultado(listaJerarquia);
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.serviceRelacion.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/relacioncoordinador", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador, PageRequest page){
		ResponseObject response = new ResponseObject();
		try {			
			List<RelacionCoordinador> listaRelacion = this.serviceRelacion.obtenerRelacionGerenciaAlcance(relacionCoordinador, page);
			if(this.serviceRelacion.getError() == null) {
				response.setResultado(listaRelacion);
				response.setPaginacion(this.serviceRelacion.getPaginacion());
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.serviceRelacion.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "/relacioncoordinador", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> guardarRelacionGerenciaAlcance(@RequestBody RelacionCoordinador relacionCoordinador){
		ResponseObject response = new ResponseObject();
		try {			
			RelacionCoordinador item = this.serviceRelacion.guardarRelacionGerenciaAlcance(relacionCoordinador);				
			if (this.serviceRelacion.getError() == null) {
				response.setResultado(item);
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			} else {
				response.setError(this.serviceRelacion.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "/relacioncoordinador/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarGerenciaAlcance(@PathVariable("id") Long idRelacion, @RequestBody RelacionCoordinador relacionCoordinador){
		ResponseObject response = new ResponseObject();
		try {
			
			RelacionCoordinador item = this.serviceRelacion.actualizarRelacionGerenciaAlcance(idRelacion, relacionCoordinador);				
			if (this.serviceRelacion.getError() == null) {
				response.setResultado(item);
				response.setEstado(Estado.OK);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			} else {
				response.setError(this.serviceRelacion.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(path = "/relacioncoordinador/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarRelacionGerenciaAlcance(@PathVariable("id")  Long idRelacion){
		ResponseObject response = new ResponseObject();
		RelacionCoordinador relacion = new RelacionCoordinador();
		relacion.setIdRelacion(idRelacion);
		try {
			
			Boolean eliminado = this.serviceRelacion.eliminarRelacionGerenciaAlcance(relacion);
			if (this.serviceRelacion.getError() == null) {
				if(eliminado) {
					response.setResultado(eliminado);
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.serviceRelacion.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);	
				}
			} else {
				response.setError(this.serviceRelacion.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/relacioncoordinador/datoscoordinador", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerDatosCoordinador(@RequestParam(required = true) Long idGerencia, @RequestParam(required = true) Long idAlcance) {
		ResponseObject response = new ResponseObject();
		try {			
			List<RelacionCoordinador> listaRelacion = this.serviceRelacion.obtenerDatosCoordinador(idGerencia, idAlcance);
			if (this.serviceRelacion.getError() == null) {
				response.setEstado(Estado.OK);
				response.setResultado(listaRelacion);			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			} else {
				response.setError(this.serviceRelacion.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(this.serviceRelacion.getError());
			response.setEstado(Estado.ERROR);		
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@GetMapping(path = "/relacioncoordinador/datosJefeEquipo/{idArea}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> obtenerDatosJefeEquipo(@PathVariable("idArea") Long idArea) {
		ResponseObject response = new ResponseObject();
		try {			
			Long numeroFicha = this.serviceRelacion.obtenerDatosJefeEquipo(idArea);
			if (this.serviceRelacion.getError() == null) {
				response.setEstado(Estado.OK);
				response.setResultado(numeroFicha);			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			} else {
				response.setError(this.serviceRelacion.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(this.serviceRelacion.getError());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
