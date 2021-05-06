package pe.com.sedapal.agi.model;

import java.io.Serializable;

public class CorreoDestinatario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idDestinatario;	
	private String correo;
	private Long idEquipo;
	
	public Long getIdDestinatario() {
		return idDestinatario;
	}
	public void setIdDestinatario(Long idDestinatario) {
		this.idDestinatario = idDestinatario;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Long getIdEquipo() {
		return idEquipo;
	}
	public void setIdEquipo(Long idEquipo) {
		this.idEquipo = idEquipo;
	}
	
}