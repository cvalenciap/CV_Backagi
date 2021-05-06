package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

public class Auditoria {		

	private Long idAuditoria;
	private String estadoAuditoria;//
	private String descripcionEstadoAuditoria;
	private String mes;//
	private Long idPrograma;
	private String gerencia;//
	private String equipo;//
	private String cargo;//
	private String comite;//
    private String descripcionEntidad;//
	private String tipoAuditoria;//
	private String descripcionTipoAuditoria;
	private Date fechaInicio;
	private Date fechaFin;
	private String textoFechaInicio;
	private String textoFechaFin;
	private String descripcionAuditoria;
	private String objetivo;//
	private String alcance;//
	private Long idColaborador;//
	private String rechazoAuditoria;//
	private Long idNorma;
	private String descripcionNorma;
	private Long idObservador;//
	private Integer anio;
	private DatosAuditoria datosAuditoria;//
	private String descripcionMes;//	
	private String cicloAuditoria;
	
	private List<CriterioResultado> listaCriterios;//
	private List<ConsideracionPlan> listaConsideracionesPlan;//
	private List<Norma> listaNormas;
	private List<DetalleAuditoria> listaDetalle;
	private String nroAuditoria;
	private Programa programa;
	private String auditorLider; 
	private String observadorLiderGrupo;
	 
	
	
	
	public Long getIdAuditoria() {
		return idAuditoria;
	}
	public void setIdAuditoria(Long idAuditoria) {
		this.idAuditoria = idAuditoria;
	}
	public String getEstadoAuditoria() {
		return estadoAuditoria;
	}
	public void setEstadoAuditoria(String estadoAuditoria) {
		this.estadoAuditoria = estadoAuditoria;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Long getIdPrograma() {
		return idPrograma;
	}
	public void setIdPrograma(Long idPrograma) {
		this.idPrograma = idPrograma;
	}
	public String getGerencia() {
		return gerencia;
	}
	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getComite() {
		return comite;
	}
	public void setComite(String comite) {
		this.comite = comite;
	}
	public String getTipoAuditoria() {
		return tipoAuditoria;
	}
	public void setTipoAuditoria(String tipoAuditoria) {
		this.tipoAuditoria = tipoAuditoria;
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
	public String getDescripcionAuditoria() {
		return descripcionAuditoria;
	}
	public void setDescripcionAuditoria(String descripcionAuditoria) {
		this.descripcionAuditoria = descripcionAuditoria;
	}
	public String getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
	public Long getIdNorma() {
		return idNorma;
	}
	public void setIdNorma(Long idNorma) {
		this.idNorma = idNorma;
	}
	public Long getIdObservador() {
		return idObservador;
	}
	public void setIdObservador(Long idObservador) {
		this.idObservador = idObservador;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public List<CriterioResultado> getListaCriterios() {
		return listaCriterios;
	}
	public void setListaCriterios(List<CriterioResultado> listaCriterios) {
		this.listaCriterios = listaCriterios;
	}
	public List<ConsideracionPlan> getListaConsideracionesPlan() {
		return listaConsideracionesPlan;
	}
	public void setListaConsideracionesPlan(List<ConsideracionPlan> listaConsideracionesPlan) {
		this.listaConsideracionesPlan = listaConsideracionesPlan;
	}
	
	
	public String getDescripcionEstadoAuditoria() {
		return descripcionEstadoAuditoria;
	}
	public void setDescripcionEstadoAuditoria(String descripcionEstadoAuditoria) {
		this.descripcionEstadoAuditoria = descripcionEstadoAuditoria;
	}
	
	public String getDescripcionMes() {
		return descripcionMes;
	}
	public void setDescripcionMes(String descripcionMes) {
		this.descripcionMes = descripcionMes;
	}
	
	public String getDescripcionEntidad() {
		return descripcionEntidad;
	}
	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}

public String getTextoFechaInicio() {
		return textoFechaInicio;
	}
	public void setTextoFechaInicio(String textoFechaInicio) {
		this.textoFechaInicio = textoFechaInicio;
	}
	public String getTextoFechaFin() {
		return textoFechaFin;
	}
	public void setTextoFechaFin(String textoFechaFin) {
		this.textoFechaFin = textoFechaFin;
	}
	public String getRechazoAuditoria() {
		return rechazoAuditoria;
	}
	public void setRechazoAuditoria(String rechazoAuditoria) {
		this.rechazoAuditoria = rechazoAuditoria;
	}
	
	public List<Norma> getListaNormas() {
		return listaNormas;
	}
	public void setListaNormas(List<Norma> listaNormas) {
		this.listaNormas = listaNormas;
	}
	public String getNroAuditoria() {
		return nroAuditoria;
	}
	public void setNroAuditoria(String nroAuditoria) {
		this.nroAuditoria = nroAuditoria;
	}
	public String getCicloAuditoria() {
		return cicloAuditoria;
	}
	public void setCicloAuditoria(String cicloAuditoria) {
		this.cicloAuditoria = cicloAuditoria;
	}
	public List<DetalleAuditoria> getListaDetalle() {
		return listaDetalle;
	}
	public void setListaDetalle(List<DetalleAuditoria> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	public String getDescripcionTipoAuditoria() {
		return descripcionTipoAuditoria;
	}
	public void setDescripcionTipoAuditoria(String descripcionTipoAuditoria) {
		this.descripcionTipoAuditoria = descripcionTipoAuditoria;
	}
	
	public String getDescripcionNorma() {
		return descripcionNorma;
	}
	public void setDescripcionNorma(String descripcionNorma) {
		this.descripcionNorma = descripcionNorma;
	}
	public String getAuditorLider() {
		return auditorLider;
	}
	public void setAuditorLider(String auditorLider) {
		this.auditorLider = auditorLider;
	}
	public String getObservadorLiderGrupo() {
		return observadorLiderGrupo;
	}
	public void setObservadorLiderGrupo(String observadorLiderGrupo) {
		this.observadorLiderGrupo = observadorLiderGrupo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
