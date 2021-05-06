package pe.com.sedapal.agi.model.request_objects;

public class RequisitoRequest {
	private Long idAuditoria;
	private Long idRequisito;
	private Long idNorma;
	private String tipoNorma;
	public Long getIdRequisito() {
		return idRequisito;
	}
	public void setIdRequisito(Long idRequisito) {
		this.idRequisito = idRequisito;
	}
	public Long getIdNorma() {
		return idNorma;
	}
	public void setIdNorma(Long idNorma) {
		this.idNorma = idNorma;
	}
	public String getTipoNorma() {
		return tipoNorma;
	}
	public void setTipoNorma(String tipoNorma) {
		this.tipoNorma = tipoNorma;
	}
	public Long getIdAuditoria() {
		return idAuditoria;
	}
	public void setIdAuditoria(Long idAuditoria) {
		this.idAuditoria = idAuditoria;
	}
	
	
	
	
	
}
