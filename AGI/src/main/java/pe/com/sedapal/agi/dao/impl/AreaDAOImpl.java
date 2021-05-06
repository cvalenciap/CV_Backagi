package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
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
import pe.com.sedapal.agi.dao.IAreaDAO;
import pe.com.sedapal.agi.model.Area;
import pe.com.sedapal.agi.model.NoConformidad;
import pe.com.sedapal.agi.model.request_objects.AreaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class AreaDAOImpl implements IAreaDAO{
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

	@Override
	public List<Area> obtenerArea(AreaRequest constanteRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Area> lista = new ArrayList<>();
		this.error=null;
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_AREA")
				.withProcedureName("PRC_AREA_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidarea", OracleTypes.NUMBER),
						new SqlParameter("i_nidcentro", OracleTypes.NUMBER),
						new SqlParameter("i_vdescripcion", OracleTypes.VARCHAR),
						new SqlParameter("i_vabreviatura", OracleTypes.VARCHAR),
						new SqlParameter("i_vtipoarea", OracleTypes.VARCHAR),
						new SqlParameter("i_nidareasuperior", OracleTypes.NUMBER),					
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlParameter("i_vorden", OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidarea", constanteRequest.getIdArea())
				.addValue("i_nidcentro", constanteRequest.getIdCentro())
				.addValue("i_vdescripcion", constanteRequest.getDescripcion())
				.addValue("i_vabreviatura", constanteRequest.getAbreviatura())
				.addValue("i_vtipoarea", constanteRequest.getTipoArea())
				.addValue("i_nidareasuperior", constanteRequest.getIdAreaSuperior())		
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros())
				.addValue("i_vorden",null);
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");			
			if(resultado == 0) {
				lista = mapearArea(out);
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

	@Override
	public List<Area> obtenerAreaMigracion(AreaRequest constanteRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Area> lista = new ArrayList<>();
		this.error=null;
		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());

		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_AREA")
				.withProcedureName("PRC_AREA_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidarea", OracleTypes.NUMBER),
						new SqlParameter("i_nidcentro", OracleTypes.NUMBER),
						new SqlParameter("i_vdescripcion", OracleTypes.VARCHAR),
						new SqlParameter("i_vabreviatura", OracleTypes.VARCHAR),
						new SqlParameter("i_vtipoarea", OracleTypes.VARCHAR),
						new SqlParameter("i_nidareasuperior", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlParameter("i_vorden", OracleTypes.VARCHAR),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidarea", constanteRequest.getIdArea())
				.addValue("i_nidcentro", constanteRequest.getIdCentro())
				.addValue("i_vdescripcion", constanteRequest.getDescripcion())
				.addValue("i_vabreviatura", constanteRequest.getAbreviatura())
				.addValue("i_vtipoarea", constanteRequest.getTipoArea())
				.addValue("i_nidareasuperior", constanteRequest.getIdAreaSuperior())
				.addValue("i_npagina",pageRequest.getPagina())
				.addValue("i_nregistros",pageRequest.getRegistros())
				.addValue("i_vorden",null);

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if(resultado == 0) {
				lista = mapearArea(out);
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

	@Override
	public Paginacion getPaginacion() {
		return this.paginacion;
	}

	@Override
	public Error getError() {
		return this.error;
	}
	
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@SuppressWarnings("unchecked")
	public List<Area> mapearArea(Map<String, Object> resultados) {
		
		List<Area> listaArea = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Area item = null;
		
		for(Map<String, Object> map : lista) {
			item = new Area();
			
			item.setIdArea(((BigDecimal)map.get("NCODAREA")).longValue());
			if(map.get("NCODCENTRO")!=null) item.setIdCentro(((BigDecimal)map.get("NCODCENTRO")).longValue());
			item.setDescripcion((String)map.get("VDESCRIPCION"));
			item.setAbreviatura((String)map.get("VABREVIATURA"));
			if(map.get("NANEXO")!=null) item.setAnexo(((BigDecimal)map.get("NANEXO")).longValue());
			item.setTipoArea((String)map.get("CTIPAREA"));
			if(map.get("NARESUPERIOR")!=null) item.setIdAreaSuperior(((BigDecimal)map.get("NARESUPERIOR")).longValue());
			item.setFechaCreacion((Date)map.get("DFECCREACION"));
			item.setFechaActualizacion((Date)map.get("DFECACTUALIZACION"));
			item.setResponsable((String)map.get("VRESPONSABLE"));
			
			listaArea.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		
		return listaArea;
	}
	
}
