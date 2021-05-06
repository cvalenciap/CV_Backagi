package pe.com.sedapal.agi.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mortbay.util.ajax.JSON;
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
import pe.com.sedapal.agi.model.AreaAlcanceAuditoria;
import pe.com.sedapal.agi.model.AreaAuditoria;
import pe.com.sedapal.agi.model.AreaCargoAuditoria;
import pe.com.sedapal.agi.model.AreaParametros;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IAreaAuditoriaService;
import pe.com.sedapal.agi.util.MapperUtil;

@RestController
@RequestMapping("/api")
public class AreaAuditoriaApi {

	@Autowired
	private IAreaAuditoriaService areaAuditoriaService;
	private static final Logger LOGGER = Logger.getLogger(AreaApi.class);

	@GetMapping(path = "/areaAuditoria/listado", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerAreaParametros() {
		ResponseObject response = new ResponseObject();
		try {
			AreaParametros areaParametros = this.areaAuditoriaService.obtenerAreaParametros();
			if (this.areaAuditoriaService.getError() == null) {
				response.setResultado(areaParametros);
				response.setEstado(Estado.OK);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			} else {
				response.setError(this.areaAuditoriaService.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(path = "areaAuditoria/guardarArea", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> registrarAreasAuditoria(@RequestBody AreaAuditoria areaAuditoria,
			@RequestParam("lstAlcances") String lstAlcances, @RequestParam("lstCargos") String lstCargos)
			throws Exception {

		ResponseObject response = new ResponseObject();
		MapperUtil util = new MapperUtil();
		
		List<AreaAlcanceAuditoria> listaAlcances = util.mapearAreaAlcanceAuditoria(lstAlcances);
		List<AreaCargoAuditoria> listaCargos = util.mapearAreaCargoAuditoria(lstCargos);
		
		try {
			Integer resultado = this.areaAuditoriaService.registrarAuditoria(areaAuditoria, listaAlcances, listaCargos);
			response.setResultado(resultado);
			response.setEstado(Estado.OK);
			response.setError(0, "Grabación exitosa", "El registro se actualizó correctamente");
			return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			response.setError(0, "Grabación fallida", "El registro no se actualizó, intente nuevamente");
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@PostMapping(path = "areaAuditoria/eliminarArea", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarAreaAuditoria(@RequestBody AreaAuditoria areaAuditoria){
		ResponseObject response = new ResponseObject();
			
		try {
			Integer resultado = this.areaAuditoriaService.eliminarAuditoria(areaAuditoria);
			response.setResultado(resultado);
			response.setEstado(Estado.OK);
			response.setError(0, "Grabación exitosa", "El registro se actualizó correctamente");
			return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			response.setError(0, "Grabación fallida", "El registro no se actualizó, intente nuevamente");
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
