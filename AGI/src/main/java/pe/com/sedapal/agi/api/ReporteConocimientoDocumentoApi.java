package pe.com.sedapal.agi.api;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.ConocimientoRevisionDocumento;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IReporteConocimientoDocumentoService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api/reporteConocimientoDocumento")
public class ReporteConocimientoDocumentoApi {
	
	@Autowired
	private IReporteConocimientoDocumentoService service;
	
	
	private static final Logger LOGGER = Logger.getLogger(ReporteConocimientoDocumentoApi.class);
	
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerConocimientoDocumuento(DocumentoRequest documentoRequest, PageRequest paginaRequest){		
		ResponseObject response = new ResponseObject();
		try {
			List<ConocimientoRevisionDocumento> lista = this.service.consultarDocumentosRevision(documentoRequest,paginaRequest);
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
			return null;
		}
	}
	
	@GetMapping(path = "/generarReporte", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> generarReporteConocimiento(RevisionRequest request) {
		ResponseObject response = new ResponseObject();
		try {
			byte[] bytes = this.service.generarReporteConocimientoDocumento(request);
			response.setResultado(bytes);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=" + "reporte" + ".pdf");
			headers.add("Access-Control-Expose-Headers", "Content-Disposition");
			return ResponseEntity.ok().headers(headers).body(bytes);
		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return null;
		}
	}

}
