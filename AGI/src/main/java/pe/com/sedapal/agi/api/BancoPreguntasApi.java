package pe.com.sedapal.agi.api;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.BancoPreguntasRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IBancoPreguntaService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api/banco-pregunta")
public class BancoPreguntasApi {
	
	@Autowired
	private IBancoPreguntaService service;
	
	private static final Logger LOGGER = Logger.getLogger(AreaApi.class);	
	
	@GetMapping(path = "/lista-preguntas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> ListaBancoPregunta(Pregunta pregunta , PageRequest pageRequest)throws GmdException {
		ResponseObject response = new ResponseObject();
		try {
			List<Pregunta> lista = this.service.ListaBancoPreguntas(pregunta, pageRequest);	
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
	
	
	@PostMapping(path = "/guardar-banco-preguntas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> GuardarDatosPregunta(@RequestBody Pregunta pregunta) throws ParseException
	{
		Map<String,Object> resultadoCons = this.service.GuardarBancoPreguntas(pregunta) ;
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);	
    }
	
	@PostMapping(path = "/actualizar-banco-preguntas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> ActualizarDatosPregunta(@RequestBody Pregunta pregunta) throws ParseException
	{
		Map<String,Object> resultadoCons = this.service.ActualizarBancoPreguntas(pregunta) ;
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);	
    } 
	

	//Eliminar Aula
	@DeleteMapping(path = "/eliminar-pregunta/{codigo}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarPregunta(@PathVariable("codigo")  Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Boolean eliminado = this.service.EliminarPregunta(codigo);
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
	}	
		
	
	@GetMapping(path = "/obtener-pregunta", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> ObtenerPregunta(@RequestParam Map<String,String> requestParm) throws ParseException
	{
		Map<String,Object> resultadoCons = this.service.ObtenerPreguntaDatos(requestParm) ;
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);
	}
	

	

	@GetMapping(path = "/obtenerRoles", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> ObtenerRoles(BancoPreguntasRequest bancoPreguntas) throws GmdException{
		ResponseObject response = new ResponseObject();
		try {   
			List<Constante> lista = this.service.buscarRoles(bancoPreguntas.getDescripcion());	
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
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	   
	
}
