package pe.com.sedapal.agi.model.request_objects;

public class AreaRequest {
	private Long idArea;
	private Long idCentro;
	private String descripcion;
	private String abreviatura;
	private String tipoArea;
	private Long idAreaSuperior;
	
	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Long getIdCentro() {
		return idCentro;
	}
	public void setIdCentro(Long idCentro) {
		this.idCentro = idCentro;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getAbreviatura(){
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public String getTipoArea() {
		return tipoArea;
	}
	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}
	public Long getIdAreaSuperior() {
		return idAreaSuperior;
	}
	public void setIdAreaSuperior(Long idAreaSuperior) {
		this.idAreaSuperior = idAreaSuperior;
	}	
	
}
