package pe.com.sedapal.agi.model.request_objects;

import pe.com.sedapal.agi.model.DatosAuditoria;

public class CursoRequest {

//	private Number id;
//	private String nombre;
//	private Number idRol;
//	private Number obligatorio;
//	public Number getId() {
//		return id;
//	}
//	public void setId(Number id) {
//		this.id = id;
//	}
//	public String getNombre() {
//		return nombre;
//	}
//	public void setNombre(String nombre) {
//		this.nombre = nombre;
//	}
//	public Number getIdRol() {
//		return idRol;
//	}
//	public void setIdRol(Number idRol) {
//		this.idRol = idRol;
//	}
//	public Number getObligatorio() {
//		return obligatorio;
//	}
//	public void setObligatorio(Number obligatorio) {
//		this.obligatorio = obligatorio;
//	}
	
	private Long idCurso;
	private String nombreCurso;
	private Long duracion;
	private String indicadorSGI;
	private String disponibilidad;
	private String codigoCurso;
	private String tipoCurso;
	private Long sesiones;
	private DatosAuditoria datosAuditoria;
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public String getNombreCurso() {
		return nombreCurso;
	}
	public void setNombreCurso(String nombreCurso) {
		this.nombreCurso = nombreCurso;
	}
	public Long getDuracion() {
		return duracion;
	}
	public void setDuracion(Long duracion) {
		this.duracion = duracion;
	}
	public String getIndicadorSGI() {
		return indicadorSGI;
	}
	public void setIndicadorSGI(String indicadorSGI) {
		this.indicadorSGI = indicadorSGI;
	}
	public String getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(String disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public String getCodigoCurso() {
		return codigoCurso;
	}
	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
	public String getTipoCurso() {
		return tipoCurso;
	}
	public void setTipoCurso(String tipoCurso) {
		this.tipoCurso = tipoCurso;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public Long getSesiones() {
		return sesiones;
	}
	public void setSesiones(Long sesiones) {
		this.sesiones = sesiones;
	}
	
}
