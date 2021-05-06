package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

import pe.com.sedapal.*;

public class Sesion {

	private Long idSesion;
	private Date fechaSesion;
	private Curso curso;
	private DatosAuditoria datosAuditoria;
	private Long duracion;
	private Long disponibilidad;
	private String horaInicio;
	private String horaFin;
	private Long idAula;
	private Long idSedeAula;
	private Long idCurso;
	private String nombreSesion;
	private int estadoRegistro;
	private Long itemColumna;
	private String descDisp;
	private List<Trabajador> listTrabajador;
	private Date fechaInicio;
	private Date fechaFin;
	private Date hInicio;
	private Date hFin;
	private Date fecSesion;
	
	public Long getIdSesion() {
		return idSesion;
	}	
	public void setIdSesion(Long idSesion) {
		this.idSesion = idSesion;
	}
	public Date getFechaSesion() {
		return fechaSesion;
	}
	public void setFechaSesion(Date fechaSesion) {
		this.fechaSesion = fechaSesion;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public Long getDuracion() {
		return duracion;
	}
	public void setDuracion(Long duracion) {
		this.duracion = duracion;
	}
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}
	public Long getIdAula() {
		return idAula;
	}
	public void setIdAula(Long idAula) {
		this.idAula = idAula;
	}
	public Long getIdSedeAula() {
		return idSedeAula;
	}
	public void setIdSedeAula(Long idSedeAula) {
		this.idSedeAula = idSedeAula;
	}
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public String getNombreSesion() {
		return nombreSesion;
	}
	public void setNombreSesion(String nombreSesion) {
		this.nombreSesion = nombreSesion;
	}
	public int getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(int estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	public Long getItemColumna() {
		return itemColumna;
	}
	public void setItemColumna(Long itemColumna) {
		this.itemColumna = itemColumna;
	}
	public String getDescDisp() {
		return descDisp;
	}
	public void setDescDisp(String descDisp) {
		this.descDisp = descDisp;
	}

	public List<Trabajador> getListTrabajador() {
		return listTrabajador;
	}
	public void setListTrabajador(List<Trabajador> listTrabajador) {
		this.listTrabajador = listTrabajador;
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
	public Date gethInicio() {
		return hInicio;
	}
	public void sethInicio(Date hInicio) {
		this.hInicio = hInicio;
	}
	public Date gethFin() {
		return hFin;
	}
	public void sethFin(Date hFin) {
		this.hFin = hFin;
	}
	public Date getFecSesion() {
		return fecSesion;
	}
	public void setFecSesion(Date fecSesion) {
		this.fecSesion = fecSesion;
	}
}
