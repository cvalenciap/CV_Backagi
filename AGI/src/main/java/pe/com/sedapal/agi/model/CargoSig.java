package pe.com.sedapal.agi.model;

public class CargoSig {

	private String nombreCargoSig;
	private String sigla;
	private String nombreCompleto;
	private Long idCargoSig;
	private Long n_Ficha;

	public String getNombreCargoSig() {
		return nombreCargoSig;
	}

	public void setNombreCargoSig(String nombreCargoSig) {
		this.nombreCargoSig = nombreCargoSig;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public Long getIdCargoSig() {
		return idCargoSig;
	}

	public void setIdCargoSig(Long idCargoSig) {
		this.idCargoSig = idCargoSig;
	}

	public Long getN_Ficha() {
		return n_Ficha;
	}

	public void setN_Ficha(Long n_Ficha) {
		this.n_Ficha = n_Ficha;
	}

}
