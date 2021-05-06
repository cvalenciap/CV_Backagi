package pe.com.sedapal.agi.api;

import pe.com.sedapal.agi.model.DatosAuditoria;

public class RequisitoAuditoriaDetalle {
	public Long idDetalleAuditoria;
	public Long id;
	public String nombre;
	public RequisitoAuditoriaDetalle children;
	public String uuid;
	public DatosAuditoria datosAuditoria;
	
	
	public Long getIdDetalleAuditoria() {
		return idDetalleAuditoria;
	}
	public void setIdDetalleAuditoria(Long idDetalleAuditoria) {
		this.idDetalleAuditoria = idDetalleAuditoria;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public RequisitoAuditoriaDetalle getChildren() {
		return children;
	}
	public void setChildren(RequisitoAuditoriaDetalle children) {
		this.children = children;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	
	
	
}
