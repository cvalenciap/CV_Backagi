package pe.com.sedapal.agi.model;

import java.util.Date;

public class PlanAccion extends DatosAuditoria {
	
	private Long idNoConformidad;
	private Long idPlanAccion;
	private String descripcionAccionPropuesta;
	private String descripcionResponsable;
	private Date fechaCumplimiento;
	private String descripcionCritica;
	private String estadoRegistro;
	private String descripcionAccionEjecutada;
	private Date fechaEjecucion;
	private String descripcionVerificacion;
	private String valorAccion;
	private String item;
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public Long getIdNoConformidad() {
		return idNoConformidad;
	}
	public void setIdNoConformidad(Long idNoConformidad) {
		this.idNoConformidad = idNoConformidad;
	}
	
	public Long getIdPlanAccion() {
		return idPlanAccion;
	}
	public void setIdPlanAccion(Long idPlanAccion) {
		this.idPlanAccion = idPlanAccion;
	}
	
	public String getDescripcionAccionPropuesta() {
		return descripcionAccionPropuesta;
	}
	public void setDescripcionAccionPropuesta(String descripcionAccionPropuesta) {
		this.descripcionAccionPropuesta = descripcionAccionPropuesta;
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
	
	public String getDescripcionCritica() {
		return descripcionCritica;
	}
	public void setDescripcionCritica(String descripcionCritica) {
		this.descripcionCritica = descripcionCritica;
	}
	
	public String getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
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
	
	public String getValorAccion() {
		return valorAccion;
	}
	public void setValorAccion(String valorAccion) {
		this.valorAccion = valorAccion;
	}
}
