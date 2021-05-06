package pe.com.sedapal.agi.api;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.EvaluacionAuditor;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.request_objects.EvaluacionAuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IEvaluacionAuditorService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UJson;

@RestController
@RequestMapping("/api/evaluacion")
public class EvaluacionAuditorApi {


	@Autowired
	private IEvaluacionAuditorService service;
	  
	
	
	private static final Logger LOGGER = Logger.getLogger(EvaluacionAuditorApi.class);
	
	//Lista Evaluacion Auditor Bandeja principal
		@GetMapping(path = "/obtenerListaEvalAuditorGrilla", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarEvalAuditorGrilla(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try { 
				List<EvaluacionAuditor> lista = this.service.obtenerListaEvaluacionAuditorGrilla(evaluacionAuditorRequest, pageRequest);	
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
		
       //para obtener una evaluacion auditor
				
		@GetMapping(path = "/obtenerEvalAuditor", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> ObtenerEvalAuditorPorNroAud(EvaluacionAuditorRequest evaluacionAuditorRequest){
			ResponseObject response = new ResponseObject();
			try { 

				EvaluacionAuditor evaluacionAuditor = this.service.obtenerEvaluacionAuditor(evaluacionAuditorRequest);	

				if(this.service.getError() == null) {
					response.setResultado(evaluacionAuditor);
					response.setEstado(Estado.OK);					
					
				
					return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
				}else {
					response.setError(this.service.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}  
			} catch (Exception e) {
				response.setError(1, "Error Interno",e.getMessage());
				String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
				LOGGER.error(error[1], e);
				response.setEstado(Estado.ERROR); 
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	
		    //para obtener la lista de aspectos a evaluar
		
		
				@GetMapping(path = "/obtenerListaAspectos", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> ObtenerListaAspectos(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest){
					ResponseObject response = new ResponseObject();
					try { 
						List<Pregunta> lista = this.service.obtenerListaAspectos(evaluacionAuditorRequest, pageRequest);	
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
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						response.setError(1, "Error Interno",e.getMessage());
						response.setEstado(Estado.ERROR);
						return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
		
	
				 //para obtener la lista de aspectos a evaluar Segun
				  
				
				@GetMapping(path = "/obtenerListaAspectosRes", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> ObtenerListaAspectosSegunIEva(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest){
					ResponseObject response = new ResponseObject();
					try { 
						List<Pregunta> lista = this.service.obtenerListaAspectosSegunIdEva(evaluacionAuditorRequest, pageRequest);	
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
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						
						response.setError(1, "Error Interno",e.getMessage());
						response.setEstado(Estado.ERROR);
						return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
		
				
				//para ver 
				
				@GetMapping(path = "/obtenerListaAspectosCount", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> buscarResPorCodEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest){
					ResponseObject response = new ResponseObject();
					try { 
						List<Pregunta> lista = this.service.buscarResPorCodEvaluacionAuditor(evaluacionAuditorRequest);	
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
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						
						response.setError(1, "Error Interno",e.getMessage());
						response.setEstado(Estado.ERROR);
						return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
		
				
				
				
	//ELIMINAR UNA EVALUACION AUDITOR  
				@DeleteMapping(path = "/eliminarEvaluacionAuditor/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> eliminarEvaluacionAuditor(@PathVariable("codigo") Long codigo){
					ResponseObject response = new ResponseObject();
					try { 
						
						Boolean eliminado = this.service.eliminarEvaluacionAuditor(codigo);
						
						System.out.println("eliminado-->"+eliminado);
						
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
	
	//*************************************Guardar Evaluacion Auditor***********************************************//
				
	
				@PostMapping(path = "/registrarEvaluacionAuditor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
				
				public ResponseEntity<ResponseObject> registroEvaluarAuditor(@RequestBody Map<String,Object> mapaParametros){
					
					ResponseObject response = new ResponseObject();
					try {
						EvaluacionAuditor evaluacionAuditor = null;
						if(mapaParametros.get("evaluacionAuditor") != null) {
							evaluacionAuditor = UJson.convertirJsonATipo(mapaParametros.get("evaluacionAuditor").toString(), EvaluacionAuditor.class);
						}
						List<Pregunta> listaPregunta = new ArrayList<>();
						if(mapaParametros.get("listaPreguntas") != null) {
							ObjectMapper objectMapper = new ObjectMapper();
							listaPregunta = objectMapper.readValue(mapaParametros.get("listaPreguntas").toString(), new TypeReference<List<Pregunta>>(){});
						}
						
						this.service.registrarEvaluacionAuditor(evaluacionAuditor, listaPregunta);
						if(this.service.getError() == null) {
							response.setResultado("Registro realizado");
							response.setPaginacion(this.service.getPaginacion());
							response.setEstado(Estado.OK);
							
							return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
						}else { 
							response.setError(this.service.getError());
							response.setEstado(Estado.ERROR);
							
							return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
						}

		
						
					}catch(Exception e) {
						response.setError(1, "Error Interno",e.getMessage());
						response.setEstado(Estado.ERROR);
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
					
					
					
				}	
				
	//*************************************	actualizar***************************************************************************//
				//*************************************Guardar Evaluacion Auditor***********************************************//
				
				
				@PostMapping(path = "/actualizarEvaluacionAuditor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
				
				public ResponseEntity<ResponseObject> actualizarEvaluarAuditor(@RequestBody Map<String,Object> mapaParametros){					
					ResponseObject response = new ResponseObject();
					try { 
						EvaluacionAuditor evaluacionAuditor = null;
						if(mapaParametros.get("evaluacionAuditor") != null) {
							evaluacionAuditor = UJson.convertirJsonATipo(mapaParametros.get("evaluacionAuditor").toString(), EvaluacionAuditor.class);
						}
						List<Pregunta> listaPregunta = new ArrayList<>();
						if(mapaParametros.get("listaPreguntas") != null) {
							ObjectMapper objectMapper = new ObjectMapper();
							listaPregunta = objectMapper.readValue(mapaParametros.get("listaPreguntas").toString(), new TypeReference<List<Pregunta>>(){});
						}
						 
						this.service.actualizarEvaluacionAuditor(evaluacionAuditor, listaPregunta);
						if(this.service.getError() == null) {
							response.setResultado("Registro realizado");
							response.setPaginacion(this.service.getPaginacion());
							response.setEstado(Estado.OK);							
							return new ResponseEntity<ResponseObject>(response,HttpStatus.OK); 
						}else { 
							response.setError(this.service.getError());
							response.setEstado(Estado.ERROR);							
							return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
						}
	
					}catch(Exception e) {
						response.setError(1, "Error Interno",e.getMessage());
						response.setEstado(Estado.ERROR);
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						
						
						return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}

				}	
				
				///para el imprimir

				@GetMapping(path = "/imprimirEvaluacion", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> imprimirEvaluacion(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest){
					ResponseObject response = new ResponseObject();
					try { 
						 this.service.imprimirPDF(evaluacionAuditorRequest, pageRequest);	
						if(this.service.getError() == null) {
							
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
