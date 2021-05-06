package pe.com.sedapal.agi.model.request_objects;

import java.util.Date;

import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Programa;

public class DeteccionHallazgosRequest {
	
	private String estado;
	private String nombreDetector;
	private String apPaternoDetector;
	private String apMaternoDetector;
	private String tipoNoConformidad;
	private String tipoOrigenDetec;
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombreDetector() {
		return nombreDetector;
	}
	public void setNombreDetector(String nombreDetector) {
		this.nombreDetector = nombreDetector;
	}
	public String getApPaternoDetector() {
		return apPaternoDetector;
	}
	public void setApPaternoDetector(String apPaternoDetector) {
		this.apPaternoDetector = apPaternoDetector;
	}
	public String getApMaternoDetector() {
		return apMaternoDetector;
	}
	public void setApMaternoDetector(String apMaternoDetector) {
		this.apMaternoDetector = apMaternoDetector;
	}
	public String getTipoNoConformidad() {
		return tipoNoConformidad;
	}
	public void setTipoNoConformidad(String tipoNoConformidad) {
		this.tipoNoConformidad = tipoNoConformidad;
	}
	public String getTipoOrigenDetec() {
		return tipoOrigenDetec;
	}
	public void setTipoOrigenDetec(String tipoOrigenDetec) {
		this.tipoOrigenDetec = tipoOrigenDetec;
	}
	
	
	
	
	
	
	

}
