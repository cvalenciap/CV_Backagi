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
import pe.com.sedapal.agi.dao.IReporteConocimientoDocumentoDAO;
import pe.com.sedapal.agi.model.ConocimientoRevisionDocumento;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class ReporteConocimientoDocumentoDAOImpl implements IReporteConocimientoDocumentoDAO {
	
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;
		
	
	private static final Logger LOGGER = Logger.getLogger(ReporteConocimientoDocumentoDAOImpl.class);	
	
	@Override
	public List<Trabajador> consultarPersonasDesconocenDocumento(RevisionRequest request) {		
		Map<String, Object> out = null;
		List<Trabajador> listaPersonas = new ArrayList<>();
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_TAREAS")
					.withProcedureName("PRC_OBTENER_DOC_DESCONOCI")
					.declareParameters(
							new SqlParameter("i_niddocu", OracleTypes.NUMBER),
							new SqlParameter("i_nidrevi", OracleTypes.NUMBER),
							new SqlParameter("i_nestado", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_niddocu", request.getIdDocumento())
					.addValue("i_nidrevi", request.getId())
					.addValue("i_nestado", request.getEstCono());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaPersonas = mapearTrabajador(out);

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en ReporteConocimientoDocumentoDAOImpl.consultarPersonasDesconocenDocumento";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return listaPersonas;
	}
	
	private List<Trabajador> mapearTrabajador(Map<String,Object> resultados) {
		List<Trabajador> listaTrabajador = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Trabajador item = null;
		int contador = 0;
		for(Map<String, Object> map : lista) {	
			contador++;
			item = new Trabajador();
			
			if(map.get("nroFicha") != null) {
			item.setNroFicha(((BigDecimal)map.get("nroFicha")).longValue());
			}
			if(map.get("nombreTrabajador") != null) {	
			item.setNombreTrabajador((String)map.get("nombreTrabajador"));
			}
			if(map.get("apePaternoTrabajador") != null) {
			item.setApePaternoTrabajador((String)map.get("apePaternoTrabajador"));
			}
			
			item.setItem(contador);
			
			listaTrabajador.add(item);

		}
		
		return listaTrabajador;
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

	@Override
	public List<ConocimientoRevisionDocumento> consultarDocumentosRevision(DocumentoRequest documentoRequest,PageRequest paginaRequest) {		
		Map<String, Object> out = null;
		List<ConocimientoRevisionDocumento> listaConocimiento = new ArrayList<>();
		this.error = null;
		this.paginacion = new Paginacion();
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_TAREAS")
					.withProcedureName("PRC_OBTENER_CONOCIMIENTO")
					.declareParameters(
							new SqlParameter("i_vcoddocu", OracleTypes.VARCHAR),
							new SqlParameter("i_nnumrevi",OracleTypes.NUMBER),
							new SqlParameter("i_feaprobini",OracleTypes.DATE),
							new SqlParameter("i_feaprobfin",OracleTypes.DATE),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_vcoddocu",documentoRequest.getCodigo())
					.addValue("i_nnumrevi",(documentoRequest.getNumrevi()==999)?null:documentoRequest.getNumrevi())					
					.addValue("i_feaprobini",(Date)documentoRequest.getFechaaprobdesde())
					.addValue("i_feaprobfin",(Date)documentoRequest.getFechaaprobhasta())
					.addValue("i_npagina", paginaRequest.getPagina())
					.addValue("i_nregistros", paginaRequest.getRegistros());
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				listaConocimiento = mapearConocimiento(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en ReporteConocimientoDocumentoDAOImpl.consultarDocumentosRevision";
			String mensajeInterno = e.getMessage();
			
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return listaConocimiento;
	}
	
	private List<ConocimientoRevisionDocumento> mapearConocimiento(Map<String,Object> resultados) {
		List<ConocimientoRevisionDocumento> listaConocimiento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		ConocimientoRevisionDocumento item = null;
		for(Map<String, Object> map : lista) {	
			item = new ConocimientoRevisionDocumento();
			item.setDocumento(new Documento());
			if(map.get("idDocumento") != null) {
				item.getDocumento().setId(((BigDecimal)map.get("idDocumento")).longValue());
			}
			if(map.get("codigoDocumento") != null) {	
				item.getDocumento().setCodigo((String)map.get("codigoDocumento"));
			}
			if(map.get("tituloDocumento") != null) {	
				item.getDocumento().setDescripcion((String)map.get("tituloDocumento"));
			}
			item.getDocumento().setEstado(new Constante());
			if(map.get("idEstadoDocumento") != null) {
				item.getDocumento().getEstado().setIdconstante(((BigDecimal)map.get("idEstadoDocumento")).longValue());
				item.getDocumento().getEstado().setV_valcons((String)map.get("estadoDocumento"));
			}
			

			
			item.setRevision(new Revision());
			if(map.get("idRevision") != null) {
				item.getRevision().setId(((BigDecimal)map.get("idRevision")).longValue());
				item.getRevision().setFechaAprobacion((Date)map.get("FECAPROB"));
			}
			
			if(map.get("numeroRevision") != null) {
				item.getRevision().setNumRevi(((BigDecimal)map.get("numeroRevision")).longValue());
			}
			
			if(map.get("cantidadDesconoc") != null) {
				item.setCantidadDesconoc(((BigDecimal)map.get("cantidadDesconoc")).longValue());
			}
			
			if(map.get("cantidadConoc") != null) {
				item.setCantidadConoc(((BigDecimal)map.get("cantidadConoc")).longValue());
			}
			
			listaConocimiento.add(item);
			
			if (map.get("numeroRegistros")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("numeroRegistros")).intValue());
			}

		}
		
		return listaConocimiento;
	}

}
