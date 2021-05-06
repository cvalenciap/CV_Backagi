package pe.com.sedapal.agi.api;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.model.Requisito;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IRequisitoService;

@RestController
@RequestMapping("/api/requisito")
public class RequisitoApi {
	@Autowired
	private IRequisitoService service;
	
	private static final Logger LOGGER = Logger.getLogger(RequisitoApi.class);	
	
	@GetMapping(path = "/obtenerNormaRequisitos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerNormaRequisitos(PageRequest pageRequest) throws GmdException {
		ResponseObject response = new ResponseObject();

		try {
			List<Requisito> lista = service.listaNormaRequerimientos(pageRequest);
			if (this.service.getError() == null) {
				response.setResultado(lista);
				response.setEstado(Estado.OK);
				response.setPaginacion(this.service.getPaginacion());
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
	
	/*@GetMapping(path = "/obtener", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarRequisito(RequisitoRequest requisitoRequest){
		ResponseObject response = new ResponseObject();
		try {			
			NodoRequisito nodo = this.service.obtenerRequisitos(requisitoRequest);
			if(this.service.getError() == null) {
				response.setResultado(nodo);
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//logger.msgInfo("consultarConstante()", "Estado del envio de correo: " + String.valueOf(bCorreoResponse.getStatus()));
		} catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/obtenerRequisitosAuditoria", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarRequisitosAuditoria(RequisitoRequest requisitoRequest){
		ResponseObject response = new ResponseObject();
		try {			
			List<NodoRequisito> listaNodos = this.service.obtenerListaRequisitos(requisitoRequest);
			if(this.service.getError() == null) {
				response.setResultado(listaNodos);
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);				
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//logger.msgInfo("consultarConstante()", "Estado del envio de correo: " + String.valueOf(bCorreoResponse.getStatus()));
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);			
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	

}
