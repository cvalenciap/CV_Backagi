package pe.com.sedapal.agi.model;

public class RequisitoRelacionado {

	private Long idReqRela;
	private Long idNorReq;
	private Long idNorma;
	private Long idRequisito;
	private String normaRelacionada;
	private String requiRelacionado;
	private DatosAuditoria datosAuditoria;
	private String vestreqrel;
	
	public String getVestreqrel() {
		return vestreqrel;
	}

	public void setVestreqrel(String vestreqrel) {
		this.vestreqrel = vestreqrel;
	}

	public Long getIdReqRela() {
		return idReqRela;
	}

	public void setIdReqRela(Long idReqRela) {
		this.idReqRela = idReqRela;
	}

	public Long getIdNorReq() {
		return idNorReq;
	}

	public void setIdNorReq(Long idNorReq) {
		this.idNorReq = idNorReq;
	}

	public Long getIdNorma() {
		return idNorma;
	}

	public void setIdNorma(Long idNorma) {
		this.idNorma = idNorma;
	}

	public Long getIdRequisito() {
		return idRequisito;
	}

	public void setIdRequisito(Long idRequisito) {
		this.idRequisito = idRequisito;
	}

	public String getNormaRelacionada() {
		return normaRelacionada;
	}

	public void setNormaRelacionada(String normaRelacionada) {
		this.normaRelacionada = normaRelacionada;
	}

	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}

	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}

	public String getRequiRelacionado() {
		return requiRelacionado;
	}

	public void setRequiRelacionado(String requiRelacionado) {
		this.requiRelacionado = requiRelacionado;
	}
}
