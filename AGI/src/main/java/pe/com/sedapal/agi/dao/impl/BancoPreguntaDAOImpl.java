package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import pe.com.sedapal.agi.dao.IBancoPreguntaDAO;
import pe.com.sedapal.agi.model.Aula;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class BancoPreguntaDAOImpl implements IBancoPreguntaDAO{
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
    Pregunta objpreg=new Pregunta();
	
	public Paginacion getPaginacion() {
		return this.paginacion;
	}	
	public Error getError() {
		return this.error;
	}
	@Override
	public List<Pregunta> ListaBancoPreguntas(Pregunta pregunta, PageRequest pageRequest) {
		SqlParameterSource in = null;
		Map<String, Object> out	= null;
		List<Pregunta> lista=new  ArrayList<>();
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		this.error		= null;

			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_BANCO_PREGUNTAS_OBTENER")					
					.declareParameters(
							new SqlParameter("i_vdescpreg", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.INTEGER),
							new SqlParameter("i_nregistros", OracleTypes.INTEGER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			in = new MapSqlParameterSource().addValue("i_vdescpreg", pregunta.getPregunta())
					.addValue("i_npagina", pageRequest.getPagina())
					.addValue("i_nregistros", pageRequest.getRegistros());
			try {
				out = this.jdbcCall.execute(in);
				Integer resultado = (Integer)out.get("o_retorno");
			if(resultado==0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				lista = this.mapearLista(listado);
			}
			else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);		
			}					
	}catch(Exception e){
		Integer resultado		= 1;
		String mensaje			= "Error en BancoPreguntaDAOImpl.ObtenerPregunta";
		String mensajeInterno	= e.toString();
		this.error = new Error(resultado,mensaje,mensajeInterno);
	}
			return lista;
	}
	
	
	@Override
	public Map<String, Object> GuardarBancoPregunta(Pregunta objPregunta) {
		this.error=null;
		Map<String, Object> out = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_BANCO_PREGUNTAS_GUARDAR")				
					.declareParameters(
							new SqlParameter("i_nidpregaudi", OracleTypes.NUMBER),
							new SqlParameter("i_vdescpreg", OracleTypes.VARCHAR),
							new SqlParameter("i_liderauditor", OracleTypes.VARCHAR),
							new SqlParameter("i_liderauditorinterno", OracleTypes.VARCHAR),
							new SqlParameter("i_auditorinterno", OracleTypes.VARCHAR),		
							//cambio 
							new  SqlParameter("i_vaudobs", OracleTypes.VARCHAR),								
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("i_nidpregaudi", objPregunta.getiD())
																	   .addValue("i_vdescpreg", objPregunta.getPregunta())
																	   .addValue("i_liderauditor", objPregunta.getAuditorLider())
																	   .addValue("i_liderauditorinterno", objPregunta.getAuditorLiderInterno())
																	   //cambio																	   
																	   .addValue("i_auditorinterno", objPregunta.getAuditorInterno())
																	   .addValue("i_vaudobs", objPregunta.getAuditorObservador());
/*			objPregunta.setvRolAuditor(retornaIdRolConcat(objPregunta.getAuditorLider().toString(),
			objPregunta.getAuditorLiderInterno().toString(),objPregunta.getAuditorInterno().toString())); */
			/* .addValue("v_RolAuditor", retornaIdRolConcat(objPregunta.getAuditorLider().toString(),
			objPregunta.getAuditorLiderInterno().toString(),objPregunta.getAuditorInterno().toString()));*/																	   			
            			out = this.jdbcCall.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_BANCO_PREGUNTAS_GUARDAR"); //183,184,182
		}
		return out;
	}	
	
	
	
	@Override
	public Boolean eliminarPregunta(Long id) {
		this.error = null;
		Boolean retorno = true;
		List<Pregunta> lista = new ArrayList<>();
		Map<String, Object> out = null;
		if (id == null) {
			id = 0L;
		}
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_BANCO_PREGUNTAS_ELIMINAR")
				.declareParameters(
						new SqlParameter("i_idpregunta", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		MapSqlParameterSource paraMap = new MapSqlParameterSource()
				.addValue("i_idpregunta", id)
				.addValue("i_vusuario","AGI");

		try {
			out = this.jdbcCall.execute(paraMap);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				retorno = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				retorno = false;
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
			retorno = false;

		}
		return retorno;
	}
		
	
	
	
	
	
	
/*	
	@Override
	public Map<String, Object> eliminarPregunta(Long codigo) {
		this.error=null;
		Map<String, Object> out = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_BANCO_PREGUNTAS_ELIMINAR")					
					.declareParameters(
							new SqlParameter("I_idPregunta", OracleTypes.NUMBER),	
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),		
							new SqlOutParameter("O_Retorno", OracleTypes.NUMBER),
							new SqlOutParameter("O_Mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource()
					.addValue("I_idPregunta", codigo)
					.addValue("i_vusuario", (((UserAuth)principal).getUsername()));
			
						
            out = this.jdbcCall.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_BANCO_PREGUNTAS_ELIMINAR");
		}
		return out;	
	}
	
*/
	
	/*@Override
	public List<Pregunta> ObtenerPregunta(Pregunta pregunta,PageRequest pageRequest) {
		
		SqlParameterSource in = null;
		Map<String, Object> out	= null;
		List<Pregunta> lista=new  ArrayList<>();
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		this.error		= null;

			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_OBTENER_PREGUNTA")					
					.declareParameters(
							new SqlParameter("i_cod", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			in = new MapSqlParameterSource().addValue("i_cod", pregunta.getiD());
			try {
				out = this.jdbcCall.execute(in);
				Integer resultado = (Integer)out.get("o_retorno");
			if(resultado==0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				lista = this.mapearLista(listado);
			}
			else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);		
			}					
	}catch(Exception e){
		Integer resultado		= 1;
		String mensaje			= "Error en BancoPreguntaDAOImpl.ObtenerPregunta";
		String mensajeInterno	= e.toString();
		this.error = new Error(resultado,mensaje,mensajeInterno);
	}
			return lista;
	}*/
			
	  
	@Override
	public Map<String, Object> ObtenerPregunta(String cod) {
		this.error=null;
		Map<String, Object> out = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_OBTENER_PREGUNTA")					
					.declareParameters(
							new SqlParameter("i_rol", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("i_cod", cod);
						
            out = this.jdbcCall.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_LIST_NIS_CLI");
		}
		return out;
	}
	  
	@Override
	public List<Constante> buscarRoles(String descripcion) {
		// TODO Auto-generated method stub
		Map<String, Object> out	= null;
		List<Constante> listaRoles		= new ArrayList<>();		
		this.error=null;
		this.jdbcCall =
				new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_OBTENER_ROL")
					.declareParameters(
						new SqlParameter("v_descripcion_rol", 			OracleTypes.VARCHAR),										
					 
						new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),						
						new SqlOutParameter("o_retorno",	OracleTypes.NUMBER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
	  
		       
		SqlParameterSource in = new MapSqlParameterSource()
	    			.addValue("v_descripcion_rol",			descripcion);
	    		
		 
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = ((BigDecimal)out.get("o_retorno")).intValue();			
			if(resultado == 0) {
				listaRoles = this.mapearRoles(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");  
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			
		}catch(Exception e) {
			Integer resultado		= 1; 
			String mensaje			= "Error en EvaluacionAuditorDAOImpl.buscarResPorCodEvaluacionAuditor";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		
		return listaRoles;
	}
	
	
	//mapear
	@SuppressWarnings("unchecked")
	private List<Constante> mapearRoles(Map<String,Object> resultados){
		List<Constante> listaRoles = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Constante item = null;
		//int size = lista.size();  
		
		for(Map<String, Object> map : lista) {		
			item = new Constante();

			if(map.get("N_IDCONS")!=null) {

			
				item.setIdconstante(((BigDecimal) map.get("N_IDCONS")).longValue());
			}
			else {							
				item.setIdconstante(((BigDecimal) map.get("0")).longValue());
			}

			/*if(map.get("V_VALCONS")!=null) {
  
				
				item.setV_valcons(((String) map.get("V_VALCONS")));
			}
			else {							
				item.setV_valcons("");
			}*/
			
			listaRoles.add(item);

		}  
		
		//logger.msgInfoFin("listaAspectos.size()-->"+listaAspectos.size());
		return listaRoles;		
	}
	
	
	
	@Override
	public Map<String, Object> ActualizarBancoPregunta(Pregunta objPregunta) {
		this.error=null;
		Map<String, Object> out = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_BANCO_PREGUNTAS_UPDATE")				
					.declareParameters(
							new SqlParameter("I_Pregunta", OracleTypes.VARCHAR),
							new SqlParameter("I_Liderauditor", OracleTypes.VARCHAR),
							new SqlParameter("I_Liderauditorinterno", OracleTypes.VARCHAR),
							new SqlParameter("i_AuditorInterno", OracleTypes.VARCHAR), 	
							
							new  SqlParameter("v_RolAuditor", OracleTypes.VARCHAR),	
							
							new SqlParameter("l_id", OracleTypes.NUMBER), 														
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("I_Pregunta", objPregunta.getPregunta())																	
																	   .addValue("I_Liderauditor", objPregunta.getAuditorLider())
																	   .addValue("I_Liderauditorinterno", objPregunta.getAuditorLiderInterno()	)
																	   .addValue("i_AuditorInterno", objPregunta.getAuditorInterno())
																	   .addValue("v_RolAuditor", objPregunta.getvRolAuditor())	
																	   .addValue("l_id", objPregunta.getiD());
						
            out = this.jdbcCall.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_LIST_NIS_CLI");
		}
		return out;
	}
	
	
//Mapear la revision 
	private List<Pregunta> mapearLista(List<Map<String, Object>> listado) {
		List<Pregunta> mLista= new ArrayList<>();
		Pregunta item = null;
		int totalListado = listado.size();
		Integer totalRegistro = 0;
		for(Map<String, Object> map : listado) {
			
			item = new Pregunta();
		//Validaciones 
		
			if(map.get("ID")!=null) {
				item.setiD(((BigDecimal)map.get("ID")).longValue());
			}
			
			
			if(map.get("pregunta")!=null) {
				item.setPregunta((String)map.get("pregunta"));
			}
			if(map.get("auditor_lider")!=null) {
				item.setAuditorLider((String)map.get("auditor_lider"));
			}
			if(map.get("auditor_lider_interno")!=null) {
				item.setAuditorLiderInterno((String)map.get("auditor_lider_interno"));
			}
			if(map.get("auditor_interno")!=null) {
				item.setAuditorInterno((String)map.get("auditor_interno"));
			}
			if(map.get("auditor_observador")!=null) {
				item.setAuditorObservador((String)map.get("auditor_observador"));
			}

			if(map.get("estado")!=null) {
				item.setEstado((String)map.get("auditor_observador"));
			}
			
		
			mLista.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
			
		}
		

		

		
		return mLista;
	}
	
	
	

}
