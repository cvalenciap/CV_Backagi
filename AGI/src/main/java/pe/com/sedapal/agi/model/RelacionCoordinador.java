package pe.com.sedapal.agi.model;

public class RelacionCoordinador {
	private Long idRelacion;
	private Long idGerencia;
	private Long idAlcance;
	private Long idCoordinador;
	private String descripcionGerencia;
	private String descripcionAlcance;
	private String nombreCompletoCoordinador;
	private Long nroFicha;
	private Long indicadorDocumento;
	private String descripcionIndicador;
	private String estadoRegistro;
	private DatosAuditoria datosAuditoria;
	private Long indicadorSinAlcance;
	
	public Long getIdRelacion() {
		return idRelacion;
	}
	public void setIdRelacion(Long idRelacion) {
		this.idRelacion = idRelacion;
	}
	public Long getIdGerencia() {
		return idGerencia;
	}
	public void setIdGerencia(Long idGerencia) {
		this.idGerencia = idGerencia;
	}
	public Long getIdAlcance() {
		return idAlcance;
	}
	public void setIdAlcance(Long idAlcance) {
		this.idAlcance = idAlcance;
	}
	public Long getIdCoordinador() {
		return idCoordinador;
	}
	public void setIdCoordinador(Long idCoordinador) {
		this.idCoordinador = idCoordinador;
	}
	public String getDescripcionGerencia() {
		return descripcionGerencia;
	}
	public void setDescripcionGerencia(String descripcionGerencia) {
		this.descripcionGerencia = descripcionGerencia;
	}
	public String getDescripcionAlcance() {
		return descripcionAlcance;
	}
	public void setDescripcionAlcance(String descripcionAlcance) {
		this.descripcionAlcance = descripcionAlcance;
	}
	public String getNombreCompletoCoordinador() {
		return nombreCompletoCoordinador;
	}
	public void setNombreCompletoCoordinador(String nombreCompletoCoordinador) {
		this.nombreCompletoCoordinador = nombreCompletoCoordinador;
	}
	public Long getNroFicha() {
		return nroFicha;
	}
	public void setNroFicha(Long nroFicha) {
		this.nroFicha = nroFicha;
	}
	public Long getIndicadorDocumento() {
		return indicadorDocumento;
	}
	public void setIndicadorDocumento(Long indicadorDocumento) {
		this.indicadorDocumento = indicadorDocumento;
	}
	public String getDescripcionIndicador() {
		return descripcionIndicador;
	}
	public void setDescripcionIndicador(String descripcionIndicador) {
		this.descripcionIndicador = descripcionIndicador;
	}
	public String getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public Long getIndicadorSinAlcance() {
		return indicadorSinAlcance;
	}
	public void setIndicadorSinAlcance(Long indicadorSinAlcance) {
		this.indicadorSinAlcance = indicadorSinAlcance;
	}
}
