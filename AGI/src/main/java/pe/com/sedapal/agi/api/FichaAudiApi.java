package pe.com.sedapal.agi.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.com.sedapal.agi.model.FichaAudi;
import pe.com.sedapal.agi.model.request_objects.FichaAudiRequest;
import pe.com.sedapal.agi.model.request_objects.InfoAuditorRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IFichaAudiService;
import pe.com.sedapal.agi.util.Constantes.Mensajes;

@RestController
@RequestMapping(path = "/api/ficha-audi")
public class FichaAudiApi {

	private static final Logger LOGGER = Logger.getLogger(FichaAudiApi.class);

	@Autowired
	private IFichaAudiService service;
	
	@GetMapping(path = "/parametros")
	public ResponseObject obtenerParametros() {
		ResponseObject response = new ResponseObject();
		try {
			response.setEstado(Estado.ERROR);
			response.setResultado(service.obtenerParametros());
			if (service.getError() == null) {
				response.setEstado(Estado.OK);
			} else {
				response.setError(service.getError());
			}
		} catch (Exception e) {
			response.setError(this.obtenerError(e.getMessage()));
			LOGGER.error(e);
		}
		return response;
	}

	@PostMapping(path = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseObject obtenerListaAuditores(@RequestBody FichaAudiRequest request) {
		ResponseObject response = new ResponseObject();
		try {
			response.setEstado(Estado.ERROR);
			response.setResultado(service.obtenerListaAuditores(request));
			if (service.getError() == null) {
				response.setEstado(Estado.OK);
			} else {
				response.setError(service.getError());
			}
		} catch (Exception e) {
			response.setError(this.obtenerError(e.getMessage()));
			LOGGER.error(e);
		}
		return response;
	}

	@PostMapping(path = "/info-auditores", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseObject obtenerInfoAuditores(@RequestBody InfoAuditorRequest request,
			@RequestParam(name = "pagina") Integer pagina, @RequestParam(name = "registros") Integer registros) {
		ResponseObject response = new ResponseObject();
		try {
			response.setEstado(Estado.ERROR);
			response.setResultado(service.obtenerInfoAuditor(request, pagina, registros));
			if (service.getError() == null) {
				response.setEstado(Estado.OK);
			} else {
				response.setError(service.getError());
			}
		} catch (Exception e) {
			response.setError(this.obtenerError(e.getMessage()));
			LOGGER.error(e);
		}
		return response;
	}

	@PostMapping(path = "/registrar", produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject registrarFichaAauditor(@RequestBody FichaAudi fichaAuditor) {
		ResponseObject response = new ResponseObject();
		try {
			response.setEstado(Estado.ERROR);
			response.setResultado(service.registrarDatosFichaAuditor(fichaAuditor));
			if (service.getError() == null) {
				response.setEstado(Estado.OK);
			} else {
				response.setError(service.getError());
			}
		} catch (Exception e) {
			response.setError(this.obtenerError(e.getMessage()));
			LOGGER.error(e);
		}
		return response;
	}

	@DeleteMapping(path = "/eliminar")
	public ResponseObject eliminarFichaAuditor(@RequestParam(name = "idFicha") Integer idFicha,
			@RequestParam(name = "usuario") String usuario) {
		ResponseObject response = new ResponseObject();
		try {
			response.setEstado(Estado.ERROR);
			response.setResultado(service.eliminarFichaAuditor(idFicha, usuario));
			if (service.getError() == null) {
				response.setEstado(Estado.OK);
			} else {
				response.setError(service.getError());
			}
		} catch (Exception e) {
			response.setError(this.obtenerError(e.getMessage()));
			LOGGER.error(e);
		}
		return response;
	}
	
	@PostMapping(path = "/actualizar")
	public ResponseObject actualizarFichaAuditor(@RequestBody FichaAudi fichaAuditor) {
		ResponseObject response = new ResponseObject();
		try {
			response.setEstado(Estado.ERROR);
			response.setResultado(service.registrarCambiosFichaAuditor(fichaAuditor));
			if (service.getError() == null) {
				response.setEstado(Estado.OK);
			} else {
				response.setError(service.getError());
			}
		} catch (Exception e) {
			response.setError(this.obtenerError(e.getMessage()));
			LOGGER.error(e);
		}
		return response;
	}

	private Error obtenerError(String mensaje) {
		return new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), Mensajes.ERROR_OPERACION, mensaje);
	}

}
