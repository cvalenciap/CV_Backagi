package pe.com.sedapal.agi.model;

public class FichaAuditor {
int codigo;
int tipo;
String numFicha;
String nomTipo;
String nombreAuditor;
String apePaternoAuditor;
String apeMaternoAuditor;
int codigoRolAuditor;
String nomRol;

public String getApePaternoAuditor() {
	return apePaternoAuditor;
}

public void setApePaternoAuditor(String apePaternoAuditor) {
	this.apePaternoAuditor = apePaternoAuditor;
}

public String getApeMaternoAuditor() {
	return apeMaternoAuditor;
}

public void setApeMaternoAuditor(String apeMaternoAuditor) {
	this.apeMaternoAuditor = apeMaternoAuditor;
}

public String getNumFicha() {
	return numFicha;
}

public void setNumFicha(String numFicha) {
	this.numFicha = numFicha;
}


public String getNomTipo() {
	return nomTipo;
}

public void setNomTipo(String nomTipo) {
	this.nomTipo = nomTipo;
}

public String getNomRol() {
	return nomRol;
}

public void setNomRol(String nomRol) {
	this.nomRol = nomRol;
}

public int getCodigo() {
		return codigo;
}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public String getNombreAuditor() {
		return nombreAuditor;
	}
	
	public void setNombreAuditor(String nombreAuditor) {
		this.nombreAuditor = nombreAuditor;
	}
	
	public int getCodigoRolAuditor() {
		return codigoRolAuditor;
	}
	
	public void setCodigoRolAuditor(int codigoRolAuditor) {
		this.codigoRolAuditor = codigoRolAuditor;
	}

	public FichaAuditor(int codigo, String numFicha, int tipo, String nomTipo, String nombreAuditor, int codigoRolAuditor,
			String nomRol) {
		super();
		this.numFicha=numFicha;
		this.codigo = codigo;
		this.tipo = tipo;
		this.nomTipo = nomTipo;
		this.nombreAuditor = nombreAuditor;
		this.codigoRolAuditor = codigoRolAuditor;
		this.nomRol = nomRol;
	}
	
	public FichaAuditor() {		
	}
	

}
