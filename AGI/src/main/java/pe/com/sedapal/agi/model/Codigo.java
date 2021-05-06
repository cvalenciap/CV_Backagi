package pe.com.sedapal.agi.model;

import java.math.BigDecimal;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Codigo {
	
	Long			id;
	String			codigo;
	String			motivo;
	EstadoConstante	estado;
	Long			idHistorial;
	BigDecimal		disponible;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public EstadoConstante getEstado() {
		return estado;
	}
	public void setEstado(EstadoConstante estado) {
		this.estado = estado;
	}
	public Long getIdHistorial() {
		return idHistorial;
	}
	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}
	public BigDecimal getDisponible() {
		return disponible;
	}
	public void setDisponible(BigDecimal disponible) {
		this.disponible = disponible;
	}

	public Codigo() {
		super();
	}
	
	public Codigo(
			Long		id,
			String		codigo,
			BigDecimal	disponible
	) {
		super();
		this.id			= id;
		this.codigo		= codigo;
		this.disponible	= disponible;
	}

}