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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.ICursoDAO;
import pe.com.sedapal.agi.dao.ISesionDAO;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.CursoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class SesionDAOImpl implements ISesionDAO{
	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

	public JdbcTemplate getJdbc() {
		return jdbc;
	}

	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public SimpleJdbcCall getJdbcCall() {
		return jdbcCall;
	}

	public void setJdbcCall(SimpleJdbcCall jdbcCall) {
		this.jdbcCall = jdbcCall;
	}

	public Paginacion getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(Paginacion paginacion) {
		this.paginacion = paginacion;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	
	private static final Logger LOGGER = Logger.getLogger(SesionDAOImpl.class);	

	public Sesion obtenerDatosSesionCurso(Long idCurso, Long idSesion) {
		Sesion sesion = null;
		Map<String, Object> out = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_OBTENER_DATOS_SESION")
					.declareParameters(
							new SqlParameter("i_nidcurs", OracleTypes.NUMBER), 
							new SqlParameter("i_nidsesi", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcurs", idCurso).addValue("i_nidsesi", idSesion);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				sesion = mapearDatosSesion(out);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.obtenerCursoSesionId";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		
		return sesion;
	}
	
	public Sesion mapearDatosSesion(Map<String, Object> resultado) {
		Sesion sesion = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		Sesion item = null;
		for (Map<String, Object> map : lista) {
			item = new Sesion();
			item.setIdCurso(((BigDecimal) map.get("n_idcurs")).longValue());
			item.setNombreSesion((String) map.get("v_nomsesi"));
			item.setIdSesion(((BigDecimal) map.get("n_idsesi")).longValue());
			item.setDuracion(((BigDecimal) map.get("n_dursesi")).longValue());
			item.setDisponibilidad(((BigDecimal)map.get("n_dissesi")).longValue());
			sesion = item;
		}
		return sesion;
	}
	
	
}
