package pe.com.sedapal.agi.model;

import java.util.List;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class NodoJerarquia {
	private Long id;
	private String nombre;
	private List<NodoJerarquia> children;
	private Long idTipoDocu;
	private String ruta;
	// YPM - INICIO
	private Long nivelJerarquia;
	private Long idPadre;
	private EstadoConstante	estado;
	private String abrJera;
	private Long indicadorDescargas;
	
	
	
	
	public Long getIndicadorDescargas() {
		return indicadorDescargas;
	}
	public void setIndicadorDescargas(Long indicadorDescargas) {
		this.indicadorDescargas = indicadorDescargas;
	}
	public String getAbrJera() {
		return abrJera;
	}
	public void setAbrJera(String abrJera) {
		this.abrJera = abrJera;
	}
	
	public Long getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(Long idPadre) {
		this.idPadre = idPadre;
	}
	public Long getNivelJerarquia() {
		return nivelJerarquia;
	}
	public void setNivelJerarquia(Long nivelJerarquia) {
		this.nivelJerarquia = nivelJerarquia;
	}
	// YPM - FIN
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
	public List<NodoJerarquia> getChildren() {
		return children;
	}
	public void setChildren(List<NodoJerarquia> children) {
		this.children = children;
	}
	public Long getIdTipoDocu() {
		return idTipoDocu;
	}
	public void setIdTipoDocu(Long idTipoDocu) {
		this.idTipoDocu = idTipoDocu;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public EstadoConstante getEstado() {
		return estado;
	}
	public void setEstado(EstadoConstante estado) {
		this.estado = estado;
	}
	
}