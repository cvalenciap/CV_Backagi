package pe.com.sedapal.agi.api;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Dashboard;
import pe.com.sedapal.agi.model.TareasPendientes;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IConstanteService;
import pe.com.sedapal.agi.service.ITareasPendientesService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class TareasPendientesApi  {
	@Autowired
	private ITareasPendientesService serviceTareasPendientes;
	
	private static final Logger LOGGER = Logger.getLogger(TareasPendientesApi.class);	
	
	
	@Autowired
	private IConstanteService service;
	
	@GetMapping(path = "/tareaspendientesdocu/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerTareasPendientesDocumental(@PathVariable("id") Long idColaborador){
		ResponseObject response = new ResponseObject();
		try {
			
			List<TareasPendientes> listaTareasPendientes = this.serviceTareasPendientes.obtenerTareasPendientesDocumental(idColaborador);
			if(this.serviceTareasPendientes.getError() == null) {
				response.setResultado(listaTareasPendientes);	
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.serviceTareasPendientes.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Long Long(String textoConstante) {
		// TODO Auto-generated method stub
		return null;
	}

	private Integer Integer(String textoConstante) {
		// TODO Auto-generated method stub
		return null;
	}

	@GetMapping(path = "/tareaspendientesaudi/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerTareasPendientesAuditoria(@PathVariable("id") Long idColaborador){
		ResponseObject response = new ResponseObject();
		try {
			
			List<TareasPendientes> listaTareasPendientes = null;//this.serviceTareasPendientes.obtenerTareasPendientesDocumental(idColaborador);
			if(this.serviceTareasPendientes.getError() == null) {
				response.setResultado(listaTareasPendientes);	
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.serviceTareasPendientes.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/tareaspendientesnoconf/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerTareasPendientesNoConformidades(@PathVariable("id") Long idColaborador){
		ResponseObject response = new ResponseObject();
		try {			
			List<TareasPendientes> listaTareasPendientes = null;//this.serviceTareasPendientes.obtenerTareasPendientesDocumental(idColaborador);
			if(this.serviceTareasPendientes.getError() == null) {
				response.setResultado(listaTareasPendientes);	
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.serviceTareasPendientes.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/dashboardDocumento/{anio}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerDashboardDocumento(@PathVariable("anio") Long anio,
			@PathVariable("id") Long idTrimestre){
		ResponseObject response = new ResponseObject();
		try {			
			Dashboard estadistica = this.serviceTareasPendientes.obtenerDashboardDocumento(anio, idTrimestre);
			if(this.serviceTareasPendientes.getError() == null) {
				response.setResultado(estadistica);	
				response.setEstado(Estado.OK);			
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.serviceTareasPendientes.getError());
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
	
	@GetMapping(path = "/dashboardExcel/{anio}/{id}/{trimestre}")
	public void obtenerDashboardExcel(HttpServletResponse servletResponse, @PathVariable("anio") Long anio,
			@PathVariable("id") Long idTrimestre, @PathVariable("trimestre") String trimestre){
		try {
			String rutaExcel = this.serviceTareasPendientes.obtenerDashboardExcel(anio, idTrimestre, trimestre);
			File filedelete  = new File(rutaExcel);
			if(!filedelete.exists()) {
				servletResponse.setStatus(HttpStatus.NOT_FOUND.value());
			} else {
				servletResponse.setStatus(HttpStatus.OK.value());
				servletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				servletResponse.addHeader("Content-Disposition", "attachment; filename=" + filedelete.getName());
				Files.copy(filedelete.toPath(), servletResponse.getOutputStream());
				servletResponse.getOutputStream().flush();
			}
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			servletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		
		/*ResponseObject response = new ResponseObject();
		try {
			Dashboard estadistica = this.serviceTareasPendientes.obtenerDashboardDocumento(anio, idTrimestre);
			if(this.serviceTareasPendientes.getError() == null) {
				response.setResultado(estadistica);	
				response.setEstado(Estado.OK);
				return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
			}else {
				response.setError(this.serviceTareasPendientes.getError());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}
	
}