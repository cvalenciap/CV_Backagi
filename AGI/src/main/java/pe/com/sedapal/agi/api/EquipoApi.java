package pe.com.sedapal.agi.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.sedapal.agi.model.request_objects.EquipoRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IEquipoService;
import pe.com.sedapal.agi.model.Equipo;

@RestController
@RequestMapping("/api")
public class EquipoApi {
	@Autowired
	private IEquipoService service;
	
	//Lista Equipo
	@GetMapping(path = "/equipo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarEquipo(EquipoRequest equipoRequest){
		ResponseObject response = new ResponseObject();
		try {
			List<Equipo> lista = this.service.obtenerEquipo(equipoRequest);	
			if(this.service.getError() == null) {
				response.setResultado(lista);
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