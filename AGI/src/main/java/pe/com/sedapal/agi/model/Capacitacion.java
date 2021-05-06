package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

public class Capacitacion {

	private Long idCapacitacion;
	private Long idCurso;
	private Long idInstructor;
	private Long indEvaluacion;
	private Long idExamen;
	private DatosAuditoria datosAuditoria;
	private Long disponibilidad;
	private String estadoCapacitacion;
	private Instructor instructorr;
	private Curso curso;
	private List<Sesion> listaSesiones;
	private String codigoCurso;
	private String nombreCapacitacion;
	private Date fechaInicio;
	private Date fechaFin;
	private String nombreInstructor;
	private String equipo;
	private Long cantParticipantes;
	private String estado;
	private String trimestre;
	private String anioPlanif;
	private String nombreCurso;
	private String hInicio;
	private String hFin;
	private Long idSesion;
	private List<Colaborador> lstColaborador;
	private List<CapacitacionDocInterno> listaDocumentos;
	private List<CapacitacionDocumentos> listaDocuCapa;
	private Long idDocumento;
	private List<PreguntaCurso> lstPreguntas;
	public Long getIdCapacitacion() {
		return idCapacitacion;
	}
	public void setIdCapacitacion(Long idCapacitacion) {
		this.idCapacitacion = idCapacitacion;
	}
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public Long getIdInstructor() {
		return idInstructor;
	}
	public void setIdInstructor(Long idInstructor) {
		this.idInstructor = idInstructor;
	}
	public Long getIndEvaluacion() {
		return indEvaluacion;
	}
	public void setIndEvaluacion(Long indEvaluacion) {
		this.indEvaluacion = indEvaluacion;
	}
	public Long getIdExamen() {
		return idExamen;
	}
	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public String getEstadoCapacitacion() {
		return estadoCapacitacion;
	}
	public void setEstadoCapacitacion(String estadoCapacitacion) {
		this.estadoCapacitacion = estadoCapacitacion;
	}
	public Instructor getInstructorr() {
		return instructorr;
	}
	public void setInstructor(Instructor instructorr) {
		this.instructorr = instructorr;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public List<Sesion> getListaSesiones() {
		return listaSesiones;
	}
	public void setListaSesiones(List<Sesion> listaSesiones) {
		this.listaSesiones = listaSesiones;
	}
	public String getCodigoCurso() {
		return codigoCurso;
	}
	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
	public String getNombreCapacitacion() {
		return nombreCapacitacion;
	}
	public void setNombreCapacitacion(String nombreCapacitacion) {
		this.nombreCapacitacion = nombreCapacitacion;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getNombreInstructor() {
		return nombreInstructor;
	}
	public void setNombreInstructor(String nombreInstructor) {
		this.nombreInstructor = nombreInstructor;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	public Long getCantParticipantes() {
		return cantParticipantes;
	}
	public void setCantParticipantes(Long cantParticipantes) {
		this.cantParticipantes = cantParticipantes;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTrimestre() {
		return trimestre;
	}
	public void setTrimestre(String trimestre) {
		this.trimestre = trimestre;
	}
	public String getAnioPlanif() {
		return anioPlanif;
	}
	public void setAnioPlanif(String anioPlanif) {
		this.anioPlanif = anioPlanif;
	}
	public String getNombreCurso() {
		return nombreCurso;
	}
	public void setNombreCurso(String nombreCurso) {
		this.nombreCurso = nombreCurso;
	}
	public String gethInicio() {
		return hInicio;
	}
	public void sethInicio(String hInicio) {
		this.hInicio = hInicio;
	}
	public String gethFin() {
		return hFin;
	}
	public void sethFin(String hFin) {
		this.hFin = hFin;
	}
	public Long getIdSesion() {
		return idSesion;
	}
	public void setIdSesion(Long idSesion) {
		this.idSesion = idSesion;
	}
	public void setInstructorr(Instructor instructorr) {
		this.instructorr = instructorr;
	}
	public List<Colaborador> getLstColaborador() {
		return lstColaborador;
	}
	public void setLstColaborador(List<Colaborador> lstColaborador) {
		this.lstColaborador = lstColaborador;
	}
	public List<CapacitacionDocInterno> getListaDocumentos() {
		return listaDocumentos;
	}
	public void setListaDocumentos(List<CapacitacionDocInterno> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public List<CapacitacionDocumentos> getListaDocuCapa() {
		return listaDocuCapa;
	}
	public void setListaDocuCapa(List<CapacitacionDocumentos> listaDocuCapa) {
		this.listaDocuCapa = listaDocuCapa;
	}
	public List<PreguntaCurso> getLstPreguntas() {
		return lstPreguntas;
	}
	public void setLstPreguntas(List<PreguntaCurso> lstPreguntas) {
		this.lstPreguntas = lstPreguntas;
	}
}
