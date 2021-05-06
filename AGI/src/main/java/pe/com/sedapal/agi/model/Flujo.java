package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Flujo {
	
	Long			idFase;
	Long			idDocumento;
	Long			idRevision;
	Long			iteracion;
	Long			idEstadoFase;
	String			estadoFase;
	Long			usuarioFase;
	Date			fechaFase;
	Long			indicadorActividad;
	String			critica;
	Long			disponible;
	EstadoConstante	estadoDisponible;
	String 			indAprobacionSoli;
	String			indicadorFase;
	Long			indicadorBloqueo;
	Long			usuarioBloqueo;
	String			nombreBloqueo;
	Date			fechaBloqueo;
	String			comentario;
	String			indicadorAprobado;
	List<Colaborador> lista;
	String			descripcionFase;

	public Long getIdFase() {
		return idFase;
	}

	public void setIdFase(Long idFase) {
		this.idFase = idFase;
	}

	public Long getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Long getIdRevision() {
		return idRevision;
	}

	public void setIdRevision(Long idRevision) {
		this.idRevision = idRevision;
	}

	public Long getIteracion() {
		return iteracion;
	}

	public void setIteracion(Long iteracion) {
		this.iteracion = iteracion;
	}

	public Long getIdEstadoFase() {
		return idEstadoFase;
	}

	public void setIdEstadoFase(Long idEstadoFase) {
		this.idEstadoFase = idEstadoFase;
	}

	public String getEstadoFase() {
		return estadoFase;
	}

	public void setEstadoFase(String estadoFase) {
		this.estadoFase = estadoFase;
	}

	public Long getUsuarioFase() {
		return usuarioFase;
	}

	public void setUsuarioFase(Long usuarioFase) {
		this.usuarioFase = usuarioFase;
	}

	public Date getFechaFase() {
		return fechaFase;
	}

	public void setFechaFase(Date fechaFase) {
		this.fechaFase = fechaFase;
	}

	public Long getIndicadorActividad() {
		return indicadorActividad;
	}

	public void setIndicadorActividad(Long indicadorActividad) {
		this.indicadorActividad = indicadorActividad;
	}

	public String getCritica() {
		return critica;
	}

	public void setCritica(String critica) {
		this.critica = critica;
	}

	public Long getDisponible() {
		return disponible;
	}

	public void setDisponible(Long disponible) {
		this.disponible = disponible;
	}

	public EstadoConstante getEstadoDisponible() {
		return estadoDisponible;
	}

	public void setEstadoDisponible(EstadoConstante estadoDisponible) {
		this.estadoDisponible = estadoDisponible;
	}
	
	public String getIndAprobacionSoli() {
		return indAprobacionSoli;
	}
	public void setIndAprobacionSoli(String indAprobacionSoli) {
		this.indAprobacionSoli = indAprobacionSoli;
	}
	public String getIndicadorFase() {
		return indicadorFase;
	}
	public void setIndicadorFase(String indicadorFase) {
		this.indicadorFase = indicadorFase;
	}
	public Long getIndicadorBloqueo() {
		return indicadorBloqueo;
	}
	public void setIndicadorBloqueo(Long indicadorBloqueo) {
		this.indicadorBloqueo = indicadorBloqueo;
	}
	public Long getUsuarioBloqueo() {
		return usuarioBloqueo;
	}
	public void setUsuarioBloqueo(Long usuarioBloqueo) {
		this.usuarioBloqueo = usuarioBloqueo;
	}
	public String getNombreBloqueo() {
		return nombreBloqueo;
	}
	public void setNombreBloqueo(String nombreBloqueo) {
		this.nombreBloqueo = nombreBloqueo;
	}
	public Date getFechaBloqueo() {
		return fechaBloqueo;
	}
	public void setFechaBloqueo(Date fechaBloqueo) {
		this.fechaBloqueo = fechaBloqueo;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getIndicadorAprobado() {
		return indicadorAprobado;
	}
	public void setIndicadorAprobado(String indicadorAprobado) {
		this.indicadorAprobado = indicadorAprobado;
	}
	public List<Colaborador> getLista() {
		return lista;
	}
	public void setLista(List<Colaborador> lista) {
		this.lista = lista;
	}
	public String getDescripcionFase() {
		return descripcionFase;
	}
	public void setDescripcionFase(String descripcionFase) {
		this.descripcionFase = descripcionFase;
	}

	public Flujo() {
		super();
		this.disponible = Long.parseLong("1");
	}
	
	public Flujo(
		Long idFase,
		Long idDocumento,
		Long idRevision,
		Long numeroIteracion) {
		super();
		this.idFase				= idFase;
		this.idDocumento		= idDocumento;
		this.idRevision			= idRevision;
		this.iteracion			= numeroIteracion;
		this.estadoDisponible	= EstadoConstante.ACTIVO;
	}

}