package pe.com.sedapal.agi.dao.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pe.com.sedapal.agi.model.response_objects.Error;

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
import pe.com.sedapal.agi.dao.IConstanteDAO;
import pe.com.sedapal.agi.dao.IParametroDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.PlanAccion;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ParametroRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.model.enums.EstadoConstante;

@Repository
public class ParametroDAOImpl implements IParametroDAO {
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;
	
	private static final Logger LOGGER = Logger.getLogger(ParametroDAOImpl.class);	
	
	
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
	
	@SuppressWarnings("unchecked")
	public List<Constante> mapearConstantes(Map<String, Object> resultados) {
		List<Constante> listaConstantes = new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Constante item = null;
		
		for(Map<String, Object> map : lista) {			
			item = new Constante();
			if(map.get("N_IDCONS")!=null) {
			item.setIdconstante(((BigDecimal)map.get("N_IDCONS")).longValue());
			}
			if(map.get("itemColumna")!=null) {
				item.setItemColumna(((BigDecimal)map.get("itemColumna")).longValue());
			}
			if ((map.get("N_IDCONSSUPE"))!=null) {
				item.setIdconstantesuper(((BigDecimal)map.get("N_IDCONSSUPE")).longValue());	
			}			
			item.setV_nomconssupe((String)map.get("V_NOMCONSSUPE"));
			item.setV_nomcons((String)map.get("V_NOMCONS"));
			item.setV_descons((String)map.get("V_DESCONS"));			
			item.setV_valcons((String)map.get("V_VALCONS"));
			item.setV_abrecons((String)map.get("V_ABRECONS"));
			item.setV_campcons1((String)map.get("V_CAMPCONS1"));
			item.setV_campcons2((String)map.get("V_CAMPCONS2"));
			item.setV_campcons3((String)map.get("V_CAMPCONS3"));
			item.setV_campcons4((String)map.get("V_CAMPCONS4"));
			item.setV_campcons5((String)map.get("V_CAMPCONS5"));
			item.setV_campcons6((String)map.get("V_CAMPCONS6"));
			item.setV_campcons7((String)map.get("V_CAMPCONS7"));
			item.setV_campcons8((String)map.get("V_CAMPCONS8"));			
			item.setN_discons(((BigDecimal)map.get("N_DISCONS")).longValue());	
			item.setEstadoConstante(EstadoConstante.setEstado((BigDecimal) map.get("N_DISCONS")));		
			listaConstantes.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaConstantes;
	}
	
	@Override
	public List<Constante> obtenerConstantes(ParametroRequest parametroRequest, PageRequest pageRequest, Long codigo) {
		Map<String, Object> out = null;
		List<Constante> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_PARAMETRO_OBTENER")
				//PCK_AGI_PlANTILLA
				.declareParameters(										
						new SqlParameter("i_vpadre", OracleTypes.VARCHAR),							
						new SqlParameter("i_vdescripcion", OracleTypes.VARCHAR),
						new SqlParameter("v_idconssupe", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),						
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));		
		SqlParameterSource in = new MapSqlParameterSource()		
		.addValue("i_vpadre",parametroRequest.getV_nomcons())		
		.addValue("i_vdescripcion",parametroRequest.getV_descons())
		.addValue("v_idconssupe",parametroRequest.getIdconstantesuper())	
		.addValue("i_npagina",pageRequest.getPagina())
		.addValue("i_nregistros",pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)  out.get("o_retorno");			
			if(resultado == 0) {
				lista = mapearConstantes(out);
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
	
	private List<Constante> mapearConstante(Map<String, Object> resultados){
		Constante item = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		List<Constante> listaConstante = new ArrayList<>();
		for(Map<String, Object> map : lista) {
			item = new Constante();

			item.setN_idcons(((BigDecimal)map.get("N_IDCONS")).longValue());
			item.setN_idconssupe(((BigDecimal)map.get("N_IDCONSSUPE")).longValue());
			item.setV_nomconssupe((String)map.get("V_NOMCONSSUPE"));
			item.setV_nomcons((String)map.get("V_NOMCONS"));
			item.setV_valcons((String)map.get("V_VALCONS"));
			item.setV_abrecons((String)map.get("V_ABRECONS"));
			item.setV_campcons1((String)map.get("V_CAMPCONS1"));
			item.setV_campcons2((String)map.get("V_CAMPCONS2"));
			item.setV_campcons3((String)map.get("V_CAMPCONS3"));
			item.setV_campcons4((String)map.get("V_CAMPCONS4"));
			item.setV_campcons5((String)map.get("V_CAMPCONS5"));
			item.setV_campcons6((String)map.get("V_CAMPCONS6"));
			item.setV_campcons7((String)map.get("V_CAMPCONS7"));
			item.setV_campcons8((String)map.get("V_CAMPCONS8"));
			if ((map.get("N_DISCONS"))!=null) {
				item.setN_discons(((BigDecimal)map.get("N_DISCONS")).longValue());
				item.setEstadoConstante(EstadoConstante.setEstado((BigDecimal) map.get("N_DISCONS")));
			}
			item.setV_descons((String)map.get("V_DESCONS"));
			listaConstante.add(item);
		}
		return listaConstante;
	}
	
	@Override
	public Constante actualizarConstante(Long id, Long i_nidpadre, String i_vcampo1, Constante constante) {
		Map<String, Object> out = null;
		List<Constante> lista = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_CONSTANTE_GUARDAR")
				.declareParameters(
//						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlParameter("i_nid", OracleTypes.NUMBER),	
						new SqlParameter("i_nidpadre", OracleTypes.NUMBER),	
						new SqlParameter("i_vnombpadre", OracleTypes.VARCHAR),
						new SqlParameter("i_vnombre", OracleTypes.VARCHAR),
						new SqlParameter("i_vdescripcion", OracleTypes.VARCHAR),
						new SqlParameter("i_vvalor", OracleTypes.NUMBER),
						new SqlParameter("i_vabreviatura", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo1", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo2", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo3", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo4", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo5", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo6", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo7", OracleTypes.VARCHAR),
						new SqlParameter("i_vcampo8", OracleTypes.VARCHAR),
						new SqlParameter("i_ndisponible", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		
		SqlParameterSource in = new MapSqlParameterSource()
		
		.addValue("i_nid", id)
		.addValue("i_nidpadre", i_nidpadre)		
		.addValue("i_vnombpadre", null)
		.addValue("i_vnombre", null)
		.addValue("i_vdescripcion", null)
		.addValue("i_vvalor", null)
		.addValue("i_vabreviatura", null)
		.addValue("i_vcampo1", i_vcampo1)
		.addValue("i_vcampo2", null)
		.addValue("i_vcampo3", null)
		.addValue("i_vcampo4", null)
		.addValue("i_vcampo5", null)
		.addValue("i_vcampo6", null)
		.addValue("i_vcampo7", null)
		.addValue("i_vcampo8", null)
		.addValue("i_ndisponible", null)
		.addValue("i_vusuario", "AGI");
		out = this.jdbcCall.execute(in);
		lista = mapearConstante(out);
		return lista.get(0);
	}
	
	//Eliminar	
	@Override
	public Boolean eliminarConstante(Long id) {
		List<Constante> lista = new ArrayList<>();		
		Map<String, Object> out = null;
		if(id == null) {
			id = 0L;
		}	
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("pck_agi_mantenimiento")
				.withProcedureName("prc_constante_eliminar")
				.declareParameters(
						new SqlParameter("i_nid", OracleTypes.NUMBER),						
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
						SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_nid", id);
						//.addValue("i_vresact", usuario);	
			out=this.jdbcCall.execute(in);
			Boolean retorno = true;		
		try {
			out=this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				retorno = true;
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);
				retorno = false;
			}			
		}
		catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			retorno = false;
		}
		return retorno;
	}
	
	@Override
	public Constante actualizarParametro(Constante constante) {		
			Constante para= null;
		try {		  				
			
			if((constante.getV_nomcons()!="" || constante.getV_nomcons()!=null) ||( constante.getV_descons()!=null || constante.getV_descons()!="")) {
				constante.getIdconstante();
				constante.getV_nomcons();
				constante.getV_descons();
				constante.setIdconstantesuper(null);
				
				constante.setDispEstado("1");
				
			para=this.agregarParametro(constante);
				
			}
			if(constante.getListaDetalle().size()>0) {
				for(Constante consta: constante.getListaDetalle()){
					if(consta.getIdconstantesuper()==null || consta.getIdconstantesuper()==0) {
						consta.setIdconstantesuper(para.getIdconstante());
						this.agregarParametro(consta);
					}else {
						this.agregarParametro(consta);
					}	
				}
			}
		} catch(Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en ParametroDAOImpl.actualizarParametro";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		return para;
	}
	
	@Override
	public Constante agregarParametro(Constante parametro){		
		Map<String, Object> out = null;
		
		Constante constante=null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_PARAMETRO_GUARDAR")
				.declareParameters(
					new SqlParameter("i_nid", OracleTypes.NUMBER),
					new SqlParameter("i_nidpadre", OracleTypes.NUMBER),
					new SqlParameter("i_vnombre", OracleTypes.VARCHAR),
					new SqlParameter("i_vdescripcion", OracleTypes.VARCHAR),
					new SqlParameter("i_vvalor", OracleTypes.VARCHAR),
					new SqlParameter("i_vabreviatura", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo1", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo2", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo3", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo4", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo5", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo6", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo7", OracleTypes.VARCHAR),
					new SqlParameter("i_vcampo8", OracleTypes.VARCHAR),
					new SqlParameter("i_ndisponible", OracleTypes.NUMBER),
					new SqlOutParameter("o_idcons", OracleTypes.INTEGER),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		
		
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nid", parametro.getIdconstante())
				.addValue("i_nidpadre", parametro.getIdconstantesuper())
				.addValue("i_vnombre", parametro.getV_nomcons())
				.addValue("i_vdescripcion", parametro.getV_descons())
				.addValue("i_vvalor", parametro.getV_valcons())
				.addValue("i_vabreviatura", parametro.getV_abrecons())
				.addValue("i_vcampo1", parametro.getV_campcons1())
				.addValue("i_vcampo2", parametro.getV_campcons2())
				.addValue("i_vcampo3", parametro.getV_campcons3())
				.addValue("i_vcampo4", parametro.getV_campcons4())
				.addValue("i_vcampo5", parametro.getV_campcons5())
				.addValue("i_vcampo6", parametro.getV_campcons6())
				.addValue("i_vcampo7", parametro.getV_campcons7())
				.addValue("i_vcampo8", parametro.getV_campcons8())				
				.addValue("i_ndisponible", parametro.getDispEstado());					
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			Integer idConstante =(Integer)out.get("o_idcons");
			
			if(resultado == 0) {				
				if(parametro.getIdconstantesuper()==null || parametro.getIdconstantesuper()==0) {
					Long x;
					x=Long.valueOf(idConstante);
					System.out.println(idConstante);
					parametro.setIdconstante(x);
				}
				
				constante = parametro;
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
		return constante;
	}
	
	/*
	@Override
	public void modificarPlanAccion(PlanAccion planAccion) {
		logger.msgInfoInicio("AGI - Inicio NoConformidadDAOImpl.modificarPlanAccion()");
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
				logger.msgInfoFin("AGI - Fin NoConformidadDAOImpl.modificarPlanAccion()");
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				logger.msgInfo("AGI - Error NoConformidadDAOImpl.modificarPlanAccion(): ", mensajeInterno);
				logger.msgInfoFin("AGI - Fin NoConformidadDAOImpl.modificarPlanAccion()");
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			logger.msgError("AGI - Error NoConformidadDAOImpl.modificarPlanAccion(): ", e.getMessage(), e);
		}
	}
	*/
}
