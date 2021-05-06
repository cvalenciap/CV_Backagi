package pe.com.sedapal.agi.model;

public class Pregunta {
Long iD;
String pregunta;
String auditorLider;
String auditorLiderInterno;
String auditorInterno;
String auditorObservador;

String estado;

public String getEstado() {
	return estado;
}


public void setEstado(String estado) {
	this.estado = estado;
}





int rNum;
int total;
String item;

String radioNum;

private String vRolAuditor;



public String getAuditorObservador() {
	return auditorObservador;
}


public void setAuditorObservador(String auditorObservador) {
	this.auditorObservador = auditorObservador;
}


public String getvRolAuditor() {
	return vRolAuditor;
}


public void setvRolAuditor(String vRolAuditor) {
	this.vRolAuditor = vRolAuditor;
}






public Long getiD() {
	return iD;
}


public void setiD(Long iD) {
	this.iD = iD;
}


public String getPregunta() {
	return pregunta;
}


public void setPregunta(String pregunta) {
	this.pregunta = pregunta;
}


public String getAuditorLider() {
	return auditorLider;
}


public void setAuditorLider(String auditorLider) {
	this.auditorLider = auditorLider;
}


public String getAuditorLiderInterno() {
	return auditorLiderInterno;
}


public void setAuditorLiderInterno(String auditorLiderInterno) {
	this.auditorLiderInterno = auditorLiderInterno;
}


public String getAuditorInterno() {
	return auditorInterno;
}


public void setAuditorInterno(String auditorInterno) {
	this.auditorInterno = auditorInterno;
}




public int getrNum() {
	return rNum;
}


public void setrNum(int rNum) {
	this.rNum = rNum;
}


public int getTotal() {
	return total;
}


public void setTotal(int total) {
	this.total = total;
}


public Pregunta() {
	super();
}


public String getItem() {
	return item;
}


public void setItem(String item) {
	this.item = item;
}


public String getRadioNum() {
	return radioNum;
}


public void setRadioNum(String radioNum) {
	this.radioNum = radioNum;
}







}
