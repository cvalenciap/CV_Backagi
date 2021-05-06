package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import pe.com.sedapal.agi.dao.IAsistenciaDAO;
import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AsistenciaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class AsistenciaDAOImpl implements IAsistenciaDAO{
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	private static final Logger LOGGER = Logger.getLogger(AsistenciaDAOImpl.class);	
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
	public List<Asistencia> obtenerAsistencia(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest){		
		Map<String, Object> out	= null;
		List<Asistencia> listaAsistencia = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_ASISTENCIA_OBTENER")
					.declareParameters(
							new SqlParameter("i_v_codcurs", 	OracleTypes.VARCHAR),
							new SqlParameter("i_v_nomcurs", 	OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 		OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_v_codcurs", asistenciaRequest.getCodCurso())
						.addValue("i_v_nomcurs", asistenciaRequest.getNomCurso())		
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaAsistencia = new ArrayList<>();
				listaAsistencia = mapearAsistencia(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en AsistenciaDaoImpl.obtenerAsistencia";
			String mensajeInterno	= e.getMessage();			
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaAsistencia;
	}
	
	
	private List<Asistencia> mapearAsistencia(Map<String,Object> resultados){
		List<Asistencia> listaAsistencia = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Asistencia item = null;
		for(Map<String, Object> map : lista) {	
			item = new Asistencia();
			
			if(map.get("n_idcapa") != null) {
				item.setIdCapacitacion(((BigDecimal)map.get("n_idcapa")).longValue());
			}
			if(map.get("n_idcurs") != null) {
				item.setIdCurso(((BigDecimal)map.get("n_idcurs")).longValue());
				
				List<Sesion> list= new ArrayList<>();
				Sesion sesion=new Sesion();
				sesion.setIdCurso(item.getIdCurso());
				list=this.obtenerSesion(sesion);
				item.setListSesion(list);
			}
			if(map.get("v_codcurs") != null) {
				item.setCodCurso((String)map.get("v_codcurs"));
			}
			if(map.get("v_nomcurs") != null) {
				item.setNomCurso((String)map.get("v_nomcurs"));
			}
			if(map.get("v_nominst") != null) {
				item.setNomInstructor((String)map.get("v_nominst"));
			}
			if(map.get("participantes") != null) {
				item.setNumParticipantes(((BigDecimal)map.get("participantes")).longValue());
			}
			
			listaAsistencia.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaAsistencia;
	}
	
	
	@Override
	public List<Sesion> obtenerSesion(Sesion sesion){		
		Map<String, Object> out	= null;
		List<Sesion> listaSesiones = null;
		this.error = null;
		
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_OBTENER_SESION_CURSO")
					.declareParameters(
							new SqlParameter("i_nidcurs", 	OracleTypes.VARCHAR),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_nidcurs", sesion.getIdCurso());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaSesiones = new ArrayList<>();
				listaSesiones = mapearSesion(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en AsistenciaDaoImpl.obtenerAsistencia";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaSesiones;
	}
	
	private List<Sesion> mapearSesion(Map<String,Object> resultados){
		List<Sesion> listaSesion = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Sesion item = null;
		for(Map<String, Object> map : lista) {	
			item = new Sesion();
			
			if(map.get("n_idcurs") != null) {
				item.setIdCurso(((BigDecimal)map.get("n_idcurs")).longValue());
			}
			if(map.get("n_idsesi") != null) {
				item.setIdSesion(((BigDecimal)map.get("n_idsesi")).longValue());
			}
			if(map.get("v_nomsesi") != null) {
				item.setNombreSesion((String)map.get("v_nomsesi"));
			}
			if(map.get("n_dursesi") != null) {
				item.setDuracion(((BigDecimal)map.get("n_dursesi")).longValue());
			}
			if(map.get("n_dissesi") != null) {
				item.setDisponibilidad(((BigDecimal)map.get("n_dissesi")).longValue());
			}
			if(map.get("descDisp") != null) {
				item.setDescDisp((String)map.get("descDisp"));
			}
			
			listaSesion.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaSesion;
	}
	
	
	@Override
	public List<Trabajador> obtenerEmpleadoSesion(Trabajador trabajador){		
		Map<String, Object> out	= null;
		List<Trabajador> listaTrabajador = null;
		this.error = null;
		
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_EMPLEADOS_SESION_OBTENER")
					.declareParameters(
							new SqlParameter("i_n_idcapa", 	OracleTypes.VARCHAR),
							new SqlParameter("i_n_idsesi", 	OracleTypes.VARCHAR),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	

			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_n_idcapa", trabajador.getIdCapacitacion())
						.addValue("i_n_idsesi", trabajador.getIdSesion());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaTrabajador = new ArrayList<>();
				listaTrabajador = mapearTrabajador(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en AsistenciaDaoImpl.obtenerEmpleadoSesion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		
		return listaTrabajador;
	}
	
	
	private List<Trabajador> mapearTrabajador(Map<String,Object> resultados){
		List<Trabajador> listaTrabajador = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Trabajador item = null;
		for(Map<String, Object> map : lista) {	
			item = new Trabajador();
			
			if(map.get("n_idcurs") != null) {
				item.setIdCurso(((BigDecimal)map.get("n_idcurs")).longValue());
			}
			if(map.get("ncodarea") != null) {
				item.setIdEquipo(((BigDecimal)map.get("ncodarea")).longValue());
			}
//			if(map.get("n_idsesi") != null) {
//				item.setIdSesion(((BigDecimal)map.get("n_idsesi")).longValue());
//			}
			if(map.get("vnombres") != null) {
				item.setNombreTrabajador((String)map.get("vnombres"));
			}
			if(map.get("v_indasis") != null) {
				item.setIdEstadoAsistencia((String)map.get("v_indasis"));
			}
//			if(map.get("nomAsis") != null) {
//				item.setDescripAsistencia((String)map.get("nomAsis"));
//			}
			if(map.get("v_jusasis") != null) {
				item.setJustificacion((String)map.get("v_jusasis"));
			}
			if(map.get("descripEquipo") != null) {
				item.setNomEquipo((String)map.get("descripEquipo"));
			}
			
			if(map.get("n_idcola") != null) {
				item.setIdTrabajador(((BigDecimal)map.get("n_idcola")).longValue());
			}
			if(map.get("v_rutasust") != null) {
//				item.setNombreArchivo((String)map.get("v_rutasust"));
				item.setArchivoAntiguo((String)map.get("v_rutasust"));
			}
			if(map.get("v_nomarchi") != null) {
				item.setNombreArchivo((String)map.get("v_nomarchi"));
				
			}
			listaTrabajador.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaTrabajador;
	}
	@Override
	public Asistencia actualizarAsistencia(Asistencia asistencia) {
		Asistencia asiste=null;
		Boolean registro=false;
		try {
			for(Trabajador empleado: asistencia.getListTrabajador()) {
				
				asistencia.setIdColaborador(empleado.getIdTrabajador());
//				asistencia.setIdSesion(empleado.getIdSesion());
				if(empleado.getJustificacion()==null) {
					empleado.setJustificacion("");
				}
				if(empleado.getRutaDocumento()==null) {
					if(empleado.getArchivoAntiguo()==null) {
						empleado.setRutaDocumento(null);
					}else {
						empleado.setRutaDocumento("NO");
					}
					
				}
				asistencia.setRuta(empleado.getRutaDocumento());
				asistencia.setJustificacion(empleado.getJustificacion());
				asistencia.setAsistencia(empleado.getIdEstadoAsistencia());
				
				this.actualizar(asistencia);
				registro=true;
			}
		}catch(Exception e) {
			registro=false;
			System.out.println("Error");
		}
		return asiste;
	}
	@Override
	public Asistencia actualizar(Asistencia asistencia) {
		this.error = null;
		Map<String, Object> out = null;
		Asistencia listAsistencia=null;
		try {
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_ASIST_ACTUALIZAR")
				.declareParameters(
						new SqlParameter("i_n_idsesi",		OracleTypes.NUMBER),
						new SqlParameter("i_n_idcapa",		OracleTypes.NUMBER),
						new SqlParameter("i_n_idcola",		OracleTypes.NUMBER),
						new SqlParameter("i_a_v_usumod",	OracleTypes.VARCHAR),
						new SqlParameter("i_v_indasis",		OracleTypes.CHAR),
						new SqlParameter("i_v_jusasis",		OracleTypes.VARCHAR),
						new SqlParameter("i_v_rutasust",	OracleTypes.VARCHAR),
						new SqlParameter("i_v_nomarchi",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor",		OracleTypes.INTEGER),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
	
		
		SqlParameterSource in =
				new MapSqlParameterSource()
					.addValue("i_n_idsesi", asistencia.getIdSesion())
					.addValue("i_n_idcapa", asistencia.getIdCapacitacion())
					.addValue("i_n_idcola",	asistencia.getIdColaborador())
					.addValue("i_a_v_usumod",		"AGI")
					.addValue("i_v_indasis",	asistencia.getAsistencia())
					.addValue("i_v_jusasis",	asistencia.getJustificacion())
					.addValue("i_v_rutasust",	asistencia.getRuta())
					.addValue("i_v_nomarchi",	asistencia.getNombreArchivo());			
					
		
		out = this.jdbcCall.execute(in);
		Integer resultado = (Integer)out.get("o_retorno");
		
		if(resultado == 0) {
			
			listAsistencia = asistencia;
			
		} else {
			String mensaje			= (String)out.get("o_mensaje");
			String mensajeInterno	= (String)out.get("o_sqlerrm");			
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en AsistenciaDaoImpl.actualizarAsistencia";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listAsistencia;
	}
	
	
	public List<Asistencia> obtenerEvaluacion(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest){
		List<Asistencia> lista= new ArrayList<>();
		lista=this.obtenerAsistencia(asistenciaRequest, paginaRequest);
//			for(Asistencia asis:lista) {
//				Trabajador trab= new Trabajador();
//				List<Trabajador> listTrab= new ArrayList<>();
//				trab.setIdCapacitacion(asis.getIdCapacitacion());
//				listTrab=this.obtenerEmpleadoEvaluacion(trab);
//				asis.setListTrabajador(listTrab);
//			}

//		


		return lista;
	}
	
	
	@Override
	public List<Trabajador> obtenerEmpleadoEvaluacion(Trabajador trabajador,PageRequest paginaRequest){		
		Map<String, Object> out	= null;
		List<Trabajador> listaTrabajador = null;
		this.error = null;
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_EMPLEADOS_EVALUACION")
					.declareParameters(
							new SqlParameter("i_n_idcapa", 	OracleTypes.VARCHAR),
							new SqlParameter("i_nombres", 	OracleTypes.VARCHAR),
							new SqlParameter("i_equipo", 	OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 		OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	

			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_n_idcapa", trabajador.getIdCapacitacion())
						.addValue("i_nombres", trabajador.getNombreTrabajador())
						.addValue("i_equipo", trabajador.getNomEquipo())
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaTrabajador = new ArrayList<>();
				listaTrabajador = mapearTraba(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "EvaluacionDaoImpl.obtenerEmpleadoEvaluacion()";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaTrabajador;
	}
	
	
	private List<Trabajador> mapearTraba(Map<String,Object> resultados){
		List<Trabajador> listaTrabajador = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Trabajador item = null;
		for(Map<String, Object> map : lista) {	
			item = new Trabajador();
			
			if(map.get("n_idcurs") != null) {
				item.setIdCurso(((BigDecimal)map.get("n_idcurs")).longValue());
			}
			if(map.get("ncodarea") != null) {
				item.setIdEquipo(((BigDecimal)map.get("ncodarea")).longValue());
			}
			if(map.get("n_idexam") != null) {
				item.setIdExamen(((BigDecimal)map.get("n_idexam")).longValue());
			}
			if(map.get("vnombres") != null) {
				item.setNombreTrabajador((String)map.get("vnombres"));
			}
			
			if(map.get("descripEquipo") != null) {
				item.setNomEquipo((String)map.get("descripEquipo"));
			}
			
			if(map.get("n_idcola") != null) {
				item.setIdTrabajador(((BigDecimal)map.get("n_idcola")).longValue());
			}
			if(map.get("v_rutasust") != null) {
				item.setNombreArchivo((String)map.get("v_rutasust"));
			}
			if(map.get("descTipo") != null) {
				item.setTipoEvaluacion((String)map.get("descTipo"));
			}
			if(map.get("n_noteval") != null) {
				item.setNota(((BigDecimal)map.get("n_noteval")).longValue());
			}
			if(map.get("descEstado") != null) {
				item.setEstadoEvaluacion((String)map.get("descEstado"));
			}
			if(map.get("itemColumna") != null) {
				item.setItemColumna(((BigDecimal)map.get("itemColumna")).longValue());
			}
			
			
			listaTrabajador.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaTrabajador;
	}
	
	
	public Asistencia actualizarEvaluacion(Asistencia asistencia) {
		Asistencia asis= new Asistencia();
		try {
			
			for(Trabajador trab:asistencia.getListTrabajador()) {
				asis.setIdExamen(trab.getIdExamen());
				asis.setIdCapacitacion(trab.getIdCapacitacion());
				asis.setIdColaborador(trab.getIdTrabajador());
				asis.setNota(trab.getNota());
				this.guardarEvaluacion(asis);
			}
		}catch(Exception e) {
			e.getStackTrace();
		}
		return asis;
	}
	
	@Override
	public Asistencia guardarEvaluacion(Asistencia asistencia) {
		this.error = null;
		Map<String, Object> out = null;
		Asistencia listaEvaluacion=null;
		try {
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_EVALUACION_ACTUALIZAR")
				.declareParameters(
						
						new SqlParameter("i_n_idcapa",		OracleTypes.NUMBER),
						new SqlParameter("i_n_idcola",		OracleTypes.NUMBER),
						new SqlParameter("i_n_idexam",		OracleTypes.NUMBER),
						new SqlParameter("i_n_noteval",		OracleTypes.NUMBER),
						new SqlParameter("i_a_v_usumod",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
	
		
		SqlParameterSource in =
				new MapSqlParameterSource()
					
					.addValue("i_n_idcapa", asistencia.getIdCapacitacion())
					.addValue("i_n_idcola",	asistencia.getIdColaborador())
					.addValue("i_n_idexam", asistencia.getIdExamen())
					.addValue("i_n_noteval", asistencia.getNota())
					.addValue("i_a_v_usumod",		"AGI");			
					
		
		out = this.jdbcCall.execute(in);
		Integer resultado = (Integer)out.get("o_retorno");
		
		if(resultado == 0) {
			
			listaEvaluacion = asistencia;
			
		} else {
			String mensaje			= (String)out.get("o_mensaje");
			String mensajeInterno	= (String)out.get("o_sqlerrm");			
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en EvaluacionDaoImpl.actualizarEvaluacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaEvaluacion;
	}
}
