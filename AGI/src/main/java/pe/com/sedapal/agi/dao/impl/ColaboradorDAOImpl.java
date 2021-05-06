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
import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.IColaboradorDAO;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.request_objects.ColaboradorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.enums.EstadoConstante;

@Repository
public class ColaboradorDAOImpl implements IColaboradorDAO {
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
	public List<Colaborador> obtenerColaborador(ColaboradorRequest colaboradorRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Colaborador> lista	= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_COLABORADOR_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
				new SqlParameter("i_vficha", 		OracleTypes.VARCHAR),
				new SqlParameter("i_nequipo", 		OracleTypes.NUMBER),
				new SqlParameter("i_vfuncion", 		OracleTypes.VARCHAR),
				new SqlParameter("i_vnombrecomp", 	OracleTypes.VARCHAR),
				new SqlParameter("i_vnombre", 		OracleTypes.VARCHAR),
				new SqlParameter("i_v_apepat", 		OracleTypes.VARCHAR),
				new SqlParameter("i_v_apemat", 		OracleTypes.VARCHAR),
				new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlParameter("i_vorden", 		OracleTypes.VARCHAR),						
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			colaboradorRequest.getId())
			.addValue("i_vficha",		colaboradorRequest.getNumeroFicha())
			.addValue("i_nequipo",		colaboradorRequest.getEquipo())
			.addValue("i_vfuncion",		colaboradorRequest.getFuncion())
			.addValue("i_vnombrecomp",	colaboradorRequest.getNombreCompleto())
			.addValue("i_vnombre",		colaboradorRequest.getNombre())
			.addValue("i_v_apepat",		colaboradorRequest.getApellidoPaterno())
			.addValue("i_v_apemat",		colaboradorRequest.getApellidoMaterno())
			.addValue("i_ndisponible",	colaboradorRequest.getEstado())
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
			String mensaje			= "Error en ColaboradorDAOImpl.obtenerColaborador";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return lista;
	}

	@SuppressWarnings("unchecked")
	private List<Colaborador> mapearColaborador(Map<String, Object> resultados) {
		List<Colaborador> listaColaborador	= new ArrayList<>();		
		List<Map<String, Object>> lista 	= (List<Map<String, Object>>)resultados.get("o_cursor");
		Colaborador item					= null;
		
		for(Map<String, Object> map : lista) {			
			item = new Colaborador();
			item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
			item.setFuncion((String)map.get("V_FUNCOLA"));
			item.setLogin((String)map.get("V_LOGCOLA"));
			item.setNumeroFicha(((BigDecimal)map.get("N_IDCOLA")).longValue());
			item.setApellidoPaterno((String)map.get("V_APEPAT"));
			item.setApellidoMaterno((String)map.get("V_APEMAT"));
			item.setNombre((String)map.get("V_NOMBRES"));
			item.setNombreCompleto(item.getApellidoPaterno()+" "+item.getApellidoMaterno()+" "+item.getNombre());
			item.setEstado((String)map.get("V_ESTCOLA"));
			item.setTipoDocumentoIdentidad((String)map.get("V_TIPDOCU"));
			item.setNumeroDocumentoIdentidad((String)map.get("V_DESDOCU"));
			item.setExperiencia((String)map.get("V_DESCEXPE"));
			item.setEducacion((String)map.get("V_TIPOEDUCA"));
			item.setFechaIngreso((Date)map.get("D_FECHINGRE"));
			item.setTiempoExperiencia((String)map.get("V_EXPELABO"));
			item.setEvaluacion((String)map.get("V_EVALSIST"));
			item.setDni((String)map.get("VDNI"));
			item.setEstadoDisponible(EstadoConstante.setEstado((BigDecimal)map.get("N_DISCOLA")));

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
	
	
	/*Auditoria*/
	@Override
	public List<Colaborador> obtenerColaboradorAuditor(ColaboradorRequest colaboradorRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Colaborador> lista	= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_COLABORADOR_OBTENER_AUDITOR")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
				new SqlParameter("i_vficha", 		OracleTypes.VARCHAR),
				new SqlParameter("i_nequipo", 		OracleTypes.NUMBER),
				new SqlParameter("i_vfuncion", 		OracleTypes.VARCHAR),
				new SqlParameter("i_vnombrecomp", 	OracleTypes.VARCHAR),
				new SqlParameter("i_vnombre", 		OracleTypes.VARCHAR),
				new SqlParameter("i_v_apepat", 		OracleTypes.VARCHAR),
				new SqlParameter("i_v_apemat", 		OracleTypes.VARCHAR),
				new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
				new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
				new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
				new SqlParameter("i_vorden", 		OracleTypes.VARCHAR),						
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			colaboradorRequest.getId())
			.addValue("i_vficha",		colaboradorRequest.getNumeroFicha())
			.addValue("i_nequipo",		colaboradorRequest.getEquipo())
			.addValue("i_vfuncion",		colaboradorRequest.getFuncion())
			.addValue("i_vnombrecomp",	colaboradorRequest.getNombreCompleto())
			.addValue("i_vnombre",		colaboradorRequest.getNombre())
			.addValue("i_v_apepat",		colaboradorRequest.getApellidoPaterno())
			.addValue("i_v_apemat",		colaboradorRequest.getApellidoMaterno())
			.addValue("i_ndisponible",	colaboradorRequest.getEstado())
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
			String mensaje			= "Error en ColaboradorDAOImpl.obtenerColaborador";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return lista;
	}
	

}