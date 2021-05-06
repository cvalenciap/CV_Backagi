package pe.com.sedapal.agi.model;


public class ConstantesLista {
	private Number idConstante;
	private Number idPadre;
	private String valorNombreConstante;
	private String valorNombrePadre;
	
	public ConstantesLista() {
		super();
	}

	public Number getIdConstante() {
		return idConstante;
	}

	public void setIdConstante(Number idConstante) {
		this.idConstante = idConstante;
	}

	public Number getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(Number idPadre) {
		this.idPadre = idPadre;
	}

	public String getValorNombreConstante() {
		return valorNombreConstante;
	}

	public void setValorNombreConstante(String valorNombreConstante) {
		this.valorNombreConstante = valorNombreConstante;
	}

	public String getValorNombrePadre() {
		return valorNombrePadre;
	}

	public void setValorNombrePadre(String valorNombrePadre) {
		this.valorNombrePadre = valorNombrePadre;
	}
		
}
