package pe.com.sedapal.agi.api;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Plantilla;
import pe.com.sedapal.agi.model.request_objects.ConocimientoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlantillaRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IPlantillaService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class PlantillaApi {
	@Autowired
	private IPlantillaService service;
	
	private static final Logger LOGGER = Logger.getLogger(PlantillaApi.class);	
	//obtener plantilla
	@GetMapping(path = "/plantilla", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarPlantilla(PlantillaRequest plantillaRequest, PageRequest pageRequest){
		ResponseObject response = new ResponseObject();
		try {
			List<Plantilla> lista = this.service.obtenerPlantilla(plantillaRequest, pageRequest);	
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
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Registrar Plantilla	 	
	@PostMapping(path = "/plantilla", produces = MediaType.APPLICATION_JSON_VALUE)	 	
		public ResponseEntity<ResponseObject> agregarPlantilla(@RequestPart("file") Optional<MultipartFile> file ,@RequestPart("plantilla") Optional<Plantilla> plantilla){
			ResponseObject response = new ResponseObject();
			MultipartFile archivo = null;
	    	Plantilla objetoPlantilla = null;
	    	if(file.isPresent()) {
	    		archivo = file.get();
	    	}
	    	if(plantilla.isPresent()) {
	    		objetoPlantilla = plantilla.get();
	    	}		
			try {				
				
				Plantilla item = this.service.agregarPlantilla(objetoPlantilla,file);				
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
	//eliminar plantilla
		@DeleteMapping(path = "/plantilla/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> eliminarPlantilla(@PathVariable("codigo") Long codigo){
			ResponseObject response = new ResponseObject();
			Plantilla plantillaEliminar = new Plantilla();
			plantillaEliminar.setIdplan(codigo);
			try {
				Boolean eliminado = this.service.eliminarPlantilla(plantillaEliminar);
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
		
		
		@GetMapping(path = "/conocimiento", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarConocimiento(ConocimientoRequest conocimientoRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Conocimiento> lista = this.service.obtenerConocimiento(conocimientoRequest, pageRequest);	
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
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		@DeleteMapping(path = "/conocimiento/{codigo}/{codigoPersona}/{codigoDocumento}", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> eliminarConocimiento(@PathVariable("codigo") Long codigo,@PathVariable("codigoPersona") Long codigoPersona,@PathVariable("codigoDocumento") Long codigoDocumento){
			ResponseObject response = new ResponseObject();
			Conocimiento ConocimientoEliminar = new Conocimiento();
			ConocimientoEliminar.setIdconocimiento(codigo);
			ConocimientoEliminar.setIdpersona(codigoPersona);
			ConocimientoEliminar.setIddocumento(codigoDocumento);
			try {
				Boolean eliminado = this.service.eliminarConocimiento(ConocimientoEliminar);
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