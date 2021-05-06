package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.ISeguimientoDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Flujo;
import pe.com.sedapal.agi.model.SeguimientoDocumento;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class SeguimientoDAOImpl implements ISeguimientoDAO{
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
	public List<SeguimientoDocumento> obtenerSeguimientoDocumento(DocumentoRequest documentoRequest,
			PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<SeguimientoDocumento> lista = new ArrayList<>();		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
          this.jdbcCall = new SimpleJdbcCall(this.jdbc)
      			.withSchemaName("AGI")
    			.withCatalogName("PCK_AGI_TAREAS")
    			.withProcedureName("PRC_OBTENER_SEGUI_DOC")
    			.declareParameters(
    				new SqlParameter("i_npagina", 			OracleTypes.NUMBER),						
    				new SqlParameter("i_nregistros", 		OracleTypes.NUMBER),
    				new SqlParameter("i_vcoddocu", 		OracleTypes.VARCHAR),
    				new SqlParameter("i_vdesdocu", 		OracleTypes.VARCHAR),						
    				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
    				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
    				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
    				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
    		
    		SqlParameterSource in =
    		new MapSqlParameterSource()
    			.addValue("i_npagina",			pageRequest.getPagina())
    			.addValue("i_nregistros",		pageRequest.getRegistros())
    			.addValue("i_vcoddocu",		documentoRequest.getCodigo())
    			.addValue("i_vdesdocu",		documentoRequest.getDescripcion());
          try {
                 out = this.jdbcCall.execute(in);
                 Integer resultado = (Integer)out.get("o_retorno");
                 if(resultado == 0) {
                        lista = mapearSeguimientoDocumento(out);                        
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
	
	private List<SeguimientoDocumento> mapearSeguimientoDocumento(Map<String, Object> resultados) {
		List<SeguimientoDocumento> listaDocumento 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		SeguimientoDocumento item = null;
		int size = lista.size();
		
		for(Map<String, Object> map : lista) {			
			item = new SeguimientoDocumento();
			if(map.get("idDocu") != null) {
				item.setIdDocumento(((BigDecimal)map.get("idDocu")).longValue());
			}
			if(map.get("codDocu") != null) {
				item.setCodDocumento((String)map.get("codDocu"));
			}
			if(map.get("desDocu") != null) {
				item.setDesDocumento((String)map.get("desDocu"));
			}
			
			
			if(map.get("idMotiRevi")!=null) {
				Constante motivoRevision = new Constante();
				motivoRevision.setIdconstante(((BigDecimal)map.get("idMotiRevi")).longValue());
				motivoRevision.setV_valcons((String)map.get("motiRevi"));
				item.setMotivoRevision(motivoRevision);
			}
			
			if(map.get("idEstado")!=null) {
				Constante estado = new Constante();
				estado.setIdconstante(((BigDecimal)map.get("idEstado")).longValue());
				estado.setV_valcons((String)map.get("estado"));
				item.setEstadoDocumento(estado);
			}
			
			if(map.get("idRevi")!=null) {
				item.setIdRevision(((BigDecimal)map.get("idRevi")).longValue());
			}
			
			if(map.get("numIter")!=null) {
				item.setNumeroIteracion(((BigDecimal)map.get("numIter")).longValue());
			}
			
			if(map.get("numRevi")!=null) {
				item.setNumeroRevision(((BigDecimal)map.get("numRevi")).longValue());
			}
			
			
			if(map.get("fecRevi") != null) {
				item.setFechaRevision(new Date(((Timestamp)map.get("fecRevi")).getTime()));
			}
			if(map.get("fecRech") != null) {
				item.setFechaRechazoRevision(new Date(((Timestamp)map.get("fecRech")).getTime()));
			}
			if(map.get("fecApro") != null) {
				item.setFechaAprobacionRevision(new Date(((Timestamp)map.get("fecApro")).getTime()));
			}
			if(map.get("idFaseActual") != null) {
				Constante faseActual = new Constante();
				faseActual.setIdconstante(((BigDecimal)map.get("idFaseActual")).longValue());
				faseActual.setV_valcons((String)map.get("faseActual"));
				item.setFaseActual(faseActual);
			}
			if(map.get("textoFase") != null) {
				item.setTextoFase((String)map.get("textoFase"));
			}
			if(map.get("textoEstadoFase") != null) {
				item.setTextoEstadoFase((String)map.get("textoEstadoFase"));
			}
			
			


			listaDocumento.add(item);

			if (this.paginacion != null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("NUMEROREGISTROS")).intValue());
			}
		}
		
		return listaDocumento;
	}
	@Override
	public List<Flujo> obtenerFlujosDocumento(RevisionRequest revisionRequest) {
		Map<String, Object> out	= null;
		List<Flujo> lista = new ArrayList<>();		
		this.error		= null;
		
          this.jdbcCall = new SimpleJdbcCall(this.jdbc)
      			.withSchemaName("AGI")
    			.withCatalogName("PCK_AGI_TAREAS")
    			.withProcedureName("PRC_OBTENER_FLUJO_DOCU")
    			.declareParameters(
    				new SqlParameter("i_niddocu", 			OracleTypes.NUMBER),						
    				new SqlParameter("i_nidrevi", 		OracleTypes.NUMBER),
    				new SqlParameter("i_nnumiter", 		OracleTypes.NUMBER),					
    				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
    				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
    				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
    				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
    		
    		SqlParameterSource in =
    		new MapSqlParameterSource()
    			.addValue("i_niddocu",			revisionRequest.getIdDocumento())
    			.addValue("i_nidrevi",		revisionRequest.getId())
    			.addValue("i_nnumiter",		revisionRequest.getNumeroIteracion());
          try {
                 out = this.jdbcCall.execute(in);
                 Integer resultado = (Integer)out.get("o_retorno");
                 if(resultado == 0) {
                        lista = mapearFasesDocumento(out);                        
                 } else {
                        String mensaje = (String)out.get("o_mensaje");
                        String mensajeInterno = (String)out.get("o_sqlerrm");
                        this.error = new Error(resultado,mensaje,mensajeInterno);
                 }
          } catch(Exception e){
        	     e.printStackTrace();
                 Integer resultado = (Integer)out.get("o_retorno");
                 String mensaje = (String)out.get("o_mensaje");
                 String mensajeInterno = (String)out.get("o_sqlerrm");
                 this.error = new Error(resultado,mensaje,mensajeInterno);
          }
          return lista;
	}
	
	private List<Flujo> mapearFasesDocumento(Map<String, Object> resultados) {
		List<Flujo> listaFlujo 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Flujo item = null;
		
		for(Map<String, Object> map : lista) {			
			item = new Flujo();
			if(map.get("idDocumento") != null) {
				item.setIdDocumento(((BigDecimal)map.get("idDocumento")).longValue());
			}
			if(map.get("idRevision") != null) {
				item.setIdRevision(((BigDecimal)map.get("idRevision")).longValue());
			}
			if(map.get("numIter") != null) {
				item.setIteracion(((BigDecimal)map.get("numIter")).longValue());
			}
			if(map.get("idFase") != null) {
				item.setIdFase(((BigDecimal)map.get("idFase")).longValue());
			}
			if(map.get("descFase") != null) {
				item.setDescripcionFase((String)map.get("descFase"));
			}
			if(map.get("indicadorFase") != null) {
				item.setIndicadorActividad(((BigDecimal)map.get("indicadorFase")).longValue());
			}
			
			listaFlujo.add(item);
		}
		
		return listaFlujo;
	}
}
