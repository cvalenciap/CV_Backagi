package pe.com.sedapal.agi.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import pe.com.sedapal.agi.dao.IFichaAudiDAO;
import pe.com.sedapal.agi.model.CursoAuditor;
import pe.com.sedapal.agi.model.CursoNorma;
import pe.com.sedapal.agi.model.FichaAudi;
import pe.com.sedapal.agi.model.GenericParam;
import pe.com.sedapal.agi.model.request_objects.FichaAudiRequest;
import pe.com.sedapal.agi.model.request_objects.InfoAuditorRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.ListaPaginada;
import pe.com.sedapal.agi.util.CastUtil;
import pe.com.sedapal.agi.util.Constantes.MantFichaAuditor;
import pe.com.sedapal.agi.util.DBConstants;

@Repository
public class FichaAudiDAOImpl implements IFichaAudiDAO {

	private static final Logger LOGGER = Logger.getLogger(FichaAudiDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;

	private Error error;

	public Error getError() {
		return this.error;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Object>> obtenerParametros() {
		Map<String, Object> out = null;
		Map<String, List<Object>> parametrosSalida = new LinkedHashMap<>();
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_OBTENER_PARAMS_REG_AUDITOR)
					.withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlOutParameter("O_C_SALIDA", OracleTypes.CURSOR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			out = this.jdbcCall.execute();
			Integer resultOperacion = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (resultOperacion == 0) {

				if (out.get("O_C_SALIDA") != null) {
					List<Map<String, Object>> listaMaps = (List<Map<String, Object>>) out.get("O_C_SALIDA");

					Map<String, List<Map<String, Object>>> listaMapsParametros = listaMaps.stream()
							.collect(Collectors.groupingBy(m -> CastUtil.leerValorMapString(m, "tipoParam")));

					parametrosSalida.put("roles", listaMapsParametros.get("ROL").stream()
							.map(GenericParam::mapperParamString).collect(Collectors.toList()));

					parametrosSalida.put("educacion", listaMapsParametros.get("EDUCACION").stream()
							.map(GenericParam::mapperParamString).collect(Collectors.toList()));

					parametrosSalida.put("normas", listaMapsParametros.get("NORMA").stream()
							.map(GenericParam::mapperParamInteger).collect(Collectors.toList()));

					parametrosSalida.put("tipoAuditoria", listaMapsParametros.get("TIPOAUDITORIA").stream()
							.map(GenericParam::mapperParamString).collect(Collectors.toList()));

				} else {
					String mensaje = "Error al recuperar datos de consulta";
					String mensajeInterno = "Error al recuperar datos de consulta";
					this.error = new Error(resultOperacion, mensaje, mensajeInterno);
				}

			} else {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(resultOperacion, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.obtenerParametros";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return parametrosSalida;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FichaAudi> obtenerListaAuditores(FichaAudiRequest request) {
		Map<String, Object> out = null;
		List<FichaAudi> listaFichasAudi = new ArrayList<>();

		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_LIST_AUDITORES)
					.declareParameters(new SqlParameter("I_N_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOMBRE", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_PAT", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_MAT", OracleTypes.VARCHAR),
							new SqlParameter("I_V_COD_ROL", OracleTypes.VARCHAR),
							new SqlOutParameter("O_C_SALIDA", OracleTypes.CURSOR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_N_FICHA", request.getFicha())
					.addValue("I_V_NOMBRE", request.getNombre()).addValue("I_V_APE_PAT", request.getApePaterno())
					.addValue("I_V_APE_MAT", request.getApeMaterno())
					.addValue("I_V_COD_ROL", request.getRol() != null ? request.getRol().getCodigo() : null);

			out = jdbcCall.execute(params);
			Integer resultadoOpe = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (resultadoOpe == 0) {
				if (out.get("O_C_SALIDA") != null) {
					List<Map<String, Object>> listaMaps = (List<Map<String, Object>>) out.get("O_C_SALIDA");
					listaFichasAudi = listaMaps.stream().map(FichaAudi::mapper).collect(Collectors.toList());
				} else {
					String mensaje = "Error al recuperar datos de consulta";
					String mensajeInterno = "Error al recuperar datos de consulta";
					this.error = new Error(resultadoOpe, mensaje, mensajeInterno);
				}
			} else {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(resultadoOpe, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.obtenerListaAuditores";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return listaFichasAudi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListaPaginada<FichaAudi> obtenerInfoAuditores(InfoAuditorRequest request, Integer pagina,
			Integer registros) {
		Map<String, Object> out = null;
		ListaPaginada<FichaAudi> listaPaginada = new ListaPaginada<>();
		listaPaginada.setPagina(pagina);
		listaPaginada.setRegistros(registros);

		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_OBTENER_INFO_AUDITOR)
					.declareParameters(new SqlParameter("I_V_FICHA", OracleTypes.VARCHAR),
							new SqlParameter("I_V_NOMBRES", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_PAT", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_MAT", OracleTypes.VARCHAR),
							new SqlParameter("I_N_PAGINA", OracleTypes.NUMBER),
							new SqlParameter("I_N_REGISTROS", OracleTypes.NUMBER),
							new SqlOutParameter("O_C_SALIDA", OracleTypes.CURSOR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_V_FICHA", request.getNroFicha())
					.addValue("I_V_NOMBRES", request.getNombre()).addValue("I_V_APE_PAT", request.getApePaterno())
					.addValue("I_V_APE_MAT", request.getApeMaterno()).addValue("I_N_PAGINA", pagina)
					.addValue("I_N_REGISTROS", registros);

			out = jdbcCall.execute(params);
			Integer resultadoOpe = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (resultadoOpe == 0) {
				if (out.get("O_C_SALIDA") != null) {
					List<Map<String, Object>> listaMaps = (List<Map<String, Object>>) out.get("O_C_SALIDA");
					if (!listaMaps.isEmpty()) {
						Map<String, Object> firstRow = listaMaps.get(0);
						listaPaginada.setTotalRegistros(CastUtil.leerValorMapInteger(firstRow, "TOTAL"));
						listaPaginada.setLista(
								listaMaps.stream().map(FichaAudi::mappertoInfoAudi).collect(Collectors.toList()));
					} else {
						listaPaginada.setTotalRegistros(0);
						listaPaginada.setLista(new ArrayList<>());
					}
				} else {
					String mensaje = "Error al recuperar datos de consulta";
					String mensajeInterno = "Error al recuperar datos de consulta";
					this.error = new Error(resultadoOpe, mensaje, mensajeInterno);
				}
			} else {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(resultadoOpe, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.obtenerInfoAuditores";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return listaPaginada;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public FichaAudi registrarDatosFichaAuditor(FichaAudi fichaAuditor) {
		try {
			fichaAuditor = this.guardarFichaAuditor(fichaAuditor);

			if (Integer.parseInt(fichaAuditor.getTipoAuditor().getCodigo()) == 
					Integer.parseInt(MantFichaAuditor.TIPO_AUD_INTERNO)) {
				for (CursoAuditor curso : fichaAuditor.getCursosPendientes()) {
					curso.setIdFichaAudi(fichaAuditor.getIdFichaAudi());
					curso.setIdUsuaCrea(fichaAuditor.getIdUsuaCrea());
					if (this.guardarCursosPendientes(curso) > 0)
						return null;
				}

				for (CursoAuditor curso : fichaAuditor.getCursosCompletados()) {
					curso.setIdFichaAudi(fichaAuditor.getIdFichaAudi());
					curso.setIdUsuaCrea(fichaAuditor.getIdUsuaCrea());
					if (this.guardarCursosCompletados(curso) > 0)
						return null;
				}

				for (CursoNorma curso : fichaAuditor.getCursosNorma()) {
					curso.setIdFichaAudi(fichaAuditor.getIdFichaAudi());
					curso.setIdUsuaCrea(fichaAuditor.getIdUsuaCrea());
					if (this.guardarCursosNorma(curso) > 0)
						return null;
				}
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en DocumentoDAOImpl.registrarDatosFichaAuditor";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}
		return fichaAuditor;
	}

	@Override
	public FichaAudi guardarFichaAuditor(FichaAudi fichaAuditor) {
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_REG_CAB_FICHA_AUDI).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_V_TIPO_AUDI", OracleTypes.VARCHAR),
							new SqlParameter("I_N_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOMBRE", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_PAT", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_MAT", OracleTypes.VARCHAR),
							new SqlParameter("I_V_FEC_INGR", OracleTypes.VARCHAR),
							new SqlParameter("I_V_EXP", OracleTypes.VARCHAR),
							new SqlParameter("I_V_CARGO", OracleTypes.VARCHAR),
							new SqlParameter("I_V_EMPRESA", OracleTypes.VARCHAR),
							new SqlParameter("I_N_COD_EQUI", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOM_EQUI", OracleTypes.VARCHAR),
							new SqlParameter("I_N_COD_GEREN", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOM_GEREN", OracleTypes.VARCHAR),
							new SqlParameter("I_V_COD_ROL", OracleTypes.VARCHAR),
							new SqlParameter("I_V_COD_EDU", OracleTypes.VARCHAR),
							new SqlParameter("I_V_EVAL", OracleTypes.VARCHAR),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource()
					.addValue("I_V_TIPO_AUDI", fichaAuditor.getTipoAuditor().getCodigo())
					.addValue("I_N_FICHA", fichaAuditor.getFicha())
					.addValue("I_V_NOMBRE", fichaAuditor.getNomAudi().toUpperCase())
					.addValue("I_V_APE_PAT", fichaAuditor.getApePaterno().toUpperCase())
					.addValue("I_V_APE_MAT", fichaAuditor.getApeMaterno().toUpperCase())
					.addValue("I_V_FEC_INGR", fichaAuditor.getFecIngreso())
					.addValue("I_V_EXP", fichaAuditor.getAnnoExp()).addValue("I_V_CARGO", fichaAuditor.getNomCargo())
					.addValue("I_V_EMPRESA", fichaAuditor.getNomEmpre().toUpperCase())
					.addValue("I_N_COD_EQUI",
							fichaAuditor.getEquipo() != null ? fichaAuditor.getEquipo().getCodigo() : null)
					.addValue("I_V_NOM_EQUI",
							fichaAuditor.getEquipo() != null ? fichaAuditor.getEquipo().getDescripcion().toUpperCase() : null)
					.addValue("I_N_COD_GEREN",
							fichaAuditor.getGerencia() != null ? fichaAuditor.getGerencia().getCodigo() : null)
					.addValue("I_V_NOM_GEREN",
							fichaAuditor.getGerencia() != null ? fichaAuditor.getGerencia().getDescripcion().toUpperCase() : null)
					.addValue("I_V_COD_ROL",
							fichaAuditor.getRolAuditor() != null ? fichaAuditor.getRolAuditor().getCodigo()
									: MantFichaAuditor.ROL_AUD_EXTERNO)
					.addValue("I_V_COD_EDU",
							fichaAuditor.getTipoEducacion() != null ? fichaAuditor.getTipoEducacion().getCodigo()
									: null)
					.addValue("I_V_EVAL", fichaAuditor.getEvalAudi())
					.addValue("I_A_USUARIO", fichaAuditor.getIdUsuaCrea());

			out = this.jdbcCall.execute(params);
			Integer retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno == -1) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			} else {
				fichaAuditor.setIdFichaAudi(retorno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.guardarFichaAuditor";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return fichaAuditor;
	}

	@Override
	public Integer guardarCursosPendientes(CursoAuditor curso) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_REG_CURSOS_PEND).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOM_CURSO", OracleTypes.VARCHAR),
							new SqlParameter("I_N_OBLIG", OracleTypes.NUMBER),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_N_ID_FICHA", curso.getIdFichaAudi())
					.addValue("I_V_NOM_CURSO", curso.getNomCurso().toUpperCase()).addValue("I_N_OBLIG", curso.getIndObli())
					.addValue("I_A_USUARIO", curso.getIdUsuaCrea());

			out = this.jdbcCall.execute(params);

			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno != 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.guardarCursosPendientes";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}
		return retorno;
	}

	@Override
	public Integer guardarCursosCompletados(CursoAuditor curso) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_REG_CURSOS_COMPL).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOM_CURSO", OracleTypes.VARCHAR),
							new SqlParameter("I_N_OBLIG", OracleTypes.NUMBER),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_N_ID_FICHA", curso.getIdFichaAudi())
					.addValue("I_V_NOM_CURSO", curso.getNomCurso().toUpperCase()).addValue("I_N_OBLIG", curso.getIndObli())
					.addValue("I_A_USUARIO", curso.getIdUsuaCrea());

			out = this.jdbcCall.execute(params);

			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno != 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.guardarCursosCompletados";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}
		return retorno;
	}

	@Override
	public Integer guardarCursosNorma(CursoNorma curso) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_REG_CURSOS_NORMA).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_N_ID_NORMA", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOM_NORMA", OracleTypes.VARCHAR),
							new SqlParameter("I_N_OBLIG", OracleTypes.NUMBER),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource()
					.addValue("I_N_ID_FICHA", curso.getIdFichaAudi())
					.addValue("I_N_ID_NORMA", curso.getIdNorma())
					.addValue("I_V_NOM_NORMA", curso.getNomNorma().toUpperCase())
					.addValue("I_N_OBLIG", curso.getIndObli())
					.addValue("I_A_USUARIO", curso.getIdUsuaCrea());

			out = this.jdbcCall.execute(params);

			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno != 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.guardarCursosNorma";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return retorno;
	}

	@Override
	public Integer eliminarFichaAuditor(Integer idFicha, String usuario) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_ELIMINAR_FICHA_AUDI).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_N_ID_FICHA", idFicha)
					.addValue("I_A_USUARIO", usuario);

			out = this.jdbcCall.execute(params);
			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno > 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.eliminarFichaAuditor";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return retorno;
	}

	@Override
	@Transactional
	public Integer registrarCambiosFichaAuditor(FichaAudi fichaAuditor) {
		Integer resultado = 1;
		try {
			resultado = this.actualizarFichaAudi(fichaAuditor);

			if (resultado == 0) {
				if (Integer.parseInt(fichaAuditor.getTipoAuditor().getCodigo()) == 
						Integer.parseInt(MantFichaAuditor.TIPO_AUD_INTERNO)) {
					for (CursoAuditor curso : fichaAuditor.getCursosPendientes()) {
						curso.setIdFichaAudi(fichaAuditor.getIdFichaAudi());
						curso.setIdUsuaModi(fichaAuditor.getIdUsuaModi());
						if (this.actualizarCursosPendientes(curso) > 0)
							return null;
					}

					for (CursoAuditor curso : fichaAuditor.getCursosCompletados()) {
						curso.setIdFichaAudi(fichaAuditor.getIdFichaAudi());
						curso.setIdUsuaModi(fichaAuditor.getIdUsuaModi());
						if (this.actualizarCursosCompletados(curso) > 0)
							return null;
					}

					for (CursoNorma curso : fichaAuditor.getCursosNorma()) {
						curso.setIdFichaAudi(fichaAuditor.getIdFichaAudi());
						curso.setIdUsuaModi(fichaAuditor.getIdUsuaModi());
						if (this.actualizarCursoNorma(curso) > 0)
							return null;
					}
				}
			} else {
				throw new Exception("Error al actualizar cabecera de ficha");
			}

		} catch (Exception e) {
			String mensaje = "Error en DocumentoDAOImpl.registrarCambiosFichaAuditor";
			String mensajeInterno = e.getMessage();
			LOGGER.error(mensaje, e);
		}
		return resultado;
	}

	@Override
	public Integer actualizarFichaAudi(FichaAudi fichaAuditor) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_ACT_CAB_FICHA_AUDI).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_V_COD_ROL", OracleTypes.VARCHAR),
							new SqlParameter("I_V_COD_EDU", OracleTypes.VARCHAR),
							new SqlParameter("I_V_EMPRESA", OracleTypes.VARCHAR),
							new SqlParameter("I_V_NOMBRES", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_PAT", OracleTypes.VARCHAR),
							new SqlParameter("I_V_APE_MAT", OracleTypes.VARCHAR),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource()
					.addValue("I_N_ID_FICHA", fichaAuditor.getIdFichaAudi())
					.addValue("I_V_COD_ROL", fichaAuditor.getRolAuditor().getCodigo())
					.addValue("I_V_COD_EDU", fichaAuditor.getTipoEducacion().getCodigo())
					.addValue("I_V_EMPRESA", fichaAuditor.getNomEmpre() != null ? fichaAuditor.getNomEmpre().toUpperCase() : null)
					.addValue("I_V_NOMBRES", fichaAuditor.getNomAudi().toUpperCase())
					.addValue("I_V_APE_PAT", fichaAuditor.getApePaterno().toUpperCase())
					.addValue("I_V_APE_MAT", fichaAuditor.getApeMaterno().toUpperCase())
					.addValue("I_A_USUARIO", fichaAuditor.getIdUsuaModi());

			out = this.jdbcCall.execute(params);
			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno != 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.actualizarFichaAudi";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return retorno;
	}

	@Override
	public Integer actualizarCursosCompletados(CursoAuditor curso) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_ACT_CURSO_COMPL).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_N_ID_CURSO", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOMBRE", OracleTypes.VARCHAR),
							new SqlParameter("I_N_OBLIG", OracleTypes.NUMBER),
							new SqlParameter("I_V_ESTADO", OracleTypes.VARCHAR),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_N_ID_FICHA", curso.getIdFichaAudi())
					.addValue("I_N_ID_CURSO", curso.getIdCurso()).addValue("I_V_NOMBRE", curso.getNomCurso().toUpperCase())
					.addValue("I_N_OBLIG", curso.getIndObli()).addValue("I_V_ESTADO", curso.getStReg())
					.addValue("I_A_USUARIO", curso.getIdUsuaModi());

			out = this.jdbcCall.execute(params);
			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno != 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.actualizarCursosCompletados";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return retorno;
	}

