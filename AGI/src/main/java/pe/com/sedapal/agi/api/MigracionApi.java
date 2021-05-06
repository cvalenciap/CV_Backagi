package pe.com.sedapal.agi.api;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;

import pe.com.sedapal.agi.service.IMigracionService;
import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Historico;

@RestController
@RequestMapping("/api/migracion")
public class MigracionApi {
	@Autowired	
	private IMigracionService service;
	
	//Lista Documento
	@GetMapping(path = "/documentomigracion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest){
		ResponseObject response = new ResponseObject();
		try {
			List<Documento> lista = this.service.obtenerDocumento(documentoRequest, pageRequest);	
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

	//Consulta Historica
		@GetMapping(path = "/consultaHistoricaMigracion", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarHistoryMigracion(DocumentoRequest documentoRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Documento> lista = this.service.obtenerHistoryMigracion(documentoRequest, pageRequest);	
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
	//Lista de Documento segun Jerarquia
	@GetMapping(path = "/documentomigracion/{idjerarquia}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, @PathVariable("idjerarquia") Long idjerarquia){
		ResponseObject response = new ResponseObject();
		try {
			List<Documento> lista = this.service.obtenerDocumentoj( documentoRequest,  pageRequest, idjerarquia);			
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
	
	//Detalle del Documento
	@GetMapping(path = "/documentoDetallemigracion/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDocumentoDetalle(@PathVariable("codigo") Long codigo){
		   ResponseObject response = new ResponseObject();
		try {
			Documento objeto = this.service.obtenerDocumentoDetalle(codigo);	
			if(this.service.getError() == null) {
				response.setResultado(objeto);
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

	//Lista Codigo Anterior
	@GetMapping(path = "/codigoAnterior", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarCodigoAnterior(DocumentoRequest documentoRequest, PageRequest pageRequest){
		ResponseObject response = new ResponseObject();
		try {
			List<Codigo> lista = this.service.obtenerCodigoAnterior(documentoRequest, pageRequest);	
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

//Registro Documento	
	@PostMapping(path = "/documentomigracion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> crearDocumento(@RequestBody Documento documento){
		ResponseObject response = new ResponseObject();
		try {
			Documento item  =  this.service.crearDocumento(documento);
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
	
	
	//Actualizar Documento
	@PostMapping(path = "/documentomigracion/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarDocumento(@RequestBody Documento documento, @PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Documento item = this.service.actualizarDocumento(documento, codigo);
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
	
	@DeleteMapping(path = "/documentomigracion/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarDocumento(@PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		
		try {
			Boolean eliminado = this.service.eliminarDocumento(codigo);
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
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*@GetMapping(path = "/documento/generar-codigo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> generarCodigoDocumento(
                                                                  @RequestParam(required = true) Long codigoGerencia,
                                                                  @RequestParam(required = true) Long codigoTipoDocumento) {

          ResponseObject responseObject = new ResponseObject();
          try {
                 String codigoDocumento = this.service.generarCodigoDocumento(codigoGerencia, codigoTipoDocumento);
                 if (this.service.getError() == null) {
                        responseObject.setEstado(Estado.OK);
                        responseObject.setResultado(codigoDocumento);
                        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
                 } else {
                        responseObject.setError(this.service.getError());
                        responseObject.setEstado(Estado.ERROR);
                        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
                 }

          } catch (Exception exception) {
                 responseObject.setError(this.service.getError());
                 responseObject.setEstado(Estado.ERROR);
                 return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
          }

    }*/

	@GetMapping(path = "/colaboradormigracion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarColaborador(ColaboradorRequest colaboradorRequest, PageRequest pageRequest) {
		ResponseObject response = new ResponseObject();
		try {
			List<Colaborador> lista = this.service.obtenerColaboradores(colaboradorRequest, pageRequest);
			if (this.service.getError() == null) {
				response.setResultado(lista);
				response.setPaginacion(this.service.getPaginacion());
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
	//Lista Historico
		@GetMapping(path = "/historicomigracion", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Historico> lista = this.service.obtenerHistorico(documentoRequest, pageRequest);	
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
		@PostMapping(path = "/enviarDocumentofile", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<ResponseObject> crearDocumentoMigracion(@RequestPart("file") Optional<MultipartFile> file,@RequestPart("fileDoc") Optional<MultipartFile> fileDoc ,@RequestPart("documento") Optional<Documento> documento) {			
	    	ResponseObject response = new ResponseObject();
	    	MultipartFile archivo = null;
	    	MultipartFile archivo1 = null;
	    	Documento objetoDocumento = null;
	    	
	    	if(file.isPresent()) {
	    		archivo = file.get();
	    	}
	    	if(fileDoc.isPresent()) {
	    		archivo1 = fileDoc.get();
	    	}
	    	if(documento.isPresent()) {
	    		objetoDocumento = documento.get();
	    	}	    	
			try {				
				boolean resultado = this.service.crearDocumentoFileMigracion(objetoDocumento,file,fileDoc);
				if (this.service.getError() == null) {
					response.setResultado(resultado);
					response.setPaginacion(this.service.getPaginacion());
					response.setEstado(Estado.OK);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
				} else {
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
		//
		
		
		//Lista Detalle Historico
				@GetMapping(path = "/historicoDetalleMigracion", produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<ResponseObject> consultarDetalleHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest){
					ResponseObject response = new ResponseObject();
					try {
						List<Historico> lista = this.service.obtenerDetalleHistorico(documentoRequest, pageRequest);	
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