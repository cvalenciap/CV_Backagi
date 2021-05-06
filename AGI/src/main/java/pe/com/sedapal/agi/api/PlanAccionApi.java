package pe.com.sedapal.agi.api;

import java.util.List;

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
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IPlanAccionService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class PlanAccionApi {
	@Autowired
	private IPlanAccionService servicePlanAccion;	
	
	private static final Logger LOGGER = Logger.getLogger(PlanAccionApi.class);	
	
	//Lista No Conformidades
	@GetMapping(path = "/reprogramacion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerNoConformidad(PlanAccionRequest planAccionRequest, PageRequest page){
		ResponseObject response = new ResponseObject();
		try {			
			List<NoConformidad> listaNoConformidad = this.servicePlanAccion.obtenerNoConformidad(planAccionRequest, page);
			if(this.servicePlanAccion.getError() == null) {
				response.setResultado(listaNoConformidad);
				response.setPaginacion(this.servicePlanAccion.getPaginacion());response.setEstado(Estado.OK);
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.servicePlanAccion.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Lista Plan Accion
	@GetMapping(path = "/reprogramacion/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerPlanAccion(@PathVariable("id") Long idNoConformidad, PlanAccionRequest planAccionRequest, PageRequest page){
		ResponseObject response = new ResponseObject();
		try {			
			List<PlanAccion> listaPlanAccion = this.servicePlanAccion.obtenerPlanAccion(idNoConformidad, page);
			if(this.servicePlanAccion.getError() == null) {
				response.setResultado(listaPlanAccion);
				response.setPaginacion(this.servicePlanAccion.getPaginacion());response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.servicePlanAccion.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {			
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Actualizar Plan Acción
	@PostMapping(path = "/reprogramacion/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarPlanAccion(@PathVariable("id") Long idPlanAccion, @RequestBody PlanAccion planAccion){
		
		ResponseObject response = new ResponseObject();
		try {			
			PlanAccion item = this.servicePlanAccion.actualizarPlanAccion(idPlanAccion, planAccion);				
			if (this.servicePlanAccion.getError() == null) {
				response.setResultado(item);
				response.setEstado(Estado.OK);
			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			} else {
				response.setError(this.servicePlanAccion.getError());
				response.setEstado(Estado.ERROR);			
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/accionPropuesta/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerListaAccionPropuesta(@PathVariable("id") Long idNoConformidad){
		ResponseObject response = new ResponseObject();
		try {			
			List<PlanAccion> listaPlanAccion = this.servicePlanAccion.obtenerListaAccionPropuesta(idNoConformidad);
			if(this.servicePlanAccion.getError() == null) {
				response.setResultado(listaPlanAccion);	
				response.setEstado(Estado.OK);				
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.servicePlanAccion.getError());
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
