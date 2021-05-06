package pe.com.sedapal.agi.api;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.DeteccionHallazgos;
import pe.com.sedapal.agi.model.request_objects.DeteccionHallazgosRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IDeteccionHallazgosService;
import pe.com.sedapal.agi.util.UConstante;


@RestController
@RequestMapping("/api/deteccion")
public class DeteccionHallazgoApi {


	@Autowired
	private IDeteccionHallazgosService service;
	
	private static final Logger LOGGER = Logger.getLogger(AreaApi.class);
	
	  
	@GetMapping(path = "/obtenerListaDeteccionHallazgo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerListasDeteccionHallazgos(DeteccionHallazgosRequest deteccionHallazgosRequest,PageRequest paginaRequest)
	throws Exception{
		ResponseObject response = new ResponseObject();
		try {
			List<DeteccionHallazgos> resultadoCons = this.service.obtenerListaDeteccionHallazgos(deteccionHallazgosRequest,paginaRequest) ;
			if(this.service.getError() == null) {
				response.setResultado(resultadoCons);
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
	
	@GetMapping(path = "/listaConstante", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> ListasConstante(@RequestParam Map<String,String> requestParm) throws ParseException
	{
		Map<String,Object> resultadoCons = this.service.obtenerListaConstantes(requestParm) ;
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);
	}
	
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> registrarDeteccion(@RequestBody DeteccionHallazgos deteccion) throws Exception{		
		ResponseObject response = new ResponseObject();
		try {
			boolean registro = this.service.registrarDatosDeteccionHallazgos(deteccion);
			if(this.service.getError() == null) {
				response.setResultado("Registro realizado");
				response.setPaginacion(this.service.getPaginacion());
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
