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
import pe.com.sedapal.agi.dao.ISedeDAO;
import pe.com.sedapal.agi.model.Sede;
import pe.com.sedapal.agi.model.request_objects.SedeRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class SedeDAOImpl implements ISedeDAO{
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;


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
	public List<Sede> obtenerSede(SedeRequest constanteRequest) {
		Map<String, Object> out = null;
		List<Sede> lista = new ArrayList<>();
		
		/*this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());*/
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_SEDE_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidsede", OracleTypes.NUMBER),                        
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));     
				SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidsede", constanteRequest.getI_nidsede());	
				//.addValue("i_npagina",pageRequest.getPagina())
				//.addValue("i_nregistros",pageRequest.getRegistros());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");			
			if(resultado == 0) {
				lista = mapearSedes(out);
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
	
	public List<Sede> mapearSedes(Map<String, Object> resultados) {
		
		List<Sede> listaSede = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Sede item = null;
		
		for(Map<String, Object> map : lista) {
			item = new Sede();
			
			if(map.get("N_IDSEDE")!=null)   item.setNidsede(((BigDecimal)map.get("N_IDSEDE")).longValue());                   
			if(map.get("V_NOMSEDE")!=null)  item.setVnomsede((String)map.get("V_NOMSEDE"));                   
		
			listaSede.add(item);
			
			/*if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}*/
		}
		
		return listaSede;
	}
	
	
	
	
	
}
