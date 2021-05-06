package pe.com.sedapal.agi.model;

public class CriterioResultado {
	private Long idCriterio;
	private String valorCriterio;
	private String descripcionCriterio;
	private Long idAuditoria;
	private String estadoRegistro;
	private DatosAuditoria datosAuditoria;
	
	
	
	public Long getIdCriterio() {
		return idCriterio;
	}
	public void setIdCriterio(Long idCriterio) {
		this.idCriterio = idCriterio;
	}
	public Long getIdAuditoria() {
		return idAuditoria;
	}
	public void setIdAuditoria(Long idAuditoria) {
		this.idAuditoria = idAuditoria;
	}
	public String getValorCriterio() {
		return valorCriterio;
	}
	public void setValorCriterio(String valorCriterio) {
		this.valorCriterio = valorCriterio;
	}
	public String getDescripcionCriterio() {
		return descripcionCriterio;
	}
	public void setDescripcionCriterio(String descripcionCriterio) {
		this.descripcionCriterio = descripcionCriterio;
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
	
	
	
	
	

	
	
}
