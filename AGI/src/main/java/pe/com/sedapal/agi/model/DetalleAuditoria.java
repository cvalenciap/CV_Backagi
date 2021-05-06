package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

public class DetalleAuditoria {
	private Long idDetalleAuditoria;
	private Long idAuditoria;
	private Date fecha;//
	private String norma;//
	private String alcance;//
	private String valorTipoEntidad;//
	private String valorEntidadGerencia;//
	private String valorEntidadEquipo;//
	private String valorEntidadCargo;//
	private String valorEntidadComite;//
	private String descripcionEntidad;
	
	private List<AuditorAuditoria> listaParticipante; //
	
	private List<RequisitoAuditoriaDetalle> listaRequisitos;//
	
	private List<Comite> listaComite;//
	
	private String area;//
	private String fechaDia;//
	private String fechaHora;//
	private String auditor;//
	private String requisito;//
	
	private DatosAuditoria datosAuditoria;
	
	
	
	
	public Long getIdDetalleAuditoria() {
		return idDetalleAuditoria;
	}
	public void setIdDetalleAuditoria(Long idDetalleAuditoria) {
		this.idDetalleAuditoria = idDetalleAuditoria;
	}
	public Long getIdAuditoria() {
		return idAuditoria;
	}
	public void setIdAuditoria(Long idAuditoria) {
		this.idAuditoria = idAuditoria;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNorma() {
		return norma;
	}
	public void setNorma(String norma) {
		this.norma = norma;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public String getValorTipoEntidad() {
		return valorTipoEntidad;
	}
	public void setValorTipoEntidad(String valorTipoEntidad) {
		this.valorTipoEntidad = valorTipoEntidad;
	}
	public String getValorEntidadGerencia() {
		return valorEntidadGerencia;
	}
	public void setValorEntidadGerencia(String valorEntidadGerencia) {
		this.valorEntidadGerencia = valorEntidadGerencia;
	}
	public String getValorEntidadEquipo() {
		return valorEntidadEquipo;
	}
	public void setValorEntidadEquipo(String valorEntidadEquipo) {
		this.valorEntidadEquipo = valorEntidadEquipo;
	}
	public String getValorEntidadCargo() {
		return valorEntidadCargo;
	}
	public void setValorEntidadCargo(String valorEntidadCargo) {
		this.valorEntidadCargo = valorEntidadCargo;
	}
	public String getValorEntidadComite() {
		return valorEntidadComite;
	}
	public void setValorEntidadComite(String valorEntidadComite) {
		this.valorEntidadComite = valorEntidadComite;
	}
	public String getDescripcionEntidad() {
		return descripcionEntidad;
	}
	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}
	public List<AuditorAuditoria> getListaParticipante() {
		return listaParticipante;
	}
	public void setListaParticipante(List<AuditorAuditoria> listaParticipante) {
		this.listaParticipante = listaParticipante;
	}
	public List<RequisitoAuditoriaDetalle> getListaRequisitos() {
		return listaRequisitos;
	}
	public void setListaRequisitos(List<RequisitoAuditoriaDetalle> listaRequisitos) {
		this.listaRequisitos = listaRequisitos;
	}
	public List<Comite> getListaComite() {
		return listaComite;
	}
	public void setListaComite(List<Comite> listaComite) {
		this.listaComite = listaComite;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getFechaDia() {
		return fechaDia;
	}
	public void setFechaDia(String fechaDia) {
		this.fechaDia = fechaDia;
	}
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getRequisito() {
		return requisito;
	}
	public void setRequisito(String requisito) {
		this.requisito = requisito;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	
	
	
	

	
	
}
