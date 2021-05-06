package pe.com.sedapal.agi.model;

import java.util.Date;

public class Area {
	private Long idArea;
	private Long idCentro;
	private String descripcion;
	private String abreviatura;
	private Long anexo;
	private String tipoArea;
	private Long idAreaSuperior;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private String responsable;
	
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
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public Long getAnexo() {
		return anexo;
	}
	public void setAnexo(Long anexo) {
		this.anexo = anexo;
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
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
}
