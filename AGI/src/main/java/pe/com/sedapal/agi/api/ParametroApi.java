package pe.com.sedapal.agi.api;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ParametroRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IParametroService;
import pe.com.sedapal.agi.model.Constante;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.util.UConstante;

//import org.apache.log4j.Logger;

@RestController
@RequestMapping("/api")
public class ParametroApi {
	@Autowired
	private IParametroService service;
	
	private static final Logger LOGGER = Logger.getLogger(ParametroApi.class);	
	
	//Lista Constante
	@GetMapping(path = "/parametro", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarConstante(ParametroRequest parametroRequest, PageRequest page){
		ResponseObject response = new ResponseObject();
		try {			
			List<Constante> lista = this.service.obtenerConstantes(parametroRequest, page,null);	
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
	
	//Registrar Parametro
		@PostMapping(path = "/parametro", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> actualizarParametro(@RequestBody Constante constante){
			ResponseObject response = new ResponseObject();
			try {				
				List<Constante> listaParametro = new ArrayList<>();
//				if(mapaParametros.get("listaParametro") != null) {
//					ObjectMapper objectMapper = new ObjectMapper();
//					listaParametro = objectMapper.readValue(mapaParametros.get("listaParametro").toString(), new TypeReference<List<Constante>>(){});
//				}
				
				this.service.actualizarParametro(constante);
				if(this.service.getError() == null) {
					response.setResultado("Actualización Exitosa");
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);					
					return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
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
	
	/*
	//Actualiza Constante
		@PostMapping(path = "/constante/{id}/{i_nidpadre}/{i_vcampo1}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> actualizarConstante(@PathVariable("id") Long id,
																  @PathVariable("i_nidpadre") Long i_nidpadre, 
																  @PathVariable("i_vcampo1") String i_vcampo1, 
																  @RequestBody Constante constante){
			
			ResponseObject response = new ResponseObject();
			try {
				logger.msgInfoInicio("AGI - Inicio actualizarConstante()");
				Constante item = this.service.actualizarConstante(id, i_nidpadre, i_vcampo1, constante);				
				if (this.service.getError() == null) {
					response.setResultado(item);
					response.setEstado(Estado.OK);
					logger.msgInfoFin("AGI - Fin actualizarConstante()");
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					logger.msgInfoFin("AGI - Fin actualizarConstante()");
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch(Exception e) {
				logger.msgError("AGI - Error actualizarConstante(): ", e.getMessage(), e);
//				response.setError(1, "Error Interno", e.getMessage());
//				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}*/
	/*		
	//Eliminar Constante
	@DeleteMapping(path = "/constante/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarFeriado(@PathVariable("id")  Long id){
		ResponseObject response = new ResponseObject();
		try {
			Boolean eliminado = this.service.eliminarConstante(id);
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
	}	*/
}
