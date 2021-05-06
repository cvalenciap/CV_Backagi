package pe.com.sedapal.agi.model;

import java.util.Date;

public class CapacitacionDocInterno {

	private Long idDocumento;
	private String codigoDocumento;
	private String nombreDocumento;
	private Long idRevision;;
	private Date fechaRevisionDocu;
	private Long idCapacitacion;
	private Long disponibilidad;
	
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public String getNombreDocumento() {
		return nombreDocumento;
	}
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	public Long getIdRevision() {
		return idRevision;
	}
	public void setIdRevision(Long idRevision) {
		this.idRevision = idRevision;
	}
	public Date getFechaRevisionDocu() {
		return fechaRevisionDocu;
	}
	public void setFechaRevisionDocu(Date fechaRevisionDocu) {
		this.fechaRevisionDocu = fechaRevisionDocu;
	}
	public Long getIdCapacitacion() {
		return idCapacitacion;
	}
	public void setIdCapacitacion(Long idCapacitacion) {
		this.idCapacitacion = idCapacitacion;
	}
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
}
