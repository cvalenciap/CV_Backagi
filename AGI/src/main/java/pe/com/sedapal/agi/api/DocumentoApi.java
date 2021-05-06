package pe.com.sedapal.agi.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wink.common.model.multipart.BufferedInMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IDocumentoService;
import pe.com.sedapal.agi.service.IFichaService;
import pe.com.sedapal.agi.service.IRevisionService;
import pe.com.sedapal.agi.service.ITareaService;
import pe.com.sedapal.agi.service.impl.RevisionServiceImpl;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IRevisionDAO;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.Revision;

@RestController
@RequestMapping("/api")
public class DocumentoApi {
	@Autowired
	private IDocumentoService service;
	@Autowired
	private IRevisionService revisionService;
	@Autowired
    ITareaService tareaService;
	@Autowired
	IFichaService fichaService;
	
	private static final Logger LOGGER = Logger.getLogger(DocumentoApi.class);	
	
	//Lista Documento
	@GetMapping(path = "/documento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest)throws Exception{
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
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Lista Documento Solicitud
		@GetMapping(path = "/documentoSolicitudRevision", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarDocumentoSolicitud(DocumentoRequest documentoRequest, PageRequest pageRequest){
			ResponseObject response = new ResponseObject();
			try {
				List<Documento> lista = this.service.obtenerDocumentoSolicitud(documentoRequest, pageRequest);	
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
	@GetMapping(path = "/documento/{idjerarquia}", produces = MediaType.APPLICATION_JSON_VALUE)
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
	@GetMapping(path = "/documentoDetalle/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@GetMapping(path = "/documentoDetalleHist/{codigo}/{idRevisionSeleccionado}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDocumentoDetalleHist(@PathVariable("codigo") Long codigo, @PathVariable("idRevisionSeleccionado") Long idRevision){
		   ResponseObject response = new ResponseObject();
		try {
			//Documento objeto = this.service.obtenerDocumentoDetalleHist(codigo, idRevision);
			//obtenerDocumentoHistorial
			Documento objeto = this.service.obtenerDocumentoHistorialRev(codigo, idRevision);	
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
	
	//Detalle del Documento Solicitud  ///cualquiera cosa aqui 
		@GetMapping(path = "/documentoSolicitudDetalle/{codigo}/{codigoSolicitud}/{codigoRevision}", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarDocumentoSolicitudDetalle(@PathVariable("codigo") Long codigo,@PathVariable("codigoSolicitud") Long codigoSolicitud,@PathVariable("codigoRevision") Long codigoRevision){
			   ResponseObject response = new ResponseObject();
			try {
				Documento objeto = this.service.obtenerDocumentoSolicitudDetalle(codigo,codigoSolicitud,codigoRevision);	
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
		//Lista Documento revision
		@GetMapping(path = "/documentoRevisionSolicitudDetalle/{codigo}/{codigoRevision}", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> consultarDocumentoRevision(@PathVariable("codigo") Long codigo,@PathVariable("codigoRevision") Long codigoRevision){
			   ResponseObject response = new ResponseObject();
			try {
				Documento objeto = this.service.obtenerDocumentoRevisionDetalle(codigo,codigoRevision);	
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
	//Generador de Excel
	@GetMapping(path = "/plazo.xls")
	public void escribirExcelPlazo(HttpServletResponse servletResponse, DocumentoRequest documentoRequest, PageRequest pageRequest){		
		try {
			String rutaPlazoXlsx = this.service.generarExcelPlazo(documentoRequest, pageRequest) ;
			File filedelete  = new File(rutaPlazoXlsx);
			if(!filedelete.exists()) {
				servletResponse.setStatus(HttpStatus.NOT_FOUND.value());
			}else {
				servletResponse.setStatus(HttpStatus.OK.value());
				servletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				servletResponse.addHeader("Content-Disposition", "attachment; filename=" + filedelete.getName());
				Files.copy(filedelete.toPath(), servletResponse.getOutputStream());
				servletResponse.getOutputStream().flush();
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			servletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
	//Generador de PDF
	@GetMapping(path = "/plazo.pdf")
	public ResponseEntity<byte[]> escribirPdfPlazo(HttpServletResponse servletResponse, DocumentoRequest documentoRequest,
			PageRequest pageRequest){
		ResponseObject response = new ResponseObject();
		try {
			byte[] bytes = this.service.generarPdfPlazo(documentoRequest, pageRequest) ;
			response.setResultado(bytes);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename="+"consulta"+".pdf");
			headers.add("Access-Control-Expose-Headers", "Content-Disposition");
			return ResponseEntity.ok().headers(headers).body(bytes);
		} catch (Exception e) {
			response.setError(1, "Error Interno",e.getMessage());
			response.setEstado(Estado.ERROR);			
			return null;
		}
	}
	//Documento Historico
	@GetMapping(path = "/documentoHistorial/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarDocumentoHistorial(@PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Documento objeto = this.service.obtenerDocumentoHistorial(codigo);	
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

//Registro Documento	
	@Transactional
	@PostMapping(path = "/documento", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@Transactional	
	@PostMapping(path = "/documento/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarDocumento(@RequestBody Documento documento, @PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			Documento item = this.service.actualizarDocumento(documento, codigo);
			//cguerra
			//item.getRevision().getNumero()
			//cguerra
			Documento objeto = this.service.obtenerIdGoogle(codigo,item.getRevision().getNumero(),item.getRevision().getId());
			boolean resultado = false;
			Revision revisionObtenido = new Revision();
			System.out.println("RESPUESTA SI ESTA PAROBADO");
			System.out.println(objeto.getV_id_googleDrive());
			System.out.println("FCHA DE APROBACION");
			System.out.println(objeto.getFechaApro());
			
			if(objeto.getFechaApro()!=null) {
				Revision objetoRevision = new Revision();
				objetoRevision.setId(item.getRevision().getId());
				objetoRevision.setFechaAprobacion(objeto.getFechaApro());
				objetoRevision.setIdDocGoogleDrive(objeto.getV_id_googleDrive());
				 revisionObtenido = this.revisionService.guardarDocumentoRevision(objetoRevision);
				//Registra las rutas del file serve a la tabla
				 resultado = this.revisionService.registrarDocumentosRevision(revisionObtenido);				
				if (!resultado) {
					response.setError(this.revisionService.getError());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			
			if (this.service.getError() == null) {
				if(resultado== true ) {
					response.setResultado(revisionObtenido);	
				}else {
					response.setResultado(item);	
				}				
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
	
		//Actualizar Documento Archivo Adjunto
			@Transactional
			@PutMapping(path = "/documento/{codigo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<ResponseObject> actualizarDocumentoArchivoAdjunto(@RequestPart("file") MultipartFile file,@RequestPart("documento") Documento documento, @PathVariable("codigo") Long codigo){
				ResponseObject response = new ResponseObject();
				System.out.println(file.getName());
				System.out.println(file.getOriginalFilename());
				
				try {
					//Actualiza documento					
					Map<String,Object> mapaRutas = this.service.manejarDocumentos(file);					
					documento.setRutaDocumento((String)mapaRutas.get("rutaArchivo"));
					//////
					Documento item = this.service.actualizarDocumento(documento, codigo);					
					//Fecha de Aprobacion					
					Documento objeto = this.service.obtenerIdGoogle(codigo,item.getRevision().getNumero(),item.getRevision().getId());
					boolean resultado = false;
					Revision revisionObtenido = new Revision();
					System.out.println("RESPUESTA SI ESTA PAROBADO");					
					System.out.println("FCHA DE APROBACION");					
					System.out.println(objeto.getFechaApro());															
					//Validamos si tiene adjunto en tabla revision
					String RutaAdjuntoRevision=objeto.getRutaDocumento();	
					
					if(RutaAdjuntoRevision==null) {
						//Ruta del adjunto en la tabla documento
						//Map<String,Object> mapaRutas = this.service.manejarDocumentos(file);					
						//documento.setRutaDocumento((String)mapaRutas.get("rutaArchivo"));						
						Revision objetoRevisionRuta = new Revision();
						objetoRevisionRuta.setId(item.getRevision().getId());
						objetoRevisionRuta.setRutaDocumentoOriginal(documento.getRutaDocumento());
						resultado = this.revisionService.registrarDocumentosRevision(objetoRevisionRuta);							
					}else {
						Revision objetoRevisionRuta = new Revision();
						objetoRevisionRuta.setId(item.getRevision().getId());
						objetoRevisionRuta.setRutaDocumentoOriginal(documento.getRutaDocumento());
						resultado = this.revisionService.registrarDocumentosRevision(objetoRevisionRuta);
					}
					
					
					//Validamos si es el ultimo que homologa 
					if(objeto.getFechaApro()!=null) {
						Revision objetoRevision = new Revision();
						objetoRevision.setId(item.getRevision().getId());
						objetoRevision.setFechaAprobacion(objeto.getFechaApro());
						//objetoRevision.setRutaDocumentoOriginal(objeto.getRutaDocumento());
						objetoRevision.setRutaDocumentoOriginal(documento.getRutaDocumento());
						//capturamos rutas para el file server
						 revisionObtenido = this.revisionService.guardarDocumentoRevisionAdjunto(objetoRevision);						
						 //Registra las rutas del file serve a la tabla						 
						 resultado = this.revisionService.registrarDocumentosRevision(revisionObtenido);						 						 
						if (!resultado) {
							response.setError(this.revisionService.getError());
							response.setEstado(Estado.ERROR);
							return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
						}
					}
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
					e.printStackTrace();
					response.setError(1, "Error Interno", e.getMessage());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}		
			}
						
			//actualizadocumentosinadjunto
			@Transactional
			@PostMapping(path = "/documentosinAdjunto/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<ResponseObject> actualizarDocumentoArchivo(@RequestBody Documento documento, @PathVariable("codigo") Long codigo){			
				ResponseObject response = new ResponseObject();			
				try {
					//Actualiza documento				
					Documento item = this.service.actualizarDocumento(documento, codigo);					
					//Fecha de Aprobacion					
					Documento objeto = this.service.obtenerIdGoogle(codigo,item.getRevision().getNumero(),item.getRevision().getId());
					boolean resultado = false;
					Revision revisionObtenido = new Revision();
					System.out.println("RESPUESTA SI ESTA PAROBADO");					
					System.out.println("FCHA DE APROBACION");					
					System.out.println(objeto.getFechaApro());															
					//Validamos si tiene adjunto en tabla revision
					String RutaAdjuntoRevision=objeto.getRutaDocumento();						
					if(RutaAdjuntoRevision==null) {
						//Ruta del adjunto en la tabla documento
						//Map<String,Object> mapaRutas = this.service.manejarDocumentos(file);					
						//documento.setRutaDocumento((String)mapaRutas.get("rutaArchivo"));						
						Revision objetoRevisionRuta = new Revision();
						objetoRevisionRuta.setId(item.getRevision().getId());
						objetoRevisionRuta.setRutaDocumentoOriginal(documento.getRutaDocumento());
						resultado = this.revisionService.registrarDocumentosRevision(objetoRevisionRuta);							
					}else {
						Revision objetoRevisionRuta = new Revision();
						objetoRevisionRuta.setId(item.getRevision().getId());
						objetoRevisionRuta.setRutaDocumentoOriginal(documento.getRutaDocumento());
						resultado = this.revisionService.registrarDocumentosRevision(objetoRevisionRuta);	
					}
					//Validamos si es el ultimo que homologa 
					if(objeto.getFechaApro()!=null) {
						Revision objetoRevision = new Revision();
						objetoRevision.setId(item.getRevision().getId());
						objetoRevision.setFechaAprobacion(objeto.getFechaApro());
						//objetoRevision.setRutaDocumentoOriginal(objeto.getRutaDocumento());
						objetoRevision.setRutaDocumentoOriginal(documento.getRutaDocumento());
						//capturamos rutas para el file server
						 revisionObtenido = this.revisionService.guardarDocumentoRevisionAdjunto(objetoRevision);						
						 //Registra las rutas del file serve a la tabla						 
						 resultado = this.revisionService.registrarDocumentosRevision(revisionObtenido);						 						 
						if (!resultado) {
							response.setError(this.revisionService.getError());
							response.setEstado(Estado.ERROR);
							return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
						}
					}
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
					e.printStackTrace();
					response.setError(1, "Error Interno", e.getMessage());
					response.setEstado(Estado.ERROR);
					return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}		
			}
			
	@DeleteMapping(path = "/documento/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@GetMapping(path = "/documento/generar-codigo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> generarCodigoDocumento(Documento documento) {
		Long codigoGerencia=null;
		Long codigoTipoDocumento=null;
		
		codigoGerencia=documento.getCodigoGerencia();
		codigoTipoDocumento=documento.getCodigoTipoDocumento();
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
    }
	
			
	
		//Actualizar Documento
		@PostMapping(path = "/documento/traslado/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> actualizarDocumentoTraslado(@RequestBody Documento documento, @PathVariable("codigo") Long codigo){
			ResponseObject response = new ResponseObject();
			try {
				Documento item = this.service.actualizarDocumentoTraslado(documento, codigo);
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
		
	//Bloquear Documento
	@PostMapping(path = "/bloquearDocumento", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> bloquearDocumento(@RequestBody Flujo bitacora){
		ResponseObject response = new ResponseObject();
		try {
			Flujo item = this.service.bloquearDocumento(bitacora);		
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
	
	//DesBloquear Documento
		@PostMapping(path = "/desBloquearDocumento", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ResponseObject> desBloquearDocumento(@RequestBody String bitacora){
			ResponseObject response = new ResponseObject();
			try {
				Flujo item = this.service.desBloquearDocumento();
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
	
	 //Generador de Excel Cancelacion
  	@GetMapping(path = "/cancelacion/plazo.xls")
  	public void escribirExcelPlazoCancelacion(HttpServletResponse servletResponse, TareaRequest tareaRequest, PageRequest pageRequest){		
  		try {
  			String rutaPlazoXlsx = this.tareaService.generarExcelPlazo(tareaRequest, pageRequest) ;
  			
  			File filedelete  = new File(rutaPlazoXlsx);  			
  			if(!filedelete.exists()) {
  				servletResponse.setStatus(HttpStatus.NOT_FOUND.value());
  			}else {
  				servletResponse.setStatus(HttpStatus.OK.value());
  				servletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
  				servletResponse.addHeader("Content-Disposition", "attachment; filename=" + filedelete.getName());
  				Files.copy(filedelete.toPath(), servletResponse.getOutputStream());
  				servletResponse.getOutputStream().flush();
  			}
  		} catch (Exception e) {
  			System.out.println("Error: " + e.getMessage());
  			servletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
  		}
  	}
  	
  	 //Generador de Excel Cancelacion
  	@GetMapping(path = "/fichaTecnica/plazo.xls")
  	public void escribirExcelPlazoCancelacion(HttpServletResponse servletResponse, FichaTecnicaRequest fichaTecnicaRequest){		
  		try {
  			String rutaPlazoXlsx = this.fichaService.generarExcelPlazo(fichaTecnicaRequest) ;
  			
  			File filedelete  = new File(rutaPlazoXlsx);  			
  			if(!filedelete.exists()) {
  				servletResponse.setStatus(HttpStatus.NOT_FOUND.value());
  			}else {
  				servletResponse.setStatus(HttpStatus.OK.value());
  				servletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
  				servletResponse.addHeader("Content-Disposition", "attachment; filename=" + filedelete.getName());
  				Files.copy(filedelete.toPath(), servletResponse.getOutputStream());
  				servletResponse.getOutputStream().flush();
  			}
  		} catch (Exception e) {
  			System.out.println("Error: " + e.getMessage());
  			servletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
  		}
  	}
	
	
}