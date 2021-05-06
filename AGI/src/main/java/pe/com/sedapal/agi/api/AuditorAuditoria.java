package pe.com.sedapal.agi.api;

import pe.com.sedapal.agi.model.DatosAuditoria;

public class AuditorAuditoria {
	private Long idAuditorAuditoria;
	private Long idDetalleAuditoria;
	private Long idAuditor;
	private String cargoAuditor;
	private String nombreAuditor;
	private String responsable;
	
	private DatosAuditoria datosAuditoria;
	public Long getIdAuditorAuditoria() {
		return idAuditorAuditoria;
	}
	public void setIdAuditorAuditoria(Long idAuditorAuditoria) {
		this.idAuditorAuditoria = idAuditorAuditoria;
	}
	public Long getIdDetalleAuditoria() {
		return idDetalleAuditoria;
	}
	public void setIdDetalleAuditoria(Long idDetalleAuditoria) {
		this.idDetalleAuditoria = idDetalleAuditoria;
	}
	public Long getIdAuditor() {
		return idAuditor;
	}
	public void setIdAuditor(Long idAuditor) {
		this.idAuditor = idAuditor;
	}
	public String getCargoAuditor() {
		return cargoAuditor;
	}
	public void setCargoAuditor(String cargoAuditor) {
		this.cargoAuditor = cargoAuditor;
	}
	public String getNombreAuditor() {
		return nombreAuditor;
	}
	public void setNombreAuditor(String nombreAuditor) {
		this.nombreAuditor = nombreAuditor;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	
	
	
	
}
