package pe.com.sedapal.agi.dao;

import java.util.List;
import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.model.CargoSig;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.RespuestaBean;

public interface ICargoSigDAO {

	public Error getError();

	public Paginacion getPaginacion();

	public List<CargoSig> obtenerListaCargoSig(Map<String, Object> requestParm) throws GmdException;

	public RespuestaBean registroCargoSig(Map<String, Object> requestParm) throws GmdException;

	public RespuestaBean ModifCargoSig(Map<String, Object> requestParm) throws GmdException;

}
