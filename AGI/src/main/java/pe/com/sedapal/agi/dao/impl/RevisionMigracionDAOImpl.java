package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pe.com.sedapal.agi.model.response_objects.Error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.IRevisionDAO;
import pe.com.sedapal.agi.dao.IRevisionMigracionDAO;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class RevisionMigracionDAOImpl implements IRevisionMigracionDAO {
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
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@SuppressWarnings("unchecked")
	public List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest) {
		//Date fechaRevision = null;
		Date fechaInicio = null;
		Date fechaFinal = null;
		SqlParameterSource in = null;
		Map<String, Object> out	= null;
		List<Revision> lista	= new ArrayList<>();
		this.paginacion = new Paginacion();
		
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("pck_agi_revision_migracion")
			.withProcedureName("PRC_REVISION_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 		OracleTypes.NUMBER),						
				//new SqlParameter("i_nnumero", 	OracleTypes.NUMBER),
				new SqlParameter("i_codigoDoc", 	OracleTypes.VARCHAR),
				new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
				//new SqlParameter("i_nestrevi",OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",OracleTypes.NUMBER),
				//new SqlParameter("i_dfecrevi",OracleTypes.DATE),
				new SqlParameter("i_fecRegInicio",OracleTypes.DATE),
				new SqlParameter("i_fecRegFinal",OracleTypes.DATE),
				
				//new SqlParameter("i_colaborador",OracleTypes.VARCHAR),
				new SqlParameter("i_tituloDoc",OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		try {
		/*if(revisionRequest.getFechaRevision() != null) {
			fechaRevision = formatter.parse(revisionRequest.getFechaRevision());
		}*/
		 
			if(revisionRequest.getFechaInicio() != null) {
				fechaInicio = formatter.parse(revisionRequest.getFechaInicio());
			}
			
			if(revisionRequest.getFechaFinal() != null) {
				fechaFinal = formatter.parse(revisionRequest.getFechaFinal());
			}
		} catch (ParseException e1) {
			fechaInicio = null;
			fechaFinal = null;
		}
			System.out.println("id documento dd "+ revisionRequest.getIdDocumento());
			in = new MapSqlParameterSource()
				.addValue("i_nid",			revisionRequest.getId())
				//.addValue("i_nnumero",		revisionRequest.getNumero())
				.addValue("i_codigoDoc",		revisionRequest.getCodigoDoc())
				.addValue("i_ndocumento",	revisionRequest.getIdDocumento())
				 //.addValue("i_nestrevi",	revisionRequest.getEstado())
				.addValue("i_fecRegInicio",	fechaInicio) 
				.addValue("i_fecRegFinal",	fechaFinal)
				//.addValue("i_dfecrevi",	fechaRevision)
				 //.addValue("i_colaborador",	revisionRequest.getNombreCompleto())
				.addValue("i_tituloDoc",	revisionRequest.getTituloDoc())
				.addValue("i_npagina",		pageRequest.getPagina())
				.addValue("i_nregistros",	pageRequest.getRegistros());
		
		    
			try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
			     lista = this.mapearRevision(listado);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerRevision";
			String mensajeInterno	= e.toString();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Revision> obtenerRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest, Long idUsuario) {
		//Date fechaRevision = null;
		
		SqlParameterSource in = null;
		Map<String, Object> out	= null;
		List<Revision> lista	= new ArrayList<>();
		this.paginacion = new Paginacion();
		
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		//SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("pck_agi_revision_migracion")
			.withProcedureName("PRC_REVISION_FASE_OBTENER")
			.declareParameters(
				new SqlParameter("i_idUsuario", 	OracleTypes.NUMBER),
				new SqlParameter("i_ncoddocu", 		OracleTypes.VARCHAR),						
				new SqlParameter("i_nidrevi", 		OracleTypes.NUMBER),
				new SqlParameter("i_fase", 	OracleTypes.NUMBER),
				new SqlParameter("i_ddesDocu",		OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlParameter("i_dfecrevi",		OracleTypes.DATE),
				new SqlParameter("i_colaborador",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		/*try {
		if(revisionRequest.getFechaRevision() != null) {
			fechaRevision = formatter.parse(revisionRequest.getFechaRevision());
		}
		 
		} catch (ParseException e1) {
			fechaRevision = null;
		}*/
	
	
			in = new MapSqlParameterSource()
				.addValue("i_idUsuario", 		154)//idUsuario)
				.addValue("i_ncoddocu",			revisionRequest.getCodigoDoc())//revisionRequest.getCodigorevision()
				.addValue("i_nidrevi",			revisionRequest.getId())//revisionRequest.getNumero()
				.addValue("i_ddesDocu",			revisionRequest.getTituloDoc()) //revisionRequest.getTitulo()
				.addValue("i_fase",	revisionRequest.getIdFase())
				.addValue("i_npagina",			pageRequest.getPagina())
				.addValue("i_nregistros",		pageRequest.getRegistros());		    
			try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
			     lista = this.mapearRevision(listado);//mapearRevisionElaboracion
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerRevisionFase";
			String mensajeInterno	= e.toString();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<Revision> obtenerRevisionHistorico(RevisionRequest revisionRequest, PageRequest pageRequest) {
		
		Map<String, Object> out	= null;
		List<Revision> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("pck_agi_revision_migracion")
			.withProcedureName("PRC_REVISIONHIST_OBTENER")
			.declareParameters(
				//new SqlParameter("i_nnumero", 	OracleTypes.NUMBER),
				new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			//.addValue("i_nnumero",		revisionRequest.getNumero())
			.addValue("i_ndocumento",	revisionRequest.getIdDocumento())
			.addValue("i_npagina",		pageRequest.getPagina())
			.addValue("i_nregistros",	pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
			     lista = this.mapearRevision(listado);
				/*List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {			
					Revision item = new Revision();
					item.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
					item.setTitulo((String)map.get("V_NOMREVI"));
					item.setNumero(((BigDecimal)map.get("N_NUMREVI")).longValue());
					item.setFecha((Date)map.get("D_FECREVI"));
					item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
					lista.add(item);
				}*/		
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerRevisionHistorico";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	/*private List<Revision> mapearRevisionElaboracion(List<Map<String, Object>> listado) {
		List<Revision> mLista	= new ArrayList<>();
		Revision item = null;
		int totalListado = listado.size();
		Integer totalRegistro = 0;
		for(Map<String, Object> map : listado) {			
			 item = new Revision();
			 
				 	item.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
					item.setTitulo((String)map.get("V_NOMREVI"));
					item.setNumero(((BigDecimal)map.get("N_NUMREVI")).longValue());
					
					item.setFecha((Date)map.get("D_FECREVI"));
					
					item.setCodigo((String)map.get("V_CODREVI"));
					if(map.get("N_ESTREVI")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_ESTREVI")).longValue());
						estado.setV_descons((String)map.get("V_ESTREVI"));
						item.setEstado(estado);
					}
					item.setDocumento(new Documento());
					if(map.get("V_ESTREVIDOC")!=null) {
						Constante estado = new Constante();
						estado.setV_descons((String)map.get("V_ESTREVIDOC"));
						item.getDocumento().setEstado(estado);
					}
					
					if(map.get("V_CODDOCU")!=null) {
						item.getDocumento().setCodigo((String)map.get("V_CODDOCU"));
						
					}
					if(map.get("V_DESDOCU")!=null) {
						item.getDocumento().setDescripcion((String)map.get("V_DESDOCU"));
						
					}
					item.setColaborador(new Colaborador());
					if(map.get("V_NOMBRES")!=null) {
						item.getColaborador().setNombre((String)map.get("V_NOMBRES"));
					}
					if(map.get("V_APEPAT")!=null) {
						item.getColaborador().setApellidoPaterno((String)map.get("V_APEPAT"));
					}
					if(map.get("V_APEMAT")!=null) {
						item.getColaborador().setApellidoMaterno((String)map.get("V_APEMAT"));
					}
					if(map.get("N_IDDOCUHIST")!=null) {
						item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
					}
					item.setEquipo(new Equipo());
					if(map.get("V_NOMEQUI")!=null) {
						item.getEquipo().setNombre((String)map.get("V_NOMEQUI"));
					}
					
					if(map.get("V_DESREVI")!=null) {
						item.setDescripcion((String)map.get("V_DESREVI"));
					}
					
					if(map.get("V_NOMOTIREVI")!=null) {
						item.setMotivoRevision((String)map.get("V_NOMOTIREVI"));
					}
					if(map.get("D_FEAPRO")!=null) {
						item.setFechaAprobacion((Date)map.get("D_FEAPRO"));
					}
					if(map.get("D_FEPLAZAPRO")!=null) {
						item.setFechaPlazoAprob((Date)map.get("D_FEPLAZAPRO"));
					}
					
					if(map.get("N_IDESTA")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
						estado.setV_descons((String)map.get("V_NOESTA"));
						item.setEstado(estado);
					}
					
					if(map.get("N_IDESTAFASEACT")!=null) {
						Constante estadofaseact = new Constante();
						estadofaseact.setIdconstante(((BigDecimal)map.get("N_IDESTAFASEACT")).longValue());
						estadofaseact.setV_descons((String)map.get("V_NOESTAFASEACT"));
						item.setEstadofaseact(estadofaseact);
					}
					if(map.get("PLAZO_DIFERENCIA")!=null) {
						item.setPlazoDiferencia(((BigDecimal)map.get("PLAZO_DIFERENCIA")).longValue());
					}					
					
					mLista.add(item);
					if(map.get("RESULT_COUNT")!= null) {
						totalRegistro = ((BigDecimal)map.get("RESULT_COUNT")).intValue();
					}
			
		}
		if (this.paginacion != null && totalListado>0) {
			this.paginacion.setTotalRegistros(totalRegistro);
		}
		return mLista;
	}*/
	
	private List<Revision> mapearRevision(List<Map<String, Object>> listado) {
		List<Revision> mLista	= new ArrayList<>();
		Revision item = null;
		int totalListado = listado.size();
		Integer totalRegistro = 0;
		for(Map<String, Object> map : listado) {			
			 item = new Revision();
			 
				 	item.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
					item.setTitulo((String)map.get("V_NOMREVI"));
					item.setNumero(((BigDecimal)map.get("N_NUMREVI")).longValue());
					
					item.setFecha((Date)map.get("D_FECREVI"));
					
					item.setCodigo((String)map.get("V_CODREVI"));
					if(map.get("N_ESTREVI")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_ESTREVI")).longValue());
						estado.setV_descons((String)map.get("V_ESTREVI"));
						item.setEstado(estado);
					}
					item.setDocumento(new Documento());
					if(map.get("V_ESTREVIDOC")!=null) {
						Constante estado = new Constante();
						estado.setV_descons((String)map.get("V_ESTREVIDOC"));
						item.getDocumento().setEstado(estado);
						
					}
					
					if(map.get("V_CODDOCU")!=null) {
						item.getDocumento().setCodigo((String)map.get("V_CODDOCU"));
						
					}
					if(map.get("V_DESDOCU")!=null) {
						item.getDocumento().setDescripcion((String)map.get("V_DESDOCU"));
						
					}
					
						/*if(map.get("N_IDESTA")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
						estado.setV_descons((String)map.get("V_NOESTA"));
						item.setEstado(estado);
					}*/
					
					if(map.get("N_IDESTAFASEACT")!=null) {
						Constante estadofaseact = new Constante();
						estadofaseact.setIdconstantefaseact(((BigDecimal)map.get("N_IDESTAFASEACT")).longValue());
						estadofaseact.setV_desconsfaseact((String)map.get("V_NOESTAFASEACT"));
						item.setEstadofaseact(estadofaseact);
					}
		
					if(map.get("N_IDDOCU")!=null) {
						item.getDocumento().setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
					}
					item.setColaborador(new Colaborador());
					if(map.get("V_NOMBRES")!=null) {
						item.getColaborador().setNombre((String)map.get("V_NOMBRES"));
					}
					if(map.get("V_APEPAT")!=null) {
						item.getColaborador().setApellidoPaterno((String)map.get("V_APEPAT"));
					}
					if(map.get("V_APEMAT")!=null) {
						item.getColaborador().setApellidoMaterno((String)map.get("V_APEMAT"));
					}
					if(map.get("N_IDDOCUHIST")!=null) {
						item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
					}
					item.setEquipo(new Equipo());
					if(map.get("V_NOMEQUI")!=null) {
						item.getEquipo().setNombre((String)map.get("V_NOMEQUI"));
					}
					
					if(map.get("V_DESREVI")!=null) {
						item.setDescripcion((String)map.get("V_DESREVI"));
					}
					if(map.get("N_IDMOTIREVI")!=null) {
						item.setIdmotirevi(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
					}
					
					if(map.get("V_NOMOTIREVI")!=null) {
						item.setMotivoRevision((String)map.get("V_NOMOTIREVI"));
					}
					if(map.get("D_FEAPRO")!=null) {
						item.setFechaAprobacion((Date)map.get("D_FEAPRO"));
					}
					if(map.get("D_FEPLAZAPRO")!=null) {
						item.setFechaPlazoAprob((Date)map.get("D_FEPLAZAPRO"));
					}
					if(map.get("D_PLAZPART")!= null) {
						item.setPlazoParticipante((Date)map.get("D_PLAZPART"));
					}
					
					if(map.get("PLAZO_DIFERENCIA")!= null) {
						item.setDiferenciaPlazo(((BigDecimal)map.get("PLAZO_DIFERENCIA")).longValue());
					}
					
					if(map.get("V_NOESTAFASEACT")!=null) {
						/*Constante estado = new Constante();
						estado.setV_descons((String)map.get("V_ESTAFASEACTUAL"));
						item.getDocumento().setEstadoFaseActual(estado);*/
						Constante estadofaseact = new Constante();
						estadofaseact.setIdconstante(((BigDecimal)map.get("N_IDESTAFASEACT")).longValue());
						estadofaseact.setV_descons((String)map.get("V_NOESTAFASEACT"));
						item.setEstadofaseact(estadofaseact);
					}
					mLista.add(item);
					if(map.get("RESULT_COUNT")!= null) {
						totalRegistro = ((BigDecimal)map.get("RESULT_COUNT")).intValue();
					}
					
					if(map.get("N_IDDOCGOOGLEDRIVE")!=null) {
						item.setIdDocGoogleDrive((String)map.get("N_IDDOCGOOGLEDRIVE"));
					}
					
			
		}
		if (this.paginacion != null && totalListado>0) {
			this.paginacion.setTotalRegistros(totalRegistro);
		}
		return mLista;
	}
	

public Boolean eliminarRevision(Long idRevision) {
				
		Map<String, Object> out = null;
		/*if(idRevision == null) {
			idRevision = 0L;
		}*/	
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("pck_agi_revision_migracion")
				.withProcedureName("prc_revision_eliminar")
				.declareParameters(
						new SqlParameter("i_nid", OracleTypes.NUMBER),	
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
						SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_nid", idRevision)
						.addValue("i_vusuario", "usuario");
						//.addValue("i_vresact", usuario);	
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

public Revision crearRevision(Revision revision, String iUsuario, Long idUsuario, Date FechaCreaDoc) {
	List<Revision> lista	= new ArrayList<>();
	Map<String, Object> out = null;
	/*if(idRevision == null) {
		idRevision = 0L;
	}*/	
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("pck_agi_revision_migracion")
			.withProcedureName("prc_revision_guardar")
			.declareParameters(			
					new SqlParameter("i_dfecha_registr", OracleTypes.DATE),
					new SqlParameter("i_numrevi", OracleTypes.NUMBER),
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_idrevi", OracleTypes.NUMBER),
					new SqlParameter("i_fecrevi", OracleTypes.DATE),
					new SqlParameter("i_usurevi", OracleTypes.NUMBER),					
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_nomrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_codrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_rutrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_iddocu", OracleTypes.NUMBER),
					new SqlParameter("i_desrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_idmotirevi", OracleTypes.NUMBER),
					new SqlParameter("i_estrevi", OracleTypes.NUMBER),
					new SqlParameter("i_feplazapro", OracleTypes.DATE),
					new SqlParameter("i_feapro", OracleTypes.DATE),
					new SqlParameter("i_nusuapro", OracleTypes.NUMBER),					
					new SqlParameter("i_rechrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_iddocuhist", OracleTypes.NUMBER),
					new SqlParameter("i_idcola", OracleTypes.NUMBER),
					new SqlParameter("i_usurevi", OracleTypes.NUMBER),					
					new SqlParameter("i_ruta_c_con", OracleTypes.VARCHAR),
					new SqlParameter("i_ruta_origi", OracleTypes.VARCHAR),
					new SqlParameter("i_ruta_c_ncot", OracleTypes.VARCHAR),
					new SqlParameter("i_ruta_c_ob", OracleTypes.VARCHAR),	
					new SqlParameter("i_ruta_doc", OracleTypes.VARCHAR),
					new SqlParameter("i_nusaprodocu",  OracleTypes.NUMBER),
					new SqlParameter("i_dfeaprodocu",	OracleTypes.DATE),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
					SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_ruta_doc", revision.getRutaDocumentoGoogle())
					.addValue("i_ruta_c_con", revision.getRutaDocumentoCopiaCont())
					.addValue("i_ruta_origi", revision.getRutaDocumentoOriginal())
					.addValue("i_ruta_c_ncot", revision.getRutaDocumentoCopiaNoCont())
					.addValue("i_ruta_c_ob", revision.getRutaDocumentoCopiaObso())
							.addValue("i_dfecha_registr", FechaCreaDoc)
					.addValue("i_numrevi", revision.getNumero())
					.addValue("i_vusuario", "AGI_MIGRACIÓN")
					.addValue("i_idrevi", revision.getId() == null ? 0 : revision.getId())//revision.getId())
					.addValue("i_fecrevi", revision.getFechaRegistroSOlicit())
					.addValue("i_usurevi", 999999999)//revision.getUsuarioRevision())
					.addValue("i_nomrevi", revision.getTitulo())
					.addValue("i_codrevi", revision.getCodigo())
					.addValue("i_rutrevi", revision.getRuta())
					.addValue("i_iddocu", revision.getDocumento().getId())
					.addValue("i_desrevi", revision.getDescripcion())
					.addValue("i_idmotirevi", revision.getIdmotirevi())
					.addValue("i_feplazapro", revision.getFecPlazoAprobacion())					
					.addValue("i_feapro", revision.getFechaAprobacion())
					.addValue("i_nusuapro", 999999999)//revision.getUsuarioDeAprobacion())
					.addValue("i_rechrevi", revision.getMotivoRechazoRev())
					.addValue("i_iddocuhist", revision.getIdHistorial())
					.addValue("i_estrevi", 143)//estado aprobado
					.addValue("i_idcola", idUsuario)
					.addValue("i_nusaprodocu", revision.getIdUsuarioAprobDocu())
					.addValue("i_dfeaprodocu", revision.getFechaAprobDocu());
					
					
				
									
	try {
		out=this.jdbcCall.execute(in);
		Integer resultado = (Integer)out.get("o_retorno");
		if(resultado == 0) {
			 
			 List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
		     lista = this.mapearRevision(listado);
		} else {
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			lista.clear();
		}			
	}
	catch (Exception e) {
		Integer resultado = (Integer)out.get("o_retorno");
		String mensaje = (String)out.get("o_mensaje");
		String mensajeInterno = (String)out.get("o_sqlerrm");
		this.error = new Error(resultado,mensaje,mensajeInterno);
		lista.clear();
	}
	return lista.get(0);
}

	@Override
	public List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Date fechaRevision = null;
		SqlParameterSource in = null;
		Map<String, Object> out	= null;
		List<Revision> lista	= new ArrayList<>();
		this.paginacion = new Paginacion();

		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


		this.error		= null;
		this.jdbcCall	=
				new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_TAREAS")
						.withProcedureName("PRC_LISTA_APROBAR")
						.declareParameters(
								new SqlParameter("i_nid", 		OracleTypes.NUMBER),
								new SqlParameter("i_nnumero", 	OracleTypes.NUMBER),
								new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
								new SqlParameter("i_vdocumento",OracleTypes.VARCHAR),
								new SqlParameter("i_vtitulo",OracleTypes.VARCHAR),
								new SqlParameter("i_nestrevi",OracleTypes.VARCHAR),
								new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
								new SqlParameter("i_nregistros",OracleTypes.NUMBER),
								new SqlParameter("i_dfecrevi",OracleTypes.DATE),
								new SqlParameter("i_colaborador",OracleTypes.VARCHAR),
								new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
								new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
								new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
								new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));
		try {
			if(revisionRequest.getFechaRevision() != null) {
				fechaRevision = formatter.parse(revisionRequest.getFechaRevision());
			}

		} catch (ParseException e1) {
			fechaRevision = null;
		}
		System.out.println("id documento dd "+ revisionRequest.getIdDocumento());
		in = new MapSqlParameterSource()
				.addValue("i_nid",			revisionRequest.getId())
				.addValue("i_nnumero",		revisionRequest.getNumero())
				.addValue("i_ndocumento",	revisionRequest.getIdDocumento())
				.addValue("i_vdocumento",	revisionRequest.getCodigoDoc())
				.addValue("i_vtitulo",	revisionRequest.getTituloDoc())
				.addValue("i_nestrevi",	revisionRequest.getEstado())
				.addValue("i_dfecrevi",	fechaRevision)
				.addValue("i_colaborador",	revisionRequest.getNombreCompleto())
				.addValue("i_npagina",		pageRequest.getPagina())
				.addValue("i_nregistros",	pageRequest.getRegistros());


		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				lista = this.mapearRevision(listado);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerRevision";
			String mensajeInterno	= e.toString();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}

public Revision crearDocumentoGoogleDrive(String idDocGoogle, Long idRevi) {
	List<Revision> lista	= new ArrayList<>();
	Map<String, Object> out = null;
	/*if(idRevision == null) {
		idRevision = 0L;
	}*/	
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("pck_agi_revision_migracion3")
			.withProcedureName("PRC_REVISION_GOOGLEDRIVEID")
			.declareParameters(
					new SqlParameter("i_idrevi", OracleTypes.NUMBER),
					new SqlParameter("i_niddocgoogledrive", OracleTypes.VARCHAR),					
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR)
					);
					SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_idrevi", idRevi)
					.addValue("i_niddocgoogledrive", idDocGoogle);
	try {
		out=this.jdbcCall.execute(in);
		Integer resultado = (Integer)out.get("o_retorno");
		if(resultado == 0) {
			 List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
		     lista = this.mapearRevision(listado);
		} else {
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
			lista.clear();
		}			
	}
	
	catch (Exception e) {
		Integer resultado = (Integer)out.get("o_retorno");
		String mensaje = (String)out.get("o_mensaje");
		String mensajeInterno = (String)out.get("o_sqlerrm");
		this.error = new Error(resultado,mensaje,mensajeInterno);
		lista.clear();
	}
	return lista.get(0);
}
}