package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import pe.com.sedapal.agi.model.request_objects.*;
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
//import pe.com.sedapal.agi.dao.IDocumentoDAO;
import pe.com.sedapal.agi.dao.IMigracionDAO;
import pe.com.sedapal.agi.model.Codigo;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Copia;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.Historico;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.enums.EstadoConstante;
import pe.com.sedapal.agi.model.enums.Fase;
import pe.com.sedapal.agi.model.enums.TipoConstante;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class MigracionDAOImpl implements IMigracionDAO {
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
	//Consulta Historica
	@Override
	public List<Documento> obtenerConsultaHistorica(DocumentoRequest documentoRequest, PageRequest pageRequest) {
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
			.withProcedureName("PRC_CONSULHIST")
			.declareParameters(
				new SqlParameter("i_nid", 				OracleTypes.NUMBER),						
				new SqlParameter("i_vcodigo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_vtitulo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_nestado", 			OracleTypes.VARCHAR),
				new SqlParameter("i_ndisponible",		OracleTypes.NUMBER),
				//new SqlParameter("i_nidjera",			OracleTypes.NUMBER),
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
				new SqlParameter("i_nidarea",			OracleTypes.NUMBER),
				new SqlParameter("i_nidparticipante",	OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseact",		OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseestadoact",	OracleTypes.NUMBER),
								
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
				new SqlParameter("i_vorden", 			OracleTypes.VARCHAR),						
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",						documentoRequest.getId()) //null)
			.addValue("i_vcodigo",					documentoRequest.getCodigo()) //documentoRequest.getCodigo())
			.addValue("i_vtitulo",					documentoRequest.getTitulo()) //documentoRequest.getDescripcion()) //documentoRequest.getDescripcion())
			.addValue("i_nestado",					(documentoRequest.getEstdoc()==null || documentoRequest.getEstdoc().equals(",,"))?null:documentoRequest.getEstdoc())//documentoRequest.getEstdoc())
			.addValue("i_ndisponible",				null) //documentoRequest.getDisponible())
			
			//.addValue("i_nidjera",				documentoRequest.getDescripcion())
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
			.addValue("i_nidarea",					(documentoRequest.getIdarea()==0)?null:documentoRequest.getIdarea())
			.addValue("i_nidparticipante",			(documentoRequest.getIdparticipante()==0)?null:documentoRequest.getIdparticipante())
			.addValue("i_nidfaseact",				(documentoRequest.getIdfaseact()==0)?null:documentoRequest.getIdfaseact())
			.addValue("i_nidfaseestadoact",			(documentoRequest.getIdfaseestadoact()==0)?null:documentoRequest.getIdfaseestadoact())
			
			
			.addValue("i_npagina",					pageRequest.getPagina())
			.addValue("i_nregistros",				pageRequest.getRegistros())
			.addValue("i_vorden",					null);
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_DOCUMENTO_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 				OracleTypes.NUMBER),						
				new SqlParameter("i_vcodigo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_vtitulo", 			OracleTypes.VARCHAR),
				new SqlParameter("i_nestado", 			OracleTypes.VARCHAR),
				new SqlParameter("i_ndisponible",		OracleTypes.NUMBER),
				//new SqlParameter("i_nidjera",			OracleTypes.NUMBER),
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
				new SqlParameter("i_nidarea",			OracleTypes.NUMBER),
				new SqlParameter("i_nidparticipante",	OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseact",		OracleTypes.NUMBER),
				new SqlParameter("i_nidfaseestadoact",	OracleTypes.NUMBER),
								
				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",		OracleTypes.NUMBER),
				new SqlParameter("i_vorden", 			OracleTypes.VARCHAR),						
				new SqlOutParameter("o_cursor", 		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",		OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",		OracleTypes.VARCHAR));		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",						documentoRequest.getId()) //null)
			.addValue("i_vcodigo",					documentoRequest.getCodigo()) //documentoRequest.getCodigo())
			.addValue("i_vtitulo",					documentoRequest.getTitulo()) //documentoRequest.getDescripcion()) //documentoRequest.getDescripcion())
			.addValue("i_nestado",					(documentoRequest.getEstdoc()==null || documentoRequest.getEstdoc().equals(",,"))?null:documentoRequest.getEstdoc())//documentoRequest.getEstdoc())
			.addValue("i_ndisponible",				null) //documentoRequest.getDisponible())
			
			//.addValue("i_nidjera",				documentoRequest.getDescripcion())
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
			.addValue("i_nidarea",					(documentoRequest.getIdarea()==0)?null:documentoRequest.getIdarea())
			.addValue("i_nidparticipante",			(documentoRequest.getIdparticipante()==0)?null:documentoRequest.getIdparticipante())
			.addValue("i_nidfaseact",				(documentoRequest.getIdfaseact()==0)?null:documentoRequest.getIdfaseact())
			.addValue("i_nidfaseestadoact",			(documentoRequest.getIdfaseestadoact()==0)?null:documentoRequest.getIdfaseestadoact())
			
			
			.addValue("i_npagina",					pageRequest.getPagina())
			.addValue("i_nregistros",				pageRequest.getRegistros())
			.addValue("i_vorden",					null);
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
    			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
			item.setId(((BigDecimal)map.get("N_IDDOCU")).longValue());
			item.setCodigo((String)map.get("V_CODDOCU"));
			item.setDescripcion((String)map.get("V_DESDOCU"));
			//Ruta del Documento Digital
			item.setRutaDocumento((String)map.get("V_RUTA_DOCUMT"));
			if (map.get("N_VERDOCU") != null) {
				item.setVersion(((BigDecimal)map.get("N_VERDOCU")).longValue());
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
			
			//aquies
			
			if(map.get("V_IDCODIANTE")!=null) {
				Codigo codigoAnterior = new Codigo();
				//codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
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
				revision.setNumero((map.get("N_NUMREVI")==null)?null:((BigDecimal)map.get("N_NUMREVI")).longValue());
				revision.setFecha((Date)map.get("D_FEREVI"));
				revision.setFechaAprobDocu((Date)map.get("D_FECHAAPROBDOCU"));
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
			
			if(map.get("N_INDDOCDIG")!=null) {
				item.setIndicadorDigital(((BigDecimal)map.get("N_INDDOCDIG")).longValue());
			}			

			listaDocumento.add(item);
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
//			if (this.paginacion != null) {
//				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
//			}
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
	
	private Historico registrarDocumentoHistorico(Long codigo, Long idRevision, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		Historico historico = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_DOCUMENTOHIST_GUARDAR")
			.declareParameters(
				new SqlParameter("i_nid",			OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlParameter("i_nidrevi",		OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",		codigo)
			.addValue("i_nidrevi",  idRevision)
			.addValue("i_vusuario",	"AGI_MIGRACION");
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			Historico beanHistorico = null;
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					beanHistorico = new Historico();
					if(map.get("n_iddocuhist") != null) {
						beanHistorico.setIdHistorico(((BigDecimal)map.get("n_iddocuhist")).longValue());
					}
					if(map.get("n_iddocu") != null) {
						beanHistorico.setIdDocumento(((BigDecimal)map.get("n_iddocu")).longValue());
					}
					if(map.get("n_idrevi") != null) {
						beanHistorico.setIdRevision(((BigDecimal)map.get("n_idrevi")).longValue());
					}
				}
				if(beanHistorico != null) {
					historico = beanHistorico;
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				historico				= null;
			}
		} catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.registrarDocumentoHistorico";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			historico				= null;
		}
		return historico;
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
			
			List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
			
			Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			objeto.setParticipanteElaboracion(this.obtenerParticipante(codigo,faseElaboracion,pagina,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			
			Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			objeto.setParticipanteConsenso(this.obtenerParticipante(codigo,faseConsenso,pagina,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			
			Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			objeto.setParticipanteAprobacion(this.obtenerParticipante(codigo,faseAprobacion,pagina,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			
			Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			objeto.setParticipanteHomologacion(this.obtenerParticipante(codigo,faseHomologacion,pagina,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			
			objeto.setListaComplementario(this.obtenerComplementario(codigo, pagina,objeto.getRevision().getId()));
			if(this.error!=null) return null;
			
			
			//objeto.setListaEquipo(this.obtenerEquipo(codigo));
			objeto.setListaEquipo(this.obtenerEquipo(codigo,objeto.getRevision().getId()));
			
			
			if(this.error!=null) return null;
			
			RevisionDAOImpl revisionDao = new RevisionDAOImpl();
			revisionDao.setJdbc(this.jdbc);
			
			RevisionRequest revision = new RevisionRequest();
			revision.setIdDocumento(codigo);
			List<Revision> listaRevision = revisionDao.obtenerRevisionHistorico(revision, pagina);
			objeto.setListaRevision(listaRevision);
			Long idRevision = objeto.getRevision().getId();
			
			for(Revision rev : listaRevision) {
				if(rev.getId() == idRevision) {
					objeto.setRevision(rev);
					break;
				}
			}
			
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.obtenerDocumentoDetalle";
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

	@SuppressWarnings("unchecked")
	private List<Colaborador> obtenerParticipante(Long codigo, Long fase, PageRequest pagina, Long idRevi) {
		
		Map<String, Object> out	= null;
		List<Colaborador> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_PARTICIPANTE_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),
				new SqlParameter("i_idrevi", 		OracleTypes.NUMBER),	
				new SqlParameter("i_nfase", 		OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			codigo)
			.addValue("i_idrevi",	    idRevi)
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
					//item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());//numero Ficha
					item.setNumeroFicha(((BigDecimal)map.get("N_IDCOLA")).longValue());//numero Ficha
					item.setNombreCompleto((String)map.get("V_NOCOLA"));
					item.setFuncion((String)map.get("V_NOFUNC"));
					item.setCiclo((map.get("N_CICLPART")==null)?null:((BigDecimal)map.get("N_CICLPART")).longValue());
					item.setComentario((String)map.get("V_COMEPART"));
					item.setPlazo(((BigDecimal)map.get("N_CANTPLAZ")==null)?null:((BigDecimal)map.get("N_CANTPLAZ")).longValue());
					item.setFechaPlazo((Date)map.get("D_PLAZPART"));
					item.setFechaLiberacion((Date)map.get("D_LIBEPART"));
					item.setDisponible(((BigDecimal)map.get("N_DISPART")).longValue());
					item.setPrioridad((map.get("N_PRIO")==null)?null:((BigDecimal)map.get("N_PRIO")).longValue());
					item.setResponsable((String)map.get("V_NOCOLA"));
					if(map.get("V_NOEQUI")!=null) {
						Equipo equipo = new Equipo();
						equipo.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
						equipo.setDescripcion((String)map.get("V_NOEQUI"));
						item.setEquipoColaborador((String)map.get("V_NOEQUI"));
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
			String mensaje			= "Error en DocumentoDAOImpl.obtenerParticipante";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Documento> obtenerComplementario(Long codigo, PageRequest pagina, Long idRevi) {
		
		Map<String, Object> out	= null;
		List<Documento> lista	= new ArrayList<>();
	
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_COMPLEMENTARIO_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 		OracleTypes.NUMBER),
				new SqlParameter("i_idrevi",	OracleTypes.NUMBER),
				new SqlParameter("i_npadre", 	OracleTypes.NUMBER),
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
			.addValue("i_idrevi",		idRevi)
			.addValue("i_npadre",		codigo)
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
					item.setDescripcion((String)map.get("V_NOMEQUI"));
					
					
					if(map.get("N_INDIRESP")!=null) {
						item.setIndicadorResp(((BigDecimal)map.get("N_INDIRESP")).longValue());
						
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
					item.setIndicadorResponsable(((BigDecimal)map.get("N_INDIRESP")).longValue());
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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
	
//Registro Documento
	@Override
	@Transactional
	public Documento guardarDocumento(Documento documento, Long codigo, String usuario , Long idUsuario) {
		Documento item = null;
		
		try {
			documento.setDisponible(Long.parseLong(EstadoConstante.ACTIVO.toString()));
			item = this.registrarDocumento(documento, codigo, usuario);
			if(item==null) {
				return null;
			}

			ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
			constanteDao.setJdbc(this.jdbc);			
			ConstanteRequest constante = new ConstanteRequest();
			constante.setPadre(TipoConstante.ETAPA_RUTA.toString());			
			PageRequest pagina = new PageRequest();
			pagina.setPagina(1);
			pagina.setRegistros(1000);
			
			List<Constante> listaRuta = constanteDao.obtenerConstantes(constante, pagina, null);
			
			/*boolean eliminado = this.eliminarParticipante(null, item.getId(), null, usuario);*/
			/*if(eliminado==false)	{
				return null;
			}*/
			
			
			///Aqui
			
			Revision pRevision = documento.getRevision();
			RevisionMigracionDAOImpl revisionDao = new RevisionMigracionDAOImpl();
			pRevision.setDocumento(new Documento());
			pRevision.getDocumento().setId(item.getId());
			revisionDao.setJdbc(this.jdbc);
			Revision revision = revisionDao.crearRevision(pRevision,usuario,idUsuario,documento.getFechaCreaDoc());
			if(revision == null) {
				return null;
			}else {
				item.setRevision(revision);
				
				this.registrarDocumento(item, item.getId(), usuario);
			}
			
			Long indicadorUltimaFase = null;
			Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			
			if(documento.getParticipanteElaboracion() != null) indicadorUltimaFase = faseElaboracion;
			if(documento.getParticipanteConsenso() != null) indicadorUltimaFase = faseConsenso;
			if(documento.getParticipanteAprobacion() != null) indicadorUltimaFase = faseAprobacion;
			if(documento.getParticipanteHomologacion() != null) indicadorUltimaFase = faseHomologacion;
			
			Date ultimaAprobacion = null;
			Long ultimaPrioridad = 0L;
			Date fechaMaximaTotal = null;
			Long usuarioMaximo = null;
			Long ultimoUsuario = null;
			
			//Long faseElaboracion = this.obtenerIdConstante(listaRuta, Fase.ELABORACION.toString());
			if(documento.getParticipanteElaboracion()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteElaboracion()) {
					participante.setDisponible(documento.getDisponible());					
					
					if(ultimaAprobacion != null) {
						if(ultimaAprobacion.before(participante.getFechaLiberacion())) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimaPrioridad = participante.getPrioridad();
							ultimoUsuario = participante.getNumeroFicha();
						}
						
						if(fechaMaximaTotal.before(ultimaAprobacion)) {
							fechaMaximaTotal = ultimaAprobacion;
							usuarioMaximo = ultimoUsuario;
						}
					//} else if(ultimaPrioridad <= participante.getPrioridad()) {
					} else {
						if(ultimaPrioridad <= participante.getPrioridad()) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimoUsuario = participante.getNumeroFicha();
							fechaMaximaTotal = ultimaAprobacion;
							usuarioMaximo = ultimoUsuario;
						}
					}
					
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(),revision.getId(), faseElaboracion, usuario,documento.getFechaCreaDoc());
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteElaboracion(lista);
				
				int tamano = documento.getParticipanteElaboracion()==null?0: documento.getParticipanteElaboracion().size();
				if(tamano > 0) {
					ultimaAprobacion = null;
					ultimaPrioridad = 0L;
					
					Flujo objeto = new Flujo();
					objeto.setIdDocumento(codigo!= null?codigo:item.getId());
					objeto.setIdRevision(item.getRevision().getId());
					//objeto.setIteracion(item.getRevision().getIteracion());
					objeto.setIdFase(faseElaboracion);
					objeto.setUsuarioFase(ultimoUsuario);
					
					boolean flujo = registrarFlujo(objeto, usuario);
					if(!flujo) {
						return null;
					}
				}
			}
			
			//Long faseConsenso = this.obtenerIdConstante(listaRuta, Fase.CONSENSO.toString());
			if(documento.getParticipanteConsenso()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteConsenso()) {
					participante.setDisponible(documento.getDisponible());
					
					if(ultimaAprobacion != null) {
						if(ultimaAprobacion.before(participante.getFechaLiberacion())) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimaPrioridad = participante.getPrioridad();
							ultimoUsuario = participante.getNumeroFicha();
						}
						if(fechaMaximaTotal.before(ultimaAprobacion)) {
							fechaMaximaTotal = ultimaAprobacion;
							usuarioMaximo = ultimoUsuario;
						}
					//} else if(ultimaPrioridad <= participante.getPrioridad()) {
					} else {
						if(ultimaPrioridad <= participante.getPrioridad()) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimoUsuario = participante.getNumeroFicha();
							if(fechaMaximaTotal.before(ultimaAprobacion)) {
								fechaMaximaTotal = ultimaAprobacion;
								usuarioMaximo = ultimoUsuario;
							}
						}
					}
					
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(),revision.getId(), faseConsenso, usuario,documento.getFechaCreaDoc());
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteConsenso(lista);
				
				int tamano = documento.getParticipanteConsenso()==null?0: documento.getParticipanteConsenso().size();
				if(tamano > 0) {
					ultimaAprobacion = null;
					ultimaPrioridad = 0L;
					
					Flujo objeto = new Flujo();
					objeto.setIdDocumento(codigo!= null?codigo:item.getId());
					objeto.setIdRevision(item.getRevision().getId());
					//objeto.setIteracion(item.getRevision().getIteracion());
					objeto.setIdFase(faseConsenso);
					objeto.setUsuarioFase(ultimoUsuario);
					
					boolean flujo = registrarFlujo(objeto, usuario);
					if(!flujo) {
						return null;
					}
				}
			}
			
			//Long faseAprobacion = this.obtenerIdConstante(listaRuta, Fase.APROBACION.toString());
			if(documento.getParticipanteAprobacion()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteAprobacion()) {
					participante.setDisponible(documento.getDisponible());
					
					if(ultimaAprobacion != null) {
						if(ultimaAprobacion.before(participante.getFechaLiberacion())) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimaPrioridad = participante.getPrioridad();
							ultimoUsuario = participante.getNumeroFicha();
						}
						if(fechaMaximaTotal.before(ultimaAprobacion)) {
							fechaMaximaTotal = ultimaAprobacion;
							usuarioMaximo = ultimoUsuario;
						}
					//} else if(ultimaPrioridad <= participante.getPrioridad()) {
					} else {
						if(ultimaPrioridad <= participante.getPrioridad()) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimoUsuario = participante.getNumeroFicha();
							if(fechaMaximaTotal.before(ultimaAprobacion)) {
								fechaMaximaTotal = ultimaAprobacion;
								usuarioMaximo = ultimoUsuario;
							}
						}
					}
					
					Colaborador temporal = this.registrarParticipante(participante,codigo!= null?codigo:item.getId(),revision.getId(), faseAprobacion, usuario,documento.getFechaCreaDoc());
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteAprobacion(lista);
				
				int tamano = documento.getParticipanteAprobacion()==null?0: documento.getParticipanteAprobacion().size();
				if(tamano > 0) {
					ultimaAprobacion = null;
					ultimaPrioridad = 0L;
					
					Flujo objeto = new Flujo();
					objeto.setIdDocumento(codigo!= null?codigo:item.getId());
					objeto.setIdRevision(item.getRevision().getId());
					//objeto.setIteracion(item.getRevision().getIteracion());
					objeto.setIdFase(faseAprobacion);
					objeto.setUsuarioFase(ultimoUsuario);
					
					boolean flujo = registrarFlujo(objeto, usuario);
					if(!flujo) {
						return null;
					}
				}
			}
			
			//Long faseHomologacion = this.obtenerIdConstante(listaRuta, Fase.HOMOLOGACION.toString());
			if(documento.getParticipanteHomologacion()!=null) {
				List<Colaborador> lista = new ArrayList<>();
				for(Colaborador participante : documento.getParticipanteHomologacion()) {
					participante.setDisponible(documento.getDisponible());
					
					if(ultimaAprobacion != null) {
						if(ultimaAprobacion.before(participante.getFechaLiberacion())) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimaPrioridad = participante.getPrioridad();
							ultimoUsuario = participante.getNumeroFicha();
						}
						if(fechaMaximaTotal.before(ultimaAprobacion)) {
							fechaMaximaTotal = ultimaAprobacion;
							usuarioMaximo = ultimoUsuario;
						}
					//} else if(ultimaPrioridad <= participante.getPrioridad()) {
					} else {
						if(ultimaPrioridad <= participante.getPrioridad()) {
							ultimaAprobacion = participante.getFechaLiberacion();
							ultimoUsuario = participante.getNumeroFicha();
							if(fechaMaximaTotal.before(ultimaAprobacion)) {
								fechaMaximaTotal = ultimaAprobacion;
								usuarioMaximo = ultimoUsuario;
							}
						}
					}
					
					Colaborador temporal = this.registrarParticipante(participante, codigo!= null?codigo:item.getId(),revision.getId(), faseHomologacion, usuario,documento.getFechaCreaDoc());
					if(temporal==null) {
						return null;
					}
					lista.add(temporal);
				}
				item.setParticipanteHomologacion(lista);
				
				int tamano = documento.getParticipanteHomologacion()==null?0: documento.getParticipanteHomologacion().size();
				if(tamano > 0) {
					ultimaAprobacion = null;
					ultimaPrioridad = 0L;
					
					Flujo objeto = new Flujo();
					objeto.setIdDocumento(codigo!= null?codigo:item.getId());
					objeto.setIdRevision(item.getRevision().getId());
					//objeto.setIteracion(item.getRevision().getIteracion());
					objeto.setIdFase(faseHomologacion);
					objeto.setUsuarioFase(ultimoUsuario);
					
					boolean flujo = registrarFlujo(objeto, usuario);
					if(!flujo) {
						return null;
					}
				}
			}
			
			Long idRevision = revision.getId();
			Historico historico = this.registrarDocumentoHistorico(codigo!= null?codigo:item.getId(), idRevision, usuario);
			if(historico == null)	{
				return null;
			}
			
			if(ultimoUsuario != null && fechaMaximaTotal != null) {
				item.setIdUsuaEsta(usuarioMaximo);
				item.setFechaApro(fechaMaximaTotal);
				item = this.registrarDocumento(item, item.getId(), usuario);
				if(item==null) {
					return null;
				} else {
					revision.setIdUsuarioAprobDocu(usuarioMaximo);
					revision.setFechaAprobDocu(fechaMaximaTotal);
					revision.setIdHistorial(historico.getIdHistorico());
					Revision revisionf = revisionDao.crearRevision(revision,usuario,idUsuario,documento.getFechaCreaDoc());
					if(revisionf == null) {
						return null;
					}
				}
			}

			if(documento.getListaComplementario()!=null) {
				/*eliminado = this.eliminarComplementario(codigo!= null?codigo:item.getId(), null, usuario);
				if(eliminado==false)	{
					return null;
				}*/
				List<Documento> listaHijo = new ArrayList<>();
				for(Documento documentoHijo : documento.getListaComplementario()) {
					documentoHijo.setDisponible(documento.getDisponible());
					Documento hijoTemporal = this.registrarComplementario(documentoHijo, codigo!= null?codigo:item.getId(), revision.getId(), usuario,  item.getFechaCreaDoc());	
					if(hijoTemporal==null) {
						return null;
					}
					listaHijo.add(hijoTemporal);
				}
				item.setListaComplementario(listaHijo);
			}
			
			if(documento.getListaEquipo()!=null) {
				/*eliminado = this.eliminarEquipo(codigo!= null?codigo:item.getId(), null, usuario);
				if(eliminado==false)	{
					return null;
				}*/
				List<Equipo> listaEquipo = new ArrayList<>();
				for(Equipo equipo : documento.getListaEquipo()) {
					equipo.setDisponible(documento.getDisponible());
					
					Equipo equipoTemporal = this.registrarEquipo(   equipo, codigo!= null?codigo:item.getId(), usuario, revision.getId(),item.getFechaCreaDoc());
					if(equipoTemporal==null) {
						return null;
					}
					listaEquipo.add(equipoTemporal);
				}
				item.setListaEquipo(listaEquipo);
			}
			
			if(documento.getCritica() != null) {
				guardarCritica(documento, item);
			}
			
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en DocumentoDAOImpl.guardarDocumento";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return item;
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
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_DOCUMENTO_GUARDAR")
				.declareParameters(
					new SqlParameter("i_nid",			OracleTypes.NUMBER),					
					new SqlParameter("i_nidrevi",		OracleTypes.NUMBER),
					new SqlParameter("i_npadre",		OracleTypes.NUMBER),
					new SqlParameter("i_vtitulo",		OracleTypes.VARCHAR),
					new SqlParameter("i_idtrabajador",	OracleTypes.NUMBER),
					new SqlParameter("i_dfechacreadoc",	OracleTypes.DATE),
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
					new SqlParameter("i_nretencion",	OracleTypes.NUMBER),
					new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_nroblid",		OracleTypes.NUMBER), //N� Revisi�n Obligatorio
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlParameter("i_n_inddocdig",	OracleTypes.NUMBER),//Indicador Digital		
					new SqlParameter("i_n_idcoor",		OracleTypes.NUMBER),
					new SqlParameter("i_n_idusuaesta",  OracleTypes.NUMBER),
					new SqlParameter("i_d_fechesta",  	OracleTypes.DATE),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nid",				documento.getId())//codigo
				.addValue("i_nidrevi",			documento.getRevision()!= null?documento.getRevision().getId():null)
				.addValue("i_npadre",			(documento.getPadre()==null)?null:documento.getPadre().getId())
				.addValue("i_vtitulo",			documento.getDescripcion())
				.addValue("i_idtrabajador",		999999999)//documento.getIdcolaboracioncreaciondoc())
				.addValue("i_dfechacreadoc",	documento.getFechaCreaDoc())
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
				.addValue("i_n_inddocdig",		documento.getIndicadorDigital())
				.addValue("i_n_idusuaesta",		documento.getIdUsuaEsta())
				.addValue("i_d_fechesta",		documento.getFechaApro())
				.addValue("i_vusuario",			"AGI_MIGRACIÓN")//usuario);
				.addValue("i_n_idcoor",			(documento.getCoordinador()==null)?null:documento.getCoordinador().getIdColaborador());
			;
		
			
			
			
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
					//Id Colaborador
					if(map.get("n_idcolacreadoc")!=null) {						
						item.setIdcolaboracioncreaciondoc(((BigDecimal)map.get("n_idcolacreadoc")).longValue());						
					}
					//Fecha de la creación del documento
					if(map.get("d_feccreadoc")!=null) {						
						item.setFechaCreaDoc(((Date)map.get("d_feccreadoc")));						
					}
					
					if(map.get("N_RETDOCU")!=null) {
						item.setRetencionRevision(((BigDecimal)map.get("N_RETDOCU")).longValue());
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
						Jerarquia jproceso = new Jerarquia();					
						item.setProceso(((BigDecimal)map.get("N_IDPROC")).longValue());
					}
					if(map.get("N_IDALCASGI")!=null) {
						Jerarquia jalcance = new Jerarquia();					
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
						//codigoAnterior.setId(((BigDecimal)map.get("V_IDCODIANTE")).longValue());
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
			String mensaje			= "Error en MigracionDAOImpl.registrarDocumento";
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
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
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

	@SuppressWarnings("unchecked")
	private Colaborador registrarParticipante(Colaborador colaborador, Long codigo,Long revi, Long fase, String usuario,Date FechaCreaDoc) {
		Colaborador item		= null;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_PARTICIPANTE_GUARDAR")
				.declareParameters(
					new SqlParameter("i_dfecha_regis",	OracleTypes.DATE),    
					new SqlParameter("i_ncolaborador",	OracleTypes.NUMBER),
					new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
					new SqlParameter("i_nfase",			OracleTypes.NUMBER),
					new SqlParameter("i_nrevi",			OracleTypes.NUMBER),
					new SqlParameter("i_dplazo",		OracleTypes.DATE),
					new SqlParameter("i_dliberacion",	OracleTypes.DATE),
					new SqlParameter("i_nciclo",		OracleTypes.NUMBER),
					new SqlParameter("i_vcomentario",	OracleTypes.VARCHAR),
					new SqlParameter("i_nplazo",		OracleTypes.NUMBER),
					new SqlParameter("i_nprioridad",	OracleTypes.NUMBER),
				    new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlParameter("i_nidequi",		OracleTypes.NUMBER),
				    new SqlParameter("i_vcargpart",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));			
			SqlParameterSource in =
			new MapSqlParameterSource()
			.addValue("i_dfecha_regis",	FechaCreaDoc)
				.addValue("i_ncolaborador",	colaborador.getNumeroFicha())//colaborador.getIdColaborador())
				.addValue("i_ndocumento",	codigo)
				.addValue("i_nfase",		fase)
				.addValue("i_nrevi",		revi/*colaborador.getDocumento().getIdrevi()*/)// ID revision
				.addValue("i_dplazo",		colaborador.getFechaPlazo())
				.addValue("i_dliberacion",	colaborador.getFechaLiberacion())
				.addValue("i_nciclo",		colaborador.getCiclo())
				.addValue("i_vcomentario",	colaborador.getComentario())
				.addValue("i_nplazo",		colaborador.getPlazo())
				.addValue("i_nprioridad",	colaborador.getPrioridad())
				.addValue("i_ndisponible",	colaborador.getDisponible())
				.addValue("i_vusuario",		"AGI MIGRACIÓN")
				.addValue("i_nidequi",		colaborador.getEquipo() != null ? colaborador.getEquipo().getId() : null)
				.addValue("i_vcargpart",	colaborador.getFuncion());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Colaborador();
					
					if (map.get("N_IDCOLA")!=null) {
						item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());	
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
					
					if (map.get("N_IDEQUI")!=null) {
						Equipo equipo = new Equipo();
						equipo.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
						item.setEquipo(equipo);
					}
					
					if (map.get("V_CARGPART")!=null) {
						item.setFuncion((String)map.get("V_CARGPART"));
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

	private Boolean eliminarParticipante(Long colaborador, Long documento, Long fase, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_PARTICIPANTE_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_ncolaborador",	OracleTypes.NUMBER),
				new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
				new SqlParameter("i_nfase",			OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_ncolaborador",	colaborador)
			.addValue("i_ndocumento",	documento)
			.addValue("i_nfase",		fase)
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
	private Documento registrarComplementario(Documento hijo, Long padre ,Long revi, String usuario, Date fechacreadoc) {
		Documento item			= null;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_COMPLEMENTARIO_GUARDAR")
				.declareParameters(	
					new SqlParameter("i_dfecha",	OracleTypes.DATE),    
					new SqlParameter("i_nhijo",			OracleTypes.NUMBER),
					new SqlParameter("i_npadre",		OracleTypes.NUMBER),
					new SqlParameter("i_ntipo",			OracleTypes.NUMBER),
				    new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlParameter("i_nrevision",		OracleTypes.NUMBER),					
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
			.addValue("i_dfecha",	fechacreadoc)
				.addValue("i_nhijo",		hijo.getId())
				.addValue("i_npadre",		padre)
				.addValue("i_nrevision",	revi)
				.addValue("i_ntipo",		(hijo.getTipoComplementario()==null)?null:
											 hijo.getTipoComplementario().getIdconstante())
				.addValue("i_ndisponible",	hijo.getDisponible())
				.addValue("i_vusuario",		"AGI MIGRACIÓN");
		
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

	private Boolean eliminarComplementario(Long padre, Long hijo, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_COMPLEMENTARIO_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_npadre",	OracleTypes.NUMBER),
				new SqlParameter("i_nhijo",		OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_npadre",	padre)
			.addValue("i_nhijo",	hijo)
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
	private Equipo registrarEquipo(Equipo equipo, Long codigo, String usuario, Long idRevi, Date fecharegistro) {
		Equipo item				= null;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_EQUIPORESPONSABLE_GUARDAR")
				.declareParameters(
						new SqlParameter("i_dfecha_regis",	OracleTypes.DATE),
					new SqlParameter("i_ndocumento",	OracleTypes.NUMBER),
					new SqlParameter("i_nequipo",		OracleTypes.NUMBER),
						new SqlParameter("i_nrevision",		OracleTypes.NUMBER),
						new SqlParameter("i_indicador",		OracleTypes.NUMBER),
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
			.addValue("i_dfecha_regis",			fecharegistro)//Fecha de registro doc
				.addValue("i_ndocumento",		codigo)
				.addValue("i_nequipo",			equipo.getId())
					.addValue("i_nrevision", 	idRevi)
					.addValue("i_indicador", 	equipo.getIndicadorResp())					
				.addValue("i_vrevision",		equipo.getIndicadorRevision())
				.addValue("i_vnotificacion",	equipo.getIndicadorNotificacion())
				.addValue("i_vequipousuario",	equipo.getIndicadorResponsable())
				.addValue("i_ndisponible",		equipo.getDisponible())
				.addValue("i_vusuario",			"AGI MIGRACIÓN");
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {	
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Equipo();
					if(map.get("N_IDEQUI")!= null) {
						item.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
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
			String mensaje			= "Error en MigracionDAOImpl.registrarEquipo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= null;
		}
		return item;
	}

	private Boolean eliminarEquipo(Long documento, Long equipo, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
			.withProcedureName("PRC_EQUIPORESPONSABLE_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_ndocumento",OracleTypes.NUMBER),
				new SqlParameter("i_nequipo",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_ndocumento",	documento)
			.addValue("i_nequipo",		equipo)
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
		jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION").withProcedureName("PRC_GENERAR_CODDOCU");
		jdbcCall.declareParameters(
				new SqlParameter("i_nidgeregnrl", OracleTypes.NUMBER),
				new SqlParameter("i_nidtipodocu", OracleTypes.NUMBER),
				new SqlOutParameter("o_codigo", OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)
		);
		SqlParameterSource in = new MapSqlParameterSource()
													.addValue("i_nidgeregnrl", codigoGerencia)
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
	@Override
	public List<Historico> obtenerHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Historico> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_HISTORICO_OBTENER")
				.declareParameters(
						new SqlParameter("i_nid", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		
		SqlParameterSource in;
		in = new MapSqlParameterSource()
				.addValue("i_nid", documentoRequest.getId())
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");	
			if(resultado == 0) {
				lista = mapearHistorico(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<Historico> mapearHistorico(Map<String, Object> resultados) {
		List<Historico> listaHistorico = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Historico item = null;
		
		for(Map<String, Object> map : lista) {
			item = new Historico();			
			item.setIdDocumento(((BigDecimal)map.get("ID_DOCUMENTO")).longValue());
			item.setIdRevision(((BigDecimal)map.get("ID_REVISION")).longValue());
			item.setNumeroRevision(((BigDecimal)map.get("NUMERO_REVISION")).longValue());
			item.setIdEtapa(((BigDecimal)map.get("ID_ETAPA")).longValue());			
			item.setEtapa((String)map.get("ETAPA"));
			item.setFecha((Date)map.get("FECHA"));
			item.setIdColaborador(((BigDecimal)map.get("ID_COLABORADOR")).longValue());
			item.setColaborador((String)map.get("COLABORADOR"));
			item.setComentario((String)map.get("COMENTARIO"));			
			item.setIdEstadoFase(((BigDecimal)map.get("IDESTAFASE")).longValue());
			listaHistorico.add(item);

			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaHistorico;
	}
	
	
	@Override
	public List<Historico> obtenerDetalleHistorico(DocumentoRequest documentoRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Historico> lista = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_HISTORICO_OBTENER_DET")
				.declareParameters(
						new SqlParameter("i_nid", OracleTypes.NUMBER),
						new SqlParameter("i_numrevi", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		
		SqlParameterSource in;
		in = new MapSqlParameterSource()
				.addValue("i_nid", documentoRequest.getId())
				.addValue("i_numrevi", documentoRequest.getNumrevi())
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");	
			if(resultado == 0) {
				lista = mapearHistorico(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private Boolean registrarFlujo(Flujo objeto, String usuario) {
		boolean item		    = true;
		Map<String, Object> out = null;
		this.error				= null;
		
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
					.withProcedureName("PRC_FLUJO_GUARDAR")
					.declareParameters(
							new SqlParameter("i_niddocu",	OracleTypes.NUMBER),
							new SqlParameter("i_nidfase",	OracleTypes.NUMBER),
							new SqlParameter("i_nidrevi",		OracleTypes.NUMBER),
							//new SqlParameter("i_nnumiter",	OracleTypes.NUMBER),
							//new SqlParameter("i_nestado",		OracleTypes.NUMBER),
							new SqlParameter("i_nusuafase",	 OracleTypes.NUMBER),
							//new SqlParameter("i_nactividad",	OracleTypes.NUMBER),
							//new SqlParameter("i_vcritica",		OracleTypes.VARCHAR),
							//new SqlParameter("i_vcomentario",	OracleTypes.VARCHAR),
							//new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
							new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
							//new SqlParameter("i_vindaprobsoli",	OracleTypes.VARCHAR),
							//new SqlParameter("i_vindfase",		OracleTypes.VARCHAR),
							//new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_niddocu",	 objeto.getIdDocumento())
					.addValue("i_nidfase",	 objeto.getIdFase())
					.addValue("i_nidrevi",	 objeto.getIdRevision())
					//.addValue("i_nnumiter",	 objeto.getIteracion())
					//.addValue("i_nestado",		 objeto.getIdEstadoFase())
					.addValue("i_nusuafase",	 objeto.getUsuarioFase())
					//.addValue("i_nactividad",	 objeto.getIndicadorActividad())
					//.addValue("i_vcritica",		 objeto.getCritica())
					//.addValue("i_vcomentario",	 objeto.getComentario())
					//.addValue("i_ndisponible",	 objeto.getDisponible())
					.addValue("i_vusuario",		 "AGI_MIGRACIÓN");
					//.addValue("i_vindaprobsoli", objeto.getIndAprobacionSoli())
					//.addValue("i_vindfase",		 objeto.getIndicadorFase());
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado != 0) {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= false;
				/*List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : listado) {
					item = new Flujo();
					if(map.get("n_iddocu")!=null) item.setIdDocumento(((BigDecimal)map.get("n_iddocu")).longValue());
					if(map.get("n_idrevi")!=null) item.setIdRevision(((BigDecimal)map.get("n_idrevi")).longValue());
					if(map.get("n_idfase")!=null) item.setIdFase(((BigDecimal)map.get("n_idfase")).longValue());
					if(map.get("n_usuafase")!=null) item.setUsuarioFase(((BigDecimal)map.get("n_usuafase")).longValue());
					if(map.get("d_fechfase")!=null) item.setFechaFase((Date)map.get("d_fechfase"));	
					if(map.get("n_indirevi")!=null) item.setIndicadorActividad(((BigDecimal)map.get("n_indirevi")).longValue());
					if(map.get("n_idestafase")!=null) item.setIdEstadoFase(((BigDecimal)map.get("n_idestafase")).longValue());
					if(map.get("v_critfase")!=null) item.setCritica((String)map.get("v_critfase"));	
					if(map.get("n_disfludoc")!=null) item.setDisponible(((BigDecimal)map.get("n_disfludoc")).longValue());
					if(map.get("v_indiapro")!=null) item.setIndicadorAprobado((String)map.get("v_indiapro"));
				}*/
			}/* else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				item					= null;
			}*/
		} catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en MigracionDAOImpl.registrarFlujo";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			item					= false;
		}
		return item;
	}

	public void guardarCritica(Documento documento, Documento item){
		this.jdbcCall = new SimpleJdbcCall(this.jdbc);
		this.jdbcCall.withSchemaName("AGI").withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_CRITICA_GUARDAR").declareParameters(
				new SqlParameter("i_niddocu", OracleTypes.NUMBER),
				new SqlParameter("i_nidfase", OracleTypes.NUMBER),
				new SqlParameter("i_nusuafase", OracleTypes.VARCHAR),
				new SqlParameter("i_dfechfase", OracleTypes.DATE),
				new SqlParameter("i_nidrevi", OracleTypes.NUMBER),
				new SqlParameter("i_vcomentario", OracleTypes.VARCHAR),
				new SqlParameter("i_vusucre", OracleTypes.VARCHAR));

		///if(documento.getCritica().getCodigoColaboradorFase() == null){ documento.getCritica().setCodigoFase(null);}
		if(documento.getCritica().getFechaFase() == null) {documento.getCritica().setCodigoFase(null);}
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_niddocu", item.getId())
				.addValue("i_nidfase", documento.getCritica().getNumeroFicha() != null? documento.getCritica().getCodigoFase(): null)
				.addValue("i_nusuafase", documento.getCritica() != null? documento.getCritica().getNumeroFicha(): null)
				.addValue("i_dfechfase", documento.getCritica() != null? documento.getCritica().getFechaFase(): null)
				.addValue("i_vcomentario", documento.getCritica() != null? documento.getCritica().getObservacion(): null)
				.addValue("i_nidrevi", item.getRevision()!= null? item.getRevision().getId():null)
				.addValue("i_vusucre", "AGI_MIGRACION");

		try {
			Map<String, Object> out = this.jdbcCall.execute(in);
			Integer resultado = ((BigDecimal)out.get("O_RETORNO")).intValue();

			if(resultado != 0) {
				String mensaje			= (String)out.get("O_MENSAJE");
				String mensajeInterno	= (String)out.get("O_SQLERRM");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				System.out.println("Error al guardar la critica" + this.error.getMensaje());
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en MigracionDAOImpl.guardarCritica";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			System.out.println("Error al guardar la critica" + this.error.getMensaje());
		}
	}

	@Override
	public List<Colaborador> obtenerColaboradores(ColaboradorRequest colaboradorRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Colaborador> lista	= new ArrayList<>();
		this.paginacion = new Paginacion();
		this.paginacion.setPagina(pageRequest.getPagina());
		this.paginacion.setRegistros(pageRequest.getRegistros());

		this.jdbcCall	=
				new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO_MIGRACION")
				.withProcedureName("PRC_COLABORADOR_OBTENER")
				.declareParameters(
				new SqlParameter("i_ncodtrabajador", OracleTypes.NUMBER),
				new SqlParameter("i_nficha", OracleTypes.NUMBER),
				new SqlParameter("i_vnombre", OracleTypes.VARCHAR),
				new SqlParameter("i_vapepaterno", OracleTypes.VARCHAR),
				new SqlParameter("i_vapematerno", OracleTypes.VARCHAR),
				new SqlParameter("i_npagina", OracleTypes.NUMBER),
				new SqlParameter("i_nregistros", OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		SqlParameterSource in =
		new MapSqlParameterSource()
				.addValue("i_ncodtrabajador", colaboradorRequest.getId())
				.addValue("i_nficha", colaboradorRequest.getNumeroFicha())
				.addValue("i_vnombre", colaboradorRequest.getNombre())
				.addValue("i_vapepaterno", colaboradorRequest.getApellidoPaterno())
				.addValue("i_vapematerno", colaboradorRequest.getApellidoMaterno())
				.addValue("i_npagina", pageRequest.getPagina())
				.addValue("i_nregistros", pageRequest.getRegistros());
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
			String mensaje			= "Error en MigracionDAOImpl.obtenerColaborador";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}/*
		try {
			Map<String, Object> resultadoDatos = this.jdbcCall.execute(parametros);
			BigDecimal codigoRetorno = (BigDecimal) resultadoDatos.get("O_RETORNO");
			if (codigoRetorno.intValue() != 0) {
				String mensajeError = (String) resultadoDatos.get("O_MENSAJE");
				String errorSql = (String) resultadoDatos.get("O_SQLERRM");
				error = new Error(codigoRetorno.intValue(), mensajeError, errorSql);
			} else {
				colaboradores = (List<Colaborador>) resultadoDatos.get("o_cursor");
				if(colaboradores.size() > 0) {
					this.paginacion.setTotalRegistros(colaboradores.get(0).getNumeroRegistros().intValue());
				} else {
					this.paginacion.setTotalRegistros(0);
				}
			}
			return colaboradores;
		} catch (Exception exception) {
			error = new Error(exception.hashCode(), exception.getCause().toString(), "");
			return colaboradores;
		}*/
		 return lista;
	}
	@SuppressWarnings("unchecked")
	private List<Colaborador> mapearColaborador(Map<String, Object> resultados) {
		List<Colaborador> listaColaborador	= new ArrayList<>();		
		List<Map<String, Object>> lista 	= (List<Map<String, Object>>)resultados.get("o_cursor");
		Colaborador item					= null;
		
		for(Map<String, Object> map : lista) {			
			item = new Colaborador();
			
			if(map.get("idColaborador")!=null){
				item.setIdColaborador(((BigDecimal)map.get("idColaborador")).longValue());	
			}
			if(map.get("funcion")!=null) {
				item.setFuncion((String)map.get("funcion"));	
			}
			if(map.get("numeroFicha")!=null) {
				item.setNumeroFicha(((BigDecimal)map.get("numeroFicha")).longValue());	
			}
			
			if(map.get("nombre")!=null) {
				item.setNombre((String)map.get("nombre"));	
			}
			if(map.get("apellidoPaterno")!=null) {
				item.setApellidoPaterno((String)map.get("apellidoPaterno"));	
			}
			if(map.get("apellidoMaterno")!=null) {
				item.setApellidoMaterno((String)map.get("apellidoMaterno"));	
			}
			if(map.get("idRolAuditor")!=null) {
				Equipo equipo = new Equipo();
				equipo.setId(((BigDecimal)map.get("idRolAuditor")).longValue());
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
}