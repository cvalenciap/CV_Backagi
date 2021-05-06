package pe.com.sedapal.agi.api;
import java.util.List;
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
import pe.com.sedapal.agi.service.IAuditoriaService;
import pe.com.sedapal.agi.util.RespuestaBean;
import pe.com.sedapal.agi.util.UConstante;

@RestController
@RequestMapping("/api")
public class AuditoriaApi {
	
	@Autowired
	private IAuditoriaService service;
	
	//Lista el resultado de la busqueda
		
	
}
