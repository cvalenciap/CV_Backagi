package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

public class ListaVerificacion {
	private Long idListaVerificacion;
	private String calificacionLV;
	private Long idArchivoEvidencia;
	private String nombreArchivoEvidencia;
	private String tipoLV;
	private String sustentoRechazo;
	private Long idAuditoria;
	private Date fecha;
	private String estadoListaVerificacion;
	private String codigoGerencia;
	private String codigoEquipo;
	private String codigoCargo;
	private String codigoComite;
	private String textoNormas;
	private String textoAuditores;
	private String auditorLider;
	private String descripcionArea;
	private String descripcionAuditoria;
	private String valorEntidad;
	private String estadoRevisionHallazgos;
	private DatosAuditoria datosAuditoria;
	private List<ListaVerificacionRequisito> listaRequisitosLV;
	private List<NodoRequisitoLV> listaNodosRequisitoLV;
	private List<ListaVerificacionAuditado> listaAuditadosLV;
	
	public Long getIdListaVerificacion() {
		return idListaVerificacion;
	}
	public void setIdListaVerificacion(Long idListaVerificacion) {
		this.idListaVerificacion = idListaVerificacion;
	}
	public String getCalificacionLV() {
		return calificacionLV;
	}
	public void setCalificacionLV(String calificacionLV) {
		this.calificacionLV = calificacionLV;
	}
	public Long getIdArchivoEvidencia() {
		return idArchivoEvidencia;
	}
	public void setIdArchivoEvidencia(Long idArchivoEvidencia) {
		this.idArchivoEvidencia = idArchivoEvidencia;
	}
	public String getNombreArchivoEvidencia() {
		return nombreArchivoEvidencia;
	}
	public void setNombreArchivoEvidencia(String nombreArchivoEvidencia) {
		this.nombreArchivoEvidencia = nombreArchivoEvidencia;
	}
	public String getTipoLV() {
		return tipoLV;
	}
	public void setTipoLV(String tipoLV) {
		this.tipoLV = tipoLV;
	}
	public String getSustentoRechazo() {
		return sustentoRechazo;
	}
	public void setSustentoRechazo(String sustentoRechazo) {
		this.sustentoRechazo = sustentoRechazo;
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
	public String getEstadoListaVerificacion() {
		return estadoListaVerificacion;
	}
	public void setEstadoListaVerificacion(String estadoListaVerificacion) {
		this.estadoListaVerificacion = estadoListaVerificacion;
	}
	public String getCodigoGerencia() {
		return codigoGerencia;
	}
	public void setCodigoGerencia(String codigoGerencia) {
		this.codigoGerencia = codigoGerencia;
	}
	public String getCodigoEquipo() {
		return codigoEquipo;
	}
	public void setCodigoEquipo(String codigoEquipo) {
		this.codigoEquipo = codigoEquipo;
	}
	public String getCodigoCargo() {
		return codigoCargo;
	}
	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}
	public String getCodigoComite() {
		return codigoComite;
	}
	public void setCodigoComite(String codigoComite) {
		this.codigoComite = codigoComite;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public String getTextoNormas() {
		return textoNormas;
	}
	public void setTextoNormas(String textoNormas) {
		this.textoNormas = textoNormas;
	}
	public String getTextoAuditores() {
		return textoAuditores;
	}
	public void setTextoAuditores(String textoAuditores) {
		this.textoAuditores = textoAuditores;
	}
	public String getAuditorLider() {
		return auditorLider;
	}
	public void setAuditorLider(String auditorLider) {
		this.auditorLider = auditorLider;
	}
	public String getDescripcionArea() {
		return descripcionArea;
	}
	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}
	public String getDescripcionAuditoria() {
		return descripcionAuditoria;
	}
	public void setDescripcionAuditoria(String descripcionAuditoria) {
		this.descripcionAuditoria = descripcionAuditoria;
	}
	public String getValorEntidad() {
		return valorEntidad;
	}
	public void setValorEntidad(String valorEntidad) {
		this.valorEntidad = valorEntidad;
	}
	public List<ListaVerificacionRequisito> getListaRequisitosLV() {
		return listaRequisitosLV;
	}
	public void setListaRequisitosLV(List<ListaVerificacionRequisito> listaRequisitosLV) {
		this.listaRequisitosLV = listaRequisitosLV;
	}
	public List<NodoRequisitoLV> getListaNodosRequisitoLV() {
		return listaNodosRequisitoLV;
	}
	public void setListaNodosRequisitoLV(List<NodoRequisitoLV> listaNodosRequisitoLV) {
		this.listaNodosRequisitoLV = listaNodosRequisitoLV;
	}
	public List<ListaVerificacionAuditado> getListaAuditadosLV() {
		return listaAuditadosLV;
	}
	public void setListaAuditadosLV(List<ListaVerificacionAuditado> listaAuditadosLV) {
		this.listaAuditadosLV = listaAuditadosLV;
	}
	public String getEstadoRevisionHallazgos() {
		return estadoRevisionHallazgos;
	}
	public void setEstadoRevisionHallazgos(String estadoRevisionHallazgos) {
		this.estadoRevisionHallazgos = estadoRevisionHallazgos;
	}
	
	
	
	
	
	
	
	
	
	

	
	
}
