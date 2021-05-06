package pe.com.sedapal.agi.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IColaboradorService;
import pe.com.sedapal.agi.model.Colaborador;

@RestController
@RequestMapping("/api")
public class ColaboradorApi {
	@Autowired
	private IColaboradorService service;
	
	//Lista Ruta
	@GetMapping(path = "/colaborador", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarColaborador(ColaboradorRequest colaboradorRequest, PageRequest pageRequest){
		ResponseObject response = new ResponseObject();
		try {
			List<Colaborador> lista = this.service.obtenerColaborador(colaboradorRequest, pageRequest);	
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
	
	
	
	//Lista Auditoria
		@GetMapping(path = "/destinatarioAuditor", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarColaboradorAuditor(ColaboradorRequest colaboradorRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Colaborador> lista = this.service.obtenerColaboradorAuditoria(colaboradorRequest, pageRequest);	
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