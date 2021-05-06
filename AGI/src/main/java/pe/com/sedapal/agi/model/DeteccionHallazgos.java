package pe.com.sedapal.agi.model;

import java.util.Date;

public class DeteccionHallazgos {
	
	
	private Integer item;
	private Long idDeteccionHallazgo;
	private String valorAmbito;
	private String ambito;//
	private Integer idorigenDeteccion;
	private String origenDeteccion;//	
	private Integer idTipoNoConformidad;//
	private String tipoNoConformidad;
	private Date  fechaDeteccion;
	private String descripHallazgo;
	private Trabajador detector;
	private String estado;
	private Requisito requisito;
	private DatosAuditoria datosAuditoria;
	private Long norma;
	private Long valorEntidadGerencia;
	private Long valorEntidadEquipo;
	private String valorTipoEntidad;

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}


	public Long getIdDeteccionHallazgo() {
		return idDeteccionHallazgo;
	}

	public void setIdDeteccionHallazgo(Long id) {
		this.idDeteccionHallazgo = id;
	}

	

	public String getValorAmbito() {
		return valorAmbito;
	}

	public void setValorAmbito(String valorAmbito) {
		this.valorAmbito = valorAmbito;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public Integer getIdorigenDeteccion() {
		return idorigenDeteccion;
	}

	public void setIdorigenDeteccion(Integer idorigenDeteccion) {
		this.idorigenDeteccion = idorigenDeteccion;
	}

	public String getOrigenDeteccion() {
		return origenDeteccion;
	}

	public void setOrigenDeteccion(String origenDeteccion) {
		this.origenDeteccion = origenDeteccion;
	}

	public Integer getIdTipoNoConformidad() {
		return idTipoNoConformidad;
	}

	public void setIdTipoNoConformidad(Integer idTipoNoConformidad) {
		this.idTipoNoConformidad = idTipoNoConformidad;
	}

	



	public String getTipoNoConformidad() {
		return tipoNoConformidad;
	}

	public void setTipoNoConformidad(String tipoNoConformidad) {
		this.tipoNoConformidad = tipoNoConformidad;
	}

	public Date getFechaDeteccion() {
		return fechaDeteccion;
	}

	public void setFechaDeteccion(Date fechaDeteccion) {
		this.fechaDeteccion = fechaDeteccion;
	}

	public String getDescripHallazgo() {
		return descripHallazgo;
	}

	public void setDescripHallazgo(String descripHallazgo) {
		this.descripHallazgo = descripHallazgo;
	}


	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Requisito getRequisito() {
		return requisito;
	}

	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}

	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}

	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}

	public Long getNorma() {
		return norma;
	}

	public void setNorma(Long norma) {
		this.norma = norma;
	}

	public Long getValorEntidadGerencia() {
		return valorEntidadGerencia;
	}

	public void setValorEntidadGerencia(Long valorEntidadGerencia) {
		this.valorEntidadGerencia = valorEntidadGerencia;
	}

	public Long getValorEntidadEquipo() {
		return valorEntidadEquipo;
	}

	public void setValorEntidadEquipo(Long valorEntidadEquipo) {
		this.valorEntidadEquipo = valorEntidadEquipo;
	}

	public String getValorTipoEntidad() {
		return valorTipoEntidad;
	}

	public void setValorTipoEntidad(String valorTipoEntidad) {
		this.valorTipoEntidad = valorTipoEntidad;
	}

	public Trabajador getDetector() {
		return detector;
	}

	public void setDetector(Trabajador detector) {
		this.detector = detector;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
