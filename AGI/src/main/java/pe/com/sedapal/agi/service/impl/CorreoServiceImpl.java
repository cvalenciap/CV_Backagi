package pe.com.sedapal.agi.service.impl;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.model.Correo;
import pe.com.sedapal.agi.model.CorreoResponse;
import pe.com.sedapal.agi.service.ICorreoService;
import pe.com.sedapal.agi.util.UCorreo;

@Service
public class CorreoServiceImpl implements ICorreoService {
	
	@Autowired
	Environment env;

	private static CorreoServiceImpl correoFacade = null;	
	UCorreo utilitarioCorreo = new UCorreo();	
	//ResourceBundle recursos = ResourceBundle.getBundle("pe.com.sedapal.agi.recursos.ServidorCorreo");
    String correo; //= recursos.getString("Correo");
    String puerto; //= recursos.getString("puerto");
    String autorizacion; //=recursos.getString("smtpAuth");
  
	private CorreoServiceImpl(){		
	}
	@Override
	public CorreoServiceImpl devuelveInstancia(){
		if(correoFacade==null)
			correoFacade = new CorreoServiceImpl();
		return correoFacade;	
	}
	@Override
	public CorreoResponse enviarCorreoHTML(Correo correoParametro) throws Exception{
		CorreoResponse correoResponse = null;
		String cuerpo = correoParametro.getMensaje();
		String asunto = correoParametro.getAsunto();
		CorreoNotificacionServiceImpl notificacion = CorreoNotificacionServiceImpl.devuelveInstancia();
		try {
			correoResponse = new CorreoResponse();
			if(correoParametro.getVariable().getNombreVariable()!=null || correoParametro.getVariable().getValorVariable()!=null) {
				for (int i = 0; i < correoParametro.getVariable().getNombreVariable().size(); i++) {
					cuerpo=cuerpo.replaceAll(correoParametro.getVariable().getNombreVariable().get(i),
							 correoParametro.getVariable().getValorVariable().get(i));
					asunto=asunto.replaceAll(correoParametro.getVariable().getNombreVariable().get(i),
							 correoParametro.getVariable().getValorVariable().get(i));
				}
			}
			correoParametro.setMensaje(cuerpo);
			correoParametro.setAsunto(asunto);
			correoResponse = notificacion.enviarCorreo(getPropiedades(),correoParametro);
		} catch (Exception exception) {
			throw exception;
		}
		return correoResponse;
	}
	
	private Properties getPropiedades() {
		
		correo = env.getProperty("app.config.servidor.correo.host");
		puerto = env.getProperty("app.config.servidor.correo.puerto");
		autorizacion = env.getProperty("app.config.servidor.correo.smtpAuth");		
		
		Properties propiedad = new Properties();
		propiedad.put("mail.smtp.auth", autorizacion);
		propiedad.put("mail.smtp.starttls.enable", "true");
		propiedad.put("mail.smtp.host", correo);	
		propiedad.put("mail.smtp.port", puerto);
		return propiedad;
	}
	
}