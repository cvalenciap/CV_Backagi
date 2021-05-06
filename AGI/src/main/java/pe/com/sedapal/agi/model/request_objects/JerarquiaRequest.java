package pe.com.sedapal.agi.model.request_objects;

public class JerarquiaRequest {
	Long	id;
	Long	tipo;
	String	descripcion;
	Long	nivel;
	Long	estado;
	Long    idTipoDocu;
	String tipoJerarquiaNombre;
	Long indicadorDescargas;
	
	
	
	public Long getIndicadorDescargas() {
		return indicadorDescargas;
	}
	public void setIndicadorDescargas(Long indicadorDescargas) {
		this.indicadorDescargas = indicadorDescargas;
	}
	public Long getIdTipoDocu() {
		return idTipoDocu;
	}
	public void setIdTipoDocu(Long idTipoDocu) {
		this.idTipoDocu = idTipoDocu;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTipo() {
		return tipo;
	}
	public void setTipo(Long tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getNivel() {
		return nivel;
	}
	public void setNivel(Long nivel) {
		this.nivel = nivel;
	}
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}
	public String getTipoJerarquiaNombre() {
		return tipoJerarquiaNombre;
	}
	public void setTipoJerarquiaNombre(String tipoJerarquiaNombre) {
		this.tipoJerarquiaNombre = tipoJerarquiaNombre;
	}	

}