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

import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.GmdException;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IAreaAuditoriaDAO;
import pe.com.sedapal.agi.model.AreaAlcance;
import pe.com.sedapal.agi.model.AreaAlcanceAuditoria;
import pe.com.sedapal.agi.model.AreaAuditoria;
import pe.com.sedapal.agi.model.AreaCargoAuditoria;
import pe.com.sedapal.agi.model.AreaNormaAuditoria;
import pe.com.sedapal.agi.model.AreaNormaDet;
import pe.com.sedapal.agi.model.AreaParametros;
import pe.com.sedapal.agi.model.CargoSig;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.response_objects.Error;

@Repository
public class AreaAuditoriaDaoImpl implements IAreaAuditoriaDAO {

	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Error error;

	private static final Logger LOGGER = Logger.getLogger(CargoSigDAOImpl.class);

	@Override
	public Error getError() {
		return this.error;
	}

	@Override
	public AreaParametros obtenerAreaParametros() {

		Map<String, Object> out = null;
		AreaParametros areaParametros = new AreaParametros();

		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA_MANT")
				.withProcedureName("PRC_MANT_OBTENER_AREA")
				.declareParameters(new SqlOutParameter("O_CURSOR_AREA", OracleTypes.CURSOR),
						new SqlOutParameter("O_CURSOR_ALCANCE_AREA", OracleTypes.CURSOR),
						new SqlOutParameter("O_CURSOR_CARGOS_AREA", OracleTypes.CURSOR),
						new SqlOutParameter("O_CURSOR_CARGOS_SIG", OracleTypes.CURSOR),
						new SqlOutParameter("O_CURSOR_ALCANCES", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		try {
			out = this.jdbcCall.execute();
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				areaParametros = mapearAreaParametros(out);
				this.error = null;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return areaParametros;
	}

	@Override
	public Integer registrarAuditoria(AreaAuditoria areaAuditoria, List<AreaAlcanceAuditoria> lstAlcance,
			List<AreaCargoAuditoria> lstCargo) throws GmdException {
		Integer resultado = null;
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA_MANT")
				.withProcedureName("PRC_MANT_REGISTRAR_AREA")
				.declareParameters(new SqlParameter("in_id_area", OracleTypes.NUMBER),
						new SqlParameter("in_tipo", OracleTypes.NUMBER),
						new SqlParameter("in_nom_area", OracleTypes.VARCHAR),
						new SqlParameter("in_sigla", OracleTypes.VARCHAR),
						new SqlParameter("in_ficha", OracleTypes.NUMBER),
						new SqlParameter("in_cod_area", OracleTypes.NUMBER),
						new SqlParameter("in_cod_area_padre", OracleTypes.NUMBER),
						new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
						new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
						new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("in_id_area", areaAuditoria.getN_id_area())
				.addValue("in_tipo", areaAuditoria.getN_id_tipo())
				.addValue("in_nom_area", areaAuditoria.getV_nom_area()).addValue("in_sigla", areaAuditoria.getV_sigla())
				.addValue("in_ficha", areaAuditoria.getN_ficha()).addValue("in_cod_area", areaAuditoria.getN_cod_area())
				.addValue("in_cod_area_padre", areaAuditoria.getN_cod_area_padre());

		try {
			out = this.jdbcCall.execute(in);
			resultado = (Integer) out.get("O_RETORNO");
			if (resultado == 0) {
				if (lstAlcance.size() == 0) {
					Integer indicador = 0;
					Integer indicadoNulo = 0;
					AreaAlcanceAuditoria nulo = new AreaAlcanceAuditoria();
					nulo.setN_id_area(areaAuditoria.getN_id_area());
					try {
						resultado = this.registrarAreaAlcanceAuditoria(nulo, indicador, indicadoNulo);
					} catch (Exception e) {
						resultado = 1;
						String mensaje = "Error en CargoSigDAOImpl.obtenerListaCargoSig";
						String mensajeInterno = e.getMessage();
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						this.error = new Error(resultado, mensaje, mensajeInterno);
					}
				} else {
					for (int i = 0; i < lstAlcance.size(); i++) {
						Integer indicador = 0;
						Integer indicadoNulo = 1;
						if (i == 0) {
							indicador = 0;
						} else {
							indicador = 1;
						}
						try {
							resultado = this.registrarAreaAlcanceAuditoria(lstAlcance.get(i), indicador, indicadoNulo);
						} catch (Exception e) {
							resultado = 1;
							String mensaje = "Error en CargoSigDAOImpl.obtenerListaCargoSig";
							String mensajeInterno = e.getMessage();
							String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
							LOGGER.error(error[1], e);
							this.error = new Error(resultado, mensaje, mensajeInterno);
						}
					}
				}
				if (lstCargo.size() == 0) {
					Integer indicador = 0;
					Integer indicadoNulo = 0;
					AreaCargoAuditoria nulo = new AreaCargoAuditoria();
					nulo.setN_id_area(areaAuditoria.getN_id_area());
					resultado = this.registrarAreaCargoAuditoria(nulo, indicador, indicadoNulo);
				} else {
					for (int i = 0; i < lstCargo.size(); i++) {
						Integer indicador = 0;
						Integer indicadoNulo = 1;
						if (i == 0) {
							indicador = 0;
						} else {
							indicador = 1;
						}
						try {
							resultado = this.registrarAreaCargoAuditoria(lstCargo.get(i), indicador, indicadoNulo);
						} catch (Exception e) {
							resultado = 1;
							String mensaje = "Error en CargoSigDAOImpl.obtenerListaCargoSig";
							String mensajeInterno = e.getMessage();
							String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
							LOGGER.error(error[1], e);
							this.error = new Error(resultado, mensaje, mensajeInterno);
						}
					}
				}
			} else {
				resultado = 1;
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			resultado = 1;
			String mensaje = "Error en CargoSigDAOImpl.obtenerListaCargoSig";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return resultado;
	}

	public Integer registrarAreaAlcanceAuditoria(AreaAlcanceAuditoria alcanceAuditoria, Integer indicador,
			Integer indicadoNulo) {
		Integer resultado = null;
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA_MANT")
				.withProcedureName("PRC_MANT_REGISTRAR_ALCANCES")
				.declareParameters(new SqlParameter("IN_ID_AREA", OracleTypes.NUMBER),
						new SqlParameter("IN_COD_ALCA", OracleTypes.NUMBER),
						new SqlParameter("IN_ABRE_ALCA", OracleTypes.VARCHAR),
						new SqlParameter("INDICADOR", OracleTypes.NUMBER),
						new SqlParameter("INDICADOR_NULO", OracleTypes.NUMBER),
						new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
						new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
						new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("IN_ID_ALC_AREA", alcanceAuditoria.getN_id_alc_area())
				.addValue("IN_ID_AREA", alcanceAuditoria.getN_id_area())
				.addValue("IN_COD_ALCA", alcanceAuditoria.getV_cod_alca())
				.addValue("IN_ABRE_ALCA", alcanceAuditoria.getV_abre_alca()).addValue("INDICADOR_NULO", indicadoNulo)
				.addValue("INDICADOR", indicador);

		try {
			out = this.jdbcCall.execute(in);
			resultado = (Integer) out.get("O_RETORNO");
		} catch (Exception e) {
			resultado = 1;
			String mensaje = (String) out.get("O_RETORNO");
			String mensajeInterno = (String) out.get("O_SQLERRM");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return resultado;
	}

	public Integer registrarAreaCargoAuditoria(AreaCargoAuditoria cargoAuditoria, Integer indicador,
			Integer indicadoNulo) {
		Integer resultado = null;
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA_MANT")
				.withProcedureName("PRC_MANT_REGISTRAR_CARGOS")
				.declareParameters(new SqlParameter("IN_ID_CARGO_SIG", OracleTypes.NUMBER),
						new SqlParameter("IN_ID_AREA", OracleTypes.NUMBER),
						new SqlParameter("IN_NOM_CARGO_SIG", OracleTypes.VARCHAR),
						new SqlParameter("IN_SIGLA", OracleTypes.VARCHAR),
						new SqlParameter("INDICADOR", OracleTypes.NUMBER),
						new SqlParameter("INDICADOR_NULO", OracleTypes.NUMBER),
						new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
						new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
						new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("IN_ID_CARGO_AREA", cargoAuditoria.getN_id_cargo_area())
				.addValue("IN_ID_CARGO_SIG", cargoAuditoria.getN_id_cargo_sig())
				.addValue("IN_ID_AREA", cargoAuditoria.getN_id_area())
				.addValue("IN_NOM_CARGO_SIG", cargoAuditoria.getV_nom_cargo_sig())
				.addValue("IN_SIGLA", cargoAuditoria.getV_sigla()).addValue("INDICADOR", indicador)
				.addValue("INDICADOR_NULO", indicadoNulo);

		try {
			out = this.jdbcCall.execute(in);
			resultado = (Integer) out.get("O_RETORNO");
		} catch (Exception e) {
			resultado = (Integer) out.get("O_RETORNO");
			String mensaje = (String) out.get("O_RETORNO");
			String mensajeInterno = (String) out.get("O_SQLERRM");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return resultado;
	}

	public AreaParametros mapearAreaParametros(Map<String, Object> map) {

		AreaParametros areaParametros = new AreaParametros();

		AreaAuditoria areaAuditoria = null;
		AreaAlcanceAuditoria areaAlcanceAuditoria = null;
		AreaCargoAuditoria areaCargoAuditoria = null;
		CargoSig cargoSig = null;
		AreaAlcance areaAlcance = null;
		Norma norma = null;
		AreaNormaAuditoria areaNorma = null;
		AreaNormaDet areaNormaDet = null;

		List<AreaAuditoria> lstAreaAuditoria = new ArrayList<AreaAuditoria>();
		List<AreaAlcanceAuditoria> lstAreaAlcanceAuditoria = new ArrayList<AreaAlcanceAuditoria>();
		List<AreaCargoAuditoria> lstAreaCargoAuditoria = new ArrayList<AreaCargoAuditoria>();
		List<CargoSig> lstCargoSig = new ArrayList<CargoSig>();
		List<AreaAlcance> lstAreaAlcance = new ArrayList<AreaAlcance>();
		List<Norma> lstNormas = new ArrayList<Norma>();
		List<AreaNormaAuditoria> lstAreaNorma = new ArrayList<AreaNormaAuditoria>();
		List<AreaNormaDet> lstAreaNormaDet = new ArrayList<AreaNormaDet>();

		List<Map<String, Object>> listaAreaAuditoria = (List<Map<String, Object>>) map.get("O_CURSOR_AREA");
		List<Map<String, Object>> listaAreaAlcanceAuditoria = (List<Map<String, Object>>) map.get("O_CURSOR_ALCANCE_AREA");
		List<Map<String, Object>> listaAreaCargoAuditoria = (List<Map<String, Object>>) map.get("O_CURSOR_CARGOS_AREA");
		List<Map<String, Object>> listaCargoSig = (List<Map<String, Object>>) map.get("O_CURSOR_CARGOS_SIG");
		List<Map<String, Object>> listaAreaAlcance = (List<Map<String, Object>>) map.get("O_CURSOR_ALCANCES");
		List<Map<String, Object>> listaNormas = (List<Map<String, Object>>) map.get("O_CURSOR_NORMAS");
		List<Map<String, Object>> listaAreaNormas = (List<Map<String, Object>>) map.get("O_CURSOR_AREA_NORMA");
		List<Map<String, Object>> listaAreaNormaDet = (List<Map<String, Object>>) map.get("O_CURSOR_NORMA_DET");

		for (Map<String, Object> map1 : listaAreaAuditoria) {
			areaAuditoria = new AreaAuditoria();
			if (map1.get("N_ID_AREA") == null) {
				areaAuditoria.setN_id_area(null);
			} else {
				areaAuditoria.setN_id_area(Integer.valueOf((map1.get("N_ID_AREA").toString())));
			}
			if (map1.get("N_ID_TIPO") == null) {
				areaAuditoria.setN_id_tipo(null);
			} else {
				areaAuditoria.setN_id_tipo(Integer.valueOf((map1.get("N_ID_TIPO").toString())));
			}
			if (map1.get("N_COD_AREA") == null) {
				areaAuditoria.setN_cod_area(null);
			} else {
				areaAuditoria.setN_cod_area(Integer.valueOf((map1.get("N_COD_AREA").toString())));
			}
			areaAuditoria.setV_nom_area((String) map1.get("V_NOM_AREA"));
			areaAuditoria.setV_sigla((String) map1.get("V_SIGLA"));
			if (map1.get("N_COD_AREA_PADRE") == null) {
				areaAuditoria.setN_cod_area_padre(null);
			} else {
				areaAuditoria.setN_cod_area_padre(Integer.valueOf((map1.get("N_COD_AREA_PADRE").toString())));
			}
			if (map1.get("N_FICHA") == null) {
				areaAuditoria.setN_ficha(null);
			} else {
				areaAuditoria.setN_ficha(Integer.valueOf((map1.get("N_FICHA").toString())));
			}
			areaAuditoria.setResponsable((String) map1.get("V_RESPONSABLE"));
			lstAreaAuditoria.add(areaAuditoria);
			if (map1.get("V_ST_REG") == null) {
				areaAuditoria.setV_st_reg(null);
			} else {
				areaAuditoria.setV_st_reg((Integer.valueOf((map1.get("V_ST_REG")).toString())));
			}
		}

		for (Map<String, Object> map2 : listaAreaAlcanceAuditoria) {
			areaAlcanceAuditoria = new AreaAlcanceAuditoria();
			areaAlcanceAuditoria.setN_id_alc_area(Integer.valueOf((map2.get("N_ID_ALC_AREA").toString())));
			areaAlcanceAuditoria.setN_id_area(Integer.valueOf((map2.get("N_ID_AREA").toString())));
			areaAlcanceAuditoria.setV_cod_alca((String) map2.get("V_COD_ALCA"));
			areaAlcanceAuditoria.setV_abre_alca((String) map2.get("V_ABRE_ALCA"));
			lstAreaAlcanceAuditoria.add(areaAlcanceAuditoria);
		}

		for (Map<String, Object> map3 : listaAreaCargoAuditoria) {
			areaCargoAuditoria = new AreaCargoAuditoria();
			areaCargoAuditoria.setN_id_cargo_area(Integer.valueOf((map3.get("N_ID_CARGO_AREA").toString())));
			areaCargoAuditoria.setN_id_cargo_sig(Integer.valueOf((map3.get("N_ID_CARGO_SIG").toString())));
			areaCargoAuditoria.setN_id_area(Integer.valueOf((map3.get("N_ID_AREA").toString())));
			areaCargoAuditoria.setV_nom_cargo_sig((String) map3.get("V_NOM_CARGO_SIG"));
			areaCargoAuditoria.setV_sigla((String) map3.get("V_SIGLA"));
			lstAreaCargoAuditoria.add(areaCargoAuditoria);
		}

		for (Map<String, Object> map4 : listaCargoSig) {
			cargoSig = new CargoSig();
			cargoSig.setIdCargoSig(((BigDecimal) map4.get("N_ID_CARGO_SIG")).longValue());
			cargoSig.setNombreCargoSig((String) map4.get("V_NOM_CARGO_SIG"));
			cargoSig.setSigla((String) map4.get("V_SIGLA"));
			lstCargoSig.add(cargoSig);
		}

		for (Map<String, Object> map5 : listaAreaAlcance) {
			areaAlcance = new AreaAlcance();
			if (map5.get("N_IDCONS") == null) {
				areaAlcance.setN_idcons(null);
			} else {
				areaAlcance.setN_idcons(Integer.valueOf((map5.get("N_IDCONS").toString())));
			}
			if (map5.get("N_IDCONSSUPE") == null) {
				areaAlcance.setN_idconssupe(null);
			} else {
				areaAlcance.setN_idconssupe(Integer.valueOf((map5.get("N_IDCONSSUPE").toString())));
			}
			areaAlcance.setV_valcons((String) map5.get("V_VALCONS"));
			areaAlcance.setV_nomcons((String) map5.get("V_NOMCONS"));
			areaAlcance.setV_abrecons((String) map5.get("V_ABRECONS"));
			areaAlcance.setV_descons((String) map5.get("V_DESCONS"));
			lstAreaAlcance.add(areaAlcance);
		}

		for (Map<String, Object> map6 : listaNormas) {
			norma = new Norma();
			if (map6.get("N_ID_NORMAS") == null) {
				norma.setN_id_normas(null);
			} else {
				norma.setN_id_normas(Integer.valueOf((map6.get("N_ID_NORMAS").toString())));
			}
			norma.setV_nom_norma((String) map6.get("V_NOM_NORMA"));
			norma.setV_tipo((String) map6.get("V_TIPO"));
			norma.setV_st_reg((String) map6.get("V_ST_REG"));
			lstNormas.add(norma);
		}

		for (Map<String, Object> map7 : listaAreaNormas) {
			areaNorma = new AreaNormaAuditoria();
			if (map7.get("N_ID_AREA_NORMA") == null) {
				areaNorma.setN_id_area_norma(null);
			} else {
				areaNorma.setN_id_area_norma(Integer.valueOf((map7.get("N_ID_AREA_NORMA").toString())));
			}
			if (map7.get("N_ID_AREA") == null) {
				areaNorma.setN_id_area(null);
			} else {
				areaNorma.setN_id_area(Integer.valueOf((map7.get("N_ID_AREA").toString())));
			}
			if (map7.get("N_ID_TIPO") == null) {
				areaNorma.setN_id_tipo(null);
			} else {
				areaNorma.setN_id_tipo(Integer.valueOf((map7.get("N_ID_TIPO").toString())));
			}
			if (map7.get("N_COD_AREA") == null) {
				areaNorma.setN_cod_area(null);
			} else {
				areaNorma.setN_cod_area(Integer.valueOf((map7.get("N_COD_AREA").toString())));
			}
			areaNorma.setV_des_area((String) map7.get("V_DES_AREA"));
			areaNorma.setV_st_reg((String) map7.get("V_ST_REG"));
			lstAreaNorma.add(areaNorma);
		}

		for (Map<String, Object> map8 : listaAreaNormaDet) {
			areaNormaDet = new AreaNormaDet();
			if (map8.get("N_ID_AREA_NORM_DET") == null) {
				areaNormaDet.setN_id_area_norm_det(null);
			} else {
				areaNormaDet.setN_id_area_norm_det(Integer.valueOf((map8.get("N_ID_AREA_NORM_DET").toString())));
			}
			if (map8.get("N_ID_AREA_NORMA") == null) {
				areaNormaDet.setN_id_area_norma(null);
			} else {
				areaNormaDet.setN_id_area_norma(Integer.valueOf((map8.get("N_ID_AREA_NORMA").toString())));
			}
			if (map8.get("N_ID_AREA") == null) {
				areaNormaDet.setN_id_area(null);
			} else {
				areaNormaDet.setN_id_area(Integer.valueOf((map8.get("N_ID_AREA").toString())));
			}
			if (map8.get("N_ID_NORMAS") == null) {
				areaNormaDet.setN_id_normas(null);
			} else {
				areaNormaDet.setN_id_normas(Integer.valueOf((map8.get("N_ID_NORMAS").toString())));
			}
			if (map8.get("N_COD_AREA") == null) {
				areaNormaDet.setN_cod_area(null);
			} else {
				areaNormaDet.setN_cod_area(Integer.valueOf((map8.get("N_COD_AREA").toString())));
			}
			areaNormaDet.setV_des_area((String) map8.get("V_DES_AREA"));
			areaNormaDet.setV_nom_norma((String) map8.get("V_NOM_NORMA"));
			areaNormaDet.setV_st_reg((String) map8.get("V_ST_REG"));
			lstAreaNormaDet.add(areaNormaDet);
		}

		areaParametros.setLstAreaAuditoria(lstAreaAuditoria);
		areaParametros.setLstAreaAlcanceAuditoria(lstAreaAlcanceAuditoria);
		areaParametros.setLstAreaCargoAuditoria(lstAreaCargoAuditoria);
		areaParametros.setLstCargoSig(lstCargoSig);
		areaParametros.setLstAlcances(lstAreaAlcance);
		areaParametros.setLstNormas(lstNormas);
		areaParametros.setLstAreaNormaAuditoria(lstAreaNorma);
		areaParametros.setLstAreaNormaDet(lstAreaNormaDet);

		return areaParametros;
	}

	@Override
	public Integer eliminarAuditoria(AreaAuditoria areaAuditoria) throws GmdException {
		Integer resultado = null;
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_AUDITORIA_MANT")
				.withProcedureName("PRC_MANT_ELIMINAR_AREA")
				.declareParameters(new SqlParameter("IN_ID_AREA", OracleTypes.NUMBER),
						new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
						new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
						new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("IN_ID_AREA", areaAuditoria.getN_id_area());

		try {
			out = this.jdbcCall.execute(in);
			resultado = (Integer) out.get("O_RETORNO");
		} catch (Exception e) {
			resultado = (Integer) out.get("O_RETORNO");
			String mensaje = (String) out.get("O_RETORNO");
			String mensajeInterno = (String) out.get("O_SQLERRM");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return resultado;

	}

}
