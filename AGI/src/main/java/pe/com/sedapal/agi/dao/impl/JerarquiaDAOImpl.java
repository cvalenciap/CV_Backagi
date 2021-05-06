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
import pe.com.sedapal.agi.dao.IJerarquiaDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.FichaTecnica;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.request_objects.JerarquiaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.model.enums.EstadoConstante;

@Repository
public class JerarquiaDAOImpl implements IJerarquiaDAO {
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	private SessionInfo session;
	
	public Paginacion getPaginacion() {
		return this.paginacion;
	}	
	public Error getError() {
		return this.error;
	}
	
	@Override	
	public List<Jerarquia> obtenerJerarquia(JerarquiaRequest parametros) {		
		this.error=null;
		Map<String, Object> out = null;
		List<Jerarquia> listaJerarquia = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_JERARQUIA_OBTENER")
				.declareParameters(
						new SqlParameter("i_nid", 			OracleTypes.NUMBER),
						new SqlParameter("i_ntipo", 		OracleTypes.NUMBER),
						new SqlParameter("i_vdescripcion", 	OracleTypes.VARCHAR),
						new SqlParameter("i_nnivel", 		OracleTypes.NUMBER),
						new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
						new SqlParameter("i_nidtipodocu",	OracleTypes.NUMBER),
						new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
						new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
						new SqlParameter("i_vorden", 		OracleTypes.VARCHAR),						
						new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nid",			parametros.getId())
				.addValue("i_ntipo",		parametros.getTipo())
				.addValue("i_vdescripcion",	parametros.getDescripcion())
				.addValue("i_nnivel",		parametros.getNivel())
				.addValue("i_ndisponible",	parametros.getEstado())
				.addValue("i_nidtipodocu",	parametros.getIdTipoDocu())
//				.addValue("i_npagina",		pageRequest.getPagina())
//				.addValue("i_nregistros",	pageRequest.getRegistros())
				.addValue("i_nidtipodocu",	parametros.getIdTipoDocu())
				.addValue("i_npagina",		0)
				.addValue("i_nregistros",	0)
				.addValue("i_vorden",		parametros.getTipoJerarquiaNombre());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)  out.get("o_retorno");			
			if(resultado == 0) {
				listaJerarquia = mapearJerarquia(out);
//				System.out.println("MENSAJE");
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
		
		for (Jerarquia jerarquia : listaJerarquia) {
			System.out.println(jerarquia.getDescripcion());			
		}
	  return listaJerarquia;
	}
		
	@SuppressWarnings("unchecked")
	private List<Jerarquia> mapearJerarquia(Map<String, Object> resultados) {
		List<Jerarquia> listaJerarquia 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Jerarquia item					= null;
		
		for(Map<String, Object> map : lista) {			
			item = new Jerarquia();
			if (map.get("N_IDJERA") != null) {
				item.setId(((BigDecimal)map.get("N_IDJERA")).longValue());
			}
			/*
			if (map.get("V_CODJERA") != null) {
				item.setCodJera(((String)map.get("V_CODJERA")));
			}*/
			
			if (map.get("V_ABRJERA") != null) {
				item.setAbrJera(((String)map.get("V_ABRJERA")));
			}
			
			//cguerra
			if (map.get("N_INDDESC") != null) {
				item.setIndicadorDescargas(((BigDecimal)map.get("N_INDDESC")).longValue());
			}			
			//cguerra						
			
			if (map.get("N_IDTIPODOCU") != null) {
				item.setIdTipoDocu(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
			}
			
			/*if (map.get("N_IDJERA") != null) {
				item.setId(((BigDecimal)map.get("N_IDJERA")).longValue());
			}*/
			
			if (map.get("N_IDTIPO") != null) {
				item.setIdTipo(((BigDecimal)map.get("N_IDTIPO")).longValue());
			}
			
			if (map.get("V_NOMTIPO") != null) {
				item.setTipo((String)map.get("V_NOMTIPO"));
			}
			
			if (map.get("V_DESJERA") != null) {
				item.setDescripcion((String)map.get("V_DESJERA"));
			}
			
			if (map.get("N_NIVJERA") != null) {
				item.setNivel(((BigDecimal)map.get("N_NIVJERA")).longValue());
			}
			
			if (map.get("N_ORDJERA") != null) {
				item.setOrden(((BigDecimal)map.get("N_ORDJERA")).longValue());
			}
			
			if (map.get("V_RUTJERA") != null) {
				item.setRuta((String)map.get("V_RUTJERA"));
			}
			
			if (map.get("V_INDGRAJERA") != null) {
				item.setIndicadorGrabar((String)map.get("V_INDGRAJERA"));
			}
			if (map.get("V_INDAGUJERA") != null) {
				item.setIndicadorAgua((String)map.get("V_INDAGUJERA"));
			}
			if (map.get("TEXTONODO") != null) {
				item.setTextoNodo((String)map.get("TEXTONODO"));
			}
			
			if (map.get("N_RETREVJERA") != null) {
				item.setRetencionRevision((map.get("N_RETREVJERA")==null)?0:((BigDecimal)map.get("N_RETREVJERA")).longValue());
			}
			
			if (map.get("N_DISJERA") != null) {
				item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISJERA")));
			}		

			if(map.get("N_IDJERAPADR")!=null) {
				Jerarquia padre = new Jerarquia();
				padre.setId(((BigDecimal)map.get("N_IDJERAPADR")).longValue());
				if (map.get("V_NOMJERAPADR") != null) {
					padre.setDescripcion((String)map.get("V_NOMJERAPADR"));
				}				
				item.setIdPadre(padre.getId());
				item.setPadre(padre);
			}
			
			if(map.get("N_IDFICH")!=null) {
				FichaTecnicaRequest fichaRequest = new FichaTecnicaRequest();
				fichaRequest.setId(((BigDecimal)map.get("N_IDFICH")).longValue());
				fichaRequest.setEstado(Long.valueOf("1"));
				
				FichaDAOImpl fichaDao = new FichaDAOImpl();
				fichaDao.setJdbc(this.jdbc);
				FichaTecnica objeto = fichaDao.obtenerFicha(fichaRequest);

				if(objeto!=null) {
					item.setFicha(objeto);
					item.setIdFicha(objeto.getId());
				}
			}
//			List<Constante> extension = this.obtenerExtension(item);
//			item.setExtension(extension);
			
			listaJerarquia.add(item);

//			if (map.get("RESULT_COUNT")!=null) {
//				BigDecimal dato = (BigDecimal) map.get("RESULT_COUNT");
//				int datoEntero = dato.intValue();
//				
//				this.paginacion.setTotalRegistros(datoEntero);
//			}

		}
		return listaJerarquia;
	}
	
	/*@SuppressWarnings("unchecked")
	private List<Constante> obtenerExtension(Jerarquia jerarquia) {
		Map<String, Object> out		= null;
		List<Constante> extension	= new ArrayList<>();
		
		this.error		= null;
		this.jdbcCall	=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_EXTENSION_OBTENER")
			.declareParameters(
				new SqlParameter("i_njerarquia",	OracleTypes.NUMBER),						
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_njerarquia", jerarquia.getId());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<Map<String, Object>> lista = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : lista) {
					Constante item = new Constante();
					item.setIdconstante(((BigDecimal)map.get("N_IDCONS")).longValue());
					item.setV_valcons((String)map.get("V_NOCONS"));
					item.setN_discons(((BigDecimal)map.get("N_DISEXTE")).longValue());
					extension.add(item);
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en JerarquiaDAOImpl.obtenerExtension";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
	  return extension;
	}*/

	@Override
	public List<Jerarquia> guardarJerarquia(Jerarquia jerarquia, Long codigo, String usuario) {
		List<Jerarquia> lista	= new ArrayList<>();
		Map<String, Object> out = null;
		FichaTecnica ficha		= null;
		this.error				= null;
		
		try {

			if(jerarquia.getFicha()!=null) {
				jerarquia.getFicha().setDisponible(jerarquia.getDisponible());
				
				FichaDAOImpl fichaDao = new FichaDAOImpl();
				fichaDao.setJdbc(this.jdbc);
				ficha = fichaDao.guardarFicha(jerarquia.getFicha(), jerarquia.getFicha().getId(), usuario);
			}
			
			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_JERARQUIA_GUARDAR")
				.declareParameters(
					new SqlParameter("i_nid",			OracleTypes.NUMBER),
					new SqlParameter("i_nidpadre",		OracleTypes.NUMBER),
					new SqlParameter("i_vdescripcion",	OracleTypes.VARCHAR),
					new SqlParameter("i_ntipo",			OracleTypes.NUMBER),
					new SqlParameter("i_vruta",			OracleTypes.VARCHAR),
					new SqlParameter("i_nficha",		OracleTypes.NUMBER),
					new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlParameter("i_ntipodocu",		OracleTypes.NUMBER),
					new SqlParameter("i_abrjera",		OracleTypes.VARCHAR),
					new SqlParameter("i_indicadesc",		OracleTypes.NUMBER),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nid",			codigo)
				.addValue("i_nidpadre",		jerarquia.getIdPadre())
				.addValue("i_vdescripcion",	jerarquia.getDescripcion())
				.addValue("i_ntipo",		jerarquia.getIdJerarquia())
				.addValue("i_vruta",		jerarquia.getRuta())
				.addValue("i_nficha",		(ficha!=null)?ficha.getId():null)
				.addValue("i_ndisponible",	jerarquia.getDisponible())
				.addValue("i_vusuario",		usuario)
				.addValue("i_ntipodocu",	jerarquia.getIdTipoDocu())
				.addValue("i_abrjera",		jerarquia.getAbrJera())
				.addValue("i_indicadesc",	jerarquia.getIndicadorDescargas());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {

			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en JerarquiaDAOImpl.guardarJerarquia";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Jerarquia actualizarPermiso(Jerarquia jerarquia, Long codigo, String usuario) {
		Jerarquia item			= null;			
		Map<String, Object> out = null;
		this.error				= null;
		
		try {

			this.jdbcCall =
			new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_DOCUMENTO")
				.withProcedureName("PRC_PERMISO_GUARDAR")
				.declareParameters(
					new SqlParameter("i_nid",			OracleTypes.NUMBER),
					new SqlParameter("i_vgrabar",		OracleTypes.VARCHAR),
					new SqlParameter("i_vagua",			OracleTypes.VARCHAR),
					new SqlParameter("i_nretencion",	OracleTypes.NUMBER),
					new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
					new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
					new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
			
			SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_nid",			codigo)
				.addValue("i_vgrabar",		jerarquia.getIndicadorGrabar())
				.addValue("i_vagua",		jerarquia.getIndicadorAgua())
				.addValue("i_nretencion",	jerarquia.getRetencionRevision())
				.addValue("i_vusuario",		usuario);
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				List<Map<String, Object>> consulta = (List<Map<String, Object>>)out.get("o_cursor");
				for(Map<String, Object> map : consulta) {
					item = new Jerarquia();
					
					item.setId(((BigDecimal)map.get("N_IDJERA")).longValue());
					item.setIndicadorGrabar((String)map.get("V_INDGRAJERA"));
					item.setIndicadorAgua((String)map.get("V_INDAGUJERA"));
					item.setRetencionRevision((map.get("N_RETREVJERA")==null)?0:((BigDecimal)map.get("N_RETREVJERA")).longValue());
				}
				
				if(jerarquia.getExtension()!=null) {
					List<Constante> listado = new ArrayList<>();
					boolean eliminado = this.eliminarExtension(null, codigo, usuario);
					if(eliminado==false)	{
						return null;
					}
					for(Constante extension : jerarquia.getExtension()) {
						Constante objeto = this.guardarExtension(extension, codigo, usuario);
						listado.add(objeto);
					}
					item.setExtension(listado);
				}				
				return item;
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				return item;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en JerarquiaDAOImpl.actualizarPermiso";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return item;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Constante guardarExtension(Constante extension, Long jerarquia, String usuario) {
		Constante objeto		= null;
		Map<String, Object> out = null;
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_EXTENSION_GUARDAR")
			.declareParameters(
			    new SqlParameter("i_nextension",	OracleTypes.NUMBER),
			    new SqlParameter("i_njerarquia",	OracleTypes.NUMBER),
			    new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
			    new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nextension",	extension.getIdconstante())
			.addValue("i_njerarquia",	jerarquia)
			.addValue("i_ndisponible",	1)
			.addValue("i_vusuario",		usuario);
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				if(listado!=null) {
					for(Map<String, Object> map : listado) {
						objeto = new Constante();
						objeto.setIdconstante(((BigDecimal)map.get("N_IDCONS")).longValue());
						objeto.setV_valcons((String)map.get("V_NOCONS"));
						objeto.setN_discons(((BigDecimal)map.get("N_DISEXTE")).longValue());
					}
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en JerarquiaDAOImpl.guardarExtension";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return objeto;
	}

	public Boolean eliminarJerarquia(Long codigo, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_JERARQUIA_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_nid",			OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
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
				FichaDAOImpl fichaDao = new FichaDAOImpl();
				fichaDao.setJdbc(this.jdbc);
				boolean eliminadoFicha = fichaDao.eliminarFicha(null, codigo, usuario);
				if(eliminadoFicha==false) {
					return false;
				}
				boolean eliminado = this.eliminarExtension(null, codigo, usuario);
				return eliminado;
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				return false;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en JerarquiaDAOImpl.eliminarJerarquia";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}

	private Boolean eliminarExtension(Long extension, Long jerarquia, String usuario) {
		Map<String, Object> out = null;
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_DOCUMENTO")
			.withProcedureName("PRC_EXTENSION_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_nextension",	OracleTypes.NUMBER),
				new SqlParameter("i_njerarquia",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nextension",	extension)
			.addValue("i_njerarquia",	jerarquia)
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
			String mensaje			= "Error en JerarquiaDAOImpl.eliminarExtension";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}
	
	
	@Override
	public List<Jerarquia> obtenerHijoJerarquia(Jerarquia jerarquia) {
		this.error = null;
		Map<String, Object> out = null;
		List<Jerarquia> jerarquiaList=null;
		this.error=null;
			try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_JERARQUIA_HIJOS_OBTENER")
					.declareParameters(
							new SqlParameter("i_v_rutjera", 	OracleTypes.VARCHAR),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_v_rutjera", jerarquia.getDetAnterior());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				jerarquiaList = new ArrayList<>();
				jerarquiaList = mapearHijosJerarquia(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
			
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.obtenerPregunta";
			String mensajeInterno	= e.getMessage();
		
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}

		
		
		return jerarquiaList;
	}
	
	private List<Jerarquia> mapearHijosJerarquia(Map<String,Object> resultados){
		List<Jerarquia> list  = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Jerarquia item = null;
		for(Map<String, Object> map : lista) {	
			item = new Jerarquia();
			
			if (map.get("N_IDJERA") != null) {
				item.setId(((BigDecimal)map.get("N_IDJERA")).longValue());
			}
			if (map.get("V_NOMTIPO") != null) {
				item.setTipo((String)map.get("V_NOMTIPO"));
			}
			
			if (map.get("V_DESJERA") != null) {
				item.setDescripcion((String)map.get("V_DESJERA"));
			}
			
			if (map.get("V_RUTJERA") != null) {
				item.setRuta((String)map.get("V_RUTJERA"));
			}
			
			if (map.get("n_nivjera") != null) {
				item.setNivel(((BigDecimal)map.get("n_nivjera")).longValue());
			}
			if (map.get("N_DISJERA") != null) {
				item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISJERA")));
			}	
			if(map.get("N_IDJERAPADR")!=null) {
				Jerarquia padre = new Jerarquia();
				padre.setId(((BigDecimal)map.get("N_IDJERAPADR")).longValue());		
				item.setIdPadre(padre.getId());
				
			}
			if (map.get("N_IDTIPODOCU") != null) {
				item.setIdTipoDocu(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
			}

			if (map.get("V_ABRJERA") != null) {
				item.setAbrJera((String)map.get("V_ABRJERA"));
			}
			
			
			
			list.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return list;
	}
	
	
	public List<Jerarquia> actualizarJerarquia(Jerarquia jerarquia, Long codigo, String usuario) {
		Jerarquia bean= null;
		List<Jerarquia> lista= new ArrayList<>();
		int numero=Integer.valueOf(jerarquia.getNivel().intValue());
		if(jerarquia.getEstado().name().equals("ACTIVO")) {
			int num=1;
			BigDecimal big= new BigDecimal(num);
			jerarquia.setDisponible(big);
		}else {
			int num=0;
			BigDecimal big= new BigDecimal(num);
			jerarquia.setDisponible(big);
		}
		this.guardarJerarquia(jerarquia, codigo, usuario);
		
		lista=this.obtenerHijoJerarquia(jerarquia);
		String ruta=null;
		
		if(lista.size()>0) {
			
			for(Jerarquia jera:lista) {
				
				if(jera.getNivel()!=numero) {
					String rutaDet=jera.getRuta().toString();
					String cadena= rutaDet.replace("\\", ",");
					String[] list=cadena.split(",");
					
					for(int i=0;i<list.length;i++) {
						
						if(list[i].contentEquals(jerarquia.getNomAnterior())) {
							String data=null;
							data=list[i];
							cadena=cadena.replace(data, jerarquia.getDescripcion());
							ruta=cadena.replace(",", "\\");
							jera.setRuta(ruta);
						}
					}
					if(jera.getEstado().name().equals("ACTIVO")) {
						int num=1;
						BigDecimal big= new BigDecimal(num);
						jera.setDisponible(big);
					}else {
						int num=0;
						BigDecimal big= new BigDecimal(num);
						jera.setDisponible(big);
					}
					this.guardarJerarquia(jera, jera.getId(), usuario);
				}
				
			}
			
		}
		
		return lista;
	}
		
	@Override
	public List<Jerarquia> obtenerDocJerarquia(Jerarquia jerarquia) {
		this.error = null;
		Map<String, Object> out = null;
		List<Jerarquia> jerarquiaList=null;
			try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_JERARQUIA_DOC_OBTENER")
					.declareParameters(
							new SqlParameter("i_v_rutjera", OracleTypes.VARCHAR),
							new SqlParameter("i_idTipo", 	OracleTypes.NUMBER),	
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_v_rutjera", jerarquia.getRuta())
						.addValue("i_idTipo", jerarquia.getIdTipo());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				jerarquiaList = new ArrayList<>();
				jerarquiaList = mapearDocJerarquia(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
			
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.obtenerPregunta";
			String mensajeInterno	= e.getMessage();
		
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}

		
		
		return jerarquiaList;
	}
	
	private List<Jerarquia> mapearDocJerarquia(Map<String,Object> resultados){
		List<Jerarquia> list  = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Jerarquia item = null;
		for(Map<String, Object> map : lista) {	
			item = new Jerarquia();
			
			if(map.get("cantDoc")!=null) {
				
				item.setCantidadDocumentos(((BigDecimal)map.get("cantDoc")).longValue());						
			}
			list.add(item);


		}
		
		return list;
	}
	
	@Override	
	public List<Jerarquia> obtenerJerarquiaTipoDocumento(JerarquiaRequest parametros, PageRequest pageRequest) {		
		this.error=null;
		Map<String, Object> out = null;
		List<Jerarquia> listaJerarquia = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_TIPO_DOCUMENTO_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidtipo", 		OracleTypes.NUMBER),
						new SqlParameter("i_nidjerapadr", 	OracleTypes.NUMBER),
						new SqlParameter("i_npagina", 		OracleTypes.NUMBER),
						new SqlParameter("i_nregistros",	OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidtipo",			parametros.getIdTipoDocu())
				.addValue("i_nidjerapadr",		parametros.getId())
				.addValue("i_npagina",		pageRequest.getPagina())
				.addValue("i_nregistros",	pageRequest.getRegistros());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)  out.get("o_retorno");			
			if(resultado == 0) {
				listaJerarquia = mapearJerarquiaTipoDocumento(out);
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
		
		for (Jerarquia jerarquia : listaJerarquia) {
			System.out.println(jerarquia.getDescripcion());			
		}
	  return listaJerarquia;
	}

	private List<Jerarquia> mapearJerarquiaTipoDocumento(Map<String,Object> resultados){
		List<Jerarquia> list  = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Jerarquia item = null;
		for(Map<String, Object> map : lista) {	
			item = new Jerarquia();
			
			if(map.get("n_idjera")!=null) {item.setId(((BigDecimal)map.get("n_idjera")).longValue());}
			if(map.get("v_desjera")!=null){item.setDescripcion((String)map.get("v_desjera"));}
			if(map.get("n_idtipo")!=null) {item.setIdTipo(((BigDecimal)map.get("n_idtipo")).longValue());}
			if(map.get("n_idtipodocu")!=null) {item.setIdTipoDocu(((BigDecimal)map.get("n_idtipodocu")).longValue());}
			list.add(item);


		}
		
		return list;
	}
	
}