	@Override
	public Integer actualizarCursosPendientes(CursoAuditor curso) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_ACT_CURSO_PEND).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_N_ID_CURSO", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOMBRE", OracleTypes.VARCHAR),
							new SqlParameter("I_N_OBLIG", OracleTypes.NUMBER),
							new SqlParameter("I_V_ESTADO", OracleTypes.VARCHAR),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_N_ID_FICHA", curso.getIdFichaAudi())
					.addValue("I_N_ID_CURSO", curso.getIdCurso()).addValue("I_V_NOMBRE", curso.getNomCurso().toUpperCase())
					.addValue("I_N_OBLIG", curso.getIndObli()).addValue("I_V_ESTADO", curso.getStReg())
					.addValue("I_A_USUARIO", curso.getIdUsuaModi());

			out = this.jdbcCall.execute(params);
			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno != 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.actualizarCursosPendientes";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return retorno;
	}

	@Override
	public Integer actualizarCursoNorma(CursoNorma curso) {
		Map<String, Object> out = null;
		Integer retorno = 1;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT_MC)
					.withProcedureName(DBConstants.PRC_MANT_ACT_CURSO_NORMA).withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("I_N_ID_CURSO", OracleTypes.NUMBER),
							new SqlParameter("I_N_ID_FICHA", OracleTypes.NUMBER),
							new SqlParameter("I_N_ID_NORMA", OracleTypes.NUMBER),
							new SqlParameter("I_V_NOMBRE", OracleTypes.VARCHAR),
							new SqlParameter("I_N_OBLIG", OracleTypes.NUMBER),
							new SqlParameter("I_V_ESTADO", OracleTypes.VARCHAR),
							new SqlParameter("I_A_USUARIO", OracleTypes.VARCHAR),
							new SqlOutParameter("O_N_RETORNO", OracleTypes.NUMBER),
							new SqlOutParameter("O_V_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_V_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource params = new MapSqlParameterSource().addValue("I_N_ID_CURSO", curso.getIdCursoNorma())
					.addValue("I_N_ID_FICHA", curso.getIdFichaAudi()).addValue("I_N_ID_NORMA", curso.getIdNorma())
					.addValue("I_V_NOMBRE", curso.getNomNorma().toUpperCase()).addValue("I_N_OBLIG", curso.getIndObli())
					.addValue("I_V_ESTADO", curso.getStReg()).addValue("I_A_USUARIO", curso.getIdUsuaModi());

			out = this.jdbcCall.execute(params);
			retorno = CastUtil.leerValorMapInteger(out, "O_N_RETORNO");

			if (retorno != 0) {
				String mensaje = CastUtil.leerValorMapString(out, "O_V_MENSAJE");
				String mensajeInterno = CastUtil.leerValorMapString(out, "O_V_SQLERRM");
				this.error = new Error(retorno, mensaje, mensajeInterno);
				throw new Exception(mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en FichaAudiDAOImpl.actualizarCursoNorma";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(mensaje, e);
		}

		return retorno;
	}

}
