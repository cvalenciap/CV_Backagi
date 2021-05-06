package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
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
import pe.com.sedapal.agi.dao.IAuditoriaDAO;
import pe.com.sedapal.agi.model.AuditorAuditoria;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.ConsideracionPlan;
import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.DetalleAuditoria;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.RequisitoAuditoriaDetalle;
import pe.com.sedapal.agi.model.request_objects.AuditoriaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.util.Constantes;
import pe.com.sedapal.agi.util.RespuestaBean;
import pe.com.sedapal.agi.util.UConstante;
import org.apache.log4j.Logger;

@Repository
public class AuditoriaDAOImpl implements IAuditoriaDAO {

	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

	private static final Logger LOGGER = Logger.getLogger(AuditoriaDAOImpl.class);

	Constantes Constante;

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
	public Auditoria obtenerAuditoriaPorId(Long idAuditoria) {
		Map<String, Object> out = null;
		List<Auditoria> listaAuditoria = new ArrayList<>();
		Auditoria auditoria = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_OBTENER_DATOS")
					.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", idAuditoria);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaAuditoria = mapearAuditoria(out);
				if (listaAuditoria.size() > 0) {
					auditoria = listaAuditoria.get(0);
				}

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerAuditoriaPorId";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return auditoria;

	}

	private List<Auditoria> mapearAuditoria(Map<String, Object> resultados) {
		List<Auditoria> listaAuditoria = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		Auditoria item = null;
		for (Map<String, Object> map : lista) {
			item = new Auditoria();
			item.setPrograma(new Programa());
			if (map.get("n_idprograma") != null) {
				item.setIdPrograma(((BigDecimal) map.get("n_idprograma")).longValue());
				item.getPrograma().setIdPrograma(((BigDecimal) map.get("n_idprograma")).longValue());
			}
			if (map.get("v_mes") != null) {
				item.setMes((String) map.get("v_mes"));
			}
			if (map.get("v_desgere") != null) {
				item.setGerencia((String) map.get("v_desgere"));
			}
			if (map.get("v_desequi") != null) {
				item.setEquipo((String) map.get("v_desequi"));
			}
			if (map.get("v_descarg") != null) {
				item.setCargo((String) map.get("v_descarg"));
			}
			if (map.get("v_descomi") != null) {
				item.setComite((String) map.get("v_descomi"));
			}
			if (map.get("n_idauditoria") != null) {
				item.setIdAuditoria(((BigDecimal) map.get("n_idauditoria")).longValue());
			}
			if (map.get("v_tipaud") != null) {
				item.setTipoAuditoria((String) map.get("v_tipaud"));
			}
			if (map.get("v_destipaud") != null) {
				item.setDescripcionTipoAuditoria((String) map.get("v_destipaud"));
			}

			if (map.get("a_d_feccre") != null) {
				DatosAuditoria datosAuditoria = new DatosAuditoria();
				datosAuditoria.setFechaCreacion(new Date(((Timestamp) map.get("a_d_feccre")).getTime()));
				item.setDatosAuditoria(datosAuditoria);
			}
			if (map.get("v_desaud") != null) {
				item.setDescripcionAuditoria((String) map.get("v_desaud"));
			}
			if (map.get("v_estfluaud") != null) {
				item.setEstadoAuditoria((String) map.get("v_estfluaud"));
			}

			if (map.get("d_fecinic") != null) {
				item.setFechaInicio(new Date(((Timestamp) map.get("d_fecinic")).getTime()));
			}
			if (map.get("d_fecfin") != null) {
				item.setFechaFin(new Date(((Timestamp) map.get("d_fecfin")).getTime()));
			}

			if (map.get("v_objaud") != null) {
				item.setObjetivo((String) map.get("v_objaud"));
			}

			if (map.get("v_alcaud") != null) {
				item.setAlcance((String) map.get("v_alcaud"));
			}

			if (map.get("v_desrech") != null) {
				item.setRechazoAuditoria((String) map.get("v_desrech"));
			}

			if (map.get("v_cicaud") != null) {
				item.setCicloAuditoria((String) map.get("v_cicaud"));
			}

			if (map.get("v_estfluaud") != null) {
				item.setEstadoAuditoria((String) map.get("v_estfluaud"));
			}

			if (map.get("auditorLider") != null) {
				item.setAuditorLider((String) map.get("auditorLider"));
			}

			if (map.get("n_idcola") != null) {
				item.setIdColaborador(((BigDecimal) map.get("n_idcola")).longValue());
			}

			if (map.get("observadorLiderGrupo") != null) {
				item.setObservadorLiderGrupo((String) map.get("observadorLiderGrupo"));
			}

			if (map.get("n_idobslider") != null) {
				item.setIdObservador(((BigDecimal) map.get("n_idobslider")).longValue());
			}

			if (map.get("d_fecha") != null) {
				item.getPrograma().setFechaPrograma(new Date(((Timestamp) map.get("d_fecha")).getTime()));
			}

			if (map.get("v_despro") != null) {
				item.getPrograma().setDescripcion((String) map.get("v_despro"));
			}

			listaAuditoria.add(item);

			if (map.get("RESULT_COUNT") != null) {
				this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
			}

		}

		return listaAuditoria;
	}

