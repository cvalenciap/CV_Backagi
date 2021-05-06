package pe.com.sedapal.agi.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.service.ICargoSigService;
import pe.com.sedapal.agi.util.RespuestaBean;

@RestController
@RequestMapping("/api/auditoria")
public class CargoSigApi {

	@Autowired
	private ICargoSigService service;

	@PostMapping(path = "/consultaCargoSig", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RespuestaBean> consultaBusqueda(@RequestBody Map<String, Object> requestParm)
			throws GmdException {
		System.out.println("entra");
		RespuestaBean resultadoCons = this.service.consultaBusqueda(requestParm);
		return new ResponseEntity<RespuestaBean>(resultadoCons, HttpStatus.OK);
	}

	// Guarda
	@PostMapping(path = "/guardaCargoSig", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RespuestaBean> saveCargoSig(@RequestBody Map<String, Object> requestParm)
			throws GmdException {
		System.out.println("entra");
		RespuestaBean resultadoCons = this.service.registraCargoSig(requestParm);
		return new ResponseEntity<RespuestaBean>(resultadoCons, HttpStatus.OK);
	}

	// Modifica
	@PostMapping(path = "/guardaModifCargoSig", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RespuestaBean> saveModifCargoSig(@RequestBody Map<String, Object> requestParm)
			throws GmdException {
		System.out.println("entra");
		RespuestaBean resultadoCons = this.service.ModifCargoSig(requestParm);
		return new ResponseEntity<RespuestaBean>(resultadoCons, HttpStatus.OK);
	}

}
