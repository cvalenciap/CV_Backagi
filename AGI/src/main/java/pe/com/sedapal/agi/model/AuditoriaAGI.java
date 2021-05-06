package pe.com.sedapal.agi.model;

public abstract class AuditoriaAGI {

	private String idUsuaCrea;
	private String feUsuaCrea;
	private String idTermCrea;
	private String idUsuaModi;
	private String feUsuaModi;
	private String idTermModi;
	private String nomPrg;

	public String getIdUsuaCrea() {
		return idUsuaCrea;
	}

	public void setIdUsuaCrea(String idUsuaCrea) {
		this.idUsuaCrea = idUsuaCrea;
	}

	public String getFeUsuaCrea() {
		return feUsuaCrea;
	}

	public void setFeUsuaCrea(String feUsuaCrea) {
		this.feUsuaCrea = feUsuaCrea;
	}

	public String getIdTermCrea() {
		return idTermCrea;
	}

	public void setIdTermCrea(String idTermCrea) {
		this.idTermCrea = idTermCrea;
	}

	public String getIdUsuaModi() {
		return idUsuaModi;
	}

	public void setIdUsuaModi(String idUsuaModi) {
		this.idUsuaModi = idUsuaModi;
	}

	public String getFeUsuaModi() {
		return feUsuaModi;
	}

	public void setFeUsuaModi(String feUsuaModi) {
		this.feUsuaModi = feUsuaModi;
	}

	public String getIdTermModi() {
		return idTermModi;
	}

	public void setIdTermModi(String idTermModi) {
		this.idTermModi = idTermModi;
	}

	public String getNomPrg() {
		return nomPrg;
	}

	public void setNomPrg(String nomPrg) {
		this.nomPrg = nomPrg;
	}

}
