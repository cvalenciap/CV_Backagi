package pe.com.sedapal.agi.model;

import java.io.Serializable;
import java.util.List;

public class Correo implements Serializable {

	private static final long serialVersionUID = 1L;
	private CorreoCabecera correoCabecera;
	private List<CorreoAdjunto> archivosAdjuntos;
	private List<CorreoImagen> imagenesAdjuntas;
	private CorreoVariable variable;
	private String mensaje;
	private String asunto;

	public CorreoVariable getVariable() {
		return variable;
	}
	public void setVariable(CorreoVariable variable) {
		this.variable = variable;
	}
	public CorreoCabecera getCorreoCabecera() {
		return correoCabecera;
	}
	public void setCorreoCabecera(CorreoCabecera correoCabecera) {
		this.correoCabecera = correoCabecera;
	}
	public List<CorreoAdjunto> getArchivosAdjuntos() {
		return archivosAdjuntos;
	}
	public void setArchivosAdjuntos(List<CorreoAdjunto> archivosAdjuntos) {
		this.archivosAdjuntos = archivosAdjuntos;
	}
	public List<CorreoImagen> getImagenesAdjuntas() {
		return imagenesAdjuntas;
	}
	public void setImagenesAdjuntas(List<CorreoImagen> imagenesAdjuntas) {
		this.imagenesAdjuntas = imagenesAdjuntas;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

}