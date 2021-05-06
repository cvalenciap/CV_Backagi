package pe.com.sedapal.agi.model.request_objects;

import pe.com.sedapal.agi.model.GenericParam;

public class FichaAudiRequest {
	private Integer ficha;
	private String nombre;
	private String apePaterno;
	private String apeMaterno;
	private GenericParam<String> rol;

	public Integer getFicha() {
		return ficha;
	}

	public void setFicha(Integer ficha) {
		this.ficha = ficha;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApePaterno() {
		return apePaterno;
	}

	public void setApePaterno(String apePaterno) {
		this.apePaterno = apePaterno;
	}

	public String getApeMaterno() {
		return apeMaterno;
	}

	public void setApeMaterno(String apeMaterno) {
		this.apeMaterno = apeMaterno;
	}

	public GenericParam<String> getRol() {
		return rol;
	}

	public void setRol(GenericParam<String> rol) {
		this.rol = rol;
	}

}
