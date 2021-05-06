package pe.com.sedapal.agi.model;

import java.util.List;

public class Curso {

	private Long idCurso;
	private String nombreCurso;
	private Long duracion;
	private String indicadorSGI;
	private Long disponibilidad;
	private String codigoCurso;
	private Long tipoCurso;
	private Long sesiones;
	private DatosAuditoria auditoria;
	private List<Sesion> listaSesiones;
	private int estadoRegistro;
	private List<Area> lstArea;
	private List<CursoArea> listaAreas;
	private Long idArea;
	private String descDisp;
	private String descTipo;
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
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public String getCodigoCurso() {
		return codigoCurso;
	}
	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
	public Long getTipoCurso() {
		return tipoCurso;
	}
	public void setTipoCurso(Long tipoCurso) {
		this.tipoCurso = tipoCurso;
	}
	public Long getSesiones() {
		return sesiones;
	}
	public void setSesiones(Long sesiones) {
		this.sesiones = sesiones;
	}
	public DatosAuditoria getAuditoria() {
		return auditoria;
	}
	public void setAuditoria(DatosAuditoria auditoria) {
		this.auditoria = auditoria;
	}
	public List<Sesion> getListaSesiones() {
		return listaSesiones;
	}
	public void setListaSesiones(List<Sesion> listaSesiones) {
		this.listaSesiones = listaSesiones;
	}
	public int getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(int estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	public List<Area> getLstArea() {
		return lstArea;
	}
	public void setLstArea(List<Area> lstArea) {
		this.lstArea = lstArea;
	}
	public List<CursoArea> getListaAreas() {
		return listaAreas;
	}
	public void setListaAreas(List<CursoArea> listaAreas) {
		this.listaAreas = listaAreas;
	}
	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public String getDescDisp() {
		return descDisp;
	}
	public void setDescDisp(String descDisp) {
		this.descDisp = descDisp;
	}
	public String getDescTipo() {
		return descTipo;
	}
	public void setDescTipo(String descTipo) {
		this.descTipo = descTipo;
	}
}
