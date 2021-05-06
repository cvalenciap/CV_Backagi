package pe.com.sedapal.agi.dao.impl;

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

import org.apache.log4j.Logger;

import pe.com.sedapal.agi.dao.INormaDAO;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.DBConstants;
import pe.com.sedapal.agi.util.ValidarCampos;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;

@Repository
public class NormaDAOImpl implements INormaDAO {
	@Autowired
	private JdbcTemplate jdbc;
	// private SimpleJdbcCall jdbcCall;
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
	public List<Norma> obtenerListaNormas(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		Map<String, Object> out = null;
		List<Norma> listaNormas = new ArrayList<>();
		this.error = null;
		this.paginacion = new Paginacion();
		this.paginacion.setPagina(pageRequest.getPagina());
		this.paginacion.setRegistros(pageRequest.getRegistros());

		try {

			SimpleJdbcCall jdbcCallAux = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT)
					.withProcedureName(DBConstants.PRC_MANT_LISTAR_NORMAS_AUDI)
					.declareParameters(new SqlOutParameter("O_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
							new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

			out = jdbcCallAux.execute();
			Integer resultado = (Integer) out.get("O_RETORNO");
			if (resultado == DBConstants.OK) {
				listaNormas = this.mapearNorma(out);
			} else {
				String mensaje = (String) out.get("O_MENSAJE");
				String mensajeInterno = (String) out.get("O_SQLERRM");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en NormaDAOImpl.obtenerListaNormas";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(e);
		}

		return listaNormas;
	}

	@SuppressWarnings("unchecked")
	private List<Norma> mapearNorma(Map<String, Object> resultados) {
		List<Norma> listaNormas = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("O_CURSOR");
		Norma norma = null;
		DatosAuditoria datosAuditoria = null;

		for (Map<String, Object> map : lista) {
			norma = new Norma();
			norma.setN_id_normas(ValidarCampos.castValorBigDecimalToInteger((BigDecimal) map.get("N_ID_NORMAS")));
			norma.setV_tipo(ValidarCampos.validaCamposString((String) map.get("V_TIPO")));
			norma.setV_nom_norma(ValidarCampos.validaCamposString((String) map.get("V_NOM_NORMA")));
			norma.setV_st_reg(ValidarCampos.validaCamposString((String) map.get("V_ST_REG")));
			datosAuditoria = new DatosAuditoria();
			datosAuditoria.setUsuarioCreacion(ValidarCampos.validaCamposString((String) map.get("A_V_ID_USUA_CREA")));
			datosAuditoria.setFechaCreacion(ValidarCampos.validaCamposDate((String) map.get("A_D_FE_USUA_CREA")));
			datosAuditoria
					.setUsuarioModificacion(ValidarCampos.validaCamposString((String) map.get("A_V_ID_USUA_MODI")));
			datosAuditoria.setFechaModificacion(ValidarCampos.validaCamposDate((String) map.get("A_D_FE_USUA_MODI")));
			norma.setDatosAuditoria(datosAuditoria);
			listaNormas.add(norma);
		}

		if (listaNormas.size() > 0) {
			this.paginacion.setTotalRegistros(((BigDecimal) lista.get(0).get("RESULT_COUNT")).intValue());
		}
		return listaNormas;

	}

	@Override
	public Integer actualizarNorma(Norma norma) {
		// TODO Auto-generated method stub
		Map<String, Object> out = null;
		this.error = null;

		Integer resultado = null;

		try {

			SimpleJdbcCall jdbcCallAux = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT)
					.withProcedureName(DBConstants.PRC_MANT_UPDATE_NORMA_AUDI)
					.declareParameters(new SqlParameter("I_TIPO_NORMA", OracleTypes.VARCHAR),
							new SqlParameter("I_NOMB_NORMA", OracleTypes.VARCHAR),
							new SqlParameter("I_ESTA_REGIS", OracleTypes.VARCHAR),
							new SqlParameter("I_USUA_MODIF", OracleTypes.VARCHAR),
							new SqlParameter("I_CODI_NORMA", OracleTypes.VARCHAR),
							new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
							new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("I_TIPO_NORMA", norma.getV_tipo())
					.addValue("I_NOMB_NORMA", norma.getV_nom_norma()).addValue("I_ESTA_REGIS", norma.getV_st_reg())
					.addValue("I_USUA_MODIF", norma.getDatosAuditoria().getUsuarioModificacion())
					.addValue("I_CODI_NORMA", norma.getN_id_normas());

			out = jdbcCallAux.execute(in);
			resultado = (Integer) out.get("O_RETORNO");

			LOGGER.info("Resultado del registro: " + resultado);

			if (resultado != DBConstants.OK) {
				String mensaje = (String) out.get("O_MENSAJE");
				String mensajeInterno = (String) out.get("O_SQLERRM");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			resultado = 1;
			String mensaje = "Error en NormaDAOImpl.actualizarNorma";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(e);
		}

		return resultado;
	}

	// la lista de la Grilla

	/*
	 * @Override public List<Norma> obtenerListaNormasGrilla(NormaRequest
	 * normaRequest, PageRequest pageRequest) { // TODO Auto-generated method stub
	 * Map<String, Object> out = null; List<Norma> listaNormas = new ArrayList<>();
	 * this.paginacion = new Paginacion(); this.error = null;
	 * paginacion.setPagina(pageRequest.getPagina());
	 * paginacion.setRegistros(pageRequest.getRegistros());
	 * 
	 * try { SimpleJdbcCall jdbcCallAux = null; jdbcCallAux = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_NORMAS_REQ_OBTENER")
	 * .declareParameters(new SqlParameter("n_tipo", OracleTypes.VARCHAR), new
	 * SqlParameter("n_idnorma2", OracleTypes.NUMBER), new SqlParameter("i_npagina",
	 * OracleTypes.NUMBER), new SqlParameter("i_nregistros", OracleTypes.NUMBER),
	 * new SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
	 * 
	 * System.out.println("jdbcCallAux(2)");
	 * System.out.println(jdbcCallAux.getProcedureName());
	 * 
	 * SqlParameterSource in = new MapSqlParameterSource().addValue("n_tipo",
	 * normaRequest.getTipo()) .addValue("n_idnorma2",
	 * normaRequest.getIdNorma()).addValue("i_npagina", pageRequest.getPagina())
	 * .addValue("i_nregistros", pageRequest.getRegistros());
	 * 
	 * System.out.println("IN: obtenerListaNormasGrilla");
	 * System.out.println("n_tipo[2]" + in.getValue("n_tipo"));
	 * System.out.println("n_idnorma2[2]" + in.getValue("n_idnorma2"));
	 * System.out.println("i_npagina[2]" + in.getValue("i_npagina"));
	 * System.out.println("i_nregistros[2]" + in.getValue("i_nregistros")); //
	 * System.out.println("i_ntipo[2]"+in.getValue("i_ntipo"));
	 * 
	 * out = jdbcCallAux.execute(in); Integer resultado = ((BigDecimal)
	 * out.get("o_retorno")).intValue(); if (resultado == 0) { listaNormas =
	 * this.mapearNormaGrilla(out); } else { String mensaje = (String)
	 * out.get("o_mensaje"); String mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.obtenerListaNormasGrilla"; String mensajeInterno =
	 * e.getMessage(); this.error = new Error(resultado, mensaje, mensajeInterno);
	 * String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); }
	 * 
	 * return listaNormas; }
	 */

	/*
	 * private List<Norma> mapearNormaGrilla(Map<String, Object> resultados) {
	 * List<Norma> listaNormas = new ArrayList<>(); List<Map<String, Object>> lista
	 * = (List<Map<String, Object>>) resultados.get("o_cursor"); Norma item = null;
	 * int size = lista.size();
	 * 
	 * for (Map<String, Object> map : lista) { item = new Norma();
	 * 
	 * if (map.get("NIDNORMA") != null) { item.setIdNorma(((BigDecimal)
	 * map.get("NIDNORMA")).longValue()); } if (map.get("VDESNORM") != null) {
	 * item.setDescripcionNorma((String) map.get("VDESNORM")); }
	 * 
	 * if (map.get("VTIPNORM") != null) { item.setTipo((String)
	 * map.get("VTIPNORM")); }
	 * 
	 * if (map.get("VORDREQ") != null) { Requisito requisito = new Requisito();
	 * requisito.setOrden((String) map.get("VORDREQ"));
	 * requisito.setDescripcionReq((String) map.get("VDESREQ"));
	 * 
	 * item.setRequisito(requisito); }
	 */

//			if(map.get("AVUSUMOD")!=null) {
//				item.getDatosAuditoria().setUsuarioModificacion((String) map.get("AVUSUMOD"));
//				item.getDatosAuditoria().setUsuarioCreacion((String) map.get("AVUSUCRE"));		
//			}

//			if(map.get("NIDREQRELA")!=null) {				
//				RequisitoRelacionado requisitoRel = new RequisitoRelacionado();
//				requisitoRel.setIdReqRela(((BigDecimal) map.get("NIDREQRELA")).longValue());
//				requisitoRel.setNordenReRel(((String) map.get("ORDENREQREL")));
//				requisitoRel.setDescripcionReqRel((String) map.get("DESREQREL"));
//				
//				
//				item.setRequisitoRelacionado(requisitoRel);
//			}

	/*
	 * listaNormas.add(item); if (map.get("RESULT_COUNT") != null) {
	 * this.paginacion.setTotalRegistros(((BigDecimal)
	 * map.get("RESULT_COUNT")).intValue()); }
	 */

	/*
	 * if (this.paginacion != null && size>1) {
	 * this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).
	 * intValue()); }
	 */

	/*
	 * } return listaNormas; }
	 */

	/*
	 * public Paginacion getPaginacion() { return this.paginacion; }
	 * 
	 */

	/*
	 * @Override public List<Requisito> obtenerNormaRequisito(NormaRequest
	 * normaRequest) { // TODO Auto-generated method stub Map<String, Object> out =
	 * null; List<Requisito> listaRequisitos = new ArrayList<>(); this.error = null;
	 * 
	 * this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_NORMA_REQUI_OBTENER")
	 * .declareParameters(new SqlParameter("i_nidnorma", OracleTypes.NUMBER), new
	 * SqlParameter("i_vtiponorma", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
	 * 
	 * SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidnorma",
	 * normaRequest.getIdNorma()) .addValue("i_vtiponorma", normaRequest.getTipo());
	 * 
	 * try { out = this.jdbcCall.execute(in); Integer resultado = ((BigDecimal)
	 * out.get("o_retorno")).intValue(); if (resultado == 0) { listaRequisitos =
	 * this.mapearNormaReq(out); } else { String mensaje = (String)
	 * out.get("o_mensaje"); String mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.obtenerNormaRequisito"; String mensajeInterno =
	 * e.getMessage(); this.error = new Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * return listaRequisitos; }
	 */
	/*
	 * private List<Requisito> mapearNormaReq(Map<String, Object> resultados) {
	 * List<Requisito> listaRequisito = new ArrayList<>(); List<Map<String, Object>>
	 * lista = (List<Map<String, Object>>) resultados.get("o_cursor"); Requisito
	 * item = null; int size = lista.size();
	 * 
	 * for (Map<String, Object> map : lista) { item = new Requisito();
	 * 
	 * if (map.get("IDREQUISITO") != null) { item.setIdRequisito(((BigDecimal)
	 * map.get("IDREQUISITO")).longValue()); } if (map.get("ORDEN") != null) {
	 * item.setOrden((String) map.get("ORDEN")); } if (map.get("REQUISITO") != null)
	 * { item.setDescripcionReq(item.getOrden() + " - " + (String)
	 * map.get("REQUISITO")); }
	 * 
	 * listaRequisito.add(item);
	 * 
	 * } return listaRequisito; }
	 */

	/*
	 * @Override
	 * 
	 * @Transactional public void guardarNormaRequisito(Norma norma) { try { Norma
	 * datosNorma = registrarNorma(norma); if (datosNorma != null) { for (NormaReq
	 * normaReq : datosNorma.getNormaReq()) {
	 * normaReq.getDatosAuditoria().setUsuarioCreacion(norma.getDatosAuditoria().
	 * getUsuarioCreacion()); normaReq.setIdRequisitoPadre(null); NormaReq datosReq
	 * = registrarReq(normaReq);
	 * 
	 * if (datosReq != null) { this.registarNormaReq(datosNorma.getIdNorma(),
	 * datosReq); this.registarReqHijos(datosNorma.getIdNorma(), datosReq); } } } }
	 * catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.guardarNormaRequisito"; String mensajeInterno =
	 * e.getMessage(); String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); } }
	 * 
	 * @Override public void registarNormaReq(Long idNorma, NormaReq normaReq) {
	 * long idNormaReq = registrarNormaReq(idNorma, normaReq); if
	 * (normaReq.getRequisitoRelacionado() != null) { for (RequisitoRelacionado
	 * requisitoRelacionado : normaReq.getRequisitoRelacionado()) {
	 * requisitoRelacionado.getDatosAuditoria()
	 * .setUsuarioCreacion(normaReq.getDatosAuditoria().getUsuarioCreacion());
	 * requisitoRelacionado.setIdNorReq(idNormaReq);
	 * registrarReqRelacionado(requisitoRelacionado); } }
	 * 
	 * if (normaReq.getRequisitoDocumento() != null) { for (RequisitoDocumento
	 * requisitoDocumento : normaReq.getRequisitoDocumento()) {
	 * requisitoDocumento.getDatosAuditoria()
	 * .setUsuarioCreacion(normaReq.getDatosAuditoria().getUsuarioCreacion());
	 * requisitoDocumento.setNidnorreq(idNormaReq);
	 * requisitoDocumento.setNidnorma(idNorma);
	 * requisitoDocumento.setNidrequisito(normaReq.getIdRequisito());
	 * registrarReqDocumento(requisitoDocumento); } }
	 * 
	 * }
	 * 
	 * public void registarReqHijos(Long idNorma, NormaReq normaReq) { if
	 * (normaReq.getNormaReq() != null) { for (NormaReq normaReqHijo :
	 * normaReq.getNormaReq()) {
	 * normaReqHijo.setIdRequisitoPadre(normaReq.getIdRequisito()); NormaReq
	 * datosReqHijos = registrarReq(normaReqHijo);
	 * 
	 * if (datosReqHijos != null) { this.registarNormaReq(idNorma, datosReqHijos);
	 * if (datosReqHijos.getNormaReq() != null) { this.registarReqHijos(idNorma,
	 * datosReqHijos); } }
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * public Norma registrarNorma(Norma norma) { Map<String, Object> out = null;
	 * Long idNorma = null; Norma datosNorma = null; this.error = null;
	 * 
	 * try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_NORMA_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidnorma", OracleTypes.NUMBER), new
	 * SqlParameter("i_vestnorm", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vtiponorma", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vdescrip", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_idNorma", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidnorma",
	 * null).addValue("i_vestnorm", "1") .addValue("i_vusuario",
	 * norma.getDatosAuditoria().getUsuarioCreacion()) .addValue("i_vtiponorma",
	 * norma.getTipo()).addValue("i_vdescrip", norma.getDescripcionNorma());
	 * 
	 * out = this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { idNorma = ((BigDecimal)
	 * out.get("o_idNorma")).longValue(); datosNorma = norma;
	 * datosNorma.setIdNorma(idNorma); } else { String mensaje = (String)
	 * out.get("o_mensaje"); String mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.registrarNormaReq"; String mensajeInterno =
	 * e.getMessage(); String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); }
	 * 
	 * return datosNorma; }
	 */

	/*
	 * public NormaReq registrarReq(NormaReq normaReq) { Map<String, Object> out =
	 * null; Long idRequisito = null; NormaReq datosReq = null; this.error = null;
	 * if (normaReq.getEstado() != "3") { try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_REQUISITO_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidrequisito", OracleTypes.NUMBER),
	 * new SqlParameter("i_vestreq", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vorden", OracleTypes.VARCHAR), new SqlParameter("i_vnivel",
	 * OracleTypes.VARCHAR), new SqlParameter("i_vdescrip", OracleTypes.VARCHAR),
	 * new SqlParameter("i_vdetalle", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlParameter("i_idreqpadre", OracleTypes.NUMBER), new
	 * SqlParameter("i_v_detreq", OracleTypes.VARCHAR), new
	 * SqlParameter("i_v_cuesti", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_idrequisito", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidrequisito", null)
	 * .addValue("i_vestreq", "1").addValue("i_vorden", normaReq.getOrden())
	 * .addValue("i_vnivel", normaReq.getNivel()).addValue("i_vdescrip",
	 * normaReq.getDescripcionReq()) .addValue("i_vdetalle",
	 * normaReq.getDescripcionReq()) .addValue("i_vusuario",
	 * normaReq.getDatosAuditoria().getUsuarioCreacion()) .addValue("i_idreqpadre",
	 * normaReq.getIdRequisitoPadre()) .addValue("i_v_detreq",
	 * normaReq.getVdetreq()).addValue("i_v_cuesti", normaReq.getVcuesti());
	 * 
	 * out = this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { idRequisito = ((BigDecimal)
	 * out.get("o_idrequisito")).longValue(); datosReq = normaReq;
	 * datosReq.setIdRequisito(idRequisito);
	 * 
	 * } else { String mensaje = (String) out.get("o_mensaje"); String
	 * mensajeInterno = (String) out.get("o_sqlerrm"); this.error = new
	 * Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.registrarReq"; String mensajeInterno = e.getMessage();
	 * String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); } } return datosReq;
	 * 
	 * }
	 */

	/*
	 * public long registrarNormaReq(Long idNorma, NormaReq normaReq) { Map<String,
	 * Object> out = null; Long idNormaReq = null; NormaReq datosReq = null;
	 * this.error = null;
	 * 
	 * try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_NORMREQU_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidnorreq", OracleTypes.NUMBER), new
	 * SqlParameter("i_vestnorreq", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new SqlParameter("i_nnorma",
	 * OracleTypes.NUMBER), new SqlParameter("i_nrequisito", OracleTypes.NUMBER),
	 * new SqlOutParameter("o_idnorreq", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidnorreq", null)
	 * .addValue("i_vestnorreq", "1").addValue("i_nnorma", idNorma)
	 * .addValue("i_vusuario", normaReq.getDatosAuditoria().getUsuarioCreacion())
	 * .addValue("i_nrequisito", normaReq.getIdRequisito());
	 * 
	 * out = this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { idNormaReq = ((BigDecimal)
	 * out.get("o_idnorreq")).longValue();
	 * 
	 * } else { String mensaje = (String) out.get("o_mensaje"); String
	 * mensajeInterno = (String) out.get("o_sqlerrm"); this.error = new
	 * Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.registrarNormaReq"; String mensajeInterno =
	 * e.getMessage(); String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); }
	 * 
	 * return idNormaReq; }
	 */

	/*
	 * @Override public void registrarReqRelacionado(RequisitoRelacionado
	 * requisitoRelacionado) { String mensajeInterno = null; String mensaje = null;
	 * Map<String, Object> out = null; try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_REQRELA_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidreqrela", OracleTypes.NUMBER), new
	 * SqlParameter("i_norreq", OracleTypes.NUMBER), new SqlParameter("i_norma",
	 * OracleTypes.NUMBER), new SqlParameter("i_nrequisito", OracleTypes.NUMBER),
	 * new SqlParameter("i_vestreqrel", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidreqrela", null)
	 * .addValue("i_norreq", requisitoRelacionado.getIdNorReq())
	 * .addValue("i_norma", requisitoRelacionado.getIdNorma())
	 * .addValue("i_nrequisito",
	 * requisitoRelacionado.getIdRequisito()).addValue("i_vestreqrel", "1")
	 * .addValue("i_vusuario", "AGI"); out = this.jdbcCall.execute(in); Integer
	 * resultado = (Integer) out.get("o_retorno");
	 * 
	 * if (resultado == 0) { List<Map<String, Object>> listado = (List<Map<String,
	 * Object>>) out.get("o_cursor"); mensaje = (String) out.get("o_mensaje");
	 * mensajeInterno = (String) out.get("o_sqlerrm"); } else { mensaje = (String)
	 * out.get("o_mensaje"); mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; mensaje =
	 * "Error en NormaDAOImpl.registrarReqRelacionado"; mensajeInterno =
	 * e.getMessage(); this.error = new Error(resultado, mensaje, mensajeInterno);
	 * System.out.println("Error al guardar requisito relacionado" +
	 * this.error.getMensaje()); }
	 * 
	 * }
	 * 
	 * @Override public void registrarReqDocumento(RequisitoDocumento
	 * requisitoDocumento) { String mensajeInterno = null; String mensaje = null;
	 * Map<String, Object> out = null; try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_REQDOC_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidreqdoc", OracleTypes.NUMBER), new
	 * SqlParameter("i_niddocu", OracleTypes.NUMBER), new SqlParameter("i_nidnorma",
	 * OracleTypes.NUMBER), new SqlParameter("i_nidnorreq", OracleTypes.NUMBER), new
	 * SqlParameter("i_nidrequisito", OracleTypes.NUMBER), new
	 * SqlParameter("i_vestreqdoc", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidreqdoc", null)
	 * .addValue("i_niddocu", requisitoDocumento.getNiddocu())
	 * .addValue("i_nidnorma", requisitoDocumento.getNidnorma())
	 * .addValue("i_nidnorreq", requisitoDocumento.getNidnorreq())
	 * .addValue("i_nidrequisito",
	 * requisitoDocumento.getNidrequisito()).addValue("i_vestreqdoc", "1")
	 * .addValue("i_vusuario", "AGI"); out = this.jdbcCall.execute(in); Integer
	 * resultado = (Integer) out.get("o_retorno");
	 * 
	 * if (resultado == 0) { List<Map<String, Object>> listado = (List<Map<String,
	 * Object>>) out.get("o_cursor"); mensaje = (String) out.get("o_mensaje");
	 * mensajeInterno = (String) out.get("o_sqlerrm"); } else { mensaje = (String)
	 * out.get("o_mensaje"); mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno);
	 * 
	 * }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; mensaje =
	 * "Error en NormaDAOImpl.registrarReqDocumento"; mensajeInterno =
	 * e.getMessage(); this.error = new Error(resultado, mensaje, mensajeInterno);
	 * System.out.println("Error al guardar documento relacionado" +
	 * this.error.getMensaje()); }
	 * 
	 * }
	 */

	/*
	 * @Override public Norma obtenerDatosNormaId(Long idNorma) { Map<String,
	 * Object> out = null; List<Norma> listaNormas = new ArrayList<>(); Norma norma
	 * = null;
	 * 
	 * try {
	 * 
	 * SimpleJdbcCall jdbcCallAux = null; jdbcCallAux = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_OBTENER_NORMA_ID")
	 * .declareParameters(new SqlParameter("i_idNorma", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
	 * 
	 * SqlParameterSource in = new MapSqlParameterSource().addValue("i_idNorma",
	 * idNorma);
	 * 
	 * out = jdbcCallAux.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { listaNormas =
	 * this.mapearNorma(out); if (listaNormas.size() > 0) { norma =
	 * listaNormas.get(0); } } else { String mensaje = (String)
	 * out.get("o_mensaje"); String mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno);
	 * 
	 * }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.obtenerDatosNormaId"; String mensajeInterno =
	 * e.getMessage(); this.error = new Error(resultado, mensaje, mensajeInterno);
	 * String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); }
	 * 
	 * return norma; }
	 */

	/*
	 * @Override
	 * 
	 * @Transactional public void actualizarNormaRequisito(Long idNorma, Norma
	 * norma) { try { Norma datosNorma = actualizarNorma(idNorma, norma); if
	 * (datosNorma != null) { for (NormaReq normaReq : datosNorma.getNormaReq()) {
	 * // normaReq.setIdRequisitoPadre(null); NormaReq datosReq =
	 * actualizarReq(normaReq.getIdRequisito(), normaReq);
	 * 
	 * if (datosReq != null) { this.actualizaNormaReq(datosReq.getIdNorReq(),
	 * datosNorma.getIdNorma(), datosReq);
	 * this.actualizarReqHijos(datosNorma.getIdNorma(), datosReq); } } } } catch
	 * (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.guardarNormaRequisito"; String mensajeInterno =
	 * e.getMessage(); String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); } }
	 * 
	 * @Override public Norma actualizarNorma(Long id, Norma norma) {
	 * 
	 * Map<String, Object> out = null; Long idNorma = null; Norma datosNorma = null;
	 * this.error = null;
	 * 
	 * try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_NORMA_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidnorma", OracleTypes.NUMBER), new
	 * SqlParameter("i_vestnorm", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vtiponorma", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vdescrip", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_idNorma", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidnorma", id)
	 * .addValue("i_vestnorm", norma.getEstado()).addValue("i_vusuario", "AGI")
	 * .addValue("i_vtiponorma", norma.getTipo()).addValue("i_vdescrip",
	 * norma.getDescripcionNorma());
	 * 
	 * out = this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { idNorma = ((BigDecimal)
	 * out.get("o_idNorma")).longValue(); datosNorma = norma;
	 * datosNorma.setIdNorma(idNorma); } else { String mensaje = (String)
	 * out.get("o_mensaje"); String mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.registrarNormaReq"; String mensajeInterno =
	 * e.getMessage(); String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); }
	 * 
	 * return datosNorma;
	 * 
	 * }
	 * 
	 * @Override public NormaReq actualizarReq(Long id, NormaReq normaReq) {
	 * 
	 * Map<String, Object> out = null; Long idRequisito = null; NormaReq datosReq =
	 * null; this.error = null; if (normaReq.getEstado() != "3") { try {
	 * this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_REQUISITO_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidrequisito", OracleTypes.NUMBER),
	 * new SqlParameter("i_vestreq", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vorden", OracleTypes.VARCHAR), new SqlParameter("i_vnivel",
	 * OracleTypes.VARCHAR), new SqlParameter("i_vdescrip", OracleTypes.VARCHAR),
	 * new SqlParameter("i_vdetalle", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlParameter("i_idreqpadre", OracleTypes.NUMBER), new
	 * SqlParameter("i_v_detreq", OracleTypes.VARCHAR), new
	 * SqlParameter("i_v_cuesti", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_idrequisito", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidrequisito", id)
	 * .addValue("i_vestreq", normaReq.getEstado()).addValue("i_vorden",
	 * normaReq.getOrden()) .addValue("i_vnivel",
	 * normaReq.getNivel()).addValue("i_vdescrip", normaReq.getDescripcionReq())
	 * .addValue("i_vdetalle", normaReq.getDescripcionReq()).addValue("i_vusuario",
	 * "AGI") .addValue("i_idreqpadre", normaReq.getIdRequisitoPadre())
	 * .addValue("i_v_detreq", normaReq.getVdetreq()).addValue("i_v_cuesti",
	 * normaReq.getVcuesti());
	 * 
	 * out = this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { idRequisito = ((BigDecimal)
	 * out.get("o_idrequisito")).longValue(); datosReq = normaReq;
	 * datosReq.setIdRequisito(idRequisito);
	 * 
	 * } else { String mensaje = (String) out.get("o_mensaje"); String
	 * mensajeInterno = (String) out.get("o_sqlerrm"); this.error = new
	 * Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.registrarReq"; String mensajeInterno = e.getMessage();
	 * String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); } } return datosReq;
	 * 
	 * }
	 */

	/*
	 * @Override public void actualizaNormaReq(Long id, Long idNorma, NormaReq
	 * normaReq) { long idNormaReq = actualizarNormaReq(id, idNorma, normaReq); if
	 * (normaReq.getRequisitoRelacionado() != null) { for (RequisitoRelacionado
	 * requisitoRelacionado : normaReq.getRequisitoRelacionado()) { if
	 * (requisitoRelacionado.getIdNorReq() == null) {
	 * requisitoRelacionado.setIdNorReq(idNormaReq); }
	 * actualizarReqRelacionado(requisitoRelacionado.getIdReqRela(),
	 * requisitoRelacionado); } }
	 * 
	 * if (normaReq.getRequisitoDocumento() != null) { for (RequisitoDocumento
	 * requisitoDocumento : normaReq.getRequisitoDocumento()) {
	 * 
	 * if (requisitoDocumento.getNidnorreq() == null) {
	 * requisitoDocumento.setNidnorreq(idNormaReq); }
	 * actualizarReqDocumento(requisitoDocumento.getNidreqdoc(),
	 * requisitoDocumento);
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * @Override public long actualizarNormaReq(Long id, Long idNorma, NormaReq
	 * normaReq) {
	 * 
	 * Map<String, Object> out = null; Long idNormaReq = null; NormaReq datosReq =
	 * null; this.error = null;
	 * 
	 * try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_NORMREQU_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidnorreq", OracleTypes.NUMBER), new
	 * SqlParameter("i_vestnorreq", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new SqlParameter("i_nnorma",
	 * OracleTypes.NUMBER), new SqlParameter("i_nrequisito", OracleTypes.NUMBER),
	 * new SqlOutParameter("o_idnorreq", OracleTypes.NUMBER), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidnorreq", id)
	 * .addValue("i_vestnorreq", normaReq.getEstado()).addValue("i_nnorma", idNorma)
	 * .addValue("i_vusuario", "AGI").addValue("i_nrequisito",
	 * normaReq.getIdRequisito());
	 * 
	 * out = this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { idNormaReq = ((BigDecimal)
	 * out.get("o_idnorreq")).longValue();
	 * 
	 * } else { String mensaje = (String) out.get("o_mensaje"); String
	 * mensajeInterno = (String) out.get("o_sqlerrm"); this.error = new
	 * Error(resultado, mensaje, mensajeInterno); }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; String mensaje =
	 * "Error en NormaDAOImpl.registrarNormaReq"; String mensajeInterno =
	 * e.getMessage(); String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
	 * LOGGER.error(error[1], e); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); }
	 * 
	 * return idNormaReq;
	 * 
	 * }
	 */

	/*
	 * @Override public void actualizarReqRelacionado(Long id, RequisitoRelacionado
	 * requisitoRelacionado) {
	 * 
	 * String mensajeInterno = null; String mensaje = null; Map<String, Object> out
	 * = null; try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_REQRELA_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidreqrela", OracleTypes.NUMBER), new
	 * SqlParameter("i_norreq", OracleTypes.NUMBER), new SqlParameter("i_norma",
	 * OracleTypes.NUMBER), new SqlParameter("i_nrequisito", OracleTypes.NUMBER),
	 * new SqlParameter("i_vestreqrel", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidreqrela", id)
	 * .addValue("i_norreq", requisitoRelacionado.getIdNorReq())
	 * .addValue("i_norma", requisitoRelacionado.getIdNorma())
	 * .addValue("i_nrequisito", requisitoRelacionado.getIdRequisito())
	 * .addValue("i_vestreqrel",
	 * requisitoRelacionado.getVestreqrel()).addValue("i_vusuario", "AGI"); out =
	 * this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno");
	 * 
	 * if (resultado == 0) { List<Map<String, Object>> listado = (List<Map<String,
	 * Object>>) out.get("o_cursor"); mensaje = (String) out.get("o_mensaje");
	 * mensajeInterno = (String) out.get("o_sqlerrm"); } else { mensaje = (String)
	 * out.get("o_mensaje"); mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno); } } catch
	 * (Exception e) { Integer resultado = 1; mensaje =
	 * "Error en EncuestaDAOImpl.registrarReqRelacionado1"; mensajeInterno =
	 * e.getMessage(); this.error = new Error(resultado, mensaje, mensajeInterno);
	 * System.out.println("Error al guardar la Encuesta" + this.error.getMensaje());
	 * }
	 * 
	 * }
	 * 
	 * @Override public void actualizarReqDocumento(Long id, RequisitoDocumento
	 * requisitoDocumento) {
	 * 
	 * String mensajeInterno = null; String mensaje = null; Map<String, Object> out
	 * = null; try { this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_REQDOC_GUARDAR")
	 * .declareParameters(new SqlParameter("i_nidreqdoc", OracleTypes.NUMBER), new
	 * SqlParameter("i_niddocu", OracleTypes.NUMBER), new SqlParameter("i_nidnorma",
	 * OracleTypes.NUMBER), new SqlParameter("i_nidnorreq", OracleTypes.NUMBER), new
	 * SqlParameter("i_nidrequisito", OracleTypes.NUMBER), new
	 * SqlParameter("i_vestreqdoc", OracleTypes.VARCHAR), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_cursor", OracleTypes.CURSOR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidreqdoc", id)
	 * .addValue("i_niddocu", requisitoDocumento.getNiddocu())
	 * .addValue("i_nidnorma", requisitoDocumento.getNidnorma())
	 * .addValue("i_nidnorreq", requisitoDocumento.getNidnorreq())
	 * .addValue("i_nidrequisito", requisitoDocumento.getNidrequisito())
	 * .addValue("i_vestreqdoc",
	 * requisitoDocumento.getVestreqdoc()).addValue("i_vusuario", "AGI"); out =
	 * this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno");
	 * 
	 * if (resultado == 0) { List<Map<String, Object>> listado = (List<Map<String,
	 * Object>>) out.get("o_cursor"); mensaje = (String) out.get("o_mensaje");
	 * mensajeInterno = (String) out.get("o_sqlerrm"); } else { mensaje = (String)
	 * out.get("o_mensaje"); mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno);
	 * 
	 * }
	 * 
	 * } catch (Exception e) { Integer resultado = 1; mensaje =
	 * "Error en EncuestaDAOImpl.registrarReqRelacionado1"; mensajeInterno =
	 * e.getMessage(); this.error = new Error(resultado, mensaje, mensajeInterno);
	 * System.out.println("Error al guardar la Encuesta" + this.error.getMensaje());
	 * }
	 * 
	 * }
	 * 
	 * @Override public void actualizarReqHijos(Long idNorma, NormaReq normaReq) {
	 * 
	 * if (normaReq.getNormaReq() != null) { for (NormaReq normaReqHijo :
	 * normaReq.getNormaReq()) {
	 * 
	 * if (normaReqHijo.getIdRequisitoPadre() == null) {
	 * normaReqHijo.setIdRequisitoPadre(normaReq.getIdRequisito()); }
	 * 
	 * NormaReq datosReqHijos = actualizarReq(normaReqHijo.getIdRequisito(),
	 * normaReqHijo);
	 * 
	 * if (datosReqHijos != null) {
	 * this.actualizaNormaReq(datosReqHijos.getIdNorReq(), idNorma, datosReqHijos);
	 * if (datosReqHijos.getNormaReq() != null) { this.actualizarReqHijos(idNorma,
	 * datosReqHijos); } }
	 * 
	 * } }
	 * 
	 * }
	 */

	/*
	 * @Override public Boolean eliminarNorma(Long id) { List<Norma> lista = new
	 * ArrayList<>(); Map<String, Object> out = null; if (id == null) { id = 0L; }
	 * this.jdbcCall = new
	 * SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName(
	 * "PCK_AGI_AUDITORIA") .withProcedureName("PRC_NORMA_ELIMINAR")
	 * .declareParameters(new SqlParameter("i_nidnorma", OracleTypes.NUMBER), new
	 * SqlParameter("i_vusuario", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_retorno", OracleTypes.INTEGER), new
	 * SqlOutParameter("o_mensaje", OracleTypes.VARCHAR), new
	 * SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR)); SqlParameterSource in =
	 * new MapSqlParameterSource().addValue("i_nidnorma", id).addValue("i_vusuario",
	 * "AGI"); out = this.jdbcCall.execute(in); Boolean retorno = true; try { out =
	 * this.jdbcCall.execute(in); Integer resultado = (Integer)
	 * out.get("o_retorno"); if (resultado == 0) { retorno = true; } else { String
	 * mensaje = (String) out.get("o_mensaje"); String mensajeInterno = (String)
	 * out.get("o_sqlerrm"); this.error = new Error(resultado, mensaje,
	 * mensajeInterno); retorno = false; } } catch (Exception e) { Integer resultado
	 * = (Integer) out.get("o_retorno"); String mensaje = (String)
	 * out.get("o_mensaje"); String mensajeInterno = (String) out.get("o_sqlerrm");
	 * this.error = new Error(resultado, mensaje, mensajeInterno); retorno = false;
	 * } return retorno; }
	 */

}
