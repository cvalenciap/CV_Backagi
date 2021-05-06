package pe.com.sedapal.agi.model;

import pe.com.sedapal.agi.model.DatosAuditoria;

public class CapacitacionColaborador {

	private Long fichaColaborador;
	private Long idCapacitacion;
	private Long disponibilidad;
	private DatosAuditoria datosAuditoria;
	public Long getFichaColaborador() {
		return fichaColaborador;
	}
	public void setFichaColaborador(Long fichaColaborador) {
		this.fichaColaborador = fichaColaborador;
	}
	public Long getIdCapacitacion() {
		return idCapacitacion;
	}
	public void setIdCapacitacion(Long idCapacitacion) {
		this.idCapacitacion = idCapacitacion;
	}
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
}
