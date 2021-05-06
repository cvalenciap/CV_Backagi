package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.ITipoNormasDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.DBConstants;
import pe.com.sedapal.agi.util.ValidarCampos;

@Repository
public class TipoNormasDAOImpl implements ITipoNormasDAO {
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
	public List<Constante> listaTipoNormas(PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Constante> listaConstantes = new ArrayList<>();
		this.error = null;
		this.paginacion = new Paginacion();
		this.paginacion.setPagina(pageRequest.getPagina());
		this.paginacion.setRegistros(pageRequest.getRegistros());

		try {

			SimpleJdbcCall jdbcCallAux = new SimpleJdbcCall(this.jdbc).withSchemaName(DBConstants.DBSCHEMA)
					.withCatalogName(DBConstants.PCK_AUDITORIA_MANT)
					.withProcedureName(DBConstants.PRC_MANT_LISTAR_TIPOS_NORMAS_AUDI)
					.declareParameters(new SqlOutParameter("O_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("O_RETORNO", OracleTypes.INTEGER),
							new SqlOutParameter("O_MENSAJE", OracleTypes.VARCHAR),
							new SqlOutParameter("O_SQLERRM", OracleTypes.VARCHAR));

			out = jdbcCallAux.execute();
			Integer resultado = (Integer) out.get("O_RETORNO");
			if (resultado == 0) {
				listaConstantes = this.mapearTiposNormas(out);
			} else {
				String mensaje = (String) out.get("O_MENSAJE");
				String mensajeInterno = (String) out.get("O_SQLERRM");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en TipoNormasDAOImpl.listaTipoNormas";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			LOGGER.error(e);
		}

		return listaConstantes;
	}

	@SuppressWarnings("unchecked")
	private List<Constante> mapearTiposNormas(Map<String, Object> resultados) {
		List<Constante> listaConstante = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("O_CURSOR");
		Constante constante = null;

		for (Map<String, Object> map : lista) {
			constante = new Constante();
			constante.setN_idcons(ValidarCampos.castValorBigDecimalToLong((BigDecimal) map.get("N_IDCONS")));
			constante.setV_valcons(ValidarCampos.validaCamposString((String) map.get("V_VALCONS")));
			constante.setV_abrecons(ValidarCampos.validaCamposString((String) map.get("V_ABRECONS")));
			constante.setN_discons(ValidarCampos.castValorBigDecimalToLong((BigDecimal) map.get("N_DISCONS")));
			constante.setV_descons(ValidarCampos.validaCamposString((String) map.get("V_DESCONS")));
			listaConstante.add(constante);
		}

		if (listaConstante.size() > 0) {
			this.paginacion.setTotalRegistros(((BigDecimal) lista.get(0).get("RESULT_COUNT")).intValue());
		}
		return listaConstante;

	}
}
