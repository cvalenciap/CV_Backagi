package pe.com.sedapal.agi.dao.impl;
 
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pe.com.sedapal.agi.model.response_objects.Error;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IRevisionDAO;
import pe.com.sedapal.agi.dao.impl.RelacionCoordinadorDAOImpl;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.Jefes;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.enums.TipoConstante;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
//import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequestAdjunto;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.security.config.UserAuth;
@Repository
public class RevisionDAOImpl implements IRevisionDAO {
	
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	private static final Logger LOGGER = Logger.getLogger(RevisionDAOImpl.class);	
	
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
	
	@Autowired
	SessionInfo session;
	
	@SuppressWarnings("unchecked")
	public List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 		OracleTypes.NUMBER),						
				new SqlParameter("i_codigoDoc", 	OracleTypes.VARCHAR),
				new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
				new SqlParameter("I_MUSUARIO",OracleTypes.NUMBER),				
				new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",OracleTypes.NUMBER),
				new SqlParameter("i_fecRegInicio",OracleTypes.DATE),
				new SqlParameter("i_fecRegFinal",OracleTypes.DATE),
				new SqlParameter("i_tituloDoc",OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		try {
		 
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
				//.addValue("I_MUSUARIO",     revisionRequest.getIdColaborador())
				.addValue("I_MUSUARIO",    new Long(((UserAuth)principal).getCodPerfil()))
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
			.withCatalogName("PCK_AGI_REVISION")
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
				.addValue("i_idUsuario", 		idUsuario)
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
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISIONHIST_OBTENER")
			.declareParameters(								
					
					new SqlParameter("I_NIDREVI",OracleTypes.NUMBER),
					
				new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
				new SqlParameter("i_nidrevi",OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_ndocumento",	revisionRequest.getIdDocumento())
			.addValue("i_nidrevi",		revisionRequest.getId())
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
			 
			if(map.get("N_IDREVI")!=null) {
				item.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
			}
			if(map.get("N_NUMREVI")!=null) {
				item.setNumero(((BigDecimal)map.get("N_NUMREVI")).longValue());
			}
			if(map.get("V_NOMREVI")!=null) {
				item.setTitulo((String)map.get("V_NOMREVI"));
				
			}
			if(map.get("N_NUMITER")!=null) {
				item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
				
			}
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
				estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
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
			/*//Cguerra
			if(map.get("v_ntipocopia")!=null) {
				item.setTipocopia((String)map.get("v_ntipocopia"));
			}	
			if(map.get("v_nmotivosolicitud")!=null) {
				item.setMotivoR((String)map.get("v_nmotivosolicitud"));
			}
			if(map.get("v_nestadosolicitud")!=null) {
				item.setEstadoSoli((String)map.get("v_nestadosolicitud"));
			}
			
			if(map.get("fechasolicitud")!=null) {
				item.setFechaSolicitud((Date)map.get("fechasolicitud"));
			}
			
			if(map.get("v_solicitante")!=null) {
				item.setSolicitantSolicitud((String)map.get("v_solicitante"));
			}
			if(map.get("v_destinatario")!=null) {
				item.setDestinatarioSolicitud((String)map.get("v_destinatario"));
			}
			
			if(map.get("idfase")!=null) {
				item.setIdususoli(((BigDecimal)map.get("idfase")).longValue());
				//IdusuarioSolic
			}
			
			
			//Cguerra*/
			if(map.get("D_FEAPRO")!=null) {
				item.setFechaAprobacion((Date)map.get("D_FEAPRO"));
			}
			//cguerra
			if(map.get("d_fechcanc")!=null) {
				item.setFechacancelacion((Date)map.get("d_fechcanc"));
			}
			
			System.out.println("FECH_CANCE");
			System.out.println((Date)map.get("d_fechcanc"));
			
			
			
			
			
			if(map.get("D_FEPLAZAPRO")!=null) {
				item.setFechaPlazoAprob((Date)map.get("D_FEPLAZAPRO"));
			}
			item.setFechaAprobacionDocumento((Date)map.get("D_FEAPRODOCU"));
			item.setUsuarioAprobacionDocumento((String)map.get("V_USAPRODOCU"));
			item.setNumeroAnterior((map.get("N_NUMREVIANTE")==null)?null:((BigDecimal)map.get("N_NUMREVIANTE")).longValue());
			item.setFechaAprobacionAnterior((Date)map.get("D_FEAPROANTE"));
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
					
			if(map.get("V_RUTA_DOCUMT")!=null) {
				item.setRutaDocumt((String)map.get("V_RUTA_DOCUMT"));
			}
			
			if(map.get("V_RUTA_DOCU")!=null) {
				item.setRutaDocumentoOriginal((String)map.get("V_RUTA_DOCU"));
			}
			if(map.get("v_ruta_docu_c_obso")!=null) {
				item.setRutaDocumentoCopiaObso((String)map.get("v_ruta_docu_c_obso"));
			}
			if(map.get("v_ruta_docu_c_cont")!=null) {
				item.setRutaDocumentoCopiaCont((String)map.get("v_ruta_docu_c_cont"));
			}
			if(map.get("v_ruta_docu_c_no_cont")!=null) {
				item.setRutaDocumentoCopiaNoCont((String)map.get("v_ruta_docu_c_no_cont"));
			}
			if(map.get("v_ruta_docu_office")!=null) {
				item.setRutaDocumentoOffice((String)map.get("v_ruta_docu_office"));
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
				.withCatalogName("pck_agi_revision")
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

public Boolean eliminarProgramacion(Long idProgramacion, String iUsuario, Long idUsuario) {	
	Map<String, Object> out = null;	
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_PROG_ELIMINAR")
			.declareParameters(
					new SqlParameter("i_nidprog", OracleTypes.NUMBER),	
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
					SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidprog", idProgramacion)
					.addValue("i_vusuario", iUsuario)
					.addValue("i_idusuario", idUsuario);
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

@SuppressWarnings("unchecked")
public Revision crearRevision(Revision revision, String iUsuario, Long idUsuario) {
	List<Revision> lista	= new ArrayList<>();
	Map<String, Object> out = null;
	/*if(idRevision == null) {
		idRevision = 0L;
	}*/	
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("pck_agi_revision")
			.withProcedureName("prc_revision_guardar")
			.declareParameters(
					
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_idrevi", OracleTypes.NUMBER),
					new SqlParameter("i_numrevi", OracleTypes.NUMBER),
					new SqlParameter("i_numiter", OracleTypes.NUMBER),
					new SqlParameter("i_fecrevi", OracleTypes.DATE),
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_nomrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_codrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_rutrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_iddocu", OracleTypes.NUMBER),
					new SqlParameter("i_desrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_idmotirevi", OracleTypes.NUMBER),
					new SqlParameter("i_estrevi", OracleTypes.NUMBER),
					new SqlParameter("i_feplazapro", OracleTypes.DATE),
					new SqlParameter("i_ddocgoogledrive", OracleTypes.VARCHAR),
					
					new SqlParameter("i_feapro", OracleTypes.DATE),
					new SqlParameter("i_rechrevi", OracleTypes.VARCHAR),
					new SqlParameter("i_idcola", OracleTypes.NUMBER),
					
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR)
					);
					SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vusuario", iUsuario)
					.addValue("i_idrevi",  revision.getId())
					.addValue("i_numrevi", revision.getNumero())
					.addValue("i_numiter", revision.getIteracion())
					.addValue("i_fecrevi", revision.getFecha())
					.addValue("i_nomrevi", revision.getTitulo())
					.addValue("i_codrevi", revision.getCodigo())
					.addValue("i_rutrevi", revision.getRuta())
					.addValue("i_iddocu", revision.getDocumento().getId())
					.addValue("i_desrevi", revision.getDescripcion())
					.addValue("i_idmotirevi", revision.getIdmotirevi())
					.addValue("i_feplazapro", revision.getFecPlazoAprobacion())
					.addValue("i_ddocgoogledrive", revision.getIdDocGoogleDrive())
					.addValue("i_feapro", revision.getFechaAprobacion())
					.addValue("i_rechrevi", revision.getMotivoRechazoRev())
					.addValue("i_estrevi", revision.getEstado().getIdconstante())
					.addValue("i_idcola", idUsuario);
			
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
@Transactional
public List<Revision> finalizarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc, String iUsuario, Long idUsuario) {
	List<Revision> listaProgDocu = null;
	List<Revision> listaDistSelect = null;
	List<Revision> listaDistNoSelect = null;
	Long estado;
			
	for (Revision listaRevisionSelecc : revisionSelecc) {
		listaRevisionSelecc.setEstados(1L);
		listaRevisionSelecc.setEstEjec("157");
		listaDistSelect = finalizarDistribuciones(codFichaLogueado, listaRevisionSelecc, iUsuario, idUsuario);
	}
	
	for (Revision listaRevisionNoSelecc : revisionNoSelecc) {
		listaRevisionNoSelecc.setIdResponsableEquipoSelecc(null);
		listaRevisionNoSelecc.setEstados(0L);
		listaRevisionNoSelecc.setEstEjec("158");
		listaDistNoSelect = finalizarDistribuciones(codFichaLogueado, listaRevisionNoSelecc, iUsuario, idUsuario);
	}	
	
	listaProgDocu = consultarProgDistri(revisionSelecc.get(0).getIdProgExistente(), revisionSelecc.get(0).getIdResponsableEquipo());
	
	return listaProgDocu;
}

@Override
@Transactional
public List<Revision> grabarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc, String iUsuario, Long idUsuario) {
	List<Revision> listaProgDocu = null;
	Revision listaDistSelect = null;
	Revision listaDistNoSelect = null;
			
	for (Revision listaRevisionSelecc : revisionSelecc) {		
		listaDistSelect = grabarDistribuciones(codFichaLogueado, listaRevisionSelecc, iUsuario, idUsuario);
	}
	
	for (Revision listaRevisionNoSelecc : revisionNoSelecc) {
		listaRevisionNoSelecc.setIdResponsableEquipoSelecc(null);
		listaDistNoSelect = grabarDistribuciones(codFichaLogueado, listaRevisionNoSelecc, iUsuario, idUsuario);
	}
	
	return listaProgDocu;
}

@SuppressWarnings("unchecked")
public Revision grabarDistribuciones(String codFichaLogueado, Revision listaRevisionSelecc, String iUsuario, Long idUsuario) {
	List<Revision> lista = new ArrayList<>();
	Map<String, Object> out = null;
	boolean registro = false;
	
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_GRABAR_DIST")
			.declareParameters(new SqlParameter("i_idprogramacion", OracleTypes.VARCHAR),
					new SqlParameter("i_iddocu", OracleTypes.NUMBER),
					new SqlParameter("i_idtrim", OracleTypes.NUMBER),
					new SqlParameter("i_idusuresp", OracleTypes.NUMBER),
					new SqlParameter("i_niddDocu", OracleTypes.NUMBER),
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_idusuario", OracleTypes.NUMBER),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
	SqlParameterSource in = new MapSqlParameterSource()
			.addValue("i_idprogramacion", listaRevisionSelecc.getIdProgExistente())
			.addValue("i_idtrim", listaRevisionSelecc.getIdTrimestre())
			.addValue("i_idusuresp", listaRevisionSelecc.getIdResponsableEquipoSelecc())
			.addValue("i_niddDocu", listaRevisionSelecc.getIdDocu()).addValue("i_vusuario", iUsuario);
	try {
		out = this.jdbcCall.execute(in);
		Integer resultado = (Integer) out.get("o_retorno");
		if (resultado == 0) {
			registro = true;
		} else {
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");			
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
	} catch (Exception e) {
		Integer resultado = 1;
		String mensaje = "Error en RevisionDAOImpl.grabarDistribucion";
		String mensajeInterno = e.getMessage();
		this.error = new Error(resultado, mensaje, mensajeInterno);
	}
	return null;
}

@Override
@Transactional
public List<Revision> grabarPrograma(String idProg, String codFichaLogueado, String idEstProg, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc, String iUsuario, Long idUsuario) {
	Revision listaProg = null;
	Revision listaProgDocu = null;
	Revision listaProgDocuMod = null;
	RelacionCoordinadorDAOImpl relacionCoordinadorDAO = new RelacionCoordinadorDAOImpl();
	Long idProgramacion = null;
	Long nidProg = null;
	Long nidJefe = null;
	List<Revision> listaProgEqui = new ArrayList<>();
	
	if (idProg != null) {
		nidProg = Long.parseLong(idProg);
	}
		
	
	if (idProg == null) {
		listaProg = registrarProgramacion(nidProg, revisionSelecc.get(0), idEstProg, iUsuario, idUsuario);
		idProgramacion = listaProg.getIdProgramacion();
	}else {
		listaProg = registrarProgramacion(nidProg, revisionSelecc.get(0), idEstProg, iUsuario, idUsuario);
		idProgramacion = listaProg.getIdProgramacion();
	}
		
	for (Revision revisionProg : revisionSelecc) {
		revisionProg.setIdProgramacion(listaProg.getIdProgramacion());
		
		if (idEstProg.equals("151")) {
			relacionCoordinadorDAO.setJdbc(this.jdbc);
			nidJefe = relacionCoordinadorDAO.obtenerDatosJefeEquipo(revisionProg.getIdResponsableEquipo());	
		}else {
			nidJefe = null;
		}		
		
		listaProgDocu = grabarProgramacionDocu(nidJefe, codFichaLogueado, idEstProg, revisionProg, iUsuario, idUsuario);
	}

	for (Revision revisionProgMod : revisionNoSelecc) {
		revisionProgMod.setIdProgramacion(listaProg.getIdProgramacion());
		listaProgDocuMod = modProgramacionDocu(revisionProgMod, iUsuario, idUsuario);
	}
	
	listaProgEqui = consultarProgEquipo(idProgramacion);

	return listaProgEqui;
}




@SuppressWarnings("unchecked")
public Revision registrarProgramacion(Long nidProg, Revision revision, String idEstProg, String iUsuario, Long idUsuario) {
	List<Revision> lista	= new ArrayList<>();
	Map<String, Object> out = null;	
	Long idProgramacion = null;
	Revision revisionRegistro = null;
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_GRABAR_PROGRAMA")
			.declareParameters(
					new SqlParameter("i_idestprog", OracleTypes.VARCHAR),
					new SqlParameter("i_idtipdoc", OracleTypes.NUMBER),
					new SqlParameter("i_idgerenparametro", OracleTypes.NUMBER),
					new SqlParameter("i_idresponsableequipo", OracleTypes.NUMBER),
					new SqlParameter("i_antiguedaddocu", OracleTypes.NUMBER),
					new SqlParameter("i_idprogexistente", OracleTypes.NUMBER),
					//new SqlParameter("i_idequiporesp", OracleTypes.NUMBER),
					//new SqlParameter("i_iddocu", OracleTypes.NUMBER),
					
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_idusuario", OracleTypes.NUMBER),
					
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
					new SqlOutParameter("o_idprog", OracleTypes.NUMBER)
					);
			
					SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_idestprog", idEstProg)	
						.addValue("i_idtipdoc",  revision.getIdTipDoc())
						.addValue("i_idgerenparametro",  revision.getGerenparametroid())
						.addValue("i_idresponsableequipo",  revision.getIdResponsableEquipo())
						.addValue("i_antiguedaddocu",  revision.getAntiguedadDocu())
						//.addValue("i_idprogexistente",  revision.getIdProgExistente())
						.addValue("i_idprogexistente",  nidProg)						
						//.addValue("i_iddocu",  revision.getIdDocu())
						.addValue("i_vusuario", iUsuario)
						.addValue("i_idusuario", idUsuario);
			
				try {
					out=this.jdbcCall.execute(in);
					Integer resultado = (Integer)out.get("o_retorno");
					if(resultado == 0) {
						idProgramacion = ((BigDecimal)out.get("o_idprog")).longValue();
						revision.setIdProgramacion(idProgramacion);
						revisionRegistro = revision;
						 //List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
					     //lista = this.mapearPrograma(out);
					} else {
						String mensaje = (String)out.get("o_mensaje");
						String mensajeInterno = (String)out.get("o_sqlerrm");
						this.error = new Error(resultado,mensaje,mensajeInterno);
						//lista.clear();
					}			
				}
				catch (Exception e) {
					Integer resultado = 1;//(Integer)out.get("o_retorno");
					String mensaje = "Error en RevisionDAOImpl.grabarPrograma";
					String mensajeInterno = e.getMessage();
					this.error = new Error(resultado,mensaje,mensajeInterno);
					//lista.clear();
				}
			
	return revisionRegistro;//(Revision)lista;//lista.get(0);
			//}
}

@SuppressWarnings("unchecked")
public Revision modProgramacionDocu(Revision revisionProgMod, String iUsuario, Long idUsuario) {
	List<Revision> lista	= new ArrayList<>();
	Map<String, Object> out = null;	
	boolean registro = false;
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_ACTU_PROGDOCU")
			.declareParameters(					
					new SqlParameter("i_idprogramacion", OracleTypes.VARCHAR),
					new SqlParameter("i_iddocu", OracleTypes.NUMBER),
					new SqlParameter("i_idprogexistente", OracleTypes.NUMBER),
					
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_idusuario", OracleTypes.NUMBER),
					
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR)
					);
			
					SqlParameterSource in = new MapSqlParameterSource()						
						.addValue("i_idprogramacion",  revisionProgMod.getIdProgramacion())
						.addValue("i_iddocu",  revisionProgMod.getIdDocu())
						.addValue("i_idprogexistente",  revisionProgMod.getIdProgExistente())
						.addValue("i_vusuario", iUsuario)
						.addValue("i_idusuario", idUsuario);			
				try {
					out=this.jdbcCall.execute(in);
					Integer resultado = (Integer)out.get("o_retorno");
					if(resultado == 0) {
						registro = true;
					} else {
						String mensaje = (String)out.get("o_mensaje");
						String mensajeInterno = (String)out.get("o_sqlerrm");						
						this.error = new Error(resultado,mensaje,mensajeInterno);						
					}			
				}
				catch (Exception e) {
					Integer resultado = 1;
					String mensaje = "Error en RevisionDAOImpl.modProgramacionDocu";
					String mensajeInterno = e.getMessage();
					this.error = new Error(resultado,mensaje,mensajeInterno);					
				}			
	return null;			
}





@SuppressWarnings("unchecked")
public List<Revision> finalizarDistribuciones(String codFichaLogueado, Revision revisionDistribuir, String iUsuario,
		Long idUsuario) {
	List<Revision> lista = new ArrayList<>();
	Map<String, Object> out = null;
	boolean registro = false;
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_FIN_DIST")
			.declareParameters(
					new SqlParameter("i_idprogramacion", OracleTypes.VARCHAR),
					new SqlParameter("i_idtrim", OracleTypes.NUMBER),
					new SqlParameter("i_ndisprgdoc", OracleTypes.NUMBER),
					new SqlParameter("i_vestejec", OracleTypes.VARCHAR),
					new SqlParameter("i_idusuresp", OracleTypes.NUMBER),
					new SqlParameter("i_niddocu", OracleTypes.NUMBER),
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_idusuario", OracleTypes.NUMBER),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", OracleTypes.CURSOR));

	SqlParameterSource in = new MapSqlParameterSource()
			.addValue("i_idprogramacion", revisionDistribuir.getIdProgExistente())
			.addValue("i_idtrim", revisionDistribuir.getIdTrimestre())
			.addValue("i_ndisprgdoc", revisionDistribuir.getEstados())
			.addValue("i_vestejec", revisionDistribuir.getEstEjec())
			.addValue("i_idusuresp", revisionDistribuir.getIdResponsableEquipoSelecc())
			.addValue("i_niddocu", revisionDistribuir.getIdDocu())
			.addValue("i_vusuario", iUsuario);
	try {
		out = this.jdbcCall.execute(in);
		Integer resultado = (Integer) out.get("o_retorno");
		if (resultado == 0) {
			registro = true;
		} else {
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");			
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
	} catch (Exception e) {
		Integer resultado = 1;
		String mensaje = "Error en RevisionDAOImpl.grabarDistribucion";
		String mensajeInterno = e.getMessage();
		this.error = new Error(resultado, mensaje, mensajeInterno);
	}
	return null;
}

@SuppressWarnings("unchecked")
	public Revision grabarProgramacionDocu(Long idJefe, String codFichaLogueado, String idEstProg, Revision revision, String iUsuario, Long idUsuario) {
		List<Revision> lista = new ArrayList<>();
		Map<String, Object> out = null;
		boolean registro = false;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_REVISION")
				.withProcedureName("PRC_REVISION_GRABAR_PROGDOCU")
				.declareParameters(
						new SqlParameter("i_idestprog", OracleTypes.VARCHAR),
						new SqlParameter("i_idrevi", OracleTypes.NUMBER),
						new SqlParameter("i_idficha", OracleTypes.NUMBER),
						new SqlParameter("i_idrespequipo", OracleTypes.NUMBER),
						new SqlParameter("i_idprogramacion", OracleTypes.VARCHAR),
						new SqlParameter("i_iddocu", OracleTypes.NUMBER),
						new SqlParameter("i_idprogexistente", OracleTypes.NUMBER),
						new SqlParameter("i_nidgeregnrl", OracleTypes.NUMBER),
						new SqlParameter("i_nidtipodocu", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlParameter("i_idusuario", OracleTypes.NUMBER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_idestprog", idEstProg)
				.addValue("i_idrevi", revision.getNidrevi())
				.addValue("i_idficha", revision.getIdResponsableEquipo())
				.addValue("i_idrespequipo", idJefe)
				.addValue("i_idprogramacion", revision.getIdProgramacion())
				.addValue("i_iddocu", revision.getIdDocu())
				.addValue("i_idprogexistente", revision.getIdProgExistente())
				.addValue("i_nidgeregnrl", revision.getGerenparametroid())
				.addValue("i_nidtipodocu", revision.getIdTipDoc())
				.addValue("i_vusuario", iUsuario)
				.addValue("i_idusuario", idUsuario);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en RevisionDAOImpl.grabarProgramacionDocu";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return null;
	}


/*@SuppressWarnings("unchecked")
private List<Revision> mapearPrograma(Map<String, Object> resultados) {
	List<Revision> listaDocumento 	= new ArrayList<>();		
	List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
	Revision item					= null;
	int size = lista.size();
	
	for(Map<String, Object> map : lista) {			
		item = new Revision();
		if (map.get("V_CODDOCU")!=null) {
			item.setCodDocu(((String)map.get("V_CODDOCU")));
		}
		if (map.get("V_DESDOCU")!=null) {
			item.setDesDocu(((String)map.get("V_DESDOCU")));
		}
		if (map.get("N_NUMREVI")!=null) {
			item.setNumRevi(((BigDecimal)map.get("N_NUMREVI")).longValue());
		}
		if (map.get("N_IDPROG")!=null) {
			item.setIdProgramacion(((BigDecimal)map.get("N_IDPROG")).longValue());
		}
		if (map.get("D_FECREVI")!=null) {
			item.setFecRevi((Date)map.get("D_FECREVI"));
		}
		if (map.get("ANTIGUEDADDOCU")!=null) {
			item.setAntiguedadDocu(((BigDecimal)map.get("ANTIGUEDADDOCU")).longValue());
		}
		if (map.get("N_ROBLI")!=null) {
			item.setPeriodoOblig(((BigDecimal)map.get("N_ROBLI")).longValue());
		}
		if (map.get("V_NOEQUIEMIS")!=null) {
			item.setResponsableEquipo(((String)map.get("V_NOEQUIEMIS")));
		}
		if (map.get("N_IDEQUIEMIS")!=null) {
			item.setIdResponsableEquipo(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
		}
		if (map.get("N_IDDOCU")!=null) {
			item.setIdDocu(((BigDecimal)map.get("N_IDDOCU")).longValue());
		}
		
		listaDocumento.add(item);

		if (this.paginacion != null && size>1) {
			this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
		}
	}
	return listaDocumento;
}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest) {
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
						.withCatalogName("PCK_AGI_TAREAS")
						.withProcedureName("PRC_LISTA_APROBAR")
						.declareParameters(
								new SqlParameter("i_nid", 		OracleTypes.NUMBER),
								//new SqlParameter("i_nnumero", 	OracleTypes.NUMBER),
								//new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
								new SqlParameter("i_vdocumento",OracleTypes.VARCHAR),
								new SqlParameter("i_vtitulo",OracleTypes.VARCHAR),
								//new SqlParameter("i_nestrevi",OracleTypes.VARCHAR),
								//new SqlParameter("i_dfecrevi",OracleTypes.DATE),
								//new SqlParameter("i_colaborador",OracleTypes.VARCHAR),
								new SqlParameter("i_vnombres",OracleTypes.VARCHAR),
								new SqlParameter("i_vapepaterno",OracleTypes.VARCHAR),
								new SqlParameter("i_vapematerno",OracleTypes.VARCHAR),
								new SqlParameter("i_nidcoord", OracleTypes.NUMBER),
								new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
								new SqlParameter("i_nregistros",OracleTypes.NUMBER),
								new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
								new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
								new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
								new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));
		/*try {
			if(revisionRequest.getFechaRevision() != null) {
				fechaRevision = formatter.parse(revisionRequest.getFechaRevision());
			}

		} catch (ParseException e1) {
			fechaRevision = null;
		}*/
		//System.out.println("id documento dd "+ revisionRequest.getIdDocumento());
		in = new MapSqlParameterSource()
				.addValue("i_nid",			revisionRequest.getId())
				//.addValue("i_nnumero",		revisionRequest.getNumero())
				//.addValue("i_ndocumento",	revisionRequest.getIdDocumento())
				.addValue("i_vdocumento",	revisionRequest.getCodigoDoc())
				.addValue("i_vtitulo",	revisionRequest.getTituloDoc())
				//.addValue("i_nestrevi",	revisionRequest.getEstado())
				//.addValue("i_dfecrevi",	fechaRevision)
				.addValue("i_nidcoord", 	revisionRequest.getIdColaborador())
				//.addValue("i_colaborador",	revisionRequest.getNombreCompleto())
				.addValue("i_vnombres",	revisionRequest.getNombres())
				.addValue("i_vapepaterno",	revisionRequest.getApellidoPaterno())
				.addValue("i_vapematerno",	revisionRequest.getApellidoMaterno())
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
public Revision crearDocumentoGoogleDrive(String idDocGoogle, Long idRevi) {
	List<Revision> lista	= new ArrayList<>();
	Map<String, Object> out = null;
	/*if(idRevision == null) {
		idRevision = 0L;
	}*/	
	this.jdbcCall = new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
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
	@Override
	public Revision rechazarDocumento(Long idDocumento, Revision revisionRechazo) {
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_TAREAS")
				.withProcedureName("PRC_SOLI_REVI_RECHAZAR")
				.declareParameters(
						new SqlParameter("i_n_idrevi", OracleTypes.NUMBER),
						new SqlParameter("i_n_iddocu", OracleTypes.NUMBER),
						new SqlParameter("i_v_rechrevi", OracleTypes.VARCHAR),
						//new SqlParameter("i_n_estrevi", OracleTypes.VARCHAR),
						new SqlParameter("i_v_usurech", OracleTypes.VARCHAR),
						//new SqlParameter("i_n_disrevi", OracleTypes.VARCHAR),
						new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idrevi", revisionRechazo.getId())
				.addValue("i_n_iddocu", idDocumento)
				.addValue("i_v_rechrevi", revisionRechazo.getMotivoRechazoRev())
				//.addValue("i_n_estrevi", revisionRechazo.getIdEsta())
				.addValue("i_v_usurech", "AGI")
				//.addValue("i_n_disrevi", revisionRechazo.getDisponible())
				.addValue("i_a_v_usumod", "AGI");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado != 0) {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return revisionRechazo;
	}
	@Override
	public boolean registrarRutasDocumentoRevision(Revision revision,String iUsuario) {		
		Map<String, Object> out = null;
		this.error = null;
		boolean respuesta = false;
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_REVISION")
						.withProcedureName("PRC_REVISION_RUTAS_DOCUMENTOS")
						.declareParameters(
							new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
							new SqlParameter("i_nidrevi",		OracleTypes.NUMBER),
							new SqlParameter("i_v_ruta_docu",		OracleTypes.VARCHAR),
							new SqlParameter("i_v_ruta_docu_office",		OracleTypes.VARCHAR),
							new SqlParameter("i_v_ruta_docu_c_no_cont",		OracleTypes.VARCHAR),
							new SqlParameter("i_v_ruta_docu_c_cont",		OracleTypes.VARCHAR),
							new SqlParameter("i_v_ruta_docu_c_obso",		OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
					
					SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_vusuario",iUsuario)
						.addValue("i_nidrevi",revision.getId())
						.addValue("i_v_ruta_docu",	revision.getRutaDocumentoOriginal())
						.addValue("i_v_ruta_docu_office",	revision.getRutaDocumentoGoogle())
						.addValue("i_v_ruta_docu_c_no_cont",	revision.getRutaDocumentoCopiaNoCont())
						.addValue("i_v_ruta_docu_c_cont",	revision.getRutaDocumentoCopiaCont())
						.addValue("i_v_ruta_docu_c_obso",	revision.getRutaDocumentoCopiaObso());
					
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {
				respuesta = true;				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);				
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.registrarRutasDocumentoRevision";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		
		return respuesta;
	}
	
	@Override
	public List<Revision> consultarDetDistribuciones(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Revision> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_DETDOCDISTRIB")
			.declareParameters(
				new SqlParameter("i_niddocu", 			OracleTypes.NUMBER),
				
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_niddocu",						revisionRequest.getIdDocumento())			
			.addValue("i_npagina",						pageRequest.getPagina())
			.addValue("i_nregistros",					pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearDetDistrib(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.consultarProgramaciones";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Revision> mapearDetDistrib(Map<String, Object> resultados) {
		List<Revision> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Revision item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Revision();
			
            if (map.get("correlativo")!=null) {
				item.setCorrelativo(((BigDecimal)map.get("correlativo")).longValue());
			}
			if (map.get("v_coddocu")!=null) {
				item.setCodDocu((String)map.get("v_coddocu"));
			}
			if (map.get("v_desdocu")!=null) {
				item.setDesDocu((String)map.get("v_desdocu"));
			}
//			if (map.get("n_robli")!=null) { --
//				item.setNumRevi(((BigDecimal)map.get("n_robli")).longValue());
//			}
			if (map.get("n_numrevi")!=null) {
				item.setNumRevi(((BigDecimal)map.get("n_numrevi")).longValue());
			}
			if (map.get("d_fecrevi")!=null) {
				item.setFecRevi((Date)map.get("d_fecrevi"));
			}			
			if (map.get("n_idequiemis")!=null) {
				item.setIdResponsableEquipo(((BigDecimal)map.get("n_idequiemis")).longValue());
			}
			if (map.get("v_noequiemis")!=null) {
				item.setResponsableEquipo(((String)map.get("v_noequiemis")));
			}			
			if (map.get("n_periobli")!=null) {
				item.setPeriodoOblig(((BigDecimal)map.get("n_periobli")).longValue());
			}
			if (map.get("n_idtrim")!=null) {
				item.setIdTrimestre(((BigDecimal)map.get("n_idtrim")).longValue());
			}
			
			listaDocumento.add(item);

			if (this.paginacion != null ) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaDocumento;
	}
	
	@Override
	public List<Revision> consultarProgramaciones(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Revision> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_PROGRAMACIONES")
			.declareParameters(
				//new SqlParameter("i_nidprog", 		OracleTypes.NUMBER),
				
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			//.addValue("i_nidprog",						revisionRequest.getIdProg())
			
			.addValue("i_npagina",						pageRequest.getPagina())
			.addValue("i_nregistros",					pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearListaProgramaciones(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.consultarProgramaciones";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Revision> mapearListaProgramaciones(Map<String, Object> resultados) {
		List<Revision> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Revision item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Revision();
			
			/*if (map.get("totalEquipos")!=null) {
				item.setTotalEquipos(((BigDecimal)map.get("totalEquipos")).longValue());
			}*/
			
			if (map.get("fechaActual")!=null) {
				item.setFechaActual((Date)map.get("fechaActual"));
			}
            if (map.get("n_idprog")!=null) {
				item.setIdProg(((BigDecimal)map.get("n_idprog")).longValue());
			}
			if (map.get("n_iddocu")!=null) {
				item.setIdDocu(((BigDecimal)map.get("n_iddocu")).longValue());
			}
			if (map.get("n_idequiemis")!=null) {
				item.setIdResponsableEquipo(((BigDecimal)map.get("n_idequiemis")).longValue());
			}
			if (map.get("v_noequiemis")!=null) {
				item.setResponsableEquipo(((String)map.get("v_noequiemis")));
			}
			if (map.get("primertrim")!=null) {
				item.setPrimerTrim(((BigDecimal)map.get("primertrim")).longValue());
			}
			if (map.get("segundotrim")!=null) {
				item.setSegundoTrim(((BigDecimal)map.get("segundotrim")).longValue());
			}
			if (map.get("tercertrim")!=null) {
				item.setTercerTrim(((BigDecimal)map.get("tercertrim")).longValue());
			}
			if (map.get("cuartotrim")!=null) {
				item.setCuartoTrim(((BigDecimal)map.get("cuartotrim")).longValue());
			}
			
			listaDocumento.add(item);

			if (this.paginacion != null ) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaDocumento;
	}
	
	@Override
	public List<Revision> consultarProgramacionPorIdProg(RevisionRequest revisionRequest) {
		Map<String, Object> out	= null;
		List<Revision> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_PROGRAMACIONEDIT")
			.declareParameters(
				new SqlParameter("i_nidprog", 		OracleTypes.NUMBER),
				new SqlParameter("i_ndusuresp", 		OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nidprog",						revisionRequest.getIdProg())
			.addValue("i_ndusuresp",					revisionRequest.getIdusuresp());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearDocumentoEditado(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerDocumento";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@Override
	public List<Jefes> obtenerJefes(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Jefes> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_LIST_JEFES")
			.declareParameters(
				new SqlParameter("nficha", 				OracleTypes.NUMBER),
				new SqlParameter("narea", 				OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
			
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("nficha",							revisionRequest.getFichaTrabajador())
			.addValue("narea",							revisionRequest.getAreaTrabajador())
			.addValue("i_npagina",						pageRequest.getPagina())
			.addValue("i_nregistros",					pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearJefes(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerJefes";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Jefes> mapearJefes(Map<String, Object> resultados) {
		List<Jefes> listaJefes 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Jefes item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Jefes();

			if (map.get("codTrabajador")!=null) {
				item.setCodTrabajador(((BigDecimal)map.get("codTrabajador")).longValue());
			}
			if (map.get("fichaTrabajador")!=null) {
				item.setFichaTrabajador(((BigDecimal)map.get("fichaTrabajador")).longValue());
			}			
			if (map.get("nombTrabajador")!=null) {
				item.setNombTrabajador((String)map.get("nombTrabajador"));
			}
			if (map.get("apellPaternoTrabajador")!=null) {
				item.setApellPaternoTrabajador((String)map.get("apellPaternoTrabajador"));
			}
			if (map.get("apellMaternoTrabajador")!=null) {
				item.setApellMaternoTrabajador((String)map.get("apellMaternoTrabajador"));
			}
			if (map.get("areaTrabajador")!=null) {
				item.setAreaTrabajador(((BigDecimal)map.get("areaTrabajador")).longValue());
			}
			
			
			if (map.get("codJefe")!=null) {
				item.setCodJefe(((BigDecimal)map.get("codJefe")).longValue());
			}
			if (map.get("fichaJefe")!=null) {
				item.setFichaJefe(((BigDecimal)map.get("fichaJefe")).longValue());
			}			
			if (map.get("nombJefe")!=null) {
				item.setNombJefe((String)map.get("nombJefe"));
			}
			if (map.get("apellPaternoJefe")!=null) {
				item.setApellPaternoJefe((String)map.get("apellPaternoJefe"));
			}
			if (map.get("apellMaternoJefe")!=null) {
				item.setApellMaternoJefe((String)map.get("apellMaternoJefe"));
			}
			if (map.get("areaJefe")!=null) {
				item.setAreaJefe(((BigDecimal)map.get("areaJefe")).longValue());
			}
			
			listaJefes.add(item);

			if (this.paginacion != null ) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaJefes;
	}
	
	@SuppressWarnings("unchecked")
	private List<Revision> mapearDocumentoEditado(Map<String, Object> resultados) {
		List<Revision> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Revision item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Revision();
			
			if (map.get("V_RUTEJEC")!=null) {
				item.setRutaDocumt((String)map.get("V_RUTEJEC"));
			}
			if (map.get("PRIMERTRIM")!=null) {
				item.setPrimerTrim(((BigDecimal)map.get("PRIMERTRIM")).longValue());
			}
			if (map.get("SEGUNDOTRIM")!=null) {
				item.setSegundoTrim(((BigDecimal)map.get("SEGUNDOTRIM")).longValue());
			}
			if (map.get("TERCERTRIM")!=null) {
				item.setTercerTrim(((BigDecimal)map.get("TERCERTRIM")).longValue());
			}
			if (map.get("CUARTOTRIM")!=null) {
				item.setCuartoTrim(((BigDecimal)map.get("CUARTOTRIM")).longValue());
			}
			if (map.get("CORRELATIVO")!=null) {
				item.setCorrelativo(((BigDecimal)map.get("CORRELATIVO")).longValue());
			}			
			if (map.get("V_CODDOCU")!=null) {
				item.setCodDocu(((String)map.get("V_CODDOCU")));
			}
			if (map.get("V_DESDOCU")!=null) {
				item.setDesDocu(((String)map.get("V_DESDOCU")));
			}
			if (map.get("N_NUMREVI")!=null) {
				item.setNumRevi(((BigDecimal)map.get("N_NUMREVI")).longValue());
			}
			if (map.get("v_destipo")!=null) {
				item.setTipodocumento(((String)map.get("v_destipo")));
			}
			if (map.get("D_FECREVI")!=null) {
				item.setFecRevi((Date)map.get("D_FECREVI"));
			}
			if (map.get("d_fecejec")!=null) {
				item.setFechaEjecucion((Date)map.get("d_fecejec"));
			}
			if (map.get("ANTIGUEDADDOCU")!=null) {
				item.setAntiguedadDocu(((BigDecimal)map.get("ANTIGUEDADDOCU")).longValue());
			}
			if (map.get("N_ROBLI")!=null) {
				item.setPeriodoOblig(((BigDecimal)map.get("N_ROBLI")).longValue());
			}
			if (map.get("V_NOEQUIEMIS")!=null) {
				item.setResponsableEquipo(((String)map.get("V_NOEQUIEMIS")));
			}
			if (map.get("N_IDEQUIEMIS")!=null) {
				item.setIdResponsableEquipo(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
			}
			if (map.get("N_IDDOCU")!=null) {
				item.setIdDocu(((BigDecimal)map.get("N_IDDOCU")).longValue());
			}
			if (map.get("N_IDPROG")!=null) {
				item.setIdProgExistente(((BigDecimal)map.get("N_IDPROG")).longValue());
			}
			if (map.get("N_IDTRIM")!=null) {
				item.setIdTrimestre(((BigDecimal)map.get("N_IDTRIM")).longValue());
			}
			if (map.get("DESCESTADOEJEC")!=null) {
				item.setEstEjec((String)map.get("DESCESTADOEJEC"));
			}
			
			if (map.get("NOMUSUDIST")!=null) {
				item.setResponsableEquipoSelecc((String)map.get("NOMUSUDIST"));
			}
			
			if (map.get("N_USUDIST")!=null) {
				item.setIdResponsableEquipoSelecc(((BigDecimal)map.get("N_USUDIST")).longValue());
			}
			
			if (map.get("n_idjerapadr")!=null) {
				item.setGerenparametroid(((BigDecimal)map.get("n_idjerapadr")).longValue());
			}
			
			if (map.get("v_rutjera")!=null) {
				item.setRuta((String)map.get("v_rutjera"));
			}
			
			listaDocumento.add(item);
		}
		return listaDocumento;
	}
	
	@Override
	public List<Revision> consultarProgramacion(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Revision> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_PROGRAMACION")
			.declareParameters(
				new SqlParameter("i_vidtipodocu", 		OracleTypes.VARCHAR),						
				new SqlParameter("i_vidgeregnrl", 		OracleTypes.VARCHAR),
				new SqlParameter("i_nanioantiguedad", 	OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_vidtipodocu",					revisionRequest.getTipodoc())
			.addValue("i_vidgeregnrl",					revisionRequest.getGerenId())
			.addValue("i_nanioantiguedad",				revisionRequest.getAnioantiguedad())
			.addValue("i_npagina",						pageRequest.getPagina())
			.addValue("i_nregistros",					pageRequest.getRegistros());
			//.addValue("i_vorden",					null);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearDocumento(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerDocumento";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	private List<Revision> mapearDocumento(Map<String, Object> resultados) {
		List<Revision> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Revision item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Revision();
			if (map.get("V_CODDOCU")!=null) {
				item.setCodDocu(((String)map.get("V_CODDOCU")));
			}
			if (map.get("v_destipo")!=null) {
				item.setTipodocumento(((String)map.get("v_destipo")));
			}
			if (map.get("n_idgeregnrl")!=null) {
				item.setGerenparametroid(((BigDecimal)map.get("n_idgeregnrl")).longValue());
			}
			if (map.get("n_idtipodocu")!=null) {
				item.setIdTipDoc(((BigDecimal)map.get("n_idtipodocu")).longValue());
			}
			if (map.get("V_DESDOCU")!=null) {
				item.setDesDocu(((String)map.get("V_DESDOCU")));
			}
			if (map.get("N_NUMREVI")!=null) {
				item.setNumRevi(((BigDecimal)map.get("N_NUMREVI")).longValue());
			}
			if (map.get("D_FECREVI")!=null) {
				item.setFecRevi((Date)map.get("D_FECREVI"));
			}
			if (map.get("ANTIGUEDADDOCU")!=null) {
				item.setAntiguedadDocu(((BigDecimal)map.get("ANTIGUEDADDOCU")).longValue());
			}
			if (map.get("N_ROBLI")!=null) {
				item.setPeriodoOblig(((BigDecimal)map.get("N_ROBLI")).longValue());
			}
			if (map.get("V_NOEQUIEMIS")!=null) {
				item.setResponsableEquipo(((String)map.get("V_NOEQUIEMIS")));
			}
			if (map.get("N_IDEQUIEMIS")!=null) {
				item.setIdResponsableEquipo(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
			}
			if (map.get("N_IDDOCU")!=null) {
				item.setIdDocu(((BigDecimal)map.get("N_IDDOCU")).longValue());
			}
			if (map.get("n_idrevi")!=null) {
				item.setNidrevi(((BigDecimal)map.get("n_idrevi")).longValue());
			}
			listaDocumento.add(item);

			if (this.paginacion != null ) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaDocumento;
	}

	@Override
	public List<RevisionRequest> consultarListaDistribucionEjec(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<RevisionRequest> lista	= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_DISTRIB_EJEC")
			.declareParameters(
				new SqlParameter("i_nroficha",			OracleTypes.NUMBER),
				new SqlParameter("i_codarea",			OracleTypes.NUMBER),
				new SqlParameter("i_nidprog",			OracleTypes.NUMBER),
				new SqlParameter("i_anio",				OracleTypes.NUMBER),
				new SqlParameter("i_equipo",			OracleTypes.VARCHAR),
				new SqlParameter("i_vestprog",			OracleTypes.NUMBER),
				new SqlParameter("i_vestejec",			OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),										
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()	
			.addValue("i_nroficha",						revisionRequest.getNroFicha())
			.addValue("i_codarea",						revisionRequest.getCodArea())
			.addValue("i_nidprog",						revisionRequest.getIdProg())
			.addValue("i_anio",							revisionRequest.getAnioProg())
			.addValue("i_equipo",						revisionRequest.getEquipoProg())
			.addValue("i_vestprog",						revisionRequest.getIdEstProg())
			.addValue("i_vestejec",						revisionRequest.getIdEstEjecProg())
			
			.addValue("i_npagina",						pageRequest.getPagina())
			.addValue("i_nregistros",					pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearDistribEjec(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.consultarListaDistribucionEjec";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<RevisionRequest> mapearDistribEjec(Map<String, Object> resultados) {
		List<RevisionRequest> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		RevisionRequest item			= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new RevisionRequest();
			            
			if (map.get("N_IDPROG")!=null) {
				item.setIdProg(((BigDecimal)map.get("N_IDPROG")).longValue());
			}
			if (map.get("D_FECDIST")!=null) {
				item.setFechDistribucion((Date)map.get("D_FECDIST"));	
			}
			if (map.get("V_ESTPROG")!=null) {
				item.setIdEstadoProg(((String)map.get("V_ESTPROG")));
			}
			/*if (map.get("N_IDEQUI")!=null) {
				item.setNombreEquipo(((String)map.get("N_IDEQUI")));
			}
			if (map.get("V_NOMEQUI")!=null) {
				item.setNombreEquipo(((String)map.get("V_NOMEQUI")));
			}*/
			
			if (map.get("v_noequiemis")!=null) {
				item.setNombreEquipo(((String)map.get("v_noequiemis")));
			}
			if (map.get("cantDocRevisar")!=null) {
				item.setCantDocRevisar(((BigDecimal)map.get("cantDocRevisar")).longValue());
			}
			if (map.get("DESCESTADO")!=null) {
				item.setEstProg((String)map.get("DESCESTADO"));
			}
			
			listaDocumento.add(item);

			if (this.paginacion != null ) {
				if (map.get("RESULT_COUNT")!=null) {
					this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
				}				
			}
		}
		return listaDocumento;
	}
	
	@Override
	public List<RevisionRequest> consultarListaProgramada(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<RevisionRequest> lista	= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_LSTPROGRAMADA")
			.declareParameters(
				new SqlParameter("i_nidprog",			OracleTypes.NUMBER),
				new SqlParameter("i_anio",				OracleTypes.VARCHAR),
				new SqlParameter("i_estprog",			OracleTypes.VARCHAR),
				new SqlParameter("i_nusuprog",			OracleTypes.NUMBER),				
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),										
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nidprog",						revisionRequest.getIdProg())
			.addValue("i_anio",							revisionRequest.getFecCreProg())
			.addValue("i_estprog",						revisionRequest.getEstProg())
			.addValue("i_nusuprog",						new Long(((UserAuth)principal).getCodPerfil()))
			.addValue("i_npagina",						pageRequest.getPagina())
			.addValue("i_nregistros",					pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearProgramaciones(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerDocumento";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<RevisionRequest> mapearProgramaciones(Map<String, Object> resultados) {
		List<RevisionRequest> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		RevisionRequest item			= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new RevisionRequest();
			
			if (map.get("ANIOPROG")!=null) {				
				item.setAnioProg(((BigDecimal)map.get("ANIOPROG")).longValue());
			}
//			if (map.get("A_V_USUCRE")!=null) {
//				item.setUsuCreProg((String)map.get("A_V_USUCRE"));
//			}
			if (map.get("nombreCompleto")!=null) {
				item.setUsuCreProg((String)map.get("nombreCompleto"));
			}
			if (map.get("A_D_FECMOD")!=null) {
				item.setFechModProg((Date)map.get("A_D_FECMOD"));	
			}
			/*if (map.get("N_IDTIPODOCU")!=null) {
				item.setDesTipoDocuProg((String)map.get("N_IDTIPODOCU"));
			}*/
			if (map.get("DESTIPODOCUPROG")!=null) {
				item.setDesTipoDocuProg((String)map.get("DESTIPODOCUPROG"));
			}
			if (map.get("V_ESTPROG")!=null) {				
				item.setIdEstProg(((BigDecimal)map.get("V_ESTPROG")).longValue());
			}
			if (map.get("DESCESTADO")!=null) {
				item.setEstProg((String)map.get("DESCESTADO"));
			}
			if (map.get("DESCESTADOEJEC")!=null) {
				item.setEstEjec((String)map.get("DESCESTADOEJEC"));
			}
			if (map.get("A_D_FECCRE")!=null) {
				item.setFecCreProg((Date)map.get("A_D_FECCRE"));
			}
			if (map.get("N_IDPROG")!=null) {
				item.setIdProg(((BigDecimal)map.get("N_IDPROG")).longValue());
			}
			
			listaDocumento.add(item);

			if (this.paginacion != null ) {
				if (map.get("RESULT_COUNT")!=null) {
					this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
				}				
			}
		}
		return listaDocumento;
	}
	
	@SuppressWarnings("unchecked")
	private Revision registrarEjecuciones(Revision revision, Long codigo, String usuario) {
		Revision item		= null;
		Map<String, Object> out = null;
		this.error				= null;
		this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_REVISION")
				.withProcedureName("PRC_REVISION_GRABAR_EJECUC")
				.declareParameters(
					new SqlParameter("i_vestejec",			OracleTypes.NUMBER),
					new SqlParameter("i_idprogramacion",	OracleTypes.NUMBER),
					new SqlParameter("i_niddocu",			OracleTypes.NUMBER),
					new SqlParameter("i_nrutadocumt",		OracleTypes.VARCHAR),
				    new SqlParameter("i_idusuresp",			OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
					
					new SqlOutParameter("o_cursor",			OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vestejec", 	revision.getIdestejec())
				.addValue("i_nidprog", 		revision.getIdProgExistente())
				.addValue("i_niddocu", 		revision.getIdDocu())
				.addValue("i_nrutadocumt",	revision.getRutaDocumt())
				.addValue("i_idusuresp", 	codigo)
				.addValue("i_vusuario", 	usuario);
				
			try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				List<Revision> listaDocumento 	= new ArrayList<>();
				int size = listado.size();
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.registrarEjecuciones";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return null;
	}
	
	//cguerra INicio
	
	@Override
	@Transactional
	public boolean guardarEjecucionesProg(RevisionRequestAdjunto revisionEjecucion, Long codigo, String usuario , Long idUsuario) {
		boolean respuesta;
		try {			
			respuesta = this.registrarEjecucionesProgr(revisionEjecucion, codigo, usuario);
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.guardarEjecuciones";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			respuesta =false;
		}
		return respuesta;
	}
	
	@SuppressWarnings("unchecked")
	private Boolean registrarEjecucionesProgr(RevisionRequestAdjunto revision, Long codigo, String usuario) {
		Revision item		= null;
		Map<String, Object> out = null;
		Boolean  repuesta= false;
		this.error				= null;
		this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_REVISION")
				.withProcedureName("PRC_REVISION_GRABAR_EJECUC")
				.declareParameters(
					new SqlParameter("i_vestejec",			OracleTypes.NUMBER),
					new SqlParameter("i_idprogramacion",	OracleTypes.NUMBER),
					new SqlParameter("i_niddocu",			OracleTypes.NUMBER),
					new SqlParameter("i_nrutadocumt",		OracleTypes.VARCHAR),
				    new SqlParameter("i_idusuresp",			OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
					
					new SqlOutParameter("o_cursor",			OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vestejec", 	revision.getIdestejec())
				.addValue("i_nidprog", 		revision.getIdProgExistente())
				.addValue("i_niddocu", 		revision.getIdDocu())
				.addValue("i_nrutadocumt",	revision.getRutaDocumtNueva())
				.addValue("i_idusuresp", 	codigo)
				.addValue("i_vusuario", 	usuario);
				
			try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				List<Revision> listaDocumento 	= new ArrayList<>();				
				repuesta = true;
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
				repuesta 				= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.registrarEjecuciones";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
			repuesta				=null;
		}
		return repuesta;
	}
	
	
	@Override
	@Transactional
	public boolean finalizarEjecuciones(RevisionRequestAdjunto revisionEjecucion, Long codigo, String usuario , Long idUsuario) {
		boolean respuesta;
		try {			
			respuesta = this.finalizarEjecuciones(revisionEjecucion, codigo, usuario);
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.guardarEjecuciones";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			respuesta =false;
		}
		return respuesta;
	}
	

	@SuppressWarnings("unchecked")
	private boolean finalizarEjecuciones(RevisionRequestAdjunto revision, Long codigo, String usuario) {
		Revision item		= null;
		Boolean  respuesta= false;
		Map<String, Object> out = null;
		this.error				= null;
		this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_REVISION")
				.withProcedureName("PRC_REVISION_FIN_EJECUC")
				.declareParameters(
					new SqlParameter("i_vestejec",			OracleTypes.NUMBER),
					new SqlParameter("i_idprogramacion",	OracleTypes.NUMBER),
					new SqlParameter("i_niddocu",			OracleTypes.NUMBER),
					new SqlParameter("i_nrutadocumt",		OracleTypes.VARCHAR),
				    new SqlParameter("i_idusuresp",			OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",			OracleTypes.VARCHAR),
					
					new SqlOutParameter("o_cursor",			OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()			
				.addValue("i_vestejec", 	revision.getIdestejec())
				.addValue("i_nidprog", 		revision.getIdProgExistente())
				.addValue("i_niddocu", 		revision.getIdDocu())
				.addValue("i_nrutadocumt",	revision.getRutaDocumt())
				.addValue("i_idusuresp", 	codigo)
				.addValue("i_vusuario", 	usuario);
				
			try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				List<Revision> listaDocumento 	= new ArrayList<>();
				int size = listado.size();
				respuesta=true;
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				respuesta = false;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.finalizarEjecuciones";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);			
			respuesta = false;
		}
		return respuesta;
	}
	
	
	
	//cguerra FIN
	
	
	
	@SuppressWarnings("unchecked")
	public List<Revision> obtenerRevisionesDoc(RevisionRequest revisionRequest, PageRequest pageRequest, Long idUsuario) {
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
			.withCatalogName("PCK_AGI_REVISION")
			.withProcedureName("PRC_REVISION_OBTENER_DOC")
			.declareParameters(
				new SqlParameter("i_usuario",OracleTypes.NUMBER),
				new SqlParameter("i_codigo",OracleTypes.VARCHAR),
				new SqlParameter("i_titulo",OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
	
			
			in = new MapSqlParameterSource()
				
				.addValue("i_usuario",     idUsuario)
				.addValue("i_codigo",     revisionRequest.getCodigoDoc())
				.addValue("i_titulo",     revisionRequest.getTituloDoc())
				.addValue("i_npagina",		pageRequest.getPagina())
				.addValue("i_nregistros",	pageRequest.getRegistros());
		
		    
			try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
			     lista = this.mapearRevisionDoc(listado);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RevisionDAOImpl.obtenerRevisionesDoc";
			String mensajeInterno	= e.toString();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	private List<Revision> mapearRevisionDoc(List<Map<String, Object>> listado) {
		List<Revision> mLista	= new ArrayList<>();
		Revision item = null;
		int totalListado = listado.size();
		Integer totalRegistro = 0;
		for(Map<String, Object> map : listado) {			
			item = new Revision();
			 
			if(map.get("N_IDREVI")!=null) {
				item.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
			}
			if(map.get("N_NUMREVI")!=null) {
				item.setNumero(((BigDecimal)map.get("N_NUMREVI")).longValue());
			}
			if(map.get("V_NOMREVI")!=null) {
				item.setTitulo((String)map.get("V_NOMREVI"));
				
			}
			if(map.get("N_NUMITER")!=null) {
				item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
				
			}
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
				estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
				estado.setV_descons((String)map.get("V_ESTREVIDOC"));
				item.getDocumento().setEstado(estado);
			}
			
			if(map.get("V_CODDOCU")!=null) {
				item.getDocumento().setCodigo((String)map.get("V_CODDOCU"));
				
			}
			if(map.get("V_DESDOCU")!=null) {
				item.getDocumento().setDescripcion((String)map.get("V_DESDOCU"));
				
			}
			
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
			item.setFechaAprobacionDocumento((Date)map.get("D_FEAPRODOCU"));
			item.setUsuarioAprobacionDocumento((String)map.get("V_USAPRODOCU"));
			item.setNumeroAnterior((map.get("N_NUMREVIANTE")==null)?null:((BigDecimal)map.get("N_NUMREVIANTE")).longValue());
			item.setFechaAprobacionAnterior((Date)map.get("D_FEAPROANTE"));
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
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
			
			if(map.get("N_IDDOCGOOGLEDRIVE")!=null) {
				item.setIdDocGoogleDrive((String)map.get("N_IDDOCGOOGLEDRIVE"));
			}
					
			if(map.get("V_RUTA_DOCUMT")!=null) {
				item.setRutaDocumt((String)map.get("V_RUTA_DOCUMT"));
			}
			
			if(map.get("V_RUTA_DOCU")!=null) {
				item.setRutaDocumentoOriginal((String)map.get("V_RUTA_DOCU"));
			}
			if(map.get("v_ruta_docu_c_obso")!=null) {
				item.setRutaDocumentoCopiaObso((String)map.get("v_ruta_docu_c_obso"));
			}
			if(map.get("v_ruta_docu_c_cont")!=null) {
				item.setRutaDocumentoCopiaCont((String)map.get("v_ruta_docu_c_cont"));
			}
			if(map.get("v_ruta_docu_c_no_cont")!=null) {
				item.setRutaDocumentoCopiaNoCont((String)map.get("v_ruta_docu_c_no_cont"));
			}	
		}
		
		return mLista;
	}
	@Override
	public boolean actualizarConocimientoLectura(Conocimiento conocimiento, String iUsuario) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		Map<String, Object> out = null;
		this.error = null;

		boolean respuesta = false;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc)
					 .withSchemaName("AGI")
					 .withCatalogName("PCK_AGI_DOCUMENTO")
					 .withProcedureName("PRC_CONOCIMIENTO_ACTUALIZAR")
					 .declareParameters(
							new SqlParameter("i_niddocu", OracleTypes.NUMBER),
							new SqlParameter("i_nidrevi", OracleTypes.NUMBER),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_nidpersona", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_niddocu", conocimiento.getIddocumento())
					.addValue("i_nidrevi", conocimiento.getIdrevision())
					.addValue("i_vusuario", iUsuario)
					.addValue("i_nidpersona", new Long(((UserAuth)principal).getCodPerfil()));

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				respuesta = true;				
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);				
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en RevisionDAOImpl.actualizarConocimientoLectura";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}

		return respuesta;

	}
	
	@Override
	public List<Revision> consultarListaEjec(RevisionRequest revisionRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Revision> lista = new ArrayList<>();

		this.paginacion = new Paginacion();
		this.error = null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_REVISION")
				.withProcedureName("PRC_LISTA_CONTROL_EJEC")
				.declareParameters(
						new SqlParameter("i_nidusuresp", OracleTypes.NUMBER),
						new SqlParameter("i_nusuprog", OracleTypes.NUMBER),
						new SqlParameter("i_nidequi", OracleTypes.VARCHAR),
						new SqlParameter("i_anio", OracleTypes.NUMBER),
						new SqlParameter("i_nidprog", OracleTypes.NUMBER),
						new SqlParameter("i_vcoddocu", OracleTypes.VARCHAR),
						new SqlParameter("i_vestejec", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidusuresp", revisionRequest.getNroFicha())
				.addValue("i_nusuprog", revisionRequest.getNroFicha())
				.addValue("i_nidequi", revisionRequest.getEquipoProg())
				.addValue("i_anio", revisionRequest.getAnioProg())
				.addValue("i_nidprog", revisionRequest.getIdProg())
				.addValue("i_vcoddocu", revisionRequest.getCodigoDocumento())
				.addValue("i_vestejec", revisionRequest.getIdEstEjecProg())
				.addValue("i_npagina", pageRequest.getPagina())
				.addValue("i_nregistros", pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = this.mapearEjec(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en RevisionDAOImpl.consultarListaEjec";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Revision> mapearEjec(Map<String, Object> resultados) {
		List<Revision> listaDocumento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		Revision item = null;
		int size = lista.size();

		for (Map<String, Object> map : lista) {
			item = new Revision();

			if (map.get("N_IDPROG") != null)  item.setIdProg(((BigDecimal) map.get("N_IDPROG")).longValue());
			if (map.get("V_ESTPROG") != null) item.setEstProgr((String) map.get("V_ESTPROG"));
			if (map.get("N_IDDOCU") != null)  item.setIdDocu(((BigDecimal) map.get("N_IDDOCU")).longValue());
			if (map.get("N_IDTRIM") != null)  item.setIdTrimestre(((BigDecimal) map.get("N_IDTRIM")).longValue());
			if (map.get("D_FECPROG") != null) item.setFechaProgramacion((Date) map.get("D_FECPROG"));
			if (map.get("D_FECDIST") != null) item.setFechaDistribucion((Date) map.get("D_FECDIST"));
			if (map.get("D_FECEJEC") != null) item.setFechaEjecucion((Date) map.get("D_FECEJEC"));
			if (map.get("V_ESTEJEC") != null) item.setEstEjec(((String) map.get("V_ESTEJEC")));
			if (map.get("V_RUTEJEC") != null) item.setRuta(((String) map.get("V_RUTEJEC")));
			if (map.get("N_IDEQUI") != null)  item.setIdEquipoProgramacion(((BigDecimal) map.get("N_IDEQUI")).longValue());
			if (map.get("V_CODDOCU") != null) item.setCodDocu(((String) map.get("V_CODDOCU")));
			if (map.get("DES_ESTPROG") != null)  item.setDesestadoProg(((String) map.get("DES_ESTPROG")));
			if (map.get("DES_ESTEJEC") != null)  item.setDesestadoejec(((String) map.get("DES_ESTEJEC")));
			if (map.get("V_NOEQUIEMIS") != null) item.setEquipoProgramacion(((String) map.get("V_NOEQUIEMIS")));
			if (map.get("ESTA_DOCU") != null) item.setEstadoSoli(((String) map.get("ESTA_DOCU")));
			if (map.get("ESTA_ACTU") != null) item.setEstadoFase(((String) map.get("ESTA_ACTU")));
			

			listaDocumento.add(item);

			if (this.paginacion != null) {
				if (map.get("RESULT_COUNT") != null) {
					this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
				}
			}
		}
		return listaDocumento;
	}
	@Override
	public List<Revision> consultarProgEquipo(Long idProgramacion) {
		Map<String, Object> out = null;
		List<Revision> lista = new ArrayList<>();

		this.paginacion = new Paginacion();
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_REVISION")
				.withProcedureName("PRC_LISTA_PROG_EQUI")
				.declareParameters(
						new SqlParameter("i_nidprog", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidprog", idProgramacion);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = this.mapearProgEquipo(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en RevisionDAOImpl.consultarListaEjec";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Revision> mapearProgEquipo(Map<String, Object> resultados) {
		List<Revision> listaDocumento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		Revision item = null;

		for (Map<String, Object> map : lista) {
			item = new Revision();

			if (map.get("N_IDPROG") != null) item.setIdProgramacion(((BigDecimal) map.get("N_IDPROG")).longValue());
			if (map.get("N_IDEQUI") != null) item.setIdEquipoProgramacion(((BigDecimal) map.get("N_IDEQUI")).longValue());

			listaDocumento.add(item);
		}
		return listaDocumento;
	}
	@Override
	public List<Revision> consultarProgDistri(Long idProgramacion,  Long idEquipo) {
		
		Map<String, Object> out = null;
		List<Revision> lista = new ArrayList<>();

		this.paginacion = new Paginacion();
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_REVISION")
				.withProcedureName("PRC_LISTA_PROG_DIST")
				.declareParameters(
						new SqlParameter("i_nidprog", OracleTypes.NUMBER),
						new SqlParameter("i_nidequi", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidprog", idProgramacion)
				.addValue("i_nidequi", idEquipo);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = this.mapearProgDistr(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en RevisionDAOImpl.consultarListaEjec";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
		
		
		
	}
	@SuppressWarnings("unchecked")
	private List<Revision> mapearProgDistr(Map<String, Object> resultados) {
		List<Revision> listaDocumento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		Revision item = null;

		for (Map<String, Object> map : lista) {
			item = new Revision();

			if (map.get("N_IDPROG") != null) item.setIdProgramacion(((BigDecimal) map.get("N_IDPROG")).longValue());
			if (map.get("N_USUDIST") != null) item.setIdEquipoProgramacion(((BigDecimal) map.get("N_USUDIST")).longValue());

			listaDocumento.add(item);
		}
		return listaDocumento;
	}
	@Override
	public boolean guardarEjecucionesProg(RevisionRequestAdjunto adjunto, Object codigo, String codUsuario,
			Long idUsuario) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean guardarEjecuciones(List<Revision> revisionEjecucion, Long codigo, String usuario, Long idUsuario) {
		// TODO Auto-generated method stub
		return false;
	}

}