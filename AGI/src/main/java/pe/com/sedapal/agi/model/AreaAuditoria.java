package pe.com.sedapal.agi.model;

public class AreaAuditoria {

	private Integer n_id_area;
	private Integer n_id_tipo;
	private Integer n_cod_area;
	private String v_nom_area;
	private String v_sigla;
	private Integer n_cod_area_padre;
	private Integer n_ficha;
	private String responsable;
	private Integer v_st_reg;

	public Integer getN_id_area() {
		return n_id_area;
	}

	public void setN_id_area(Integer n_id_area) {
		this.n_id_area = n_id_area;
	}

	public Integer getN_id_tipo() {
		return n_id_tipo;
	}

	public void setN_id_tipo(Integer n_id_tipo) {
		this.n_id_tipo = n_id_tipo;
	}

	public Integer getN_cod_area() {
		return n_cod_area;
	}

	public void setN_cod_area(Integer n_cod_area) {
		this.n_cod_area = n_cod_area;
	}

	public String getV_nom_area() {
		return v_nom_area;
	}

	public void setV_nom_area(String v_nom_area) {
		this.v_nom_area = v_nom_area;
	}

	public String getV_sigla() {
		return v_sigla;
	}

	public void setV_sigla(String v_sigla) {
		this.v_sigla = v_sigla;
	}

	public Integer getN_cod_area_padre() {
		return n_cod_area_padre;
	}

	public void setN_cod_area_padre(Integer n_cod_area_padre) {
		this.n_cod_area_padre = n_cod_area_padre;
	}

	public Integer getN_ficha() {
		return n_ficha;
	}

	public void setN_ficha(Integer n_ficha) {
		this.n_ficha = n_ficha;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Integer getV_st_reg() {
		return v_st_reg;
	}

	public void setV_st_reg(Integer v_st_reg) {
		this.v_st_reg = v_st_reg;
	}

}
