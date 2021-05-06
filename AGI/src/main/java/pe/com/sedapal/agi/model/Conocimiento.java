package pe.com.sedapal.agi.model;

public class Conocimiento {
	private Long	idconocimiento;
	private Long	idpersona;
	private Long	iddocumento;
	private Long	estado;
	private Long 	idrevision;
		
	public Long getIdrevision() {
		return idrevision;
	}
	public void setIdrevision(Long idrevision) {
		this.idrevision = idrevision;
	}
	public Long getIdconocimiento() {
		return idconocimiento;
	}
	public void setIdconocimiento(Long idconocimiento) {
		this.idconocimiento = idconocimiento;
	}
	public Long getIdpersona() {
		return idpersona;
	}
	public void setIdpersona(Long idpersona) {
		this.idpersona = idpersona;
	}
	public Long getIddocumento() {
		return iddocumento;
	}
	public void setIddocumento(Long iddocumento) {
		this.iddocumento = iddocumento;
	}
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}
}