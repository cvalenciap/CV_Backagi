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
import pe.com.sedapal.agi.dao.IDetEncuestaDAO;
import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class DetEncuestaDAOImpl implements IDetEncuestaDAO{
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

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
	
	@Override
	public List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest constanteRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<DetEncuesta> lista = new ArrayList<>();
		this.error=null;
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO_JJ")
				.withProcedureName("PRC_DET_ENCUESTA_OBTENER")
				.declareParameters(
						new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),                        
						new SqlParameter("i_nidencu", OracleTypes.NUMBER),                         
						new SqlParameter("i_vdespre", OracleTypes.VARCHAR), 
						new SqlParameter("i_vcodetaencu", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));     
				SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_niddetaencu", constanteRequest.getIniddetaencu())	
				.addValue("i_nidencu", constanteRequest.getInidencu())
				.addValue("i_vdespre", constanteRequest.getIvdespre())
				.addValue("i_vcodetaencu", constanteRequest.getIvcodetaencu())
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");			
			if(resultado == 0) {
				lista = mapearDetEncuestas(out);
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
	
	public List<DetEncuesta> mapearDetEncuestas(Map<String, Object> resultados) {
		
		List<DetEncuesta> listaDetEncuesta = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		DetEncuesta item = null;
		
		for(Map<String, Object> map : lista) {
			item = new DetEncuesta();
			
			if(map.get("N_IDDETAENCU")!=null)  item.setNiddetaencu(((BigDecimal)map.get("N_IDDETAENCU")).longValue());   
			if(map.get("V_CODETAENCU")!=null)  item.setVcodetaencu((String)map.get("V_CODETAENCU"));
			if(map.get("V_DESPRE")!=null)  item.setVdespre((String)map.get("V_DESPRE"));    
			if(map.get("N_IDENCU")!=null)  item.setNidencu(((BigDecimal)map.get("N_IDENCU")).longValue());      
			if(map.get("N_DES_CUR")!=null)  item.setN_des_cur(((BigDecimal)map.get("N_DES_CUR")).longValue());     
			if(map.get("N_DISDETENC")!=null)  item.setNdisdetenc(((BigDecimal)map.get("N_DISDETENC")).longValue());   
			if(map.get("A_V_USUCRE")!=null)  item.setAvusucre((String)map.get("A_V_USUCRE"));  
			if(map.get("A_D_FECCRE")!=null)  item.setAdfeccre((Date)map.get("A_D_FECCRE"));  
			if(map.get("A_V_USUMOD")!=null)  item.setAvusumod((String)map.get("A_V_USUMOD"));  
			if(map.get("A_D_FECMOD")!=null)  item.setAdfecmod((Date)map.get("A_D_FECMOD"));  
			if(map.get("A_V_NOMPRG")!=null)  item.setAv_nomprg((String)map.get("A_V_NOMPRG"));  
			
			listaDetEncuesta.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		
		return listaDetEncuesta;
	}
	
	
	private List<DetEncuesta> mapearDetEncuesta (Map<String, Object> resultados){
		DetEncuesta item = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		List<DetEncuesta> listaDetEncuesta = new ArrayList<>();
		for(Map<String, Object> map : lista) {
			
			item = new DetEncuesta();

			if(map.get("N_IDDETAENCU")!=null)  item.setNiddetaencu(((BigDecimal)map.get("N_IDDETAENCU")).longValue());   
			if(map.get("V_CODETAENCU")!=null)  item.setVcodetaencu((String)map.get("V_CODETAENCU"));
			if(map.get("V_DESPRE")!=null)  item.setVdespre((String)map.get("V_DESPRE"));    
			if(map.get("N_IDENCU")!=null)  item.setNidencu(((BigDecimal)map.get("N_IDENCU")).longValue());      
			if(map.get("N_DES_CUR")!=null)  item.setN_des_cur(((BigDecimal)map.get("N_DES_CUR")).longValue());     
			if(map.get("N_DISDETENC")!=null)  item.setNdisdetenc(((BigDecimal)map.get("N_DISDETENC")).longValue());   
			if(map.get("A_V_USUCRE")!=null)  item.setAvusucre((String)map.get("A_V_USUCRE"));  
			if(map.get("A_D_FECCRE")!=null)  item.setAdfeccre((Date)map.get("A_D_FECCRE"));  
			if(map.get("A_V_USUMOD")!=null)  item.setAvusumod((String)map.get("A_V_USUMOD"));  
			if(map.get("A_D_FECMOD")!=null)  item.setAdfecmod((Date)map.get("A_D_FECMOD"));  
			if(map.get("A_V_NOMPRG")!=null)  item.setAv_nomprg((String)map.get("A_V_NOMPRG")); 
			
			listaDetEncuesta.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaDetEncuesta;
	}
		
	
		
	@Override
	public DetEncuesta actualizarDetEncuesta(Long id, DetEncuesta detEncuesta) {
		Map<String, Object> out = null;
		List<DetEncuesta> lista = new ArrayList<>();
		this.error=null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO_JJ")
				.withProcedureName("PRC_DET_ENCUESTA_GUARDAR")
				.declareParameters(
						new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),
						new SqlParameter("i_nidencu", OracleTypes.NUMBER),
						new SqlParameter("i_vdespre", OracleTypes.VARCHAR),
						new SqlParameter("i_vcodetaencu", OracleTypes.VARCHAR),
						new SqlParameter("i_ndisdetenc", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_niddetaencu", id)
				.addValue("i_nidencu",detEncuesta.getNidencu())
				.addValue("i_vdespre",detEncuesta.getVdespre())
				.addValue("i_vcodetaencu",detEncuesta.getVcodetaencu())
				.addValue("i_ndisdetenc",detEncuesta.getNdisdetenc())
				.addValue("i_vusuario","AGI");		
		out = this.jdbcCall.execute(in);
		lista = mapearDetEncuesta(out);
		return lista.get(0);
	}

	@Override
	public Boolean eliminarDetEncuesta(Long id) {
		List<DetEncuesta> lista = new ArrayList<>();		
		Map<String, Object> out = null;
		this.error=null;
		if(id == null) {
			id = 0L;
		}	
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO_JJ")
				.withProcedureName("PRC_DET_ENCUESTA_ELIMINAR")
				.declareParameters(
						new SqlParameter("i_niddetEncuesta", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
						SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_niddetEncuesta", id)
						.addValue("i_vusuario", "AGI");	
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
	public DetEncuesta insertarDetEncuesta(DetEncuesta detEncuesta) {
		Map<String, Object> out = null;
		List<DetEncuesta> lista = new ArrayList<>();
		this.error=null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO_JJ")
				.withProcedureName("PRC_DET_ENCUESTA_GUARDAR")
				.declareParameters(
						new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),
						new SqlParameter("i_nidencu", OracleTypes.NUMBER),
						new SqlParameter("i_vdespre", OracleTypes.VARCHAR),
						new SqlParameter("i_vcodetaencu", OracleTypes.VARCHAR),
						new SqlParameter("i_ndisdetenc", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
			SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_niddetaencu", null)
						.addValue("i_nidencu",detEncuesta.getNidencu())
						.addValue("i_vdespre",detEncuesta.getVdespre())
						.addValue("i_vcodetaencu",detEncuesta.getVcodetaencu())
						.addValue("i_ndisdetenc",detEncuesta.getNdisdetenc())
						.addValue("i_vusuario","AGI");	
		out = this.jdbcCall.execute(in);
		lista = mapearDetEncuesta(out);
		return lista.get(0);
	}
	
	
}
