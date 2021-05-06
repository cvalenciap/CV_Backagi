package pe.com.sedapal.agi.model;

public class ConocimientoRevisionDocumento {
	private Documento documento;
	private Revision revision;
	private Long cantidadDesconoc;
	private Long cantidadConoc;
	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	public Revision getRevision() {
		return revision;
	}
	public void setRevision(Revision revision) {
		this.revision = revision;
	}
	public Long getCantidadDesconoc() {
		return cantidadDesconoc;
	}
	public void setCantidadDesconoc(Long cantidadDesconoc) {
		this.cantidadDesconoc = cantidadDesconoc;
	}
	public Long getCantidadConoc() {
		return cantidadConoc;
	}
	public void setCantidadConoc(Long cantidadConoc) {
		this.cantidadConoc = cantidadConoc;
	}
	
	
}
