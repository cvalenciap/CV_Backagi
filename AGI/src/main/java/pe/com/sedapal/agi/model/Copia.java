package pe.com.sedapal.agi.model;

import java.math.BigDecimal;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Copia {
	
	Long			id;
	EstadoConstante	estado;
	BigDecimal		disponible;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public EstadoConstante getEstado() {
		return estado;
	}
	public void setEstado(EstadoConstante estado) {
		this.estado = estado;
	}
	public BigDecimal getDisponible() {
		return disponible;
	}
	public void setDisponible(BigDecimal disponible) {
		this.disponible = disponible;
	}

	public Copia() {
		super();
	}
	
	public Copia(
			Long		id,
			BigDecimal	disponible
	) {
		super();
		this.id			= id;
		this.disponible	= disponible;
	}

}