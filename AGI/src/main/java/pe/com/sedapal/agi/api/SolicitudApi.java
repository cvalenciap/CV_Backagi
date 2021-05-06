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
import pe.com.sedapal.agi.model.Area;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.Solicitud;
import pe.com.sedapal.agi.model.SolicitudCopia;
import pe.com.sedapal.agi.model.request_objects.AreaRequest;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IColaboradorService;
import pe.com.sedapal.agi.service.ISolicitudService;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class SolicitudApi {
	@Autowired
	private ISolicitudService service;
	@Autowired
	private IColaboradorService serviceDes;
	
	private static final Logger LOGGER = Logger.getLogger(SolicitudApi.class);	
	
	
	//Registro Solicitud de Copia	
		@PostMapping(path = "/solicitud", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> crearDocumento(@RequestBody SolicitudCopia solicitud ){
			ResponseObject response = new ResponseObject();
			try {
				Documento item  =  this.service.crearSolicitud(solicitud);
				if (this.service.getError() == null) {
					response.setResultado(item);
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				response.setError(1, "Error Interno", e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//Actualizar Solicitud (Aprobar o rechazar )		
		@PostMapping(path = "/solicitud/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)		
		public ResponseEntity<ResponseObject> actualizarDocumento(@RequestBody SolicitudCopia solicitud, @PathVariable("codigo") Long codigo){
			ResponseObject response = new ResponseObject();
			try {
				Documento item = this.service.actualizarDocumento(solicitud, codigo);
				if (this.service.getError() == null) {
					response.setResultado(item);
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				response.setError(1, "Error Interno", e.getMessage());
				response.setEstado(Estado.ERROR);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}		
		}
		// Lista de Solicitud
				@GetMapping(path = "/solicitudBitacora", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> consultarRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest){
					ResponseObject response = new ResponseObject();
					try {
						List<Revision> lista = this.service.obtenerSolicitudBitacora(revisionRequest, pageRequest);	
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
		//Obtener Distinatario
				@GetMapping(path = "/destinatario", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> consultarColaborador(ColaboradorRequest colaboradorRequest, PageRequest pageRequest){
					ResponseObject response = new ResponseObject();
					try {
						List<Colaborador> lista = this.serviceDes.obtenerDestinatario(colaboradorRequest, pageRequest);	
						if(this.serviceDes.getError() == null) {
							response.setResultado(lista);
							response.setPaginacion(this.serviceDes.getPaginacion());
							response.setEstado(Estado.OK);
							return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
						}else {
							response.setError(this.serviceDes.getError());
							response.setEstado(Estado.ERROR);
							return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
						}
					} catch (Exception e) {
						response.setError(1, "Error Interno",e.getMessage());
						response.setEstado(Estado.ERROR);
						return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				// Lista de BandejaSolicitudCopia Documento
						@GetMapping(path = "/BandejaSolicitudCopia", produces = MediaType.APPLICATION_JSON_VALUE)
						public ResponseEntity<ResponseObject> consultarRevisionD(RevisionRequest revisionRequest, PageRequest pageRequest){
							ResponseObject response = new ResponseObject();
							try {
								List<Revision> lista = this.service.obtenerSolicitudBitacoraDocumento(revisionRequest, pageRequest);	
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
