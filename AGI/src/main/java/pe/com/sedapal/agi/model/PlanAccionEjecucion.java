package pe.com.sedapal.agi.model;

import java.util.Date;

public class PlanAccionEjecucion {
	
	private Long idPlanAccion;
	private Long idEjecucion;
	private String numeral;
	private String descripcionAccionPropuesta;
	private String descripcionAccionEjecutada;
	private Date fechaEjecucion;
	private String descripcionVerificacion;
	private String descripcionResponsable;
	private Date fechaCumplimiento;
	private String estadoRegistro;
	private String item;
	
	public Long getIdPlanAccion() {
		return idPlanAccion;
	}
	public void setIdPlanAccion(Long idPlanAccion) {
		this.idPlanAccion = idPlanAccion;
	}
	
	public Long getIdEjecucion() {
		return idEjecucion;
	}
	public void setIdEjecucion(Long idEjecucion) {
		this.idEjecucion = idEjecucion;
	}
	
	public String getNumeral() {
		return numeral;
	}
	public void setNumeral(String numeral) {
		this.numeral = numeral;
	}
	
	public String getDescripcionAccionPropuesta() {
		return descripcionAccionPropuesta;
	}
	public void setDescripcionAccionPropuesta(String descripcionAccionPropuesta) {
		this.descripcionAccionPropuesta = descripcionAccionPropuesta;
	}
	
	public String getDescripcionAccionEjecutada() {
		return descripcionAccionEjecutada;
	}
	public void setDescripcionAccionEjecutada(String descripcionAccionEjecutada) {
		this.descripcionAccionEjecutada = descripcionAccionEjecutada;
	}
	
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	
	public String getDescripcionVerificacion() {
		return descripcionVerificacion;
	}
	public void setDescripcionVerificacion(String descripcionVerificacion) {
		this.descripcionVerificacion = descripcionVerificacion;
	}
	
	public String getDescripcionResponsable() {
		return descripcionResponsable;
	}
	public void setDescripcionResponsable(String descripcionResponsable) {
		this.descripcionResponsable = descripcionResponsable;
	}
	
	public Date getFechaCumplimiento() {
		return fechaCumplimiento;
	}
	public void setFechaCumplimiento(Date fechaCumplimiento) {
		this.fechaCumplimiento = fechaCumplimiento;
	}
	
	public String getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
}
