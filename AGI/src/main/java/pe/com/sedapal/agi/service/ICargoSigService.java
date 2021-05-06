package pe.com.sedapal.agi.service;

import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.RespuestaBean;

public interface ICargoSigService {

	public Error getError();

	public Paginacion getPaginacion();

	public RespuestaBean consultaBusqueda(Map<String, Object> requestParm) throws GmdException;

	public RespuestaBean registraCargoSig(Map<String, Object> requestParm) throws GmdException;

	public RespuestaBean ModifCargoSig(Map<String, Object> requestParm) throws GmdException;

}
