package pe.com.sedapal.agi.model.request_objects;

public class AuditorRequest {
	private String nroFicha;
	private String nombres;
	private String apePaterno;
	private String apeMaterno;
	private Long idRol;
	public String getNroFicha() {
		return nroFicha;
	}
	public void setNroFicha(String nroFicha) {
		this.nroFicha = nroFicha;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
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
	public Long getIdRol() {
		return idRol;
	}
	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}
	
	

}
