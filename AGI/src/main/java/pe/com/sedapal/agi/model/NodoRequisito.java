package pe.com.sedapal.agi.model;

import java.util.List;

public class NodoRequisito {
	private Long id;
	private String nombre;
	private List<NodoRequisitos> children;
	
	//SE agregó
	private Long idNorma;
	private int estado;
	private String tipo;
	private String descripcionNorma;
	private DatosAuditoria datosAuditoria;
	
	
	
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
	public List<NodoRequisitos> getChildren() {
		return children;
	}
	public void setChildren(List<NodoRequisitos> children) {
		this.children = children;
	}
	public Long getIdNorma() {
		return idNorma;
	}
	public void setIdNorma(Long idNorma) {
		this.idNorma = idNorma;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescripcionNorma() {
		return descripcionNorma;
	}
	public void setDescripcionNorma(String descripcionNorma) {
		this.descripcionNorma = descripcionNorma;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}

	
	
	
}
