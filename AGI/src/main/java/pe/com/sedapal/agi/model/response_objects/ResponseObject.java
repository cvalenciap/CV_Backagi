package pe.com.sedapal.agi.model.response_objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import pe.com.sedapal.agi.model.response_objects.Error;

@JsonInclude(Include.NON_NULL)
public class ResponseObject {

	private Estado estado;
	private Paginacion paginacion;
	private Error error;
	private Object resultado;
	
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public Paginacion getPaginacion() {
		return paginacion;
	}
	public void setPaginacion(Paginacion paginacion) {
		this.paginacion = paginacion;
	}
	public pe.com.sedapal.agi.model.response_objects.Error getError() {
		return error;
	}
	public void setError(pe.com.sedapal.agi.model.response_objects.Error error) {
		this.error = error;
	}
	public void setError(Integer codigo, String mensaje, String mensajeInterno) {
		this.error = new pe.com.sedapal.agi.model.response_objects.Error(codigo, mensaje, mensajeInterno);
	}
	public Object getResultado() {
		return resultado;
	}
	public void setResultado(Object resultado) {
		this.resultado = resultado;
	}
}
