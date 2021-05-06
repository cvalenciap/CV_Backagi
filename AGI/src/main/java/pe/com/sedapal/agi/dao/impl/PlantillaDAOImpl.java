package pe.com.sedapal.agi.dao.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IPlantillaDAO;
import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Plantilla;
import pe.com.sedapal.agi.model.request_objects.ConocimientoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlantillaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
//import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class PlantillaDAOImpl implements IPlantillaDAO {
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	@Autowired
	Environment env;
	
	private String endpointServidor;
	
	public Paginacion getPaginacion() {
		return this.paginacion;
	}	
	public Error getError() {
		return this.error;
	}
	
	
	private static final Logger LOGGER = Logger.getLogger(PlantillaDAOImpl.class);	
	
	
	@Override
	public List<Plantilla> obtenerPlantilla(PlantillaRequest plantillaRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Plantilla> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
				
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_PLANTILLA")
				.withProcedureName("PRC_PLANTILLA_OBTENER")
				.declareParameters(
						new SqlParameter("i_vdesplan", OracleTypes.VARCHAR),						
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),						
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
				
		SqlParameterSource in;
		try {
			in = new MapSqlParameterSource()
					.addValue("i_vdesplan", plantillaRequest.getDesplan())					
					.addValue("i_npagina",pageRequest.getPagina())
					.addValue("i_nregistros",pageRequest.getRegistros());
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				lista = mapearPlantilla(out);
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

	
	
	@SuppressWarnings("unchecked")
	private List<Plantilla> mapearPlantilla(Map<String, Object> resultados) {
		endpointServidor = env.getProperty("app.config.servidor.fileserver");
		List<Plantilla> listaPlantilla = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Plantilla item = null;
		
		for(Map<String, Object> map : lista) {
			item = new Plantilla();
			item.setIdplan(((BigDecimal)map.get("N_IDPLAN")).longValue());
			item.setDesplan((String)map.get("V_DESPLAN"));
			item.setNomplan((String)map.get("V_NOMPLAN"));
			item.setRutplan(endpointServidor + (String)map.get("V_RUTPLAN"));
			item.setDisplan(((BigDecimal)map.get("N_DISPLAN")).longValue());
			item.setTipoplan((String)map.get("V_TIPOPLAN"));
			listaPlantilla.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaPlantilla;
	}

	
	@SuppressWarnings("unchecked")
	private List<Conocimiento> mapearConocimiento(Map<String, Object> resultados) {
		List<Conocimiento> listaConocimiento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Conocimiento item = null;
		
		for(Map<String, Object> map : lista) {
			item = new Conocimiento();
			item.setIdconocimiento(((BigDecimal)map.get("n_idconoci")).longValue());
			item.setIdpersona(((BigDecimal)map.get("n_idpersona")).longValue());
			item.setIddocumento(((BigDecimal)map.get("n_iddocu")).longValue());			
			item.setEstado(((BigDecimal)map.get("n_estado")).longValue());			
			listaConocimiento.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return 			listaConocimiento;
	}
	
	@Override
	public Plantilla agregarPlantilla(Plantilla plantilla) {
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_PLANTILLA")
				.withProcedureName("PRC_PLANTILLA_GUARDAR")
				.declareParameters(
					new SqlParameter("i_a_v_usucre", OracleTypes.VARCHAR),
					new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),					
					new SqlParameter("i_v_desplan", OracleTypes.VARCHAR),
					new SqlParameter("i_v_nomplan", OracleTypes.VARCHAR),
					new SqlParameter("i_v_rutplan", OracleTypes.VARCHAR),
					new SqlParameter("i_n_displan", OracleTypes.NUMBER),	
					new SqlParameter("i_v_tipoplan", OracleTypes.VARCHAR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()				
				.addValue("i_a_v_usucre",  plantilla.getDatosAuditoria().getUsuarioCreacion())
				.addValue("i_a_v_usumod", plantilla.getDatosAuditoria().getUsuarioCreacion())				
				.addValue("i_v_desplan", plantilla.getDesplan())
				.addValue("i_v_nomplan", plantilla.getNomplan())
				.addValue("i_v_rutplan", plantilla.getRutplan())
				.addValue("i_n_displan", 1)
				.addValue("i_v_tipoplan", plantilla.getTipoplan());				
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
		return plantilla;
	}
	
	@Override
	public Boolean eliminarPlantilla(Plantilla plantilla) {		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_PLANTILLA")
				.withProcedureName("PRC_PLANTILLA_ELIMINAR")
				.declareParameters(
					new SqlParameter("i_n_idplan", OracleTypes.NUMBER),					
					new SqlParameter("i_a_v_usucre", OracleTypes.VARCHAR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idplan", plantilla.getIdplan())
				.addValue("i_a_v_usucre", "AGI");
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
			String mensaje = "Error en PlantillaDAOImpl.eliminarPlanAccion()";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	
	@Override
	public Boolean eliminarConocimiento(Conocimiento conocimiento) {		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_PLANTILLA")
				.withProcedureName("PRC_CONOCIMIENTO_ELIMINAR")
				.declareParameters(
					new SqlParameter("i_n_idconoci", OracleTypes.NUMBER),
					new SqlParameter("i_n_idpersona", OracleTypes.NUMBER),					
					new SqlParameter("i_n_iddocu", OracleTypes.NUMBER),					
					new SqlParameter("i_a_v_usucre", OracleTypes.VARCHAR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idconoci", conocimiento.getIdconocimiento())
				.addValue("i_n_idpersona", conocimiento.getIdpersona())
				.addValue("i_n_iddocu", conocimiento.getIddocumento())
				.addValue("i_a_v_usucre", "AGI");
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
			String mensaje = "Error en PlantillaDAOImpl.eliminarPlanAccion()";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	
	
	
	@Override
	public List<Conocimiento> obtenerConocimiento(ConocimientoRequest conocimientoRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Conocimiento> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
				
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_PLANTILLA")
				.withProcedureName("PRC_CONOCIMIENTO_OBTENER")
				.declareParameters(
						new SqlParameter("i_n_idpersona", OracleTypes.NUMBER),						
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),						
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
				
		SqlParameterSource in;
		try {
			in = new MapSqlParameterSource()
					.addValue("i_n_idpersona", conocimientoRequest.getIdpersona())					
					.addValue("i_npagina",pageRequest.getPagina())
					.addValue("i_nregistros",pageRequest.getRegistros());
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				lista = mapearConocimiento(out);
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
	
}