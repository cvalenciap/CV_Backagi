package pe.com.sedapal.agi.model;

import java.util.List;
import java.util.Date;
public class Cancelacion {
	 private Long idDocumento;
	 private String codigoDocumento;
	 private String tituloDocumento;
	 private Long numEstadoDocumento;
	 private Long idRevision;
	 private Integer numeroRevision;
	 private String estadoDocumento;
	 private Long idSolicitudCancelacion;
	 private Long numEstadoSolicitud;
	 private String estadoSolicitud;
	 private Long numMotivoCancelacion;
	 private String motivoCancelacion;
	 private Long numTipoCancelacion;
	 private String tipoCancelacion;
	 private Long idColaborador;
	 private String nombreColaborador;
	 private String Aprobador;
	 private String Cancelador;
	 private Long idUsuAprobador;
	 
	 //cguerra
	 private String idGoogleDrive;
	 //cguerra
	 
	 
	 
	 
	 
	 public String getIdGoogleDrive() {
		return idGoogleDrive;
	}
	public void setIdGoogleDrive(String idGoogleDrive) {
		this.idGoogleDrive = idGoogleDrive;
	}
	public String getAprobador() {
		return Aprobador;
	}
	public void setAprobador(String aprobador) {
		Aprobador = aprobador;
	}
	public String getCancelador() {
		return Cancelador;
	}
	public void setCancelador(String cancelador) {
		Cancelador = cancelador;
	}

	private String apePatColaborador;
	 private String apeMatColaborador;
	 private String sustentoSolicitud;
	 private String sustentoAprobacion;
	 private Date fechaSolicitud;
	 private Date fechaAprobacion;
	 private Date fechaCancelacion;
	 public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}
	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}
	
	 private String rutaArchivoSustento;
	 private String nombreArchivoSustento;
	 private String sustentoRechazo;
	 private DatosAuditoria datosAuditoria;
	 private String descripcionAlcance;
	 private String descripcionGerencia;
	 private String descripcionProceso;
	 private List<SolicitudDocumentoComplementario> listaSolicitudesDocComp;
	 private int tipo;
	 
	 private String rutaDocumento;
	 
	 
	public Long getIdSolicitudCancelacion() {
		return idSolicitudCancelacion;
	}
	public void setIdSolicitudCancelacion(Long idSolicitudCancelacion) {
		this.idSolicitudCancelacion = idSolicitudCancelacion;
	}

	
	public Long getNumEstadoDocumento() {
		return numEstadoDocumento;
	}
	public void setNumEstadoDocumento(Long numEstadoDocumento) {
		this.numEstadoDocumento = numEstadoDocumento;
	}
	public Long getNumEstadoSolicitud() {
		return numEstadoSolicitud;
	}
	public void setNumEstadoSolicitud(Long numEstadoSolicitud) {
		this.numEstadoSolicitud = numEstadoSolicitud;
	}
	public Long getNumMotivoCancelacion() {
		return numMotivoCancelacion;
	}
	public void setNumMotivoCancelacion(Long numMotivoCancelacion) {
		this.numMotivoCancelacion = numMotivoCancelacion;
	}
	public Long getNumTipoCancelacion() {
		return numTipoCancelacion;
	}
	public void setNumTipoCancelacion(Long numTipoCancelacion) {
		this.numTipoCancelacion = numTipoCancelacion;
	}
	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
	public String getNombreColaborador() {
		return nombreColaborador;
	}
	public void setNombreColaborador(String nombreColaborador) {
		this.nombreColaborador = nombreColaborador;
	}
	public String getApePatColaborador() {
		return apePatColaborador;
	}
	public void setApePatColaborador(String apePatColaborador) {
		this.apePatColaborador = apePatColaborador;
	}
	public String getApeMatColaborador() {
		return apeMatColaborador;
	}
	public void setApeMatColaborador(String apeMatColaborador) {
		this.apeMatColaborador = apeMatColaborador;
	}
	public String getSustentoSolicitud() {
		return sustentoSolicitud;
	}
	public void setSustentoSolicitud(String sustentoSolicitud) {
		this.sustentoSolicitud = sustentoSolicitud;
	}
	public String getSustentoAprobacion() {
		return sustentoAprobacion;
	}
	public void setSustentoAprobacion(String sustentoAprobacion) {
		this.sustentoAprobacion = sustentoAprobacion;
	}
	public String getRutaArchivoSustento() {
		return rutaArchivoSustento;
	}
	public void setRutaArchivoSustento(String rutaArchivoSustento) {
		this.rutaArchivoSustento = rutaArchivoSustento;
	}
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public String getTituloDocumento() {
		return tituloDocumento;
	}
	public void setTituloDocumento(String tituloDocumento) {
		this.tituloDocumento = tituloDocumento;
	}
	public Long getIdRevision() {
		return idRevision;
	}
	public void setIdRevision(Long idRevision) {
		this.idRevision = idRevision;
	}
	public Integer getNumeroRevision() {
		return numeroRevision;
	}
	public void setNumeroRevision(Integer numeroRevision) {
		this.numeroRevision = numeroRevision;
	}
	public String getEstadoDocumento() {
		return estadoDocumento;
	}
	public void setEstadoDocumento(String estadoDocumento) {
		this.estadoDocumento = estadoDocumento;
	}
	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}
	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}
	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}
	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}
	public String getTipoCancelacion() {
		return tipoCancelacion;
	}
	public void setTipoCancelacion(String tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getNombreArchivoSustento() {
		return nombreArchivoSustento;
	}
	public void setNombreArchivoSustento(String nombreArchivoSustento) {
		this.nombreArchivoSustento = nombreArchivoSustento;
	}
	public List<SolicitudDocumentoComplementario> getListaSolicitudesDocComp() {
		return listaSolicitudesDocComp;
	}
	public void setListaSolicitudesDocComp(List<SolicitudDocumentoComplementario> listaSolicitudesDocComp) {
		this.listaSolicitudesDocComp = listaSolicitudesDocComp;
	}
	public String getSustentoRechazo() {
		return sustentoRechazo;
	}
	public void setSustentoRechazo(String sustentoRechazo) {
		this.sustentoRechazo = sustentoRechazo;
	}
	public String getDescripcionAlcance() {
		return descripcionAlcance;
	}
	public void setDescripcionAlcance(String descripcionAlcance) {
		this.descripcionAlcance = descripcionAlcance;
	}
	public String getDescripcionGerencia() {
		return descripcionGerencia;
	}
	public void setDescripcionGerencia(String descripcionGerencia) {
		this.descripcionGerencia = descripcionGerencia;
	}
	public String getDescripcionProceso() {
		return descripcionProceso;
	}
	public void setDescripcionProceso(String descripcionProceso) {
		this.descripcionProceso = descripcionProceso;
	}
	public String getRutaDocumento() {
		return rutaDocumento;
	}
	public void setRutaDocumento(String rutaDocumento) {
		this.rutaDocumento = rutaDocumento;
	}
	public Long getIdUsuAprobador() {
		return idUsuAprobador;
	}
	public void setIdUsuAprobador(Long idUsuAprobador) {
		this.idUsuAprobador = idUsuAprobador;
	}
	
	
	
	
	
	
	
	
	
	 
}
