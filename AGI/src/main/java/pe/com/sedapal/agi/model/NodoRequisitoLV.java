package pe.com.sedapal.agi.model;

import java.util.List;

public class NodoRequisitoLV {
	private Long id;
	private String nombre;
	private List<NodoRequisitoLV> children;
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
	public List<NodoRequisitoLV> getChildren() {
		return children;
	}
	public void setChildren(List<NodoRequisitoLV> children) {
		this.children = children;
	}
	
	

}
