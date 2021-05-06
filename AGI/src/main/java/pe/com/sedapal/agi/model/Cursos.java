package pe.com.sedapal.agi.model;

public class Cursos {
	
Number id;
String nombre;
Number idRol;
Number obligatorio;
public Number getId() {
	return id;
}
public void setId(Number id) {
	this.id = id;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public Number getIdRol() {
	return idRol;
}
public void setIdRol(Number idRol) {
	this.idRol = idRol;
}
public Number getObligatorio() {
	return obligatorio;
}
public void setObligatorio(Number obligatorio) {
	this.obligatorio = obligatorio;
}
public Cursos(Number id, String nombre, Number idRol, Number obligatorio) {
	super();
	this.id = id;
	this.nombre = nombre;
	this.idRol = idRol;
	this.obligatorio = obligatorio;
}
public Cursos() {
	super();
	// TODO Auto-generated constructor stub
}

	

}
