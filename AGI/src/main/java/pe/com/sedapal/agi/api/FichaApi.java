package pe.com.sedapal.agi.api;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IFichaService;
import pe.com.sedapal.agi.model.FichaTecnica;

@RestController
@RequestMapping("/api")
public class FichaApi {
	@Autowired
	private IFichaService service;
	
	//Lista Ficha
	@GetMapping(path = "/ficha", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> consultarFicha(FichaTecnicaRequest fichaRequest){
		ResponseObject response = new ResponseObject();
		try {
			FichaTecnica objeto = this.service.obtenerFicha(fichaRequest);	
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
	
	@PostMapping(path = "/ficha", produces = MediaType.APPLICATION_JSON_VALUE)	 	
	public ResponseEntity<ResponseObject> crearFicha(@RequestPart("file") Optional<MultipartFile> file ,@RequestPart("fichaTecnica") Optional<FichaTecnica> fichaTecnica){
		ResponseObject response = new ResponseObject();
		MultipartFile archivo = null;
		FichaTecnica ficha = null;
    	if(file.isPresent()) {
    		archivo = file.get();
    	}
    	if(fichaTecnica.isPresent()) {
    		ficha = fichaTecnica.get();
    	}		
		try {
			FichaTecnica item  =  this.service.crearFicha(ficha,file);
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
	
	@PostMapping(path = "/ficha/{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarFicha(@RequestBody FichaTecnica ficha, @PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		try {
			FichaTecnica item = this.service.actualizarFicha(ficha, codigo);
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

	@DeleteMapping(path = "/ficha/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> eliminarFicha(@PathVariable("codigo") Long codigo){
		ResponseObject response = new ResponseObject();
		
		try {
			Boolean eliminado = this.service.eliminarFicha(codigo);
			if (this.service.getError()==null) {
				if (eliminado) {
					response.setResultado("registro eliminado");
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
	
}