package pe.com.sedapal.agi.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import pe.com.sedapal.agi.model.response_objects.Error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.IDocumentoDAO;
//import pe.com.sedapal.agi.dao.IRevisionDAO;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Copia;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.RelacionCoordinador;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.SolicitudCopia;
import pe.com.sedapal.agi.model.enums.EstadoConstante;
import pe.com.sedapal.agi.model.enums.Fase;
import pe.com.sedapal.agi.model.enums.Mensaje;
import pe.com.sedapal.agi.model.enums.TipoConstante;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
//import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.service.impl.RevisionServiceImpl;
//import pe.com.sedapal.agi.model.enums.EstadoConstante;
//import pe.com.sedapal.agi.model.enums.Fase;
//import pe.com.sedapal.agi.model.enums.TipoConstante;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import pe.com.sedapal.agi.util.UArchivo;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Repository
public class DocumentoDAOImpl implements IDocumentoDAO {
	//Subir documento a Google Drive - Inicio.
	@Autowired
	Environment env;	
	private String endpointServidor;	
	private static String carpetaResources;
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();    
    private static  String TOKENS_DIRECTORY_PATH;
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static  String CREDENTIALS_FILE_PATH;
    //Subir documento a Google Drive - Fin.
    
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
	public List<Documento> obtenerDocumento(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Documento> lista		= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_DOCUMENTO_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 				OracleTypes.NUMBER),						
				new SqlParameter("i_vcodigo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_vtitulo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_nestado", 			OracleTypes.VARCHAR),
				new SqlParameter("i_ndisponible",		OracleTypes.NUMBER),
				new SqlParameter("i_nidproc",	    	OracleTypes.NUMBER),
				new SqlParameter("i_nidalcasgi",		OracleTypes.NUMBER),
				new SqlParameter("i_nidgeregnrl",		OracleTypes.NUMBER),
				new SqlParameter("i_nidtipodocu",		OracleTypes.NUMBER),				
				new SqlParameter("i_nfecharevdesde",	OracleTypes.DATE),
				new SqlParameter("i_nfecharevhasta",	OracleTypes.DATE),
				new SqlParameter("i_nfechaaprobdesde",	OracleTypes.DATE),
				new SqlParameter("i_nfechaaprobhasta",	OracleTypes.DATE),
				new SqlParameter("i_nperiodooblig",		OracleTypes.NUMBER),
				new SqlParameter("i_nmotirevision",		OracleTypes.NUMBER),
				new SqlParameter("i_nnumrevi",			OracleTypes.NUMBER),
				new SqlParameter("i_nidarea",			OracleTypes.VARCHAR),
				new SqlParameter("i_nidparticipante",	OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseact",		OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseestadoact",	OracleTypes.NUMBER),
				new SqlParameter("i_vmodalava",			OracleTypes.VARCHAR),
				new SqlParameter("i_vmodal",			OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",						documentoRequest.getId()) 
			.addValue("i_vcodigo",					documentoRequest.getCodigo())
			.addValue("i_vtitulo",					documentoRequest.getTitulo())
			.addValue("i_nestado",					(documentoRequest.getEstdoc()==null || documentoRequest.getEstdoc().equals(",,"))?null:documentoRequest.getEstdoc())
			.addValue("i_ndisponible",				null)
			.addValue("i_nidproc",	    			documentoRequest.getIdproc())
			.addValue("i_nidalcasgi",				documentoRequest.getIdalcasgi())
			.addValue("i_nidgeregnrl",				documentoRequest.getIdgeregnrl())
			.addValue("i_nidtipodocu",				(documentoRequest.getTipodocumento()==0)?null:documentoRequest.getTipodocumento())
			.addValue("i_nfecharevdesde",			documentoRequest.getFecharevdesde())
			.addValue("i_nfecharevhasta",			documentoRequest.getFecharevhasta())
			.addValue("i_nfechaaprobdesde",			documentoRequest.getFechaaprobdesde())
			.addValue("i_nfechaaprobhasta",			documentoRequest.getFechaaprobhasta())
			.addValue("i_nperiodooblig",			(documentoRequest.getPeriodooblig()==0)?null:documentoRequest.getPeriodooblig())
			.addValue("i_nmotirevision",			(documentoRequest.getMotirevision()==0)?null:documentoRequest.getMotirevision())
			.addValue("i_nnumrevi",					(documentoRequest.getNumrevi()==0)?null:documentoRequest.getNumrevi())
			.addValue("i_nidarea",					documentoRequest.getListaArea())
			.addValue("i_nidparticipante",			(documentoRequest.getIdparticipante()==0)?null:documentoRequest.getIdparticipante())
			.addValue("i_nidfaseact",				(documentoRequest.getIdfaseact()==0)?null:documentoRequest.getIdfaseact())
			.addValue("i_nidfaseestadoact",			(documentoRequest.getIdfaseestadoact()==0)?null:documentoRequest.getIdfaseestadoact())
			.addValue("i_vmodalava",				documentoRequest.getVmodalava())
			.addValue("i_vmodal",					documentoRequest.getVmodal())
			.addValue("i_npagina",					pageRequest.getPagina())
			.addValue("i_nregistros",				pageRequest.getRegistros());
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
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumento";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@Override
	public List<Documento> obtenerDocumentoHisto(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Documento> lista		= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_DOCUMENTO_OBTENER_HIST")
			.declareParameters(				
				new SqlParameter("i_nid", 				OracleTypes.NUMBER),
				new SqlParameter("i_nidrevi", 			OracleTypes.NUMBER),
								
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
				//new SqlParameter("i_vorden", 			OracleTypes.VARCHAR),						
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()			
			.addValue("i_nid",						documentoRequest.getId())
			.addValue("i_nidrevi",					documentoRequest.getIdrevison())
			
			.addValue("i_npagina",					pageRequest.getPagina())
			.addValue("i_nregistros",				pageRequest.getRegistros());
			
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
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumento";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
		
	/////////////////////////////////////////////
	@Override
	public List<Documento> obtenerDocumentoRevision(DocumentoRequest documentoRequest,Long codigoRevision, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Documento> lista		= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION") 
			.withProcedureName("PRC_DOCUSOLIC_REVI")
			.declareParameters(
				new SqlParameter("i_nid", 				OracleTypes.NUMBER),
				new SqlParameter("i_nidrevi", 				OracleTypes.NUMBER),				
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),										
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",						documentoRequest.getId())
			.addValue("i_nidrevi",					codigoRevision) 			
			.addValue("i_npagina",					pageRequest.getPagina())
			.addValue("i_nregistros",				pageRequest.getRegistros());
			//.addValue("i_vorden",					null);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearDocumentoRevision(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en Documentorevision.obtenerDocumento";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}	
	
	@SuppressWarnings("unchecked")
	private List<Documento> mapearDocumentoRevision(Map<String, Object> resultados) {
		List<Documento> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Documento item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Documento();
			item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
			item.setCodigo((String)map.get("V_CODDOCU"));
			item.setDescripcion((String)map.get("V_DESDOCU"));
			//Ruta del Documento Digital
			item.setRutaDocumento((String)map.get("V_RUTA_DOCUMT"));			
			
			if (map.get("N_VERDOCU") != null) {
				item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());
			}
			if (map.get("N_RETDOCU") != null) {
				item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
			}

			if (map.get("N_ROBLI") != null) {
				item.setPeriodo(((BigDecimal)map.get("N_ROBLI")).longValue());
			}
			if (map.get("N_INDDOCDIG") != null) {
				item.setIndicadorDigital(((BigDecimal)map.get("N_INDDOCDIG")).longValue());
			}
			/*
			if (map.get("N_PERIOBLI") != null) {
				item.setPeriodo(((BigDecimal)map.get("N_PERIOBLI")).longValue());
			}*/
			if (map.get("V_JUSTDOCU") != null) {
				item.setJustificacion(((String)map.get("V_JUSTDOCU")));
			}
			if (map.get("V_NOARCHDOCU") != null) {
				item.setNombreArchivo(((String)map.get("V_NOARCHDOCU")));
			}
			item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));
			item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));
			item.setEstadoDercarga((String)map.get("V_ESTADESC"));
			item.setRaiz((String)map.get("V_RAIZDOCU"));
			
			if(map.get("N_DISDOCU")!=null) {
			item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCU")));
			}
			
			if(map.get("N_IDDOCUPADR")!=null) {
				Documento padre = new Documento(); 
				padre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
				item.setPadre(padre);
			}
			
			if(map.get("N_IDESTA")!=null) {
				Constante estado = new Constante();
				estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
				estado.setV_descons((String)map.get("V_NOESTA"));
				item.setEstado(estado);
			}
			
			if(map.get("N_IDPROC")!=null) {
				Jerarquia jproceso = new Jerarquia();
				//Constante proceso = new Constante();
				//proceso.setIdconstante(((BigDecimal)map.get("N_IDPROC")).longValue());
				jproceso.setIdJerarquia(((BigDecimal)map.get("N_IDPROC")).longValue());
				//proceso.setV_descons((String)map.get("V_NOPROC"));
				jproceso.setV_descons((String)map.get("V_NOPROC"));
				jproceso.setDescripcion((String)map.get("V_RUPROC"));
				//item.setProceso(proceso);
				item.setJproceso(jproceso);
			}
			
			if(map.get("N_IDALCASGI")!=null) {
				
				Jerarquia jalcance = new Jerarquia();
				//Constante alcance = new Constante();
				jalcance.setIdJerarquia(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				//alcance.setIdconstante(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				jalcance.setV_descons((String)map.get("V_NOALCA"));
				//alcance.setV_descons((String)map.get("V_NOALCA"));
				//item.setAlcanceSGI(alcance);
				jalcance.setDescripcion((String)map.get("V_RUALCA"));
				item.setJalcanceSGI(jalcance);
			}
			
			if(map.get("N_IDGEREGNRL")!=null) {
				Jerarquia jgerencia = new Jerarquia();
				//Constante gerencia = new Constante();
				jgerencia.setIdJerarquia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				//gerencia.setIdconstante(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				jgerencia.setV_descons((String)map.get("V_NOGERE"));
				//gerencia.setV_descons((String)map.get("V_NOGERE"));
				jgerencia.setDescripcion((String)map.get("V_RUGERE"));
				item.setJgerencia(jgerencia);
				//item.setGerencia(gerencia);
			}
			
			if(map.get("N_IDTIPODOCU")!=null) {
				Constante tipoDocumento = new Constante();
				tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
				tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
				item.setCtipoDocumento(tipoDocumento);
			}
			
			if(map.get("N_IDCODIANTE")!=null) {
				Codigo codigoAnterior = new Codigo();
				//codigoAnterior.setId(((BigDecimal)map.get("N_IDCODIANTE")).longValue());
				codigoAnterior.setCodigo((String)map.get("V_IDCODIANTE"));
				item.setCodigoAnterior(codigoAnterior);
			}
			
			if(map.get("N_IDMOTIREVI")!=null) {
				Constante motivoRevision = new Constante();
				motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
				motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
				item.setMotivoRevision(motivoRevision);
			}
			
			if(map.get("N_IDEMIS")!=null) {
				Colaborador emisor = new Colaborador();
				emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
				emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
				if(map.get("N_IDEQUIEMIS")!=null) {
					Equipo equipoEmisor = new Equipo();
					equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
					equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
					emisor.setEquipo(equipoEmisor);
				}
				item.setEmisor(emisor);
			}
			
			if(map.get("N_IDFASE")!=null) {
				Constante fase = new Constante();
				fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
				fase.setV_descons((String)map.get("V_NOFASE"));
				item.setFase(fase);
			}
			
			if(map.get("N_IDREVI")!=null) {
				Revision revision = new Revision();
				revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
				revision.setNumero((map.get("N_NUMREVI")==null)?null:((BigDecimal)map.get("N_NUMREVI")).longValue());
				revision.setFecha((Date)map.get("D_FEREVI"));
				//revision.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
				item.setRevision(revision);
				item.setIdrevision(String.valueOf(revision.getId()));
			}

			if(map.get("N_IDCARP")!=null) {
				Jerarquia fase = new Jerarquia();
				fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
				item.setNodo(fase);
			}

			if(map.get("N_IDCOPI")!=null) {
				Copia copia = new Copia();
				copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
				item.setCopia(copia);
			}
			
			if(map.get("N_IDCOOR")!=null) {
				Colaborador coordinador = new Colaborador();
				coordinador.setIdColaborador(((BigDecimal)map.get("N_IDCOOR")).longValue());
				coordinador.setNombreCompleto((String)map.get("V_NOCOOR"));
				item.setCoordinador(coordinador);
			}

			listaDocumento.add(item);

			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaDocumento;
	}
	
	//Documento Solicitud
	@Override
	public List<Documento> obtenerDocumentoSolicitud(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Documento> lista		= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION") 
			.withProcedureName("PRC_DOCU_SOLICITUD_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 				OracleTypes.NUMBER),
				new SqlParameter("i_ntipocopi", 		OracleTypes.NUMBER),				
				new SqlParameter("i_nidrevi", 			OracleTypes.NUMBER),
				new SqlParameter("i_vcodigo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_vtitulo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_nestado", 			OracleTypes.VARCHAR),
				new SqlParameter("i_ndisponible",		OracleTypes.NUMBER),				
				new SqlParameter("i_nidproc",	    	OracleTypes.NUMBER),
				new SqlParameter("i_nidalcasgi",		OracleTypes.NUMBER),
				new SqlParameter("i_nidgeregnrl",		OracleTypes.NUMBER),
				new SqlParameter("i_nidtipodocu",		OracleTypes.NUMBER),			
				new SqlParameter("i_nfecharevdesde",	OracleTypes.DATE),
				new SqlParameter("i_nfecharevhasta",	OracleTypes.DATE),
				new SqlParameter("i_nfechaaprobdesde",	OracleTypes.DATE),
				new SqlParameter("i_nfechaaprobhasta",	OracleTypes.DATE),
				new SqlParameter("i_nperiodooblig",		OracleTypes.NUMBER),
				new SqlParameter("i_nmotirevision",		OracleTypes.NUMBER),
				new SqlParameter("i_nnumrevi",			OracleTypes.NUMBER),
				new SqlParameter("i_nidarea",			OracleTypes.VARCHAR),
				new SqlParameter("i_nidparticipante",	OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseact",		OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseestadoact",	OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),										
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));	
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",						documentoRequest.getId())
			.addValue("i_ntipocopi",				documentoRequest.getIdtipocopia())
			.addValue("i_nidrevi",					documentoRequest.getIdrevison())			
			.addValue("i_vcodigo",					documentoRequest.getCodigo())
			.addValue("i_vtitulo",					documentoRequest.getTitulo())
			.addValue("i_nestado",					(documentoRequest.getEstdoc()==null || documentoRequest.getEstdoc().equals(",,"))?null:documentoRequest.getEstdoc())//documentoRequest.getEstdoc())
			.addValue("i_ndisponible",				null)			
			.addValue("i_nidproc",	    			documentoRequest.getIdproc())
			.addValue("i_nidalcasgi",				documentoRequest.getIdalcasgi())
			.addValue("i_nidgeregnrl",				documentoRequest.getIdgeregnrl())
			.addValue("i_nidtipodocu",				(documentoRequest.getTipodocumento()==0)?null:documentoRequest.getTipodocumento())
			.addValue("i_nfecharevdesde",			documentoRequest.getFecharevdesde())
			.addValue("i_nfecharevhasta",			documentoRequest.getFecharevhasta())
			.addValue("i_nfechaaprobdesde",			documentoRequest.getFechaaprobdesde())
			.addValue("i_nfechaaprobhasta",			documentoRequest.getFechaaprobhasta())
			.addValue("i_nperiodooblig",			(documentoRequest.getPeriodooblig()==0)?null:documentoRequest.getPeriodooblig())
			.addValue("i_nmotirevision",			(documentoRequest.getMotirevision()==0)?null:documentoRequest.getMotirevision())
			.addValue("i_nnumrevi",					(documentoRequest.getNumrevi()==0)?null:documentoRequest.getNumrevi())
			.addValue("i_nidarea",					documentoRequest.getListaArea())
			.addValue("i_nidparticipante",			(documentoRequest.getIdparticipante()==0)?null:documentoRequest.getIdparticipante())
			.addValue("i_nidfaseact",				(documentoRequest.getIdfaseact()==0)?null:documentoRequest.getIdfaseact())
			.addValue("i_nidfaseestadoact",			(documentoRequest.getIdfaseestadoact()==0)?null:documentoRequest.getIdfaseestadoact())			
			.addValue("i_npagina",					pageRequest.getPagina())
			.addValue("i_nregistros",				pageRequest.getRegistros());			
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearDocumentoSolicitud(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en Documentosolicitud.obtenerDocumento";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Documento> mapearDocumentoSolicitud(Map<String, Object> resultados) {
		List<Documento> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Documento item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Documento();
			item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
			item.setCodigo((String)map.get("V_CODDOCU"));
			item.setDescripcion((String)map.get("V_DESDOCU"));
			//Ruta del Documento Digital
			item.setRutaDocumento((String)map.get("V_RUTA_DOCUMT"));			
			if (map.get("N_VERDOCU") != null) {
				item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());
			}
			if (map.get("N_RETDOCU") != null) {
				item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
			}

