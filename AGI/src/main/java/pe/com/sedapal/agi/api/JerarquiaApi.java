package pe.com.sedapal.agi.api;

import java.util.List;
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

import pe.com.sedapal.agi.model.request_objects.JerarquiaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IJerarquiaService;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.NodoJerarquia;

@RestController
@RequestMapping("/api")
public class JerarquiaApi {
	@Autowired
	private IJerarquiaService service;
	
	//Lista Jerarquia
	@GetMapping(path = "/jerarquia", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarJerarquia(JerarquiaRequest jerarquiaRequest){	
		ResponseObject response = new ResponseObject();
		try {
			//logger.msgInfoInicio("AGI - Inicio consultarRequisito()");
			List<NodoJerarquia> nodo = this.service.obtenerJerarquia(jerarquiaRequest);
			if(this.service.getError() == null) {
				response.setResultado(nodo);
				response.setEstado(Estado.OK);
				//logger.msgInfoFin("AGI - Fin consultarRequisito()");
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);
				//logger.msgInfoFin("AGI - Fin consultarRequisito()");
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//logger.msgInfo("consultarConstante()", "Estado del envio de correo: " + String.valueOf(bCorreoResponse.getStatus()));
		} catch (Exception e) {
			//logger.msgError("AGI - Error consultarRequisito(): ", e.getMessage(), e);
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			//logger.msgInfoFin("AGI - Fin consultarRequisito()");
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/jerarquia/tipo-documento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarJerarquiaTipoDocumento(JerarquiaRequest jerarquiaRequest, PageRequest pageRequest){	
		ResponseObject response = new ResponseObject();
		try {
			List<Jerarquia> jerarquia = this.service.obtenerJerarquiaTipoDocumento(jerarquiaRequest,pageRequest);
			if(this.service.getError() == null) {
				response.setResultado(jerarquia);
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
	
	@GetMapping(path = "/jerarquiaIdPadre", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarJerarquiaID(JerarquiaRequest jerarquiaRequest){
		ResponseObject response = new ResponseObject();
		try {
		
			List<Jerarquia> jerarquia = this.service.obtenerJerarquiaIdPadre(jerarquiaRequest);
			if(this.service.getError() == null) {
				response.setResultado(jerarquia);
				response.setEstado(Estado.OK);
				//logger.msgInfoFin("AGI - Fin consultarRequisito()");
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);
				//logger.msgInfoFin("AGI - Fin consultarRequisito()");
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//logger.msgInfo("consultarConstante()", "Estado del envio de correo: " + String.valueOf(bCorreoResponse.getStatus()));
		} catch (Exception e) {
			//logger.msgError("AGI - Error consultarRequisito(): ", e.getMessage(), e);
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			//logger.msgInfoFin("AGI - Fin consultarRequisito()");
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping(path = "/jerarquia", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> crearJerarquia(@RequestBody Jerarquia jerarquia){
		ResponseObject response = new ResponseObject();
		try {
			Jerarquia item  =  this.service.crearJerarquia(jerarquia);
			if (this.service.getError() == null) {
				response.setResultado(item);
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
	
	@PostMapping(path = "/jerarquia/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarJerarquia(@RequestBody Jerarquia jerarquia, @PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Jerarquia item = this.service.actualizarJerarquia(jerarquia, codigo);
			if (this.service.getError() == null) {
				response.setResultado(item);
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
	
	@PostMapping(path = "/permiso/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarPermiso(@RequestBody Jerarquia jerarquia, @PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Jerarquia item = this.service.actualizarPermiso(jerarquia, codigo);
			if (this.service.getError() == null) {
				response.setResultado(item);
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

	@DeleteMapping(path = "/jerarquia/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarJerarquia(@PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		
		try {
			Boolean eliminado = this.service.eliminarJerarquia(codigo);
			if (this.service.getError()==null) {
				if (eliminado) {
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
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping(path = "/jerarquia/hijos/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarHijosJerarquia(@RequestBody Jerarquia jerarquia, @PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Jerarquia item = this.service.actualizarHijosJerarquia(jerarquia, codigo);
			if (this.service.getError() == null) {
				response.setResultado(item);
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
	
	
	@GetMapping(path = "/jerarquia/hijos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarHijosJerarquias(Jerarquia jerarquia){
		ResponseObject response = new ResponseObject();
		try {
			List<Jerarquia> nodo = this.service.obtenerHijoJerarquia(jerarquia);
			if(this.service.getError() == null) {
				response.setResultado(nodo);
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
	
	@GetMapping(path = "/jerarquia/doc", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDocJerarquia(Jerarquia jerarquia){
		ResponseObject response = new ResponseObject();
		try {
			List<Jerarquia> nodo = this.service.obtenerDocJerarquia(jerarquia);
			if(this.service.getError() == null) {
				response.setResultado(nodo);
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
}