	@Override
	public List<Auditoria> obtenerListaAuditorias(AuditoriaRequest auditoriaRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Auditoria> listaAuditoria = null;
		this.error = null;

		this.paginacion = new Paginacion();

		this.paginacion.setPagina(pageRequest.getPagina());
		this.paginacion.setRegistros(pageRequest.getRegistros());
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_OBTENER")
					.declareParameters(new SqlParameter("i_idprograma", OracleTypes.NUMBER),
							new SqlParameter("i_vtipo", OracleTypes.VARCHAR),
							new SqlParameter("i_vestado", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idprograma", null)
					.addValue("i_vtipo", auditoriaRequest.getTipo()).addValue("i_vestado", auditoriaRequest.getEstado())
					.addValue("i_npagina", pageRequest.getPagina())
					.addValue("i_nregistros", pageRequest.getRegistros());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaAuditoria = new ArrayList<>();
				listaAuditoria = mapearAuditoria(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerListaAuditorias";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return listaAuditoria;
	}

	public List<Auditoria> obtenerListaAuditoriasPrograma(Long idPrograma) {
		Map<String, Object> out = null;
		List<Auditoria> listaAuditoria = null;
		this.error = null;
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_OBTENER")
					.declareParameters(new SqlParameter("i_idprograma", OracleTypes.NUMBER),
							new SqlParameter("i_vtipo", OracleTypes.VARCHAR),
							new SqlParameter("i_vestado", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idprograma", idPrograma)
					.addValue("i_vtipo", null).addValue("i_vestado", null).addValue("i_npagina", 0)
					.addValue("i_nregistros", 0);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaAuditoria = new ArrayList<>();
				listaAuditoria = mapearAuditoria(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerListaAuditoriasPrograma";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return listaAuditoria;
	}
	
	@Override
	public boolean registrarNormaAuditoria(Norma norma, Auditoria auditoria) {
		boolean registro = false;
		return registro;
	}

	/*
	@Override
	public boolean registrarNormaAuditoria(Norma norma, Auditoria auditoria) {
		// TODO Auto-generated method stub
		Map<String, Object> out = null;
		this.error = null;
		boolean registro = false;
		try {
			
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_NORMA_GUARDAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_vestado", OracleTypes.VARCHAR),
							new SqlParameter("i_idnorma", OracleTypes.NUMBER),
							new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", auditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_vestado", "1").addValue("i_idnorma", norma.getV_nom_norma())
					.addValue("i_idauditoria", auditoria.getIdAuditoria());
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_AUDITORIA_NORMA_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_vestado",		OracleTypes.VARCHAR),
							new SqlParameter("i_idnorma",	OracleTypes.NUMBER),
							new SqlParameter("i_idauditoria",			OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =	new MapSqlParameterSource()
						.addValue("i_vusuario",	auditoria.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_vestado",	"1")
			//			.addValue("i_idnorma",	norma.getIdNorma())
						.addValue("i_idauditoria",	auditoria.getIdAuditoria());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				registro = false;
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarNormaAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
			registro = false;
		}

		return registro;

	}
	*/

	@Override
	public List<Norma> obtenerNormasAuditoria(Long idAuditoria) {
		Map<String, Object> out = null;
		this.error = null;
		List<Norma> listaNormas = new ArrayList<>();
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_NORMA_OBTENER")
					.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", idAuditoria);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaNormas = new ArrayList<>();
				listaNormas = mapearNorma(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerNormasAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return listaNormas;
	}

	private List<Norma> mapearNorma(Map<String, Object> resultadoMapa) {
		List<Norma> listaNormas = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultadoMapa.get("o_cursor");
		Norma item = null;
		for (Map<String, Object> map : lista) {
			item = new Norma();
			if (map.get("n_idnorma") != null) {
		/*	if(map.get("n_idnorma") != null) {
				item.setIdNorma(((BigDecimal) map.get("n_idnorma")).longValue());
			}
			if (map.get("n_idauditoria") != null) {
				item.setIdAuditoria(((BigDecimal) map.get("n_idauditoria")).longValue());
			}

			if (map.get("v_desnorm") != null) {
				item.setDescripcionNorma((String) map.get("v_desnorm"));
			}			
			if(map.get("v_desnorm") != null) {
				item.setDescripcionNorma((String)map.get("v_desnorm"));
			}*/
			}
			listaNormas.add(item);			
		}
		return listaNormas;
	}

	@Override
	public boolean eliminarAuditoria(Auditoria auditoria) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_AUDITORIA_ELIMINAR")
				.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", auditoria.getIdAuditoria())
				.addValue("i_vusuario", auditoria.getDatosAuditoria().getUsuarioCreacion());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarAuditoria";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}

	}

	@Override
	public boolean eliminarNormasAuditoria(Auditoria auditoria) {
		Map<String, Object> out = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_NORMA_ELIMINAR")
					.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", auditoria.getIdAuditoria())
					.addValue("i_vusuario", auditoria.getDatosAuditoria().getUsuarioCreacion());
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarNormasAuditoria";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public void actualizarAuditoriasPrograma(Long idPrograma, String estado, String usuario) {
		Map<String, Object> out = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_PROGRAMA_UPDATE")
					.declareParameters(new SqlParameter("i_idprograma", OracleTypes.NUMBER),
							new SqlParameter("i_vcicaud", OracleTypes.VARCHAR),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idprograma", idPrograma)
					.addValue("i_vcicaud", estado).addValue("i_vusuario", usuario);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.actualizarAuditoriasPrograma";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
	}

	@Override
	public Auditoria registrarPlanAuditoria(Auditoria auditoria) {
		Map<String, Object> out = null;
		Long idAuditoria = null;
		Auditoria auditoriaRegistro = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_PLAN_AUDITORIA_GUARDAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_vtipo", OracleTypes.VARCHAR),
							new SqlParameter("i_vcicaud", OracleTypes.VARCHAR),
							new SqlParameter("i_dfecini", OracleTypes.DATE),
							new SqlParameter("i_dfecfin", OracleTypes.DATE),
							new SqlParameter("i_vdesaud", OracleTypes.VARCHAR),
							new SqlParameter("i_vobjaud", OracleTypes.VARCHAR),
							new SqlParameter("i_valcaud", OracleTypes.VARCHAR),
							new SqlParameter("i_nidaudlid", OracleTypes.NUMBER),
							new SqlParameter("i_nidobslid", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_idauditoria", OracleTypes.NUMBER));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", auditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_vtipo", auditoria.getTipoAuditoria()).addValue("i_vcicaud", "2")
					.addValue("i_dfecini", auditoria.getFechaInicio()).addValue("i_dfecfin", auditoria.getFechaFin())
					.addValue("i_vdesaud", auditoria.getDescripcionAuditoria())
					.addValue("i_vobjaud", auditoria.getObjetivo()).addValue("i_valcaud", auditoria.getAlcance())
					.addValue("i_nidaudlid", auditoria.getIdColaborador())
					.addValue("i_nidobslid", auditoria.getIdObservador());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				idAuditoria = ((BigDecimal) out.get("o_idauditoria")).longValue();
				auditoria.setIdAuditoria(idAuditoria);
				auditoriaRegistro = auditoria;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarPlanAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return auditoriaRegistro;
	}

	@Override
	public Auditoria actualizarAuditoria(Auditoria auditoria) {
		Map<String, Object> out = null;
		// Long idPrograma = null;
		Auditoria auditoriaActualizar = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_UPDATE")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_vtipaud", OracleTypes.VARCHAR),
							new SqlParameter("i_dfecinic", OracleTypes.DATE),
							new SqlParameter("i_dfecfin", OracleTypes.DATE),
							new SqlParameter("i_vdesaud", OracleTypes.VARCHAR),
							new SqlParameter("i_vobjaud", OracleTypes.VARCHAR),
							new SqlParameter("i_valcaud", OracleTypes.VARCHAR),
							new SqlParameter("i_idcola", OracleTypes.NUMBER),
							new SqlParameter("i_idobslider", OracleTypes.NUMBER),
							new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlParameter("i_vestaud", OracleTypes.VARCHAR),
							new SqlParameter("i_vcicaud", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", auditoria.getDatosAuditoria().getUsuarioModificacion())
					.addValue("i_vtipaud", auditoria.getTipoAuditoria())
					.addValue("i_dfecinic", auditoria.getFechaInicio()).addValue("i_dfecfin", auditoria.getFechaFin())
					.addValue("i_vdesaud", auditoria.getDescripcionAuditoria())
					.addValue("i_vobjaud", auditoria.getObjetivo()).addValue("i_valcaud", auditoria.getAlcance())
					.addValue("i_idcola", auditoria.getIdColaborador())
					.addValue("i_idobslider", auditoria.getIdObservador())
					.addValue("i_idauditoria", auditoria.getIdAuditoria())
					.addValue("i_vestaud", auditoria.getEstadoAuditoria())
					.addValue("i_vcicaud", auditoria.getCicloAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				auditoriaActualizar = auditoria;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.actualizarAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return auditoriaActualizar;
	}

	@Override
	public Auditoria registrarDatosPlanAuditoria(Auditoria auditoria) {
		// TODO Auto-generated method stub
		Auditoria registroAuditoria = null;
		try {
			registroAuditoria = this.registrarPlanAuditoria(auditoria);
			if (registroAuditoria.getListaConsideracionesPlan().size() > 0) {
				for (ConsideracionPlan consideracion : auditoria.getListaConsideracionesPlan()) {
					boolean registro = false;
					if (consideracion.getEstadoRegistro().equals("1")) {
						consideracion.setIdAuditoria(registroAuditoria.getIdAuditoria());
						consideracion.setDatosAuditoria(auditoria.getDatosAuditoria());
						registro = this.registrarConsideracionesAuditoria(consideracion);
					} else {
						registro = true;
					}
					if (!registro) {
						break;
					}
				}
			}

			if (registroAuditoria.getListaCriterios().size() > 0) {
				for (CriterioResultado criterio : auditoria.getListaCriterios()) {
					boolean registro = false;
					if (criterio.getEstadoRegistro().equals("1")) {
						criterio.setIdAuditoria(registroAuditoria.getIdAuditoria());
						criterio.setDatosAuditoria(auditoria.getDatosAuditoria());
						registro = this.registrarCriteriosAuditoria(criterio);
					} else {
						registro = true;
					}
					if (!registro) {
						break;
					}
				}
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarDatosPlanAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registroAuditoria;
	}

	@Override
	public boolean registrarConsideracionesAuditoria(ConsideracionPlan consideracion) {
		Map<String, Object> out = null;
		boolean registro = false;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_CONSIDE_GUARDAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_vestado", OracleTypes.VARCHAR),
							new SqlParameter("i_vdescons", OracleTypes.VARCHAR),
							new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", consideracion.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_vestado", "1").addValue("i_vdescons", consideracion.getTextoConsideracion())
					.addValue("i_idauditoria", consideracion.getIdAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarConsideracionesAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registro;
	}

	@Override
	public boolean registrarCriteriosAuditoria(CriterioResultado criterio) {
		Map<String, Object> out = null;
		boolean registro = false;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_CRITERIO_GUARDAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_vestado", OracleTypes.VARCHAR),
							new SqlParameter("i_vcriterio", OracleTypes.VARCHAR),
							new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", criterio.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_vestado", "1").addValue("i_vcriterio", criterio.getValorCriterio())
					.addValue("i_idauditoria", criterio.getIdAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarCriteriosAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registro;
	}

	@Override
	public List<ConsideracionPlan> obtenerListaConsideracionesAuditoria(Long idAuditoria) {
		Map<String, Object> out = null;
		this.error = null;
		List<ConsideracionPlan> listaConsideracionPlan = null;
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_OBTENER_CONS")
					.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", idAuditoria);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaConsideracionPlan = new ArrayList<>();
				listaConsideracionPlan = mapearConsideracion(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerListaConsideracionesAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return listaConsideracionPlan;
	}

	@Override
	public List<CriterioResultado> obtenerListaCriteriosAuditoria(Long idAuditoria) {
		Map<String, Object> out = null;
		this.error = null;
		List<CriterioResultado> listaCriteriosAuditoria = null;
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITORIA_OBTENER_CRIT")
					.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", idAuditoria);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaCriteriosAuditoria = new ArrayList<>();
				listaCriteriosAuditoria = mapearCriterios(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerListaCriteriosAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return listaCriteriosAuditoria;
	}

	private List<ConsideracionPlan> mapearConsideracion(Map<String, Object> resultados) {
		List<ConsideracionPlan> listaConsideracion = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		ConsideracionPlan item = null;
		for (Map<String, Object> map : lista) {
			item = new ConsideracionPlan();
			if (map.get("n_idconsideracion") != null) {
				item.setIdConsideracion(((BigDecimal) map.get("n_idconsideracion")).longValue());
			}
			if (map.get("v_descons") != null) {
				item.setTextoConsideracion((String) map.get("v_descons"));
			}
			if (map.get("v_estcons") != null) {
				item.setEstadoRegistro((String) map.get("v_estcons"));
			}
			if (map.get("n_idauditoria") != null) {
				item.setIdAuditoria(((BigDecimal) map.get("n_idauditoria")).longValue());
			}

			listaConsideracion.add(item);

		}

		return listaConsideracion;
	}

	private List<CriterioResultado> mapearCriterios(Map<String, Object> resultados) {
		List<CriterioResultado> listaCriterios = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		CriterioResultado item = null;
		for (Map<String, Object> map : lista) {
			item = new CriterioResultado();
			if (map.get("n_idcritresu") != null) {
				item.setIdCriterio(((BigDecimal) map.get("n_idcritresu")).longValue());
			}
			if (map.get("v_valcritresu") != null) {
				item.setValorCriterio((String) map.get("v_valcritresu"));
			}

			if (map.get("descripcionCriterio") != null) {
				item.setDescripcionCriterio((String) map.get("descripcionCriterio"));
			}
			if (map.get("v_estcritresu") != null) {
				item.setEstadoRegistro((String) map.get("v_estcritresu"));
			}
			if (map.get("n_idauditoria") != null) {
				item.setIdAuditoria(((BigDecimal) map.get("n_idauditoria")).longValue());
			}

			listaCriterios.add(item);

		}

		return listaCriterios;
	}

	@Override
	public Auditoria actualizarDatosPlanAuditoria(Auditoria auditoria) {
		Auditoria auditoriaModificacion = this.actualizarAuditoria(auditoria);
		for (ConsideracionPlan consideracion : auditoria.getListaConsideracionesPlan()) {
			boolean registro = false;
			if (consideracion.getEstadoRegistro().equals("1")) {
				if (consideracion.getIdConsideracion() == null) {
					consideracion.setIdAuditoria(auditoria.getIdAuditoria());
					consideracion.setDatosAuditoria(auditoria.getDatosAuditoria());
					registro = this.registrarConsideracionesAuditoria(consideracion);
					if (!registro) {
						break;
					}
				}
			} else if (consideracion.getEstadoRegistro().equals("0")) {
				if (consideracion.getIdConsideracion() != null) {
					consideracion.setDatosAuditoria(auditoria.getDatosAuditoria());
					registro = this.eliminarConsideracion(consideracion);
					if (!registro) {
						break;
					}
				}
			}
		}

		for (CriterioResultado criterio : auditoria.getListaCriterios()) {
			boolean registro = false;
			if (criterio.getEstadoRegistro().equals("1")) {
				if (criterio.getIdCriterio() == null) {
					criterio.setIdAuditoria(auditoria.getIdAuditoria());
					criterio.setDatosAuditoria(auditoria.getDatosAuditoria());
					registro = this.registrarCriteriosAuditoria(criterio);
					if (!registro) {
						break;
					}
				}
			} else if (criterio.getEstadoRegistro().equals("0")) {
				if (criterio.getIdCriterio() != null) {
					criterio.setDatosAuditoria(auditoria.getDatosAuditoria());
					registro = this.eliminarCriterio(criterio);
					if (!registro) {
						break;
					}
				}
			}

		}

		// Lista detalle
		List<DetalleAuditoria> listaDetalleBD = this.obtenerListaDetalleAuditoria(auditoria.getIdAuditoria());
		List<DetalleAuditoria> listaDetalleObtenida = auditoria.getListaDetalle();
		List<DetalleAuditoria> listaDetalleEliminar = new ArrayList<>();
		for (DetalleAuditoria detalleBD : listaDetalleBD) {
			boolean encontroDetalle = false;
			for (DetalleAuditoria detalleObt : listaDetalleObtenida) {
				if (detalleObt.getIdDetalleAuditoria() != null) {
					if (detalleObt.getIdDetalleAuditoria().equals(detalleBD.getIdDetalleAuditoria())) {
						encontroDetalle = true;
					}
				}
			}
			if (!encontroDetalle) {
				listaDetalleEliminar.add(detalleBD);
			}
		}

		for (DetalleAuditoria detalleAuditoria : auditoria.getListaDetalle()) {
			if (detalleAuditoria.getIdDetalleAuditoria() == null) {
				detalleAuditoria.setDatosAuditoria(auditoria.getDatosAuditoria());
				detalleAuditoria.setIdAuditoria(auditoria.getIdAuditoria());
				// DetalleAuditoria detalleAuditoriaObtenido =
				this.registrarDatosDetalleAuditoria(detalleAuditoria);
			} else {
				detalleAuditoria.setDatosAuditoria(auditoria.getDatosAuditoria());
				detalleAuditoria.setIdAuditoria(auditoria.getIdAuditoria());
				// DetalleAuditoria detalleAuditoriaObt =
				this.actualizarDatosDetalleAuditoria(detalleAuditoria);
			}
		}

		// Eliminar detalle
		boolean elimino = false;
		for (DetalleAuditoria detalleEliminar : listaDetalleEliminar) {
			detalleEliminar.setDatosAuditoria(auditoria.getDatosAuditoria());
			elimino = this.eliminarDatosDetalleAuditoria(detalleEliminar);
			if (!elimino) {
				break;
			}
		}
		return auditoriaModificacion;
	}

	@Override
	public boolean eliminarConsideracion(ConsideracionPlan consideracion) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_CONSIDERACIONES_ELIMINAR")
				.declareParameters(new SqlParameter("i_idconsideracion", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_idconsideracion", consideracion.getIdConsideracion())
				.addValue("i_vusuario", consideracion.getDatosAuditoria().getUsuarioCreacion());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {

				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarConsideracion";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean eliminarCriterio(CriterioResultado criterio) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_CRITERIOS_ELIMINAR")
				.declareParameters(new SqlParameter("i_idcritresu", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("i_idcritresu", criterio.getIdCriterio())
				.addValue("i_vusuario", criterio.getDatosAuditoria().getUsuarioCreacion());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {

				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarCriterio";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean eliminarAuditoriaExterna(Auditoria auditoria) {
		boolean elimino = false;
		elimino = this.eliminarAuditoria(auditoria);
		if (elimino) {
			elimino = this.eliminarCriteriosAuditoria(auditoria);
			elimino = this.eliminarConsideracionesAuditoria(auditoria);
		}
		return elimino;
	}

	@Override
	public boolean eliminarCriteriosAuditoria(Auditoria auditoria) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_CRIT_AUDIT_ELIMINAR")
				.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", auditoria.getIdAuditoria())
				.addValue("i_vusuario", auditoria.getDatosAuditoria().getUsuarioCreacion());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {

				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);

				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarCriteriosAuditoria";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean eliminarConsideracionesAuditoria(Auditoria auditoria) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_CONS_AUDIT_ELIMINAR")
				.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", auditoria.getIdAuditoria())
				.addValue("i_vusuario", auditoria.getDatosAuditoria().getUsuarioCreacion());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {

				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarConsideracionesAuditoria";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public DetalleAuditoria registrarDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		Map<String, Object> out = null;
		Long idDetalleAuditoria = null;
		DetalleAuditoria detalleAuditoriaRegistro = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETALLEAUD_GUARDAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_vdesalca", OracleTypes.VARCHAR),
							new SqlParameter("i_vdesgere", OracleTypes.VARCHAR),
							new SqlParameter("i_vdesequi", OracleTypes.VARCHAR),
							new SqlParameter("i_vdescarg", OracleTypes.VARCHAR),
							new SqlParameter("i_vdescomi", OracleTypes.VARCHAR),
							new SqlParameter("i_vestdetaudreq", OracleTypes.VARCHAR),
							new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlParameter("i_fecha", OracleTypes.DATE),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_iddetaaudreq", OracleTypes.NUMBER));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", detalleAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_vdesalca", detalleAuditoria.getAlcance())
					.addValue("i_vdesgere",
							detalleAuditoria.getValorTipoEntidad().equals("1")
									? detalleAuditoria.getValorEntidadGerencia()
									: null)
					.addValue("i_vdesequi",
							detalleAuditoria.getValorTipoEntidad().equals("2")
									? detalleAuditoria.getValorEntidadEquipo()
									: null)
					.addValue("i_vdescarg",
							detalleAuditoria.getValorTipoEntidad().equals("3") ? detalleAuditoria.getValorEntidadCargo()
									: null)
					.addValue("i_vdescomi", null).addValue("i_vestdetaudreq", "1")
					.addValue("i_idauditoria", detalleAuditoria.getIdAuditoria())
					.addValue("i_fecha", detalleAuditoria.getFecha());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				idDetalleAuditoria = ((BigDecimal) out.get("o_iddetaaudreq")).longValue();
				detalleAuditoria.setIdDetalleAuditoria(idDetalleAuditoria);
				detalleAuditoriaRegistro = detalleAuditoria;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return detalleAuditoriaRegistro;
	}

	@Override
	public DetalleAuditoria registrarDatosDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		boolean registro = false;
		DetalleAuditoria detalleAuditoriaObtenido = this.registrarDetalleAuditoria(detalleAuditoria);
		for (RequisitoAuditoriaDetalle requisitoAuditoria : detalleAuditoria.getListaRequisitos()) {
			requisitoAuditoria.setIdDetalleAuditoria(detalleAuditoriaObtenido.getIdDetalleAuditoria());
			requisitoAuditoria.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
			registro = this.registrarRequisitoAuditoriaDetalle(requisitoAuditoria);
			if (!registro) {
				break;
			}
		}

		for (AuditorAuditoria auditorAuditoria : detalleAuditoria.getListaParticipante()) {
			auditorAuditoria.setIdDetalleAuditoria(detalleAuditoriaObtenido.getIdDetalleAuditoria());
			auditorAuditoria.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
			registro = this.registrarAuditorAuditoriaDetalle(auditorAuditoria);
			if (!registro) {
				break;
			}
		}
		return detalleAuditoriaObtenido;
	}

	@Override
	public boolean registrarRequisitoAuditoriaDetalle(RequisitoAuditoriaDetalle requisitoAuditoria) {
		Map<String, Object> out = null;
		// RequisitoAuditoriaDetalle requisitoAuditoriaRegistro = null;
		boolean registro = false;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUD_REQ_GUARDAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_idrequisito", OracleTypes.NUMBER),
							new SqlParameter("i_idnorma", OracleTypes.NUMBER),
							new SqlParameter("i_iddetaudreq", OracleTypes.NUMBER),
							new SqlParameter("i_vestaudreq", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", requisitoAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_idrequisito", requisitoAuditoria.getId())
					.addValue("i_idnorma", requisitoAuditoria.getIdNorma())
					.addValue("i_iddetaudreq", requisitoAuditoria.getIdDetalleAuditoria())
					.addValue("i_vestaudreq", "1");

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				// requisitoAuditoriaRegistro = requisitoAuditoria;
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarRequisitoAuditoriaDetalle";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registro;
	}

	@Override
	public boolean registrarAuditorAuditoriaDetalle(AuditorAuditoria auditorAuditoria) {
		Map<String, Object> out = null;
		boolean registro = false;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETAUD_COL_GUARDAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_idcola", OracleTypes.NUMBER),
							new SqlParameter("i_iddetaudreq", OracleTypes.NUMBER),
							new SqlParameter("i_indresp", OracleTypes.VARCHAR),
							new SqlParameter("i_vestdetaudcol", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", auditorAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_idcola", auditorAuditoria.getIdAuditor())
					.addValue("i_iddetaudreq", auditorAuditoria.getIdDetalleAuditoria())
					.addValue("i_indresp", auditorAuditoria.getResponsable()).addValue("i_vestdetaudcol", "1");

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarRequisitoAuditoriaDetalle";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registro;
	}

	@Override
	public List<DetalleAuditoria> obtenerDetalleAuditoria(Long idAuditoria) {
		List<DetalleAuditoria> listaDetalleAuditoria = this.obtenerListaDetalleAuditoria(idAuditoria);
		for (DetalleAuditoria detalleAuditoria : listaDetalleAuditoria) {
			detalleAuditoria.setListaParticipante(
					this.obtenerAuditoresDetalleAuditoria(detalleAuditoria.getIdDetalleAuditoria()));
			detalleAuditoria.setListaRequisitos(
					this.obtenerRequisitosDetalleAuditoria(detalleAuditoria.getIdDetalleAuditoria()));
		}
		return listaDetalleAuditoria;
	}

	@Override
	public List<DetalleAuditoria> obtenerListaDetalleAuditoria(Long idAuditoria) {
		Map<String, Object> out = null;
		this.error = null;
		List<DetalleAuditoria> listaDetalleAuditoria = null;
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETALLEAUD_OBTENER")
					.declareParameters(new SqlParameter("i_idauditoria", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_idauditoria", idAuditoria);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaDetalleAuditoria = new ArrayList<>();
				listaDetalleAuditoria = mapearDetallesAuditoria(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerListaDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return listaDetalleAuditoria;
	}

	private List<DetalleAuditoria> mapearDetallesAuditoria(Map<String, Object> resultados) {
		List<DetalleAuditoria> listaDetalleAuditoria = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		DetalleAuditoria item = null;
		for (Map<String, Object> map : lista) {
			item = new DetalleAuditoria();
			if (map.get("n_iddetaudreq") != null) {
				item.setIdDetalleAuditoria(((BigDecimal) map.get("n_iddetaudreq")).longValue());
			}
			if (map.get("v_desalca") != null) {
				item.setAlcance((String) map.get("v_desalca"));
			}
			if (map.get("v_desgere") != null) {
				item.setValorEntidadGerencia((String) map.get("v_desgere"));
			}
			if (map.get("v_desequi") != null) {
				item.setValorEntidadEquipo((String) map.get("v_desequi"));
			}
			if (map.get("v_descarg") != null) {
				item.setValorEntidadCargo((String) map.get("v_descarg"));
			}
			if (map.get("v_descomi") != null) {
				item.setValorEntidadComite((String) map.get("v_descomi"));
			}
			if (map.get("d_fecha") != null) {
				item.setFecha(new Date(((Timestamp) map.get("d_fecha")).getTime()));
			}
			if (map.get("n_idauditoria") != null) {
				item.setIdAuditoria(((BigDecimal) map.get("n_idauditoria")).longValue());
			}

			if (map.get("v_desgere") != null) {
				item.setValorTipoEntidad("1");
			} else if (map.get("v_desequi") != null) {
				item.setValorTipoEntidad("2");
			} else if (map.get("v_descarg") != null) {
				item.setValorTipoEntidad("3");
			} else {
				item.setValorTipoEntidad("4");
			}

			listaDetalleAuditoria.add(item);

		}

		return listaDetalleAuditoria;
	}

	@Override
	public List<AuditorAuditoria> obtenerAuditoresDetalleAuditoria(Long idDetalleAuditoria) {
		Map<String, Object> out = null;
		this.error = null;
		List<AuditorAuditoria> listaAuditoresAuditoria = null;
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETAUD_COL_OBTENER")
					.declareParameters(new SqlParameter("i_iddetaudreq", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_iddetaudreq", idDetalleAuditoria);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaAuditoresAuditoria = new ArrayList<>();
				listaAuditoresAuditoria = mapearAuditoresAuditoria(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerListaDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return listaAuditoresAuditoria;
	}

	private List<AuditorAuditoria> mapearAuditoresAuditoria(Map<String, Object> resultados) {
		List<AuditorAuditoria> listaAuditoresAuditoria = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		AuditorAuditoria item = null;
		for (Map<String, Object> map : lista) {
			item = new AuditorAuditoria();
			if (map.get("n_iddetaudcol") != null) {
				item.setIdAuditorAuditoria(((BigDecimal) map.get("n_iddetaudcol")).longValue());
			}
			if (map.get("n_iddetaaudreq") != null) {
				item.setIdDetalleAuditoria(((BigDecimal) map.get("n_iddetaaudreq")).longValue());
			}
			if (map.get("n_idcola") != null) {
				item.setIdAuditor(((BigDecimal) map.get("n_idcola")).longValue());
			}
			if (map.get("v_indresp") != null) {
				item.setResponsable(((String) map.get("v_indresp")));
			}
			if (map.get("cargoauditor") != null) {
				item.setCargoAuditor((String) map.get("cargoauditor"));
			}

			if (map.get("idrolauditor") != null) {
				item.setIdRolAuditor(((BigDecimal) map.get("idrolauditor")).longValue());
			}
			if (map.get("nombreauditor") != null) {
				item.setNombreAuditor((String) map.get("nombreauditor"));
			}

			listaAuditoresAuditoria.add(item);

		}

		return listaAuditoresAuditoria;
	}

	@Override
	public List<RequisitoAuditoriaDetalle> obtenerRequisitosDetalleAuditoria(Long idDetalleAuditoria) {
		Map<String, Object> out = null;
		this.error = null;
		List<RequisitoAuditoriaDetalle> listaRequisitosAuditoria = null;
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUD_REQ_OBTENER")
					.declareParameters(new SqlParameter("i_iddetaudreq", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_iddetaudreq", idDetalleAuditoria);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaRequisitosAuditoria = new ArrayList<>();
				listaRequisitosAuditoria = mapearRequisitosAuditoria(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");

				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.obtenerListaDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return listaRequisitosAuditoria;
	}

	private List<RequisitoAuditoriaDetalle> mapearRequisitosAuditoria(Map<String, Object> resultados) {
		List<RequisitoAuditoriaDetalle> listaRequisitosAuditoria = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		RequisitoAuditoriaDetalle item = null;
		for (Map<String, Object> map : lista) {
			item = new RequisitoAuditoriaDetalle();
			if (map.get("n_idaudreq") != null) {
				item.setIdRequisitoDetalle(((BigDecimal) map.get("n_idaudreq")).longValue());
			}

			if (map.get("n_iddetaudreq") != null) {
				item.setIdDetalleAuditoria(((BigDecimal) map.get("n_iddetaudreq")).longValue());
			}
			if (map.get("n_idrequisito") != null) {
				item.setId(((BigDecimal) map.get("n_idrequisito")).longValue());
			}

			if (map.get("nombrereq") != null) {
				item.setNombre((String) map.get("nombrereq"));
			}

			if (map.get("n_idnorma") != null) {
				item.setIdNorma(((BigDecimal) map.get("n_idnorma")).longValue());
			}

			listaRequisitosAuditoria.add(item);

		}

		return listaRequisitosAuditoria;
	}

	@Override
	public DetalleAuditoria actualizarDatosDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		DetalleAuditoria detalleAuditoriaObtenido = this.actualizarDetalleAuditoria(detalleAuditoria);
		if (detalleAuditoriaObtenido != null) {
			// MODIFICACION LISTA DE REQUISITOS
			List<RequisitoAuditoriaDetalle> listaRequisitosBD = this
					.obtenerRequisitosDetalleAuditoria(detalleAuditoria.getIdDetalleAuditoria());
			List<RequisitoAuditoriaDetalle> listaRequisitosNuevos = new ArrayList<>();
			List<RequisitoAuditoriaDetalle> listaRequisitosEliminar = new ArrayList<>();
			List<RequisitoAuditoriaDetalle> listaRequisitosObtenidos = detalleAuditoria.getListaRequisitos();
			for (RequisitoAuditoriaDetalle requisitoObtenido : listaRequisitosObtenidos) {
				boolean encontro = false;
				for (RequisitoAuditoriaDetalle requisitoBD : listaRequisitosBD) {
					if (requisitoBD.getId().equals(requisitoObtenido.getId())) {
						encontro = true;
						break;
					}
				}
				if (!encontro) {
					listaRequisitosNuevos.add(requisitoObtenido);
				}
			}

			for (RequisitoAuditoriaDetalle requisitoBDElim : listaRequisitosBD) {
				boolean encontroBD = false;
				for (RequisitoAuditoriaDetalle requisitoObtenidoAux : listaRequisitosObtenidos) {
					if (requisitoObtenidoAux.getId().equals(requisitoBDElim.getId())) {
						encontroBD = true;
						break;
					}
				}
				if (!encontroBD) {
					listaRequisitosEliminar.add(requisitoBDElim);
				}

			}
			boolean elimino = false;
			for (RequisitoAuditoriaDetalle requisitoEliminado : listaRequisitosEliminar) {
				requisitoEliminado.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
				elimino = this.eliminarRequisitoAuditoriaDetalle(requisitoEliminado);
				if (!elimino) {
					break;
				}
			}

			boolean registro = false;
			for (RequisitoAuditoriaDetalle requisitoNuevo : listaRequisitosNuevos) {
				requisitoNuevo.setIdDetalleAuditoria(detalleAuditoria.getIdDetalleAuditoria());
				requisitoNuevo.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
				registro = this.registrarRequisitoAuditoriaDetalle(requisitoNuevo);
				if (!registro) {
					break;
				}
			}

			// MODIFICACION LISTA DE PARTICIPANTES
			List<AuditorAuditoria> listaAuditoresBD = this
					.obtenerAuditoresDetalleAuditoria(detalleAuditoria.getIdDetalleAuditoria());
			List<AuditorAuditoria> listaAuditoresObtenida = detalleAuditoria.getListaParticipante();
			List<AuditorAuditoria> listaAuditoresEliminar = new ArrayList<>();
			List<AuditorAuditoria> listaAuditoresAgregar = new ArrayList<>();
			List<AuditorAuditoria> listaAuditoresModificar = new ArrayList<>();

			for (AuditorAuditoria auditorBD : listaAuditoresBD) {
				boolean encontroAuditorElim = false;
				for (AuditorAuditoria auditorObtenido : listaAuditoresObtenida) {
					if (auditorBD.getIdAuditor().equals(auditorObtenido.getIdAuditor())) {
						encontroAuditorElim = true;
						break;
					}
				}
				if (!encontroAuditorElim) {
					listaAuditoresEliminar.add(auditorBD);
				}
			}

			for (AuditorAuditoria auditorObtenidoAux : listaAuditoresObtenida) {
				boolean encontroAuditorAgr = false;
				for (AuditorAuditoria auditorBDAux : listaAuditoresBD) {
					if (auditorBDAux.getIdAuditor().equals(auditorObtenidoAux.getIdAuditor())) {
						encontroAuditorAgr = true;
						AuditorAuditoria auditorModificar = auditorBDAux;
						auditorModificar.setResponsable(auditorObtenidoAux.getResponsable());
						listaAuditoresModificar.add(auditorModificar);
						break;
					}
				}
				if (!encontroAuditorAgr) {
					listaAuditoresAgregar.add(auditorObtenidoAux);
				}
			}

			boolean eliminoAud = false;
			for (AuditorAuditoria auditorElim : listaAuditoresEliminar) {
				auditorElim.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
				eliminoAud = this.eliminarAuditorAuditoriaDetalle(auditorElim);
				if (!eliminoAud) {
					break;
				}
			}

			boolean registroAud = false;
			for (AuditorAuditoria auditorGrabar : listaAuditoresAgregar) {
				auditorGrabar.setIdDetalleAuditoria(detalleAuditoria.getIdDetalleAuditoria());
				auditorGrabar.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
				registroAud = this.registrarAuditorAuditoriaDetalle(auditorGrabar);
				if (!registroAud) {
					break;
				}
			}

			boolean modificoAud = false;
			for (AuditorAuditoria auditorModif : listaAuditoresModificar) {
				auditorModif.setDatosAuditoria(detalleAuditoria.getDatosAuditoria());
				modificoAud = this.actualizarAuditorAuditoriaDetalle(auditorModif);
				if (!modificoAud) {
					break;
				}
			}

		}
		return detalleAuditoriaObtenido;
	}

	@Override
	public DetalleAuditoria actualizarDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		Map<String, Object> out = null;
		DetalleAuditoria detalleAuditoriaObtenido = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETALLEAUD_UPDATE")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_vdesalca", OracleTypes.VARCHAR),
							new SqlParameter("i_vdesgere", OracleTypes.VARCHAR),
							new SqlParameter("i_vdesequi", OracleTypes.VARCHAR),
							new SqlParameter("i_vdescarg", OracleTypes.VARCHAR),
							new SqlParameter("i_vdescomi", OracleTypes.VARCHAR),
							new SqlParameter("i_fecha", OracleTypes.DATE),
							new SqlParameter("i_iddetaaudreq", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", detalleAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_vdesalca", detalleAuditoria.getAlcance())
					.addValue("i_vdesgere",
							detalleAuditoria.getValorTipoEntidad().equals("1")
									? detalleAuditoria.getValorEntidadGerencia()
									: null)
					.addValue("i_vdesequi",
							detalleAuditoria.getValorTipoEntidad().equals("2")
									? detalleAuditoria.getValorEntidadEquipo()
									: null)
					.addValue("i_vdescarg",
							detalleAuditoria.getValorTipoEntidad().equals("3") ? detalleAuditoria.getValorEntidadCargo()
									: null)
					.addValue("i_vdescomi", null).addValue("i_fecha", detalleAuditoria.getFecha())
					.addValue("i_iddetaaudreq", detalleAuditoria.getIdDetalleAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				detalleAuditoriaObtenido = detalleAuditoria;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.registrarDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return detalleAuditoriaObtenido;
	}

	@Override
	public boolean eliminarRequisitoAuditoriaDetalle(RequisitoAuditoriaDetalle requisitoAuditoria) {
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUD_REQ_ELIMINAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_idaudreq", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", requisitoAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_idaudreq", requisitoAuditoria.getIdRequisitoDetalle());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarRequisitoAuditoriaDetalle";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean actualizarAuditorAuditoriaDetalle(AuditorAuditoria auditorAuditoria) {
		Map<String, Object> out = null;
		boolean registro = false;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETAUD_COL_UPDATE")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_iddetaudcol", OracleTypes.NUMBER),
							new SqlParameter("i_indresp", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", auditorAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_iddetaudcol", auditorAuditoria.getIdAuditorAuditoria())
					.addValue("i_indresp", auditorAuditoria.getResponsable());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.actualizarAuditorAuditoriaDetalle";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registro;
	}

	@Override
	public boolean eliminarAuditorAuditoriaDetalle(AuditorAuditoria auditorAuditoria) {
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETAUD_COL_ELIMINAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_iddetaudcol", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", auditorAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_iddetaudcol", auditorAuditoria.getIdAuditorAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarAuditorAuditoriaDetalle";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean eliminarDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_DETALLEAUD_ELIMINAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_iddetaudreq", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", detalleAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_iddetaaudreq", detalleAuditoria.getIdDetalleAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean eliminarRequisitosDeDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_REQ_AUDDET_ELIMINAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_iddetaudreq", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", detalleAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_iddetaudreq", detalleAuditoria.getIdDetalleAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarRequisitosDeDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean eliminarAuditoresDeDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_COL_AUDDET_ELIMINAR")
					.declareParameters(new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_iddetaudreq", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", detalleAuditoria.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_iddetaudreq", detalleAuditoria.getIdDetalleAuditoria());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en AuditoriaDAOImpl.eliminarRequisitosDeDetalleAuditoria";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	@Override
	public boolean eliminarDatosDetalleAuditoria(DetalleAuditoria detalleAuditoria) {
		// TODO Auto-generated method stub
		boolean eliminoRequisitos = this.eliminarRequisitosDeDetalleAuditoria(detalleAuditoria);
		boolean eliminoAuditores = false;
		boolean eliminoDetalle = false;
		if (eliminoRequisitos) {
			eliminoAuditores = this.eliminarAuditoresDeDetalleAuditoria(detalleAuditoria);
		}
		if (eliminoAuditores) {
			eliminoDetalle = this.eliminarDetalleAuditoria(detalleAuditoria);
		}

		return eliminoDetalle;
	}

}
