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
import pe.com.sedapal.agi.dao.IDeteccionHallazgosDAO;
import pe.com.sedapal.agi.model.DeteccionHallazgos;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.DeteccionHallazgosRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class DeteccionHallazgosDAOimpl implements IDeteccionHallazgosDAO {
	
	private static final Logger LOGGER = Logger.getLogger(DeteccionHallazgosDAOimpl.class);	
	
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	
	
	@Override
	public Paginacion getPaginacion() {
		return this.paginacion;
	}	
	
	@Override
	public Error getError() {
		return this.error;
	}
	
	@Override
	public List<DeteccionHallazgos> obtenerListaDeteccionHallazgos(DeteccionHallazgosRequest deteccionHallazgosRequest,PageRequest paginaRequest){
		
		
		Map<String, Object> out	= null;
		List<DeteccionHallazgos> listaDeteccionHallazgo = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		
		SimpleJdbcCall	jdbcCallBD = null;
		
		try {
			
			jdbcCallBD = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_DETEC_HALLAZGOS_OBTENER")					
					.declareParameters(
							new SqlParameter("I_TipoNoConformidad", OracleTypes.NUMBER),
							new SqlParameter("I_TipoOrigenDetec", OracleTypes.NUMBER),
							new SqlParameter("I_NombreDetector", OracleTypes.VARCHAR),
							new SqlParameter("I_ApPaternoDetector", OracleTypes.VARCHAR),
							new SqlParameter("I_ApMaternoDetector", OracleTypes.VARCHAR),
							new SqlParameter("I_Estado", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
														
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));
			
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("I_TipoNoConformidad", deteccionHallazgosRequest.getTipoNoConformidad() )		
     																	.addValue("I_TipoOrigenDetec", deteccionHallazgosRequest.getTipoOrigenDetec())	
	     																.addValue("I_NombreDetector", deteccionHallazgosRequest.getNombreDetector())
	     																.addValue("I_ApPaternoDetector", deteccionHallazgosRequest.getApPaternoDetector())	
	     																.addValue("I_ApMaternoDetector", deteccionHallazgosRequest.getApMaternoDetector())	
		    															.addValue("I_Estado", deteccionHallazgosRequest.getEstado())	
																	   .addValue("i_npagina",		paginaRequest.getPagina())
																	   .addValue("i_nregistros",	paginaRequest.getRegistros());
						
            out = jdbcCallBD.execute(paraMap);
			
			Integer resultado = ((BigDecimal)out.get("o_retorno")).intValue();
			
			if(resultado == 0) {
				listaDeteccionHallazgo = new ArrayList<>();
				listaDeteccionHallazgo = mapearDeteccionHallazgo(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DeteccionHallazgoDAOImpl.obtenerListaDeteccionHallazgos";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return listaDeteccionHallazgo;
		
	}
	
	private List<DeteccionHallazgos> mapearDeteccionHallazgo(Map<String,Object> resultados){
		List<DeteccionHallazgos> listaDeteccionHallazgos = new ArrayList<>();
		try {
			
			List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
			DeteccionHallazgos item = null;
			for(Map<String, Object> map : lista) {	
				item = new DeteccionHallazgos();
				item.setDetector(new Trabajador());
				if(map.get("ValorAmbito") != null) {
					item.setValorAmbito(((BigDecimal)map.get("ValorAmbito")).toString());
				}
				
				if(map.get("Ambito") != null) {
				item.setAmbito((String)map.get("Ambito"));
				}
				if(map.get("Codigo_Deteccion") != null) {
				item.setIdDeteccionHallazgo(((BigDecimal)map.get("Codigo_Deteccion")).longValue());
				}
				if(map.get("Id_Origen_Deteccion") != null) {
				item.setIdorigenDeteccion(((BigDecimal)map.get("Id_Origen_Deteccion")).intValue());
				}
				if(map.get("Origen_Deteccion") != null) {
				item.setOrigenDeteccion((String)map.get("Origen_Deteccion"));
				}
				if(map.get("Id_Tipo_No_Conformidad") != null) {
				item.setIdTipoNoConformidad(((BigDecimal)map.get("Id_Tipo_No_Conformidad")).intValue());
				}
				if(map.get("Tipo_No_Conformidad") != null) {
				item.setTipoNoConformidad((String)map.get("Tipo_No_Conformidad"));
				}
				if(map.get("Fecha_Deteccion") != null) {
					item.setFechaDeteccion(new Date(((Timestamp)map.get("Fecha_Deteccion")).getTime()));
				}
				if(map.get("Descripcion_Hallazgo") != null) {
					item.setDescripHallazgo((String)map.get("Descripcion_Hallazgo"));
				}
				
				
				if(map.get("Id_Detector") != null) {
					item.getDetector().setIdTrabajador(((BigDecimal)map.get("Id_Detector")).longValue());
				}
				
				if(map.get("Detector") != null) {
					item.getDetector().setNombreTrabajador((String) map.get("Detector"));
				}
				
				if(map.get("Estado") != null) {
					item.setEstado((String) map.get("Estado"));
				}
			
				listaDeteccionHallazgos.add(item);
				
				if (map.get("RESULT_COUNT")!=null) {
					this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
				}
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en DeteccionHallazgoDAOImpl.mapearDeteccionHallazgo";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return listaDeteccionHallazgos;
	}
	
	@Override
	public Map<String, Object> ListaConstantes(String listaConstante) {
		Map<String, Object> out = null;
		this.error=null;
		try {
			SimpleJdbcCall	jdbcCallBD  = new SimpleJdbcCall(this.jdbc)			
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA") //PCK_AGI_AUDITORIA
					.withProcedureName("PRC_LISTA_CONSTANTE_OBTENER")					
					.declareParameters(
							new SqlParameter("I_listaNombre", OracleTypes.VARCHAR),

														
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));			
			
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("I_listaNombre", listaConstante);
						
            out = jdbcCallBD.execute(paraMap);
			out.entrySet().forEach(System.out::println);
		}
		catch (Exception e){
			System.out.println(e+"ERROR EJECUTANDO PRC_LISTA_CONSTANTE_OBTENER");
		}
		return out;		
	}

	@Override
	public boolean registrarDatosDeteccionHallazgos(DeteccionHallazgos deteccion) {
		boolean registro = false;
		DeteccionHallazgos deteccionRegistro = this.registrarDeteccionHallazgos(deteccion);
		if(deteccionRegistro!=null) {
			if(deteccionRegistro.getRequisito()!=null) {
				registro = this.registrarRequisitoDeteccion(deteccionRegistro);
			}
			
			if(deteccionRegistro.getDetector().getIdTrabajador()!=null) {
				registro = this.registrarDetectorDeteccionHallazgos(deteccionRegistro);
			}
		}
		
		return registro;
		
	}

	@Override
	public DeteccionHallazgos registrarDeteccionHallazgos(DeteccionHallazgos deteccion) {
		Map<String, Object> out = null;
		Long idDeteccion = null;
		DeteccionHallazgos deteccionRegistro = null;
		this.error=null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_DETEC_HALLAZGOS_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_vtipodete",		OracleTypes.NUMBER),
							new SqlParameter("i_origdete",	OracleTypes.NUMBER),
							new SqlParameter("i_nidnorma",			OracleTypes.NUMBER),
							new SqlParameter("i_tiponc",		OracleTypes.NUMBER),
							new SqlParameter("i_desgere",		OracleTypes.NUMBER),
							new SqlParameter("i_desequi",		OracleTypes.NUMBER),
							new SqlParameter("i_fechdete",		OracleTypes.DATE),
							new SqlParameter("i_estadete",		OracleTypes.VARCHAR),
							new SqlParameter("i_descprob",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_iddeteccion",	OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",			deteccion.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_vtipodete",		deteccion.getValorAmbito())
						.addValue("i_origdete",			deteccion.getIdorigenDeteccion())
						.addValue("i_nidnorma",			deteccion.getNorma())
						.addValue("i_tiponc",			deteccion.getIdTipoNoConformidad())
						.addValue("i_desgere",			deteccion.getValorEntidadGerencia())
						.addValue("i_desequi",			deteccion.getValorEntidadEquipo())
						.addValue("i_fechdete",			deteccion.getFechaDeteccion())
						.addValue("i_estadete", 		"1")
						.addValue("i_descprob", 		deteccion.getDescripHallazgo());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				idDeteccion = ((BigDecimal)out.get("o_iddeteccion")).longValue();
				deteccion.setIdDeteccionHallazgo(idDeteccion);
				deteccionRegistro = deteccion;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			
			
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en DeteccionHallazgosDaoImpl.registrarDeteccionHallazgos";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return deteccionRegistro;
	}

	@Override
	public boolean registrarRequisitoDeteccion(DeteccionHallazgos deteccion) {
		Map<String, Object> out = null;
		boolean registro = false;
		this.error=null;
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_DETEC_REQUI_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_nidrequisito",		OracleTypes.NUMBER),
							new SqlParameter("i_niddeteccion",	OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",			deteccion.getDatosAuditoria().getUsuarioCreacion())
					//	.addValue("i_nidrequisito",		deteccion.getRequisito().getIdRequisito())
						.addValue("i_niddeteccion",		deteccion.getIdDeteccionHallazgo());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				registro = true;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en DeteccionHallazgosDaoImpl.registrarRequisitoDeteccion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public boolean registrarDetectorDeteccionHallazgos(DeteccionHallazgos deteccion) {
		Map<String, Object> out = null;
		boolean registro = false;
		this.error=null;
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_DETEC_COLA_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_niddeteccion",		OracleTypes.NUMBER),
							new SqlParameter("i_nidcola",	OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",			deteccion.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_niddeteccion",		deteccion.getIdDeteccionHallazgo())
						.addValue("i_nidcola",		deteccion.getDetector().getIdTrabajador());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				registro = true;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			
			
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en DeteccionHallazgosDaoImpl.registrarDetectorDeteccionHallazgos";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	
	
}