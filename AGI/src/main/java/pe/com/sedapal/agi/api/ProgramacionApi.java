package pe.com.sedapal.agi.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ProgramaRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IProgramacionService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UJson;

@RestController
@RequestMapping("/api/programacion")
public class ProgramacionApi {
	@Autowired
	private IProgramacionService service;
	
	private static final Logger LOGGER = Logger.getLogger(ProgramacionApi.class);	
	
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarProgramas(ProgramaRequest programaRequest, PageRequest paginaRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<Programa> lista = this.service.obtenerProgramas(programaRequest, paginaRequest);
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
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> registrarPrograma(@RequestBody Map<String,Object> mapaParametros){		
		ResponseObject response = new ResponseObject();
		try {
			Programa programa = null;
			if(mapaParametros.get("programacion") != null) {
				programa = UJson.convertirJsonATipo(mapaParametros.get("programacion").toString(), Programa.class);
			}
			List<Auditoria> listaAuditoria = new ArrayList<>();
			if(mapaParametros.get("listaAuditorias") != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				listaAuditoria = objectMapper.readValue(mapaParametros.get("listaAuditorias").toString(), new TypeReference<List<Auditoria>>(){});
			}
			
			this.service.registrarDatosPrograma(programa, listaAuditoria);
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
	
	@PostMapping(path = "/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarPrograma(@RequestBody Map<String,Object> mapaParametros, @PathVariable("codigo") Long codigo){		
		ResponseObject response = new ResponseObject();
		try {
			Programa programa = null;
			if(mapaParametros.get("programacion") != null) {
				programa = UJson.convertirJsonATipo(mapaParametros.get("programacion").toString(), Programa.class);
			}
			List<Auditoria> listaAuditoriasNuevas = new ArrayList<>();
			if(mapaParametros.get("listaAuditoriasNuevas") != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				listaAuditoriasNuevas = objectMapper.readValue(mapaParametros.get("listaAuditoriasNuevas").toString(), new TypeReference<List<Auditoria>>(){});
			}
			
			List<Auditoria> listaAuditoriasEliminadas = new ArrayList<>();
			if(mapaParametros.get("listaAuditoriasEliminadas") != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				listaAuditoriasEliminadas = objectMapper.readValue(mapaParametros.get("listaAuditoriasEliminadas").toString(), new TypeReference<List<Auditoria>>(){});
			}
			
			this.service.actualizarDatosPrograma(programa, listaAuditoriasEliminadas, listaAuditoriasNuevas);
			if(this.service.getError() == null) {
				response.setResultado("Actualizacion realizado");
				response.setPaginacion(this.service.getPaginacion());
				response.setEstado(Estado.OK);				
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.service.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}


		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerProgramaPorId(@PathVariable("codigo") Long idPrograma){
		
		ResponseObject response = new ResponseObject();
		try {
			Programa programa = this.service.obtenerProgramaPorId(idPrograma);
			if(this.service.getError() == null) {
				System.out.println(programa.getFechaPrograma());
				response.setResultado(programa);
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
	
	
	
	@DeleteMapping(path = "/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarPrograma(@PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Boolean eliminado = this.service.eliminarPrograma(codigo);
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
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
