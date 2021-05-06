package pe.com.sedapal.agi.model;

public class ListaVerificacionAuditor {
	private Long idListaVerificacion;
	private Long idAuditor;
	private Long idRolAuditor;
	private DatosAuditoria datosAuditoria;
	public Long getIdListaVerificacion() {
		return idListaVerificacion;
	}
	public void setIdListaVerificacion(Long idListaVerificacion) {
		this.idListaVerificacion = idListaVerificacion;
	}
	public Long getIdAuditor() {
		return idAuditor;
	}
	public void setIdAuditor(Long idAuditor) {
		this.idAuditor = idAuditor;
	}
	public Long getIdRolAuditor() {
		return idRolAuditor;
	}
	public void setIdRolAuditor(Long idRolAuditor) {
		this.idRolAuditor = idRolAuditor;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	
	
}
