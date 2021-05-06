package pe.com.sedapal.agi.model;

import java.util.Date;

public class HallazgoRequisito {
	private Long idHallazgo;
	private String tipoHallazgo;
	private String descripcionHallazgo;
	private Date fechaHallazgo;
	private String indicadorHallazgo;
	private Long idLVRequisito;
	private String usuarioHallazgo;
	private DatosAuditoria datosAuditoria;
	
	public Long getIdHallazgo() {
		return idHallazgo;
	}
	public void setIdHallazgo(Long idHallazgo) {
		this.idHallazgo = idHallazgo;
	}
	public String getTipoHallazgo() {
		return tipoHallazgo;
	}
	public void setTipoHallazgo(String tipoHallazgo) {
		this.tipoHallazgo = tipoHallazgo;
	}
	public String getDescripcionHallazgo() {
		return descripcionHallazgo;
	}
	public void setDescripcionHallazgo(String descripcionHallazgo) {
		this.descripcionHallazgo = descripcionHallazgo;
	}
	public Date getFechaHallazgo() {
		return fechaHallazgo;
	}
	public void setFechaHallazgo(Date fechaHallazgo) {
		this.fechaHallazgo = fechaHallazgo;
	}
	public String getIndicadorHallazgo() {
		return indicadorHallazgo;
	}
	public void setIndicadorHallazgo(String indicadorHallazgo) {
		this.indicadorHallazgo = indicadorHallazgo;
	}
	public Long getIdLVRequisito() {
		return idLVRequisito;
	}
	public void setIdLVRequisito(Long idLVRequisito) {
		this.idLVRequisito = idLVRequisito;
	}
	public String getUsuarioHallazgo() {
		return usuarioHallazgo;
	}
	public void setUsuarioHallazgo(String usuarioHallazgo) {
		this.usuarioHallazgo = usuarioHallazgo;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	
	
	

}
