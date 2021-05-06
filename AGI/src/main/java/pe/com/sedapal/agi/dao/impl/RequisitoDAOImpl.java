package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import pe.com.sedapal.agi.dao.IRequisitoDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Requisito;
import pe.com.sedapal.agi.model.RequisitoDocumento;
import pe.com.sedapal.agi.model.RequisitoRelacionado;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RequisitoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.DBConstants;
//import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.ValidarCampos;

@Repository
public class RequisitoDAOImpl implements IRequisitoDAO{
	
	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

	private static final Logger LOGGER = Logger.getLogger(NormaDAOImpl.class);

	public Error getError() {
		return this.error;
	}

	public Paginacion getPaginacion() {
		return this.paginacion;
	}

	@Override
	public List<Requisito> listaNormaRequerimientos(PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Requisito> listaConstantes = new ArrayList<>();
		this.error = null;
		this.paginacion = new Paginacion();
		this.paginacion.setPagina(pageRequest.getPagina());
		this.paginacion.setRegistros(pageRequest.getRegistros());

		try {

			SimpleJdbcCall jdbcCallAux = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT)
					.withProcedureName(DBConstants.PRC_MANT_LISTAR_NORMAS_REQUISITOS_AUDI)
					.declareParameters(new SqlOutParameter("O_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
							new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

			out = jdbcCallAux.execute();
			Integer resultado = (Integer) out.get("O_RETORNO");
			if (resultado == 0) {
				listaConstantes = this.mapearNormaRequisitos(out);
			} else {
				String mensaje = (String) out.get("O_MENSAJE");
				String mensajeInterno = (String) out.get("O_SQLERRM");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en RequisitoDAOImpl.listaNormaRequerimientos";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(e);
		}

		return listaConstantes;
	}

	@SuppressWarnings("unchecked")
	private List<Requisito> mapearNormaRequisitos(Map<String, Object> resultados) {
		List<Requisito> listaRequisito = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("O_CURSOR");
		Requisito requisito = null;
		DatosAuditoria datosAuditoria = null;

		for (Map<String, Object> map : lista) {
			requisito = new Requisito();
			requisito.setN_id_requisito(ValidarCampos.castValorBigDecimalToInteger((BigDecimal) map.get("N_ID_REQUISITO")));
			requisito.setN_id_normas(ValidarCampos.castValorBigDecimalToInteger((BigDecimal) map.get("N_ID_NORMAS")));
			requisito.setV_num_req(ValidarCampos.validaCamposString((String) map.get("V_NUM_REQ")));
			requisito.setV_nom_req(ValidarCampos.validaCamposString((String) map.get("V_NOM_REQ")));
			requisito.setN_nivel_req(ValidarCampos.castValorBigDecimalToInteger((BigDecimal) map.get("N_NIVEL_REQ")));
			requisito.setN_id_req_padre(ValidarCampos.castValorBigDecimalToInteger((BigDecimal) map.get("N_ID_REQ_PADRE")));
			requisito.setN_auditable(ValidarCampos.castValorBigDecimalToInteger((BigDecimal) map.get("N_AUDITABLE")));
			requisito.setV_desc_req(ValidarCampos.validaCamposString((String) map.get("V_DESC_REQ")));
			requisito.setV_st_reg(ValidarCampos.validaCamposString((String) map.get("V_ST_REG")));
			datosAuditoria = new DatosAuditoria();
			datosAuditoria.setUsuarioCreacion(ValidarCampos.validaCamposString((String) map.get("A_V_ID_USUA_CREA")));
			datosAuditoria.setFechaCreacion(ValidarCampos.validaCamposDate((String) map.get("A_D_FE_USUA_CREA")));
			datosAuditoria
					.setUsuarioModificacion(ValidarCampos.validaCamposString((String) map.get("A_V_ID_USUA_MODI")));
			datosAuditoria.setFechaModificacion(ValidarCampos.validaCamposDate((String) map.get("A_D_FE_USUA_MODI")));
			requisito.setDatosAuditoria(datosAuditoria);
			listaRequisito.add(requisito);
		}

		if (listaRequisito.size() > 0) {
			this.paginacion.setTotalRegistros(((BigDecimal) lista.get(0).get("RESULT_COUNT")).intValue());
		}
		return listaRequisito;

	}
	/*
	
	private static final Logger LOGGER = Logger.getLogger(RequisitoDAOImpl.class);	
	
	@Override
	public List<Requisito> obtenerRequisitos(RequisitoRequest parametros) {		
		Map<String, Object> out = null;
		List<Requisito> listaRequisito = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_NORMA_REQUI_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidnorma", OracleTypes.NUMBER),						
						new SqlParameter("i_vtiponorma", OracleTypes.VARCHAR),					
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));		
		SqlParameterSource in = new MapSqlParameterSource()
		.addValue("i_nidnorma",parametros.getIdNorma() )
		.addValue("i_vtiponorma",parametros.getTipoNorma() );
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)  out.get("o_retorno");			
			if(resultado == 0) {
				listaRequisito = mapearRequisito(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
	  return listaRequisito;
	}
	
	private List<Requisito> mapearRequisito(Map<String,Object> mapaRequisitos) {
		List<Requisito> listaRequisitos = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>)mapaRequisitos.get("o_cursor");
		Requisito item = null;
		
		for(Map<String, Object> map : lista) {			
			item = new Requisito();
			item.setTipoNorma((String)map.get("tipo"));
			item.setDescripcionNorma((String)map.get("norma"));
			item.setIdNorma(((BigDecimal)map.get("idNorma")).longValue());
			item.setIdRequisito(((BigDecimal)map.get("idRequisito")).longValue());
			item.setOrden((String)map.get("orden"));
			item.setNivel((String)map.get("nivel"));
			item.setDescripcionReq((String)map.get("requisito"));
			if(map.get("idRequisitoPadre")!=null) {
				item.setIdRequisitoPadre(((BigDecimal)map.get("idRequisitoPadre")).longValue());
			}
			item.setVdetreq((String)map.get("v_detreq"));
			item.setVcuesti((String)map.get("v_cuesti"));
			
			//Se agregó
			item.setIdNorma(((BigDecimal)map.get("idNorma")).longValue());
			item.setIdNorReq(((BigDecimal)map.get("idNorreq")).longValue());
			item.setIdRequisito(((BigDecimal)map.get("idRequisito")).longValue());
			ArrayList<RequisitoRelacionado> listaReqRela = this.obtenerRequisitosRelacionado(item.getIdNorReq());
			item.setRequisitoRelacionado(listaReqRela);
			ArrayList<RequisitoDocumento> listaReqDocu = this.obtenerRequisitosDocumento(item.getIdNorReq());
			item.setRequisitoDocumento(listaReqDocu);

				
			listaRequisitos.add(item);

		}
		return listaRequisitos;
	}
	

	public ArrayList<RequisitoRelacionado> obtenerRequisitosRelacionado(Long norreq) {
		Map<String, Object> out = null;
		ArrayList<RequisitoRelacionado> listaRequisito = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_NORMA_REQUI_RELA_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidnorreq", OracleTypes.NUMBER),						
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));		
		
				SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidnorreq",norreq );

		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)  out.get("o_retorno");			
			if(resultado == 0) {
				listaRequisito = mapearRequisitoRelacionado(out);
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
	  return listaRequisito;
	}
	
	private ArrayList<RequisitoRelacionado> mapearRequisitoRelacionado(Map<String,Object> mapaRequisitos) {
		ArrayList<RequisitoRelacionado> listaRequisitos = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>)mapaRequisitos.get("o_cursor");
		RequisitoRelacionado item = null;
		
		for(Map<String, Object> map : lista) {	
			
			item = new RequisitoRelacionado();
			
			if(map.get("NORMA")!=null) item.setNormaRelacionada((String)map.get("NORMA"));
			if(map.get("ORDENREQ")!=null && map.get("REQUISITO")!=null) item.setRequiRelacionado((String)map.get("ORDENREQ") + ' '+ (String)map.get("REQUISITO"));
     		if(map.get("N_IDREQRELA")!=null) item.setIdReqRela(((BigDecimal)map.get("N_IDREQRELA")).longValue());
			if(map.get("N_IDNORMA")!=null) item.setIdNorma(((BigDecimal)map.get("N_IDNORMA")).longValue());
			if(map.get("N_IDNORREQ")!=null) item.setIdNorReq(((BigDecimal)map.get("N_IDNORREQ")).longValue());
			if(map.get("N_IDREQUISITO")!=null) item.setIdRequisito(((BigDecimal)map.get("N_IDREQUISITO")).longValue());
			if(map.get("V_ESTREQREL")!=null) item.setVestreqrel((String)map.get("V_ESTREQREL"));
			
			listaRequisitos.add(item);
			
		}
		return listaRequisitos;
	}
	
	public ArrayList<RequisitoDocumento> obtenerRequisitosDocumento(Long norreq) {
		Map<String, Object> out = null;
		ArrayList<RequisitoDocumento> listaRequisito = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_NORMA_REQUI_DOCU_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidnorreq", OracleTypes.NUMBER),						
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));		
		
				SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidnorreq",norreq );
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)  out.get("o_retorno");			
			if(resultado == 0) {
				listaRequisito = mapearRequisitoDocumento(out);
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
	  return listaRequisito;
	}
	
	private ArrayList<RequisitoDocumento> mapearRequisitoDocumento(Map<String,Object> mapaRequisitos) {
		ArrayList<RequisitoDocumento> listaRequisitos = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lista = (List<Map<String, Object>>)mapaRequisitos.get("o_cursor");
		RequisitoDocumento item = null;
		
		for(Map<String, Object> map : lista) {	
			
			item = new RequisitoDocumento();
			
			if(map.get("CODIGO")!=null) item.setVcodigo((String)map.get("CODIGO"));
			if(map.get("DOCUMENTO")!=null) item.setVdocumento((String)map.get("DOCUMENTO"));
			if(map.get("N_IDDOCU")!=null) item.setNiddocu(((BigDecimal)map.get("N_IDDOCU")).longValue());
     		if(map.get("N_IDREQDOC")!=null) item.setNidreqdoc(((BigDecimal)map.get("N_IDREQDOC")).longValue());
     		if(map.get("N_IDNORMA")!=null) item.setNidnorma(((BigDecimal)map.get("N_IDNORMA")).longValue());
     		if(map.get("N_IDNORREQ")!=null) item.setNidnorreq(((BigDecimal)map.get("N_IDNORREQ")).longValue());
     		if(map.get("N_IDREQUISITO")!=null) item.setNidrequisito(((BigDecimal)map.get("N_IDREQUISITO")).longValue());
			if(map.get("V_ESTREQDOC")!=null) item.setVestreqdoc((String)map.get("V_ESTREQDOC"));
			
			listaRequisitos.add(item);
			
		}
		return listaRequisitos;
	}
	
	@Override
	public Error getError() {
		return this.error;
	}

	@Override
	public Long obtenerNorma(Long idRequisito) {		
		Long idNorma = null;
		Map<String, Object> out = null;
		//List<Requisito> listaRequisito = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_AUDITORIA")
				.withProcedureName("PRC_OBTENER_ID_NORMA")
				.declareParameters(
						new SqlParameter("i_idrequisito", OracleTypes.NUMBER),										
						new SqlOutParameter("o_idnorma", OracleTypes.NUMBER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));		
		SqlParameterSource in = new MapSqlParameterSource()
		.addValue("i_idrequisito",idRequisito );
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)  out.get("o_retorno");			
			if(resultado == 0) {
				if(out.get("o_idnorma") != null) {
					idNorma = ((BigDecimal) out.get("o_idnorma")).longValue();
				}
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
	  return idNorma;
	}*/
}
