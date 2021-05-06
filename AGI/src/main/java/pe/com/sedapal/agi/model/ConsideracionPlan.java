package pe.com.sedapal.agi.model;

public class ConsideracionPlan {
	private Long idConsideracion;
	private String textoConsideracion;
	private Long idAuditoria;
	private String estadoRegistro;
	private DatosAuditoria datosAuditoria;
	
	public Long getIdConsideracion() {
		return idConsideracion;
	}
	public void setIdConsideracion(Long idConsideracion) {
		this.idConsideracion = idConsideracion;
	}
	public String getTextoConsideracion() {
		return textoConsideracion;
	}
	public void setTextoConsideracion(String textoConsideracion) {
		this.textoConsideracion = textoConsideracion;
	}
	public Long getIdAuditoria() {
		return idAuditoria;
	}
	public void setIdAuditoria(Long idAuditoria) {
		this.idAuditoria = idAuditoria;
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
