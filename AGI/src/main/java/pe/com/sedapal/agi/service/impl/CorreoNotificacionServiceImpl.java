package pe.com.sedapal.agi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.apache.log4j.Logger;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.Correo;
import pe.com.sedapal.agi.model.CorreoAdjunto;
import pe.com.sedapal.agi.model.CorreoResponse;
import pe.com.sedapal.agi.model.CorreoImagen;
import pe.com.sedapal.agi.util.UConstante;

public class CorreoNotificacionServiceImpl implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(CorreoNotificacionServiceImpl.class);	
	
	private static final long serialVersionUID = -8962671151431873396L;	
	private static CorreoNotificacionServiceImpl notificacionCorreo;
	
	private CorreoNotificacionServiceImpl(){		
	}
	
	public static CorreoNotificacionServiceImpl devuelveInstancia(){
		if(notificacionCorreo==null){
			notificacionCorreo = new CorreoNotificacionServiceImpl();
		}
		return notificacionCorreo;		
	}
	
	public CorreoResponse enviarCorreo(Properties paramProperties, Correo paramCorreoBean) throws Exception {
		
		CorreoResponse bCorreoResponse = null;
		try {
				bCorreoResponse = enviarCorreoFormatoHTML(paramProperties,paramCorreoBean);
		} catch (Exception exception) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(exception);
			LOGGER.error(error[1], exception);
		}		
		return bCorreoResponse;
	}
	
	public CorreoResponse enviarCorreoFormatoHTML(Properties configuracionServidor,final Correo correoBean) throws Exception {		
		CorreoResponse correoResponse = new CorreoResponse();
		InternetAddress correoRemitente = null;	
		//java.util.Date fecha = new Date();
		try {		
			Session sesion = Session.getInstance(configuracionServidor, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(correoBean.getCorreoCabecera().getCorreoRemitente(),
							correoBean.getCorreoCabecera().getClaveRemitente());
				}
			});
			Message mensaje = new MimeMessage(sesion);
			
			if (esNulo(correoBean.getCorreoCabecera().getNombreRemiente())) {			
				correoRemitente = new InternetAddress(correoBean.getCorreoCabecera().getCorreoRemitente());
			} else {			
				correoRemitente = new InternetAddress(correoBean.getCorreoCabecera().getCorreoRemitente(),
						correoBean.getCorreoCabecera().getNombreRemiente());
			}
			
			mensaje.setFrom(correoRemitente);
			establecerDestinatarios(correoBean, mensaje);

			mensaje.setSentDate(new Date());
			mensaje.setSubject(correoBean.getAsunto());
			establecerCuerpo(correoBean, mensaje);

			Transport.send(mensaje);
			correoResponse.setStatus(1);
			correoResponse.setMensaje("");
						
		} catch (Exception exception) {
			correoResponse.setStatus(0);
			correoResponse.setMensaje(exception.getMessage());
			throw new Exception(exception);			
		}		
		return correoResponse;
	}
	
	private static void establecerDestinatarios(Correo correoBean, Message mensaje) throws Exception {		
		try {
			Address[] correoPara = new Address[correoBean.getCorreoCabecera().getCorreoDestino().size()];
			Address[] correoCopia = null;
			Address[] correoCopiaOculta = null;
			int contReg = 0;
			
			for (String correo : correoBean.getCorreoCabecera().getCorreoDestino()) {
				if(correo!=null) {
					correoPara[contReg] = new InternetAddress(correo);
					contReg++;
				}
			}
			mensaje.setRecipients(Message.RecipientType.TO, correoPara);
			contReg = 0;
			
			if (!esNulo(correoBean.getCorreoCabecera().getCorreoCopia())) {			
				correoCopia = new Address[correoBean.getCorreoCabecera().getCorreoCopia().size()];
				for (String correo : correoBean.getCorreoCabecera().getCorreoCopia()) {
					if(correo!=null) {
						correoCopia[contReg] = new InternetAddress(correo);
						contReg++;
					}
				}
				mensaje.setRecipients(Message.RecipientType.CC, correoCopia);
				contReg = 0;
			
			}
			if (!esNulo(correoBean.getCorreoCabecera().getCorreoCopiaOculta())) {				
				correoCopiaOculta = new Address[correoBean.getCorreoCabecera().getCorreoCopiaOculta().size()];
				for (String correo : correoBean.getCorreoCabecera().getCorreoCopiaOculta()) {
					if(correo!=null) {
						correoCopiaOculta[contReg] = new InternetAddress(correo);
						contReg++;
					}
				}
				mensaje.setRecipients(Message.RecipientType.BCC, correoCopiaOculta);
				contReg= 0;				
			}
		} catch (Exception exception) {
			throw new Exception(exception);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	private static void establecerCuerpo(Correo correoBean, Message mensaje) throws Exception {
		try {
			MimeMultipart mimeMultipart = new MimeMultipart("related");
			DataSource dataSource = null;
			ByteArrayDataSource rawData = null;
			
			BodyPart cuerpoDelMensaje = new MimeBodyPart();
			cuerpoDelMensaje.setContent(correoBean.getMensaje(), "text/html; charset=utf-8");
			mimeMultipart.addBodyPart(cuerpoDelMensaje);
			
			if (!esNulo(correoBean.getImagenesAdjuntas())) {
				
				for (CorreoImagen imagenCorreoBean : correoBean.getImagenesAdjuntas()) {
					
					if(imagenCorreoBean.getUrlImagen()!=null && imagenCorreoBean.getNombreVariable()!=null &&
							 !String.valueOf(imagenCorreoBean.getUrlImagen()).isEmpty()){
						cuerpoDelMensaje = new MimeBodyPart();
						dataSource = new FileDataSource(imagenCorreoBean.getUrlImagen());
						cuerpoDelMensaje.setDataHandler(new DataHandler(dataSource));
						cuerpoDelMensaje.setHeader("Content-ID", "<" + imagenCorreoBean.getNombreVariable() + ">");
						mimeMultipart.addBodyPart(cuerpoDelMensaje);
					}
					
					if(imagenCorreoBean.getArchivoAdjunto()!=null &&  imagenCorreoBean.getArchivoAdjunto().length>0){
						cuerpoDelMensaje = new MimeBodyPart();						
						rawData = new ByteArrayDataSource(imagenCorreoBean.getArchivoAdjunto(),
								"application/octet-stream");
						cuerpoDelMensaje.setDataHandler(new DataHandler(rawData));
						cuerpoDelMensaje.setFileName(imagenCorreoBean.getNombreVariable());
						cuerpoDelMensaje.setHeader("Content-Type", "image/png");
						cuerpoDelMensaje.setHeader("Content-Transfer-Encoding", "base64");
						cuerpoDelMensaje.setHeader("Content-ID","<"+imagenCorreoBean.getNombreVariable()+">");
						mimeMultipart.addBodyPart(cuerpoDelMensaje);
					}
				}			
			}
			
			if (!esNulo(correoBean.getArchivosAdjuntos())) {
			
				for (CorreoAdjunto adjuntoCorreoBean : correoBean.getArchivosAdjuntos()) {
					cuerpoDelMensaje = new MimeBodyPart();

					if (adjuntoCorreoBean.getPathAdjunto() != null
							&& !String.valueOf(adjuntoCorreoBean.getPathAdjunto()).isEmpty()) {
						dataSource = new FileDataSource(adjuntoCorreoBean.getPathAdjunto());
						cuerpoDelMensaje.setDataHandler(new DataHandler(dataSource));
						if ((adjuntoCorreoBean.getNombreArchivo() != null)
								&& (!"".equals(adjuntoCorreoBean.getNombreArchivo()))) {
							cuerpoDelMensaje.setFileName(adjuntoCorreoBean.getNombreArchivo());
						} else {
							cuerpoDelMensaje.setFileName(adjuntoCorreoBean.getPathAdjunto());
						}
						mimeMultipart.addBodyPart(cuerpoDelMensaje);
					}

					if (adjuntoCorreoBean.getArchivoAdjunto() != null && adjuntoCorreoBean.getArchivoAdjunto().length>0) {
						cuerpoDelMensaje = new MimeBodyPart();
						rawData = new ByteArrayDataSource(adjuntoCorreoBean.getArchivoAdjunto(),
								"application/octet-stream");
						cuerpoDelMensaje.setDataHandler(new DataHandler(rawData));
						if ((adjuntoCorreoBean.getArchivoAdjunto() != null)
								&& (!"".equals(adjuntoCorreoBean.getArchivoAdjunto()))) {
							cuerpoDelMensaje.setFileName(adjuntoCorreoBean.getNombreArchivo());
						}
						mimeMultipart.addBodyPart(cuerpoDelMensaje);
					}
				}				
			}
			mensaje.setContent(mimeMultipart);
		} catch (Exception exception) {
			throw new Exception(exception);
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean esNulo(Object valor) {
		if (valor == null) {
			return true;
		}
		if (((valor instanceof String)) && (((String)valor).trim().length() == 0)) {
			return true;
		}
		if (((valor instanceof List)) && (((List)valor).size() <= 0)) {
			return true;
		}
		return false;
	}
	
}