package pe.com.sedapal.agi.dao.impl;


import java.math.BigDecimal;
import java.sql.Timestamp;
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
import pe.com.sedapal.agi.dao.IFichaAuditorDAO;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.FichaAuditor;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class FichaAuditorDAOImpl implements IFichaAuditorDAO{
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	public Paginacion getPaginacion() {
		return this.paginacion;
	}	
	public Error getError() {
		return this.error;
	}
	
	private static final Logger LOGGER = Logger.getLogger(FichaAuditorDAOImpl.class);	
	
	
	
	public Map<String, Object> obtenerListaFichaAuditor(String iavanzada, String iNumFicha, String iNombreAuditor, String iApePaternoAuditor, String iApeMaternoAuditor, PageRequest pageRequest) {
		Map<String, Object> out = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_FICHA_AUDITOR_OBTENER")					
					.declareParameters(
							new SqlParameter("i_avanzada", OracleTypes.VARCHAR),
							new SqlParameter("i_numFicha", OracleTypes.VARCHAR),
							new SqlParameter("i_nombreAuditor", OracleTypes.VARCHAR),
							new SqlParameter("i_apePaternoAuditor", OracleTypes.VARCHAR),
							new SqlParameter("i_apeMaternoAuditor", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
														
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("i_numFicha", iNumFicha)
																	   .addValue("i_avanzada", iavanzada)
																	   .addValue("i_nombreAuditor", iNombreAuditor)
																	   .addValue("i_apePaternoAuditor", iApePaternoAuditor)
																	   .addValue("i_apeMaternoAuditor", iApeMaternoAuditor)
																	   .addValue("i_npagina",		pageRequest.getPagina())
																	   .addValue("i_nregistros",	pageRequest.getRegistros());
						
            out = this.jdbcCall.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_LIST_NIS_CLI");
		}
		return out;
	}
	@Override
	public Map<String, Object> eliminarFichaAuditor(Long codigo) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_AUDITORIA")
			.withProcedureName("PRC_FICHA_AUDITOR_ELIMINAR")
			.declareParameters(				
				new SqlParameter("i_codigo",		OracleTypes.NUMBER),				
				new SqlOutParameter("o_retorno",	OracleTypes.NUMBER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_codigo",		codigo);

	
			out = this.jdbcCall.execute(in);
			return out;	
	}
	@Override
	public Map<String, Object> listaConsultaConstate(Long iCodConstante) {
		Map<String, Object> out = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_CARGA_COMBO_CONSTANTE")					
					.declareParameters(
							new SqlParameter("i_vcodigo", OracleTypes.NUMBER),														
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("i_vcodigo", iCodConstante);
						
            out = this.jdbcCall.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_CARGA_COMBO_CONSTANTE");
		}
		return out;
	}
	@Override
	public Map<String, Object> cargaCursosObligatorios(Long iCodConstante, Long iObligatorio) {
		Map<String, Object> out = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_CARGA_CURSOS_OBLIGA_LIBRE")					
					.declareParameters(
							new SqlParameter("i_vcodigo", OracleTypes.NUMBER),
							new SqlParameter("I_nobligatorio", OracleTypes.NUMBER),	
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("i_vcodigo", iCodConstante)
																       .addValue("i_nobligatorio", iObligatorio);
						
            out = this.jdbcCall.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_CARGA_CURSOS_OBLIGA_LIBRE");
		}
		return out;
	}
	@Override
	public List<FichaAuditor> obtenerListaAuditores(AuditorRequest auditoriaRequest, PageRequest paginaRequest) {		
		Map<String, Object> out	= null;
		List<FichaAuditor> listaAuditores = new ArrayList<>();
		
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		this.jdbcCall =
				new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_AUDITOR_OBTENER")
					.declareParameters(
						new SqlParameter("i_vficha", 			OracleTypes.VARCHAR),						
						new SqlParameter("i_vnombres", 		OracleTypes.VARCHAR),
						new SqlParameter("i_vapepat", 		OracleTypes.VARCHAR),
						new SqlParameter("i_vapemat",	OracleTypes.VARCHAR),
						new SqlParameter("i_nrolaud",	OracleTypes.NUMBER),
						new SqlParameter("i_npagina",	OracleTypes.NUMBER),
						new SqlParameter("i_npagina",	OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
				
				
				try {
					SqlParameterSource in =
							new MapSqlParameterSource()
								.addValue("i_vficha",		auditoriaRequest.getNroFicha())
								.addValue("i_vnombres",		auditoriaRequest.getNombres())
								.addValue("i_vapepat",		auditoriaRequest.getApePaterno())
								.addValue("i_vapemat",		auditoriaRequest.getApeMaterno())
								.addValue("i_nrolaud",		auditoriaRequest.getIdRol())
								.addValue("i_npagina",		paginaRequest.getPagina())
								.addValue("i_nregistros",	paginaRequest.getRegistros());
					
					out = this.jdbcCall.execute(in);
					Integer resultado = (Integer)out.get("o_retorno");			
					if(resultado == 0) {
						listaAuditores = this.mapearAuditor(out);
					} else {
						String mensaje			= (String)out.get("o_mensaje");
						String mensajeInterno	= (String)out.get("o_sqlerrm");						
						this.error				= new Error(resultado,mensaje,mensajeInterno);
					}
				}catch(Exception e){
					Integer resultado		= 1;
					String mensaje			= "Error en FichaAuditorDAOImpl.obtenerListaAuditores()";
					String mensajeInterno	= e.getMessage();
					String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
					LOGGER.error(error[1], e);
					this.error = new Error(resultado,mensaje,mensajeInterno);
				}				
			  return listaAuditores;
	}
	
	private List<FichaAuditor> mapearAuditor(Map<String,Object> resultados) {
		List<FichaAuditor> listaAuditor = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		FichaAuditor item = null;
		for(Map<String, Object> map : lista) {	
			item = new FichaAuditor();
			if(map.get("idColaborador")!= null) {
				item.setCodigo(((BigDecimal) map.get("idColaborador")).intValueExact());
			}
			if(map.get("nroFicha")!= null) {
				item.setNumFicha((String)map.get("nroFicha"));//
			}
			if(map.get("nombreColaborador")!= null) {
				item.setNombreAuditor((String)map.get("nombreColaborador"));
			}
			
			if(map.get("tipoColaborador") != null) {
				item.setTipo(((BigDecimal) map.get("tipoColaborador")).intValueExact());
			}

			if(map.get("descTipoCola")!=null) {
				item.setNomTipo((String)map.get("descTipoCola"));
			}
			
			
			if(map.get("idRol") != null) {
				item.setCodigoRolAuditor(((BigDecimal) map.get("idRol")).intValueExact());
			}

			if(map.get("cargoColaborador")!=null) {
				item.setNomRol((String)map.get("cargoColaborador"));
			}
			
			
			listaAuditor.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaAuditor;
	}
}
