package pe.com.sedapal.agi.model;

import java.util.Date;

public class SeguimientoDocumento {
	private Long idDocumento;
	private String codDocumento;
	private String desDocumento;
	private Constante motivoRevision;
	private Constante estadoDocumento;
	private Long idRevision;
	private Long numeroRevision;
	private Long numeroIteracion;
	private Date fechaRevision;
	private Date fechaRechazoRevision;
	private Date fechaAprobacionRevision;
	private Constante faseActual;
	private String textoFase;
	private String textoEstadoFase;
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}
	public String getDesDocumento() {
		return desDocumento;
	}
	public void setDesDocumento(String desDocumento) {
		this.desDocumento = desDocumento;
	}
	public Constante getMotivoRevision() {
		return motivoRevision;
	}
	public void setMotivoRevision(Constante motivoRevision) {
		this.motivoRevision = motivoRevision;
	}
	public Constante getEstadoDocumento() {
		return estadoDocumento;
	}
	public void setEstadoDocumento(Constante estadoDocumento) {
		this.estadoDocumento = estadoDocumento;
	}
	public Long getIdRevision() {
		return idRevision;
	}
	public void setIdRevision(Long idRevision) {
		this.idRevision = idRevision;
	}
	public Long getNumeroRevision() {
		return numeroRevision;
	}
	public void setNumeroRevision(Long numeroRevision) {
		this.numeroRevision = numeroRevision;
	}
	
	public Long getNumeroIteracion() {
		return numeroIteracion;
	}
	public void setNumeroIteracion(Long numeroIteracion) {
		this.numeroIteracion = numeroIteracion;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}
	public Date getFechaRechazoRevision() {
		return fechaRechazoRevision;
	}
	public void setFechaRechazoRevision(Date fechaRechazoRevision) {
		this.fechaRechazoRevision = fechaRechazoRevision;
	}
	public Date getFechaAprobacionRevision() {
		return fechaAprobacionRevision;
	}
	public void setFechaAprobacionRevision(Date fechaAprobacionRevision) {
		this.fechaAprobacionRevision = fechaAprobacionRevision;
	}
	public Constante getFaseActual() {
		return faseActual;
	}
	public void setFaseActual(Constante faseActual) {
		this.faseActual = faseActual;
	}
	public String getTextoFase() {
		return textoFase;
	}
	public void setTextoFase(String textoFase) {
		this.textoFase = textoFase;
	}
	public String getTextoEstadoFase() {
		return textoEstadoFase;
	}
	public void setTextoEstadoFase(String textoEstadoFase) {
		this.textoEstadoFase = textoEstadoFase;
	}
	
	
	

}