			if (map.get("N_ROBLI") != null) {
				item.setPeriodo(((BigDecimal)map.get("N_ROBLI")).longValue());
			}
			if (map.get("N_INDDOCDIG") != null) {
				item.setIndicadorDigital(((BigDecimal)map.get("N_INDDOCDIG")).longValue());
			}			
			if (map.get("V_JUSTDOCU") != null) {
				item.setJustificacion(((String)map.get("V_JUSTDOCU")));
			}
			if (map.get("V_NOARCHDOCU") != null) {
				item.setNombreArchivo(((String)map.get("V_NOARCHDOCU")));
			}
			item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));
			item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));
			item.setEstadoDercarga((String)map.get("V_ESTADESC"));
			item.setRaiz((String)map.get("V_RAIZDOCU"));
			
			if(map.get("N_DISDOCU")!=null) {
			item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCU")));
			}
			
			if(map.get("N_IDDOCUPADR")!=null) {
				Documento padre = new Documento(); 
				padre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
				item.setPadre(padre);
			}
			
			if(map.get("N_IDESTA")!=null) {
				Constante estado = new Constante();
				estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
				estado.setV_descons((String)map.get("V_NOESTA"));
				item.setEstado(estado);
			}
			
			if(map.get("N_IDPROC")!=null) {
				Jerarquia jproceso = new Jerarquia();
				//Constante proceso = new Constante();
				//proceso.setIdconstante(((BigDecimal)map.get("N_IDPROC")).longValue());
				jproceso.setIdJerarquia(((BigDecimal)map.get("N_IDPROC")).longValue());
				//proceso.setV_descons((String)map.get("V_NOPROC"));
				jproceso.setV_descons((String)map.get("V_NOPROC"));
				jproceso.setDescripcion((String)map.get("V_RUPROC"));
				//item.setProceso(proceso);
				item.setJproceso(jproceso);
			}
			
			if(map.get("N_IDALCASGI")!=null) {
				
				Jerarquia jalcance = new Jerarquia();
				//Constante alcance = new Constante();
				jalcance.setIdJerarquia(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				//alcance.setIdconstante(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				jalcance.setV_descons((String)map.get("V_NOALCA"));
				//alcance.setV_descons((String)map.get("V_NOALCA"));
				//item.setAlcanceSGI(alcance);
				jalcance.setDescripcion((String)map.get("V_RUALCA"));
				item.setJalcanceSGI(jalcance);
			}
			
			if(map.get("N_IDGEREGNRL")!=null) {
				Jerarquia jgerencia = new Jerarquia();
				//Constante gerencia = new Constante();
				jgerencia.setIdJerarquia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				//gerencia.setIdconstante(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				jgerencia.setV_descons((String)map.get("V_NOGERE"));
				//gerencia.setV_descons((String)map.get("V_NOGERE"));
				jgerencia.setDescripcion((String)map.get("V_RUGERE"));
				item.setJgerencia(jgerencia);
				//item.setGerencia(gerencia);
			}
			
			if(map.get("N_IDTIPODOCU")!=null) {
				Constante tipoDocumento = new Constante();
				tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
				tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
				item.setCtipoDocumento(tipoDocumento);
			}
			
			if(map.get("N_IDCODIANTE")!=null) {
				Codigo codigoAnterior = new Codigo();
				//codigoAnterior.setId(((BigDecimal)map.get("N_IDCODIANTE")).longValue());
				codigoAnterior.setCodigo((String)map.get("V_IDCODIANTE"));
				item.setCodigoAnterior(codigoAnterior);
			}
			
			if(map.get("N_IDMOTIREVI")!=null) {
				Constante motivoRevision = new Constante();
				motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
				motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
				item.setMotivoRevision(motivoRevision);
			}
			
			if(map.get("N_IDEMIS")!=null) {
				Colaborador emisor = new Colaborador();
				emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
				emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
				if(map.get("N_IDEQUIEMIS")!=null) {
					Equipo equipoEmisor = new Equipo();
					equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
					equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
					emisor.setEquipo(equipoEmisor);
				}
				item.setEmisor(emisor);
			}
			
			if(map.get("N_IDFASE")!=null) {
				Constante fase = new Constante();
				fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
				fase.setV_descons((String)map.get("V_NOFASE"));
				item.setFase(fase);
			}
			if(map.get("N_IDREVISION")!=null) {
				Revision revision = new Revision();
				revision.setId(((BigDecimal)map.get("N_IDREVISION")).longValue());
				revision.setNumero((map.get("N_NUMREVI")==null)?null:((BigDecimal)map.get("N_NUMREVI")).longValue());
				revision.setFecha((Date)map.get("D_FEREVI"));
				revision.setFechaAprobDocu((Date)map.get("d_fechaaprobdocu"));
				revision.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
				item.setRevision(revision);
				item.setIdrevision(String.valueOf(revision.getId()));
			}

			if(map.get("N_IDCARP")!=null) {
				Jerarquia fase = new Jerarquia();
				fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
				item.setNodo(fase);
			}

			if(map.get("N_IDCOPI")!=null) {
				Copia copia = new Copia();
				copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
				item.setCopia(copia);
			}
			
			if(map.get("N_IDCOOR")!=null) {
				Colaborador coordinador = new Colaborador();
				coordinador.setIdColaborador(((BigDecimal)map.get("N_IDCOOR")).longValue());
				coordinador.setNombreCompleto((String)map.get("V_NOCOOR"));
				item.setCoordinador(coordinador);
			}			
			
			listaDocumento.add(item);

			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
			
		}
		
		return listaDocumento;
	}	
	//Documento Revision	
	public Documento obtenerDocumentoRevisionDetalle(Long codigo, Long codigoRevision) {
		Map<String, Object> out	= null;
		Documento objeto		= null;		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_DOCU_REVISION_OBTENER")
			.declareParameters(
				new SqlParameter("i_niddoc", 			OracleTypes.NUMBER),	
				new SqlParameter("i_idrevi", 			OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_niddoc",codigo)
			.addValue("i_idrevi",codigoRevision);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
			objeto = this.mapearRevisionDocumento(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumentoCritica";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return objeto;
	}
	
	@SuppressWarnings("unchecked")
	private Documento mapearRevisionDocumento(Map<String, Object> resultados) {
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");	
		Documento item					= null;
		for(Map<String, Object> map : lista) {			
			item = new Documento();				    
			if(map.get("v_ruta_docu_c_cont")!=null) {	
				item.setRutacopiacontrolada((String)map.get("v_ruta_docu_c_cont"));				
			}
			if(map.get("v_coddocu")!=null) {	
				item.setVcoddocu((String)map.get("v_coddocu"));
			}
			if(map.get("v_desdocu")!=null) {				
				item.setVdesdocu((String)map.get("v_desdocu"));	
			}
			if(map.get("n_idgeregnrl")!=null) {				
				item.setIdjerageneral(((BigDecimal)map.get("n_idgeregnrl")).longValue());
			}
			if(map.get("v_rutagerencia")!=null) {	
				item.setRutagerencia((String)map.get("v_rutagerencia"));//ruta de gerencia 
			}
			if(map.get("v_rutalcance")!=null) {	
				item.setRutalcance((String)map.get("v_rutalcance"));//ruta de alcance
			}
			if(map.get("v_rutaproceso")!=null) {	
				item.setRutaproceso((String)map.get("v_rutaproceso"));//ruta de proeceso 
			}
			if(map.get("n_idalcasgi")!=null) {				
				item.setIdjeralc(((BigDecimal)map.get("n_idalcasgi")).longValue());
			}
			if(map.get("n_idproc")!=null) {				
				item.setIproce(((BigDecimal)map.get("n_idproc")).longValue());
			}
			if(map.get("n_numrevi")!=null) {				
				item.setNrevision(((BigDecimal)map.get("n_numrevi")).longValue());
			}
			if(map.get("n_estrevi")!=null) {				
				item.setNestrevision(((BigDecimal)map.get("n_estrevi")).longValue());
			}
			if(map.get("n_iddocu")!=null) {				
				item.setNidocu(((BigDecimal)map.get("n_iddocu")).longValue());
			}
			if(map.get("n_idrevi")!=null) {				
				item.setNidrevi(((BigDecimal)map.get("n_idrevi")).longValue());
			}
			if(map.get("D_FEAPRO")!=null) {				
				item.setFechaApro(((Date)map.get("D_FEAPRO")));
			}
			
		}
		return item;
	}	
	//Buscaqueda de documento por jerarquia
	@Override
    public List<Documento> obtenerDocumentoj(DocumentoRequest documentoRequest, PageRequest pageRequest, Long idjerarquia) {
		Map<String, Object> out	= null;
		List<Documento> lista		= new ArrayList<>();		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
          this.jdbcCall = new SimpleJdbcCall(this.jdbc)
      			.withSchemaName("AGI")
    			.withCatalogName("PCK_AGI_DOCUMENTO")
    			.withProcedureName("PRC_DOCUMENTO_OBTENER")
    			.declareParameters(
    				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
    				new SqlParameter("i_vcodigo", 		OracleTypes.VARCHAR),
    				new SqlParameter("i_vtitulo", 		OracleTypes.VARCHAR),
    				new SqlParameter("i_nestado", 		OracleTypes.NUMBER),
    				new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
    				new SqlParameter("i_nidjera",		OracleTypes.NUMBER),
    				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
    				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
    				new SqlParameter("i_vorden", 		OracleTypes.VARCHAR),						
    				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
    				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
    				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
    				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
    		
    		SqlParameterSource in =
    		new MapSqlParameterSource()
    			.addValue("i_nid",			documentoRequest.getId())
    			.addValue("i_vcodigo",		documentoRequest.getCodigo())
    			.addValue("i_vtitulo",		documentoRequest.getDescripcion())
    			.addValue("i_nestado",		documentoRequest.getEstado())
    			.addValue("i_ndisponible",	documentoRequest.getDisponible())
    			.addValue("i_nidjera",	documentoRequest.getIdjerarquia())
    			.addValue("i_npagina",		pageRequest.getPagina())
    			.addValue("i_nregistros",	pageRequest.getRegistros())
    			.addValue("i_vorden",		null);
          try {
                 out = this.jdbcCall.execute(in);
                 Integer resultado = (Integer)out.get("o_retorno");
                 if(resultado == 0) {
                        lista = mapearDocumento(out);                        
                 } else {
                        String mensaje = (String)out.get("o_mensaje");
                        String mensajeInterno = (String)out.get("o_sqlerrm");
                        this.error = new Error(resultado,mensaje,mensajeInterno);
                 }
          } catch(Exception e){
                 Integer resultado = (Integer)out.get("o_retorno");
                 String mensaje = (String)out.get("o_mensaje");
                 String mensajeInterno = (String)out.get("o_sqlerrm");
                 this.error = new Error(resultado,mensaje,mensajeInterno);
          }
          return lista;
    }
	@SuppressWarnings("unchecked")
	private List<Documento> mapearDocumento(Map<String, Object> resultados) {
		List<Documento> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Documento item					= null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new Documento();
			
			if (map.get("N_IDDOCU") != null)  item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
			if (map.get("V_CODDOCU") != null) item.setCodigo((String)map.get("V_CODDOCU"));
			if (map.get("V_DESDOCU") != null) item.setDescripcion((String)map.get("V_DESDOCU"));
			//Ruta del Documento Digital 
			if (map.get("V_RUTA_DOCU") != null) item.setRutaDocumento((String)map.get("V_RUTA_DOCU"));
			
			if((String)map.get("v_ruta_docu_c_obso")!=null) {item.setRutaDocumentoCopiaObso((String)map.get("v_ruta_docu_c_obso"));}
			if (map.get("v_idcodiante") != null) item.setCodigoAntiguo((String)map.get("v_idcodiante"));
			
			
			if (map.get("N_VERDOCU") != null) {
				item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());
			}	
			
			
			
			if (map.get("rutaoffice") != null) {			
				item.setRutaDocumentoOffice(((String)map.get("rutaoffice")));			
				}
			
			
			if (map.get("DOCUORIGIN") != null) {			
				item.setRutaDocumentoOriginal(((String)map.get("DOCUORIGIN")));			
				}
			if (map.get("rutacontrolada") != null) {			
				item.setRutaDocumentoCopiaCont(((String)map.get("rutacontrolada")));			
				}
			if (map.get("nocontrolado") != null) {			
				item.setRutaDocumentoCopiaNoCont(((String)map.get("nocontrolado")));			
				}
			if (map.get("obsoleta") != null) {			
				item.setRutaDocumentoCopiaObso(((String)map.get("obsoleta")));			
				}
			if (map.get("N_RETDOCU") != null) {
				item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
			}

			if (map.get("N_ROBLI") != null) {
				item.setPeriodo(((BigDecimal)map.get("N_ROBLI")).longValue());
			}
			if (map.get("N_INDDOCDIG") != null) {
				item.setIndicadorDigital(((BigDecimal)map.get("N_INDDOCDIG")).longValue());
			}			
			if (map.get("V_JUSTDOCU") != null) {
				item.setJustificacion(((String)map.get("V_JUSTDOCU")));
			}
			if (map.get("V_NOARCHDOCU") != null) {
				item.setNombreArchivo(((String)map.get("V_NOARCHDOCU")));
			}
			item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));
			item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));
			item.setEstadoDercarga((String)map.get("V_ESTADESC"));
			item.setRaiz((String)map.get("V_RAIZDOCU"));
			
			if(map.get("N_DISDOCU")!=null) {
			item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCU")));
			}
			
			if(map.get("N_IDDOCUPADR")!=null) {
				Documento padre = new Documento(); 
				padre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
				item.setPadre(padre);
			}
			
			if(map.get("N_IDESTA")!=null) {
				Constante estado = new Constante();
				estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
				estado.setV_descons((String)map.get("V_NOESTA"));
				item.setEstado(estado);
			}
			
			if(map.get("N_IDPROC")!=null) {
				Jerarquia jproceso = new Jerarquia();
				//Constante proceso = new Constante();
				//proceso.setIdconstante(((BigDecimal)map.get("N_IDPROC")).longValue());
				jproceso.setIdJerarquia(((BigDecimal)map.get("N_IDPROC")).longValue());
				//proceso.setV_descons((String)map.get("V_NOPROC"));
				jproceso.setV_descons((String)map.get("V_NOPROC"));
				jproceso.setDescripcion((String)map.get("V_RUPROC"));
				//item.setProceso(proceso);
				item.setJproceso(jproceso);
			}
			
			if(map.get("N_IDALCASGI")!=null) {
				
				Jerarquia jalcance = new Jerarquia();
				//Constante alcance = new Constante();
				jalcance.setIdJerarquia(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				//alcance.setIdconstante(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				jalcance.setV_descons((String)map.get("V_NOALCA"));
				//alcance.setV_descons((String)map.get("V_NOALCA"));
				//item.setAlcanceSGI(alcance);
				jalcance.setDescripcion((String)map.get("V_RUALCA"));
				item.setJalcanceSGI(jalcance);
			}
			
			if(map.get("N_IDGEREGNRL")!=null) {
				Jerarquia jgerencia = new Jerarquia();				
				jgerencia.setIdJerarquia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				//gerencia.setIdconstante(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				jgerencia.setV_descons((String)map.get("V_NOGERE"));
				//gerencia.setV_descons((String)map.get("V_NOGERE"));
				jgerencia.setDescripcion((String)map.get("V_RUGERE"));
				//cguerra INICIO		
				if(map.get("n_inddesc")!=null) {
					jgerencia.setIndicadorDescargas(((BigDecimal)map.get("n_inddesc")).longValue());	
				}
				
				//cguerra FIN
				item.setJgerencia(jgerencia);
				//item.setGerencia(gerencia);
			}			
			if(map.get("N_IDTIPODOCU")!=null) {
				Constante tipoDocumento = new Constante();
				if(map.get("N_IDTIPODOCU")!=null) tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
				if(map.get("V_NOTIPODOCU")!=null) tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
				item.setCtipoDocumento(tipoDocumento);
			}		
			
			if(map.get("N_IDMOTIREVI")!=null) {
				Constante motivoRevision = new Constante();
				motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
				motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
				item.setMotivoRevision(motivoRevision);
			}
			
			if(map.get("N_IDEMIS")!=null) {
				Colaborador emisor = new Colaborador();
				emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
				emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
				if(map.get("N_IDEQUIEMIS")!=null) {
					Equipo equipoEmisor = new Equipo();
					equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
					equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
					emisor.setEquipo(equipoEmisor);
				}
				item.setEmisor(emisor);
			}
			
			if(map.get("N_IDFASE")!=null) {
				Constante fase = new Constante();
				fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
				fase.setV_descons((String)map.get("V_NOFASE"));
				item.setFase(fase);
			}
			
			if(map.get("N_IDREVI")!=null) {
				Revision revision = new Revision();
				revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
				revision.setNumero((map.get("N_NUMREVI")==null)?null:((BigDecimal)map.get("N_NUMREVI")).longValue());
				revision.setFecha((Date)map.get("D_FEREVI"));
				revision.setFechaAprobacion((Date)map.get("V_FECHAPROBACION"));
				//cguerra
				revision.setFechacancelacion((Date)map.get("V_FECHAPROBACION"));
				
				
				if(map.get("N_NUMITER")!=null) {
					revision.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());	
				}
				revision.setFechaAprobacionDocumento((Date)map.get("D_FEAPRODOCU"));
				revision.setUsuarioAprobacionDocumento((String)map.get("V_USAPRODOCU"));
				revision.setNumeroAnterior((map.get("N_NUMREVIANTE")==null)?null:((BigDecimal)map.get("N_NUMREVIANTE")).longValue());
				revision.setFechaAprobacionAnterior((Date)map.get("D_FEAPROANTE"));
				item.setRevision(revision);
				item.setIdrevision(String.valueOf(revision.getId()));
			}

			if(map.get("N_IDCARP")!=null) {
				Jerarquia fase = new Jerarquia();
				fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
				item.setNodo(fase);
			}

			if(map.get("N_IDCOPI")!=null) {
				Copia copia = new Copia();
				copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
				item.setCopia(copia);
			}
			
			Colaborador coordinador = new Colaborador();
			if(map.get("N_IDCOOR")!=null) {
				coordinador.setIdColaborador(((BigDecimal)map.get("N_IDCOOR")).longValue());
				coordinador.setNombreCompleto((String)map.get("V_NOCOOR"));
			}
			item.setCoordinador(coordinador);

			listaDocumento.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
			
			
		}
		
		return listaDocumento;
	}
	
	public Documento obtenerDocumentoHistorial(Long codigo) {
		Map<String, Object> out	= null;
		Documento objeto		= null;		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_DOCUMENTOHIST_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",	codigo);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				objeto = this.mapearDocumentoHistorial(out);
				
				ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
				constanteDao.setJdbc(this.jdbc);
				
				ConstanteRequest constante = new ConstanteRequest();
				constante.setPadre(TipoConstante.ETAPA_RUTA.toString());
				
				PageRequest pagina = new PageRequest();
				pagina.setPagina(1);
				pagina.setRegistros(1000);
				
				List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
				
				Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
				objeto.setParticipanteElaboracion(this.obtenerParticipanteHistorico(codigo,faseElaboracion,pagina));
				
				Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
				objeto.setParticipanteConsenso(this.obtenerParticipanteHistorico(codigo,faseConsenso,pagina));
				
				Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
				objeto.setParticipanteAprobacion(this.obtenerParticipanteHistorico(codigo,faseAprobacion,pagina));
				
				Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
				objeto.setParticipanteHomologacion(this.obtenerParticipanteHistorico(codigo,faseHomologacion,pagina));
				
				objeto.setListaComplementario(this.obtenerComplementarioHistorico(codigo, pagina));
				objeto.setListaEquipo(this.obtenerEquipoHistorico(codigo));
				
				RevisionDAOImpl revisionDao = new RevisionDAOImpl();
				revisionDao.setJdbc(this.jdbc);
				
				RevisionRequest revision = new RevisionRequest();
				revision.setIdDocumento(objeto.getId());
				revision.setNumero((objeto.getRevision()==null)?0:objeto.getRevision().getNumero());
				objeto.setListaRevision(revisionDao.obtenerRevisionHistorico(revision, pagina));
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumentoHistorial";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return objeto;
	}
	
	@SuppressWarnings("unchecked")
	private Documento mapearDocumentoHistorial(Map<String, Object> resultados) {
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Documento item					= null;
		
		for(Map<String, Object> map : lista) {			
			item = new Documento();
			item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
			item.setCodigo((String)map.get("V_CODDOCU"));
			item.setDescripcion((String)map.get("V_DESDOCU"));
			//Ruta del Docume en el FileServe
			item.setRutaDocumento((String)map.get("V_RUTA_DOCUMT"));
			item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());
			item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
			item.setPeriodo(((BigDecimal)map.get("N_PERIOBLI")).longValue());
			item.setJustificacion((String)map.get("V_JUSTDOCU"));
			item.setNombreArchivo((String)map.get("V_NOARCHDOCU"));
			item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));
			item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));
			item.setEstadoDercarga((String)map.get("V_ESTADESC"));
			item.setRaiz((String)map.get("V_RAIZDOCU"));
			item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCHIS")));

			if(map.get("N_IDDOCUPADR")!=null) {
				Documento padre = new Documento(); 
				padre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
				item.setPadre(padre);
			}
			
			if(map.get("N_IDESTA")!=null) {
				Constante estado = new Constante();
				estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
				estado.setV_descons((String)map.get("V_NOESTA"));
				item.setEstado(estado);
			}
			
			if(map.get("N_IDPROC")!=null) {
				Jerarquia jproceso = new Jerarquia();
				//Constante proceso = new Constante();
				//proceso.setIdconstante(((BigDecimal)map.get("N_IDPROC")).longValue());
				jproceso.setIdJerarquia(((BigDecimal)map.get("N_IDPROC")).longValue());
				//proceso.setV_descons((String)map.get("V_NOPROC"));
				jproceso.setV_descons((String)map.get("V_NOPROC"));
				//item.setProceso(proceso);
				item.setJproceso(jproceso);
			}
			
			if(map.get("N_IDALCASGI")!=null) {
				Jerarquia jalcance = new Jerarquia();
				//Constante alcance = new Constante();
				jalcance.setIdJerarquia(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				//alcance.setIdconstante(((BigDecimal)map.get("N_IDALCASGI")).longValue());
				jalcance.setV_descons((String)map.get("V_NOALCA"));
				//alcance.setV_descons((String)map.get("V_NOALCA"));
				//item.setAlcanceSGI(alcance);
				item.setJalcanceSGI(jalcance);
			}
			
			if(map.get("N_IDGEREGNRL")!=null) {
				Jerarquia jgerencia = new Jerarquia();
				//Constante gerencia = new Constante();
				jgerencia.setIdJerarquia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				//gerencia.setIdconstante(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
				jgerencia.setV_descons((String)map.get("V_NOGERE"));
				//gerencia.setV_descons((String)map.get("V_NOGERE"));
				item.setJgerencia(jgerencia);
				//item.setGerencia(gerencia);
			}
			
			if(map.get("N_IDTIPODOCU")!=null) {
				Constante tipoDocumento = new Constante();
				tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
				tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
				item.setCtipoDocumento(tipoDocumento);
			}
			
			if(map.get("V_IDCODIANTE")!=null) {
				Codigo codigoAnterior = new Codigo();
				//codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
				codigoAnterior.setCodigo((String)map.get("V_IDCODIANTE"));
				item.setCodigoAnterior(codigoAnterior);
			}
			
			if(map.get("N_IDMOTIREVI")!=null) {
				Constante motivoRevision = new Constante();
				motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
				motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
				item.setMotivoRevision(motivoRevision);
			}
			
			if(map.get("N_IDEMIS")!=null) {
				Colaborador emisor = new Colaborador();
				emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
				emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
				if(map.get("N_IDEMIS")!=null) {
					Equipo equipoEmisor = new Equipo();
					equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
					equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
					emisor.setEquipo(equipoEmisor);
				}
				item.setEmisor(emisor);
			}
			
			if(map.get("N_IDFASE")!=null) {
				Constante fase = new Constante();
				fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
				fase.setV_descons((String)map.get("V_NOFASE"));
				item.setFase(fase);
			}
			
			if(map.get("N_IDREVI")!=null) {
				Revision revision = new Revision();
				revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
				revision.setFecha((Date)map.get("D_FEREVI"));
				item.setRevision(revision);
			}

			if(map.get("N_IDCARP")!=null) {
				Jerarquia fase = new Jerarquia();
				fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
				item.setNodo(fase);
			}

			if(map.get("N_IDCOPI")!=null) {
				Copia copia = new Copia();
				copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
				item.setCopia(copia);
			}

		}
		return item;
	}
	
	private Boolean registrarDocumentoHistorico(Long codigo, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_DOCUMENTOHIST_GUARDAR")
			.declareParameters(
				new SqlParameter("i_nid",			OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",		codigo)
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
			String mensaje			= "Error en DocumentoDAOImpl.registrarDocumentoHistorico";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}
		
	@Override
	public List<Codigo> obtenerCodigoAnterior(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Codigo> lista		= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_CODIGOANTERIOR_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			documentoRequest.getId())
			.addValue("i_npagina",		pageRequest.getPagina())
			.addValue("i_nregistros",	pageRequest.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearCodigo(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerCodigoAnterior";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return lista;
	}

	@SuppressWarnings("unchecked")
	private List<Codigo> mapearCodigo(Map<String, Object> resultados) {
		List<Codigo> listaCodigo	 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Codigo item						= null;
		
		for(Map<String, Object> map : lista) {			
			item = new Codigo();
			item.setId(((BigDecimal)map.get("N_IDCODIHIST")).longValue());
			item.setCodigo((String)map.get("V_CODDOCU"));
			item.setMotivo((String)map.get("V_NOMOTIREVI"));
			item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
			listaCodigo.add(item);

			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		return listaCodigo;
	}
	
	@SuppressWarnings("unused")
	private Boolean registrarCodigoAnterior(Long documento, String codigo, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_CODIGOANTERIOR_GUARDAR")
			.declareParameters(
				new SqlParameter("i_nid",			OracleTypes.NUMBER),
				new SqlParameter("i_vcodigo",		OracleTypes.VARCHAR),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",		documento)
			.addValue("i_vcodigo",	codigo)
			.addValue("i_vusuario",	usuario);

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			//Cerramos conexion JDBC
			this.jdbc.getDataSource().getConnection().close();
			
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
			String mensaje			= "Error en DocumentoDAOImpl.registrarCodigoAnterior";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}
	
	@Override
	public Documento obtenerDocumentoDetalle(Long codigo) {
		Documento objeto= null;
		this.error		= null;

		try {
			objeto = new Documento();
			
			ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
			constanteDao.setJdbc(this.jdbc);
			
			ConstanteRequest constante = new ConstanteRequest();
			constante.setPadre(TipoConstante.ETAPA_RUTA.toString());
			
			PageRequest pagina = new PageRequest();
			pagina.setPagina(1);
			pagina.setRegistros(1000);
			
			DocumentoRequest documentoRequest = new DocumentoRequest();
			documentoRequest.setId(codigo);
			List<Documento> documentos = obtenerDocumento(documentoRequest,pagina);
			
			if(this.error!=null) return null;
			objeto = documentos.get(0);
			
			RevisionDAOImpl revisionDao = new RevisionDAOImpl();
			revisionDao.setJdbc(this.jdbc);
			RevisionRequest revision = new RevisionRequest();
			revision.setIdDocumento(codigo);
			List<Revision> listaRevision = revisionDao.obtenerRevisionHistorico(revision, pagina);
			if(revisionDao.getError()!=null) {
				this.error = revisionDao.getError();
				return null;
			}
			objeto.setListaRevision(listaRevision);
			Long idRevision = objeto.getRevision().getId();
			
			for(Revision rev:listaRevision) {
				if(rev.getId().equals(idRevision)) {
					objeto.setRevision(rev);
					break;
				}
			}
			
			List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
			
			Flujo flujo = new Flujo();
			flujo.setIdDocumento(objeto.getId());
			flujo.setIdRevision(objeto.getRevision().getId());
			flujo.setIteracion(objeto.getRevision().getIteracion());
			List<Flujo> listaFlujo = this.obtenerFlujo(flujo);
			if(this.error!=null) return null;
			
			Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			objeto.setParticipanteElaboracion(this.obtenerParticipante(codigo,faseElaboracion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseElaboracion(this.obtenerFase(listaFlujo,faseElaboracion));
			
			Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			objeto.setParticipanteConsenso(this.obtenerParticipante(codigo,faseConsenso,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseConsenso(this.obtenerFase(listaFlujo,faseConsenso));
			
			Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			objeto.setParticipanteAprobacion(this.obtenerParticipante(codigo,faseAprobacion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseAprobacion(this.obtenerFase(listaFlujo,faseAprobacion));
			
			Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			objeto.setParticipanteHomologacion(this.obtenerParticipante(codigo,faseHomologacion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseHomologacion(this.obtenerFase(listaFlujo,faseHomologacion));
			
			objeto.setBitacora(new Flujo());
			objeto.getBitacora().setLista(this.obtenerParticipanteBitacora(codigo,null,
					objeto.getRevision().getId(),null,pagina));
			if(this.error!=null) return null;
			
			objeto.setListaComplementario(this.obtenerComplementario(codigo, objeto.getRevision().getId(), pagina));
			if(this.error!=null) return null;
			
			objeto.setListaEquipo(this.obtenerEquipo(codigo,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			//Cerramos conexion JDBC
			this.jdbc.getDataSource().getConnection().close();
			
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumentoDetalle";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return objeto;
	}
	
	@Override
	public Documento obtenerDocumentoHistorialRev(Long codigo, Long idRev) {
		Documento objeto= null;
		this.error		= null;

		try {
			objeto = new Documento();			
			ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
			constanteDao.setJdbc(this.jdbc);			
			ConstanteRequest constante = new ConstanteRequest();
			constante.setPadre(TipoConstante.ETAPA_RUTA.toString());			
			PageRequest pagina = new PageRequest();
			pagina.setPagina(1);
			pagina.setRegistros(1000);
			
			DocumentoRequest documentoRequest = new DocumentoRequest();
			documentoRequest.setId(codigo);
			documentoRequest.setIdrevison(idRev);
			List<Documento> documentos = obtenerDocumentoHisto(documentoRequest,pagina);////////
			if(this.error!=null) return null;
			objeto = documentos.get(0);
			
			RevisionDAOImpl revisionDao = new RevisionDAOImpl();
			revisionDao.setJdbc(this.jdbc);
			
			RevisionRequest revision = new RevisionRequest();
			revision.setIdDocumento(codigo);
			revision.setId(idRev);
			List<Revision> listaRevision = revisionDao.obtenerRevisionHistorico(revision, pagina);
			if(revisionDao.getError()!=null) return null;
			objeto.setListaRevision(listaRevision);
			Long idRevision = objeto.getRevision().getId();
			
			for(Revision rev:listaRevision) {
				if(rev.getId().equals(idRevision)) {
					objeto.setRevision(rev);
					break;
				}
			}
			
			List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
			
			Flujo flujo = new Flujo();
			flujo.setIdDocumento(objeto.getId());
			flujo.setIdRevision(objeto.getRevision().getId());
			flujo.setIteracion(objeto.getRevision().getIteracion());
			List<Flujo> listaFlujo = this.obtenerFlujo(flujo);
			if(this.error!=null) return null;
			
			Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			objeto.setParticipanteElaboracion(this.obtenerParticipanteAll(codigo,faseElaboracion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseElaboracion(this.obtenerFase(listaFlujo,faseElaboracion));
			
			Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			objeto.setParticipanteConsenso(this.obtenerParticipanteAll(codigo,faseConsenso,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseConsenso(this.obtenerFase(listaFlujo,faseConsenso));
			
			Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			objeto.setParticipanteAprobacion(this.obtenerParticipanteAll(codigo,faseAprobacion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseAprobacion(this.obtenerFase(listaFlujo,faseAprobacion));
			
			Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			objeto.setParticipanteHomologacion(this.obtenerParticipanteAll(codigo,faseHomologacion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseHomologacion(this.obtenerFase(listaFlujo,faseHomologacion));
			
			objeto.setBitacora(new Flujo());
			objeto.getBitacora().setLista(this.obtenerParticipanteAll(codigo,null,
					objeto.getRevision().getId(),null,pagina));
			if(this.error!=null) return null;
			
			objeto.setListaComplementario(this.obtenerComplementario(codigo, objeto.getRevision().getId(), pagina));
			if(this.error!=null) return null;
			
			objeto.setListaEquipo(this.obtenerEquipo(codigo,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumentoDetalle";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return objeto;
	}
	
	/* Solicitud  */
	@Override
	public Documento obtenerDocumentoSolicitudDetalle(Long codigo,Long codigoSolicitud,Long codigoRevision) {
		Documento objeto= null;
		this.error		= null;

		try {
			objeto = new Documento();
			
			ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
			constanteDao.setJdbc(this.jdbc);
			
			ConstanteRequest constante = new ConstanteRequest();
			constante.setPadre(TipoConstante.ETAPA_RUTA.toString());
			
			PageRequest pagina = new PageRequest();
			pagina.setPagina(1);
			pagina.setRegistros(1000);
			
			DocumentoRequest documentoRequest = new DocumentoRequest();
			documentoRequest.setId(codigo);
			
			//Obtener Documento Revisión
			List<Documento> documentos = obtenerDocumentoRevision(documentoRequest, codigoRevision,pagina);
			
			
			if(this.error!=null) return null;
			objeto = documentos.get(0);
			
			RevisionDAOImpl revisionDao = new RevisionDAOImpl();
			revisionDao.setJdbc(this.jdbc);
			
			RevisionRequest revision = new RevisionRequest();
			revision.setIdDocumento(codigo);
			List<Revision> listaRevision = revisionDao.obtenerRevisionHistorico(revision, pagina);
			if(this.error!=null) return null;
			objeto.setListaRevision(listaRevision);
			Long idRevision = objeto.getRevision().getId();
			
			for(Revision rev:listaRevision) {
				if(rev.getId().equals(idRevision)) {
					objeto.setRevision(rev);
					break;
				}
			}
			
			List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
			
			Flujo flujo = new Flujo();
			flujo.setIdDocumento(objeto.getId());
			flujo.setIdRevision(objeto.getRevision().getId());
			flujo.setIteracion(objeto.getRevision().getIteracion());
			List<Flujo> listaFlujo = this.obtenerFlujo(flujo);
			if(this.error!=null) return null;
			
			Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			objeto.setParticipanteElaboracion(this.obtenerParticipante(codigo,faseElaboracion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseElaboracion(this.obtenerFase(listaFlujo,faseElaboracion));
			
			Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			objeto.setParticipanteConsenso(this.obtenerParticipante(codigo,faseConsenso,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseConsenso(this.obtenerFase(listaFlujo,faseConsenso));
			
			Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			objeto.setParticipanteAprobacion(this.obtenerParticipante(codigo,faseAprobacion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseAprobacion(this.obtenerFase(listaFlujo,faseAprobacion));
			
			Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			objeto.setParticipanteHomologacion(this.obtenerParticipante(codigo,faseHomologacion,
					objeto.getRevision().getId(),objeto.getRevision().getIteracion(),pagina));
			if(this.error!=null) return null;
			objeto.setFaseHomologacion(this.obtenerFase(listaFlujo,faseHomologacion));
			
			objeto.setBitacora(new Flujo());
			objeto.getBitacora().setLista(this.obtenerParticipante(codigo,null,
					objeto.getRevision().getId(),null,pagina));
			if(this.error!=null) return null;
			
			objeto.setListaComplementario(this.obtenerComplementario(codigo, objeto.getRevision().getId(), pagina));
			if(this.error!=null) return null;
			
			objeto.setListaEquipo(this.obtenerEquipo(codigo,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			
			/*Solicitud */
			//Obtener result de la pestaña Bitacora
			objeto.setListaRevision(this.obtenerSolicitud(codigo,null));
			if(this.error!=null) return null;		
			 
			objeto.setCriticaporDocumento(this.obtenerCriticaPorDocumento(codigo,codigoSolicitud));
			if(this.error!=null) return null;
			//Revision 
			if(codigoRevision !=null && codigoSolicitud!=null ) {
				codigoRevision =null;
			}
			
			if(codigoRevision !=null ) { 
				objeto = (this.obtenerDocumentoRevisionDetalle(codigo,codigoRevision));
				if(this.error!=null) return null;	
			}		
			/* Solicitud   */ 
			
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumentoSolicitudDetalle";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return objeto;
	}
	
	private Long obtenerIdConstante(List<Constante> lista, String nombre) {
		for(Constante item: lista) {
			if(item.getV_valcons().equals(nombre)) {
				return item.getIdconstante();
			}
		}
		return Long.valueOf("0");
	}
/*Solicitud lista la pestaña BITACORA*/
	@SuppressWarnings("unchecked")
	public List<Revision> obtenerSolicitud(Long codigo,  Long idUsuario) {
		//Date fechaRevision = null;		
		SqlParameterSource in = null;
		Map<String, Object> out	= null;
		List<Revision> lista	= null;
		this.paginacion = new Paginacion();
		//paginacion.setPagina(pageRequest.getPagina());
		//paginacion.setRegistros(pageRequest.getRegistros());		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_SOLICITUD_COPIA_OBTENER")
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
				.addValue("i_niddocu", 		codigo)//revisionRequest.getIdDocumento()) 
				.addValue("i_tipocopi", 		null)
				.addValue("i_vcoddocument", 		null)
				.addValue("i_vtitulodocum", 		null)
				.addValue("i_npagina",			1)
				.addValue("i_nregistros",		10000);		 
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
		//Integer totalRegistro = 0;
		for(Map<String, Object> map : listado) {			
			item = new Revision();
						
			if(map.get("v_destinatarioape")!=null) {
				item.setNomapellidoparterDestina((String)map.get("v_destinatarioape"));
			}			
			if(map.get("v_ruta_docu_c_cont")!=null) {
				item.setRutaDocumentoCopiaCont((String)map.get("v_ruta_docu_c_cont"));
				
			}
			if(map.get("N_NUMREVI")!=null) {
				item.setNumero(map.get("N_NUMREVI")!=null?((BigDecimal)map.get("N_NUMREVI")).longValue():null);
			}			
			if(map.get("V_NOMREVI")!=null) {
				item.setTitulo((String)map.get("V_NOMREVI"));
				
			}
			if(map.get("V_NOMREVI")!=null) {
				item.setTitulo((String)map.get("V_NOMREVI"));
				
			}
			if(map.get("n_motcopi")!=null) {
				item.setN_motivo(((BigDecimal)map.get("n_motcopi")).longValue());
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
			
			if(map.get("idfase")!=null) {
				item.setIdususoli(((BigDecimal)map.get("idfase")).longValue());			
			}
			//cguerra
			if(map.get("D_FEAPRO")!=null) {
				item.setFechacancelacion((Date)map.get("D_FEAPRO"));
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
			/*if(map.get("RESULT_COUNT")!= null) {
				totalRegistro = ((BigDecimal)map.get("RESULT_COUNT")).intValue();
			}*/
			
			if(map.get("N_IDDOCGOOGLEDRIVE")!=null) {
				item.setIdDocGoogleDrive((String)map.get("N_IDDOCGOOGLEDRIVE"));
			}
		}
		/*if (this.paginacion != null && totalListado>0) {
			this.paginacion.setTotalRegistros(totalRegistro);
		} else {
			this.paginacion.setTotalRegistros(0);
		}*/
		return mLista;
	}
	/* Obtener Critica x Documento */	
	public SolicitudCopia obtenerCriticaPorDocumento(Long codigo, Long codigoSolicitud) {
		Map<String, Object> out	= null;
		SolicitudCopia objeto		= null;		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_CRITICA_DOC_OBTENER")
			.declareParameters(
				new SqlParameter("i_niddoc", 			OracleTypes.NUMBER),	
				new SqlParameter("i_idcopi", 			OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_niddoc",codigo)
			.addValue("i_idcopi",codigoSolicitud);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				objeto = this.mapearCritica(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumentoCritica";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return objeto;
	}
	
	@SuppressWarnings("unchecked")
	private SolicitudCopia mapearCritica(Map<String, Object> resultados) {
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");	
		SolicitudCopia item					= null;		
		for(Map<String, Object> map : lista) {			
			item = new SolicitudCopia();
			
			if(map.get("v_comentcritica")!=null) {	
				item.setResumenCritica((String)map.get("v_comentcritica"));
			}
			if(map.get("v_obscopi")!=null) {				
				item.setObserva((String)map.get("v_obscopi"));	
			}
			if(map.get("n_motcopi")!=null) {				
				item.setNmotivo(((BigDecimal)map.get("n_motcopi")).longValue());
			}
			if(map.get("n_tipcopi")!=null) {				
				item.setNumtipoestasoli(((BigDecimal)map.get("n_tipcopi")).longValue());
			}
			if(map.get("n_estcopi")!=null) {				
				item.setNestcopi(((BigDecimal)map.get("n_estcopi")).longValue());
			}
			
		}
		return item;
	}
	 
	@SuppressWarnings("unchecked")
	private List<Colaborador> obtenerParticipanteBitacora(Long codigo, Long fase,
			Long revision, Long iteracion, PageRequest pagina) {
		
		Map<String, Object> out	= null;
		List<Colaborador> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_PARTICIPANTE_OBTENER_BIT")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
				new SqlParameter("i_nfase", 		OracleTypes.VARCHAR),
				new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
				new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			codigo)
			.addValue("i_nfase",		fase)
			.addValue("i_nrevision",	revision)
			.addValue("i_niteracion",	iteracion)
			.addValue("i_npagina",		pagina.getPagina())
			.addValue("i_nregistros",	pagina.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {			
					Colaborador item = new Colaborador();
					if(map.get("N_IDCOLA")!=null) item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
					if(map.get("N_IDREVI")!=null) item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					if(map.get("N_NUMITER")!=null) item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
					if(map.get("V_NOCOLA")!=null) item.setNombreCompleto((String)map.get("V_NOCOLA"));
					if(map.get("V_NOFUNC")!=null) item.setFuncion((String)map.get("V_NOFUNC"));
					if(map.get("N_CICLPART")!=null) item.setCiclo((map.get("N_CICLPART")==null)?null:((BigDecimal)map.get("N_CICLPART")).longValue());
					if(map.get("V_COMEPART")!=null) item.setComentario((String)map.get("V_COMEPART"));
					if(map.get("N_CANTPLAZ")!=null) item.setPlazo(((BigDecimal)map.get("N_CANTPLAZ")==null)?null:((BigDecimal)map.get("N_CANTPLAZ")).longValue());
					if(map.get("D_PLAZPART")!=null) item.setFechaPlazo((Date)map.get("D_PLAZPART"));
					//if(map.get("D_LIBEPART")!=null) item.setFechaLiberacion((Date)map.get("D_LIBEPART"));
					item.setFechaLiberacion((Date)map.get("D_LIBEPART")==null?(Date)map.get("D_RECHPART"):(Date)map.get("D_LIBEPART"));
					if(map.get("N_DISPART")!=null) item.setDisponible(((BigDecimal)map.get("N_DISPART")).longValue());
					if(map.get("N_PRIO")!=null) item.setPrioridad((map.get("N_PRIO")==null)?null:((BigDecimal)map.get("N_PRIO")).longValue());
					if(map.get("V_NOCOLA")!=null) item.setResponsable((String)map.get("V_NOCOLA"));
					if(map.get("N_IDFASE")!=null) item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					if(map.get("V_NOFASE")!=null) item.setNombreFase((String)map.get("V_NOFASE"));
					if(map.get("N_INDRECH")!=null) item.setIndicadorRechazo((map.get("N_INDRECH")==null)?null:((BigDecimal)map.get("N_INDRECH")).longValue());
					if(map.get("D_RECHPART")!=null) item.setFechaRechazo((Date)map.get("D_RECHPART"));
					if(map.get("N_IDTRAB")!=null) item.setIdTrabajador(map.get("N_IDTRAB")==null?null:((BigDecimal)map.get("N_IDTRAB")).longValue());
					if(map.get("N_INDTRAB")!=null) item.setIndicadorTrabajador(((BigDecimal)map.get("N_INDTRAB")).longValue());
					if(map.get("N_INDEQUI")!=null) item.setIndicadorEquipo(((BigDecimal)map.get("N_INDEQUI")).longValue());
					if(map.get("N_INDFUNC")!=null) item.setIndicadorFuncion(((BigDecimal)map.get("N_INDFUNC")).longValue());
					if(map.get("N_INDJEFE")!=null) item.setIndicadorJefe(((BigDecimal)map.get("N_INDJEFE")).longValue());
					if(map.get("N_INDJEFACT")!=null) item.setIndicadorJefeActual(((BigDecimal)map.get("N_INDJEFACT")).longValue());if(map.get("N_IDCOLA")!=null) item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
					if(map.get("N_IDREVI")!=null) item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					if(map.get("N_NUMITER")!=null) item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
					if(map.get("V_NOCOLA")!=null) item.setNombreCompleto((String)map.get("V_NOCOLA"));
					if(map.get("V_NOFUNC")!=null) item.setFuncion((String)map.get("V_NOFUNC"));
					if(map.get("N_CICLPART")!=null) item.setCiclo((map.get("N_CICLPART")==null)?null:((BigDecimal)map.get("N_CICLPART")).longValue());
					if(map.get("V_COMEPART")!=null) item.setComentario((String)map.get("V_COMEPART"));
					if(map.get("N_CANTPLAZ")!=null) item.setPlazo(((BigDecimal)map.get("N_CANTPLAZ")==null)?null:((BigDecimal)map.get("N_CANTPLAZ")).longValue());
					if(map.get("D_PLAZPART")!=null) item.setFechaPlazo((Date)map.get("D_PLAZPART"));
					item.setFechaLiberacion((Date)map.get("D_LIBEPART")==null?(Date)map.get("D_RECHPART"):(Date)map.get("D_LIBEPART"));
					if(map.get("N_DISPART")!=null) item.setDisponible(((BigDecimal)map.get("N_DISPART")).longValue());
					if(map.get("N_PRIO")!=null) item.setPrioridad((map.get("N_PRIO")==null)?null:((BigDecimal)map.get("N_PRIO")).longValue());
					if(map.get("V_NOCOLA")!=null) item.setResponsable((String)map.get("V_NOCOLA"));
					if(map.get("N_IDFASE")!=null) item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					if(map.get("V_NOFASE")!=null) item.setNombreFase((String)map.get("V_NOFASE"));
					if(map.get("N_INDRECH")!=null) item.setIndicadorRechazo((map.get("N_INDRECH")==null)?null:((BigDecimal)map.get("N_INDRECH")).longValue());
					if(map.get("D_RECHPART")!=null) item.setFechaRechazo((Date)map.get("D_RECHPART"));
					if(map.get("N_IDTRAB")!=null) item.setIdTrabajador(map.get("N_IDTRAB")==null?null:((BigDecimal)map.get("N_IDTRAB")).longValue());
					if(map.get("N_INDTRAB")!=null) item.setIndicadorTrabajador(((BigDecimal)map.get("N_INDTRAB")).longValue());
					if(map.get("N_INDEQUI")!=null) item.setIndicadorEquipo(((BigDecimal)map.get("N_INDEQUI")).longValue());
					if(map.get("N_INDFUNC")!=null) item.setIndicadorFuncion(((BigDecimal)map.get("N_INDFUNC")).longValue());
					if(map.get("N_INDJEFE")!=null) item.setIndicadorJefe(((BigDecimal)map.get("N_INDJEFE")).longValue());
					if(map.get("N_INDJEFACT")!=null) item.setIndicadorJefeActual(((BigDecimal)map.get("N_INDJEFACT")).longValue());if(map.get("N_IDCOLA")!=null) item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
					if(map.get("N_IDREVI")!=null) item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					if(map.get("N_NUMITER")!=null) item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
					if(map.get("V_NOCOLA")!=null) item.setNombreCompleto((String)map.get("V_NOCOLA"));
					if(map.get("V_NOFUNC")!=null) item.setFuncion((String)map.get("V_NOFUNC"));
					if(map.get("N_CICLPART")!=null) item.setCiclo((map.get("N_CICLPART")==null)?null:((BigDecimal)map.get("N_CICLPART")).longValue());
					if(map.get("V_COMEPART")!=null) item.setComentario((String)map.get("V_COMEPART"));
					if(map.get("N_CANTPLAZ")!=null) item.setPlazo(((BigDecimal)map.get("N_CANTPLAZ")==null)?null:((BigDecimal)map.get("N_CANTPLAZ")).longValue());
					if(map.get("D_PLAZPART")!=null) item.setFechaPlazo((Date)map.get("D_PLAZPART"));
					item.setFechaLiberacion((Date)map.get("D_LIBEPART")==null?(Date)map.get("D_RECHPART"):(Date)map.get("D_LIBEPART"));
					if(map.get("N_DISPART")!=null) item.setDisponible(((BigDecimal)map.get("N_DISPART")).longValue());
					if(map.get("N_PRIO")!=null) item.setPrioridad((map.get("N_PRIO")==null)?null:((BigDecimal)map.get("N_PRIO")).longValue());
					if(map.get("V_NOCOLA")!=null) item.setResponsable((String)map.get("V_NOCOLA"));
					if(map.get("N_IDFASE")!=null) item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					if(map.get("V_NOFASE")!=null) item.setNombreFase((String)map.get("V_NOFASE"));
					if(map.get("N_INDRECH")!=null) item.setIndicadorRechazo((map.get("N_INDRECH")==null)?null:((BigDecimal)map.get("N_INDRECH")).longValue());
					if(map.get("D_RECHPART")!=null) item.setFechaRechazo((Date)map.get("D_RECHPART"));
					if(map.get("N_IDTRAB")!=null) item.setIdTrabajador(map.get("N_IDTRAB")==null?null:((BigDecimal)map.get("N_IDTRAB")).longValue());
					if(map.get("N_INDTRAB")!=null) item.setIndicadorTrabajador(((BigDecimal)map.get("N_INDTRAB")).longValue());
					if(map.get("N_INDEQUI")!=null) item.setIndicadorEquipo(((BigDecimal)map.get("N_INDEQUI")).longValue());
					if(map.get("N_INDFUNC")!=null) item.setIndicadorFuncion(((BigDecimal)map.get("N_INDFUNC")).longValue());
					if(map.get("N_INDJEFE")!=null) item.setIndicadorJefe(((BigDecimal)map.get("N_INDJEFE")).longValue());
					if(map.get("N_INDJEFACT")!=null) item.setIndicadorJefeActual(((BigDecimal)map.get("N_INDJEFACT")).longValue());
					if((item.getIndicadorTrabajador()==0 || item.getIndicadorEquipo()==0 || 
						(item.getIndicadorJefe()==1 && item.getIndicadorJefeActual()==0))
						&& item.getFechaLiberacion()==null){
						item.setEstiloBloqueado(true);
						if(item.getIndicadorTrabajador()==0)	item.setTextoBloqueado(Mensaje.MENS_TRAB_BAJA.toString());
						else if(item.getIndicadorEquipo()==0)	item.setTextoBloqueado(Mensaje.MENS_EQUI_BAJA.toString());
						else if(item.getIndicadorJefe()==1 && item.getIndicadorJefeActual()==0)
							item.setTextoBloqueado(Mensaje.MENS_CARG_BAJA.toString());
					} else {
						item.setEstiloBloqueado(false);
						item.setTextoBloqueado("");
					}
					
					Equipo equipo = new Equipo();
					equipo.setId((map.get("N_IDEQUI")==null)?null:((BigDecimal)map.get("N_IDEQUI")).longValue());
					equipo.setDescripcion((String)map.get("V_NOEQUI"));
					item.setEquipoColaborador((String)map.get("V_NOEQUI"));
					item.setEquipo(equipo);
					
					lista.add(item);
					
					if (map.get("RESULT_COUNT")!=null) {
						this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
					}
				}				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerParticipanteBitacora";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	 
		@SuppressWarnings("unchecked")
		private List<Colaborador> obtenerParticipante(Long codigo, Long fase,
				Long revision, Long iteracion, PageRequest pagina) {
			
			Map<String, Object> out	= null;
			List<Colaborador> lista	= new ArrayList<>();
		
			this.error		= null;
			this.jdbcCall	=
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_PARTICIPANTE_OBTENER")
				.declareParameters(
					new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
					new SqlParameter("i_nfase", 		OracleTypes.VARCHAR),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
					new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
					new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
					new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
					new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nid",			codigo)
				.addValue("i_nfase",		fase)
				.addValue("i_nrevision",	revision)
				.addValue("i_niteracion",	iteracion)
				.addValue("i_npagina",		pagina.getPagina())
				.addValue("i_nregistros",	pagina.getRegistros());
			try {
				out = this.jdbcCall.execute(in);
				Integer resultado = (Integer)out.get("o_retorno");			
				if(resultado == 0) {
					List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
					for(Map<String, Object> map : listado) {			
						Colaborador item = new Colaborador();
						if(map.get("N_IDCOLA")!=null) item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
						if(map.get("N_IDREVI")!=null) item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
						if(map.get("N_NUMITER")!=null) item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
						if(map.get("V_NOCOLA")!=null) item.setNombreCompleto((String)map.get("V_NOCOLA"));
						if(map.get("V_NOFUNC")!=null) item.setFuncion((String)map.get("V_NOFUNC"));
						if(map.get("N_CICLPART")!=null) item.setCiclo((map.get("N_CICLPART")==null)?null:((BigDecimal)map.get("N_CICLPART")).longValue());
						if(map.get("V_COMEPART")!=null) item.setComentario((String)map.get("V_COMEPART"));
						if(map.get("N_CANTPLAZ")!=null) item.setPlazo(((BigDecimal)map.get("N_CANTPLAZ")==null)?null:((BigDecimal)map.get("N_CANTPLAZ")).longValue());
						if(map.get("D_PLAZPART")!=null) item.setFechaPlazo((Date)map.get("D_PLAZPART"));
						if(map.get("D_LIBEPART")!=null) item.setFechaLiberacion((Date)map.get("D_LIBEPART"));
						if(map.get("N_DISPART")!=null) item.setDisponible(((BigDecimal)map.get("N_DISPART")).longValue());
						if(map.get("N_PRIO")!=null) item.setPrioridad((map.get("N_PRIO")==null)?null:((BigDecimal)map.get("N_PRIO")).longValue());
						if(map.get("V_NOCOLA")!=null) item.setResponsable((String)map.get("V_NOCOLA"));
						if(map.get("N_IDFASE")!=null) item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
						if(map.get("V_NOFASE")!=null) item.setNombreFase((String)map.get("V_NOFASE"));
						if(map.get("N_INDRECH")!=null) item.setIndicadorRechazo((map.get("N_INDRECH")==null)?null:((BigDecimal)map.get("N_INDRECH")).longValue());
						if(map.get("D_RECHPART")!=null) item.setFechaRechazo((Date)map.get("D_RECHPART"));
						if(map.get("N_IDTRAB")!=null) item.setIdTrabajador(map.get("N_IDTRAB")==null?null:((BigDecimal)map.get("N_IDTRAB")).longValue());
						if(map.get("N_INDTRAB")!=null) item.setIndicadorTrabajador(((BigDecimal)map.get("N_INDTRAB")).longValue());
						if(map.get("N_INDEQUI")!=null) item.setIndicadorEquipo(((BigDecimal)map.get("N_INDEQUI")).longValue());
						if(map.get("N_INDFUNC")!=null) item.setIndicadorFuncion(((BigDecimal)map.get("N_INDFUNC")).longValue());
						if(map.get("N_INDJEFE")!=null) item.setIndicadorJefe(((BigDecimal)map.get("N_INDJEFE")).longValue());
						if(map.get("N_INDJEFACT")!=null) item.setIndicadorJefeActual(((BigDecimal)map.get("N_INDJEFACT")).longValue());
						if((item.getIndicadorTrabajador()==0 || item.getIndicadorEquipo()==0 || 
							(item.getIndicadorJefe()==1 && item.getIndicadorJefeActual()==0))
							&& item.getFechaLiberacion()==null){
							item.setEstiloBloqueado(true);
							if(item.getIndicadorTrabajador()==0)	item.setTextoBloqueado(Mensaje.MENS_TRAB_BAJA.toString());
							else if(item.getIndicadorEquipo()==0)	item.setTextoBloqueado(Mensaje.MENS_EQUI_BAJA.toString());
							else if(item.getIndicadorJefe()==1 && item.getIndicadorJefeActual()==0)
								item.setTextoBloqueado(Mensaje.MENS_CARG_BAJA.toString());
						} else {
							item.setEstiloBloqueado(false);
							item.setTextoBloqueado("");
						}
						
						Equipo equipo = new Equipo();
						equipo.setId((map.get("N_IDEQUI")==null)?null:((BigDecimal)map.get("N_IDEQUI")).longValue());
						equipo.setDescripcion((String)map.get("V_NOEQUI"));
						item.setEquipoColaborador((String)map.get("V_NOEQUI"));
						item.setEquipo(equipo);
						
						lista.add(item);
						
						if (map.get("RESULT_COUNT")!=null) {
							this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
						}
					}				
				} else {
					String mensaje			= (String)out.get("o_mensaje");
					String mensajeInterno	= (String)out.get("o_sqlerrm");
					this.error				= new Error(resultado,mensaje,mensajeInterno);
				}
			}catch(Exception e){
				Integer resultado		= 1;
				String mensaje			= "Error en DocumentoDAOImpl.obtenerParticipante";
				String mensajeInterno	= e.getMessage();
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
			return lista;
		}
		
	
	private Flujo obtenerFase(List<Flujo> lista, Long id) {
		for(Flujo item: lista) {
			if(item.getIdFase().longValue()==id.longValue()) {
				return item;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Flujo> obtenerFlujo(Flujo objeto) {
		List<Flujo> lista		= new ArrayList<>();
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_FLUJO_OBTENER")
				.declareParameters(
					new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
					new SqlParameter("i_nid",			OracleTypes.NUMBER),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
					new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_ndocumento",	objeto.getIdDocumento())
				.addValue("i_nid",			objeto.getIdFase())
				.addValue("i_nrevision",	objeto.getIdRevision())
				.addValue("i_niteracion",	objeto.getIteracion());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					Flujo item = new Flujo();
					if(map.get("N_IDDOCU")!=null) 		item.setIdDocumento(((BigDecimal)map.get("N_IDDOCU")).longValue());
					if(map.get("N_IDREVI")!=null)		item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					if(map.get("N_NUMITER")!=null)		item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
					if(map.get("N_IDFASE")!=null)		item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					if(map.get("V_USUAFASE")!=null) 	item.setUsuarioFase(((BigDecimal)map.get("V_USUAFASE")).longValue());
					if(map.get("D_FECHFASE")!=null) 	item.setFechaFase((Date)map.get("D_FECHFASE"));	
					if(map.get("N_INDIREVI")!=null)		item.setIndicadorActividad(((BigDecimal)map.get("N_INDIREVI")).longValue());
					if(map.get("N_IDESTAFASE")!=null)	item.setIdEstadoFase(((BigDecimal)map.get("N_IDESTAFASE")).longValue());
					if(map.get("V_NOESTAFASE")!=null)	item.setEstadoFase((String)map.get("V_NOESTAFASE"));
					if(map.get("V_CRITFASE")!=null)		item.setCritica((String)map.get("V_CRITFASE"));	
					if(map.get("N_INDIBLOQ")!=null)		item.setIndicadorBloqueo(((BigDecimal)map.get("N_INDIBLOQ")).longValue());
					if(map.get("N_USUABLOQ")!=null)		item.setUsuarioBloqueo(((BigDecimal)map.get("N_USUABLOQ")).longValue());
					if(map.get("V_NOUSUABLOQ")!=null)	item.setNombreBloqueo((String)map.get("V_NOUSUABLOQ"));
					if(map.get("D_FECHBLOQ")!=null)		item.setFechaBloqueo((Date)map.get("D_FECHBLOQ"));
					if(map.get("N_DISFLUDOC")!=null)	item.setDisponible(((BigDecimal)map.get("N_DISFLUDOC")).longValue());
					lista.add(item);
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerFlujo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Documento> obtenerComplementario(Long codigo, Long idRevision, PageRequest pagina) {
		
		Map<String, Object> out	= null;
		List<Documento> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_COMPLEMENTARIO_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 		OracleTypes.NUMBER),						
				new SqlParameter("i_npadre", 	OracleTypes.NUMBER),
				new SqlParameter("i_nrevision", OracleTypes.NUMBER),
				new SqlParameter("i_ntipodocu", OracleTypes.NUMBER),
				new SqlParameter("i_vcodigo", 	OracleTypes.VARCHAR),
				new SqlParameter("i_vtitulo", 	OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",OracleTypes.NUMBER),
				new SqlParameter("i_vorden", 	OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			null)
			.addValue("i_npadre",		codigo)
			.addValue("i_nrevision",	idRevision)
			.addValue("i_ntipodocu",	null)
			.addValue("i_vcodigo",		null)
			.addValue("i_vtitulo",		null)
			.addValue("i_npagina",		pagina.getPagina())
			.addValue("i_nregistros",	pagina.getRegistros())
			.addValue("i_vorden",		null);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {			
					Documento item = new Documento();
					item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
					item.setCodigo((String)map.get("V_CODDOCU"));
					item.setDescripcion((String)map.get("V_DESDOCU"));
					//Ruta del documento complentario
					
					if(map.get("RUTA_NO_CONTROLADA")!=null) {
						item.setRutaDocumentoCopiaNoCont((String)map.get("RUTA_NO_CONTROLADA"));	
					}
					if(map.get("N_VERDOCU")!=null) {
						item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());	
					}
					if(map.get("N_RETDOCU")!=null) {
						item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
					}
					if(map.get("N_PERIOBLI")!=null) {
						item.setPeriodo(((BigDecimal)map.get("N_PERIOBLI")).longValue());	
					}
					if(map.get("V_JUSTDOCU")!=null) {
						item.setJustificacion((String)map.get("V_JUSTDOCU"));	
					}
					if(map.get("V_NOARCHDOCU")!=null) {
						item.setNombreArchivo((String)map.get("V_NOARCHDOCU"));	
					}
					if(map.get("V_RUARCHDOCU")!=null) {
						item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));	
					}
					if(map.get("V_INDESTDOCU")!=null) {
						item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));	
					}
					if(map.get("V_ESTADESC")!=null) {
						item.setEstadoDercarga((String)map.get("V_ESTADESC"));	
					}
					if(map.get("V_RAIZDOCU")!=null) {
						item.setRaiz((String)map.get("V_RAIZDOCU"));	
					}
					if(map.get("N_DISDOCCOM")!=null) {
						item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCCOM")));	
					}
					
					if(map.get("N_IDDOCUPADR")!=null) {
						Documento padre = new Documento(); 
						padre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
						item.setPadre(padre);
					}
					if(map.get("N_IDESTA")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
						estado.setV_descons((String)map.get("V_NOESTA"));
						item.setEstado(estado);
					}
					if(map.get("N_IDPROC")!=null) {
						Jerarquia jproceso = new Jerarquia();
						//Constante proceso = new Constante();
						//proceso.setIdconstante(((BigDecimal)map.get("N_IDPROC")).longValue());
						jproceso.setIdJerarquia(((BigDecimal)map.get("N_IDPROC")).longValue());
						//proceso.setV_descons((String)map.get("V_NOPROC"));
						jproceso.setV_descons((String)map.get("V_NOPROC"));
						//item.setProceso(proceso);
						item.setJproceso(jproceso);
					}
					if(map.get("N_IDALCASGI")!=null) {
						Jerarquia jalcance = new Jerarquia();
						//Constante alcance = new Constante();
						jalcance.setIdJerarquia(((BigDecimal)map.get("N_IDALCASGI")).longValue());
						//alcance.setIdconstante(((BigDecimal)map.get("N_IDALCASGI")).longValue());
						jalcance.setV_descons((String)map.get("V_NOALCA"));
						//alcance.setV_descons((String)map.get("V_NOALCA"));
						//item.setAlcanceSGI(alcance);
						item.setJalcanceSGI(jalcance);
					}
					if(map.get("N_IDGEREGNRL")!=null) {
						Jerarquia jgerencia = new Jerarquia();
						//Constante gerencia = new Constante();
						jgerencia.setIdJerarquia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
						//gerencia.setIdconstante(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
						jgerencia.setV_descons((String)map.get("V_NOGERE"));
						//gerencia.setV_descons((String)map.get("V_NOGERE"));
						item.setJgerencia(jgerencia);
						//item.setGerencia(gerencia);
					}
					if(map.get("N_IDTIPODOCU")!=null) {
						Constante tipoDocumento = new Constante();
						tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
						tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
						item.setCtipoDocumento(tipoDocumento);
					}
					if(map.get("V_IDCODIANTE")!=null) {
						Codigo codigoAnterior = new Codigo();
						//codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
						codigoAnterior.setCodigo((String)map.get("V_IDCODIANTE"));
						item.setCodigoAnterior(codigoAnterior);
					}
					if(map.get("N_IDMOTIREVI")!=null) {
						Constante motivoRevision = new Constante();
						motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
						motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
						item.setMotivoRevision(motivoRevision);
					}
					if(map.get("N_IDEMIS")!=null) {
						Colaborador emisor = new Colaborador();
						emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
						emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
						if(map.get("N_IDEQUIEMIS")!=null) {
							Equipo equipoEmisor = new Equipo();
							equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
							equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
							emisor.setEquipo(equipoEmisor);
						}
						item.setEmisor(emisor);
					}
					if(map.get("N_IDFASE")!=null) {
						Constante fase = new Constante();
						fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
						fase.setV_descons((String)map.get("V_NOFASE"));
						item.setFase(fase);
					}
					if(map.get("N_IDREVI")!=null) {
						Revision revision = new Revision();
						revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
						//revision.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());
						revision.setFecha((Date)map.get("D_FEREVI"));
						item.setRevision(revision);
					}
					if(map.get("N_IDCARP")!=null) {
						Jerarquia fase = new Jerarquia();
						fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
						item.setNodo(fase);
					}
					if(map.get("N_IDCOPI")!=null) {
						Copia copia = new Copia();
						copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
						item.setCopia(copia);
					}
					if(map.get("N_IDTIPOCOMP")!=null) {
						Constante tipoComplementario = new Constante();
						tipoComplementario.setIdconstante(((BigDecimal)map.get("N_IDTIPOCOMP")).longValue());
						tipoComplementario.setV_descons((String)map.get("V_NOTIPOCOMP"));
						item.setTipoComplementario(tipoComplementario);
					}
					lista.add(item);
				}				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerComplementario";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Equipo> obtenerEquipo(Long codigo, Long revision) {
		
		Map<String, Object> out	= null;
		List<Equipo> lista		= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_EQUIPORESPONSABLE_OBTENER")
			.declareParameters(
				new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
				new SqlParameter("i_nrevision", OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_ndocumento", codigo)
			.addValue("i_nrevision",  revision);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {			
					Equipo item = new Equipo();
					item.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
					item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					item.setDescripcion((String)map.get("V_NOMEQUI"));
					item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISEQUI")));
					//if(map.get("V_INDREVI")!=null)  item.setIndicadorRevision(((BigDecimal)map.get("V_INDREVI")).longValue());
					//if(map.get("V_INDNOTI")!=null)  item.setIndicadorNotificacion(((BigDecimal)map.get("V_INDNOTI")).longValue());
					if(map.get("N_INDIRESP")!=null) item.setIndicadorResponsable(((BigDecimal)map.get("N_INDIRESP")).longValue());
					if(map.get("N_JEFEQUI")!=null) {
						Colaborador jefe = new Colaborador();
						jefe.setIdColaborador(((BigDecimal)map.get("N_JEFEQUI")).longValue());
						jefe.setNombreCompleto((String)map.get("V_JEFEQUI"));
						item.setJefe(jefe);
					}
					lista.add(item);
				}				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerEquipo";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Colaborador> obtenerParticipanteHistorico(Long codigo, Long fase, PageRequest pagina) {
		
		Map<String, Object> out	= null;
		List<Colaborador> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_PARTICIPANTEHIST_OBTENER")
			.declareParameters(
				new SqlParameter("i_nhistorial", 	OracleTypes.NUMBER),						
				new SqlParameter("i_nfase", 		OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nhistorial",	codigo)
			.addValue("i_nfase",		fase)
			.addValue("i_npagina",		pagina.getPagina())
			.addValue("i_nregistros",	pagina.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {			
					Colaborador item = new Colaborador();
					item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
					item.setNombreCompleto((String)map.get("V_NOCOLA"));
					item.setFuncion((String)map.get("V_NOFUNC"));
					item.setCiclo(((BigDecimal)map.get("N_CICLPART")).longValue());
					item.setComentario((String)map.get("V_COMEPART"));
					item.setPlazo(((BigDecimal)map.get("N_CANTPLAZ")).longValue());
					item.setFechaPlazo((Date)map.get("D_PLAZPART"));
					item.setFechaLiberacion((Date)map.get("D_LIBEPART"));
					item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
					item.setDisponible(((BigDecimal)map.get("N_DISPARHIS")).longValue());
					
					if(map.get("V_NOEQUI")!=null) {
						Equipo equipo = new Equipo();
						equipo.setDescripcion((String)map.get("V_NOEQUI"));
						item.setEquipo(equipo);
					}
					
					lista.add(item);
				}				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerParticipanteHistorico";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Equipo> obtenerEquipoHistorico(Long codigo) {
		
		Map<String, Object> out	= null;
		List<Equipo> lista		= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_EQUIPORESPOHIST_OBTENER")
			.declareParameters(
				new SqlParameter("i_nhistorial",OracleTypes.NUMBER),						
				new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nhistorial", codigo);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {			
					Equipo item = new Equipo();
					item.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
					item.setDescripcion((String)map.get("V_NOMEQUI"));
					item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISEQUI")));
					item.setIndicadorRevision(((BigDecimal)map.get("V_INDREVI")).longValue());
					item.setIndicadorNotificacion(((BigDecimal)map.get("V_INDNOTI")).longValue());
					item.setIndicadorResponsable(((BigDecimal)map.get("V_EQUIUSUA")).longValue());
					item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
					if(map.get("N_JEFEQUI")!=null) {
						Colaborador jefe = new Colaborador();
						jefe.setIdColaborador(((BigDecimal)map.get("N_JEFEQUI")).longValue());
						jefe.setNombreCompleto((String)map.get("V_JEFEQUI"));
						item.setJefe(jefe);
					}
					lista.add(item);
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerEquipoHistorico";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Documento> obtenerComplementarioHistorico(Long codigo, PageRequest pagina) {
		
		Map<String, Object> out	= null;
		List<Documento> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_COMPLEMENTHIST_OBTENER")
			.declareParameters(
				new SqlParameter("i_nhistorial",OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",OracleTypes.NUMBER),
				new SqlParameter("i_vorden", 	OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nhistorial",	codigo)
			.addValue("i_npagina",		pagina.getPagina())
			.addValue("i_nregistros",	pagina.getRegistros())
			.addValue("i_vorden",		null);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {			
					Documento item = new Documento();
					item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
					item.setCodigo((String)map.get("V_CODDOCU"));
					item.setDescripcion((String)map.get("V_DESDOCU"));
					item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());
					item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
					item.setPeriodo(((BigDecimal)map.get("N_PERIOBLI")).longValue());
					item.setJustificacion((String)map.get("V_JUSTDOCU"));
					item.setNombreArchivo((String)map.get("V_NOARCHDOCU"));
					item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));
					item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));
					item.setEstadoDercarga((String)map.get("V_ESTADESC"));
					item.setRaiz((String)map.get("V_RAIZDOCU"));
					item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
					item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCOHI")));
					if(map.get("N_IDDOCUPADR")!=null) {
						Documento padre = new Documento(); 
						padre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
						item.setPadre(padre);
					}
					if(map.get("N_IDESTA")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
						estado.setV_descons((String)map.get("V_NOESTA"));
						item.setEstado(estado);
					}
					if(map.get("N_IDPROC")!=null) {
						Jerarquia jproceso = new Jerarquia();
						//Constante proceso = new Constante();
						//proceso.setIdconstante(((BigDecimal)map.get("N_IDPROC")).longValue());
						jproceso.setIdJerarquia(((BigDecimal)map.get("N_IDPROC")).longValue());
						//proceso.setV_descons((String)map.get("V_NOPROC"));
						jproceso.setV_descons((String)map.get("V_NOPROC"));
						//item.setProceso(proceso);
						item.setJproceso(jproceso);
					}
					if(map.get("N_IDALCASGI")!=null) {
						Jerarquia jalcance = new Jerarquia();
						//Constante alcance = new Constante();
						jalcance.setIdJerarquia(((BigDecimal)map.get("N_IDALCASGI")).longValue());
						//alcance.setIdconstante(((BigDecimal)map.get("N_IDALCASGI")).longValue());
						jalcance.setV_descons((String)map.get("V_NOALCA"));
						//alcance.setV_descons((String)map.get("V_NOALCA"));
						//item.setAlcanceSGI(alcance);
						item.setJalcanceSGI(jalcance);
					}
					if(map.get("N_IDGEREGNRL")!=null) {
									
						
						Jerarquia jgerencia = new Jerarquia();
						//Constante gerencia = new Constante();
						jgerencia.setIdJerarquia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
						//gerencia.setIdconstante(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
						jgerencia.setV_descons((String)map.get("V_NOGERE"));
						//gerencia.setV_descons((String)map.get("V_NOGERE"));
						item.setJgerencia(jgerencia);
						//item.setGerencia(gerencia);
						
					
						
					}
					if(map.get("N_IDTIPODOCU")!=null) {
						Constante tipoDocumento = new Constante();
						tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
						tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
						item.setCtipoDocumento(tipoDocumento);
					}
					if(map.get("V_IDCODIANTE")!=null) {
						Codigo codigoAnterior = new Codigo();
						//codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
						codigoAnterior.setCodigo((String)map.get("V_IDCODIANTE"));
						item.setCodigoAnterior(codigoAnterior);
					}
					if(map.get("N_IDMOTIREVI")!=null) {
						Constante motivoRevision = new Constante();
						motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
						motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
						item.setMotivoRevision(motivoRevision);
					}
					if(map.get("N_IDEMIS")!=null) {
						Colaborador emisor = new Colaborador();
						emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
						emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
						if(map.get("N_IDEQUIEMIS")!=null) {
							Equipo equipoEmisor = new Equipo();
							equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
							equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
							emisor.setEquipo(equipoEmisor);
						}
						item.setEmisor(emisor);
					}
					if(map.get("N_IDFASE")!=null) {
						Constante fase = new Constante();
						fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
						fase.setV_descons((String)map.get("V_NOFASE"));
						item.setFase(fase);
					}
					if(map.get("N_IDREVI")!=null) {
						Revision revision = new Revision();
						revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
						revision.setFecha((Date)map.get("D_FEREVI"));
						item.setRevision(revision);
					}
					if(map.get("N_IDCARP")!=null) {
						Jerarquia fase = new Jerarquia();
						fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
						item.setNodo(fase);
					}
					if(map.get("N_IDCOPI")!=null) {
						Copia copia = new Copia();
						copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
						item.setCopia(copia);
					}
					if(map.get("N_IDTIPOCOMP")!=null) {
						Constante tipoComplementario = new Constante();
						tipoComplementario.setIdconstante(((BigDecimal)map.get("N_IDTIPOCOMP")).longValue());
						tipoComplementario.setV_descons((String)map.get("V_NOTIPOCOMP"));
						item.setTipoComplementario(tipoComplementario);
					}
					lista.add(item);
				}				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerComplementarioHistorico";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	//guardarDocumentoTraslado
	@Override
	@Transactional
	public Documento guardarDocumentoTraslado(Documento documento, Long codigo, String usuario , Long idUsuario) {		
		Documento item = null;
		String indicadorAprobacion = null;
		String indicadorSolicitudRevision = null;
		String indicadorFase = null;
		List<Colaborador> listElaboracion=new ArrayList<>();
		List<Colaborador> listConsenso=new ArrayList<>();
		List<Colaborador> listAprobacion=new ArrayList<>();
		List<Colaborador> listHomologacion=new ArrayList<>();
		try {
			//Obtener Constante
			Date hoy = new Date();
			documento.setDisponible(Long.parseLong(EstadoConstante.ACTIVO.toString()));
			
			ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
			constanteDao.setJdbc(this.jdbc);			
			ConstanteRequest constante = new ConstanteRequest();
			constante.setPadre(TipoConstante.ETAPA_RUTA.toString());			
			PageRequest pagina = new PageRequest();
			pagina.setPagina(1);
			pagina.setRegistros(1000);
			
			List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
			Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			
			//Inicio Cambio Godar obtener participantes asociados al documento
			listElaboracion=this.obtenerParticipante(codigo,faseElaboracion , null, null, pagina);
			listConsenso=this.obtenerParticipante(codigo,faseConsenso , null, null, pagina);
			listAprobacion=this.obtenerParticipante(codigo,faseAprobacion , null, null, pagina);
			listHomologacion=this.obtenerParticipante(codigo,faseHomologacion , null, null, pagina);
			
			documento.setParticipanteElaboracion(listElaboracion);
			documento.setParticipanteConsenso(listConsenso);
			documento.setParticipanteAprobacion(listAprobacion);
			documento.setParticipanteHomologacion(listHomologacion);
			
			RelacionCoordinadorDAOImpl relaDao= new RelacionCoordinadorDAOImpl();
			relaDao.setJdbc(this.jdbc);
			
			List<RelacionCoordinador> listaRelacion = new ArrayList<>();
			Long idAlcance=null;
			idAlcance=documento.getJalcanceSGI().getId();
			Long idGerencia=null;
			idGerencia=documento.getGerencia();
			
			listaRelacion=relaDao.obtenerDatosCoordinador(idGerencia,idAlcance);
			int numFicha1=documento.getCoordinador().getIdColaborador().intValue();
			int numFicha2=listaRelacion.get(0).getNroFicha().intValue();
			
			if(numFicha1!=numFicha2) {
				documento.getCoordinador().setNumeroFicha(listaRelacion.get(0).getNroFicha());
			}
			
			//Fin cambio
			
			//Si se aprobo la solicitud de revision (1)
			indicadorAprobacion = (documento.getIndAprobacionSoli()==null)?null:
				(!documento.getIndAprobacionSoli().equals("1")?null:documento.getIndAprobacionSoli());
			indicadorSolicitudRevision = documento.getIndicadorSolicitudRevision();
			indicadorFase = documento.getIndicadorFase();
			documento.setIndAprobacionSoli(indicadorAprobacion);
			this.actualizarTrasladoDocumento(documento, codigo, usuario);
			
			if(documento.getGerencia()!=null) {
			item = this.obtenerDocumentoDetalle(codigo);
			if(item==null) {
				return null;
			} else {
				item.setIndAprobacionSoli(indicadorAprobacion);
			}
			
			//Registrar Revision
			if(indicadorFase==null) {
				Revision pRevision = documento.getRevision();
				RevisionDAOImpl revisionDao = new RevisionDAOImpl();
				pRevision.setDocumento(new Documento());
				pRevision.getDocumento().setId(item.getId());
				if(indicadorSolicitudRevision!=null) {
					pRevision.setId(null);
					pRevision.setNumero(pRevision.getNumero()+1);
				}
				if(indicadorAprobacion!=null) {
					pRevision.setFechaAprobacion(hoy);
				}
				//Inicio Cambio Godar
				if(pRevision.getEstado().getIdconstante()==143) {
					int x=141;
					Long num=Long.valueOf(x);
					pRevision.getEstado().setIdconstante(num);
				}
				
				String idDocGD=null;
					if (pRevision.getRutaDocumentoOffice()!=null) {
						//conexión a Google Drive
						final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
					    Drive serviceGD = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					            .setApplicationName(APPLICATION_NAME)
					            .build();
					    
					    Date date = new Date();
		    			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		    	        String fechaFormateada = formatter.format(date);
					    
						//descargo documento de fileserver
						carpetaResources = env.getProperty("app.config.paths.toke.directory");	
						String nombreArchivo = "documento";//+fechaFormateada;
						URL urlWord = new URL(pRevision.getRutaDocumentoOffice());
						System.out.println(urlWord);
						String cadena = urlWord.toString();
						String ExtenDocuAnterior= cadena.substring(cadena.length()-4, cadena.length());
						System.out.println(ExtenDocuAnterior);				
						
						java.io.File destinationWord = new java.io.File("");
						if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
							 destinationWord = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".docx");	
						}else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
							 destinationWord = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".xlsx");
						}     
						
						FileUtils.copyURLToFile(urlWord, destinationWord);
						//subo documento descargado a google drive (carpeta sedapal)
						//especifico nombre de documento al crearlo y fomato mismo de GD
						  File fileMetadata = new File();
						  fileMetadata.setName("Nombre Documento");
						  fileMetadata.setMimeType("application/vnd.google-apps.document");					
						  fileMetadata.setParents(Collections.singletonList("1GiNU-9y4eRIBmdbP5lcG9if2Txb2gbji"));
						//subo documento descargado a GD
						  
						  java.io.File filePath = new java.io.File("");
						  if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
							   filePath = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".docx");  
						  }else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
							  filePath = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".xlsx");
						  }
						  FileContent mediaContent = new FileContent("",filePath);
						  if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
							  mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.wordprocessingml.document", filePath);  
						  }else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
							  mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", filePath);
						  }
						  File file = serviceGD.files().create(fileMetadata, mediaContent)
						      .setFields("id")
						      .execute();
						  System.out.println("File ID: " + file.getId());
						  //al nuevo Id del GD le concatenamos la extension
						  if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
							  idDocGD = file.getId()+"word";  
						  }else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
							  idDocGD = file.getId()+"exel"; 
						  }
						//eliminar carpeta de creada para descargar archivo de fileserver
						  UArchivo.eliminarCarpeta(carpetaResources+"carpeta-descarga");
					}
				
					pRevision.setIdDocGoogleDrive(idDocGD);
				//Fin cambio 
				revisionDao.setJdbc(this.jdbc);
				
				
				
				Revision revision = revisionDao.crearRevision(pRevision,usuario,idUsuario);
				if(revision == null) {
					return null;
				}else {
					item.setRevision(revision);
					item.setIndAprobacionSoli(documento.getIndAprobacionSoli());
					this.actualizarTrasladoDocumento(item, item.getId(), usuario);
				}
			} else if(indicadorFase.equals("3")) {
				item.setRevision(documento.getRevision());
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(documento.getBitacora().getIdFase().longValue());
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(indicadorAprobacion);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				objeto.setComentario(documento.getBitacora().getComentario());
				objeto.setUsuarioFase(idUsuario);
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				}
				
				Revision revision = documento.getRevision();
				revision.setDocumento(new Documento());
				revision.getDocumento().setId(item.getId());
				revision.setIteracion(documento.getRevision().getIteracion()+1);
				
				RevisionDAOImpl revisionDao = new RevisionDAOImpl();
				revisionDao.setJdbc(this.jdbc);
				Revision resultado = revisionDao.crearRevision(revision,usuario,idUsuario);
				if(resultado == null) {
					return null;
				} else {
					item.setRevision(resultado);
				}
			} else {
				item.setRevision(documento.getRevision());
			}
			
			//Registrar Participantes y Fases
			boolean eliminado = this.eliminarParticipante(null,item.getId(),null,
					item.getRevision().getId(),item.getRevision().getIteracion(),usuario);
			if(eliminado==false)	{
				return null;
			}
			
			boolean eliminadoFlujo = this.eliminarFlujo(item.getId(),null,
					item.getRevision().getId(),item.getRevision().getIteracion(),usuario);
			if(eliminadoFlujo==false)	{
				return null;
			}
			
			//Fase Elaboraci�n
			if(documento.getParticipanteElaboracion()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteElaboracion()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}else {
						participante.setComentario(null);
						participante.setFechaLiberacion(null);
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseElaboracion, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteElaboracion(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseElaboracion);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(indicadorAprobacion);//documento.getIndAprobacionSoli().equals("1") ? "1":null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseElaboracion.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndAprobacionSoli("1");
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Fase Consenso
			if(documento.getParticipanteConsenso()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteConsenso()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}else {
						participante.setComentario(null);
						participante.setFechaLiberacion(null);
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseConsenso, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteConsenso(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseConsenso);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseConsenso.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Fase Aprobaci�n
			if(documento.getParticipanteAprobacion()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteAprobacion()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}else {
						participante.setComentario(null);
						participante.setFechaLiberacion(null);
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseAprobacion, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteAprobacion(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseAprobacion);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseAprobacion.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Fase Homologacion
			if(documento.getParticipanteHomologacion()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteHomologacion()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}else {
						participante.setComentario(null);
						participante.setFechaLiberacion(null);
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseHomologacion, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteHomologacion(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseHomologacion);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseHomologacion.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Registrar Documentos Complementarios
			if(documento.getListaComplementario()!=null) {
				eliminado = this.eliminarComplementario(codigo!= null?codigo:item.getId(), null, item.getRevision().getId(), usuario);
				if(eliminado==false)	{
					return null;
				}
				List<Documento> listaHijo = new ArrayList<>();
				for(Documento documentoHijo : documento.getListaComplementario()) {
					documentoHijo.setDisponible(documento.getDisponible());
					documentoHijo.setIdrevision(item.getRevision().getId()+"");
					Documento hijoTemporal = this.registrarComplementario(documentoHijo, codigo!= null?codigo:item.getId(), usuario);
					if(hijoTemporal==null) {
						return null;
					}
					listaHijo.add(hijoTemporal);
				}
				item.setListaComplementario(listaHijo);
			}
			
			//Registrar Equipo Usuario
			if(documento.getListaEquipo()!=null) {
				eliminado = this.eliminarEquipo(codigo!= null?codigo:item.getId(), null, item.getRevision().getId(), usuario);
				if(eliminado==false)	{
					return null;
				}
				List<Equipo> listaEquipo = new ArrayList<>();
				for(Equipo equipo : documento.getListaEquipo()) {
					equipo.setDisponible(documento.getDisponible());
					equipo.setIdRevision(item.getRevision().getId());
					Equipo equipoTemporal = this.registrarEquipo(equipo, codigo!= null?codigo:item.getId(), usuario);
					if(equipoTemporal==null) {
						return null;
					}
					listaEquipo.add(equipoTemporal);
				}
				item.setListaEquipo(listaEquipo);
			}
			}
			/*
			//Registrar Historico
			boolean historico = this.registrarDocumentoHistorico(codigo, usuario);
			if(historico==false)	{
				return null;
			}
			*/
			/*
			boolean codigoAnterior = this.registrarCodigoAnterior(codigo, item.getCodigo(), usuario);//
			if(codigoAnterior==false)	{
				return null;
			}
			*/
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.guardarDocumento";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return item;
	}
//Registro Documento
	@Override
	@Transactional
	public Documento guardarDocumento(Documento documento, Long codigo, String usuario , Long idUsuario) {
		Documento item = null;
		String indicadorAprobacion = null;
		String indicadorSolicitudRevision = null;
		String indicadorFase = null;
		String idDocGD = null;
		try {
			//Verificar si el documento existe en file server inicio
				//si existe se revive, se descarga y se sube a GD
				//if (documento.getListaRevision().get((documento.getListaRevision().size())-1).getRutaDocumentoOffice()!=null) {
			//if (documento.getListaRevision().size() > 0) {
			int iteracion = 0;
			for (int i=0; i<documento.getListaRevision().size();i++) {
				iteracion = i;
				System.out.println("iteracion 1>"+iteracion);
			}
				System.out.println("iteracion2>"+iteracion);
			if (!documento.getListaRevision().isEmpty()) {
				if (documento.getListaRevision().get(iteracion).getRutaDocumentoOffice()!=null) {
					//conexión a Google Drive
					final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
				    Drive serviceGD = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				            .setApplicationName(APPLICATION_NAME)
				            .build();
				    
				    Date date = new Date();
	    			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
	    	        String fechaFormateada = formatter.format(date);
	    	        carpetaResources = env.getProperty("app.config.paths.toke.directory");	//Capturamos el token    	        
					String nombreArchivo = "documento";//+fechaFormateada;
					String RutaIpFileServer= env.getProperty("app.config.servidor.fileserver");	//Capturamos ipFileServer
					URL urlWord = new URL(RutaIpFileServer+documento.getListaRevision().get(iteracion).getRutaDocumentoOffice());
					System.out.println(urlWord);
					String cadena = urlWord.toString();
					String ExtenDocuAnterior= cadena.substring(cadena.length()-4, cadena.length());
					System.out.println(ExtenDocuAnterior);				
					
					java.io.File destinationWord = new java.io.File("");
					if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
						 destinationWord = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".docx");	
					}else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
						 destinationWord = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".xlsx");
					}     
					
					FileUtils.copyURLToFile(urlWord, destinationWord);
					//subo documento descargado a google drive (carpeta sedapal)
					//especifico nombre de documento al crearlo y fomato mismo de GD
					  File fileMetadata = new File();
					  fileMetadata.setName("Nombre Documento");
					  fileMetadata.setMimeType("application/vnd.google-apps.document");					
					  fileMetadata.setParents(Collections.singletonList("1GiNU-9y4eRIBmdbP5lcG9if2Txb2gbji"));
					//subo documento descargado a GD
					  
					  java.io.File filePath = new java.io.File("");
					  if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
						   filePath = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".docx");  
					  }else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
						  filePath = new java.io.File(carpetaResources+"carpeta-descarga/"+nombreArchivo+".xlsx");
					  }
					  FileContent mediaContent = new FileContent("",filePath);
					  if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
						  mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.wordprocessingml.document", filePath);  
					  }else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
						  mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", filePath);
					  }
					  File file = serviceGD.files().create(fileMetadata, mediaContent)
					      .setFields("id")
					      .execute();
					  System.out.println("File ID: " + file.getId());
					  //al nuevo Id del GD le concatenamos la extension
					  if(ExtenDocuAnterior.equals("docx") || ExtenDocuAnterior.equals(".doc")) {
						  idDocGD = file.getId()+"word";  
					  }else if(ExtenDocuAnterior.equals("xlsx")|| ExtenDocuAnterior.equals(".xls")) {
						  idDocGD = file.getId()+"exel"; 
					  }
					//eliminar carpeta de creada para descargar archivo de fileserver
					  UArchivo.eliminarCarpeta(carpetaResources+"carpeta-descarga");
				}
			}
				//de lo contrario no
					
			//Verificar si el documento existe en file server fin
			
			//Obtener Constante
			Date hoy = new Date();
			documento.setDisponible(Long.parseLong(EstadoConstante.ACTIVO.toString()));
			
			ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
			constanteDao.setJdbc(this.jdbc);			
			ConstanteRequest constante = new ConstanteRequest();
			constante.setPadre(TipoConstante.ETAPA_RUTA.toString());			
			PageRequest pagina = new PageRequest();
			pagina.setPagina(1);
			pagina.setRegistros(1000);
			
			List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
			Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			
			//Si se aprobo la solicitud de revision (1)
			indicadorAprobacion = (documento.getIndAprobacionSoli()==null)?null:
				(!documento.getIndAprobacionSoli().equals("1")?null:documento.getIndAprobacionSoli());
			indicadorSolicitudRevision = documento.getIndicadorSolicitudRevision();
			indicadorFase = documento.getIndicadorFase();
			documento.setIndAprobacionSoli(indicadorAprobacion);
			item = this.registrarDocumento(documento, codigo, usuario);
			if(item==null) {
				return null;
			} else {
				item.setIndAprobacionSoli(indicadorAprobacion);
			}
			
			//Registrar Revision
			if(indicadorFase==null) {
				Revision pRevision = documento.getRevision();
				RevisionDAOImpl revisionDao = new RevisionDAOImpl();
				pRevision.setDocumento(new Documento());
				pRevision.getDocumento().setId(item.getId());
				
				
				if (!documento.getListaRevision().isEmpty()) {
					if (documento.getListaRevision().get(iteracion).getRutaDocumentoOffice()!=null) {
						pRevision.setIdDocGoogleDrive(idDocGD);
					}
				}
				
				
				if(indicadorSolicitudRevision!=null) {
					pRevision.setId(null);
					pRevision.setNumero(pRevision.getNumero()+1);
				}
				if(indicadorAprobacion!=null) {
					pRevision.setFechaAprobacion(hoy);
				}
				revisionDao.setJdbc(this.jdbc);
				Revision revision = revisionDao.crearRevision(pRevision,usuario,idUsuario);
				if(revision == null) {
					return null;
				}else {
					item.setRevision(revision);
					item.setIndAprobacionSoli(documento.getIndAprobacionSoli());
					//cguerra
					//item.getRevision().setRutaDocumentoOriginal(null);
					//cguerra
					this.registrarDocumento(item, item.getId(), usuario);
				}
			} else if(indicadorFase.equals("3")) {//rechazar
				item.setRevision(documento.getRevision());
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(documento.getBitacora().getIdFase().longValue());
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(indicadorAprobacion);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				objeto.setComentario(documento.getBitacora().getComentario());
				objeto.setUsuarioFase(idUsuario);
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				}
				
				Revision revision = documento.getRevision();
				revision.setDocumento(new Documento());
				revision.getDocumento().setId(item.getId());
				revision.setIteracion(documento.getRevision().getIteracion()+1);
				
				if (!documento.getListaRevision().isEmpty()) {
					if (documento.getListaRevision().get(iteracion).getRutaDocumentoOffice()!=null) {
						revision.setIdDocGoogleDrive(idDocGD);
					}
				}
				
				RevisionDAOImpl revisionDao = new RevisionDAOImpl();
				revisionDao.setJdbc(this.jdbc);
				Revision resultado = revisionDao.crearRevision(revision,usuario,idUsuario);
				if(resultado == null) {
					return null;
				} else {
					item.setRevision(resultado);
				}
			} else {				
				item.setRevision(documento.getRevision());
			}
			
			
			
			//Registrar Participantes y Fases
			boolean eliminado = this.eliminarParticipante(null,item.getId(),null,
					item.getRevision().getId(),item.getRevision().getIteracion(),usuario);
			if(eliminado==false)	{
				return null;
			}
			
			boolean eliminadoFlujo = this.eliminarFlujo(item.getId(),null,
					item.getRevision().getId(),item.getRevision().getIteracion(),usuario);
			if(eliminadoFlujo==false)	{
				return null;
			}
			
			//Fase Elaboración
			int tamano = documento.getParticipanteElaboracion()==null?0:
					documento.getParticipanteElaboracion().size();
			if(tamano > 0) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteElaboracion()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseElaboracion, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteElaboracion(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseElaboracion);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(indicadorAprobacion);//documento.getIndAprobacionSoli().equals("1") ? "1":null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseElaboracion.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndAprobacionSoli("1");
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Fase Consenso
			tamano = documento.getParticipanteConsenso()==null?0:
				documento.getParticipanteConsenso().size();
			if(tamano > 0) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteConsenso()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseConsenso, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteConsenso(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseConsenso);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseConsenso.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Fase Aprobación
			tamano = documento.getParticipanteAprobacion()==null?0:
				documento.getParticipanteAprobacion().size();
			if(tamano > 0) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteAprobacion()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseAprobacion, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteAprobacion(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseAprobacion);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseAprobacion.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Fase Homologacion
			tamano = documento.getParticipanteHomologacion()==null?0:
				documento.getParticipanteHomologacion().size();
			if(tamano > 0) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteHomologacion()) {
					participante.setDisponible(documento.getDisponible());
					participante.setIdRevision(item.getRevision().getId());
					participante.setIteracion(item.getRevision().getIteracion());
					if(indicadorFase!=null) {
						if(indicadorFase.equals("3")) {
							participante.setComentario(null);
							participante.setFechaLiberacion(null);
						}
					}
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(), faseHomologacion, usuario);
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteHomologacion(lista);
				
				Flujo objeto = new Flujo();
				objeto.setIdFase(faseHomologacion);
				objeto.setIdDocumento(codigo!= null?codigo:item.getId());
				objeto.setIdRevision(item.getRevision().getId());
				objeto.setIteracion(item.getRevision().getIteracion());
				objeto.setIndAprobacionSoli(null);
				objeto.setIndicadorFase(indicadorFase);
				objeto.setDisponible(Long.parseLong("1"));
				if(indicadorFase!=null) {
					if(!indicadorFase.equals("3")) {
						if(faseHomologacion.longValue()==documento.getBitacora().getIdFase().longValue()) {
							objeto.setComentario(documento.getBitacora().getComentario());
							objeto.setUsuarioFase(idUsuario);
						}
					} else {
						objeto.setIndicadorFase(null);
					}
				}
				Flujo flujo = this.registrarFlujo(objeto, usuario);
				
				if(flujo==null) {
					return null;
				} else if(flujo.getIndicadorAprobado().equals("1")) {
					item.setIndicadorAprobado(flujo.getIndicadorAprobado());
				}
			}
			
			//Registrar Documentos Complementarios
			if(documento.getListaComplementario()!=null) {
				eliminado = this.eliminarComplementario(codigo!= null?codigo:item.getId(), null, item.getRevision().getId(), usuario);
				if(eliminado==false)	{
					return null;
				}
				List<Documento> listaHijo = new ArrayList<>();
				for(Documento documentoHijo : documento.getListaComplementario()) {
					documentoHijo.setDisponible(documento.getDisponible());
					documentoHijo.setIdrevision(item.getRevision().getId()+"");
					Documento hijoTemporal = this.registrarComplementario(documentoHijo, codigo!= null?codigo:item.getId(), usuario);
					if(hijoTemporal==null) {
						return null;
					}
					listaHijo.add(hijoTemporal);
				}
				item.setListaComplementario(listaHijo);
			}
			
			//Registrar Equipo Usuario
			if(documento.getListaEquipo()!=null) {
				eliminado = this.eliminarEquipo(codigo!= null?codigo:item.getId(), null, item.getRevision().getId(), usuario);
				if(eliminado==false)	{
					return null;
				}
				List<Equipo> listaEquipo = new ArrayList<>();
				for(Equipo equipo : documento.getListaEquipo()) {
					equipo.setDisponible(documento.getDisponible());
					equipo.setIdRevision(item.getRevision().getId());
					Equipo equipoTemporal = this.registrarEquipo(equipo, codigo!= null?codigo:item.getId(), usuario);
					if(equipoTemporal==null) {
						return null;
					}
					listaEquipo.add(equipoTemporal);
				}
				item.setListaEquipo(listaEquipo);
			}	
			///cguerra				
			
			//cguerra		
			
			
			//Registrar Historico
			/*boolean historico = this.registrarDocumentoHistorico(codigo, usuario);
			if(historico==false)	{
				return null;
			}*/
			
			/*
			boolean codigoAnterior = this.registrarCodigoAnterior(codigo, item.getCodigo(), usuario);//
			if(codigoAnterior==false)	{
				return null;
			}
			*/
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.guardarDocumento";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return item;
	}
	
	//CGUERRA GOOGLE DRIVE
		@Override
		public Documento obtenerIdGoogle(Long codigo,Long numero,Long idrevi) {
			Map<String, Object> out	= null;
			Documento lista =  null;
			this.error		= null;
			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_GOOGLEDOC")				
				.declareParameters(
					new SqlParameter("i_nid", 				OracleTypes.NUMBER),
					new SqlParameter("i_nidrevi", 			OracleTypes.NUMBER),
					new SqlParameter("i_nnum_revi",			OracleTypes.NUMBER),					
					new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",        OracleTypes.VARCHAR));					
			SqlParameterSource in =
			new MapSqlParameterSource()
					.addValue("i_nid",codigo)
					.addValue("i_nidrevi",idrevi)
					.addValue("i_nnum_revi",numero);			
			try {
				out = this.jdbcCall.execute(in);
				Integer resultado = (Integer)out.get("o_retorno");
				if(resultado == 0) {
					lista = this.mapearGoogleDoc(out);
				} else {
					String mensaje			= (String)out.get("o_mensaje");
					String mensajeInterno	= (String)out.get("o_sqlerrm");
					this.error				= new Error(resultado,mensaje,mensajeInterno);
				}
			}catch(Exception e){
				Integer resultado		= 1;
				String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumento";
				String mensajeInterno	= e.getMessage();
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
			System.out.println(lista);
			return lista;
		}
		//Mapear Google Doc para obtener el ID y Copiar al File Serve
		@SuppressWarnings("unchecked")
		private Documento mapearGoogleDoc(Map<String, Object> resultados) {
			List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");			
			Documento item					= null;
			int size = lista.size();	
			
			for(Map<String, Object> map : lista) {		
				item = new Documento();							
				if (map.get("d_feaprodocu") != null) {
					item.setFechaApro((Date)map.get("d_feaprodocu"));
				}				
				if (map.get("n_iddocgoogledrive") != null) {
					item.setV_id_googleDrive((String)map.get("n_iddocgoogledrive"));
					}
				
				if(map.get("v_ruta_docu")!=null) {
					item.setRutaDocumento((String)map.get("v_ruta_docu"));
				}
				
				if(map.get("v_ruta_docu_office")!=null) {
					item.setRutaDocumentoOriginal((String)map.get("v_ruta_docu_office"));
				}
				if (map.get("rutaoffice") != null) {			
					item.setRutaDocumentoOffice(((String)map.get("rutaoffice")));			
					}
				
				if(map.get("n_inddocdig")!=null) {					
					item.setIndicadorDigital(((BigDecimal)map.get("n_inddocdig")).longValue());
				}
			}			
			return item;
		}
		//CGUERRA GOOGLE DRIVE
		
	public void  actualizarTrasladoDocumento(Documento documento, Long codigo, String usuario) {
	//	logger.msgInfoInicio("AGI - Inicio DocumentoDAOImpl.actualizarTrasladoDocumento()");
		Map<String, Object> out = null;
		this.error=null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_DOCU_UPDATE")
				.declareParameters(
					new SqlParameter("i_n_iddocu", OracleTypes.NUMBER),
					new SqlParameter("i_v_coddocu", OracleTypes.VARCHAR),
					new SqlParameter("i_n_idgeregnrl", OracleTypes.NUMBER),
					new SqlParameter("i_nidalcasgi", OracleTypes.NUMBER),
					new SqlParameter("i_n_idcoor", OracleTypes.NUMBER),
					new SqlParameter("i_n_idproc", OracleTypes.NUMBER),
					new SqlParameter("i_n_idrevision", OracleTypes.NUMBER),
					new SqlParameter("i_n_idcodiante", OracleTypes.VARCHAR),
					new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),	
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_iddocu",	documento.getId())
				.addValue("i_v_coddocu",	null)
				.addValue("i_n_idgeregnrl",	documento.getGerencia())
				.addValue("i_nidalcasgi",	documento.getAlcanceSGI())
				.addValue("i_n_idcoor",	documento.getCoordinador().getIdColaborador())
				.addValue("i_n_idproc",	documento.getProceso())
				.addValue("i_n_idrevision",	documento.getRevision().getId())
				.addValue("i_n_idcodiante",	documento.getListaRevision().get(0).getDocumento().getCodigo())
				.addValue("i_a_v_usumod", "AGI");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				//logger.msgInfoFin("AGI - Fin NoConformidadDAOImpl.modificarEjecucion()");
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			//	logger.msgInfo("AGI - Error DocumentoDAOImpl.actualizarTrasladoDocumento(): ", mensajeInterno);
			//	logger.msgInfoFin("AGI - DocumentoDAOImpl.actualizarTrasladoDocumento()");
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		//	logger.msgError("AGI - Error DocumentoDAOImpl.actualizarTrasladoDocumento(): ", e.getMessage(), e);
		}
	}
	
	//Registro Documento
	@SuppressWarnings("unchecked")
	private Documento registrarDocumento(Documento documento, Long codigo, String usuario) {
		Documento item			= null;
 		Map<String, Object> out = null;
		this.error				= null;		
		try {
			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_DOCUMENTO_GUARDAR")
				.declareParameters(
					new SqlParameter("i_nid",			OracleTypes.NUMBER),
					new SqlParameter("i_nidrevi",		OracleTypes.NUMBER),
					new SqlParameter("i_npadre",		OracleTypes.NUMBER),
					new SqlParameter("i_vtitulo",		OracleTypes.VARCHAR),
					new SqlParameter("i_vrutadocument",	OracleTypes.VARCHAR),
					new SqlParameter("i_nproceso",		OracleTypes.NUMBER),
					new SqlParameter("i_nalcance",		OracleTypes.NUMBER),
					new SqlParameter("i_ngerencia",		OracleTypes.NUMBER),
					new SqlParameter("i_ntipodocu",		OracleTypes.NUMBER),
					new SqlParameter("i_vcodigo",		OracleTypes.VARCHAR),
					new SqlParameter("i_nmotivo",		OracleTypes.NUMBER),
					new SqlParameter("i_vjustificacion",OracleTypes.VARCHAR),
					new SqlParameter("i_nemisor",		OracleTypes.NUMBER),
					new SqlParameter("i_nequipo",		OracleTypes.NUMBER),
					//new SqlParameter("i_ncarpeta",	OracleTypes.NUMBER),
					new SqlParameter("i_nretencion",	OracleTypes.NUMBER),
					new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_nroblid",		OracleTypes.NUMBER),
					new SqlParameter("i_ndigital",		OracleTypes.NUMBER),
					new SqlParameter("v_n_idesta",		OracleTypes.NUMBER),
					new SqlParameter("v_indaprobsoli",	OracleTypes.VARCHAR),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlParameter("i_n_idcoor",		OracleTypes.NUMBER),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nid",				codigo)
				.addValue("i_nidrevi",			documento.getRevision()!= null?documento.getRevision().getId():null)
				.addValue("i_npadre",			(documento.getPadre()==null)?null:documento.getPadre().getId())
				.addValue("i_vtitulo",			documento.getDescripcion())
				.addValue("i_nproceso",			documento.getProceso())//Proceso
				.addValue("i_nalcance",			documento.getAlcanceSGI())//SGI
				.addValue("i_ngerencia",		documento.getGerencia())//Gerencia
				.addValue("i_ntipodocu",		documento.getTipoDocumento())//()==null)?null:documento.getTipoDocumento().getIdconstante())
				.addValue("i_vcodigo",			documento.getCodigo())
				.addValue("i_vrutadocument",	documento.getRutaDocumento())
				.addValue("i_nmotivo",			(documento.getMotivoRevision()==null)?null:documento.getMotivoRevision().getIdconstante())
				.addValue("i_vjustificacion",	documento.getJustificacion())
				.addValue("i_nemisor",			(documento.getEmisor()==null)?null:documento.getEmisor().getIdColaborador())
				.addValue("i_nequipo",			(documento.getEmisor()==null)?null:
												(documento.getEmisor().getEquipo()==null?null:
												documento.getEmisor().getEquipo().getId()))				
				.addValue("i_nretencion",		documento.getRetencionRevision())
				.addValue("i_ndisponible",		documento.getDisponible())
				.addValue("i_nroblid",			documento.getPeriodo())
				.addValue("i_ndigital",			documento.getIndicadorDigital())
				.addValue("v_n_idesta", 		documento.getEstado().getIdconstante())
				.addValue("v_indaprobsoli", 	documento.getIndAprobacionSoli())
				.addValue("i_vusuario",			usuario)
				.addValue("i_n_idcoor",			(documento.getCoordinador()==null)?null:documento.getCoordinador().getIdColaborador());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Documento();
					
					
					item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());					
					item.setCodigo((String)map.get("V_CODDOCU"));
					item.setDescripcion((String)map.get("V_DESDOCU"));
					if ((BigDecimal)map.get("N_VERDOCU")!=null) {
						item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());	
					}
					
					
					if(map.get("N_ROBLI")!=null) {
						//Documento padre = new Documento(); 
						item.setPeriodo(((BigDecimal)map.get("N_ROBLI")).longValue());						
					}
					if(map.get("N_RETDOCU")!=null) {
						item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
					}
					if(map.get("N_INDDOCDIG")!=null) {
						item.setIndicadorDigital(((BigDecimal)map.get("N_INDDOCDIG")).longValue());						
					}
					//Revision Obligatoria
				/*	if(map.get("N_ROBLI")!=null) {						
						item.setRevisonobligatoria(((BigDecimal)map.get("N_ROBLI")).longValue());
						
					}*/
					/* */
					if(map.get("V_JUSTDOCU")!=null) {						
						item.setJustificacion((String)map.get("V_JUSTDOCU"));
					}
					
					if(map.get("V_NOARCHDOCU")!=null) {						
						item.setNombreArchivo((String)map.get("V_NOARCHDOCU"));
					}
					
					//Ruta del Documento FileServer
					if(map.get("V_RUTA_DOCUMT")!=null) {
						item.setRutaDocumento((String)map.get("V_RUTA_DOCUMT"));	
					}
					
					if(map.get("V_RUARCHDOCU")!=null) {						
						item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));
					}
					
					if(map.get("V_INDESTDOCU")!=null) {						
						item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));
					}
					
					if(map.get("V_ESTADESC")!=null) {						
						item.setEstadoDercarga((String)map.get("V_ESTADESC"));
					}
					if(map.get("V_RAIZDOCU")!=null) {						
						item.setRaiz((String)map.get("V_RAIZDOCU"));
						}
					//item.setPeriodo(((BigDecimal)map.get("N_PERIOBLI")).longValue());//
					
					if(map.get("N_IDDOCUHIST")!=null) {
						//Documento padre = new Documento(); 
						item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());//xaqui						
					}	
					if(map.get("N_DISDOCOHI")!=null) {//N_PERIOBLI
						//Documento padre = new Documento(); 
						item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCOHI")));						
					}
					
				//	item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());//xaqui
					//item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCOHI")));

					if(map.get("N_IDDOCUPADR")!=null) {
						Documento padre = new Documento(); 
						padre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
						item.setPadre(padre);
					}
					if(map.get("n_disdocu")!=null) {
						item.setDisponible(((BigDecimal)map.get("n_disdocu")).longValue());
					}
					
					
					if(map.get("N_IDESTA")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
						estado.setV_descons((String)map.get("V_NOESTA"));
						item.setEstado(estado);
					}
					if(map.get("N_IDPROC")!=null) {
						//Jerarquia jproceso = new Jerarquia();					
						item.setProceso(((BigDecimal)map.get("N_IDPROC")).longValue());
					}
					if(map.get("N_IDALCASGI")!=null) {
						//Jerarquia jalcance = new Jerarquia();					
						item.setAlcanceSGI(((BigDecimal)map.get("N_IDALCASGI")).longValue());
					}
					if(map.get("N_IDGEREGNRL")!=null) {//nad
						item.setGerencia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
					}
					if(map.get("N_IDTIPODOCU")!=null) {
						/*Constante tipoDocumento = new Constante();
						tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
						tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
						item.setCtipoDocumento(tipoDocumento);*/
						item.setTipoDocumento(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
					}
					if(map.get("V_IDCODIANTE")!=null) {
						Codigo codigoAnterior = new Codigo();
//						codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
						//codigoAnterior.setCodigo((String)map.get("V_CODIANTE"));
						codigoAnterior.setCodigo((String)map.get("V_IDCODIANTE"));
						item.setCodigoAnterior(codigoAnterior);
					}
					if(map.get("N_IDMOTIREVI")!=null) {
						Constante motivoRevision = new Constante();
						motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
						motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
						item.setMotivoRevision(motivoRevision);
					}
					if(map.get("N_IDEMIS")!=null) {
						Colaborador emisor = new Colaborador();
						emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
						emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
						if(map.get("N_IDEQUIEMIS")!=null) {
							Equipo equipoEmisor = new Equipo();
							equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
							equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
							emisor.setEquipo(equipoEmisor);
						}
						item.setEmisor(emisor);
					}
					if(map.get("N_IDFASE")!=null) {
						Constante fase = new Constante();
						fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
						fase.setV_descons((String)map.get("V_NOFASE"));
						item.setFase(fase);
					}
					if(map.get("N_IDREVI")!=null) {
						Revision revision = new Revision();
						revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
						revision.setFecha((Date)map.get("D_FEREVI"));
						item.setRevision(revision);
					}
					if(map.get("N_IDCARP")!=null) {
						Jerarquia fase = new Jerarquia();
						fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
						item.setNodo(fase);
					}
					if(map.get("N_IDCOPI")!=null) {
						Copia copia = new Copia();
						copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
						item.setCopia(copia);
					}
					if(map.get("N_IDCOOR")!=null) {
						Colaborador coordinador = new Colaborador();
						coordinador.setIdColaborador(((BigDecimal)map.get("N_IDCOOR")).longValue());
						coordinador.setNombreCompleto((String)map.get("V_NOMCOOR"));
						item.setCoordinador(coordinador);
					}
				}

			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.registrarDocumento";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return item;
	}

	public Boolean eliminarDocumento(Long codigo, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_DOCUMENTO_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_nid",			OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",		codigo)
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
			String mensaje			= "Error en DocumentoDAOImpl.eliminarDocumento";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}
	
	//Bloquear Documento
	@SuppressWarnings("unchecked")
	public Flujo bloquearDocumento(Flujo bitacora, String usuario) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Flujo item				= null;
		Map<String, Object> out = null;
		this.error				= null;		
		try {
			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_FLUJO_BLOQUEAR")
				.declareParameters(
					new SqlParameter("i_nid",			OracleTypes.NUMBER),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
					new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
					new SqlParameter("i_nfase",			OracleTypes.NUMBER),
					new SqlParameter("i_nusuario",		OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nid",			bitacora.getIdDocumento())
				.addValue("i_nrevision",	bitacora.getIdRevision())
				.addValue("i_niteracion",	bitacora.getIteracion())
				.addValue("i_nfase",		bitacora.getIdFase())
				.addValue("i_nusuario",		new Long(((UserAuth)principal).getCodPerfil()))
				.addValue("i_vusuario",		usuario);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Flujo();
					if(map.get("N_IDDOCU")!=null) item.setIdDocumento(((BigDecimal)map.get("N_IDDOCU")).longValue());
					if(map.get("N_IDREVI")!=null) item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					if(map.get("N_IDFASE")!=null) item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					if(map.get("V_USUAFASE")!=null) item.setUsuarioFase(((BigDecimal)map.get("V_USUAFASE")).longValue());
					if(map.get("D_FECHFASE")!=null) item.setFechaFase((Date)map.get("D_FECHFASE"));	
					if(map.get("N_INDIREVI")!=null) item.setIndicadorActividad(((BigDecimal)map.get("N_INDIREVI")).longValue());
					if(map.get("N_IDESTAFASE")!=null) item.setIdEstadoFase(((BigDecimal)map.get("N_IDESTAFASE")).longValue());
					if(map.get("V_CRITFASE")!=null) item.setCritica((String)map.get("V_CRITFASE"));	
					if(map.get("N_DISFLUDOC")!=null) item.setDisponible(((BigDecimal)map.get("N_DISFLUDOC")).longValue());
					if(map.get("V_INDIAPRO")!=null) item.setIndicadorAprobado((String)map.get("V_INDIAPRO"));
				}

			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.bloquearDocumento";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return item;
	}
	
	//DesBloquear Documento
	@SuppressWarnings("unchecked")
	public Flujo desBloquearDocumento(String usuario) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Flujo item = null;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_DOCUMENTO")
					.withProcedureName("PRC_FLUJO_DESBLOQUEAR")
					.declareParameters(
							new SqlParameter("i_nusuario", OracleTypes.NUMBER),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nusuario", new Long(((UserAuth)principal).getCodPerfil()))
					.addValue("i_vusuario", usuario);
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>) out.get("o_cursor");
				for (Map<String, Object> map : listado) {
					item = new Flujo();
					if (map.get("N_IDDOCU") != null)
						item.setIdDocumento(((BigDecimal) map.get("N_IDDOCU")).longValue());
					if (map.get("N_IDREVI") != null)
						item.setIdRevision(((BigDecimal) map.get("N_IDREVI")).longValue());
					if (map.get("N_IDFASE") != null)
						item.setIdFase(((BigDecimal) map.get("N_IDFASE")).longValue());
					if (map.get("V_USUAFASE") != null)
						item.setUsuarioFase(((BigDecimal) map.get("V_USUAFASE")).longValue());
					if (map.get("D_FECHFASE") != null)
						item.setFechaFase((Date) map.get("D_FECHFASE"));
					if (map.get("N_INDIREVI") != null)
						item.setIndicadorActividad(((BigDecimal) map.get("N_INDIREVI")).longValue());
					if (map.get("N_IDESTAFASE") != null)
						item.setIdEstadoFase(((BigDecimal) map.get("N_IDESTAFASE")).longValue());
					if (map.get("V_CRITFASE") != null)
						item.setCritica((String) map.get("V_CRITFASE"));
					if (map.get("N_DISFLUDOC") != null)
						item.setDisponible(((BigDecimal) map.get("N_DISFLUDOC")).longValue());
					if (map.get("V_INDIAPRO") != null)
						item.setIndicadorAprobado((String) map.get("V_INDIAPRO"));
				}

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				item = null;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en DocumentoDAOImpl.desBloquearDocumento";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			item = null;
		}
		return item;
	}

	@SuppressWarnings("unchecked")
	private Colaborador registrarParticipante(Colaborador colaborador, Long documento, Long fase, String usuario) {
		Colaborador item		= null;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_PARTICIPANTE_GUARDAR")
				.declareParameters(
					new SqlParameter("i_ncolaborador",	OracleTypes.NUMBER),
					new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
					new SqlParameter("i_nfase",			OracleTypes.NUMBER),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
					new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
					new SqlParameter("i_dplazo",		OracleTypes.DATE),
					new SqlParameter("i_dliberacion",	OracleTypes.DATE),
					new SqlParameter("i_nciclo",		OracleTypes.NUMBER),
					new SqlParameter("i_vcomentario",	OracleTypes.VARCHAR),
					new SqlParameter("i_nplazo",		OracleTypes.NUMBER),
					new SqlParameter("i_nprioridad",	OracleTypes.NUMBER),
					new SqlParameter("i_nequipo",		OracleTypes.NUMBER),
					new SqlParameter("i_vfuncion",		OracleTypes.VARCHAR),
				    new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_ncolaborador",	colaborador.getIdColaborador())
				.addValue("i_ndocumento",	documento)
				.addValue("i_nfase",		fase)
				.addValue("i_nrevision",	colaborador.getIdRevision())
				.addValue("i_niteracion",	colaborador.getIteracion())
				.addValue("i_dplazo",		colaborador.getFechaPlazo())
				.addValue("i_dliberacion",	colaborador.getFechaLiberacion())
				.addValue("i_nciclo",		colaborador.getCiclo())
				.addValue("i_vcomentario",	colaborador.getComentario())
				.addValue("i_nplazo",		colaborador.getPlazo())
				.addValue("i_nprioridad",	colaborador.getPrioridad())
				.addValue("i_nequipo",		colaborador.getEquipo().getId())
				.addValue("i_vfuncion",		colaborador.getFuncion())
				.addValue("i_ndisponible",	colaborador.getDisponible())
				.addValue("i_vusuario",		usuario);
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Colaborador();
					if (map.get("N_IDCOLA")!=null) {
						item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());	
					}
					
					if (map.get("N_IDREVI")!=null) {
						item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());	
					}
					
					if (map.get("N_NUMITER")!=null) {
						item.setIteracion(((BigDecimal)map.get("N_NUMITER")).longValue());	
					}
					
					if (map.get("V_NOCOLA")!=null) {
						item.setNombreCompleto((String)map.get("V_NOCOLA"));	
					}
					
					if (map.get("V_NOFUNC")!=null) {
						item.setFuncion((String)map.get("V_NOFUNC"));	
					}
					
					if (map.get("N_CICLPART")!=null) {
						item.setCiclo(((BigDecimal)map.get("N_CICLPART")).longValue());
					}
					
					if (map.get("V_COMEPART")!=null) {
						item.setComentario((String)map.get("V_COMEPART"));
					}
					
					if (map.get("N_CANTPLAZ")!=null) {
						item.setPlazo(((BigDecimal)map.get("N_CANTPLAZ")).longValue());
					}
					
					if (map.get("N_PRIO")!=null) {
						item.setPrioridad(((BigDecimal)map.get("N_PRIO")).longValue());
					}
					
					if (map.get("D_PLAZPART")!=null) {
						item.setFechaPlazo((Date)map.get("D_PLAZPART"));
					}
					
					if (map.get("D_LIBEPART")!=null) {
						item.setFechaLiberacion((Date)map.get("D_LIBEPART"));
					}
					
					if (map.get("N_DISPART")!=null) {
					    item.setDisponible(((BigDecimal)map.get("N_DISPART")).longValue());
					}
					
					if(map.get("V_NOEQUI")!=null) {
						Equipo equipo = new Equipo();
						equipo.setDescripcion((String)map.get("V_NOEQUI"));
						item.setEquipo(equipo);
					}
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.registrarParticipante";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return item;
	}

	private Boolean eliminarParticipante(Long colaborador, Long documento, Long fase, Long revision, Long iteracion, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_PARTICIPANTE_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_ncolaborador",	OracleTypes.NUMBER),
				new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
				new SqlParameter("i_nfase",			OracleTypes.NUMBER),
				new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
				new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_ncolaborador",	colaborador)
			.addValue("i_ndocumento",	documento)
			.addValue("i_nfase",		fase)
			.addValue("i_nrevision",	revision)
			.addValue("i_niteracion",	iteracion)
			.addValue("i_vusuario",		usuario);

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
	
	@SuppressWarnings("unchecked")
	private Flujo registrarFlujo(Flujo objeto, String usuario) {
		Flujo item				= null;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_FLUJO_GUARDAR")
				.declareParameters(
					new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
					new SqlParameter("i_nfase",			OracleTypes.NUMBER),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
					new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
					new SqlParameter("i_nestado",		OracleTypes.NUMBER),
					new SqlParameter("i_nusuaEstado",	OracleTypes.NUMBER),
					new SqlParameter("i_nactividad",	OracleTypes.NUMBER),
					new SqlParameter("i_vcritica",		OracleTypes.VARCHAR),
					new SqlParameter("i_vcomentario",	OracleTypes.VARCHAR),
					new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlParameter("i_vindaprobsoli",	OracleTypes.VARCHAR),
					new SqlParameter("i_vindfase",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_ndocumento",	 objeto.getIdDocumento())
				.addValue("i_nfase",		 objeto.getIdFase())
				.addValue("i_nrevision",	 objeto.getIdRevision())
				.addValue("i_niteracion",	 objeto.getIteracion())
				.addValue("i_nestado",		 objeto.getIdEstadoFase())
				.addValue("i_nusuaEstado",	 objeto.getUsuarioFase())
				.addValue("i_nactividad",	 objeto.getIndicadorActividad())
				.addValue("i_vcritica",		 objeto.getCritica())
				.addValue("i_vcomentario",	 objeto.getComentario())
				.addValue("i_ndisponible",	 objeto.getDisponible())
				.addValue("i_vusuario",		 usuario)
				.addValue("i_vindaprobsoli", objeto.getIndAprobacionSoli())
				.addValue("i_vindfase",		 objeto.getIndicadorFase());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Flujo();
					if(map.get("N_IDDOCU")!=null) item.setIdDocumento(((BigDecimal)map.get("N_IDDOCU")).longValue());
					if(map.get("N_IDREVI")!=null) item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					if(map.get("N_IDFASE")!=null) item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					if(map.get("V_USUAFASE")!=null) item.setUsuarioFase(((BigDecimal)map.get("V_USUAFASE")).longValue());
					if(map.get("D_FECHFASE")!=null) item.setFechaFase((Date)map.get("D_FECHFASE"));	
					if(map.get("N_INDIREVI")!=null) item.setIndicadorActividad(((BigDecimal)map.get("N_INDIREVI")).longValue());
					if(map.get("N_IDESTAFASE")!=null) item.setIdEstadoFase(((BigDecimal)map.get("N_IDESTAFASE")).longValue());
					if(map.get("V_CRITFASE")!=null) item.setCritica((String)map.get("V_CRITFASE"));	
					if(map.get("N_DISFLUDOC")!=null) item.setDisponible(((BigDecimal)map.get("N_DISFLUDOC")).longValue());
					if(map.get("V_INDIAPRO")!=null) item.setIndicadorAprobado((String)map.get("V_INDIAPRO"));
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.registrarFlujo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return item;
	}

	private Boolean eliminarFlujo(Long documento, Long fase, Long revision, Long iteracion,String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_FLUJO_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
				new SqlParameter("i_nfase",			OracleTypes.NUMBER),
				new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
				new SqlParameter("i_niteracion",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_ndocumento",	documento)
			.addValue("i_nfase",		fase)
			.addValue("i_nrevision",	revision)
			.addValue("i_niteracion",	iteracion)
			.addValue("i_vusuario",		usuario);

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
			String mensaje			= "Error en DocumentoDAOImpl.eliminarFlujo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private Documento registrarComplementario(Documento hijo, Long padre, String usuario) {
		Documento item			= null;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_COMPLEMENTARIO_GUARDAR")
				.declareParameters(
					new SqlParameter("i_nhijo",			OracleTypes.NUMBER),
					new SqlParameter("i_npadre",		OracleTypes.NUMBER),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
					new SqlParameter("i_ntipo",			OracleTypes.NUMBER),
				    new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nhijo",		hijo.getId())
				.addValue("i_npadre",		padre)
				.addValue("i_nrevision",	hijo.getIdrevision())
				.addValue("i_ntipo",		(hijo.getTipoComplementario()==null)?null:
											 hijo.getTipoComplementario().getIdconstante())
				.addValue("i_ndisponible",	hijo.getDisponible())
				.addValue("i_vusuario",		usuario);
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Documento();
                    if(map.get("N_IDDOCU")!=null) {
					    item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
                    }
					if(map.get("V_CODDOCU")!=null) {
					item.setCodigo((String)map.get("V_CODDOCU"));
					}
					if(map.get("V_DESDOCU")!=null) {
					item.setDescripcion((String)map.get("V_DESDOCU"));
					}
					//Ruta del Documento FileServer
					if(map.get("V_RUTA_DOCUMT")!=null) {
						item.setRutaDocumento((String)map.get("V_RUTA_DOCUMT"));	
					}
					
					if(map.get("N_VERDOCU")!=null) {
					item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());
					}
					if(map.get("N_RETDOCU")!=null) {
					item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
					}
					if(map.get("N_PERIOBLI")!=null) {
					item.setPeriodo(((BigDecimal)map.get("N_PERIOBLI")).longValue());
					}
					if(map.get("V_JUSTDOCU")!=null) {
					item.setJustificacion((String)map.get("V_JUSTDOCU"));
					//item.setPeriodo(((BigDecimal)map.get("N_PERIOBLI")).longValue());
					}
					if(map.get("V_NOARCHDOCU")!=null) {
					item.setNombreArchivo((String)map.get("V_NOARCHDOCU"));
					}
					if(map.get("V_RUARCHDOCU")!=null) {
					item.setRutaArchivo((String)map.get("V_RUARCHDOCU"));
					}
					if(map.get("V_INDESTDOCU")!=null) {
					item.setIndicadorAvance((String)map.get("V_INDESTDOCU"));
					}
					if(map.get("V_ESTADESC")!=null) {
					item.setEstadoDercarga((String)map.get("V_ESTADESC"));
					}
					if(map.get("V_RAIZDOCU")!=null) {
					item.setRaiz((String)map.get("V_RAIZDOCU"));
					}
					if(map.get("N_DISDOCCOM")!=null) {
					item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISDOCCOM")));
					}	
					if(map.get("N_IDDOCUPADR")!=null) {
						Documento documentoPadre = new Documento(); 
						documentoPadre.setId(((BigDecimal)map.get("N_IDDOCUPADR")).longValue());
						item.setPadre(documentoPadre);
					}
					if(map.get("N_IDESTA")!=null) {
						Constante estado = new Constante();
						estado.setIdconstante(((BigDecimal)map.get("N_IDESTA")).longValue());
						estado.setV_descons((String)map.get("V_NOESTA"));
						item.setEstado(estado);
					}
					if(map.get("N_IDPROC")!=null) {
						Jerarquia jproceso = new Jerarquia();
						//Constante proceso = new Constante();
						//proceso.setIdconstante(((BigDecimal)map.get("N_IDPROC")).longValue());
						jproceso.setIdJerarquia(((BigDecimal)map.get("N_IDPROC")).longValue());
						//proceso.setV_descons((String)map.get("V_NOPROC"));
						jproceso.setV_descons((String)map.get("V_NOPROC"));
						//item.setProceso(proceso);
						item.setJproceso(jproceso);
					}
					if(map.get("N_IDALCASGI")!=null) {
						Jerarquia jalcance = new Jerarquia();
						//Constante alcance = new Constante();
						jalcance.setIdJerarquia(((BigDecimal)map.get("N_IDALCASGI")).longValue());
						//alcance.setIdconstante(((BigDecimal)map.get("N_IDALCASGI")).longValue());
						jalcance.setV_descons((String)map.get("V_NOALCA"));
						//alcance.setV_descons((String)map.get("V_NOALCA"));
						//item.setAlcanceSGI(alcance);
						item.setJalcanceSGI(jalcance);
					}
					if(map.get("N_IDGEREGNRL")!=null) {
						Jerarquia jgerencia = new Jerarquia();
						//Constante gerencia = new Constante();
						jgerencia.setIdJerarquia(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
						//gerencia.setIdconstante(((BigDecimal)map.get("N_IDGEREGNRL")).longValue());
						jgerencia.setV_descons((String)map.get("V_NOGERE"));
						//gerencia.setV_descons((String)map.get("V_NOGERE"));
						item.setJgerencia(jgerencia);
						//item.setGerencia(gerencia);
					}
					if(map.get("N_IDTIPODOCU")!=null) {
						Constante tipoDocumento = new Constante();
						tipoDocumento.setIdconstante(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
						tipoDocumento.setV_descons((String)map.get("V_NOTIPODOCU"));
						item.setCtipoDocumento(tipoDocumento);
					}
					if(map.get("V_IDCODIANTE")!=null) {
						Codigo codigoAnterior = new Codigo();
						//codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
						codigoAnterior.setCodigo((String)map.get("V_IDCODIANTE"));
						item.setCodigoAnterior(codigoAnterior);
					}
					if(map.get("N_IDMOTIREVI")!=null) {
						Constante motivoRevision = new Constante();
						motivoRevision.setIdconstante(((BigDecimal)map.get("N_IDMOTIREVI")).longValue());
						motivoRevision.setV_descons((String)map.get("V_NOMOTIREVI"));
						item.setMotivoRevision(motivoRevision);
					}
					if(map.get("N_IDEMIS")!=null) {
						Colaborador emisor = new Colaborador();
						emisor.setIdColaborador(((BigDecimal)map.get("N_IDEMIS")).longValue());
						emisor.setNombreCompleto((String)map.get("V_NOEMIS"));
						if(map.get("N_IDEQUIEMIS")!=null) {
							Equipo equipoEmisor = new Equipo();
							equipoEmisor.setId(((BigDecimal)map.get("N_IDEQUIEMIS")).longValue());
							equipoEmisor.setDescripcion((String)map.get("V_NOEQUIEMIS"));
							emisor.setEquipo(equipoEmisor);
						}
						item.setEmisor(emisor);
					}
					if(map.get("N_IDFASE")!=null) {
						Constante fase = new Constante();
						fase.setIdconstante(((BigDecimal)map.get("N_IDFASE")).longValue());
						fase.setV_descons((String)map.get("V_NOFASE"));
						item.setFase(fase);
					}
					if(map.get("N_IDREVI")!=null) {
						Revision revision = new Revision();
						revision.setId(((BigDecimal)map.get("N_IDREVI")).longValue());
						revision.setFecha((Date)map.get("D_FEREVI"));
						item.setRevision(revision);
					}
					if(map.get("N_IDCARP")!=null) {
						Jerarquia fase = new Jerarquia();
						fase.setId(((BigDecimal)map.get("N_IDCARP")).longValue());
						item.setNodo(fase);
					}
					if(map.get("N_IDCOPI")!=null) {
						Copia copia = new Copia();
						copia.setId(((BigDecimal)map.get("N_IDCOPI")).longValue());
						item.setCopia(copia);
					}
					if(map.get("N_IDTIPOCOMP")!=null) {
						Constante tipoComplementario = new Constante();
						tipoComplementario.setIdconstante(((BigDecimal)map.get("N_IDTIPOCOMP")).longValue());
						tipoComplementario.setV_descons((String)map.get("V_NOTIPOCOMP"));
						item.setTipoComplementario(tipoComplementario);
					}
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.registrarComplementario";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return item;
	}

	private Boolean eliminarComplementario(Long padre, Long hijo, Long revision, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_COMPLEMENTARIO_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_npadre",	OracleTypes.NUMBER),
				new SqlParameter("i_nhijo",		OracleTypes.NUMBER),
				new SqlParameter("i_nrevision",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_npadre",	padre)
			.addValue("i_nhijo",	hijo)
			.addValue("i_nrevision",revision)
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
			String mensaje			= "Error en DocumentoDAOImpl.eliminarComplementario";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private Equipo registrarEquipo(Equipo equipo, Long documento, String usuario) {
		Equipo item				= null;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_EQUIPORESPONSABLE_GUARDAR")
				.declareParameters(
					new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
					new SqlParameter("i_nequipo",		OracleTypes.NUMBER),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
					new SqlParameter("i_vrevision",		OracleTypes.VARCHAR),
				    new SqlParameter("i_vnotificacion",	OracleTypes.VARCHAR),
					new SqlParameter("i_vequipousuario",OracleTypes.VARCHAR),
				    new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_ndocumento",		documento)
				.addValue("i_nequipo",			equipo.getId())
				.addValue("i_nrevision",		equipo.getIdRevision())
				.addValue("i_vrevision",		equipo.getIndicadorRevision())
				.addValue("i_vnotificacion",	equipo.getIndicadorNotificacion())
				.addValue("i_vequipousuario",	equipo.getIndicadorResponsable())
				.addValue("i_ndisponible",		equipo.getDisponible())
				.addValue("i_vusuario",			usuario);
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Equipo();
					if(map.get("N_IDEQUI")!= null) {
						item.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
					}
					if(map.get("N_IDREVI")!= null) {
						item.setIdRevision(((BigDecimal)map.get("N_IDREVI")).longValue());
					}
					if(map.get("V_NOMEQUI")!= null) {
						item.setDescripcion((String)map.get("V_NOMEQUI"));
					}
					if(map.get("N_DISEQUI")!= null) {
						item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISEQUI")));
					}
					if(map.get("V_INDREVI")!= null) {
						item.setIndicadorRevision(((BigDecimal)map.get("V_INDREVI")).longValue());
					}
					if(map.get("V_INDNOTI")!= null) {
						item.setIndicadorNotificacion(((BigDecimal)map.get("V_INDNOTI")).longValue());
					}
					if(map.get("N_INDIRESP")!= null) {
						item.setIndicadorResponsable(((BigDecimal)map.get("N_INDIRESP")).longValue());
					}
					if(map.get("N_IDDOCUHIST")!= null) {
						item.setIdHistorial(((BigDecimal)map.get("N_IDDOCUHIST")).longValue());
					}
					if(map.get("N_JEFEQUI")!=null) {
						Colaborador jefe = new Colaborador();
						jefe.setIdColaborador(((BigDecimal)map.get("N_JEFEQUI")).longValue());
						jefe.setNombreCompleto((String)map.get("V_JEFEQUI"));
						item.setJefe(jefe);
					}
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.registrarEquipo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return item;
	}

	private Boolean eliminarEquipo(Long documento, Long equipo, Long revision, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_EQUIPORESPONSABLE_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
				new SqlParameter("i_nequipo",	OracleTypes.NUMBER),
				new SqlParameter("i_nrevision",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_ndocumento",	documento)
			.addValue("i_nequipo",		equipo)
			.addValue("i_nrevision",	revision)
			.addValue("i_vusuario",		usuario);

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
			String mensaje			= "Error en DocumentoDAOImpl.eliminarEquipo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}

	public String generarCodigoDocumento (Long codigoGerencia, Long codigoTipoDocumento) {
		this.error = null;
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc);
		jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_DOCUMENTO").withProcedureName("PRC_GENERAR_CODIGO");
		jdbcCall.declareParameters(
				new SqlParameter("i_ngerencia", OracleTypes.NUMBER),
				new SqlParameter("i_nidtipodocu", OracleTypes.NUMBER),
				new SqlOutParameter("o_codigo", OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)
		);
		SqlParameterSource in = new MapSqlParameterSource()
													.addValue("i_ngerencia", codigoGerencia)
													.addValue("i_nidtipodocu", codigoTipoDocumento);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				return (String) out.get("o_codigo");
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				return "";
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.eliminarEquipo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return "";
		}

	}

	/**
     * Crea objeto de autenticación.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return Un bjeto de autenticación.
     * @throws IOException si el credentials.json no es encontrado.
     */
	    private  Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {			
	    	DocumentoDAOImpl.CREDENTIALS_FILE_PATH= env.getProperty("app.config.paths.carpetaResources");
			DocumentoDAOImpl.TOKENS_DIRECTORY_PATH= env.getProperty("app.config.paths.toke.directory");	
	    	InputStream in = new java.io.FileInputStream(new java.io.File(CREDENTIALS_FILE_PATH));
	        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
	                .setAccessType("offline")
	                .build();
	        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
	        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	    }
	
	@SuppressWarnings("unchecked")
	private List<Colaborador> obtenerParticipanteAll(Long codigo, Long fase, Long revision, Long iteracion,
			PageRequest pagina) {

		Map<String, Object> out = null;
		List<Colaborador> lista = new ArrayList<>();

		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_PARTICIPANTE_OBTENER_ALL")
				.declareParameters(
						new SqlParameter("i_nid", OracleTypes.NUMBER), 
						new SqlParameter("i_nfase", OracleTypes.VARCHAR),
						new SqlParameter("i_nrevision", OracleTypes.NUMBER),
						new SqlParameter("i_niteracion", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nid", codigo)
				.addValue("i_nfase", fase)
				.addValue("i_nrevision", revision)
				.addValue("i_niteracion", iteracion)
				.addValue("i_npagina", pagina.getPagina())
				.addValue("i_nregistros", pagina.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>) out.get("o_cursor");
				for (Map<String, Object> map : listado) {
					Colaborador item = new Colaborador();
					if (map.get("N_IDCOLA") != null)
						item.setIdColaborador(((BigDecimal) map.get("N_IDCOLA")).longValue());
					if (map.get("N_IDREVI") != null)
						item.setIdRevision(((BigDecimal) map.get("N_IDREVI")).longValue());
					if (map.get("N_NUMITER") != null)
						item.setIteracion(((BigDecimal) map.get("N_NUMITER")).longValue());
					if (map.get("V_NOCOLA") != null)
						item.setNombreCompleto((String) map.get("V_NOCOLA"));
					if (map.get("V_NOFUNC") != null)
						item.setFuncion((String) map.get("V_NOFUNC"));
					if (map.get("N_CICLPART") != null)
						item.setCiclo((map.get("N_CICLPART") == null) ? null
								: ((BigDecimal) map.get("N_CICLPART")).longValue());
					if (map.get("V_COMEPART") != null)
						item.setComentario((String) map.get("V_COMEPART"));
					if (map.get("N_CANTPLAZ") != null)
						item.setPlazo(((BigDecimal) map.get("N_CANTPLAZ") == null) ? null
								: ((BigDecimal) map.get("N_CANTPLAZ")).longValue());
					if (map.get("D_PLAZPART") != null)
						item.setFechaPlazo((Date) map.get("D_PLAZPART"));
					if (map.get("D_LIBEPART") != null)
						item.setFechaLiberacion((Date) map.get("D_LIBEPART"));
					if (map.get("N_DISPART") != null)
						item.setDisponible(((BigDecimal) map.get("N_DISPART")).longValue());
					if (map.get("N_PRIO") != null)
						item.setPrioridad(
								(map.get("N_PRIO") == null) ? null : ((BigDecimal) map.get("N_PRIO")).longValue());
					if (map.get("V_NOCOLA") != null)
						item.setResponsable((String) map.get("V_NOCOLA"));
					if (map.get("N_IDFASE") != null)
						item.setIdFase(((BigDecimal) map.get("N_IDFASE")).longValue());
					if (map.get("V_NOFASE") != null)
						item.setNombreFase((String) map.get("V_NOFASE"));
					if (map.get("N_INDRECH") != null)
						item.setIndicadorRechazo((map.get("N_INDRECH") == null) ? null
								: ((BigDecimal) map.get("N_INDRECH")).longValue());
					if (map.get("D_RECHPART") != null)
						item.setFechaRechazo((Date) map.get("D_RECHPART"));
					if (map.get("N_IDTRAB") != null)
						item.setIdTrabajador(
								map.get("N_IDTRAB") == null ? null : ((BigDecimal) map.get("N_IDTRAB")).longValue());
					if (map.get("N_INDTRAB") != null)
						item.setIndicadorTrabajador(((BigDecimal) map.get("N_INDTRAB")).longValue());
					if (map.get("N_INDEQUI") != null)
						item.setIndicadorEquipo(((BigDecimal) map.get("N_INDEQUI")).longValue());
					if (map.get("N_INDFUNC") != null)
						item.setIndicadorFuncion(((BigDecimal) map.get("N_INDFUNC")).longValue());
					if (map.get("N_INDJEFE") != null)
						item.setIndicadorJefe(((BigDecimal) map.get("N_INDJEFE")).longValue());
					if (map.get("N_INDJEFACT") != null)
						item.setIndicadorJefeActual(((BigDecimal) map.get("N_INDJEFACT")).longValue());
					if ((item.getIndicadorTrabajador() == 0 || item.getIndicadorEquipo() == 0
							|| (item.getIndicadorJefe() == 1 && item.getIndicadorJefeActual() == 0))
							&& item.getFechaLiberacion() == null) {
						item.setEstiloBloqueado(true);
						if (item.getIndicadorTrabajador() == 0)
							item.setTextoBloqueado(Mensaje.MENS_TRAB_BAJA.toString());
						else if (item.getIndicadorEquipo() == 0)
							item.setTextoBloqueado(Mensaje.MENS_EQUI_BAJA.toString());
						else if (item.getIndicadorJefe() == 1 && item.getIndicadorJefeActual() == 0)
							item.setTextoBloqueado(Mensaje.MENS_CARG_BAJA.toString());
					} else {
						item.setEstiloBloqueado(false);
						item.setTextoBloqueado("");
					}

					Equipo equipo = new Equipo();
					equipo.setId((map.get("N_IDEQUI") == null) ? null : ((BigDecimal) map.get("N_IDEQUI")).longValue());
					equipo.setDescripcion((String) map.get("V_NOEQUI"));
					item.setEquipoColaborador((String) map.get("V_NOEQUI"));
					item.setEquipo(equipo);

					lista.add(item);

					if (map.get("RESULT_COUNT") != null) {
						this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
					}
				}
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en DocumentoDAOImpl.obtenerParticipante";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
}