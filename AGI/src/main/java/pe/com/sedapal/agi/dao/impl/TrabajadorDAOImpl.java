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
import pe.com.sedapal.agi.dao.ITrabajadorDAO;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class TrabajadorDAOImpl implements ITrabajadorDAO{
	
	private static final Logger LOGGER = Logger.getLogger(TrabajadorDAOImpl.class);	
	
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	@Override
	public List<Trabajador> obtenerTrabajador(AuditorRequest trabajadorRequest, PageRequest paginaRequest) {		
		Map<String, Object> out	= null;
		List<Trabajador> listaTrabajador = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_TRAB_OBTENER")
						.declareParameters(
							new SqlParameter("i_nficha", 			OracleTypes.NUMBER),
							new SqlParameter("i_vnombres", 			OracleTypes.VARCHAR),
							new SqlParameter("i_vapepaterno", 		OracleTypes.VARCHAR),
							new SqlParameter("i_vapematerno", 		OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 			OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 		OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_nficha", trabajadorRequest.getNroFicha())
						.addValue("i_vnombres", trabajadorRequest.getNombres())
						.addValue("i_vapepaterno", trabajadorRequest.getApePaterno())
						.addValue("i_vapematerno", trabajadorRequest.getApeMaterno())
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
			
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaTrabajador = new ArrayList<>();
				listaTrabajador = mapearTrabajador(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en TrabajadorDaoImpl.obtenerTrabajador";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
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
	
	private List<Trabajador> mapearTrabajador(Map<String,Object> resultados){
		List<Trabajador> listaTrabajador = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Trabajador item = null;
		for(Map<String, Object> map : lista) {	
			item = new Trabajador();
			
			if(map.get("idtrabajador") != null) {
				item.setIdTrabajador(((BigDecimal) map.get("idtrabajador")).longValue());
			}
			if(map.get("nficha") != null) {
				item.setNroFicha(((BigDecimal) map.get("nficha")).longValue());
			}
			if(map.get("nombres") != null) {
				item.setNombreTrabajador((String)map.get("nombres"));
			}
			if(map.get("cargo") != null) {
				item.setCargoTrabajador((String)map.get("cargo"));
			}
			
			listaTrabajador.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaTrabajador;
	}

}
