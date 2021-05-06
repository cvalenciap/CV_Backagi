package pe.com.sedapal.agi.service.impl;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.ICargoSigDAO;
import pe.com.sedapal.agi.model.CargoSig;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.ICargoSigService;
import pe.com.sedapal.agi.util.Constantes;
import pe.com.sedapal.agi.util.RespuestaBean;

@Service
public class CargoSigServiceImpl implements ICargoSigService {

	private static final Logger LOGGER = Logger.getLogger(CargoSigServiceImpl.class);

	RespuestaBean respuesta = new RespuestaBean();

	@Autowired
	ICargoSigDAO dao;

	@Override
	public RespuestaBean consultaBusqueda(Map<String, Object> requestParm) throws GmdException {
		List<CargoSig> listaCargoSig = new ArrayList<CargoSig>();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			listaCargoSig = this.dao.obtenerListaCargoSig(requestParm);
			if (listaCargoSig.size() > 0) {
				mapResult.put("listaCargoSig", listaCargoSig);
				respuesta.setEstadoRespuesta(Constantes.RESULTADO_OK);
				respuesta.setParametros(mapResult);
			} else {
				throw new GmdException(Constantes.MESSAGE_ERROR.get(3005));
			}
		} catch (Exception exception) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(exception);
			respuesta.setEstadoRespuesta(Constantes.RESULTADO_ERROR);
			respuesta.setMensajeRespuesta(format(Constantes.MENSAJE_ERROR_LOG, error[0]) + ".Traza inicial del error : "
					+ exception.getMessage());
			LOGGER.error(error[1], exception);
		}
		return respuesta;
	}

	// Registra un cargo SIG
	@Override
	public RespuestaBean registraCargoSig(Map<String, Object> requestParm) throws GmdException {
		List<CargoSig> listaCargoSig = new ArrayList<CargoSig>();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		RespuestaBean retorno = new RespuestaBean();
		try {
			retorno = this.dao.registroCargoSig(requestParm);
			if (retorno.getEstadoRespuesta().equals("OK")) {
				mapResult.put("Repuesta", retorno);
				respuesta.setEstadoRespuesta(Constantes.RESULTADO_OK);
				respuesta.setParametros(mapResult);
			} else {
				throw new GmdException(Constantes.MESSAGE_ERROR.get(3005));
			}
		} catch (Exception exception) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(exception);
			respuesta.setEstadoRespuesta(Constantes.RESULTADO_ERROR);
			respuesta.setMensajeRespuesta(format(Constantes.MENSAJE_ERROR_LOG, error[0]) + ".Traza inicial del error : "
					+ exception.getMessage());
			LOGGER.error(error[1], exception);
		}
		return respuesta;
	}

	@Override
	public RespuestaBean ModifCargoSig(Map<String, Object> requestParm) throws GmdException {
		List<CargoSig> listaCargoSig = new ArrayList<CargoSig>();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		RespuestaBean retorno = new RespuestaBean();
		try {
			retorno = this.dao.ModifCargoSig(requestParm);
			if (retorno.getEstadoRespuesta().equals("OK")) {
				mapResult.put("Repuesta", retorno);
				respuesta.setEstadoRespuesta(Constantes.RESULTADO_OK);
				respuesta.setParametros(mapResult);
			} else {
				throw new GmdException(Constantes.MESSAGE_ERROR.get(3005));
			}
		} catch (Exception exception) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(exception);
			respuesta.setEstadoRespuesta(Constantes.RESULTADO_ERROR);
			respuesta.setMensajeRespuesta(format(Constantes.MENSAJE_ERROR_LOG, error[0]) + ".Traza inicial del error : "
					+ exception.getMessage());
			LOGGER.error(error[1], exception);
		}
		return respuesta;
	}

	@Override
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}

}
