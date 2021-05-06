package pe.com.sedapal.agi.dao.impl;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IPreguntaCursoDAO;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.request_objects.PreguntaCursoRequest;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.PreguntaDetalle;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;

@Repository
public class PreguntaCursoDAOImpl implements IPreguntaCursoDAO {
	
	
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	private static final Logger LOGGER = Logger.getLogger(PreguntaCursoDAOImpl.class);	
	
	@Override
	public List<PreguntaCurso> obtenerPregunta(PreguntaCursoRequest preguntaRequest, PageRequest paginaRequest){		
		Map<String, Object> out	= null;
		List<PreguntaCurso> listaPregunta = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_PREGUNTA_OBTENER")
					.declareParameters(
							new SqlParameter("i_v_codcurs", 	OracleTypes.VARCHAR),
							new SqlParameter("i_v_nomcurs", 	OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 		OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			System.out.println("entro al metodo ini: ");
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_v_codcurs", preguntaRequest.getCodCurso())
						.addValue("i_v_nomcurs", preguntaRequest.getNomCurso())		
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaPregunta = new ArrayList<>();
				listaPregunta = mapearPregunta(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.obtenerPregunta";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaPregunta;
	}
	
	
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
	
	private List<PreguntaCurso> mapearPregunta(Map<String,Object> resultados){
		List<PreguntaCurso> listaPregunta = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		PreguntaCurso item = null;
		for(Map<String, Object> map : lista) {	
			item = new PreguntaCurso();
			
			if(map.get("v_codcurs") != null) {
				item.setCodCurso((String)map.get("v_codcurs"));
			}
			if(map.get("v_nomcurs") != null) {
				item.setNomCurso((String)map.get("v_nomcurs"));
			}
			if(map.get("v_tipopreg") != null) {
				item.setTipo((String)map.get("v_tipopreg"));
			}
			if(map.get("n_idpreg") != null) {
				item.setCodPregunta(((BigDecimal) map.get("n_idpreg")).longValue());
				
				List<PreguntaDetalle> list= new ArrayList<>();
						list=this.obtenerDetalle(item.getCodPregunta());
				item.setListPregunta(list);
			}
			if(map.get("v_descpreg") != null) {
				item.setPregunta((String)map.get("v_descpreg"));
			}
			if(map.get("n_pesopreg") != null) {
				item.setPuntaje(((BigDecimal) map.get("n_pesopreg")).longValue());
			}
			if(map.get("n_dispreg") != null) {
				item.setDisponibilidad(((BigDecimal) map.get("n_dispreg")).longValue());
			}
			if(map.get("n_idcurs") != null) {
				item.setIdCurso(((BigDecimal) map.get("n_idcurs")).longValue());
			}
			if(map.get("nomTipo") != null) {
				item.setNomTipo((String)map.get("nomTipo"));
			}
			listaPregunta.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaPregunta;
	}
	
	
	public PreguntaCurso guardarPregunta(PreguntaCurso preguntaCurso){
		PreguntaCurso pregunta=null;
		Boolean registro=false;
		try {
			pregunta=this.registrarPregunta(preguntaCurso);
			if(preguntaCurso.getListPregunta().size()>0) {
				registro=true;
				for(PreguntaDetalle detalle:preguntaCurso.getListPregunta()) {
					detalle.setCodPregunta(pregunta.getCodPregunta());
//					detalle.setDatosAuditoria(pregunta.getDatosAuditoria().getUsuarioCreacion());
					 this.registrarDetalle(detalle);
				}
			}
		}catch(Exception e) {
			registro=false;
			System.out.println("Error");
		}
		return pregunta;
	}
	
	
	public PreguntaCurso guardarDetalle(PreguntaCurso preguntaCurso) {
		PreguntaCurso pregunta=null;
		List<PreguntaDetalle> listDetalle= new ArrayList<>();
		try {
			Long id= preguntaCurso.getCodPregunta();
			pregunta=this.actualizarPregunta(id, preguntaCurso);
			listDetalle=this.obtenerDetalle(id);
			
			if(listDetalle.size()==preguntaCurso.getListPregunta().size()) {
			
				
				for(PreguntaDetalle detalleLista: preguntaCurso.getListPregunta()) {
					if(detalleLista.getIdDetalle()!=0 && detalleLista.getIdDetalle()!=null) {
						this.actualizarDetalle(detalleLista);
					}else {
						this.registrarDetalle(detalleLista);
						
					}
					
					
				}
			}else {

				for(PreguntaDetalle detalleLista: preguntaCurso.getListPregunta()) {
					if(detalleLista.getIdDetalle()!=0 && detalleLista.getIdDetalle()!=null) {
						this.actualizarDetalle(detalleLista);
					}else {
						detalleLista.setCodPregunta(preguntaCurso.getCodPregunta());
						this.registrarDetalle(detalleLista);
						
					}
					
					
				}
			}
			

			
		}catch(Exception e) {
			System.out.println("Error");
		}
		return pregunta;
	}
	
	@Override
	public PreguntaCurso registrarPregunta(PreguntaCurso preguntaCurso) {
		Map<String, Object> out = null;
		this.error=null;
		boolean registro = false;
		Long idPregunta=null;
		PreguntaCurso pregunta=null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_MANTENIMIENTO")
						.withProcedureName("PRC_PREGUNTA_GUARDAR")
						.declareParameters(
								new SqlParameter("i_n_idcurs",		OracleTypes.NUMBER),
								new SqlParameter("i_n_idpreg",		OracleTypes.NUMBER),
								new SqlParameter("i_v_descpreg",	OracleTypes.VARCHAR),
								new SqlParameter("i_v_tipopreg",	OracleTypes.VARCHAR),
								new SqlParameter("i_n_pesopreg",	OracleTypes.NUMBER),
								new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
//								new SqlParameter("i_n_indiresp",		OracleTypes.NUMBER),
								new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
								new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
								new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
 			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_n_idcurs", preguntaCurso.getCodCurso())
						.addValue("i_n_idpreg", preguntaCurso.getCodPregunta())
						.addValue("i_v_descpreg",		preguntaCurso.getPregunta())
						.addValue("i_v_tipopreg",		preguntaCurso.getTipo())
						.addValue("i_n_pesopreg",		preguntaCurso.getPuntaje())
//						.addValue("i_vusuario",		preguntaCurso.getDatosAuditoria().getUsuarioCreacion())
						.addValue("i_vusuario",		"AGI");
//						.addValue("i_n_indiresp",		preguntaCurso.getValorRespuesta());
				
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				Long  idPreg=((BigDecimal)out.get("O_NIDPREG")).longValue();
				System.out.println(idPreg);
				preguntaCurso.setCodPregunta(idPreg);
				pregunta = preguntaCurso;
				registro=true;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				registro=false;
			}

		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.registrarPregunta";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			registro=false;
		}
		return pregunta;
	}
	
	public PreguntaDetalle registrarDetalle(PreguntaDetalle preguntaDetalle) {
		Map<String, Object> out = null;
		this.error=null;
		boolean registro = false;
		
		PreguntaDetalle detalle=null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_MANTENIMIENTO")
						.withProcedureName("PRC_RESPUESTA_REGISTRAR")
						.declareParameters(
								new SqlParameter("i_n_iddetapreg",  OracleTypes.NUMBER),
								new SqlParameter("i_n_idpreg",		OracleTypes.NUMBER),
								new SqlParameter("i_v_descdetprg",	OracleTypes.VARCHAR),
								new SqlParameter("i_n_dispdetprg",	OracleTypes.VARCHAR),
								new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
								new SqlParameter("i_n_indiresp",	OracleTypes.NUMBER),
								new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
								new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
								new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
								new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			if(preguntaDetalle.getCodDetalle()>=0) {
				preguntaDetalle.setCodDetalle(null);
			}
			
				preguntaDetalle.setIdDetalle(null);
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_n_iddetapreg", preguntaDetalle.getIdDetalle())
						.addValue("i_n_idpreg", preguntaDetalle.getCodPregunta())
						.addValue("i_v_descdetprg", preguntaDetalle.getDescPregunta())
						.addValue("i_n_dispdetprg", preguntaDetalle.getDisponibilidad())
//						.addValue("i_vusuario",		preguntaDetalle.getDatosAuditoria())
						.addValue("i_vusuario",		"AGI")
						.addValue("i_n_indiresp",		preguntaDetalle.getValorRespuesta());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				
				detalle = preguntaDetalle;
				registro=true;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				registro=false;
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaDetalleDAOImpl.registrarRespuesta";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			registro=false;
		}
		return detalle;
	}
	
	
	public Boolean eliminarPregunta(Long id) {
		Map<String, Object> out = null;
		this.error=null;
		boolean registro = false;
		
		PreguntaCurso pregunta=null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_MANTENIMIENTO")
						.withProcedureName("PRC_PREGUNTA_ELIMINAR")
						.declareParameters(
								new SqlParameter("i_n_idpreg",		OracleTypes.NUMBER),
								new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
								new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
								new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
								new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_n_idpreg", id)
						.addValue("i_vusuario",	"AGI");
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				
				registro=true;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				registro=false;
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaDetalleDAOImpl.eliminarPregunta";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			registro=false;
		}
		return registro;
	}
	
	public List<ConstanteRequest> obtenerTipoPregunta(){
		Map<String, Object> out	= null;
		List<ConstanteRequest> constant = null;
		this.error = null;
		
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_TIPO_PREGUNTA")
					.declareParameters(	
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			
			
			SqlParameterSource in =
					new MapSqlParameterSource();
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				constant = new ArrayList<>();
				constant = mapearConstante(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.obtenerPregunta";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return constant;
	}
	
	private List<ConstanteRequest> mapearConstante(Map<String,Object> resultados){
		List<ConstanteRequest> listaTipo = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		ConstanteRequest item = null;
		for(Map<String, Object> map : lista) {	
			item = new ConstanteRequest();
			
			if(map.get("n_idcons") != null) {
				item.setIdconstante(((BigDecimal)map.get("n_idcons")).longValue());
			}
			if(map.get("v_valcons") != null) {
				item.setV_nomcons((String)map.get("v_valcons"));
			}
			
			listaTipo.add(item);


		}
		
		return listaTipo;
	}
	
	
	public PreguntaCurso actualizarPregunta(Long id, PreguntaCurso preguntaCurso) {
		this.error = null;
		Map<String, Object> out = null;
		PreguntaCurso pregunta=null;
		try {
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_PREGUNTA_GUARDAR")
				.declareParameters(
						new SqlParameter("i_n_idcurs",		OracleTypes.NUMBER),
						new SqlParameter("i_n_idpreg",		OracleTypes.NUMBER),
						new SqlParameter("i_v_descpreg",	OracleTypes.VARCHAR),
						new SqlParameter("i_v_tipopreg",	OracleTypes.VARCHAR),
						new SqlParameter("i_n_pesopreg",	OracleTypes.NUMBER),
						new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
						new SqlParameter("i_n_indiresp",		OracleTypes.NUMBER),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		
		SqlParameterSource in =
				new MapSqlParameterSource()
					.addValue("i_n_idcurs", preguntaCurso.getCodCurso())
					.addValue("i_n_idpreg", id)
					.addValue("i_v_descpreg",		preguntaCurso.getPregunta())
					.addValue("i_v_tipopreg",		preguntaCurso.getTipo())
					.addValue("i_n_pesopreg",		preguntaCurso.getPuntaje())
//					.addValue("i_vusuario",		preguntaCurso.getDatosAuditoria().getUsuarioModificacion())
					.addValue("i_vusuario",		"AGI")
					.addValue("i_n_indiresp",		preguntaCurso.getValorRespuesta());
		
		out = this.jdbcCall.execute(in);
		Integer resultado = (Integer)out.get("o_retorno");
		
		if(resultado == 0) {
			
			pregunta = preguntaCurso;
			
		} else {
			String mensaje			= (String)out.get("o_mensaje");
			String mensajeInterno	= (String)out.get("o_sqlerrm");			
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.obtenerPregunta";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return pregunta;
	}
	
	
	public PreguntaDetalle actualizarDetalle(PreguntaDetalle preguntaDetalle) {
		Map<String, Object> out = null;
		this.error=null;
		boolean registro = false;
		
		PreguntaDetalle detalle=null;
		
		try {
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_MANTENIMIENTO")
						.withProcedureName("PRC_RESPUESTA_REGISTRAR")
						.declareParameters(
								new SqlParameter("i_n_iddetapreg",  OracleTypes.NUMBER),
								new SqlParameter("i_n_idpreg",		OracleTypes.NUMBER),
								new SqlParameter("i_v_descdetprg",	OracleTypes.VARCHAR),
								new SqlParameter("i_n_dispdetprg",	OracleTypes.VARCHAR),
								new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
								new SqlParameter("i_n_indiresp",	OracleTypes.NUMBER),
								new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
								new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
								new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
								new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_n_iddetapreg", preguntaDetalle.getIdDetalle())
						.addValue("i_n_idpreg", preguntaDetalle.getCodPregunta())
						.addValue("i_v_descdetprg", preguntaDetalle.getDescPregunta())
						.addValue("i_n_dispdetprg", preguntaDetalle.getDisponibilidad())
//						.addValue("i_vusuario",		preguntaDetalle.getDatosAuditoria())
						.addValue("i_vusuario",		"AGI")
						.addValue("i_n_indiresp",		preguntaDetalle.getValorRespuesta());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				
				detalle = preguntaDetalle;
				registro=true;
			}else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				registro=false;
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaDetalleDAOImpl.actualizarDetalle";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			registro=false;
		}
		return detalle;
	}
	

	@Override
	public List<PreguntaCurso> obtenerCurso(PreguntaCursoRequest preguntaRequest, PageRequest paginaRequest){		
		Map<String, Object> out	= null;
		List<PreguntaCurso> listaCurso = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_CURSO_OBTENER")
					.declareParameters(
							new SqlParameter("c_vcodcurs", 	OracleTypes.VARCHAR),
							new SqlParameter("c_vnomcurs", 	OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 		OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			System.out.println("entro al metodo ini: ");
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("c_vcodcurs", preguntaRequest.getCodCurso())
						.addValue("c_vnomcurs", preguntaRequest.getNomCurso())		
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaCurso = new ArrayList<>();
				listaCurso = mapearCurso(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.obtenerCurso";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaCurso;
	}
	
	private List<PreguntaCurso> mapearCurso(Map<String,Object> resultados){
		List<PreguntaCurso> listaCurso = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		PreguntaCurso item = null;
		for(Map<String, Object> map : lista) {	
			item = new PreguntaCurso();
			
			if(map.get("n_idcurs") != null) {
				item.setIdCurso(((BigDecimal)map.get("n_idcurs")).longValue());
			}
			if(map.get("v_codcurs") != null) {
				item.setCodCurso((String)map.get("v_codcurs"));
			}
			if(map.get("v_nomcurs") != null) {
				item.setNomCurso((String)map.get("v_nomcurs"));
			}
			if(map.get("n_idtipocurs") != null) {
				item.setIdTipoCurso(((BigDecimal)map.get("n_idtipocurs")).longValue());
			}
			if(map.get("n_discurs") != null) {
				item.setDisponibilidadCurso(((BigDecimal) map.get("n_discurs")).longValue());
			}
			if(map.get("descTipo") != null) {
				item.setDescTipoCurso((String)map.get("descTipo"));
			}
			listaCurso.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaCurso;
	}
	
	@Override
	public List<PreguntaDetalle> obtenerDetalle(Long id){		
		Map<String, Object> out	= null;
		List<PreguntaDetalle> listaDetalle = null;
		this.error = null;
		
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_DETALLE_OBTENER")
					.declareParameters(
							new SqlParameter("i_n_idpreg", 	OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	

			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_n_idpreg",id);
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaDetalle = new ArrayList<>();
				listaDetalle = mapearDetalle(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.obtenerDetalle";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaDetalle;
	}
	
	private List<PreguntaDetalle> mapearDetalle(Map<String,Object> resultados){
		List<PreguntaDetalle> listaDetalle = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		PreguntaDetalle item = null;
		for(Map<String, Object> map : lista) {	
			item = new PreguntaDetalle();
			
			if(map.get("n_iddetapreg") != null) {
				item.setIdDetalle(((BigDecimal)map.get("n_iddetapreg")).longValue());
			}
			if(map.get("v_valodetprg") != null) {
				item.setDescPregunta((String)map.get("v_valodetprg"));
			}
			if(map.get("n_dispdetprg") != null) {
				item.setDisponibilidad(((BigDecimal)map.get("n_dispdetprg")).longValue());
			}
			if(map.get("n_indiresp") != null) {
				item.setValorRespuesta(((BigDecimal)map.get("n_indiresp")).longValue());
			}
			if(map.get("n_idpreg") != null) {
				item.setCodPregunta(((BigDecimal) map.get("n_idpreg")).longValue());
			}
			if(map.get("nomDisp") != null) {
				item.setNomDisp((String)map.get("nomDisp"));
			}
			if(map.get("nomResp") != null) {
				item.setNomResp((String)map.get("nomResp"));
			}
			listaDetalle.add(item);
		}
		
		return listaDetalle;
	}
}
