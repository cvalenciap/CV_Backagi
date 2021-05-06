package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.ICargoSigDAO;
import pe.com.sedapal.agi.model.CargoSig;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.util.Constantes;
import pe.com.sedapal.agi.util.RespuestaBean;

@Repository
public class CargoSigDAOImpl implements ICargoSigDAO {

	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

	private static final Logger LOGGER = Logger.getLogger(CargoSigDAOImpl.class);

	@Override
	public Error getError() {
		return this.error;
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.paginacion;
	}

	@Override
	public List<CargoSig> obtenerListaCargoSig(Map<String, Object> requestParm) throws GmdException {
		Map<String, Object> out = null;
		this.error = null;
		List<CargoSig> listaDetalleCargoSig = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_MAT_OBTENER_CARGO_SIG")
					.declareParameters(new SqlParameter("i_nombre", OracleTypes.VARCHAR),
							new SqlParameter("i_carsig", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nombre", requestParm.get("nombrecompleto"))
					.addValue("i_carsig", requestParm.get("carSig"));
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaDetalleCargoSig = new ArrayList<>();
				listaDetalleCargoSig = mapearDetallesCargoSigs(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CargoSigDAOImpl.obtenerListaCargoSig";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return listaDetalleCargoSig;
	}

	private List<CargoSig> mapearDetallesCargoSigs(Map<String, Object> resultados) throws GmdException {
		List<CargoSig> listaDetalleCargoSig = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		CargoSig item = null;
		try {
			for (Map<String, Object> map : lista) {
				item = new CargoSig();
				if (map.get("V_NOM_CARGO_SIG") != null) {
					item.setNombreCargoSig((String) map.get("V_NOM_CARGO_SIG"));
				}
				if (map.get("V_SIGLA") != null) {
					item.setSigla((String) map.get("V_SIGLA"));
				}
				if (map.get("nomCompleto") != null) {
					item.setNombreCompleto((String) map.get("nomCompleto"));
				}
				if (map.get("N_ID_CARGO_SIG") != null) {
					item.setIdCargoSig(((BigDecimal) map.get("N_ID_CARGO_SIG")).longValue());
				}
				if (map.get("n_ficha") != null) {
					item.setN_Ficha(((BigDecimal) map.get("n_ficha")).longValue());
				}
				listaDetalleCargoSig.add(item);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CargoSigDAOImpl.mapearDetallesCargoSigs";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return listaDetalleCargoSig;
	}

	// registra

	@Override
	public RespuestaBean registroCargoSig(Map<String, Object> requestParm) throws GmdException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String Username = ((UserAuth) principal).getUsername();
		Map<String, Object> out = null;
		this.error = null;
		RespuestaBean respuesta = new RespuestaBean();
		List<CargoSig> listaDetalleCargoSig = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_MAT_REGISTRO_CARGO_SIG")
					.declareParameters(new SqlParameter("i_cargo_sig", OracleTypes.VARCHAR),
							new SqlParameter("i_sigla", OracleTypes.VARCHAR),
							new SqlParameter("i_colaborador", OracleTypes.INTEGER),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource().addValue("i_cargo_sig", requestParm.get("cargosig"))
					.addValue("i_sigla", requestParm.get("sigla")).addValue("i_vusuario", Username)
					.addValue("i_colaborador", requestParm.get("nroficha")); // validar ficha
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				respuesta.setEstadoRespuesta(Constantes.RESULTADO_OK);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CargoSigDAOImpl.registroCargoSig";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return respuesta;
	}

	// Modifica
	@Override
	public RespuestaBean ModifCargoSig(Map<String, Object> requestParm) throws GmdException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String Username = ((UserAuth) principal).getUsername();

		Map<String, Object> out = null;
		this.error = null;
		RespuestaBean respuesta = new RespuestaBean();
		List<CargoSig> listaDetalleCargoSig = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_MAT_MODIFICACION_CARGO_SIG")
					.declareParameters(new SqlParameter("i_cargo_sig", OracleTypes.VARCHAR),
							new SqlParameter("i_sigla", OracleTypes.VARCHAR),
							new SqlParameter("i_colaborador", OracleTypes.INTEGER),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_indicador", OracleTypes.INTEGER),
							new SqlParameter("i_idcargo_sig", OracleTypes.INTEGER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource().addValue("i_cargo_sig", requestParm.get("cargosig"))
					.addValue("i_sigla", requestParm.get("sigla")).addValue("i_vusuario", Username)
					.addValue("i_colaborador", requestParm.get("nroficha"))
					.addValue("i_indicador", requestParm.get("indicador"))
					.addValue("i_idcargo_sig", requestParm.get("idCargoSig"));
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				respuesta.setEstadoRespuesta(Constantes.RESULTADO_OK);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CargoSigDAOImpl.ModifCargoSig";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return respuesta;
	}

}
