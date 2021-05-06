package pe.com.sedapal.agi.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.sedapal.agi.model.Correo;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.ICorreoDatosService;

@RestController
@RequestMapping("/api")
public class CorreoDatosApi {
	@Autowired
	private ICorreoDatosService service;	
	
	@GetMapping(path = "/obtenerCorreo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerCorreo(Long idDocumento, Long idOperacion, String tipoCorreo)
	throws Exception {
		ResponseObject response = new ResponseObject();
		try {
			List<Long> listaId = new ArrayList<>();
			listaId.add(idDocumento);
			listaId.add(idOperacion);
			Correo objeto = this.service.obtenerCorreo(listaId, tipoCorreo);
			if(this.service.getError() == null) {
				response.setResultado(objeto);
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
	
	@GetMapping(path = "/obtenerListaCorreo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerListaCorreo(Long idDocumento, Long idOperacion, String tipoCorreo)
	throws Exception {
		ResponseObject response = new ResponseObject();
		try {
			List<Long> listaId = new ArrayList<>();
			listaId.add(idDocumento);
			listaId.add(idOperacion);
			List<Correo> lista = this.service.obtenerListaCorreo(listaId, tipoCorreo);
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