package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
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
import pe.com.sedapal.agi.dao.ITareasPendientesDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Dashboard;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.TareasPendientes;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class TareasPendientesDAOImpl implements ITareasPendientesDAO {
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Error error;
	
	public Error getError() {
		return this.error;
	}
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	private static final Logger LOGGER = Logger.getLogger(TareasPendientesDAOImpl.class);	
	
	@Override
	public List<TareasPendientes> obtenerTareasPendientesDocumental(Long idColaborador) {		
		Map<String, Object> out = null;
		List<TareasPendientes> listaTareasPendientes = new ArrayList<>();
		jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_TAREAS")
				.withProcedureName("PRC_TAREA_PENDIENTE_OBTENER")
				.declareParameters(
						new SqlParameter("i_n_idcola", OracleTypes.NUMBER),						
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idcola", idColaborador);				
		try {
			out = jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				listaTareasPendientes = new ArrayList<>();
				listaTareasPendientes = mapearTareasPendientesDocumental(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return listaTareasPendientes;
	}
	
	@SuppressWarnings("unchecked")
	public List<TareasPendientes> mapearTareasPendientesDocumental(Map<String, Object> resultados){
		List<TareasPendientes> listaTareasPendientes = new ArrayList<>();
		List<Map<String,Object>> listaResultado = (List<Map<String, Object>>)resultados.get("o_cursor");
		TareasPendientes tareasPendientes = null;
		for(Map<String, Object> map : listaResultado) {
			tareasPendientes = new TareasPendientes();
			if(map.get("i_n_idcola") != null) {
				tareasPendientes.setIdColaborador(((BigDecimal)map.get("i_n_idcola")).longValue());
			}
			if(map.get("n_cantparevi") != null) {
				tareasPendientes.setCantidadParaRevision(((BigDecimal)map.get("n_cantparevi")).intValueExact());
			}
			if(map.get("n_cantpacanc") != null) {
				tareasPendientes.setCantidadParaCancelar(((BigDecimal)map.get("n_cantpacanc")).intValueExact());
			}
			if(map.get("n_cantpaimpr") != null) {
				tareasPendientes.setCantidadParaImprimir(((BigDecimal)map.get("n_cantpaimpr")).intValueExact());
			}
			if(map.get("n_cantelab") != null) {
				tareasPendientes.setCantidadElaboracion(((BigDecimal)map.get("n_cantelab")).intValueExact());
			}
			if(map.get("n_cantcons") != null) {
				tareasPendientes.setCantidadConsenso(((BigDecimal)map.get("n_cantcons")).intValueExact());
			}
			if(map.get("n_cantapro") != null) {
				tareasPendientes.setCantidadAprobacion(((BigDecimal)map.get("n_cantapro")).intValueExact());
			}
			if(map.get("n_canthomo") != null) {
				tareasPendientes.setCantidadHomologacion(((BigDecimal)map.get("n_canthomo")).intValueExact());
			}
			if(map.get("n_cantcanc") != null) {
				tareasPendientes.setCantidadCancelacion(((BigDecimal)map.get("n_cantcanc")).intValueExact());
			}
			if(map.get("n_cantcambpers") != null) {
				tareasPendientes.setCantidadCambioPersonal(((BigDecimal)map.get("n_cantcambpers")).intValueExact());
			}
			if(map.get("n_cantrealrevi") != null) {
				tareasPendientes.setCantidadRealizarRevision(((BigDecimal)map.get("n_cantrealrevi")).intValueExact());
			}
			listaTareasPendientes.add(tareasPendientes);
		}
		return listaTareasPendientes;
	}
	
	@Override
	public Dashboard obtenerDashboardDocumento(Long anio, Long idTrimestre) {		
		Map<String, Object> out = null;
		Dashboard objeto = null;
		
		jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_TAREAS")
				.withProcedureName("PRC_DASHBOARD_DOCUMENTO")
				.declareParameters(
						new SqlParameter("i_anio", 		 OracleTypes.NUMBER),
						new SqlParameter("i_trimestre",  OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor",  OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_anio", 	anio)
				.addValue("i_trimestre",idTrimestre);
		try {
			out = jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				objeto = this.mapearDashboard(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return objeto;
	}
	
	@SuppressWarnings("unchecked")
	public Dashboard mapearDashboard(Map<String, Object> resultados){
		Dashboard resultado = new Dashboard();
		List<String> listaTexto = new ArrayList<>();
		List<String> listaDato = new ArrayList<>();
		List<Map<String,Object>> cursor = (List<Map<String, Object>>)resultados.get("o_cursor");
		for(Map<String, Object> map: cursor) {
			if(map.get("vTexto")!= null)	listaTexto.add(map.get("vTexto").toString());
			if(map.get("vDato")!= null)		listaDato.add(map.get("vDato").toString());
		}
		resultado.setListaTexto(listaTexto);
		resultado.setListaDato(listaDato);
		return resultado;
	}
	
	@Override
	public List<Dashboard> obtenerDashboardExcel(Long anio, Long idTrimestre) {		
		Map<String, Object> out = null;
		List<Dashboard> lista	= null;
		
		jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_TAREAS")
				.withProcedureName("PRC_DASHBOARD_DETALLE")
				.declareParameters(
						new SqlParameter("i_anio", 		 OracleTypes.NUMBER),
						new SqlParameter("i_trimestre",  OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor",  OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_anio", 	anio)
				.addValue("i_trimestre",idTrimestre);
		try {
			out = jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				lista = this.mapearDashboardDetalle(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<Dashboard> mapearDashboardDetalle(Map<String, Object> resultados){
		List<Dashboard> lista = new ArrayList<>();
		List<Map<String,Object>> cursor = (List<Map<String, Object>>)resultados.get("o_cursor");
		for(Map<String, Object> map: cursor) {
			Dashboard objeto = new Dashboard();
			if(map.get("N_IDPROG")!= null)	objeto.setIdProgramacion(((BigDecimal)map.get("N_IDPROG")).longValue());
			if(map.get("D_FECPROG")!= null)	objeto.setFechaProgramacion((Date)map.get("D_FECPROG"));
			if(map.get("N_IDDOCU")!= null) {
				Documento documento = new Documento();
				documento.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
				if(map.get("V_CODDOCU")!= null)	documento.setCodigo((String)map.get("V_CODDOCU"));
				if(map.get("V_DESDOCU")!= null)	documento.setDescripcion((String)map.get("V_DESDOCU"));
				if(map.get("D_FEAPRO")!= null)	documento.setFechaApro((Date)map.get("D_FEAPRO"));
				
				if(map.get("N_IDREVI")!= null) {
					Revision revision = new Revision();
					revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
					if(map.get("N_NUMREVI")!= null)	revision.setNumero(((BigDecimal)map.get("N_NUMREVI")).longValue());
					documento.setRevision(revision);
				}
				
				if(map.get("N_IDESTA")!= null) {
					Constante estado = new Constante();
					estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
					if(map.get("V_IDESTA")!= null)	estado.setV_descons((String)map.get("V_IDESTA"));
					documento.setEstado(estado);
				}
				objeto.setDocumento(documento);
			}
			if(map.get("N_IDTRIM")!= null)	{
				Constante trimestre = new Constante();
				trimestre.setIdconstante(((BigDecimal)map.get("N_IDTRIM")).longValue());
				if(map.get("V_IDTRIM")!= null)	trimestre.setV_descons((String)map.get("V_IDTRIM"));
				objeto.setTrimestre(trimestre);
			}
			if(map.get("N_IDEQUI")!= null)	{
				Equipo equipo = new Equipo();
				equipo.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
				if(map.get("V_IDEQUI")!= null)	equipo.setDescripcion((String)map.get("V_IDEQUI"));
				objeto.setEquipo(equipo);
			}
			lista.add(objeto);
		}
		return lista;
	}
}