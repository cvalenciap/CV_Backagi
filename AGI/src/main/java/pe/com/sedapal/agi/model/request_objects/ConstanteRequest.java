package pe.com.sedapal.agi.model.request_objects;

public class ConstanteRequest {
	private Long idconstante;
	private Long n_discons;
	private String v_padre;
	private String v_nomcons;
	private String v_descons;
	
	public Long getN_discons() {
		return n_discons;
	}
	public void setN_discons(Long n_discons) {
		this.n_discons = n_discons;
	}
	public String getV_padre() {
		return v_padre;
	}
	public void setV_padre(String v_padre) {
		this.v_padre = v_padre;
	}
	public String getV_nomcons() {
		return v_nomcons;
	}
	public void setV_nomcons(String v_nomcons) {
		this.v_nomcons = v_nomcons;
	}
	public String getV_descons() {
		return v_descons;
	}
	public void setV_descons(String v_descons) {
		this.v_descons = v_descons;
	}
	public Long getIdconstante() {
		return idconstante;
	}
	public void setIdconstante(Long idconstante) {
		this.idconstante = idconstante;
	}	
	public void setEstado(Long n_discons) {
		this.n_discons = n_discons;
	}
	public String getDescripcion() {
		return v_descons;
	}
	
	public String getNombre() {
		return v_nomcons;
	}
	public void setNomconstante(String v_nomcons) {
		this.v_nomcons = v_nomcons;
	}
	public void setDescripcion(String v_descons) {
		this.v_descons = v_descons;
	}
	public String getPadre() {
		return v_padre;
	}
	public void setPadre(String v_padre) {
		this.v_padre = v_padre;
	}
	
}