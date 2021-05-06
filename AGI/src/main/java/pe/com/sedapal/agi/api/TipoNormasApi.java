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
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.ITipoNormasService;

@RestController
@RequestMapping("/api/tipoNormas")
public class TipoNormasApi {
	@Autowired
	private ITipoNormasService service;

	private static final Logger LOGGER = Logger.getLogger(TipoNormasApi.class);

	@GetMapping(path = "/obtenerTiposNormasAudi", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerTiposNormas(PageRequest pageRequest) throws GmdException {
		ResponseObject response = new ResponseObject();

		try {
			List<Constante> lista = service.listaTipoNormas(pageRequest);
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
	
}
