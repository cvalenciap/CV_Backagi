package pe.com.sedapal.agi.model.request_objects;

import pe.com.sedapal.agi.model.DatosAuditoria;

public class PreguntaDetalleRequest {
	private Long codPregunta;
	private Long codDetalle;
	private Long idDetalle;
	private String descPregunta;
	private String datosAuditoria;
	private Long  valorRespuesta;
	private Long  disponibilidad;
	private String nomDisp;
	private String nomResp;
	
	public Long getCodPregunta() {
		return codPregunta;
	}
	public void setCodPregunta(Long codPregunta) {
		this.codPregunta = codPregunta;
	}
	public Long getCodDetalle() {
		return codDetalle;
	}
	public void setCodDetalle(Long codDetalle) {
		this.codDetalle = codDetalle;
	}
	public Long getIdDetalle() {
		return idDetalle;
	}
	public void setIdDetalle(Long idDetalle) {
		this.idDetalle = idDetalle;
	}
	public String getDescPregunta() {
		return descPregunta;
	}
	public void setDescPregunta(String descPregunta) {
		this.descPregunta = descPregunta;
	}
	public String getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(String datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public Long getValorRespuesta() {
		return valorRespuesta;
	}
	public void setValorRespuesta(Long valorRespuesta) {
		this.valorRespuesta = valorRespuesta;
	}
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public String getNomDisp() {
		return nomDisp;
	}
	public void setNomDisp(String nomDisp) {
		this.nomDisp = nomDisp;
	}
	public String getNomResp() {
		return nomResp;
	}
	public void setNomResp(String nomResp) {
		this.nomResp = nomResp;
	}
	
	
}
