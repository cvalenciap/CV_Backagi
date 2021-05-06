package pe.com.sedapal.agi.model;

import java.io.Serializable;
import java.sql.Blob;

public class FormatoHTML implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;	
	private String descripcion;
	private String titulo;
	private Blob formatoHtml;
	private byte[] archivo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Blob getFormatoHtml() {
		return formatoHtml;
	}
	public void setFormatoHtml(Blob formatoHtml) {
		this.formatoHtml = formatoHtml;
	}
	public byte[] getArchivo() {
		return archivo;
	}
	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}
	
}