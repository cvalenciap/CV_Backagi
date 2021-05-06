package pe.com.sedapal.agi.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.ICursoService;
import pe.com.sedapal.agi.service.ISesionService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class SesionApi {
	
	@Autowired ISesionService service;
	
	
	private static final Logger LOGGER = Logger.getLogger(SesionApi.class);	
	
	@GetMapping(path = "/{codCurso}/{codSesion}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerDatoCurso(@PathVariable("codCurso") Long idCurso, @PathVariable("codSesion") Long idSesion){		
		ResponseObject response = new ResponseObject();
		try {
			Sesion sesion = this.service.obtenerDatosSesionCurso(idCurso, idSesion);
			if(this.service.getError() == null) {
				response.setResultado(sesion);
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
}
