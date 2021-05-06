package pe.com.sedapal.agi.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Ruta {
	
	Long					id;
	String					descripcion;
	EstadoConstante			estado;
	String					usuarioCreacion;
	Date					fechaCreacion;
	BigDecimal				disponible;
	List<RutaResponsable>	listaElaboracion;
	List<RutaResponsable>	listaConsenso;
	List<RutaResponsable>	listaAprobacion;
	List<RutaResponsable>	listaHomologacion;

	public Long getId() {
		return id;
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
	public EstadoConstante getEstado() {
		return estado;
	}
	public void setEstado(EstadoConstante estado) {
		this.estado = estado;
	}	
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public BigDecimal getDisponible() {
		return disponible;
	}
	public void setDisponible(BigDecimal disponible) {
		this.disponible = disponible;
	}
	public List<RutaResponsable> getListaElaboracion() {
		return listaElaboracion;
	}
	public void setListaElaboracion(List<RutaResponsable> listaElaboracion) {
		this.listaElaboracion = listaElaboracion;
	}
	public List<RutaResponsable> getListaConsenso() {
		return listaConsenso;
	}
	public void setListaConsenso(List<RutaResponsable> listaConsenso) {
		this.listaConsenso = listaConsenso;
	}
	public List<RutaResponsable> getListaAprobacion() {
		return listaAprobacion;
	}
	public void setListaAprobacion(List<RutaResponsable> listaAprobacion) {
		this.listaAprobacion = listaAprobacion;
	}
	public List<RutaResponsable> getListaHomologacion() {
		return listaHomologacion;
	}
	public void setListaHomologacion(List<RutaResponsable> listaHomologacion) {
		this.listaHomologacion = listaHomologacion;
	}
	
	public Ruta() {
		super();
	}
	
	public Ruta(Long id,
				String descripcion,
				EstadoConstante estado,
				String usuarioCreacion,
				Date fechaCreacion) {
		super();
		this.id				= id;
		this.descripcion	= descripcion;
		this.estado			= estado;
		this.usuarioCreacion= usuarioCreacion;
		this.fechaCreacion	= fechaCreacion;
	}
}
