package pe.com.sedapal.agi.model.response_objects;

import java.math.BigDecimal;

public class Paginacion {

	private Integer pagina;
	private Integer registros;
	private Integer totalPaginas;
	private Integer totalRegistros;
		
	public Integer getPagina() {
		return pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	public Integer getRegistros() {
		return registros;
	}
	public void setRegistros(Integer registros) {
		this.registros = registros;
	}
	public Integer getTotalPaginas() {
		return totalPaginas;
	}
	public void setTotalPaginas(Integer totalPaginas) {
		this.totalPaginas = totalPaginas;
	}
	public Integer getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
		if (this.registros > 0) {
			this.totalPaginas = (int)Math.ceil(this.totalRegistros / (float)this.registros);
		}
	}
	
	
	
}
