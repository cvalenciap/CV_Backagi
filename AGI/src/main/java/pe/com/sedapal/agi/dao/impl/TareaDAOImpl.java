package pe.com.sedapal.agi.dao.impl;

import oracle.jdbc.OracleTypes;
import com.google.api.services.drive.model.File;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
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


import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.ITareaDAO;
import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Copia;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.SolicitudDocumentoComplementario;
import pe.com.sedapal.agi.model.enums.EstadoConstante;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.impl.TareaServiceImpl;
import pe.com.sedapal.agi.util.UArchivo;
import pe.com.sedapal.agi.util.UConstante;
import pe.sedapal.componentes.fs.ClientFS;

//import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class TareaDAOImpl implements ITareaDAO {
    private Error error;
    private Paginacion paginacion;
    private SimpleJdbcCall jdbcCall;

    
    @Autowired
	private JdbcTemplate	jdbc;	
    
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
    
    
    private static final Logger LOGGER = Logger.getLogger(TareaDAOImpl.class);	
    
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
    
    

    @Override
    public List<Cancelacion> obtenerSolicitudesCancelacion(TareaRequest tareaRequest, PageRequest pageRequest, Long iUsuario) {
        List<Cancelacion> solicitudesCancelacion = new ArrayList<>();
        
        try {
				this.error = null;
			    this.paginacion = new Paginacion();
			    this.paginacion.setPagina(pageRequest.getPagina());
			    this.paginacion.setRegistros(pageRequest.getRegistros());
			
			    this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
			    this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
			            .withProcedureName("PRC_LISTADO_CANCELADO").declareParameters(
			            new SqlParameter("i_idusuario", OracleTypes.NUMBER),
			            new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
			            new SqlParameter("i_vcoddocu", OracleTypes.VARCHAR),
			            new SqlParameter("i_vdesdocu", OracleTypes.VARCHAR),
			            new SqlParameter("i_vnombres", OracleTypes.VARCHAR),
			            new SqlParameter("i_vapepat", OracleTypes.VARCHAR),
			            new SqlParameter("i_vapemat", OracleTypes.VARCHAR),
			            new SqlParameter("i_nestcanc", OracleTypes.VARCHAR),
			            new SqlParameter("i_nestdoc", OracleTypes.NUMBER),
			            new SqlParameter("i_nidproc", OracleTypes.NUMBER),
			            new SqlParameter("i_nidalcasgi", OracleTypes.NUMBER),
			            new SqlParameter("i_nidgeregnrl", OracleTypes.NUMBER),
			            new SqlParameter("i_nidtipodocu", OracleTypes.NUMBER),
			            new SqlParameter("i_nfechacandesde",	OracleTypes.DATE),
						new SqlParameter("i_nfechacanhasta",	OracleTypes.DATE),
			            new SqlParameter("i_npagina", OracleTypes.NUMBER),
			            new SqlParameter("i_nregistros", OracleTypes.NUMBER),
			            new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
			    );
						
			    SqlParameterSource parametros = new MapSqlParameterSource()
			    		.addValue("i_idusuario", iUsuario)
	                    .addValue("i_nidcanc", tareaRequest.getCodigoSolicitud())
	                    .addValue("i_vcoddocu", tareaRequest.getCodigoDocumento())
	                    .addValue("i_vdesdocu", tareaRequest.getTituloDocumento())
	                    .addValue("i_vnombres", tareaRequest.getNombres())
	                    .addValue("i_vapepat", tareaRequest.getApellidoPaterno())
	                    .addValue("i_vapemat", tareaRequest.getApellidoMaterno())
	                    .addValue("i_nestcanc", tareaRequest.getEstado())
	                    .addValue("i_nestdoc", tareaRequest.getEstadoDoc())
	                    .addValue("i_npagina", pageRequest.getPagina())
	                    .addValue("i_nregistros", pageRequest.getRegistros())
			    		.addValue("i_nidproc",tareaRequest.getIdproc())
						.addValue("i_nidalcasgi",tareaRequest.getIdalcasgi())
						.addValue("i_nidgeregnrl",tareaRequest.getIdgeregnrl())
						.addValue("i_nidtipodocu",tareaRequest.getIdtipodocu())
						.addValue("i_nfechacandesde",tareaRequest.getFechacandesde())
						.addValue("i_nfechacanhasta",tareaRequest.getFechacanhasta());
	        Map<String, Object> out = this.jdbcCall.execute(parametros);
	        Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				solicitudesCancelacion = mapearCancelacion(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado.intValue(),mensaje,mensajeInterno);
			}
		}catch(Exception e){
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.obtenerSolicitudesCancelacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return solicitudesCancelacion;
    }
    
    @Override
	public List<Cancelacion> obtenerSolicitudesCancelacionReporte(TareaRequest tareaRequest, PageRequest pageRequest,
			Long iUsuario) {
		List<Cancelacion> solicitudesCancelacion = new ArrayList<>();

		try {
			this.error = null;
			this.paginacion = new Paginacion();
			this.paginacion.setPagina(pageRequest.getPagina());
			this.paginacion.setRegistros(pageRequest.getRegistros());

			this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
			this.jdbcCall.withSchemaName("AGI")
						 .withCatalogName("PCK_AGI_TAREAS")
						 .withProcedureName("PRC_LISTADO_CANCELADO_REPORTE")
						 .declareParameters(
							new SqlParameter("i_idusuario", OracleTypes.NUMBER),
							new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
							new SqlParameter("i_vcoddocu", OracleTypes.VARCHAR),
							new SqlParameter("i_vdesdocu", OracleTypes.VARCHAR),
							new SqlParameter("i_vnombres", OracleTypes.VARCHAR),
							new SqlParameter("i_vapepat", OracleTypes.VARCHAR),
							new SqlParameter("i_vapemat", OracleTypes.VARCHAR),
							new SqlParameter("i_nestcanc", OracleTypes.VARCHAR),
							new SqlParameter("i_nestdoc", OracleTypes.NUMBER),
							new SqlParameter("i_nidproc", OracleTypes.NUMBER),
							new SqlParameter("i_nidalcasgi", OracleTypes.NUMBER),
							new SqlParameter("i_nidgeregnrl", OracleTypes.NUMBER),
							new SqlParameter("i_nidtipodocu", OracleTypes.NUMBER),
							new SqlParameter("i_nfechacandesde", OracleTypes.DATE),
							new SqlParameter("i_nfechacanhasta", OracleTypes.DATE),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource parametros = new MapSqlParameterSource()
					.addValue("i_idusuario", iUsuario)
					.addValue("i_nidcanc", tareaRequest.getCodigoSolicitud())
					.addValue("i_vcoddocu", tareaRequest.getCodigoDocumento())
					.addValue("i_vdesdocu", tareaRequest.getTituloDocumento())
					.addValue("i_vnombres", tareaRequest.getNombres())
					.addValue("i_vapepat", tareaRequest.getApellidoPaterno())
					.addValue("i_vapemat", tareaRequest.getApellidoMaterno())
					.addValue("i_nestcanc", tareaRequest.getEstado()).addValue("i_nestdoc", tareaRequest.getEstadoDoc())
					.addValue("i_npagina", pageRequest.getPagina()).addValue("i_nregistros", pageRequest.getRegistros())
					.addValue("i_nidproc", tareaRequest.getIdproc())
					.addValue("i_nidalcasgi", tareaRequest.getIdalcasgi())
					.addValue("i_nidgeregnrl", tareaRequest.getIdgeregnrl())
					.addValue("i_nidtipodocu", tareaRequest.getIdtipodocu())
					.addValue("i_nfechacandesde", (Date)tareaRequest.getFechacandesde())
					.addValue("i_nfechacanhasta", (Date)tareaRequest.getFechacanhasta());
			Map<String, Object> out = this.jdbcCall.execute(parametros);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				solicitudesCancelacion = mapearCancelacion(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");				
				this.error = new Error(resultado.intValue(), mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en TareaDaoImpl.obtenerSolicitudesCancelacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return solicitudesCancelacion;
	}
    
    @SuppressWarnings("unchecked")
	private List<Cancelacion> mapearCancelacion(Map<String,Object> resultados){
    	List<Cancelacion> listaSolicitudCancelacion = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Cancelacion item = null;
		for(Map<String, Object> map : lista) {	
			item = new Cancelacion();
			
			if(map.get("codigo") != null) {
			item.setIdSolicitudCancelacion(((BigDecimal)map.get("codigo")).longValue());
			}
			
			if(map.get("numTipoCanc") != null) {
				item.setNumTipoCancelacion(((BigDecimal)map.get("numTipoCanc")).longValue());
			}
			if(map.get("tipoCancelacion") != null) {
				item.setTipoCancelacion((String)map.get("tipoCancelacion"));
			}
			
			if(map.get("numMotivoCanc") != null) {
				item.setNumMotivoCancelacion(((BigDecimal)map.get("numMotivoCanc")).longValue());
			}
			if(map.get("motivo") != null) {
			item.setMotivoCancelacion((String)map.get("motivo"));
			}
			if(map.get("sustentoSolicitado") != null) {
			item.setSustentoSolicitud((String)map.get("sustentoSolicitado"));
			}
			if(map.get("sustentoAprobado") != null) {
			item.setSustentoAprobacion((String)map.get("sustentoAprobado"));
			}
			if(map.get("fechaSolicitud") != null) {
				item.setFechaSolicitud(new Date(((Timestamp)map.get("fechaSolicitud")).getTime()));	
			}
			if(map.get("fechaAprobacion") != null) {				
				item.setFechaAprobacion(new Date(((Timestamp)map.get("fechaAprobacion")).getTime()));		
			}
			if(map.get("fechaCancelacion") != null) {
				item.setFechaCancelacion(new Date(((Timestamp)map.get("fechaCancelacion")).getTime()));	
			}	
			if(map.get("numEstadoCanc") != null) {
				item.setNumEstadoDocumento(((BigDecimal)map.get("numEstadoCanc")).longValue());
			}
			
			if(map.get("estado") != null) {
			item.setEstadoSolicitud((String)map.get("estado"));
			}
			if(map.get("idRevision") != null) {
			item.setIdRevision(((BigDecimal)map.get("idRevision")).longValue());
			}
			if(map.get("numeroRevision") != null) {
				item.setNumeroRevision(((BigDecimal)map.get("numeroRevision")).intValue());
			}
			if(map.get("nroCodigoDocumento") != null) {
				item.setIdDocumento(((BigDecimal)map.get("nroCodigoDocumento")).longValue());
			}
			if(map.get("codigoDocumento") != null) {
				item.setCodigoDocumento((String)map.get("codigoDocumento"));
			}
			if(map.get("descripcionDocumento") != null) {
				item.setTituloDocumento((String)map.get("descripcionDocumento"));
			}
			
			if(map.get("estadoDocumento") != null) {
				item.setEstadoDocumento((String)map.get("estadoDocumento"));
			}
			if(map.get("nombre") !=null) {
				item.setNombreColaborador((String) map.get("nombre"));
			}
			if(map.get("apellidoPaterno") != null) {
				item.setApePatColaborador((String) map.get("apellidoPaterno"));
			}
			
			if(map.get("apellidoMaterno") != null) {
				item.setApeMatColaborador((String) map.get("apellidoMaterno"));
			}
			if(map.get("Aprobador") != null) {
				item.setAprobador((String) map.get("Aprobador"));
			}
			
			if(map.get("Cancelador") != null) {
				item.setCancelador((String) map.get("Cancelador"));
			}
			
			
			
			if(map.get("rutaSustentoCanc") != null) {
				item.setRutaArchivoSustento((String)map.get("rutaSustentoCanc"));
			}
			
			if(map.get("nombreArchivo") != null) {
				item.setNombreArchivoSustento((String)map.get("nombreArchivo"));
			}
			
			if(map.get("sustentoRechazo") != null) {
				item.setSustentoRechazo((String)map.get("sustentoRechazo"));
			}
			
			
			listaSolicitudCancelacion.add(item);
			
			if (map.get("numeroRegistros")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("numeroRegistros")).intValue());
			}

		}
		
		return listaSolicitudCancelacion;
    }

    @Override
    public Cancelacion crearActualizarSolicitudCancelacion(Cancelacion cancelacion) {
    	Cancelacion cancelacionObtenido = null;
    	Long idSolicitudCancelacion = null;
        this.error = null;
        String mensajeRespuesta = "";
        System.out.println("Tipo: "+cancelacion.getTipo());
        try {
        	this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
            this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
                    .withProcedureName("PRC_SOLICITUD_GUARDAR").declareParameters(
                    new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
                    new SqlParameter("i_nusuario", OracleTypes.NUMBER),
                    new SqlParameter("i_niddocu", OracleTypes.NUMBER),
                    new SqlParameter("i_nmotcanc", OracleTypes.NUMBER),
                    new SqlParameter("i_ntipcanc", OracleTypes.NUMBER),
                    new SqlParameter("i_vsussoli", OracleTypes.VARCHAR),
                    new SqlParameter("i_vrutcanc", OracleTypes.VARCHAR),
                    new SqlParameter("i_tipo", OracleTypes.NUMBER),
                    new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
                    new SqlParameter("i_vnomarcsus", OracleTypes.VARCHAR),
                    new SqlParameter("i_usuaapro", OracleTypes.NUMBER),
                    new SqlOutParameter("o_resultado",	OracleTypes.NUMBER),
                    new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
    				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
    				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
            );

            SqlParameterSource parametros = new MapSqlParameterSource()
                    .addValue("i_vusuario",cancelacion.getDatosAuditoria().getUsuarioCreacion())
                    .addValue("i_nusuario",cancelacion.getIdColaborador())
                    .addValue("i_niddocu", cancelacion.getIdDocumento())
                    .addValue("i_nmotcanc", cancelacion.getNumMotivoCancelacion())
                    .addValue("i_ntipcanc", cancelacion.getNumTipoCancelacion())
                    .addValue("i_vsussoli", cancelacion.getSustentoSolicitud())
                    .addValue("i_vrutcanc", cancelacion.getRutaArchivoSustento())
                    .addValue("i_tipo", cancelacion.getTipo())
                    .addValue("i_nidcanc", cancelacion.getIdSolicitudCancelacion())
                    .addValue("i_vnomarcsus", cancelacion.getNombreArchivoSustento())
                    .addValue("i_usuaapro", cancelacion.getIdUsuAprobador());

            Map<String, Object> out = this.jdbcCall.execute(parametros);
            
            Integer codigoRetorno = (Integer) out.get("o_retorno");
            if (codigoRetorno != 0) {
            	mensajeRespuesta		= (String)out.get("o_mensaje");
    			String mensajeInterno	= (String)out.get("o_sqlerrm");    			
    			this.error				= new Error(codigoRetorno,mensajeRespuesta,mensajeInterno);
            } else {
            	cancelacionObtenido = cancelacion;
            	if(cancelacion.getTipo()==1 || cancelacion.getTipo()==4) {
            		if(out.get("o_resultado") instanceof Integer) {
        	        	System.out.println("Integer");
        	        }
        	        if(out.get("o_resultado") instanceof BigDecimal) {
        	        	System.out.println("BigDecimal");
        	        }
        	        if(out.get("o_resultado") instanceof BigInteger) {
        	        	System.out.println("BigInteger");
        	        }
        	        if(out.get("o_resultado") instanceof Long) {
        	        	System.out.println("Long");
        	        }
            		idSolicitudCancelacion = ((BigDecimal)out.get("o_resultado")).longValue();
            		cancelacionObtenido.setIdSolicitudCancelacion(idSolicitudCancelacion);
            	}
            	
                mensajeRespuesta = (String) out.get("o_mensaje");
            }
        }catch(Exception e){
        	/*
        	ClientFS cliente = new ClientFS();
        	if((cancelacion.getTipo()==1 || cancelacion.getTipo()==4) && cancelacion.getRutaArchivoSustento()!=null) {
        		cliente.eliminarArchivo(cancelacion.getRutaArchivoSustento());
        	}
        	*/
        	
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.crearActualizarSolicitudCancelacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
        return cancelacionObtenido;
    }

    @Override
    public String subirDocumentoGoogleDrive(TareaRequest tareaRequest, String idDocGoogle, String iUsuario) {
        this.error = null;
        String mensajeRespuesta = "";
        this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
        this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
                .withProcedureName("PRC_SUBIR_DOC_GOOGLEDRIVE").declareParameters(
                new SqlParameter("i_n_idrevi", OracleTypes.NUMBER),
                new SqlParameter("i_n_iddocu", OracleTypes.NUMBER),
                new SqlParameter("i_v_ruta_docu", OracleTypes.VARCHAR),
                new SqlParameter("i_n_iddocgoogledrive", OracleTypes.VARCHAR),
                new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR)
        );

        SqlParameterSource parametros = new MapSqlParameterSource()
                .addValue("i_n_idrevi", tareaRequest.getIdRevisionSelecc())
                .addValue("i_n_iddocu", tareaRequest.getIdDocumenSelecc())
                .addValue("i_v_ruta_docu", tareaRequest.getRutaDocuSelecc())
                .addValue("i_n_iddocgoogledrive", idDocGoogle)
        		.addValue("i_a_v_usumod", iUsuario);
        
        Map<String, Object> resultadoDatos = this.jdbcCall.execute(parametros);
        BigDecimal codigoRetorno = (BigDecimal) resultadoDatos.get("O_RETORNO");
        if (codigoRetorno.intValue() != 0) {
            String mensajeError = (String) resultadoDatos.get("O_MENSAJE");
            String errorSql = (String) resultadoDatos.get("O_SQLERRM");
            error = new Error(codigoRetorno.intValue(), mensajeError, errorSql);
        } else {
            mensajeRespuesta = (String) resultadoDatos.get("O_MENSAJE");
        }
        return mensajeRespuesta;
    }
    
    @Override
    public Error obtenerError() {
        return this.error;
    }

    @Override
    public Paginacion obtenerPaginacion() { return this.paginacion; }

	@Override
	public Cancelacion obtenerSolicitudCancelacion(Long idCancelacion) {
		Cancelacion datosSolicitudCancelacion = null;
        
        try {
				this.error = null;
			
			    this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
			    this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
			            .withProcedureName("PRC_SOLICITUD_CANC_OBTENER").declareParameters(
			            new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
			            new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
			    );
						
			    SqlParameterSource parametros = new MapSqlParameterSource()
	                    .addValue("i_nidcanc", idCancelacion);

	        Map<String, Object> out = this.jdbcCall.execute(parametros);
	        Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				List<Cancelacion> solicitudesCancelacion = mapearCancelacion(out);
				if(solicitudesCancelacion.size()>0) {
					datosSolicitudCancelacion = solicitudesCancelacion.get(0);
				}
				
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado.intValue(),mensaje,mensajeInterno);
			}
		}catch(Exception e){
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.obtenerSolicitudesCancelacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return datosSolicitudCancelacion;
	}

	@Override
	public boolean crearActualizarSolicitudCancDocComplementario(
			SolicitudDocumentoComplementario solicitudDocumentoComplementario) {
		boolean respuesta= false;
        this.error = null;
        String mensajeRespuesta = "";
        System.out.println("Tipo: "+solicitudDocumentoComplementario.getTipo());
        try {
        	this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
            this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
                    .withProcedureName("PRC_SOLI_DOC_COMP_GUARDAR").declareParameters(
                    new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
                    new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
                    new SqlParameter("i_niddocu", OracleTypes.NUMBER),
                    new SqlParameter("i_vindcanc", OracleTypes.VARCHAR),
                    new SqlParameter("i_tipo"	, OracleTypes.NUMBER),
                    new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
    				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
    				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
            );

            SqlParameterSource parametros = new MapSqlParameterSource()
                    .addValue("i_vusuario",solicitudDocumentoComplementario.getDatosAuditoria().getUsuarioCreacion())
                    .addValue("i_nidcanc",solicitudDocumentoComplementario.getIdSolicitudCancelacion())
                    .addValue("i_niddocu", solicitudDocumentoComplementario.getIdDocumento())
                    .addValue("i_vindcanc", solicitudDocumentoComplementario.getIndicadorSolicitud())
                    .addValue("i_tipo", solicitudDocumentoComplementario.getTipo());

            Map<String, Object> out = this.jdbcCall.execute(parametros);
            Integer codigoRetorno = (Integer) out.get("o_retorno");
            if (codigoRetorno != 0) {
            	mensajeRespuesta		= (String)out.get("o_mensaje");
    			String mensajeInterno	= (String)out.get("o_sqlerrm");    			
    			this.error				= new Error(codigoRetorno,mensajeRespuesta,mensajeInterno);
            } else {
            	respuesta = true;
                mensajeRespuesta = (String) out.get("o_mensaje");
            }
        }catch(Exception e){
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.crearActualizarSolicitudCancDocComplementario";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
        

        return respuesta;
	}

	@Override
	@Transactional
	public boolean crearActualizarDatosSolicitudCancelacion(Cancelacion cancelacion) {
		// TODO Auto-generated method stub
		boolean respuesta = false;
		try {
			Cancelacion cancelacionObtenido = this.crearActualizarSolicitudCancelacion(cancelacion);
			if(cancelacionObtenido != null) {
				int tipo = cancelacion.getTipo();
				List<SolicitudDocumentoComplementario> listaSolicitud = cancelacionObtenido.getListaSolicitudesDocComp();
				if(tipo == 1 || tipo == 4) {
					listaSolicitud.forEach(obj -> obj.setIdSolicitudCancelacion(cancelacionObtenido.getIdSolicitudCancelacion()));
				}
				
				if(tipo == 2 || tipo == 3) {
					tipo = 2;
				}else if(tipo == 1 || tipo == 4) {
					tipo = 1;
				}
				
				
				//Para el caso especial de una creacion y una actualización hay que actualizar este codigo
			    
				
			    if(tipo == 1) {
			    	for(SolicitudDocumentoComplementario solicitud: listaSolicitud) {
						solicitud.setDatosAuditoria(cancelacion.getDatosAuditoria());
						solicitud.setTipo(tipo);
						respuesta = this.crearActualizarSolicitudCancDocComplementario(solicitud);
						if(!respuesta) {
							break;
						}
					}
			    }else {
			    	List<SolicitudDocumentoComplementario> listaDocCompActu = this.obtenerSolicitudesDocumentoComplementario(cancelacion.getIdSolicitudCancelacion());
			    	for(SolicitudDocumentoComplementario solicitud: listaSolicitud) {
			    		List<SolicitudDocumentoComplementario> listaAux = listaDocCompActu.stream().filter(obj -> obj.getIdDocumento().equals(solicitud.getIdDocumento())).collect(Collectors.toList());
			    		if(listaAux.size()>0) {
			    			solicitud.setDatosAuditoria(cancelacion.getDatosAuditoria());
							solicitud.setTipo(2);
							respuesta = this.crearActualizarSolicitudCancDocComplementario(solicitud);
			    		}else {
			    			solicitud.setDatosAuditoria(cancelacion.getDatosAuditoria());
							solicitud.setTipo(1);
							solicitud.setIdSolicitudCancelacion(cancelacion.getIdSolicitudCancelacion());
							respuesta = this.crearActualizarSolicitudCancDocComplementario(solicitud);
			    		}
			    		if(!respuesta) {
							break;
						}
			    	}
			    }
				
				
				
				/*Crear solicitudes de Documentos complementarios*/
				if(cancelacion.getTipo() == 3 || cancelacion.getTipo() == 4) {
					
					List<SolicitudDocumentoComplementario> listaSolicitudes = this.obtenerSolicitudesDocumentoComplementario(cancelacionObtenido.getIdSolicitudCancelacion());
					for(SolicitudDocumentoComplementario solicitud:listaSolicitudes) {
						if(solicitud.getIndicadorSolicitud().equals("1")) {
							Constante constanteEstado = solicitud.getDocumento().getEstado();
							if(!constanteEstado.getV_descons().equals("Cancelado")){
								Cancelacion solicitudCancelacionObt = this.obtenerSolicitudCancelacionActivaDeDocumento(solicitud.getIdDocumento());
								if(solicitudCancelacionObt == null) {
									Cancelacion nuevaCancelacion = new Cancelacion();
									nuevaCancelacion.setIdDocumento(solicitud.getIdDocumento());
									nuevaCancelacion.setNumMotivoCancelacion(cancelacion.getNumMotivoCancelacion());
									nuevaCancelacion.setNumTipoCancelacion(cancelacion.getNumTipoCancelacion());
									nuevaCancelacion.setIdColaborador(cancelacion.getIdColaborador());
									nuevaCancelacion.setDatosAuditoria(cancelacion.getDatosAuditoria());
									nuevaCancelacion.setSustentoSolicitud(cancelacion.getSustentoSolicitud());
									nuevaCancelacion.setTipo(4);
									nuevaCancelacion = this.crearActualizarSolicitudCancelacion(nuevaCancelacion);
								
								}
							}
							
						}
						
						
					}
				}
				
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.crearActualizarDatosSolicitudCancelacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
        	e.printStackTrace();
		}
		
		
		return respuesta;
		
	}

	@Override
	public List<SolicitudDocumentoComplementario> obtenerSolicitudesDocumentoComplementario(Long idCancelacion) {
		 List<SolicitudDocumentoComplementario> solicitudesDocsComp = new ArrayList<>();
	        
	        try {
					this.error = null;
				
				    this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
				    this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
				            .withProcedureName("PRC_SOLI_DOC_COMP_OBTENER").declareParameters(
				            new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
				            new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
				    );
							
				    SqlParameterSource parametros = new MapSqlParameterSource()
		                    .addValue("i_nidcanc", idCancelacion);

		        Map<String, Object> out = this.jdbcCall.execute(parametros);
		        
		        /*
		        if(out.get("o_retorno") instanceof Integer) {
		        	System.out.println("Integer");
		        }
		        if(out.get("o_retorno") instanceof BigDecimal) {
		        	System.out.println("BigDecimal");
		        }
		        if(out.get("o_retorno") instanceof BigInteger) {
		        	System.out.println("BigInteger");
		        }
		        if(out.get("o_retorno") instanceof Long) {
		        	System.out.println("Long");
		        }*/
		        Integer resultado = (Integer) out.get("o_retorno");
				if(resultado == 0) {
					solicitudesDocsComp = mapearSolicitudesDocumentosComplementarios(out);
					
				} else {
					String mensaje			= (String)out.get("o_mensaje");
					String mensajeInterno	= (String)out.get("o_sqlerrm");					
					this.error				= new Error(resultado.intValue(),mensaje,mensajeInterno);
				}
			}catch(Exception e){
				e.printStackTrace();
				Integer resultado		= 1;
				String mensaje			= "Error en TareaDaoImpl.obtenerSolicitudesCancelacion";
				String mensajeInterno	= e.getMessage();
				String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
				LOGGER.error(error[1], e);
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}			
			return solicitudesDocsComp;
	}
	
	@SuppressWarnings("unchecked")
	private List<SolicitudDocumentoComplementario> mapearSolicitudesDocumentosComplementarios(Map<String,Object> resultados){
		List<SolicitudDocumentoComplementario> listaSolicitudDocumentosComp = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		SolicitudDocumentoComplementario item = null;
		for(Map<String, Object> map : lista) {	
			item = new SolicitudDocumentoComplementario();
			
			Documento doc = new Documento();
			Constante conEsta = new Constante();
			doc.setEstado(conEsta);
			
			item.setDocumento(doc);
			
			if(map.get("n_idcanc") != null) {
			item.setIdSolicitudCancelacion(((BigDecimal)map.get("n_idcanc")).longValue());
			}
			
			if(map.get("n_iddocu") != null) {
				item.setIdDocumento(((BigDecimal)map.get("n_iddocu")).longValue());
				item.getDocumento().setId(((BigDecimal)map.get("n_iddocu")).longValue());
			}
			if(map.get("v_indcanc") != null) {
				item.setIndicadorSolicitud((String)map.get("v_indcanc"));
			}
			
			if(map.get("n_idesta") != null) {
				item.getDocumento().getEstado().setIdconstante(((BigDecimal)map.get("n_idesta")).longValue());
			}
			
			if(map.get("v_desesta") != null) {
				item.getDocumento().getEstado().setV_descons((String)map.get("v_desesta"));
			}
			
			listaSolicitudDocumentosComp.add(item);
		}
		return listaSolicitudDocumentosComp;
	}

	@Override
	public boolean aprobarRechazarSolicitudCancelacion(Cancelacion cancelacion) {
		boolean respuesta= false;
        this.error = null;
        String mensajeRespuesta = "";
        System.out.println("Tipo: "+cancelacion.getTipo());
        System.out.println("Id " +cancelacion.getIdSolicitudCancelacion());
        try {
        	this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
            this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
                    .withProcedureName("PRC_APRO_RECH_SOLIC_CANC").declareParameters(
                    new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
                    new SqlParameter("i_nusuario", OracleTypes.NUMBER),
                    new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
                    new SqlParameter("i_vsustrech", OracleTypes.VARCHAR),
                    new SqlParameter("i_tipo"	, OracleTypes.NUMBER),
                    new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
    				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
    				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
            );

            SqlParameterSource parametros = new MapSqlParameterSource()
                    .addValue("i_vusuario",cancelacion.getDatosAuditoria().getUsuarioCreacion())
                    .addValue("i_nusuario",cancelacion.getIdColaborador())
                    .addValue("i_nidcanc", cancelacion.getIdSolicitudCancelacion())
                    .addValue("i_vsustrech", cancelacion.getSustentoRechazo())
                    .addValue("i_tipo", cancelacion.getTipo());

            Map<String, Object> out = this.jdbcCall.execute(parametros);
            Integer codigoRetorno = (Integer) out.get("o_retorno");
            if (codigoRetorno != 0) {
            	mensajeRespuesta		= (String)out.get("o_mensaje");
    			String mensajeInterno	= (String)out.get("o_sqlerrm");    			
    			this.error				= new Error(codigoRetorno,mensajeRespuesta,mensajeInterno);
            } else {
            	respuesta = true;
                mensajeRespuesta = (String) out.get("o_mensaje");
            }
        }catch(Exception e){
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.aprobarRechazarSolicitudCancelacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
        return respuesta;
	}

	@Override
	public Cancelacion obtenerSolicitudCancelacionActivaDeDocumento(Long idDocumento) {
		Cancelacion datosSolicitudCancelacion = null;
        
        try {
				this.error = null;
			
			    this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
			    this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
			            .withProcedureName("PRC_OBTENER_SOLI_CANC_DOC").declareParameters(
			            new SqlParameter("i_niddocu", OracleTypes.NUMBER),
			            new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
			    );
						
			    SqlParameterSource parametros = new MapSqlParameterSource()
	                    .addValue("i_niddocu", idDocumento);

	        Map<String, Object> out = this.jdbcCall.execute(parametros);
	        
	        /*
	        if(out.get("o_retorno") instanceof Integer) {
	        	System.out.println("Integer");
	        }
	        if(out.get("o_retorno") instanceof BigDecimal) {
	        	System.out.println("BigDecimal");
	        }
	        if(out.get("o_retorno") instanceof BigInteger) {
	        	System.out.println("BigInteger");
	        }
	        if(out.get("o_retorno") instanceof Long) {
	        	System.out.println("Long");
	        }*/
	        Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				List<Cancelacion> solicitudesCancelacion = mapearCancelacion(out);
				if(solicitudesCancelacion.size()>0) {
					datosSolicitudCancelacion = solicitudesCancelacion.get(0);
				}
				
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado.intValue(),mensaje,mensajeInterno);
			}
		}catch(Exception e){
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.obtenerSolicitudCancelacionActivaDeDocumento";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		
		return datosSolicitudCancelacion;
	}

	@Override
	@Transactional
	public boolean cancelarDocumento(Cancelacion cancelacion) {
		boolean respuesta = false;
		this.error = null;
		String mensajeRespuesta = "";
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc);
			//this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
			this.jdbcCall.withSchemaName("AGI")
						 .withCatalogName("PCK_AGI_TAREAS")
						 .withProcedureName("PRC_CANCELAR_DOCUMENTO")
						 .declareParameters(
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlParameter("i_nusuario", OracleTypes.NUMBER),
							new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
							new SqlParameter("i_niddocu", OracleTypes.NUMBER),
							new SqlParameter("i_ntipcanc", OracleTypes.NUMBER),
							new SqlParameter("i_vidgoogledrive", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource parametros = new MapSqlParameterSource()
					.addValue("i_vusuario", cancelacion.getDatosAuditoria().getUsuarioCreacion())
					.addValue("i_nusuario", cancelacion.getIdColaborador())
					.addValue("i_nidcanc", cancelacion.getIdSolicitudCancelacion())
					.addValue("i_niddocu", cancelacion.getIdDocumento())
					.addValue("i_ntipcanc", cancelacion.getNumTipoCancelacion())
			.addValue("i_vidgoogledrive", cancelacion.getIdGoogleDrive());

			Map<String, Object> out = this.jdbcCall.execute(parametros);
			Integer codigoRetorno = (Integer) out.get("o_retorno");
			
			if (codigoRetorno != 0) {
				mensajeRespuesta = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				
				this.error = new Error(codigoRetorno, mensajeRespuesta, mensajeInterno);
			} else {
				respuesta = true;
				mensajeRespuesta = (String) out.get("o_mensaje");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en TareaDaoImpl.cancelarDocumento";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		

		return respuesta;
	}

	//cguerra
	
	
	
	
	/**
     * Crea objeto de autenticación.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return Un bjeto de autenticación.
     * @throws IOException si el credentials.json no es encontrado.
     */
	    private  Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {			
	    	TareaDAOImpl.CREDENTIALS_FILE_PATH= env.getProperty("app.config.paths.carpetaResources");
	    	TareaDAOImpl.TOKENS_DIRECTORY_PATH= env.getProperty("app.config.paths.toke.directory");	
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
	
	
	@Override
	@Transactional
	public boolean cancelarDocumentoGoogleDrive(Cancelacion cancelacion) {
		boolean respuesta = false;
		this.error = null;
		String mensajeRespuesta = "";
		try {
			String idDocGD=null;

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
			
			URL urlWord = new URL(RutaIpFileServer+cancelacion.getRutaDocumento());
			
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
			  //seteo el ID al campo
			  cancelacion.setIdGoogleDrive(idDocGD);
			  //Inicializamos dao
			  TareaDAOImpl tareaService = new TareaDAOImpl();
			  tareaService.setJdbc(this.jdbc);
			  //insertamos ruta del ID 
			  boolean resultado = tareaService.cancelarDocumento(cancelacion);
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en TareaDaoImpl.cancelarDocumentoGOOGLE";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return respuesta;
	}
	
	//cguerra
	
	
	
	@Override
	public String obtenerUltimaRutaCopiaControladaDocumento(Long idDocumento) {
		String ruta = null;
        List<Revision> listaRevisiones = new ArrayList<>();
        try {
				this.error = null;
			
			    this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
			    this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
			            .withProcedureName("PRC_RUTA_C_CONTROL_DOCU").declareParameters(
			            new SqlParameter("i_niddocu", OracleTypes.NUMBER),
			            new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
			    );
						
			    SqlParameterSource parametros = new MapSqlParameterSource()
	                    .addValue("i_niddocu", idDocumento);

	        Map<String, Object> out = this.jdbcCall.execute(parametros);

	        Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				listaRevisiones = mapearRevisionesDoc(out);
				if(listaRevisiones.size()>0) {
					Revision revision = listaRevisiones.get(0);
					ruta = revision.getRutaDocumentoCopiaCont();
				}else {
					ruta = null;
				}
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado.intValue(),mensaje,mensajeInterno);
			}
		}catch(Exception e){
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.obtenerUltimaRutaCopiaControladaDocumento";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		
		return ruta;
	}
	
	@SuppressWarnings("unchecked")
	private List<Revision> mapearRevisionesDoc(Map<String,Object> resultados){
		List<Revision> listaRevision = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Revision item = null;
		for(Map<String, Object> map : lista) {	
			item = new Revision();
			
			if(map.get("n_idrevi") != null) {
			item.setId(((BigDecimal)map.get("n_idrevi")).longValue());
			}
			
			if(map.get("v_ruta_docu_c_cont") != null) {
				item.setRutaDocumentoCopiaConf((String)map.get("v_ruta_docu_c_cont"));
			}

			listaRevision.add(item);
		}
		
		return listaRevision;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Documento> obtenerDocumentosJerarquicos(Long idDocumentoHijo) {
		Map<String, Object> out	= null;
		List<Documento> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbcTemplate)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_TAREAS")
			.withProcedureName("PRC_OBTENER_DOC_JERARQUICO")
			.declareParameters(
				new SqlParameter("i_niddocu", 		OracleTypes.NUMBER),						
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_niddocu",idDocumentoHijo);
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
						codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
						codigoAnterior.setCodigo((String)map.get("V_CODIANTE"));
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
			String mensaje			= "Error en TareaDAOImpl.obtenerComplementario";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}

	@Override
	public Integer obtenerCantidadSolicitudCancelacion(Long idDocumento) {
		Map<String, Object> out	= null;
		Integer respuesta = new Integer(0);
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbcTemplate)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_TAREAS")
			.withProcedureName("PRC_CANT_SOLI_CANC")
			.declareParameters(
				new SqlParameter("i_niddocu", 		OracleTypes.NUMBER),						
				new SqlOutParameter("o_respuesta", OracleTypes.NUMBER),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_niddocu",idDocumento);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				respuesta = ((BigDecimal)out.get("o_respuesta")).intValue();		
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDAOImpl.obtenerCantidadSolicitudCancelacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return respuesta;
	}
	
	@Override
    public List<Cancelacion> obtenerCancelacionAprobacion(TareaRequest tareaRequest, PageRequest pageRequest, Long iUsuario) {
        List<Cancelacion> solicitudesCancelacion = new ArrayList<>();
        
        try {
				this.error = null;
			    this.paginacion = new Paginacion();
			    this.paginacion.setPagina(pageRequest.getPagina());
			    this.paginacion.setRegistros(pageRequest.getRegistros());
			
			    this.jdbcCall = new SimpleJdbcCall(this.jdbcTemplate);
			    this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_TAREAS")
			            .withProcedureName("PRC_LISTADO_CANCELADO_APROB").declareParameters(
			            new SqlParameter("i_idusuario", OracleTypes.NUMBER),
			            new SqlParameter("i_nidcanc", OracleTypes.NUMBER),
			            new SqlParameter("i_vcoddocu", OracleTypes.VARCHAR),
			            new SqlParameter("i_vdesdocu", OracleTypes.VARCHAR),
			            new SqlParameter("i_vnombres", OracleTypes.VARCHAR),
			            new SqlParameter("i_vapepat", OracleTypes.VARCHAR),
			            new SqlParameter("i_vapemat", OracleTypes.VARCHAR),
			            new SqlParameter("i_nestcanc", OracleTypes.VARCHAR),
			            new SqlParameter("i_nestdoc", OracleTypes.NUMBER),
			            new SqlParameter("i_nidproc", OracleTypes.NUMBER),
			            new SqlParameter("i_nidalcasgi", OracleTypes.NUMBER),
			            new SqlParameter("i_nidgeregnrl", OracleTypes.NUMBER),
			            new SqlParameter("i_nidtipodocu", OracleTypes.NUMBER),
			            new SqlParameter("i_nfechacandesde",	OracleTypes.DATE),
						new SqlParameter("i_nfechacanhasta",	OracleTypes.DATE),
			            new SqlParameter("i_npagina", OracleTypes.NUMBER),
			            new SqlParameter("i_nregistros", OracleTypes.NUMBER),
			            new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR)
			    );
						
			    SqlParameterSource parametros = new MapSqlParameterSource()
			    		.addValue("i_idusuario", iUsuario)
	                    .addValue("i_nidcanc", tareaRequest.getCodigoSolicitud())
	                    .addValue("i_vcoddocu", tareaRequest.getCodigoDocumento())
	                    .addValue("i_vdesdocu", tareaRequest.getTituloDocumento())
	                    .addValue("i_vnombres", tareaRequest.getNombres())
	                    .addValue("i_vapepat", tareaRequest.getApellidoPaterno())
	                    .addValue("i_vapemat", tareaRequest.getApellidoMaterno())
	                    .addValue("i_nestcanc", tareaRequest.getEstado())
	                    .addValue("i_nestdoc", tareaRequest.getEstadoDoc())
	                    .addValue("i_npagina", pageRequest.getPagina())
	                    .addValue("i_nregistros", pageRequest.getRegistros())
			    		.addValue("i_nidproc",tareaRequest.getIdproc())
						.addValue("i_nidalcasgi",tareaRequest.getIdalcasgi())
						.addValue("i_nidgeregnrl",tareaRequest.getIdgeregnrl())
						.addValue("i_nidtipodocu",tareaRequest.getIdtipodocu())
						.addValue("i_nfechacandesde",tareaRequest.getFechacandesde())
						.addValue("i_nfechacanhasta",tareaRequest.getFechacanhasta());
	        Map<String, Object> out = this.jdbcCall.execute(parametros);
	        
	        /*
	        if(out.get("o_retorno") instanceof Integer) {
	        	System.out.println("Integer");
	        }
	        if(out.get("o_retorno") instanceof BigDecimal) {
	        	System.out.println("BigDecimal");
	        }
	        if(out.get("o_retorno") instanceof BigInteger) {
	        	System.out.println("BigInteger");
	        }
	        if(out.get("o_retorno") instanceof Long) {
	        	System.out.println("Long");
	        }*/
	        Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				solicitudesCancelacion = mapearCancelacion(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado.intValue(),mensaje,mensajeInterno);
			}
		}catch(Exception e){
			e.printStackTrace();
			Integer resultado		= 1;
			String mensaje			= "Error en TareaDaoImpl.obtenerSolicitudesCancelacion";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		
		return solicitudesCancelacion;
    }
}
