package pe.com.sedapal.agi.model;

import java.io.Serializable;
import java.util.List;

public class CorreoCabecera implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<String> correoDestino;	
	private List<String> correoCopia;
	private List<String> correoCopiaOculta;
	private String correoRemitente;
	private String nombreRemiente;
	private String claveRemitente;
	
	public List<String> getCorreoDestino() {
		return correoDestino;
	}
	public void setCorreoDestino(List<String> correoDestino) {
		this.correoDestino = correoDestino;
	}
	public List<String> getCorreoCopia() {
		return correoCopia;
	}
	public void setCorreoCopia(List<String> correoCopia) {
		this.correoCopia = correoCopia;
	}
	public List<String> getCorreoCopiaOculta() {
		return correoCopiaOculta;
	}
	public void setCorreoCopiaOculta(List<String> correoCopiaOculta) {
		this.correoCopiaOculta = correoCopiaOculta;
	}
	public String getCorreoRemitente() {
		return correoRemitente;
	}
	public void setCorreoRemitente(String correoRemitente) {
		this.correoRemitente = correoRemitente;
	}
	public String getNombreRemiente() {
		return nombreRemiente;
	}
	public void setNombreRemiente(String nombreRemiente) {
		this.nombreRemiente = nombreRemiente;
	}
	public String getClaveRemitente() {
		return claveRemitente;
	}
	public void setClaveRemitente(String claveRemitente) {
		this.claveRemitente = claveRemitente;
	}

}