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
import pe.com.sedapal.agi.dao.IInstructorDAO;
import pe.com.sedapal.agi.model.Instructor;
import pe.com.sedapal.agi.model.request_objects.InstructorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class InstructorDAOImpl implements IInstructorDAO{
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
	public List<Instructor> obtenerInstructor(InstructorRequest constanteRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Instructor> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_INSTRUCTOR_OBTENER")
				.declareParameters(
						new SqlParameter("i_vcodinst", OracleTypes.VARCHAR),
						new SqlParameter("i_vnominst", OracleTypes.VARCHAR), 
						new SqlParameter("i_vapepatinst", OracleTypes.VARCHAR),
						new SqlParameter("i_vapematinst", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));     
				SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_vcodinst", constanteRequest.getIvcodinst())     
				.addValue("i_vnominst", constanteRequest.getIvnominst())     
				.addValue("i_vapepatinst", constanteRequest.getIvapepatinst())  
				.addValue("i_vapematinst", constanteRequest.getIvapematinst())  
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");			
			if(resultado == 0) {
				lista = mapearInstructores(out);
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
	
	public List<Instructor> mapearInstructores(Map<String, Object> resultados) {
		
		List<Instructor> listaInstructor = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Instructor item = null;
		
		for(Map<String, Object> map : lista) {
			item = new Instructor(); 
			
		    if(map.get("N_IDINST")!=null)  item.setN_idinst(((BigDecimal)map.get("N_IDINST")).longValue());        
		    if(map.get("N_DISINST")!=null)  item.setN_disinst(((BigDecimal)map.get("N_DISINST")).longValue());     
		    if(map.get("V_NOMINST")!=null)  item.setV_nominst((String)map.get("V_NOMINST"));      
		    if(map.get("V_TIPINST")!=null)  item.setV_tipinst((String)map.get("V_TIPINST"));      
		    if(map.get("V_CODINST")!=null)  item.setV_codinst((String)map.get("V_CODINST"));      
		    if(map.get("V_APEPATINST")!=null)  item.setV_apepatinst((String)map.get("V_APEPATINST"));
		    if(map.get("V_APEMATINST")!=null)  item.setV_apematinst((String)map.get("V_APEMATINST"));
		    if(map.get("V_TIPDOCINST")!=null)  item.setV_tipdocinst((String)map.get("V_TIPDOCINST"));
		    if(map.get("V_NOMCOMP")!=null)  item.setV_nomcomp((String)map.get("V_NOMCOMP"));
			if(map.get("V_NUMDOCINST")!=null)  item.setV_numdocinst((String)map.get("V_NUMDOCINST"));
			
			listaInstructor.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		
		return listaInstructor;
	}
	
	
	private List<Instructor> mapearInstructor (Map<String, Object> resultados){
		Instructor item = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		List<Instructor> listaInstructor = new ArrayList<>();
		for(Map<String, Object> map : lista) {
			
			item = new Instructor();

		    if(map.get("N_IDINST")!=null)  item.setN_idinst(((BigDecimal)map.get("N_IDINST")).longValue());        
		    if(map.get("N_DISINST")!=null)  item.setN_disinst(((BigDecimal)map.get("N_DISINST")).longValue());     
		    if(map.get("V_NOMINST")!=null)  item.setV_nominst((String)map.get("V_NOMINST"));      
		    if(map.get("V_TIPINST")!=null)  item.setV_tipinst((String)map.get("V_TIPINST"));      
		    if(map.get("V_CODINST")!=null)  item.setV_codinst((String)map.get("V_CODINST"));      
		    if(map.get("V_APEPATINST")!=null)  item.setV_apepatinst((String)map.get("V_APEPATINST"));
		    if(map.get("V_APEMATINST")!=null)  item.setV_apematinst((String)map.get("V_APEMATINST"));
		    if(map.get("V_TIPDOCINST")!=null)  item.setV_tipdocinst((String)map.get("V_TIPDOCINST"));
			if(map.get("V_NUMDOCINST")!=null)  item.setV_numdocinst((String)map.get("V_NUMDOCINST")); 
			
			listaInstructor.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaInstructor;
	}
		
	
		
	@Override
	public Instructor actualizarInstructor(Long id, Instructor instructor) {
		Map<String, Object> out = null;
		List<Instructor> lista = new ArrayList<>();
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_INSTRUCTOR_GUARDAR")
				.declareParameters(
						new SqlParameter("i_n_idinst", OracleTypes.NUMBER),
						new SqlParameter("i_n_disinst", OracleTypes.NUMBER),
						new SqlParameter("i_v_nominst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_tipinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_codinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_apepatinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_apematinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_tipdocinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_numdocinst", OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idinst", id)
				.addValue("i_n_disinst",instructor.getN_disinst())
				.addValue("i_v_nominst",instructor.getV_nominst())
				.addValue("i_v_tipinst",instructor.getV_tipinst())
				.addValue("i_v_codinst",instructor.getV_codinst())
				.addValue("i_v_apepatinst",instructor.getV_apepatinst())
				.addValue("i_v_apematinst",instructor.getV_apematinst())
				.addValue("i_v_tipdocinst",instructor.getV_tipdocinst())
				.addValue("i_v_numdocinst",instructor.getV_numdocinst())
				.addValue("i_avusucre","AGI");		
		out = this.jdbcCall.execute(in);
		lista = mapearInstructor(out);
		return lista.get(0);
	}

	@Override
	public Boolean eliminarInstructor(Long id) {
		Map<String, Object> out = null;
		if(id == null) {
			id = 0L;
		}	
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_INSTRUCTOR_ELIMINAR")
				.declareParameters(
						new SqlParameter("i_nidinst", OracleTypes.VARCHAR),
						new SqlParameter("i_avusumod", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
						SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_nidinst", id)
						.addValue("i_avusumod", "AGI");	
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
	public Instructor insertarInstructor(Instructor instructor) {
		Map<String, Object> out = null;
		List<Instructor> lista = new ArrayList<>();
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_INSTRUCTOR_GUARDAR")
				.declareParameters(
						new SqlParameter("i_n_idinst", OracleTypes.NUMBER),
						new SqlParameter("i_n_disinst", OracleTypes.NUMBER),
						new SqlParameter("i_v_nominst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_tipinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_codinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_apepatinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_apematinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_tipdocinst", OracleTypes.VARCHAR),
						new SqlParameter("i_v_numdocinst", OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idinst", null)
				.addValue("i_n_disinst",instructor.getN_disinst())
				.addValue("i_v_nominst",instructor.getV_nominst())
				.addValue("i_v_tipinst",instructor.getV_tipinst())
				.addValue("i_v_codinst",instructor.getV_codinst())
				.addValue("i_v_apepatinst",instructor.getV_apepatinst())
				.addValue("i_v_apematinst",instructor.getV_apematinst())
				.addValue("i_v_tipdocinst",instructor.getV_tipdocinst())
				.addValue("i_v_numdocinst",instructor.getV_numdocinst())
				.addValue("i_avusucre","AGI");		
		out = this.jdbcCall.execute(in);
		lista = mapearInstructor(out);
		return lista.get(0);
	}
	
	
}
