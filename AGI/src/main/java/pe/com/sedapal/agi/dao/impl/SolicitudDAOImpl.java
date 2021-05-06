package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.ISolicitudDAO;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.SolicitudCopia;
import pe.com.sedapal.agi.model.enums.EstadoConstante;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class SolicitudDAOImpl implements ISolicitudDAO {
	//private Long  EstadoRevision;
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
	@Override
	@Transactional
	public Documento guardarSolicitudParticipantes(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario) {		
		Documento item = new Documento();
		try {			
			item = this.guardarSolicitud(solicitud, codigo, usuario,idUsuario);
			if(item==null) {
				return null;
			}			
			if(solicitud.getListaParticipante()!=null) {	
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : solicitud.getListaParticipante()) {					
					Colaborador temporal = this.guardarSolicitudDetalle(solicitud, codigo!= null?codigo:item.getId(), usuario, idUsuario,participante);
					if(temporal==null) {
						return null;
					}					
					lista.add(temporal);				
				}
				item.setListaParticipante(lista);
			}
			
		}catch(Exception e){
				Integer resultado		= 1;
				String mensaje			= "Error en DocumentoDAOImpl.guardarDocumento";
				String mensajeInterno	= e.getMessage();
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
			return item;		
	}
	@Override
	public Documento guardarSolicitud(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario) {
		Documento item			= null;
		this.error=null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc);
			this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_COPIA_IMPRESA_GUARDAR").declareParameters(					
				    new SqlParameter("i_idcopi", OracleTypes.NUMBER),
				    new SqlParameter("i_nidocu", OracleTypes.NUMBER),
				    new SqlParameter("i_nidrevi", OracleTypes.NUMBER),
					new SqlParameter("i_ndisponicopi", OracleTypes.NUMBER),
					new SqlParameter("i_nidsoli", OracleTypes.NUMBER),				
					new SqlParameter("i_nestacopi", OracleTypes.NUMBER),				
					new SqlParameter("i_vsustencopi", OracleTypes.VARCHAR),
					new SqlParameter("i_nmoticopi", OracleTypes.NUMBER),				
					new SqlParameter("i_ntipocopi", OracleTypes.NUMBER),
					new SqlParameter("i_vcodcopia", OracleTypes.VARCHAR),
					new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlParameter("i_vusuAprob", OracleTypes.NUMBER),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource()				
					.addValue("i_idcopi", codigo)
					.addValue("i_nidocu", solicitud.getId())
					.addValue("i_nidrevi", solicitud.getNidrevision())
					.addValue("i_ndisponicopi", 1)
					.addValue("i_nidsoli",idUsuario)
					.addValue("i_nestacopi", 141)
					.addValue("i_vsustencopi", solicitud.getSustento())
					.addValue("i_nmoticopi", solicitud.getMotivoR())
					.addValue("i_ntipocopi", solicitud.getTipoCopia())
					.addValue("i_vcodcopia", 0)			
					.addValue("i_vusuario", usuario)
					.addValue("i_vusuAprob",solicitud.getIdUsuAprobador());
			Map<String, Object> out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");				
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Documento();
					if(map.get("N_IDCOPI")!=null){
						item.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());	
					}
				}
			}
			else {			
					String mensaje			= (String)out.get("o_mensaje");
					String mensajeInterno	= (String)out.get("o_sqlerrm");
					this.error				= new Error(resultado,mensaje,mensajeInterno);
					item					= null;
				}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en SolicitudDAOImpl.guardarSolicitud";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			System.out.println("Error al guardar la Solicitud" + this.error.getMensaje());
		}
		return item;
	}
	
	@Override
	public Colaborador guardarSolicitudDetalle(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario,Colaborador colaborador) {
		Colaborador item		= null;
		this.error=null;
		try {
				this.jdbcCall = new SimpleJdbcCall(this.jdbc);
				this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
						.withProcedureName("PRC_COPIA_DETALLE_GUARDAR").declareParameters(
						new SqlParameter("i_nidcola", OracleTypes.NUMBER),
						//new SqlParameter("i_nidocu", OracleTypes.NUMBER),
					    new SqlParameter("i_idcopi", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
				SqlParameterSource in = new MapSqlParameterSource()
						.addValue("i_nidcola", colaborador.getIdColaborador())					
						.addValue("i_idcopi", codigo)					
						.addValue("i_vusuario", usuario);
			Map<String, Object> out = this.jdbcCall.execute(in);			
			Integer resultado = (Integer)out.get("o_retorno");	
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Colaborador();
					if(map.get("N_IDCOPI")!=null){
						item.setIdColaborador(((BigDecimal)map.get("N_IDCOPI")).longValue());	
					}
				}
			}else {			
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en SolicitudDAOImpl.guardarSolicitudDetalles";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			System.out.println("Error al guardar la Solicitud de Detalle" + this.error.getMensaje());
		}
		return item;
	}
	
	//Obtener Solicitud
	@SuppressWarnings("unchecked")
	public List<Revision> obtenerSolicitud(RevisionRequest revisionRequest, PageRequest pageRequest, Long idUsuario) {
		//Date fechaRevision = null;		
		SqlParameterSource in = null;
		Map<String, Object> out	= null;
		List<Revision> lista	= null;
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());		
		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_SOLICITUD_COPIA_OBTENER")//PRC_SOLICITUD_COPIA_OBTENER
			.declareParameters(					
				new SqlParameter("i_idUsuario", 	OracleTypes.NUMBER),
				new SqlParameter("i_niddocu", 	OracleTypes.NUMBER),				
				new SqlParameter("i_tipocopi", 	OracleTypes.NUMBER),
				new SqlParameter("i_vcoddocument",	OracleTypes.VARCHAR),
				new SqlParameter("i_vtitulodocum",	OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlParameter("i_dfecrevi",		OracleTypes.DATE),				
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			in = new MapSqlParameterSource()
				.addValue("i_idUsuario", 		idUsuario)	
				.addValue("i_niddocu", 		revisionRequest.getIdDocumento()) 
				.addValue("i_tipocopi", 		revisionRequest.getBtipocopi())
				.addValue("i_vcoddocument", 		revisionRequest.getCodigoDocumento())
				.addValue("i_vtitulodocum", 		revisionRequest.getTitulodocumento())
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
	private List<Revision> mapearRevision(List<Map<String, Object>> listado) {
		List<Revision> mLista	= new ArrayList<>();
		Revision item = null;
		int totalListado = listado.size();
		Integer totalRegistro = 0;
		for(Map<String, Object> map : listado) {			
			item = new Revision();
			
			if(map.get("N_NUMREVI")!=null) {
				item.setNumero(map.get("N_NUMREVI")!=null?((BigDecimal)map.get("N_NUMREVI")).longValue():null);			
			}
			
			
			if(map.get("V_NOMREVI")!=null) {
				item.setTitulo((String)map.get("V_NOMREVI"));
				
			}
			if(map.get("V_NOMREVI")!=null) {
				item.setTitulo((String)map.get("V_NOMREVI"));
				
			}
			
			if(map.get("D_FECREVI")!=null) {
				item.setFecha((Date)map.get("D_FECREVI"));
				
			}
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
			if(map.get("V_DESDOCU")!=null) {
				item.getDocumento().setDescripcion((String)map.get("V_DESDOCU"));
			}			
			if(map.get("N_IDESTAFASEACT")!=null) {
				Constante estadofaseact = new Constante();
				estadofaseact.setIdconstantefaseact(((BigDecimal)map.get("N_IDESTAFASEACT")).longValue());
				estadofaseact.setV_desconsfaseact((String)map.get("V_NOESTAFASEACT"));
				item.setEstadofaseact(estadofaseact);
			}
	 //sabado 
			if(map.get("N_IDDOCU")!=null) {
				item.getDocumento().setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
			}
			if(map.get("V_CODDOCU")!=null) {
				item.getDocumento().setCodigo((String)map.get("V_CODDOCU"));
			}
			if(map.get("N_IDREVI")!=null) {
				item.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
			}
			if(map.get("v_solicitante")!=null) {
				item.setSolicitantSolicitud((String)map.get("v_solicitante"));
			}
			if(map.get("v_destinatario")!=null) {
				item.setDestinatarioSolicitud((String)map.get("v_destinatario"));
			}			
			if(map.get("N_TIPCOPI")!=null) {
				item.setNumerotipocopia(((BigDecimal)map.get("N_TIPCOPI")).longValue());
			}
			if(map.get("v_ntipocopia")!=null) {
				item.setTipocopia((String)map.get("v_ntipocopia"));
			}
			if(map.get("N_MOTCOPI")!=null) {
				item.setNumeromotivo(((BigDecimal)map.get("N_MOTCOPI")).longValue());
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
			if(map.get("numero_solicitud")!=null) {
				item.setNumerosol(((BigDecimal)map.get("numero_solicitud")).longValue());
			}
			if(map.get("V_OBSCOPI")!=null) {
				item.setSusteso((String)map.get("V_OBSCOPI"));
			}
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
			if(map.get("RNUM")!=null) {
				item.setNrum(((BigDecimal)map.get("RNUM")).longValue());
			}
			if(map.get("v_nmotivosolicitud")!=null) {
				item.setMotivoR((String)map.get("v_nmotivosolicitud"));
			}
			if(map.get("N_IDREVI")!=null) {
				item.setNidrevi(((BigDecimal)map.get("N_IDREVI")).longValue());
			}
			if(map.get("idfase")!=null) {
				item.setIdususoli(((BigDecimal)map.get("idfase")).longValue());
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
		} else {
			this.paginacion.setTotalRegistros(0);
		}
		return mLista;
	}
		
	/* Obtener Destinatario */
	@Override 
	public List<Colaborador> obtenerDestinatario(ColaboradorRequest colaboradorRequest, PageRequest pageRequest) {//obtenerDestinatarioDocumento
		Map<String, Object> out	= null;
		List<Colaborador> lista	= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_DEST_COLA_OBTENER")
			.declareParameters(					
				new SqlParameter("i_niddoc", 			OracleTypes.NUMBER),
				new SqlParameter("i_nidcopi", 			OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlParameter("i_vorden", 		OracleTypes.VARCHAR),						
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
		
		SqlParameterSource in =
		new MapSqlParameterSource()
		.addValue("i_niddoc",			colaboradorRequest.getIdDocumento())	
		.addValue("i_nidcopi",			colaboradorRequest.getIdSolicitud())
			.addValue("i_npagina",		pageRequest.getPagina())
			.addValue("i_nregistros",	pageRequest.getRegistros())
			.addValue("i_vorden",		null);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearColaborador(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en ColaboradorDAOImpl.obtenerColaboradors";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return lista;
	}
	@SuppressWarnings("unchecked")
	private List<Colaborador> mapearColaborador(Map<String, Object> resultados) {
		List<Colaborador> listaColaborador	= new ArrayList<>();		
		List<Map<String, Object>> lista 	= (List<Map<String, Object>>)resultados.get("o_cursor");
		Colaborador item					= new Colaborador(); //null;
		
		for(Map<String, Object> map : lista) {			
			item = new Colaborador();
			if(map.get("N_IDCOLA")!=null) {				
				item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
				
			}
			if(map.get("V_FUNCOLA")!=null) {				
				item.setFuncion((String)map.get("V_FUNCOLA"));
			}
			if(map.get("V_NROFICHA")!=null) {				
				item.setNumeroFicha(((BigDecimal)map.get("V_NROFICHA")).longValue());
				//item.setNumeroFicha((Long)map.get("V_NROFICHA"));
			}
			if(map.get("V_APEPAT")!=null) {				
				item.setApellidoPaterno((String)map.get("V_APEPAT"));
			}
			if(map.get("V_APEMAT")!=null) {				
				item.setApellidoMaterno((String)map.get("V_APEMAT"));
			}
			if(map.get("V_NOMBRES")!=null) {				
				item.setNombre((String)map.get("V_NOMBRES"));
			}
			if(map.get("N_DISCOLA")!=null) {				
				item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISCOLA")));
			}
						
			item.setNombreCompleto(item.getNombre()+" "+item.getApellidoPaterno()+" "+item.getApellidoMaterno());
			if(map.get("N_IDCOPI")!=null) {				
				item.setIdsolicitud(((BigDecimal)map.get("N_IDCOPI")).longValue());
			}
			if(map.get("V_NESTADOSOLICITUD")!=null) {
				item.setEstadoSoli((String)map.get("V_NESTADOSOLICITUD"));
			}			
			if(map.get("V_TIPCOLA")!=null) {
				Constante tipo = new Constante();
				tipo.setIdconstante(((BigDecimal)map.get("V_TIPCOLA")).longValue());
				tipo.setV_nomcons((String)map.get("V_NOMTIPO"));
				item.setTipo(tipo);
			}

			if(map.get("V_ROLAUD")!=null) {
				Constante rolAuditor = new Constante();
				rolAuditor.setIdconstante(((BigDecimal)map.get("V_ROLAUD")).longValue());
				rolAuditor.setV_nomcons((String)map.get("V_NOMROL"));
				item.setTipo(rolAuditor);
			}

			if(map.get("N_IDEQUI")!=null) {
				Equipo equipo = new Equipo();
				equipo.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
				equipo.setDescripcion((String)map.get("V_NOEQUI"));
				item.setEquipo(equipo);
			}
			
			listaColaborador.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		return listaColaborador;
	}
	
	
	/* Obtener obtenerDestinatarioDocumento */	
		@SuppressWarnings("unchecked")
		public List<Revision> obtenerSolicitudDocument(RevisionRequest revisionRequest, PageRequest pageRequest, Long idUsuario) {
			SqlParameterSource in = null;
			Map<String, Object> out	= null;
			List<Revision> lista	= new ArrayList<>();
			this.paginacion = new Paginacion();
			paginacion.setPagina(pageRequest.getPagina());
			paginacion.setRegistros(pageRequest.getRegistros());		
			
			this.error		= null;
			this.jdbcCall	=
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_SOLICITUD_DOC_OBTENER") ///consulta
				.declareParameters(					
					new SqlParameter("i_idUsuario", 	OracleTypes.NUMBER),
					new SqlParameter("i_niddocu", 	OracleTypes.NUMBER),				
					new SqlParameter("i_tipocopi", 	OracleTypes.NUMBER),
					new SqlParameter("i_vcoddocument",	OracleTypes.VARCHAR),
					new SqlParameter("i_vtitulodocum",	OracleTypes.VARCHAR),
					
					new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
					new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
					new SqlParameter("i_dfecrevi",		OracleTypes.DATE),
					new SqlParameter("i_colaborador",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			
				in = new MapSqlParameterSource()
					.addValue("i_idUsuario", 		idUsuario)	
					.addValue("i_niddocu", 		revisionRequest.getIdDocumento()) 
					.addValue("i_tipocopi", 		revisionRequest.getBtipocopi())
					.addValue("i_vcoddocument", 		revisionRequest.getCodigoDocumento())
					.addValue("i_vtitulodocum", 		revisionRequest.getTitulodocumento())
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
	
		//Actualiza Solicitud
		@Override
		public Documento UpdateSolicitud(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario) {
			Documento item			= null;
			this.error=null;
			try {
				this.jdbcCall = new SimpleJdbcCall(this.jdbc);
				this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_COPIA_IMPRESA_UPDATE").declareParameters(					
					    new SqlParameter("i_idcopi", OracleTypes.NUMBER),
					    new SqlParameter("i_nidocu", OracleTypes.NUMBER),				    
						new SqlParameter("i_ndisponicopi", OracleTypes.NUMBER),
						new SqlParameter("i_nidsoli", OracleTypes.NUMBER),				
						new SqlParameter("i_nestacopia", OracleTypes.NUMBER),				
						new SqlParameter("i_vsustencopi", OracleTypes.VARCHAR),
						new SqlParameter("i_nmoticopi", OracleTypes.NUMBER),				
						new SqlParameter("i_ntipocopi", OracleTypes.NUMBER),
						new SqlParameter("i_vcodcopia", OracleTypes.VARCHAR),
						new SqlParameter("i_vcomentcritica", OracleTypes.VARCHAR),						
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),				
						new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
				SqlParameterSource in = new MapSqlParameterSource()				
						.addValue("i_idcopi", codigo)
						.addValue("i_nidocu", solicitud.getId())
						.addValue("i_ndisponicopi", 1)
						.addValue("i_nidsoli",idUsuario)
						.addValue("i_nestacopia", solicitud.getIndicadorestado())//Aprobado 143
						.addValue("i_vsustencopi", solicitud.getSustento())
						.addValue("i_nmoticopi", solicitud.getMotivoR())
						.addValue("i_ntipocopi", solicitud.getTipoCopia())
						.addValue("i_vcodcopia", 0)	
						.addValue("i_vcomentcritica", solicitud.getResumenCritica())//falta critica)
						.addValue("i_vusuario", usuario);
				Map<String, Object> out = this.jdbcCall.execute(in);
				Integer resultado = (Integer)out.get("o_retorno");				
				if(resultado == 0) {
					List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
					for(Map<String, Object> map : listado) {
						item = new Documento();
						if(map.get("N_IDCOPI")!=null){
							item.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());	
						}
					}
				}
				else {			
						String mensaje			= (String)out.get("o_mensaje");
						String mensajeInterno	= (String)out.get("o_sqlerrm");
						this.error				= new Error(resultado,mensaje,mensajeInterno);
						item					= null;
					}
			}catch(Exception e){
				Integer resultado		= 1;
				String mensaje			= "Error en SolicitudDAOImpl.guardarSolicitud";
				String mensajeInterno	= e.getMessage();
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				System.out.println("Error al guardar la Solicitud" + this.error.getMensaje());
			}
			return item;
		}
			
		//Eliminar Destinatario
		private Boolean eliminarDestinatario(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario) {
			Map<String, Object> out = null;
			this.error = null;
			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_COPIA_DETALLE_ELIMINAR")
				.declareParameters(
					new SqlParameter("i_nidcopi",	OracleTypes.NUMBER), //Id de copia solicitud
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nidcopi",	codigo)				
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
				String mensaje			= "Error en DocumentoDAOImpl.eliminarParticipante";
				String mensajeInterno	= e.getMessage();
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				return false;
			}
		}
		/*       Actulizar Solicitud      */ 
		@Override
		@Transactional		
		public Documento ActualizaSolicitud(SolicitudCopia solicitud, Long codigo, String usuario, Long idUsuario) {		
			Documento item = new Documento();
			try {			
				item = this.UpdateSolicitud(solicitud, codigo, usuario,idUsuario);
				if(item==null) {
					return null;
				}
				
				// Eliminar Destinatario			
				boolean eliminado = this.eliminarDestinatario(solicitud, codigo!= null?codigo:item.getId(), usuario, idUsuario);
				if(eliminado==false)	{
					return null;
				}
				
			if(solicitud.getListaParticipante()!=null) {	
					List<Colaborador> lista = new ArrayList<>();
					for(Colaborador participante : solicitud.getListaParticipante()) {					
						Colaborador temporal = this.guardarSolicitudDetalle(solicitud, codigo!= null?codigo:item.getId(), usuario, idUsuario,participante);
						if(temporal==null) {
							return null;
						}					
						lista.add(temporal);				
					}
					item.setListaParticipante(lista);
									
				}
				
			}catch(Exception e){
					Integer resultado		= 1;
					String mensaje			= "Error en SolicitudDAOImpl.actualizaSolicitud";
					String mensajeInterno	= e.getMessage();
					this.error				= new Error(resultado,mensaje,mensajeInterno);
				}
				return item;		
		}
		
		



}
