package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.IPlanAccionDAO;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.request_objects.PlanAccionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;

@Repository
public class PlanAccionDAOImpl implements IPlanAccionDAO{
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;
	
	public Paginacion getPaginacion() {
		return this.paginacion;
	}	
	public Error getError() {
		return this.error;
	}
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public List<NoConformidad> obtenerNoConformidad(PlanAccionRequest planAccionRequest, PageRequest pageRequest){
		Map<String, Object> out = null;
		List<NoConformidad> listaNoConformidad = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_NO_CONF_USUA_OBTENER")
				.declareParameters(
						new SqlParameter("n_idnoconf", OracleTypes.NUMBER),
						new SqlParameter("v_tiponc", OracleTypes.VARCHAR),
						new SqlParameter("v_descgere", OracleTypes.VARCHAR),
						new SqlParameter("v_descnorm", OracleTypes.VARCHAR),
						new SqlParameter("v_desalca", OracleTypes.VARCHAR),
						new SqlParameter("v_ordereq", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("n_idnoconf",null)
				.addValue("v_tiponc",null)
				.addValue("v_descgere",null)
				.addValue("v_descnorm",null)
				.addValue("v_desalca",null)
				.addValue("v_ordereq",null)
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				listaNoConformidad = mapearNoConformidad(out);
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
		}
		return listaNoConformidad;
	}
	
	public List<NoConformidad> mapearNoConformidad(Map<String, Object> resultados){
		List<NoConformidad> listaNoConformidad = new ArrayList<>();
		List<Map<String,Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		NoConformidad noConformidad = null;
		
		for(Map<String, Object> map : lista) {
			noConformidad = new NoConformidad();
			noConformidad.setIdNoConformidad(((BigDecimal)map.get("n_idnoconf")).longValue());
			noConformidad.setDescripcionTipoNoConformidad((String)map.get("v_tiponc"));
			noConformidad.setDescripcionGerencia((String)map.get("v_descgere"));
			noConformidad.setDescripcionNorma((String)map.get("v_descnorm"));
			noConformidad.setDescripcionAlcance((String)map.get("v_desalca"));
			noConformidad.setDescripcionRequisito((String)map.get("v_ordereq"));
			listaNoConformidad.add(noConformidad);
			
			if(map.get("RESULT_COUNT")!= null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaNoConformidad;
	}
	
	@Override
	public List<PlanAccion> obtenerPlanAccion(Long idNoConformidad, PageRequest pageRequest){
		Map<String, Object> out = null;
		List<PlanAccion> listaPlanAccion = new ArrayList<>();
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_PLAN_ACCION_OBTENER")
				.declareParameters(
						new SqlParameter("n_idnoconf", OracleTypes.NUMBER),
						new SqlParameter("n_idplanacci", OracleTypes.NUMBER),
						new SqlParameter("v_accipro", OracleTypes.VARCHAR),
						new SqlParameter("v_respacc", OracleTypes.VARCHAR),
						new SqlParameter("d_fechcump", OracleTypes.DATE),
						new SqlParameter("v_estaccpro", OracleTypes.VARCHAR),
						new SqlParameter("v_critacc", OracleTypes.VARCHAR),
						new SqlParameter("v_accejec", OracleTypes.VARCHAR),
						new SqlParameter("d_fechejec", OracleTypes.DATE),
						new SqlParameter("v_veriejec", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idnoconf",idNoConformidad)
				.addValue("n_idplanacci",null)
				.addValue("v_accipro",null)
				.addValue("v_respacc",null)
				.addValue("d_fechcump",null)
				.addValue("v_estaccpro", null)
				.addValue("v_critacc",null)
				.addValue("v_accejec",null)
				.addValue("d_fechejec",null)
				.addValue("v_veriejec", null)
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				listaPlanAccion = mapearPlanAccion(out);
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
		}
		return listaPlanAccion;
	}
	
	public List<PlanAccion> mapearPlanAccion(Map<String, Object> resultados){
		List<PlanAccion> listaPlanAccion = new ArrayList<>();
		List<Map<String,Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		PlanAccion planAccion = null;
		
		for(Map<String, Object> map : lista) {
			planAccion = new PlanAccion();
			if(map.get("n_idnoconf") != null) {
				planAccion.setIdNoConformidad(((BigDecimal)map.get("n_idnoconf")).longValue());
			}
			if(map.get("n_idplanacci") != null) {
				planAccion.setIdPlanAccion(((BigDecimal)map.get("n_idplanacci")).longValue());
			}
			if(map.get("v_accipro") != null) {
				planAccion.setDescripcionAccionPropuesta((String)map.get("v_accipro"));
			}
			if(map.get("v_respacc") != null) {
				planAccion.setDescripcionResponsable((String)map.get("v_respacc"));
			}
			if(map.get("d_fechcump") != null) {
				planAccion.setFechaCumplimiento((Date)map.get("d_fechcump"));
			}
			if(map.get("v_estaccpro") != null) {
				planAccion.setEstadoRegistro((String)map.get("v_estaccpro"));
			}
			if(map.get("v_critacc") != null) {
				planAccion.setDescripcionCritica((String)map.get("v_critacc"));
			}
			if(map.get("v_accejec") != null) {
				planAccion.setDescripcionAccionEjecutada((String)map.get("v_accejec"));
			}
			if(map.get("d_fechejec") != null) {
				planAccion.setFechaEjecucion((Date)map.get("d_fechejec"));
			}
			if(map.get("v_veriejec") != null) {
				planAccion.setDescripcionVerificacion((String)map.get("v_veriejec"));
			}
			listaPlanAccion.add(planAccion);
			
			if(map.get("RESULT_COUNT")!= null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaPlanAccion;
	}
	
	@Override
	public PlanAccion actualizarPlanAccion(Long idPlanAccion, PlanAccion planAccion) {
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
				.withProcedureName("PRC_PLAN_ACCION_GUARDAR")
				.declareParameters(
						new SqlParameter("i_n_idplanacci", OracleTypes.NUMBER),
						new SqlParameter("i_n_idnoconf", OracleTypes.NUMBER),
						new SqlParameter("i_d_fechejec", OracleTypes.DATE),
						new SqlParameter("i_v_accejec", OracleTypes.VARCHAR),
						new SqlParameter("i_v_critacc", OracleTypes.VARCHAR),
						new SqlParameter("i_v_estaccpro", OracleTypes.VARCHAR),
						new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
						new SqlParameter("i_v_veriejec", OracleTypes.VARCHAR),
						new SqlParameter("i_v_accion", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idplanacci", idPlanAccion)
				.addValue("i_n_idnoconf", planAccion.getIdNoConformidad())
				.addValue("i_d_fechejec", planAccion.getFechaEjecucion())
				.addValue("i_v_accejec", planAccion.getDescripcionAccionEjecutada())
				.addValue("i_v_critacc", planAccion.getDescripcionCritica())
				.addValue("i_v_estaccpro", planAccion.getEstadoRegistro())
				.addValue("i_a_v_usumod", "AGI")
				.addValue("i_v_veriejec", planAccion.getDescripcionVerificacion())
				.addValue("i_v_accion", planAccion.getValorAccion());
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
		return planAccion;
	}
	
	//ejecucion
		public List<PlanAccion> obtenerListaAccionPropuesta(Long idNoConformidad) {			
			Map<String, Object> out = null;
			List<PlanAccion> listaPlanAccion = new ArrayList<>();
			this.paginacion = new Paginacion();	
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_NO_CONFORMIDADES")
					.withProcedureName("PRC_PLAN_ACCION_OBTENER")
					.declareParameters(
							new SqlParameter("n_idnoconf", OracleTypes.NUMBER),
							new SqlParameter("n_idplanacci", OracleTypes.NUMBER),
							new SqlParameter("v_accipro", OracleTypes.VARCHAR),
							new SqlParameter("v_respacc", OracleTypes.VARCHAR),
							new SqlParameter("d_fechcump", OracleTypes.DATE),
							new SqlParameter("v_estaccpro", OracleTypes.VARCHAR),
							new SqlParameter("v_critacc", OracleTypes.VARCHAR),
							new SqlParameter("v_accejec", OracleTypes.VARCHAR),
							new SqlParameter("d_fechejec", OracleTypes.DATE),
							new SqlParameter("v_veriejec", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_n_idnoconf",idNoConformidad)
					.addValue("n_idplanacci",null)
					.addValue("v_accipro",null)
					.addValue("v_respacc",null)
					.addValue("d_fechcump",null)
					.addValue("v_estaccpro", null)
					.addValue("v_critacc",null)
					.addValue("v_accejec",null)
					.addValue("d_fechejec",null)
					.addValue("v_veriejec", null)
					.addValue("i_npagina",0)
					.addValue("i_nregistros",0);
			try {
				out = this.jdbcCall.execute(in);
				Integer resultado = (Integer)out.get("o_retorno");
				if(resultado == 0) {
					listaPlanAccion = new ArrayList<>();
					listaPlanAccion = mapearPlanAccion(out);
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
			}
			return listaPlanAccion;
	}
}
