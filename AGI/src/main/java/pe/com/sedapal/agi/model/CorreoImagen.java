package pe.com.sedapal.agi.model;

import java.io.Serializable;

public class CorreoImagen implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombreVariable;
	private String urlImagen;
	private byte[] archivoAdjunto;

	public CorreoImagen(){}
	
	public CorreoImagen(String nombreVariable, String urlImagen){
	    this.nombreVariable = nombreVariable;
	    this.urlImagen = urlImagen;
	}

	public String getNombreVariable() {
		return nombreVariable;
	}
	public void setNombreVariable(String nombreVariable) {
		this.nombreVariable = nombreVariable;
	}
	public String getUrlImagen() {
		return urlImagen;
	}
	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	public byte[] getArchivoAdjunto() {
		return archivoAdjunto;
	}
	public void setArchivoAdjunto(byte[] archivoAdjunto) {
		this.archivoAdjunto = archivoAdjunto;
	}
	
}