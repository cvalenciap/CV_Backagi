package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Correo;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface ICorreoDatosService {
	Correo obtenerCorreo(List<Long> listaId, String tipoCorreo) throws Exception;
	List<Correo> obtenerListaCorreo(List<Long> listaId, String tipoCorreo) throws Exception;
	Error getError();
}