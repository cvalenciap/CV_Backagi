package pe.com.sedapal.agi.model;

import java.util.Date;

public class NoConformidadSeguimiento extends DatosAuditoria {
	private String ordenEtapa;	
	private Long idNoConformidad;
	private Long idNoConformidadSeguimiento;
	private String etapaSeguimiento;
	private Date fechaseguimiento;
	private String estadoSeguimiento;
	private String etapa;
	private Integer NumeroFila;
	public String getEtapa() {
		return etapa;
	}
	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}
	public String getOrdenEtapa() {
		return ordenEtapa;
	}
	public void setOrdenEtapa(String ordenEtapa) {
		this.ordenEtapa = ordenEtapa;
	}
	public Long getIdNoConformidad() {
		return idNoConformidad;
	}
	public void setIdNoConformidad(Long idNoConformidad) {
		this.idNoConformidad = idNoConformidad;
	}
	public Long getIdNoConformidadSeguimiento() {
		return idNoConformidadSeguimiento;
	}
	public void setIdNoConformidadSeguimiento(Long idNoConformidadSeguimiento) {
		this.idNoConformidadSeguimiento = idNoConformidadSeguimiento;
	}
	public String getEtapaSeguimiento() {
		return etapaSeguimiento;
	}
	public void setEtapaSeguimiento(String etapaSeguimiento) {
		this.etapaSeguimiento = etapaSeguimiento;
	}
	public Date getFechaseguimiento() {
		return fechaseguimiento;
	}
	public void setFechaseguimiento(Date fechaseguimiento) {
		this.fechaseguimiento = fechaseguimiento;
	}
	public String getEstadoSeguimiento() {
		return estadoSeguimiento;
	}
	public void setEstadoSeguimiento(String estadoSeguimiento) {
		this.estadoSeguimiento = estadoSeguimiento;
	}
	public Integer getNumeroFila() {
		return NumeroFila;
	}
	public void setNumeroFila(Integer numeroFila) {
		NumeroFila = numeroFila;
	}	

}
