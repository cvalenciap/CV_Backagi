package pe.com.sedapal.agi.model;

import java.util.Date;

public class Programa {
	private Long idPrograma;
	private String descripcion;
	private Date fechaPrograma;
	private String estadoPrograma;
	private DatosAuditoria datosAuditoria;
	private String usuarioCreacion;
	private String fechaCreacion;
	private String usuarioModificacion;
	private String fechaModificacion;
	private String procesoPrograma;
	
	
		
	public Long getIdPrograma() {
		return idPrograma;
	}
	public void setIdPrograma(Long idPrograma) {
		this.idPrograma = idPrograma;
	}
	

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEstadoPrograma() {
		return estadoPrograma;
	}
	public void setEstadoPrograma(String estadoPrograma) {
		this.estadoPrograma = estadoPrograma;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Date getFechaPrograma() {
		return fechaPrograma;
	}
	public void setFechaPrograma(Date fechaPrograma) {
		this.fechaPrograma = fechaPrograma;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public String getProcesoPrograma() {
		return procesoPrograma;
	}
	public void setProcesoPrograma(String procesoPrograma) {
		this.procesoPrograma = procesoPrograma;
	}
	
	
	
	
}
