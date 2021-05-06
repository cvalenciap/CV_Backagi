package pe.com.sedapal.agi.model;

import java.io.Serializable;
import java.util.List;

public class CorreoVariable implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<String> nombreVariable;	
	private List<String> valorVariable;
	
	public List<String> getNombreVariable() {
		return nombreVariable;
	}
	public void setNombreVariable(List<String> nombreVariable) {
		this.nombreVariable = nombreVariable;
	}
	public List<String> getValorVariable() {
		return valorVariable;
	}
	public void setValorVariable(List<String> valorVariable) {
		this.valorVariable = valorVariable;
	}
}