package pe.com.sedapal.agi.model.request_objects;

import java.util.List;

import pe.com.sedapal.agi.model.Cursos;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.PreguntaDetalle;

public class PreguntaCursoRequest {
	
	private String codCurso;
	private Long codPregunta;
	private String pregunta;
	private String tipo;
	private Long puntaje;
	private Long disponibilidad;
	private String nomCurso;
	private Long valorRespuesta;
	private DatosAuditoria datosAuditoria;
	private List<PreguntaDetalle> listPregunta;
	private Long idCurso;
	private Long disponibilidadCurso;
	private Long idTipoCurso;
	private String nomTipo;
	
	public String getCodCurso() {
		return codCurso;
	}
	public void setCodCurso(String codCurso) {
		this.codCurso = codCurso;
	}
	public Long getCodPregunta() {
		return codPregunta;
	}
	public void setCodPregunta(Long codPregunta) {
		this.codPregunta = codPregunta;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Long getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(Long puntaje) {
		this.puntaje = puntaje;
	}
	public Long getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Long disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public String getNomCurso() {
		return nomCurso;
	}
	public void setNomCurso(String nomCurso) {
		this.nomCurso = nomCurso;
	}
	public Long getValorRespuesta() {
		return valorRespuesta;
	}
	public void setValorRespuesta(Long valorRespuesta) {
		this.valorRespuesta = valorRespuesta;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public List<PreguntaDetalle> getListPregunta() {
		return listPregunta;
	}
	public void setListPregunta(List<PreguntaDetalle> listPregunta) {
		this.listPregunta = listPregunta;
	}
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public Long getDisponibilidadCurso() {
		return disponibilidadCurso;
	}
	public void setDisponibilidadCurso(Long disponibilidadCurso) {
		this.disponibilidadCurso = disponibilidadCurso;
	}
	public Long getIdTipoCurso() {
		return idTipoCurso;
	}
	public void setIdTipoCurso(Long idTipoCurso) {
		this.idTipoCurso = idTipoCurso;
	}
	public String getNomTipo() {
		return nomTipo;
	}
	public void setNomTipo(String nomTipo) {
		this.nomTipo = nomTipo;
	}
	
}
