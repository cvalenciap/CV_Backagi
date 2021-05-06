package pe.com.sedapal.agi.model;

import java.util.Date;

public class Historico {
	private Long idHistorico;
	private Long idDocumento;
	private Long idRevision;
	private Long numeroRevision;
	private Long idEtapa;
	private String etapa;
	private Date fecha;
	private Long idColaborador;
	private String colaborador;
	private String comentario;
	private Long idEstadoFase;
	
	
	
	public Long getIdHistorico() {
		return idHistorico;
	}
	public void setIdHistorico(Long idHistorico) {
		this.idHistorico = idHistorico;
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
	public Long getNumeroRevision() {
		return numeroRevision;
	}
	public void setNumeroRevision(Long numeroRevision) {
		this.numeroRevision = numeroRevision;
	}
	public Long getIdEtapa() {
		return idEtapa;
	}
	public void setIdEtapa(Long idEtapa) {
		this.idEtapa = idEtapa;
	}
	public String getEtapa() {
		return etapa;
	}
	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
	public String getColaborador() {
		return colaborador;
	}
	public void setColaborador(String colaborador) {
		this.colaborador = colaborador;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}	
	public Long getIdEstadoFase() {
		return idEstadoFase;
	}
	public void setIdEstadoFase(Long idEstadoFase) {
		this.idEstadoFase = idEstadoFase;
	}

}
