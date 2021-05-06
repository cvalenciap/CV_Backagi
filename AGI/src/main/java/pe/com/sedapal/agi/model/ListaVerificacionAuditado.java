package pe.com.sedapal.agi.model;

public class ListaVerificacionAuditado {
	
	private Long idListVeriAuditado;
	private Trabajador trabajador;
	private Long idListVeri;
	private String estadoRegistro;
	private DatosAuditoria datosAuditoria;
	public Long getIdListVeriAuditado() {
		return idListVeriAuditado;
	}
	public void setIdListVeriAuditado(Long idListVeriAuditado) {
		this.idListVeriAuditado = idListVeriAuditado;
	}
	public Trabajador getTrabajador() {
		return trabajador;
	}
	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public Long getIdListVeri() {
		return idListVeri;
	}
	public void setIdListVeri(Long idListVeri) {
		this.idListVeri = idListVeri;
	}
	public String getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	
	
	

}
