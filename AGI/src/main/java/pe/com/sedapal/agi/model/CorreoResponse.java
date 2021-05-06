package pe.com.sedapal.agi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CorreoResponse implements Serializable {
	
	private int status;	
	private String mensaje;	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}	

}