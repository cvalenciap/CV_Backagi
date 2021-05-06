package pe.com.sedapal.agi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.sedapal.agi.model.Correo;
import pe.com.sedapal.agi.model.CorreoResponse;
import pe.com.sedapal.agi.service.ICorreoService;

@RestController
@RequestMapping("/api")
public class CorreoApi {
	@Autowired
	private ICorreoService service;	
	
	@PostMapping(path = "/enviarCorreo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CorreoResponse enviarCorreoHTML(@RequestBody Correo correo) throws Exception {
		CorreoResponse correoResponse = null;		
		/*Correo correo = new Correo();
		CorreoCabecera cabecera = new CorreoCabecera();
		CorreoVariable variable = new CorreoVariable();
		String estilo = utilitario.obtenerEstilosCorreoHtml();
		List<String> nombreVariable = new ArrayList<>();
		nombreVariable.add(0,"estiloUsado");
		nombreVariable.add(1,"NombreJefe");
		nombreVariable.add(2,"NRevision");
		nombreVariable.add(3,"FechaDato");
		List<String> valorVariable = new ArrayList<>();
		valorVariable.add(0,estilo);
		valorVariable.add(1,"Juan Deza");
		valorVariable.add(2,"12");
		valorVariable.add(3,"06/02/2019");
		variable.setNombreVariable(nombreVariable);
		variable.setValorVariable(valorVariable);
		
		//Correo Destino
		List<String> correoDestino = new ArrayList<>();
		//for (int i = 0; i < 1000; i++) {
			correoDestino.add(0, "ralonzo@mail.canvia.com.pe");
			correoDestino.add(i+1, "bmartel@mail.canvia.com.pe");
			correoDestino.add(i+2, "jherrrera@mail.canvia.com.pe");
			correoDestino.add(i+3, "jvargas@mail.canvia.com.pe");
			correoDestino.add(i+4, "jflores@mail.canvia.com.pe");
			correoDestino.add(i+5, "bmartel2@mail.canvia.com.pe");
			correoDestino.add(i+6, "jherrrera2@mail.canvia.com.pe");
			correoDestino.add(i+7, "jvargas2@mail.canvia.com.pe");
			correoDestino.add(i+8, "jflores2@mail.canvia.com.pe");
			correoDestino.add(i+9, "bmartel3@mail.canvia.com.pe");
			correoDestino.add(i+10, "jherrrera3@mail.canvia.com.pe");
			correoDestino.add(i+11, "jvargas3@mail.canvia.com.pe");
			correoDestino.add(i+12, "jflores3@mail.canvia.com.pe");
			correoDestino.add(i+13, "bmartelC@mail.canvia.com.pe");
			correoDestino.add(i+14, "jherrreraC@mail.canvia.com.pe");
			correoDestino.add(i+15, "jvargasC@mail.canvia.com.pe");
			correoDestino.add(i+16, "jfloresC@mail.canvia.com.pe");
			correoDestino.add(i+17, "bmartel2C@mail.canvia.com.pe");
			correoDestino.add(i+18, "jherrrera2C@mail.canvia.com.pe");
			correoDestino.add(i+19, "jvargas2C@mail.canvia.com.pe");
			correoDestino.add(i+20, "jflores2C@mail.canvia.com.pe");
			correoDestino.add(i+21, "bmartel3C@mail.canvia.com.pe");
			correoDestino.add(i+22, "jherrrera3C@mail.canvia.com.pe");
			correoDestino.add(i+23, "jvargas3C@mail.canvia.com.pe");
			correoDestino.add(i+24, "jflores3C@mail.canvia.com.pe");
			i=i+24;
		//}
		cabecera.setCorreoDestino(correoDestino);
		
		//Correo Copia
		List<String> correoCopia = new ArrayList<>();
		correoCopia.add(0, "kmartel@mail.canvia.com.pe");
		cabecera.setCorreoCopia(correoCopia);
		
		//Correo Copia Oculta
		List<String> correoCopiaOculta = new ArrayList<>();
		correoCopiaOculta.add(0, "kmartel@mail.canvia.com.pe");
		cabecera.setCorreoCopiaOculta(correoCopiaOculta);
		
		//Remitente
		cabecera.setCorreoRemitente("ralonzo@mail.canvia.com.pe");
		cabecera.setNombreRemiente("ralonzo");
		cabecera.setClaveRemitente("ralonzo");
		
		correo.setVariable(variable);
		correo.setCorreoCabecera(cabecera);			
		correo.setAsunto("SEDAPAL-AGI-Solicitud de Revisión del Documento");
		
		Blob formato = dao.obtenerFormato("ENVIO DE NOTIFICACION DE REGISTRO DE SOLICITUD DE REVISION").getFormatoHtml();
		//dao.obtenerFormato().get(0).getFormatoHtml();
		String mensaje = new String(formato.getBytes(1l, (int) formato.length()));
		correo.setMensaje(mensaje);*/
		try {
			correoResponse = service.enviarCorreoHTML(correo);
		} catch (Exception exception) {
			throw exception;
		}
		return correoResponse;
	}
	
}