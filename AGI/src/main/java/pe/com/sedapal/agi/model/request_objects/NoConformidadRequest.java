package pe.com.sedapal.agi.model.request_objects;

import java.util.Date;

public class NoConformidadRequest {
	
	private String tipoFecha;
	private String fechaDesde;
//	private Date fechaDesde;
//	private Date fechaHasta;
	private String fechaHasta;
	private String codigo; 
	private String tipoConformidad; 
	private String norma; 
	private String alcance; 
	private String requisito;
	private String origenDeteccion;
	private String gerencia;
	private String equipo;
	public String getTipoFecha() {
		return tipoFecha;
	}
	public void setTipoFecha(String tipoFecha) {
		this.tipoFecha = tipoFecha;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getTipoConformidad() {
		return tipoConformidad;
	}
	public void setTipoConformidad(String tipoConformidad) {
		this.tipoConformidad = tipoConformidad;
	}
	public String getNorma() {
		return norma;
	}
	public void setNorma(String norma) {
		this.norma = norma;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public String getRequisito() {
		return requisito;
	}
	public void setRequisito(String requisito) {
		this.requisito = requisito;
	}
	public String getOrigenDeteccion() {
		return origenDeteccion;
	}
	public void setOrigenDeteccion(String origenDeteccion) {
		this.origenDeteccion = origenDeteccion;
	}
	public String getGerencia() {
		return gerencia;
	}
	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}	

}
