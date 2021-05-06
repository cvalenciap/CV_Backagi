package pe.com.sedapal.agi.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.SeguimientoDocumento;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.ISeguimientoService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api/seguimientoDocumento")
public class SeguimientoDocumentoApi {
	
	@Autowired
	private ISeguimientoService service;
	
	
	private static final Logger LOGGER = Logger.getLogger(SeguimientoDocumentoApi.class);	
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarSeguimientoDocumento(DocumentoRequest documentoRequest, PageRequest paginaRequest){
		ResponseObject response = new ResponseObject();
		try {
			List<SeguimientoDocumento> lista = this.service.obtenerSeguimientoDocumento(documentoRequest, paginaRequest);
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
		}catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/flujoDocumento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarFlujoDocumento(RevisionRequest revisionRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<Flujo> lista = this.service.obtenerFlujosDocumento(revisionRequest);
			if(this.service.getError() == null) {
				response.setResultado(lista);
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
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
