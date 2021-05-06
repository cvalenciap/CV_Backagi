package pe.com.sedapal.agi.model;

import java.math.BigDecimal;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class RutaResponsable {

	Long			idRuta;
	Long			idColaborador;
	Long			idFase;
	String			ruta;
	String			responsable;
	String			funcion;
	String			equipo;
	String			fase;
	BigDecimal		disponible;
	EstadoConstante	estado;

	public Long getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(Long idRuta) {
		this.idRuta = idRuta;
	}
	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
	public Long getIdFase() {
		return idFase;
	}
	public void setIdFase(Long idFase) {
		this.idFase = idFase;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getFuncion() {
		return funcion;
	}
	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	public String getFase() {
		return fase;
	}
	public void setFase(String fase) {
		this.fase = fase;
	}
	public BigDecimal getDisponible() {
		return disponible;
	}
	public void setDisponible(BigDecimal disponible) {
		this.disponible = disponible;
	}
	public EstadoConstante getEstado() {
		return estado;
	}
	public void setEstado(EstadoConstante estado) {
		this.estado = estado;
	}

	public RutaResponsable() {
		super();
	}
	
	public RutaResponsable(
		Long			idRuta,
		Long			idColaborador,
		Long			idFase,
		String			ruta,
		String			responsable,
		String			funcion,
		String			equipo,
		String			fase,
		EstadoConstante	estado
	) {
		super();
		this.idRuta			= idRuta;
		this.idColaborador	= idColaborador;
		this.idFase			= idFase;
		this.ruta			= ruta;
		this.responsable	= responsable;
		this.funcion		= funcion;
		this.equipo			= equipo;
		this.fase			= fase;
		this.estado			= estado;
	}

}