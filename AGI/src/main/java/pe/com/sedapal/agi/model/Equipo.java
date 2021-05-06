package pe.com.sedapal.agi.model;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Equipo {
	
	Long			id;
	String			descripcion;
	Colaborador		jefe;
	Long			indicadorNotificacion;
	Long			indicadorRevision;
	Long			indicadorResponsable;
	Long			idHistorial;
	Long			idRevision;
	Long			disponible;
	EstadoConstante	estado;
	String          nombre;
	
	//cguerra
	
	Long indicadorResp;
	//cguerra
	
	
	public Long getId() {
		return id;
	}
	public Long getIndicadorResp() {
		return indicadorResp;
	}
	public void setIndicadorResp(Long indicadorResp) {
		this.indicadorResp = indicadorResp;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Colaborador getJefe() {
		return jefe;
	}
	public void setJefe(Colaborador jefe) {
		this.jefe = jefe;
	}
	public Long getIndicadorNotificacion() {
		return indicadorNotificacion;
	}
	public void setIndicadorNotificacion(Long indicadorNotificacion) {
		this.indicadorNotificacion = indicadorNotificacion;
	}
	public Long getIndicadorRevision() {
		return indicadorRevision;
	}
	public void setIndicadorRevision(Long indicadorRevision) {
		this.indicadorRevision = indicadorRevision;
	}
	public Long getIndicadorResponsable() {
		return indicadorResponsable;
	}
	public void setIndicadorResponsable(Long indicadorResponsable) {
		this.indicadorResponsable = indicadorResponsable;
	}
	public Long getIdHistorial() {
		return idHistorial;
	}
	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}
	public Long getIdRevision() {
		return idRevision;
	}
	public void setIdRevision(Long idRevision) {
		this.idRevision = idRevision;
	}
	public Long getDisponible() {
		return disponible;
	}
	public void setDisponible(Long disponible) {
		this.disponible = disponible;
	}
	public EstadoConstante getEstado() {
		return estado;
	}
	public void setEstado(EstadoConstante estado) {
		this.estado = estado;
	}	

	public Equipo() {
		super();
	}
	
	public Equipo(Long id,
				String descripcion,
				EstadoConstante estado) {
		super();
		this.id				= id;
		this.descripcion	= descripcion;
		this.estado			= estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
