package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.INoConformidadDAO;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.PlanAccionEjecucion;
import pe.com.sedapal.agi.model.NoConformidadSeguimiento;
import pe.com.sedapal.agi.model.request_objects.NoConformidadRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UConvierteFecha;

@Repository
public class NoConformidadDAOImpl implements INoConformidadDAO{
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;
	
	private static final Logger LOGGER = Logger.getLogger(NoConformidadDAOImpl.class);	
	
	@Override
	public List<NoConformidad> obtenerNoConformidad(NoConformidadRequest constanteRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<NoConformidad> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
				
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NO_CONFORMIDAD_OBTENER")
				.declareParameters(
						new SqlParameter("i_tipoFecha", OracleTypes.VARCHAR),
						new SqlParameter("i_fechaDesde", OracleTypes.DATE),
						new SqlParameter("i_fechaHasta", OracleTypes.DATE),
						new SqlParameter("i_codigo", OracleTypes.NUMBER),
						new SqlParameter("i_tipoConformidad", OracleTypes.VARCHAR),
						new SqlParameter("i_norma", OracleTypes.NUMBER),
						new SqlParameter("i_requisito", OracleTypes.VARCHAR),
						new SqlParameter("i_origenDeteccion", OracleTypes.VARCHAR),
						new SqlParameter("i_gerencia", OracleTypes.VARCHAR),
						new SqlParameter("i_equipo", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlParameter("i_vorden", OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
				
		SqlParameterSource in;
		try {
			in = new MapSqlParameterSource()
					.addValue("i_tipoFecha", constanteRequest.getTipoFecha())
					.addValue("i_fechaDesde", constanteRequest.getFechaDesde()!= null ? UConvierteFecha.convertirStringADate(constanteRequest.getFechaDesde()):null)
					.addValue("i_fechaHasta", constanteRequest.getFechaHasta()!= null ? UConvierteFecha.convertirStringADate(constanteRequest.getFechaHasta()):null)
					.addValue("i_codigo", constanteRequest.getCodigo())
					.addValue("i_tipoConformidad", constanteRequest.getTipoConformidad())
					.addValue("i_norma", constanteRequest.getNorma())
					.addValue("i_requisito", constanteRequest.getRequisito())
					.addValue("i_origenDeteccion", constanteRequest.getOrigenDeteccion())
					.addValue("i_gerencia", constanteRequest.getGerencia())
					.addValue("i_equipo", constanteRequest.getEquipo())
					.addValue("i_npagina",pageRequest.getPagina())
					.addValue("i_nregistros",pageRequest.getRegistros())
					.addValue("i_vorden",null);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				lista = mapearNoConformidad(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}											
		return lista;
	}

	@Override
	public Paginacion getPaginacion() {
		return this.paginacion;
	}
	
	@Override
	public Error getError() {
		return this.error;
	}
	
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}	
	
	public List<NoConformidad> mapearNoConformidad(Map<String, Object> resultados) {
		List<NoConformidad> listaNoConformidad = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		NoConformidad item = null;
		
		for(Map<String, Object> map : lista) {
			item = new NoConformidad();
			item.setIdNoConformidad(((BigDecimal)map.get("N_IDNOCONF")).longValue());
			item.setFechaIdentificacion((Date)map.get("D_FECHIDEN"));
			item.setIdTipoNoConformidad((String)map.get("V_TIPONC"));
			item.setDescripcionTipoNoConformidad((String)map.get("DESC_V_TIPONC"));
			item.setIdGerencia((String)map.get("V_DESCGERE"));
			item.setDescripcionGerencia((String)map.get("DESC_V_DESCGERE"));
			item.setIdNorma((((BigDecimal)map.get("N_IDNORM")) != null ? ((BigDecimal)map.get("N_IDNORM")) : 0).longValue());
			item.setDescripcionNorma((String)map.get("DESC_N_IDNORM"));
			item.setDescripcionAlcance((String)map.get("DESC_ALCANCE"));
			item.setIdOrigenDeteccion((String)map.get("V_ORGINC"));
			item.setDescripcionOrigenDeteccion((String)map.get("DESC_V_ORGINC"));
			item.setIdRequisito((String)map.get("V_ORDEREQ"));
			item.setDescripcionRequisito((String)map.get("DES_V_ORDEREQ"));
			item.setIdEquipo((String)map.get("V_DESEQUI"));
			item.setDescripcionEquipo((String)map.get("DESC_V_DESEQUI"));
			item.setIdTipoAmbito((String)map.get("V_TIPOAMB"));
			item.setDescripcionTipoAmbito((String)map.get("DESC_V_TIPOAMB"));
			item.setIdEtapa((String)map.get("V_ETAPSEGU"));
			item.setDescripcionEtapa((String)map.get("DESC_V_ETAPSEGU"));
			item.setIdEstadoNoConformidad((String)map.get("V_ESTNOCONF"));
			item.setDescripcionEstadoNoConformidad((String)map.get("DESC_V_ESTNOCONF"));			
			listaNoConformidad.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaNoConformidad;
	}

	@Override
	public List<NoConformidadSeguimiento> obtenerNoConformidadSeguimiento(NoConformidadRequest noConformidadRequest,PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<NoConformidadSeguimiento> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_ETAPAS_NO_CONF_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidnoconf", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		
		SqlParameterSource in;
		in = new MapSqlParameterSource()
				.addValue("i_nidnoconf", noConformidadRequest.getCodigo())
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");	
			if(resultado == 0) {
				lista = mapearNoConformidadSeguimiento(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	public List<NoConformidadSeguimiento> mapearNoConformidadSeguimiento(Map<String, Object> resultados) {
		List<NoConformidadSeguimiento> listaNoConformidadSeguimiento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		NoConformidadSeguimiento item = null;
		
		for(Map<String, Object> map : lista) {
			item = new NoConformidadSeguimiento();
			item.setOrdenEtapa((String)map.get("ORDEN_ETAPA"));
			item.setIdNoConformidad(((BigDecimal)map.get("N_IDNOCONF")).longValue());
			item.setIdNoConformidadSeguimiento(((BigDecimal)map.get("N_IDNCSEGU")).longValue());
			item.setEtapaSeguimiento((String)map.get("V_ETAPSEGU"));
			item.setFechaseguimiento((Date)map.get("D_FECHSEGU"));
			item.setEstadoSeguimiento((String)map.get("V_ESTSEGU"));
			item.setUsuarioCreacion((String)map.get("A_V_USUCRE"));
			item.setFechaCreacion((Date)map.get(" A_D_FECCRE"));
			item.setUsuarioModificacion((String)map.get("A_V_USUMOD"));
			item.setFechaModificacion((Date)map.get("A_D_FECMOD"));
			item.setEtapa((String)map.get("V_VALCONS"));
			item.setNumeroFila(((BigDecimal)map.get("RNUM")).intValue());
			listaNoConformidadSeguimiento.add(item);

			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaNoConformidadSeguimiento;
	}
	
	@Override
	public NoConformidad obtenerDatosNoConformidad(NoConformidadRequest noConformidadRequest) {
		Map<String, Object> out = null;
		NoConformidad datosNoConformidad = new NoConformidad(); 
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_DATOS_NO_CONF_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidnoconf", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),	
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));		
		SqlParameterSource in;
		in = new MapSqlParameterSource().addValue("i_nidnoconf", noConformidadRequest.getCodigo());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");	
			if(resultado == 0) {
				datosNoConformidad = mapearDatosNoConformidad(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return datosNoConformidad;
	}
	
	public NoConformidad mapearDatosNoConformidad(Map<String, Object> resultados) {
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		NoConformidad item = null;
		NoConformidad datos = new NoConformidad();
		for(Map<String, Object> map : lista) {
			item = new NoConformidad();
			item.setIdNoConformidad(((BigDecimal)map.get("n_idnoconf")).longValue());
			item.setIdEstadoNoConformidad((String)map.get("v_estnoconf"));
			item.setDescripcionEstadoNoConformidad((String)map.get("v_desestnoconf"));
			item.setIdEtapa((String)map.get("v_etapsegu"));
			item.setDescripcionEtapa((String)map.get("v_desetapsegu"));
			item.setDescripcionIdentifProblema((String)map.get("v_idenprob"));
			item.setDescripcionAccionInmediata((String)map.get("v_accinme"));
			item.setDescripcionObservacion((String)map.get("v_obsana"));
			item.setDescripcionAnalisisCausa((String)map.get("v_anacaus"));
			item.setCriticaAnalisisCausa((String)map.get("v_crianacau"));
			item.setCriticaAccionInmediata((String)map.get("v_criaccinm"));
			item.setComentarioVerificacion((String)map.get("v_coverest"));
			item.setComentarioCierre((String)map.get("v_cocierre"));
			datos=item;
		}
		return datos;
	}
	
	@Override
	public NoConformidad actualizarNoConformidad(Long idNoConformidad, NoConformidad noConformidad) {
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NO_CONF_GUARDAR")
				.declareParameters(
						new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
						new SqlParameter("i_v_idenprob", OracleTypes.VARCHAR),
						new SqlParameter("i_v_accinme", OracleTypes.VARCHAR),
						new SqlParameter("i_v_obsana", OracleTypes.VARCHAR),
						new SqlParameter("i_v_anacaus", OracleTypes.VARCHAR),
						new SqlParameter("i_v_crianacau", OracleTypes.VARCHAR),
						new SqlParameter("i_v_criaccinm", OracleTypes.VARCHAR),
						new SqlParameter("i_v_coverest", OracleTypes.VARCHAR),
						new SqlParameter("i_v_cocierre", OracleTypes.VARCHAR),
						new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
						new SqlParameter("i_v_etapa", OracleTypes.INTEGER),
						new SqlParameter("i_v_indusu", OracleTypes.INTEGER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf", noConformidad.getIdNoConformidad())
				.addValue("i_v_idenprob", noConformidad.getDescripcionIdentifProblema())
				.addValue("i_v_accinme", noConformidad.getDescripcionAccionInmediata())
				.addValue("i_v_obsana", noConformidad.getDescripcionObservacion())
				.addValue("i_v_anacaus", noConformidad.getDescripcionAnalisisCausa())
				.addValue("i_v_crianacau", noConformidad.getCriticaAnalisisCausa())
				.addValue("i_v_criaccinm", noConformidad.getCriticaAccionInmediata())
				.addValue("i_v_coverest", noConformidad.getComentarioVerificacion())
				.addValue("i_v_cocierre", noConformidad.getComentarioCierre())
				.addValue("i_a_v_usumod", "AGI")
				.addValue("i_v_etapa", noConformidad.getNumeroTab())
				.addValue("i_v_indusu", noConformidad.getIndicadorUsuario());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado != 0) {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return noConformidad;
	}
	
	@Override
	public NoConformidad insertarNoCoformidadSeguimiento(Long idNoConformidad, NoConformidad noConformidad) {
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NO_CONF_SEGU_GUARDAR")
				.declareParameters(
						new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
						new SqlParameter("i_v_etapsegu", OracleTypes.VARCHAR),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),						
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf", idNoConformidad)
				.addValue("i_v_etapsegu", noConformidad.getIdEtapa())
				.addValue("i_vusuario", "PRUEBA");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado != 0) {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return noConformidad;
	}
	
	@Override
	public void actualizarDatosPlanAccion(List<PlanAccion> listaPlanAccion, Long idNoConformidad) {		
		try {
			PlanAccion beanPlanAccion = null;
			if(listaPlanAccion.size() > 0) {
				for(PlanAccion planAccion: listaPlanAccion) {
					beanPlanAccion = new PlanAccion();
					if(planAccion.getEstadoRegistro().equals("0")) {
						beanPlanAccion.setIdNoConformidad(idNoConformidad);
						beanPlanAccion.setIdPlanAccion(planAccion.getIdPlanAccion());
						beanPlanAccion.setEstadoRegistro(planAccion.getEstadoRegistro());
						boolean eliminacion = this.eliminarPlanAccion(beanPlanAccion);
					} else if(planAccion.getEstadoRegistro().equals("1")) {						
						this.agregarPlanAccion(planAccion, idNoConformidad	);
					} else {
						this.modificarPlanAccion(planAccion);
					}
				}
			}
		} catch(Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en NoConformidadDAOImpl.actualizarDatosPlanAccion";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
	}
	
	@Override
	public boolean eliminarPlanAccion(PlanAccion beanPlanAccion) {		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NOCONF_PLANACCI_ELIMINAR")
				.declareParameters(
					new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
					new SqlParameter("i_n_idplanacci", OracleTypes.NUMBER),
					new SqlParameter("i_v_estaccpro", OracleTypes.VARCHAR),
					new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf", beanPlanAccion.getIdNoConformidad())
				.addValue("i_n_idplanacci",	beanPlanAccion.getIdPlanAccion())
				.addValue("i_v_estaccpro", beanPlanAccion.getEstadoRegistro())
				//.addValue("i_a_v_usumod", beanPlanAccion.getUsuarioModificacion());
				.addValue("i_a_v_usumod", "AGI");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {				
				return true;
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);				
				return false;
			}
		} catch(Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en NoConformidadDAOImpl.eliminarPlanAccion";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	
	@Override
	public void agregarPlanAccion(PlanAccion planAccion, Long idNoConformidad) {		
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NOCONF_PLANACCI_GUARDAR")
				.declareParameters(
					new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
					new SqlParameter("i_v_accipro", OracleTypes.VARCHAR),
					new SqlParameter("i_v_respacc", OracleTypes.VARCHAR),
					new SqlParameter("i_d_fechcump", OracleTypes.DATE),
					new SqlParameter("i_v_critacc", OracleTypes.VARCHAR),
					new SqlParameter("i_v_estaccpro", OracleTypes.VARCHAR),
					new SqlParameter("i_v_accejec", OracleTypes.VARCHAR),
					new SqlParameter("i_d_fechejec", OracleTypes.DATE),
					new SqlParameter("i_v_veriejec", OracleTypes.VARCHAR),
					new SqlParameter("i_a_v_usucre", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf", idNoConformidad)
				.addValue("i_v_accipro", planAccion.getDescripcionAccionPropuesta())
				.addValue("i_v_respacc", planAccion.getDescripcionResponsable())
				.addValue("i_d_fechcump", planAccion.getFechaCumplimiento())
				.addValue("i_v_critacc", planAccion.getDescripcionCritica())
				.addValue("i_v_estaccpro", planAccion.getEstadoRegistro())
				.addValue("i_v_accejec", planAccion.getDescripcionAccionEjecutada())
				.addValue("i_d_fechejec", planAccion.getFechaEjecucion())
				.addValue("i_v_veriejec", planAccion.getDescripcionVerificacion())
				//.addValue("i_a_v_usucre", planAccion.getUsuarioCreacion());
				.addValue("i_a_v_usucre", "AGI");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {				
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
	}
	
	@Override
	public void modificarPlanAccion(PlanAccion planAccion){
		
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NOCONF_PLANACCI_UPDATE")
				.declareParameters(
					new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
					new SqlParameter("i_n_idplanacci", OracleTypes.NUMBER),
					new SqlParameter("i_v_accipro", OracleTypes.VARCHAR),
					new SqlParameter("i_d_fechcump", OracleTypes.DATE),
					new SqlParameter("i_v_critacc", OracleTypes.VARCHAR),
					new SqlParameter("i_v_estaccpro", OracleTypes.VARCHAR),
					new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf", planAccion.getIdNoConformidad())
				.addValue("i_n_idplanacci",	planAccion.getIdPlanAccion())
				.addValue("i_v_accipro", planAccion.getDescripcionAccionPropuesta())
				.addValue("i_d_fechcump", planAccion.getFechaCumplimiento())
				.addValue("i_v_critacc", planAccion.getDescripcionCritica())
				.addValue("i_v_estaccpro", "1")
				//.addValue("i_a_v_usumod", planAccion.getUsuarioModificacion());
				.addValue("i_a_v_usumod", "AGI");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {				
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);				
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
	}
	
	@Override
	public void actualizarDatosEjecucion(List<PlanAccionEjecucion> listaEjecucion, Long idPlanAccion) {		
		try {
			if(listaEjecucion.size() > 0) {
				for(PlanAccionEjecucion ejecucion: listaEjecucion) {
					if(ejecucion.getEstadoRegistro().equals("1")) {
						this.agregarEjecucion(ejecucion, idPlanAccion);
					} else {
						this.modificarEjecucion(ejecucion);
					}
				}
			}
		} catch(Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en NoConformidadDAOImpl.actualizarDatosEjecucion";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
	}
	
	@Override
	public void agregarEjecucion(PlanAccionEjecucion ejecucion, Long idPlanAccion) {		
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NOCONF_EJECU_GUARDAR")
				.declareParameters(
					new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
					new SqlParameter("i_v_respacc", OracleTypes.VARCHAR),
					new SqlParameter("i_d_fechcump", OracleTypes.DATE),
					new SqlParameter("i_v_estaccpro", OracleTypes.VARCHAR),
					new SqlParameter("i_v_accejec", OracleTypes.VARCHAR),
					new SqlParameter("i_d_fechejec", OracleTypes.DATE),
					new SqlParameter("i_v_veriejec", OracleTypes.VARCHAR),
					new SqlParameter("i_a_v_usucre", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf", idPlanAccion)
				.addValue("i_v_respacc", ejecucion.getDescripcionResponsable())
				.addValue("i_d_fechcump", ejecucion.getFechaCumplimiento())
				.addValue("i_v_estaccpro", ejecucion.getEstadoRegistro())
				.addValue("i_v_accejec", ejecucion.getDescripcionAccionEjecutada())
				.addValue("i_d_fechejec", ejecucion.getFechaEjecucion())
				.addValue("i_v_veriejec", ejecucion.getDescripcionVerificacion())
				//.addValue("i_a_v_usucre", ejecucion.getUsuarioCreacion());
				.addValue("i_a_v_usucre", "AGI");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {				
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);				
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
	}
	
	@Override
	public void modificarEjecucion(PlanAccionEjecucion ejecucion) {		
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NOCONF_EJECU_UPDATE")
				.declareParameters(
					new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
					new SqlParameter("i_n_idplanacci", OracleTypes.NUMBER),
					new SqlParameter("i_d_fechcump", OracleTypes.DATE),
					new SqlParameter("i_v_estaccpro", OracleTypes.VARCHAR),
					new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idplanacci",	ejecucion.getIdPlanAccion())
				.addValue("i_d_fechcump", ejecucion.getFechaCumplimiento())
				.addValue("i_v_estaccpro", "1")
				//.addValue("i_a_v_usumod", planAccion.getUsuarioModificacion());
				.addValue("i_a_v_usumod", "AGI");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {				
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);				
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
	}
	
	@Override
	public List<PlanAccionEjecucion> obtenerEjecucion(Long idNoConformidad, PageRequest pageRequest){
		Map<String, Object> out = null;
		List<PlanAccionEjecucion> listaEjecucion = new ArrayList<>();
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NOCONF_EJECU_OBTENER")
				.declareParameters(
						new SqlParameter("n_idplanacci", OracleTypes.NUMBER),
						new SqlParameter("n_idacciejec", OracleTypes.NUMBER),
						new SqlParameter("v_accipro", OracleTypes.VARCHAR),
						new SqlParameter("v_accejec", OracleTypes.VARCHAR),
						new SqlParameter("d_fechejec", OracleTypes.DATE),
						new SqlParameter("v_veriejec", OracleTypes.VARCHAR),
						new SqlParameter("v_respacc", OracleTypes.VARCHAR),
						new SqlParameter("d_fechcump", OracleTypes.DATE),
						new SqlParameter("v_estaccpro", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf",idNoConformidad)
				.addValue("n_idplanacci", null)
				.addValue("n_idacciejec", null)
				.addValue("v_accipro", null)
				.addValue("v_accejec", null)
				.addValue("d_fechejec", null)
				.addValue("v_veriejec", null)
				.addValue("v_respacc", null)
				.addValue("d_fechcump", null)
				.addValue("v_estaccpro", null)
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				listaEjecucion = mapearEjecucion(out);
				
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);
				
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}		
		return listaEjecucion;
	}
	
	public List<PlanAccionEjecucion> mapearEjecucion(Map<String, Object> resultados){
		List<PlanAccionEjecucion> listaEjecucion = new ArrayList<>();
		List<Map<String,Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		PlanAccionEjecucion ejecucion = null;
		for(Map<String, Object> map : lista) {
			ejecucion = new PlanAccionEjecucion();
			if(map.get("n_idplanacci") != null) {
				ejecucion.setIdPlanAccion(((BigDecimal)map.get("n_idplanacci")).longValue());
			}
			if(map.get("n_idacciejec") != null) {
				ejecucion.setIdEjecucion(((BigDecimal)map.get("n_idacciejec")).longValue());
			}
			if(map.get("v_accipro") != null) {
				ejecucion.setDescripcionAccionPropuesta((String)map.get("v_accipro"));
			}
			if(map.get("v_accejec") != null) {
				ejecucion.setDescripcionAccionEjecutada((String)map.get("v_accejec"));
			}
			if(map.get("d_fechejec") != null) {
				ejecucion.setFechaEjecucion((Date)map.get("d_fechejec"));
			}
			if(map.get("v_veriejec") != null) {
				ejecucion.setDescripcionVerificacion((String)map.get("v_veriejec"));
			}
			if(map.get("v_respacc") != null) {
				ejecucion.setDescripcionResponsable((String)map.get("v_respacc"));
			}
			if(map.get("d_fechcump") != null) {
				ejecucion.setFechaCumplimiento((Date)map.get("d_fechcump"));
			}
			if(map.get("v_estaccpro") != null) {
				ejecucion.setEstadoRegistro((String)map.get("v_estaccpro"));
			}
			listaEjecucion.add(ejecucion);
			
			if(map.get("RESULT_COUNT")!= null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaEjecucion;
	}
}