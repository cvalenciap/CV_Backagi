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
import pe.com.sedapal.agi.dao.IAuditoriaDAO;
import pe.com.sedapal.agi.dao.IProgramacionDAO;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ProgramaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UConvierteFecha;

@Repository
public class ProgramacionDAOImpl implements IProgramacionDAO{
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	@Autowired private IAuditoriaDAO auditoriaDao;
		
	private static final Logger LOGGER = Logger.getLogger(ProgramacionDAOImpl.class);	
	
	public Paginacion getPaginacion() {
		return this.paginacion;
	}	
	public Error getError() {
		return this.error;
	}
	@Override
	public List<Programa> obtenerProgramas(ProgramaRequest programaRequest, PageRequest paginaRequest) {		
		Map<String, Object> out	= null;
		List<Programa> listaProgramas = new ArrayList<>();
		
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		this.jdbcCall =
				new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_AUDITORIA")
					.withProcedureName("PRC_PROGRAMACION_OBTENER")
					.declareParameters(
						new SqlParameter("i_vusuario", 			OracleTypes.VARCHAR),						
						new SqlParameter("i_dfecha", 		OracleTypes.DATE),
						new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
						new SqlParameter("i_nregistros",	OracleTypes.NUMBER),					
						new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
				
				
				try {
					SqlParameterSource in =
							new MapSqlParameterSource()
								.addValue("i_vusuario",		programaRequest.getUsuario())
								.addValue("i_dfecha",		programaRequest.getFecha()!= null ? UConvierteFecha.convertirStringADate(programaRequest.getFecha()):null)
								.addValue("i_npagina",		paginaRequest.getPagina())
								.addValue("i_nregistros",	paginaRequest.getRegistros());
					
					out = this.jdbcCall.execute(in);
					Integer resultado = (Integer)out.get("o_retorno");			
					if(resultado == 0) {
						listaProgramas = this.mapearPrograma(out);
					} else {
						String mensaje			= (String)out.get("o_mensaje");
						String mensajeInterno	= (String)out.get("o_sqlerrm");						
						this.error				= new Error(resultado,mensaje,mensajeInterno);
					}
				}catch(Exception e){
					Integer resultado		= 1;
					String mensaje			= "Error en ProgramacionDAOImpl.obtenerProgramas";
					String mensajeInterno	= e.getMessage();
					String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
					LOGGER.error(error[1], e);					
					this.error = new Error(resultado,mensaje,mensajeInterno);
				}				
			  return listaProgramas;
	}
	
	private List<Programa> mapearPrograma(Map<String,Object> resultados){
		List<Programa> listaProgramas = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Programa item = null;
		for(Map<String, Object> map : lista) {	
			item = new Programa();
			if(map.get("N_IDPROGRAMA")!= null) {
				item.setIdPrograma(((BigDecimal) map.get("N_IDPROGRAMA")).longValue());
			}
			if(map.get("V_DESPRO")!= null) {
				item.setDescripcion((String)map.get("V_DESPRO"));//
			}
			if(map.get("D_FECHA")!= null) {
				item.setFechaPrograma(new Date(((Timestamp)map.get("D_FECHA")).getTime()));
			}
			if(map.get("A_V_USUCRE")!= null) {
				DatosAuditoria auditoria = new DatosAuditoria();
				auditoria.setUsuarioCreacion((String)map.get("A_V_USUCRE"));
				item.setDatosAuditoria(auditoria);
			}
			
			if(map.get("V_PROCPROG")!=null) {
				item.setProcesoPrograma((String)map.get("V_PROCPROG"));
			}
			
			
			listaProgramas.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaProgramas;
	}
	@Override
	public Boolean eliminarPrograma(Long codigo, String usuario) {		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_AUDITORIA")
			.withProcedureName("PRC_PROGRAMACION_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_idprograma",			OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_idprograma",codigo)
			.addValue("i_vusuario",	usuario);

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {				
				return true;
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);				
				return false;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ProgramacionDAOImpl.eliminarPrograma";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	@Override
	public void registrarDatosPrograma(Programa programa, List<Auditoria> listaAuditoria) {
		
		try {
			Programa datosPrograma = registrarPrograma(programa);
			if(datosPrograma != null) {
				for(Auditoria auditoria: listaAuditoria) {
					auditoria.setIdPrograma(datosPrograma.getIdPrograma());
					auditoria.setDatosAuditoria(datosPrograma.getDatosAuditoria());
					auditoria.setCicloAuditoria("1");
					Auditoria auditoriaRegistro = registrarAuditoria(auditoria);
					if(auditoriaRegistro != null) {
						for(Norma norma:auditoriaRegistro.getListaNormas()) {
							boolean registro =this.auditoriaDao.registrarNormaAuditoria(norma, auditoriaRegistro);
							if(!registro) {
								break;
							}
						}
					}
				}
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en ProgramacionDAOImpl.registrarDatosPrograma";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		
		
		
	}
	@Override
	public Programa registrarPrograma(Programa programa) {
		
		Map<String, Object> out = null;
		Long idPrograma = null;
		Programa datosPrograma = null;
		this.error = null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_PROGRAMACION_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_vdespro",		OracleTypes.VARCHAR),
							new SqlParameter("i_dfecha",	OracleTypes.DATE),
							new SqlParameter("i_vestpro",			OracleTypes.VARCHAR),
							new SqlParameter("i_vproprg",			OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_idprograma",	OracleTypes.NUMBER));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",			programa.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_vdespro",		programa.getDescripcion())
						.addValue("i_dfecha",	programa.getFechaPrograma())
						.addValue("i_vestpro",		"1")
						.addValue("i_vproprg",	"1");
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				idPrograma = ((BigDecimal)out.get("o_idprograma")).longValue();
				datosPrograma = programa;
				datosPrograma.setIdPrograma(idPrograma);
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			
			
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en ProgramacionDAOImpl.registrarPrograma";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return datosPrograma;
	}
	@Override
	public Auditoria registrarAuditoria(Auditoria auditoria) {
		Map<String, Object> out = null;
		Long idAuditoria = null;
		Auditoria auditoriaRegistro = null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_AUDITORIA_GUARDAR")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_vestadoud",		OracleTypes.VARCHAR),
							new SqlParameter("i_vmes",	OracleTypes.VARCHAR),
							new SqlParameter("i_idprograma",			OracleTypes.NUMBER),
							new SqlParameter("i_vdesgere",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdesequi",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdescarg",		OracleTypes.VARCHAR),
							new SqlParameter("i_vdescomi",		OracleTypes.VARCHAR),
							new SqlParameter("i_vtipo",		OracleTypes.VARCHAR),
							new SqlParameter("i_vcicaud",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_idauditoria",	OracleTypes.NUMBER));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",			auditoria.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_vestadoud",		"1")
						.addValue("i_vmes",	auditoria.getMes())
						.addValue("i_idprograma",		auditoria.getIdPrograma())
						.addValue("i_vdesgere",	(auditoria.getGerencia()!= null && !auditoria.getGerencia().equals("")) ? auditoria.getGerencia() : null)
						.addValue("i_vdesequi",	(auditoria.getEquipo()!= null && !auditoria.getEquipo().equals("")) ? auditoria.getEquipo() : null)
						.addValue("i_vdescarg",	(auditoria.getCargo()!= null && !auditoria.getCargo().equals("")) ? auditoria.getCargo() : null)
						.addValue("i_vdescomi",	(auditoria.getComite()!= null && !auditoria.getComite().equals("")) ? auditoria.getComite() : null)
						.addValue("i_vtipo", "1")
						.addValue("i_vcicaud", auditoria.getCicloAuditoria());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				idAuditoria = ((BigDecimal)out.get("o_idauditoria")).longValue();
				auditoriaRegistro = auditoria;
				auditoriaRegistro.setIdAuditoria(idAuditoria);
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			
			
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en ProgramacionDAOImpl.registrarAuditoria";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return auditoriaRegistro;
	}
	@Override
	public Programa obtenerProgramaPorId(Long idPrograma) {		
		Map<String, Object> out	= null;
		List<Programa> listaProgramas = new ArrayList<>();
		Programa programa = null;
		this.error = null;
			
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_PROGRAMACION_OBTENER_DATOS")
						.declareParameters(
							new SqlParameter("i_idprograma", 			OracleTypes.NUMBER),											
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_idprograma",		idPrograma);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaProgramas = this.mapearPrograma(out);
				if(listaProgramas.size()>0) {
					programa = listaProgramas.get(0);
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ProgramacionDAOImpl.obtenerProgramas";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		
		return programa;
	}
	@Override
	public void actualizarDatosPrograma(Programa programa, List<Auditoria> listaAuditoriasEliminadas,
			List<Auditoria> listaAuditoriasNuevas) {
		// TODO Auto-generated method stub
		try {
			Programa datosPrograma = modificarPrograma(programa);
			
			
			if(datosPrograma != null) {
				
				for(Auditoria auditoriaEliminada:listaAuditoriasEliminadas) {
					auditoriaEliminada.setIdPrograma(datosPrograma.getIdPrograma());
					auditoriaEliminada.setDatosAuditoria(datosPrograma.getDatosAuditoria());
					boolean elimino = this.auditoriaDao.eliminarNormasAuditoria(auditoriaEliminada);
					elimino = this.auditoriaDao.eliminarAuditoria(auditoriaEliminada);
				}
				
				
				for(Auditoria auditoria: listaAuditoriasNuevas) {
					auditoria.setIdPrograma(datosPrograma.getIdPrograma());
					auditoria.setDatosAuditoria(datosPrograma.getDatosAuditoria());
					auditoria.setCicloAuditoria("1");
					Auditoria auditoriaRegistro = registrarAuditoria(auditoria);
					if(auditoriaRegistro != null) {
						for(Norma norma:auditoriaRegistro.getListaNormas()) {
							boolean registro =this.auditoriaDao.registrarNormaAuditoria(norma, auditoriaRegistro);
							if(!registro) {
								break;
							}
						}
					}
				}
				
				if(programa.getProcesoPrograma().equals("2")) {
					this.auditoriaDao.actualizarAuditoriasPrograma(programa.getIdPrograma(), "2", programa.getDatosAuditoria().getUsuarioCreacion());
				}
				
				
			}
			
			
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en ProgramacionDAOImpl.actualizarDatosPrograma";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		
	}
	@Override
	public Programa modificarPrograma(Programa programa) {
		Map<String, Object> out = null;
		Long idPrograma = null;
		Programa datosPrograma = null;
		this.error = null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_PROGRAMACION_UPDATE")
						.declareParameters(
							new SqlParameter("i_idprograma",		OracleTypes.NUMBER),
							new SqlParameter("i_vusuario",	OracleTypes.VARCHAR),
							new SqlParameter("i_vdespro",			OracleTypes.VARCHAR),
							new SqlParameter("i_vproprg", 			OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_idprograma",	programa.getIdPrograma())
						.addValue("i_vusuario",		programa.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_vdespro",	programa.getDescripcion())
						.addValue("i_vproprg", programa.getProcesoPrograma());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				datosPrograma = programa;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}

	}catch(Exception e) {
		Integer resultado		= 1;
		String mensaje			= "Error en ProgramacionDAOImpl.modificarPrograma";
		String mensajeInterno	= e.getMessage();
		String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
		LOGGER.error(error[1], e);
		this.error				= new Error(resultado,mensaje,mensajeInterno);
	}
		
	return datosPrograma;
	
	}
	
	@Override
	public boolean eliminarDatosPrograma(Long idPrograma,String usuario, List<Auditoria> listaAuditorias) {
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion(usuario);
		boolean eliminoAuditoria = true;
		boolean eliminoPrograma = false;
		try {
			
			for(Auditoria auditoria:listaAuditorias) {
				auditoria.setDatosAuditoria(datosAuditoria);
				boolean eliminoNormas = this.auditoriaDao.eliminarNormasAuditoria(auditoria);
				if(eliminoNormas) {
					eliminoAuditoria = this.auditoriaDao.eliminarAuditoria(auditoria);
				}
				
				if(!eliminoAuditoria) {
					break;
				}
			}
			if(eliminoAuditoria) {
				eliminoPrograma = this.eliminarPrograma(idPrograma, usuario);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en ProgramacionDAOImpl.eliminarDatosPrograma";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		
		return eliminoPrograma;
		
	}
	
	
}
