package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import pe.com.sedapal.agi.dao.IEquipoDAO;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.request_objects.EquipoRequest;
import pe.com.sedapal.agi.model.enums.EstadoConstante;

@Repository
public class EquipoDAOImpl implements IEquipoDAO {
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Error			error;
	
	public Error getError() {
		return this.error;
	}
	
	@Override
	public List<Equipo> obtenerEquipo(EquipoRequest equipoRequest) {
		Map<String, Object> out	= null;
		List<Equipo> lista		= new ArrayList<>();
		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_EQUIPO_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
				new SqlParameter("i_vnombre", 		OracleTypes.VARCHAR),
				new SqlParameter("i_vresponsable", 	OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			equipoRequest.getId())
			.addValue("i_vnombre",		equipoRequest.getNombre())
			.addValue("i_vresponsable",	equipoRequest.getJefe());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearEquipo(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en EquipoDAOImpl.obtenerEquipo";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return lista;
	}

	@SuppressWarnings("unchecked")
	private List<Equipo> mapearEquipo(Map<String, Object> resultados) {
		List<Equipo> listaEquipo	 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Equipo item						= null;
		
		for(Map<String, Object> map : lista) {			
			item = new Equipo();
			item.setId(((BigDecimal)map.get("N_IDEQUI")).longValue());
			item.setDescripcion((String)map.get("V_NOMEQUI"));
			item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISEQUI")));		

			if(map.get("N_JEFEQUI")!=null) {
				Colaborador jefe = new Colaborador();
				jefe.setIdColaborador(((BigDecimal)map.get("N_JEFEQUI")).longValue());
				jefe.setNombreCompleto((String)map.get("V_JEFEQUI"));
				item.setJefe(jefe);
			}
			listaEquipo.add(item);
		}
		return listaEquipo;
	}
	
}