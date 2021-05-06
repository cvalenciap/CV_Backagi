package pe.com.sedapal.agi.model;

public class CapacitacionDocumentos {

	private Long idDocumento;
	private Long idCapacitacion;
	private String rutaDocumento;
	private String nombreDocumento;
	private Long disponibilidad;
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public Long getIdCapacitacion() {
		return idCapacitacion;
	}
	public void setIdCapacitacion(Long idCapacitacion) {
		this.idCapacitacion = idCapacitacion;
	}
	public String getRutaDocumento() {
		return rutaDocumento;
	}
	public void setRutaDocumento(String rutaDocumento) {
		this.rutaDocumento = rutaDocumento;
	}
	public String getNombreDocumento() {
		return nombreDocumento;
	}
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
}
