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
import pe.com.sedapal.agi.dao.IListaVerificacionDAO;
import pe.com.sedapal.agi.model.CriterioResultado;
import pe.com.sedapal.agi.model.HallazgoRequisito;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.ListaVerificacionAuditado;
import pe.com.sedapal.agi.model.ListaVerificacionAuditor;
import pe.com.sedapal.agi.model.ListaVerificacionRequisito;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.ListaVerificacionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class ListaVerificacionDAOImpl implements IListaVerificacionDAO{
	
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	
	
	private static final Logger LOGGER = Logger.getLogger(ListaVerificacionDAOImpl.class);	
	

	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return this.error;
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.paginacion;
	}

	@Override
	public ListaVerificacion registrarListaVerificacion(ListaVerificacion listaVerificacion) {
		Map<String, Object> out = null;
		Long idListaVerificacion = null;
		ListaVerificacion listaVerificacionRegistro = null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_LISTAVERI_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_vestlist",		OracleTypes.VARCHAR),
							new SqlParameter("i_vtiplist",	OracleTypes.VARCHAR),
							new SqlParameter("i_idauditoria",			OracleTypes.NUMBER),
							new SqlParameter("i_dfecha",		OracleTypes.DATE),
							new SqlParameter("i_vestadolv",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdesgere",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdesequi",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdescarg",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdescomi",		OracleTypes.VARCHAR),
							new SqlOutParameter("o_idlistveri",	OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	listaVerificacion.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_vestlist",	"1")
						.addValue("i_vtiplist",	listaVerificacion.getTipoLV())
						.addValue("i_idauditoria",listaVerificacion.getIdAuditoria())
						.addValue("i_dfecha",	listaVerificacion.getFecha())
						.addValue("i_vestadolv",	listaVerificacion.getEstadoListaVerificacion())
						.addValue("i_vdesgere",	listaVerificacion.getCodigoGerencia())
						.addValue("i_vdesequi",	listaVerificacion.getCodigoEquipo())
						.addValue("i_vdescarg",	listaVerificacion.getCodigoCargo())
						.addValue("i_vdescomi",	listaVerificacion.getCodigoComite());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				idListaVerificacion = ((BigDecimal)out.get("o_idlistveri")).longValue();
				listaVerificacionRegistro = listaVerificacion;
				listaVerificacionRegistro.setIdListaVerificacion(idListaVerificacion);
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			
			
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.registrarListaVerificacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return listaVerificacionRegistro;
	}

	@Override
	public boolean registrarListaVerificacionRequisito(ListaVerificacionRequisito listaVerificacionRequisito) {
		Map<String, Object> out = null;
		boolean registro = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_LISTAREQ_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_idlistveri",		OracleTypes.NUMBER),
							new SqlParameter("i_vestlistreq",	OracleTypes.VARCHAR),
							new SqlParameter("i_nidrequisito",			OracleTypes.NUMBER),
							new SqlParameter("i_nidnorma",			OracleTypes.NUMBER),
							new SqlParameter("i_niddetaudreq",			OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	listaVerificacionRequisito.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_idlistveri",	listaVerificacionRequisito.getIdListaVerificacion())
						.addValue("i_vestlistreq",	"1")
						.addValue("i_nidrequisito",listaVerificacionRequisito.getIdRequisito())
						.addValue("i_nidnorma",listaVerificacionRequisito.getIdNorma())
						.addValue("i_niddetaudreq",listaVerificacionRequisito.getIdDetalleRequisito());
			
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
			String mensaje			= "Error en ListaVerificacionDAOImpl.registrarListaVerificacionRequisito";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public boolean registrarListaVerificacionAuditor(ListaVerificacionAuditor listaVerificacionAuditor) {
		Map<String, Object> out = null;
		boolean registro = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_LISVECOL_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_idcola",		OracleTypes.NUMBER),
							new SqlParameter("i_idrolcola",	OracleTypes.NUMBER),
							new SqlParameter("i_idlistveri",			OracleTypes.NUMBER),
							new SqlParameter("i_vestlisvecol",			OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	listaVerificacionAuditor.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_idcola",	listaVerificacionAuditor.getIdAuditor())
						.addValue("i_idrolcola",listaVerificacionAuditor.getIdRolAuditor())
						.addValue("i_idlistveri",listaVerificacionAuditor.getIdListaVerificacion())
						.addValue("i_vestlisvecol","1");
			
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
			String mensaje			= "Error en ListaVerificacionDAOImpl.registrarListaVerificacionAuditor";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public List<ListaVerificacion> consultarListaVerificacion(ListaVerificacionRequest listaVerificacionRequest,
			PageRequest paginaRequest) {		
		Map<String, Object> out	= null;
		List<ListaVerificacion> listaListaVerificacion = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_OBTENER_LISTA_VERI")
						.declareParameters(
							new SqlParameter("i_vestado", 			OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 			OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 			OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vestado", listaVerificacionRequest.getEstado())
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaListaVerificacion = new ArrayList<>();
				listaListaVerificacion = mapearListaVerificacion(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.consultarListaVerificacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaListaVerificacion;
	}
	
	private List<ListaVerificacion> mapearListaVerificacion(Map<String,Object> resultados){
		List<ListaVerificacion> listaListaVerificacion = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		ListaVerificacion item = null;
		for(Map<String, Object> map : lista) {	
			item = new ListaVerificacion();
			
			if(map.get("normas") != null) {
				item.setTextoNormas((String)map.get("normas"));
			}
			if(map.get("tipolv") != null) {
				item.setTipoLV((String)map.get("tipolv"));
			}
			if(map.get("fecha") != null) {
				item.setFecha(new Date(((Timestamp)map.get("fecha")).getTime()));
			}
			if(map.get("descripcionAuditoria") != null) {
				item.setDescripcionAuditoria((String)map.get("descripcionAuditoria"));
			}
			if(map.get("estadolv") != null) {
				item.setEstadoListaVerificacion((String)map.get("estadolv"));
			}
			if(map.get("auditorLider") != null) {
				item.setAuditorLider((String)map.get("auditorLider"));
			}
			if(map.get("auditores") != null) {
				item.setTextoAuditores((String)map.get("auditores"));
			}
			if(map.get("idlistveri") != null) {
				item.setIdListaVerificacion(((BigDecimal) map.get("idlistveri")).longValue());
			}
			
			
			if(map.get("desgere") != null) {
				item.setCodigoGerencia((String) map.get("desgere"));
			}
			
			if(map.get("desequi") != null) {
				item.setCodigoEquipo((String) map.get("desequi"));
			}
			
			if(map.get("descarg") != null) {
				item.setCodigoCargo((String) map.get("descarg"));
			}
			
			if(map.get("descomi") != null) {
				item.setCodigoComite((String) map.get("descomi"));
			}
			
			if(item.getCodigoGerencia() != null) {
				item.setValorEntidad("1");
			}else if(item.getCodigoEquipo() != null) {
				item.setValorEntidad("2");
			}else if(item.getCodigoCargo() != null) {
				item.setValorEntidad("3");
			}else if(item.getCodigoComite() != null) {
				item.setValorEntidad("4");
			}
			
			if(map.get("estadorh") != null) {
				item.setEstadoRevisionHallazgos((String) map.get("estadorh"));
			}
			
			listaListaVerificacion.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaListaVerificacion;
	}

	@Override
	public ListaVerificacion consultarListaVerifcacionPorId(Long idListaVerificacion) {		
		Map<String, Object> out	= null;
		List<ListaVerificacion> listaListaVerificacion = null;
		ListaVerificacion listaVerificacion = null;
		this.error = null;
		
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_OBTENER_DATOS_LV")
						.declareParameters(
							new SqlParameter("i_idlistveri", 	OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_idlistveri", idListaVerificacion);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaListaVerificacion = new ArrayList<>();
				listaListaVerificacion = mapearListaVerificacion(out);
				if(listaListaVerificacion.size()>0) {
					listaVerificacion = listaListaVerificacion.get(0);
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en AuditoriaDAOImpl.consultarListaVerifcacionPorId";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaVerificacion;
	}

	@Override
	public List<ListaVerificacionRequisito> obtenerRequisitosListaVerificacion(Long idListaVerificacion) {		
		Map<String, Object> out	= null;
		List<ListaVerificacionRequisito> listaRequisitosLV= null;
		this.error = null;
		
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_OBTENER_REQUISITOS_LV")
						.declareParameters(
							new SqlParameter("i_idlistveri", 	OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_idlistveri", idListaVerificacion);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaRequisitosLV = new ArrayList<>();
				listaRequisitosLV = mapearRequisitosLV(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en AuditoriaDAOImpl.consultarListaVerifcacionPorId";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaRequisitosLV;
	}
	
	private List<ListaVerificacionRequisito> mapearRequisitosLV(Map<String,Object> resultados){
		List<ListaVerificacionRequisito> listaRequisitosLV = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		ListaVerificacionRequisito item = null;
		for(Map<String, Object> map : lista) {	
			item = new ListaVerificacionRequisito();
			item.setHallazgo(new HallazgoRequisito());
			
			if(map.get("idlistreq") != null) {
				item.setIdLVRequisito(((BigDecimal) map.get("idlistreq")).longValue());
			}
			if(map.get("idlistveri") != null) {
				item.setIdListaVerificacion(((BigDecimal) map.get("idlistveri")).longValue());
			}
			if(map.get("idrequisito") != null) {
				item.setIdRequisito(((BigDecimal) map.get("idrequisito")).longValue());
			}
			if(map.get("descRequisito") != null) {
				item.setDescripcionRequisito((String)map.get("descRequisito"));
			}
			if(map.get("idnorma") != null) {
				item.setIdNorma(((BigDecimal) map.get("idnorma")).longValue());
			}
			if(map.get("descNorma") != null) {
				item.setDescripcionNorma((String)map.get("descNorma"));
			}
			if(map.get("detareq") != null) {
				item.setDetalleRequisito((String)map.get("detareq"));
			}
			if(map.get("cuesreq") != null) {
				item.setCuestionarioRequisito((String)map.get("cuesreq"));
			}
			
			if(map.get("valcali") != null) {
				item.setValorCalificacion((String)map.get("valcali"));
			}
			
			if(map.get("descali") != null) {
				item.setDescripcionCalificacion((String)map.get("descali"));
			}
			
			if(map.get("idHallazgo") != null) {
				item.getHallazgo().setIdHallazgo(((BigDecimal) map.get("idHallazgo")).longValue());
			}
			
			if(map.get("tiphall") != null) {
				item.getHallazgo().setTipoHallazgo((String)map.get("tiphall"));
			}
			
			if(map.get("deshall") != null) {
				item.getHallazgo().setDescripcionHallazgo((String)map.get("deshall"));
			}
			
			if(map.get("fechall") != null) {
				item.getHallazgo().setFechaHallazgo(new Date(((Timestamp)map.get("fechall")).getTime()));
			}
			
			
			
			
			
			
			listaRequisitosLV.add(item);

		}
		
		return listaRequisitosLV;
	}

	@Override
	public boolean actualizarRequisitosListaVerificacion(ListaVerificacionRequisito listaVerificacionRequisito,int indicador) {
		Map<String, Object> out = null;
		boolean registro = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_REQUISITOS_LV_UPDATE")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_idlistreq",	OracleTypes.NUMBER),
							new SqlParameter("i_indicador",	OracleTypes.NUMBER),
							new SqlParameter("i_vcuesreq",			OracleTypes.VARCHAR),
							new SqlParameter("i_valcali",			OracleTypes.VARCHAR),
							new SqlParameter("i_descali",			OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	listaVerificacionRequisito.getDatosAuditoria().getUsuarioModificacion())
						.addValue("i_idlistreq",	listaVerificacionRequisito.getIdLVRequisito())
						.addValue("i_indicador", indicador)
						.addValue("i_vcuesreq",	listaVerificacionRequisito.getCuestionarioRequisito())
						.addValue("i_valcali",	listaVerificacionRequisito.getValorCalificacion())
						.addValue("i_descali",	listaVerificacionRequisito.getDescripcionCalificacion());
			
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
			String mensaje			= "Error en ListaVerificacionDAOImpl.actualizarRequisitosListaVerificacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public boolean actualizarListaVerificacion(ListaVerificacion listaVerificacion,int indicador) {
		Map<String, Object> out = null;
		boolean registro = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_LISTA_VERIF_UPDATE")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_idlistveri",	OracleTypes.NUMBER),
							new SqlParameter("i_indicador",		OracleTypes.NUMBER),
							new SqlParameter("i_vestadolv",			OracleTypes.VARCHAR),
							new SqlParameter("i_vestadorh",			OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	listaVerificacion.getDatosAuditoria().getUsuarioModificacion())
						.addValue("i_idlistveri",	listaVerificacion.getIdListaVerificacion())
						.addValue("i_indicador",	indicador)
						.addValue("i_vestadolv",	listaVerificacion.getEstadoListaVerificacion())
						.addValue("i_vestadorh",	listaVerificacion.getEstadoRevisionHallazgos());
			
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
			String mensaje			= "Error en ListaVerificacionDAOImpl.actualizarListaVerificacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public boolean registrarListaVerificacionAuditado(ListaVerificacionAuditado listaVerificacionAuditado) {
		Map<String, Object> out = null;
		boolean registro = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_LISVERI_TRAB_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_idcola",		OracleTypes.NUMBER),
							new SqlParameter("i_idrolcola",	OracleTypes.NUMBER),
							new SqlParameter("i_idlistveri",			OracleTypes.NUMBER),
							new SqlParameter("i_vestlisvecol",			OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	listaVerificacionAuditado.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_nlistveri",	listaVerificacionAuditado.getIdListVeri())
						.addValue("i_idtrabajador",listaVerificacionAuditado.getTrabajador().getIdTrabajador());
			
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
			String mensaje			= "Error en ListaVerificacionDAOImpl.registrarListaVerificacionAuditado";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public List<ListaVerificacionAuditado> obtenerAuditadosListaVerificacion(Long idListaVerificacion) {
		
		Map<String, Object> out	= null;
		List<ListaVerificacionAuditado> listaAuditadosLV= null;
		this.error = null;
		
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_LISVERI_TRAB_OBTENER")
						.declareParameters(
							new SqlParameter("i_idlistveri", 	OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_idlistveri", idListaVerificacion);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaAuditadosLV = new ArrayList<>();
				listaAuditadosLV = mapearAuditadosLV(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.consultarListaVerifcacionPorId";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaAuditadosLV;
	}
	
	private List<ListaVerificacionAuditado> mapearAuditadosLV(Map<String,Object> resultados){
		List<ListaVerificacionAuditado> listaAuditadosLV = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		ListaVerificacionAuditado item = null;
		for(Map<String, Object> map : lista) {	
			item = new ListaVerificacionAuditado();
			item.setTrabajador(new Trabajador());
			
			if(map.get("nidlvtrab") != null) {
				item.setIdListVeriAuditado(((BigDecimal) map.get("nidlvtrab")).longValue());
			}
			if(map.get("nidlv") != null) {
				item.setIdListVeri(((BigDecimal) map.get("nidlv")).longValue());
			}
			if(map.get("nidtrab") != null) {
				item.getTrabajador().setIdTrabajador(((BigDecimal) map.get("nidtrab")).longValue());
			}
			if(map.get("nombres") != null) {
				item.getTrabajador().setNombreTrabajador((String)map.get("nombres"));
			}
			
			if(map.get("estlvtrab") != null) {
				item.setEstadoRegistro((String)map.get("estlvtrab"));
			}
			listaAuditadosLV.add(item);

		}
		
		return listaAuditadosLV;
	}

	@Override
	public boolean eliminarListaVerificacionAuditado(ListaVerificacionAuditado listaVerificacionAuditado) {
		Map<String, Object> out = null;
		boolean elimino = false;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_AUDITORIA")
			.withProcedureName("PRC_LISVERI_TRAB_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
				new SqlParameter("i_idlisvetrab",		OracleTypes.NUMBER),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_vusuario",listaVerificacionAuditado.getDatosAuditoria().getUsuarioCreacion())
			.addValue("i_idlisvetrab",	listaVerificacionAuditado.getIdListVeriAuditado());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {				
				elimino = true;
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);				
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.eliminarListaVerificacionAuditado";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		
		return elimino;
	}

	@Override
	public boolean aprobarRechazarListaVerificacion(ListaVerificacion listaVerificacion, int indicador) {		
		Map<String, Object> out = null;
		boolean registro = false;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_AUDITORIA")
			.withProcedureName("PRC_APROBAR_RECHAZAR_LV")
			.declareParameters(
				new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
				new SqlParameter("i_idlistveri",		OracleTypes.NUMBER),
				new SqlParameter("i_indicador",		OracleTypes.NUMBER),
				new SqlParameter("i_sustrech",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_vusuario",listaVerificacion.getDatosAuditoria().getUsuarioModificacion())
			.addValue("i_idlistveri",	listaVerificacion.getIdListaVerificacion())
			.addValue("i_indicador",	indicador)
			.addValue("i_sustrech",	listaVerificacion.getSustentoRechazo());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {				
				registro = true;
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.aprobarRechazarListaVerificacion";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		
		return registro;
	}

	@Override
	public List<ListaVerificacion> obtenerRevisionHallazgos(ListaVerificacionRequest listaVerificacionRequest,
			PageRequest paginaRequest) {		
		Map<String, Object> out	= null;
		List<ListaVerificacion> listaRevisionHallazgos = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_OBTENER_REVI_HALLA")
						.declareParameters(
							new SqlParameter("i_vestado", 			OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 			OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 			OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vestado", listaVerificacionRequest.getEstado())
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaRevisionHallazgos = new ArrayList<>();
				listaRevisionHallazgos = mapearListaVerificacion(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.consultarListaVerificacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaRevisionHallazgos;
	}

	@Override
	public List<CriterioResultado> obtenerCriteriosCalificacion(Long idListaVerificacion) {
		Map<String, Object> out = null;
		this.error = null;
		List<CriterioResultado> listaCriterios = null;
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_OBTENER_CRIT_HALLA")
						.declareParameters(
							new SqlParameter("i_idlistveri", 	OracleTypes.NUMBER),			
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_idlistveri",idListaVerificacion);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaCriterios = new ArrayList<>();
				listaCriterios = mapearCriterios(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.obtenerCriteriosCalificacion";
			String mensajeInterno	= e.getMessage();			
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		
		return listaCriterios;
	}
	
	private List<CriterioResultado> mapearCriterios(Map<String,Object> resultados){
		List<CriterioResultado> listaCriterios = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		CriterioResultado item = null;
		for(Map<String, Object> map : lista) {	
			item = new CriterioResultado();
			if(map.get("valorCriterio") != null) {
				item.setValorCriterio((String)map.get("valorCriterio"));
			}
			if(map.get("textoCriterio") != null) {
				item.setDescripcionCriterio((String)map.get("textoCriterio"));
			}
			
			listaCriterios.add(item);

		}
		
		return listaCriterios;
	}

	@Override
	public boolean registrarHallazgoRequisito(HallazgoRequisito hallazgo) {
		Map<String, Object> out = null;
		boolean registro = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_GUARDAR_REQ_HALLA")
						.declareParameters(
							new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
							new SqlParameter("i_idlistreq",		OracleTypes.NUMBER),
							new SqlParameter("i_vtiphall",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdeshall",		OracleTypes.VARCHAR),
							new SqlParameter("i_dfechall",		OracleTypes.DATE),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	hallazgo.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_idlistreq",hallazgo.getIdLVRequisito())
						.addValue("i_vtiphall",hallazgo.getTipoHallazgo())
						.addValue("i_vdeshall",hallazgo.getDescripcionHallazgo())
						.addValue("i_dfechall",hallazgo.getFechaHallazgo());
			
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
			String mensaje			= "Error en ListaVerificacionDAOImpl.registrarHallazgoRequisito";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public boolean actualizarHallazgoRequisito(HallazgoRequisito hallazgo) {
		Map<String, Object> out = null;
		boolean registro = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_UPDATE_REQ_HALLA")
						.declareParameters(
							new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
							new SqlParameter("i_idhallazgo",		OracleTypes.NUMBER),
							new SqlParameter("i_vtiphall",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdeshall",		OracleTypes.VARCHAR),
							new SqlParameter("i_dfechall",		OracleTypes.DATE),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	hallazgo.getDatosAuditoria().getUsuarioModificacion())
						.addValue("i_idhallazgo",hallazgo.getIdHallazgo())
						.addValue("i_vtiphall",hallazgo.getTipoHallazgo())
						.addValue("i_vdeshall",hallazgo.getDescripcionHallazgo())
						.addValue("i_dfechall",hallazgo.getFechaHallazgo());
			
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
			String mensaje			= "Error en ListaVerificacionDAOImpl.actualizarHallazgoRequisito";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return registro;
	}

	@Override
	public boolean eliminarHallazgoRequisito(HallazgoRequisito hallazgo) {
		Map<String, Object> out = null;
		boolean elimino = false;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_ELIMINAR_REQ_HALLA")
						.declareParameters(
							new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
							new SqlParameter("i_idhallazgo",		OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",	hallazgo.getDatosAuditoria().getUsuarioModificacion())
						.addValue("i_idhallazgo",hallazgo.getIdHallazgo());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				elimino = true;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			
			
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.actualizarHallazgoRequisito";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return elimino;
	}

	@Override
	public Long buscarHallazgoRequisito(Long idRequisitoLV) {
		Map<String, Object> out = null;
		this.error = null;
		Long idHallazgo = new Long(0);
		List<Long> listaHallazgos = null;
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_OBTENER_REQ_HALLA")
						.declareParameters(
							new SqlParameter("i_idlistreq", 	OracleTypes.NUMBER),			
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_idlistreq",idRequisitoLV);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaHallazgos = new ArrayList<>();
				listaHallazgos = mapearHallazgos(out);
				if(listaHallazgos.size()>0) {
					idHallazgo = listaHallazgos.get(0);
				}
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.buscarHallazgoRequisito";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		
		return idHallazgo;
	}
	
	private List<Long> mapearHallazgos(Map<String,Object> resultados){
		List<Long> listaHallazgos = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Long item = null;
		for(Map<String, Object> map : lista) {	
			if(map.get("n_idhallazgo") != null) {
				item = ((BigDecimal) map.get("n_idhallazgo")).longValue();
			}
			listaHallazgos.add(item);
		}
		
		return listaHallazgos;
	}

	@Override
	public boolean aprobarRechazarRevisionHallazgos(ListaVerificacion listaVerificacion, int indicador) {		
		Map<String, Object> out = null;
		boolean registro = false;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_AUDITORIA")
			.withProcedureName("PRC_APROBAR_RECHAZAR_RH")
			.declareParameters(
				new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
				new SqlParameter("i_idlistveri",		OracleTypes.NUMBER),
				new SqlParameter("i_indicador",		OracleTypes.NUMBER),
				new SqlParameter("i_sustrech",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_vusuario",listaVerificacion.getDatosAuditoria().getUsuarioModificacion())
			.addValue("i_idlistveri",	listaVerificacion.getIdListaVerificacion())
			.addValue("i_indicador",	indicador)
			.addValue("i_sustrech",	listaVerificacion.getSustentoRechazo());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {				
				registro = true;
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);				
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ListaVerificacionDAOImpl.aprobarRechazarRevisionHallazgos";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		
		return registro;
	}
 
}
