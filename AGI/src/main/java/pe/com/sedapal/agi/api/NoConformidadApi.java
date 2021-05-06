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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.NoConformidadSeguimiento;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.PlanAccionEjecucion;
import pe.com.sedapal.agi.model.request_objects.NoConformidadRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.INoConformidadService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class NoConformidadApi {
	@Autowired
	private INoConformidadService service;	
	
	private static final Logger LOGGER = Logger.getLogger(NoConformidadApi.class);	
	
	@GetMapping(path = "/noConformidad", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarNoConformidad(NoConformidadRequest areaRequest, PageRequest page){
		ResponseObject response = new ResponseObject();
		try {			
			List<NoConformidad> lista = this.service.obtenerNoConformidad(areaRequest, page);	
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
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/noConformidadSeguimiento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarNoConformidadSeguimiento(NoConformidadRequest noConformidadRequest, PageRequest pageRequest){
		ResponseObject response = new ResponseObject();
		try {			
			List<NoConformidadSeguimiento> lista = this.service.obtenerNoConformidadSeguimiento(noConformidadRequest,pageRequest);	
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

	@GetMapping(path = "/noConformidadDatos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDatosNoConformidad(NoConformidadRequest noConformidadRequest){
		ResponseObject response = new ResponseObject();
		try {			
			NoConformidad datosNoConformidades = this.service.obtenerDatosNoConformidad(noConformidadRequest);	
			if(this.service.getError() == null) {
				response.setResultado(datosNoConformidades);
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
	
	//Registrar No conformidad
	@PostMapping(path = "/noConformidad/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarNoCoformidad(@PathVariable("id") Long idNoConformidad, @RequestBody NoConformidad noConformidad){
		ResponseObject response = new ResponseObject();
		try {
			
			NoConformidad item = this.service.actualizarNoConformidad(idNoConformidad, noConformidad);				
			if (this.service.getError() == null) {
				response.setResultado(item);
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
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
	
	//Registrar No conformidad Seguimiento
	@PostMapping(path = "/insertarNoCoformidadSeguimiento/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> insertarNoCoformidadSeguimiento(@PathVariable("id") Long idNoConformidad, @RequestBody NoConformidad noConformidad){
		ResponseObject response = new ResponseObject();
		try {			
			NoConformidad item = this.service.insertarNoCoformidadSeguimiento(idNoConformidad, noConformidad);
			if (this.service.getError() == null) {
				response.setResultado(item);
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
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
	
	//Registrar Plan de Accion
	@PostMapping(path = "planAccion/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarDatosPlanAccion(@RequestBody Map<String,Object> mapaParametros, @PathVariable("codigo") Long idNoConformidad){
		ResponseObject response = new ResponseObject();
		try {			
			List<PlanAccion> listaPlanAccion = new ArrayList<>();
			if(mapaParametros.get("listaPlanAccion") != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				listaPlanAccion = objectMapper.readValue(mapaParametros.get("listaPlanAccion").toString(), new TypeReference<List<PlanAccion>>(){});
			}
			
			this.service.actualizarDatosPlanAccion(listaPlanAccion, idNoConformidad);
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
	
	//Registrar Ejecucion
	@PostMapping(path = "ejecucion/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarDatosEjecucion(@RequestBody Map<String,Object> mapaParametros, @PathVariable("codigo") Long idPlanAccion){
		ResponseObject response = new ResponseObject();
		try {			
			List<PlanAccionEjecucion> listaEjecucion = new ArrayList<>();
			if(mapaParametros.get("listaEjecucion") != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				listaEjecucion = objectMapper.readValue(mapaParametros.get("listaEjecucion").toString(), new TypeReference<List<PlanAccionEjecucion>>(){});
			}
			
			this.service.actualizarDatosEjecucion(listaEjecucion, idPlanAccion);
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
	
	//Lista Ejecucion
	@GetMapping(path = "/ejecucion/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerEjecucion(@PathVariable("id") Long idNoConformidad, PageRequest page){
		ResponseObject response = new ResponseObject();
		try {			
			List<PlanAccionEjecucion> listaEjecucion = this.service.obtenerEjecucion(idNoConformidad, page);
			if(this.service.getError() == null) {
				response.setResultado(listaEjecucion);
				response.setPaginacion(this.service.getPaginacion());response.setEstado(Estado.OK);
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
}
