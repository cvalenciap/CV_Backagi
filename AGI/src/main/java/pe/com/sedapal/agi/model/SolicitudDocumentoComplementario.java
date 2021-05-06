package pe.com.sedapal.agi.model;

public class SolicitudDocumentoComplementario {
	private Long idSolicitudCancelacion;
	private Long idDocumento;
	private String indicadorSolicitud;
	private Documento documento;
	private int tipo;
	private DatosAuditoria datosAuditoria;
	public Long getIdSolicitudCancelacion() {
		return idSolicitudCancelacion;
	}
	public void setIdSolicitudCancelacion(Long idSolicitudCancelacion) {
		this.idSolicitudCancelacion = idSolicitudCancelacion;
	}
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getIndicadorSolicitud() {
		return indicadorSolicitud;
	}
	public void setIndicadorSolicitud(String indicadorSolicitud) {
		this.indicadorSolicitud = indicadorSolicitud;
	}
	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	
	
	
}
