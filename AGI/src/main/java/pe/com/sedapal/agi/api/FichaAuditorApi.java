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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.sedapal.agi.model.FichaAuditor;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IFichaAuditorService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api/ficha-auditor")
public class FichaAuditorApi {
	
	@Autowired
	private IFichaAuditorService service;
	
	private static final Logger LOGGER = Logger.getLogger(FichaAuditorApi.class);	
	
	@GetMapping(path = "/lista-constante", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> ConsultaConstante(@RequestParam Map<String,String> requestParm) throws ParseException
	{
		Map<String,Object> resultadoCons = this.service.ListaConsultaConstante(requestParm) ;
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);
	}
	
	@GetMapping(path = "/lista-bandeja", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> ConsultaFichaAuditor(@RequestParam Map<String,String> requestParm) throws ParseException
	{
		Map<String,Object> resultadoCons = this.service.ListaAuditorBandeja(requestParm) ;
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);
	}
	
	
	@DeleteMapping(path = "/lista-bandeja/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> eliminarRuta(@PathVariable("codigo") Long codigo){		
		Map<String,Object> resultadoCons = this.service.EliminarFicha(codigo);
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);				
	}
	
	@GetMapping(path = "/carga-cursos-obligatorios", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> CargaCursosObligatorios(@RequestParam Map<String,String> requestParm) throws ParseException
	{
		Map<String,Object> resultadoCons = this.service.ListaCursosObligatorios(requestParm) ;
		return new ResponseEntity<Map<String,Object>>(resultadoCons, HttpStatus.OK);
	}
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarAuditores(AuditorRequest auditoriaRequest, PageRequest paginaRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<FichaAuditor> lista = this.service.obtenerListaAuditores(auditoriaRequest, paginaRequest);
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
}
