package pe.com.sedapal.agi.model;

import java.util.List;

public class Asistencia {
	private Long idCurso;
	private String codCurso;
	private String nomCurso;
	private String nomInstructor;
	private Long numParticipantes;
	private Long idCapacitacion;
	private List<Sesion> listSesion;
	private List<Trabajador> listTrabajador;
	private Long idSesion;
	private Long idColaborador;
	private String justificacion;
	private String asistencia;
	private String ruta;
	private String nombreArchivo;
	private String descEliminar;
	private Long idExamen;
	private Long nota;
	private Long indAsistencia;
	private Long dispAsistencia;
	private String estCapacitacion;
	private String archivoAntiguo;
	
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public String getCodCurso() {
		return codCurso;
	}
	public void setCodCurso(String codCurso) {
		this.codCurso = codCurso;
	}
	public String getNomCurso() {
		return nomCurso;
	}
	public void setNomCurso(String nomCurso) {
		this.nomCurso = nomCurso;
	}
	public String getNomInstructor() {
		return nomInstructor;
	}
	public void setNomInstructor(String nomInstructor) {
		this.nomInstructor = nomInstructor;
	}

	public Long getNumParticipantes() {
		return numParticipantes;
	}
	public void setNumParticipantes(Long numParticipantes) {
		this.numParticipantes = numParticipantes;
	}
	public Long getIdCapacitacion() {
		return idCapacitacion;
	}
	public void setIdCapacitacion(Long idCapacitacion) {
		this.idCapacitacion = idCapacitacion;
	}
	
	public List<Sesion> getListSesion() {
		return listSesion;
	}
	public void setListSesion(List<Sesion> listSesion) {
		this.listSesion = listSesion;
	}
	public List<Trabajador> getListTrabajador() {
		return listTrabajador;
	}
	public void setListTrabajador(List<Trabajador> listTrabajador) {
		this.listTrabajador = listTrabajador;
	}
	public Long getIdSesion() {
		return idSesion;
	}
	public void setIdSesion(Long idSesion) {
		this.idSesion = idSesion;
	}
	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
	public String getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}
	
	public String getAsistencia() {
		return asistencia;
	}
	public void setAsistencia(String asistencia) {
		this.asistencia = asistencia;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getDescEliminar() {
		return descEliminar;
	}
	public void setDescEliminar(String descEliminar) {
		this.descEliminar = descEliminar;
	}
	public Long getIdExamen() {
		return idExamen;
	}
	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}
	public Long getNota() {
		return nota;
	}
	public void setNota(Long nota) {
		this.nota = nota;
	}
	public Long getIndAsistencia() {
		return indAsistencia;
	}
	public void setIndAsistencia(Long indAsistencia) {
		this.indAsistencia = indAsistencia;
	}
	public Long getDispAsistencia() {
		return dispAsistencia;
	}
	public void setDispAsistencia(Long dispAsistencia) {
		this.dispAsistencia = dispAsistencia;
	}
	public String getEstCapacitacion() {
		return estCapacitacion;
	}
	public void setEstCapacitacion(String estCapacitacion) {
		this.estCapacitacion = estCapacitacion;
	}
	public String getArchivoAntiguo() {
		return archivoAntiguo;
	}
	public void setArchivoAntiguo(String archivoAntiguo) {
		this.archivoAntiguo = archivoAntiguo;
	}
	
}
