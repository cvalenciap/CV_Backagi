package pe.com.sedapal.agi.model;

public class AreaCargoAuditoria {

	private Integer n_id_cargo_area;
	private Integer n_id_cargo_sig;
	private Integer n_id_area;
	private String v_nom_cargo_sig;
	private String v_sigla;

	public Integer getN_id_cargo_area() {
		return n_id_cargo_area;
	}

	public void setN_id_cargo_area(Integer n_id_cargo_area) {
		this.n_id_cargo_area = n_id_cargo_area;
	}

	public Integer getN_id_cargo_sig() {
		return n_id_cargo_sig;
	}

	public void setN_id_cargo_sig(Integer n_id_cargo_sig) {
		this.n_id_cargo_sig = n_id_cargo_sig;
	}

	public Integer getN_id_area() {
		return n_id_area;
	}

	public void setN_id_area(Integer n_id_area) {
		this.n_id_area = n_id_area;
	}

	public String getV_nom_cargo_sig() {
		return v_nom_cargo_sig;
	}

	public void setV_nom_cargo_sig(String v_nom_cargo_sig) {
		this.v_nom_cargo_sig = v_nom_cargo_sig;
	}

	public String getV_sigla() {
		return v_sigla;
	}

	public void setV_sigla(String v_sigla) {
		this.v_sigla = v_sigla;
	}

}
