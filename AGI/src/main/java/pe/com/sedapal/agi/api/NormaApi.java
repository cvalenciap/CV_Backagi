package pe.com.sedapal.agi.api;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.request_objects.AulaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.INormaService;

@RestController
@RequestMapping("/api/norma")
public class NormaApi {

	@Autowired
	private INormaService service;

	private static final Logger LOGGER = Logger.getLogger(NormaApi.class);

	@GetMapping(path = "/obtenerLista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> obtenerListaNormas(PageRequest pageRequest) throws GmdException {
		ResponseObject response = new ResponseObject();

		try {
			List<Norma> lista = service.obtenerListaNormas(pageRequest);
			if (this.service.getError() == null) {
				response.setResultado(lista);
				response.setEstado(Estado.OK);
				response.setPaginacion(this.service.getPaginacion());
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

	@PostMapping(path = "/actualizarNorma", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> actualizarNorma(@RequestBody Norma norma) {
		ResponseObject response = new ResponseObject();
		try {
			Integer resultado = this.service.actualizarNorma(norma);
			
			if (this.service.getError() == null) {
				response.setResultado(resultado);
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

	/*
	 * //Lista Normas
	 * 
	 * @GetMapping(path = "/obtenerListaGrilla", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<ResponseObject>
	 * consultarNormaGrilla(NormaRequest normaRequest, PageRequest pageRequest){
	 * ResponseObject response = new ResponseObject();
	 * System.out.println("consultarNormaGrilla");
	 * System.out.println(normaRequest.getIdNorma());
	 * System.out.println(normaRequest.getIdRequisito());
	 * System.out.println(normaRequest.getOrden());
	 * System.out.println(normaRequest.getNivel());
	 * System.out.println(normaRequest.getRequisito());
	 * System.out.println(normaRequest.getIdRequisitoPadre());
	 * System.out.println(normaRequest.getIdNormaReq());
	 * System.out.println(normaRequest.getTipo());
	 * System.out.println(normaRequest.getDescripcionNorma()); try { List<Norma>
	 * lista = this.service.obtenerListaNormasGrilla(normaRequest, pageRequest);
	 * if(this.service.getError() == null) { response.setResultado(lista);
	 * response.setPaginacion(this.service.getPaginacion());
	 * response.setEstado(Estado.OK); return new
	 * ResponseEntity<ResponseObject>(response,HttpStatus.OK); }else {
	 * response.setError(this.service.getError()); response.setEstado(Estado.ERROR);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } } catch (Exception e) {
	 * response.setError(1, "Error Interno",e.getMessage());
	 * response.setEstado(Estado.ERROR); return new
	 * ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR); }
	 * }
	 * 
	 * @GetMapping(path = "/obtenerNormaReq", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<ResponseObject>
	 * obtenerNormaRequisito(NormaRequest normaRequest){ ResponseObject response =
	 * new ResponseObject(); try { List<Requisito> lista =
	 * this.service.obtenerNormaRequisito(normaRequest); if(this.service.getError()
	 * == null) { response.setResultado(lista);
	 * response.setPaginacion(this.service.getPaginacion());
	 * response.setEstado(Estado.OK); return new
	 * ResponseEntity<ResponseObject>(response,HttpStatus.OK); }else {
	 * response.setError(this.service.getError()); response.setEstado(Estado.ERROR);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } } catch (Exception e) {
	 * response.setError(1, "Error Interno",e.getMessage());
	 * response.setEstado(Estado.ERROR); String[] error =
	 * MensajeExceptionUtil.obtenerMensajeError(e); LOGGER.error(error[1], e);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }
	 * 
	 * 
	 * @PostMapping(path = "/guardarNormaReq", consumes =
	 * MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<ResponseObject>
	 * guardarNormaRequisito(@RequestBody Map<String,Object> mapaParametros){
	 * ResponseObject response = new ResponseObject(); try { Norma norma = null;
	 * if(mapaParametros.get("norma") != null) { norma =
	 * UJson.convertirJsonATipo(mapaParametros.get("norma").toString(),
	 * Norma.class); } this.service.guardarNormaRequisito(norma);
	 * if(this.service.getError() == null) {
	 * response.setResultado("Registro realizado");
	 * response.setPaginacion(this.service.getPaginacion());
	 * response.setEstado(Estado.OK); return new
	 * ResponseEntity<ResponseObject>(response,HttpStatus.OK); }else {
	 * response.setError(this.service.getError()); response.setEstado(Estado.ERROR);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }catch(Exception e) {
	 * response.setError(1, "Error Interno",e.getMessage());
	 * response.setEstado(Estado.ERROR); String[] error =
	 * MensajeExceptionUtil.obtenerMensajeError(e); LOGGER.error(error[1], e);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * @PostMapping(path = "/guardarNormaReq/{id}", consumes =
	 * MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<ResponseObject>
	 * actualizarNormaRequisito( @PathVariable("id") Long id,
	 * 
	 * @RequestBody Map<String,Object> mapaParametros){
	 * 
	 * ResponseObject response = new ResponseObject(); try { Norma norma = null;
	 * if(mapaParametros.get("norma") != null) { norma =
	 * UJson.convertirJsonATipo(mapaParametros.get("norma").toString(),
	 * Norma.class); } this.service.actualizarNormaRequisito(id, norma);
	 * if(this.service.getError() == null) {
	 * response.setResultado("Registro realizado");
	 * response.setPaginacion(this.service.getPaginacion());
	 * response.setEstado(Estado.OK); return new
	 * ResponseEntity<ResponseObject>(response,HttpStatus.OK); }else {
	 * response.setError(this.service.getError()); response.setEstado(Estado.ERROR);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }catch(Exception e) {
	 * response.setError(1, "Error Interno",e.getMessage());
	 * response.setEstado(Estado.ERROR); String[] error =
	 * MensajeExceptionUtil.obtenerMensajeError(e); LOGGER.error(error[1], e);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	 * public ResponseEntity<ResponseObject> obtenerNormasPorId(@PathVariable("id")
	 * Long idNorma){ ResponseObject response = new ResponseObject();
	 * 
	 * 
	 * try { Norma norma = service.obtenerDatosNormaId(idNorma);
	 * if(this.service.getError() == null) { response.setResultado(norma);
	 * response.setEstado(Estado.OK); return new
	 * ResponseEntity<ResponseObject>(response,HttpStatus.OK); }else {
	 * response.setError(this.service.getError()); response.setEstado(Estado.ERROR);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } } catch (Exception e) {
	 * response.setError(1, "Error Interno",e.getMessage());
	 * response.setEstado(Estado.ERROR); return new
	 * ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR); }
	 * }
	 * 
	 * @DeleteMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	 * public ResponseEntity<ResponseObject> eliminarNorma(@PathVariable("id") Long
	 * id){ ResponseObject response = new ResponseObject(); try { Boolean eliminado
	 * = this.service.eliminarNorma(id); if (this.service.getError() == null) {
	 * response.setResultado(eliminado); response.setEstado(Estado.OK); return new
	 * ResponseEntity<ResponseObject>(response, HttpStatus.OK); } else {
	 * response.setError(this.service.getError()); response.setEstado(Estado.ERROR);
	 * return new ResponseEntity<ResponseObject>(response,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } } catch(Exception e) {
	 * response.setError(1, "Error Interno", e.getMessage());
	 * response.setEstado(Estado.ERROR); return new
	 * ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR); }
	 * }
	 */

}
