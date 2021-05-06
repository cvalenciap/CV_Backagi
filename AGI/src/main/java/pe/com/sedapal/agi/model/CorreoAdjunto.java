package pe.com.sedapal.agi.model;

import java.io.Serializable;

public class CorreoAdjunto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String pathAdjunto;
	private String nombreArchivo;
	private byte[] archivoAdjunto;

	public CorreoAdjunto(){		
	}
	
	public CorreoAdjunto(String pathAdjunto) {
		this.pathAdjunto = pathAdjunto;
	}
	
	public String getPathAdjunto() {
		return pathAdjunto;
	}
	public void setPathAdjunto(String pathAdjunto) {
		this.pathAdjunto = pathAdjunto;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public byte[] getArchivoAdjunto() {
		return archivoAdjunto;
	}
	public void setArchivoAdjunto(byte[] archivoAdjunto) {
		this.archivoAdjunto = archivoAdjunto;
	}

}