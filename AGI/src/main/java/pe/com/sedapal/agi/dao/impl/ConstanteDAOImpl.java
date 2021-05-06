package pe.com.sedapal.agi.dao.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pe.com.sedapal.agi.model.response_objects.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.IConstanteDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.enums.EstadoConstante;

@Repository
public class ConstanteDAOImpl implements IConstanteDAO {
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
	
	@SuppressWarnings("unchecked")
	public List<Constante> mapearConstantes(Map<String, Object> resultados) {
		List<Constante> listaConstantes = new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Constante item = null;
		
		for(Map<String, Object> map : lista) {			
			item = new Constante();
			item.setIdconstante(((BigDecimal)map.get("N_IDCONS")).longValue());
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
	public List<Constante> obtenerConstantes(ConstanteRequest constanteRequest, PageRequest pageRequest, Long codigo) {
		Map<String, Object> out = null;
		List<Constante> lista = new ArrayList<>();
		this.error=null;
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_CONSTANTE_OBTENER")
				.declareParameters(
						new SqlParameter("i_nid", OracleTypes.NUMBER),						
						new SqlParameter("i_vpadre", OracleTypes.VARCHAR),
						new SqlParameter("i_vnombre", OracleTypes.VARCHAR),	
						new SqlParameter("i_vdescripcion", OracleTypes.VARCHAR),
						new SqlParameter("i_ndisponible", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlParameter("i_vorden", OracleTypes.VARCHAR),						
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));		
		SqlParameterSource in = new MapSqlParameterSource()
		.addValue("i_nid",null )
		.addValue("i_vpadre",constanteRequest.getPadre())
		.addValue("i_vnombre",null )
//		.addValue("i_vdescripcion",null)
		.addValue("i_vdescripcion",constanteRequest.getV_descons())
		.addValue("i_ndisponible",null)
		.addValue("i_npagina",pageRequest.getPagina())
		.addValue("i_nregistros",pageRequest.getRegistros())
		.addValue("i_vorden",null);
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
	
	@SuppressWarnings("unchecked")
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
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaConstante;
	}
	
	
	@Override
	public Constante actualizarConstante(Long id, Long i_nidpadre, String i_vcampo1, Constante constante) {
		Map<String, Object> out = null;
		List<Constante> lista = new ArrayList<>();
		this.error=null;
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
		//List<Constante> lista = new ArrayList<>();
		this.error=null;
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
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
						SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_nid", id)
						.addValue("i_vusuario", "agi");	
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
	
}
