package pe.com.sedapal.agi.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import pe.com.sedapal.agi.util.CastUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericParam<T> {

	private T codigo;
	private String descripcion;

	public T getCodigo() {
		return codigo;
	}

	public void setCodigo(T codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@SuppressWarnings("unchecked")
	public static GenericParam<Integer> mapperParamInteger(Map<String, Object> map) {
		GenericParam<Integer> param = new GenericParam<>();
		param.setCodigo(CastUtil.leerValorMapInteger(map, "codigo"));
		param.setDescripcion(CastUtil.leerValorMapString(map, "descripcion"));
		return param;
	}
	
	@SuppressWarnings("unchecked")
	public static GenericParam<String> mapperParamString(Map<String, Object> map) {
		GenericParam<String> param = new GenericParam<>();
		param.setCodigo(CastUtil.leerValorMapString(map, "codigo"));
		param.setDescripcion(CastUtil.leerValorMapString(map, "descripcion"));
		return param;
	}

}
