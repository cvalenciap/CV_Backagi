package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

public class Dashboard {
	private List<String> listaTexto;
	private List<String> listaDato;
	private Long idProgramacion;
	private Constante trimestre;
	private Equipo equipo;
	private Date fechaProgramacion;
	private Documento documento;
	
	public List<String> getListaTexto() {
		return listaTexto;
	}
	public void setListaTexto(List<String> listaTexto) {
		this.listaTexto = listaTexto;
	}
	public List<String> getListaDato() {
		return listaDato;
	}
	public void setListaDato(List<String> listaDato) {
		this.listaDato = listaDato;
	}
	public Long getIdProgramacion() {
		return idProgramacion;
	}
	public void setIdProgramacion(Long idProgramacion) {
		this.idProgramacion = idProgramacion;
	}
	public Constante getTrimestre() {
		return trimestre;
	}
	public void setTrimestre(Constante trimestre) {
		this.trimestre = trimestre;
	}
	public Equipo getEquipo() {
		return equipo;
	}
	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
	public Date getFechaProgramacion() {
		return fechaProgramacion;
	}
	public void setFechaProgramacion(Date fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}
	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	
}