package pe.com.sedapal.agi.model.request_objects;

public class InfoAuditorRequest {

	private String nroFicha;
	private String nombre;
	private String apePaterno;
	private String apeMaterno;

	public String getNroFicha() {
		return nroFicha;
	}

	public void setNroFicha(String nroFicha) {
		this.nroFicha = nroFicha;
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

}
