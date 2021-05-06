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
import pe.com.sedapal.agi.dao.IAulaDAO;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.request_objects.AulaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class AulaDAOImpl implements IAulaDAO{
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
	public List<Aula> obtenerAula(AulaRequest constanteRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Aula> lista = new ArrayList<>();
		this.error=null;
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_AULA_OBTENER")
				.declareParameters(
						new SqlParameter("i_vcodaula", OracleTypes.VARCHAR),                        
						new SqlParameter("i_nidaula", OracleTypes.NUMBER),                         
						new SqlParameter("i_vnomaula", OracleTypes.VARCHAR),                        
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));     
				SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_vcodaula", constanteRequest.getIvcodaula())	
				.addValue("i_nidaula", constanteRequest.getInidaula())
				.addValue("i_vnomaula", constanteRequest.getIvnomaula())
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");			
			if(resultado == 0) {
				lista = mapearAulas(out);
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
	
	public List<Aula> mapearAulas(Map<String, Object> resultados) {
		
		List<Aula> listaAula = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Aula item = null;
		
		for(Map<String, Object> map : lista) {
			item = new Aula();
			
			if(map.get("N_IDAULA")!=null)   item.setNidaula(((BigDecimal)map.get("N_IDAULA")).longValue());                   
			if(map.get("V_NOMAULA")!=null)  item.setVnomaula((String)map.get("V_NOMAULA"));                   
			if(map.get("N_CAPAULA")!=null)  item.setNcapaula(((BigDecimal)map.get("N_CAPAULA")).longValue()); 
			if(map.get("N_IDSEDE")!=null)   item.setNidsede(((BigDecimal)map.get("N_IDSEDE")).longValue());
			if(map.get("V_NOMSEDE")!=null)  item.setVnomsede((String)map.get("V_NOMSEDE")); 
			if(map.get("V_CODAULA")!=null)  item.setVcodaula((String)map.get("V_CODAULA"));                   
			if(map.get("N_DISAULA")!=null)  item.setNdisaula(((BigDecimal)map.get("N_DISAULA")).longValue()); 
			if(map.get("A_D_FECCRE")!=null) item.setAd_feccre((Date)map.get("A_D_FECCRE"));                     
			if(map.get("A_V_USUMOD")!=null) item.setAvusumod((String)map.get("A_V_USUMOD"));                   
			if(map.get("A_D_FECMOD")!=null) item.setAd_fecmod((Date)map.get("A_D_FECMOD"));                     
			if(map.get("A_V_NOMPRG")!=null) item.setAvnomprg((String)map.get("A_V_NOMPRG"));                   
			if(map.get("A_V_USUCRE")!=null) item.setAvusucre((String)map.get("A_V_USUCRE")); 
		
			listaAula.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		
		return listaAula;
	}
	
	
	private List<Aula> mapearAula (Map<String, Object> resultados){
		Aula item = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		List<Aula> listaAula = new ArrayList<>();
		for(Map<String, Object> map : lista) {
			
			item = new Aula();

			if(map.get("N_IDAULA")!=null)   item.setNidaula(((BigDecimal)map.get("N_IDAULA")).longValue());                   
			if(map.get("V_NOMAULA")!=null)  item.setVnomaula((String)map.get("V_NOMAULA"));                   
			if(map.get("N_CAPAULA")!=null)  item.setNcapaula(((BigDecimal)map.get("N_CAPAULA")).longValue()); 
			if(map.get("N_IDSEDE")!=null)   item.setNidsede(((BigDecimal)map.get("N_IDSEDE")).longValue());  
			if(map.get("V_CODAULA")!=null)  item.setVcodaula((String)map.get("V_CODAULA"));                   
			if(map.get("N_DISAULA")!=null)  item.setNdisaula(((BigDecimal)map.get("N_DISAULA")).longValue()); 
			if(map.get("A_D_FECCRE")!=null) item.setAd_feccre((Date)map.get("A_D_FECCRE"));                     
			if(map.get("A_V_USUMOD")!=null) item.setAvusumod((String)map.get("A_V_USUMOD"));                   
			if(map.get("A_D_FECMOD")!=null) item.setAd_fecmod((Date)map.get("A_D_FECMOD"));                     
			if(map.get("A_V_NOMPRG")!=null) item.setAvnomprg((String)map.get("A_V_NOMPRG"));                   
			if(map.get("A_V_USUCRE")!=null) item.setAvusucre((String)map.get("A_V_USUCRE")); 
			
			listaAula.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaAula;
	}
		
	
		
	@Override
	public Aula actualizarAula(Long id, Aula aula) {
		Map<String, Object> out = null;
		List<Aula> lista = new ArrayList<>();
		this.error=null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_AULA_GUARDAR")
				.declareParameters(
						new SqlParameter("i_nidaula", OracleTypes.NUMBER),
						new SqlParameter("i_vnomaula", OracleTypes.VARCHAR),
						new SqlParameter("i_ncapaula", OracleTypes.NUMBER),
						new SqlParameter("i_nidsede", OracleTypes.NUMBER),
						new SqlParameter("i_vcodaula", OracleTypes.VARCHAR),
						new SqlParameter("i_ndisaula", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidaula", id)
				.addValue("i_vnomaula",aula.getVnomaula())
				.addValue("i_ncapaula",aula.getNcapaula())
				.addValue("i_nidsede",aula.getNidsede())
				.addValue("i_vcodaula",aula.getVcodaula())
				.addValue("i_ndisaula",aula.getNdisaula())
				.addValue("i_vusuario","AGI");		
		out = this.jdbcCall.execute(in);
		lista = mapearAula(out);
		return lista.get(0);
	}

	@Override
	public Boolean eliminarAula(Long id) {
		this.error=null;
		List<Aula> lista = new ArrayList<>();		
		Map<String, Object> out = null;
		if(id == null) {
			id = 0L;
		}	
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_AULA_ELIMINAR")
				.declareParameters(
						new SqlParameter("i_nidaula", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
						SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_nidaula", id)
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
	public Aula insertarAula(Aula aula) {
		Map<String, Object> out = null;
		List<Aula> lista = new ArrayList<>();
		this.error=null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_AULA_GUARDAR")
				.declareParameters(
						new SqlParameter("i_nidaula", OracleTypes.NUMBER),
						new SqlParameter("i_vnomaula", OracleTypes.VARCHAR),
						new SqlParameter("i_ncapaula", OracleTypes.NUMBER),
						new SqlParameter("i_nidsede", OracleTypes.NUMBER),
						new SqlParameter("i_vcodaula", OracleTypes.VARCHAR),
						new SqlParameter("i_ndisaula", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidaula", null)
				.addValue("i_vnomaula",aula.getVnomaula())
				.addValue("i_ncapaula",aula.getNcapaula())
				.addValue("i_nidsede",aula.getNidsede())
				.addValue("i_vcodaula",aula.getVcodaula())
				.addValue("i_ndisaula",aula.getNdisaula())
				.addValue("i_vusuario","AGI");		
		out = this.jdbcCall.execute(in);
		lista = mapearAula(out);
		return lista.get(0);
	}
	
	
}
