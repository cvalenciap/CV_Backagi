package pe.com.sedapal.agi.security.model;

import java.io.Serializable;

public class Perfil implements Serializable {
	
	private int codPerfil;
	private String descripcion;
	
	public Perfil(int codPerfil, String descripcion) {
		super();
		this.codPerfil = codPerfil;
		this.descripcion = descripcion;
	}
	
	public int getCodPerfil() {
		return codPerfil;
	}
	public void setCodPerfil(int codPerfil) {
		this.codPerfil = codPerfil;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
