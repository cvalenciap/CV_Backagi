package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import pe.com.sedapal.agi.dao.IRutaDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Ruta;
import pe.com.sedapal.agi.model.RutaResponsable;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RutaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.enums.EstadoConstante;
import pe.com.sedapal.agi.model.enums.Fase;
import pe.com.sedapal.agi.model.enums.TipoConstante;

@Repository
public class RutaDAOImpl implements IRutaDAO {
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
	public List<Ruta> obtenerRuta(RutaRequest rutaRequest, PageRequest pageRequest) {
		Map<String, Object> out	= null;
		List<Ruta> lista		= new ArrayList<>();
		
		this.paginacion = new Paginacion();
		this.error		= null;
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());			
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_RUTA_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),						
				new SqlParameter("i_vnombre", 		OracleTypes.VARCHAR),	
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
			.addValue("i_nid",			rutaRequest.getId())
			.addValue("i_vnombre",		rutaRequest.getDescripcion())
			.addValue("i_ndisponible",	rutaRequest.getEstado())
			.addValue("i_npagina",		pageRequest.getPagina())
			.addValue("i_nregistros",	pageRequest.getRegistros())
			.addValue("i_vorden",		null);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearRuta(out);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RutaDAOImpl.obtenerRuta";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}
	  return lista;
	}
	
	private Long obtenerIdConstante(List<Constante> lista, String nombre) {
		for(Constante item: lista) {
			if(item.getV_valcons().equals(nombre)) {
				return item.getIdconstante();
			}
		}
		return Long.valueOf("0");
	}

	@SuppressWarnings("unchecked")
	private List<Ruta> mapearRuta(Map<String, Object> resultados) {
		List<Ruta> listaRuta 			= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Ruta item						= null;
		
		//Obtener Fase
		ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
		constanteDao.setJdbc(this.jdbc);
		
		ConstanteRequest constante = new ConstanteRequest();
		constante.setPadre(TipoConstante.ETAPA_RUTA.toString());
		
		PageRequest pagina = new PageRequest();
		pagina.setPagina(1);
		pagina.setRegistros(1000);
		
		List<Constante> listaFase = constanteDao.obtenerConstantes(constante, pagina, null);
		Long faseElaboracion  = this.obtenerIdConstante(listaFase, Fase.ELABORACION.toString());
		Long faseConsenso	  = this.obtenerIdConstante(listaFase, Fase.CONSENSO.toString());
		Long faseAprobacion   = this.obtenerIdConstante(listaFase, Fase.APROBACION.toString());
		Long faseHomologacion = this.obtenerIdConstante(listaFase, Fase.HOMOLOGACION.toString());
		
		for(Map<String, Object> map : lista) {			
			item = new Ruta();
			item.setId(((BigDecimal)map.get("N_IDRUTA")).longValue());
			item.setUsuarioCreacion((String)map.get("A_V_USUCRE"));
			item.setFechaCreacion((Date)map.get("A_D_FECCRE"));
			item.setDescripcion((String)map.get("V_NOMRUTA"));
			item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISRUTA")));		
			
			//Obtener Participantes por Fase
			List<RutaResponsable> listaParticipante=this.obtenerParticipanteFase(item.getId(),null);
			item.setListaElaboracion(listaParticipante.stream().filter(obj -> 
				(obj.getIdFase()+"").equals(faseElaboracion+"")).collect(Collectors.toList()));
			item.setListaConsenso(listaParticipante.stream().filter(obj -> 
				(obj.getIdFase()+"").equals(faseConsenso+"")).collect(Collectors.toList()));
			item.setListaAprobacion(listaParticipante.stream().filter(obj -> 
				(obj.getIdFase()+"").equals(faseAprobacion+"")).collect(Collectors.toList()));
			item.setListaHomologacion(listaParticipante.stream().filter(obj -> 
				(obj.getIdFase()+"").equals(faseHomologacion+"")).collect(Collectors.toList()));
			
			listaRuta.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		return listaRuta;
	}
	
	@SuppressWarnings("unchecked")
	private List<RutaResponsable> obtenerParticipanteFase(Long id, Long fase) {
		Map<String, Object> out	= null;
		List<RutaResponsable> listaResponsable	= null;
	
		this.error		= null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_RUTACOLABORADOR_OBTENER")
			.declareParameters(
				new SqlParameter("i_nidruta", 		OracleTypes.NUMBER),						
				new SqlParameter("i_nidcola", 		OracleTypes.NUMBER),	
				new SqlParameter("i_nidfase",		OracleTypes.NUMBER),
				new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nidruta",		id)
			.addValue("i_nidcola",		null)
			.addValue("i_nidfase",		fase)
			.addValue("i_ndisponible",	1);
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				listaResponsable = new ArrayList<>();		
				List<Map<String, Object>> lista	= (List<Map<String, Object>>)out.get("o_cursor");
				
				for(Map<String, Object> map : lista) {			
					RutaResponsable item = new RutaResponsable();
					item.setIdRuta(((BigDecimal)map.get("N_IDRUTA")).longValue());
					item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
					item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					item.setRuta((String)map.get("V_NOMRUTA"));
					item.setResponsable((String)map.get("V_NOMCOLA"));
					item.setFuncion((String)map.get("V_FUNCOLA"));
					item.setEquipo((String)map.get("V_EQUCOLA"));
					item.setFase((String)map.get("V_NOMFASE"));
					item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISRUTCOL")));		
					listaResponsable.add(item);
					
					if (map.get("RESULT_COUNT")!=null) {
						this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
					}
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RutaDAOImpl.obtenerParticipanteFase";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			return null;
		}
		return listaResponsable;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ruta> guardarRuta(Ruta ruta, Long codigo, String usuario) {
		List<Ruta> lista = null;
		List<RutaResponsable> listaResponsable = new ArrayList<>();
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_RUTA_GUARDAR")
			.declareParameters(
				new SqlParameter("i_nid",			OracleTypes.NUMBER),
				new SqlParameter("i_vnombre",		OracleTypes.VARCHAR),						
				new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			codigo)
			.addValue("i_vnombre",		ruta.getDescripcion())
			.addValue("i_ndisponible",	ruta.getDisponible())
			.addValue("i_vusuario",		usuario);
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				if(codigo!=null) {
					boolean eliminado = this.eliminarRutaColaborador(codigo, usuario);
					if(eliminado==false) {
						return lista;
					}
				}
				lista = new ArrayList<>();
				
				for(Map<String, Object> map : listado) {
					Ruta item = new Ruta();
					item.setId(((BigDecimal)map.get("N_IDRUTA")).longValue());
					item.setUsuarioCreacion((String)map.get("A_V_USUCRE"));
					item.setFechaCreacion((Date)map.get("A_D_FECCRE"));
					item.setDescripcion((String)map.get("V_NOMRUTA"));
					item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISRUTA")));
					
					if(ruta.getListaElaboracion()!=null) {
						for(RutaResponsable responsable: ruta.getListaElaboracion()) {
							responsable.setIdRuta(item.getId());
							responsable.setDisponible(ruta.getDisponible());
							RutaResponsable rutaResponsable = this.registrarRutaColaborador(responsable, usuario);
							listaResponsable.add(rutaResponsable);
						}
					}
					item.setListaElaboracion(listaResponsable);
					
					if(ruta.getListaConsenso()!=null) {
						for(RutaResponsable responsable: ruta.getListaConsenso()) {
							responsable.setIdRuta(item.getId());
							responsable.setDisponible(ruta.getDisponible());
							RutaResponsable rutaResponsable = this.registrarRutaColaborador(responsable, usuario);
							listaResponsable.add(rutaResponsable);
						}
					}
					item.setListaConsenso(listaResponsable);
					
					if(ruta.getListaAprobacion()!=null) {
						for(RutaResponsable responsable: ruta.getListaAprobacion()) {
							responsable.setIdRuta(item.getId());
							responsable.setDisponible(ruta.getDisponible());
							RutaResponsable rutaResponsable = this.registrarRutaColaborador(responsable, usuario);
							listaResponsable.add(rutaResponsable);
						}
					}
					item.setListaAprobacion(listaResponsable);
					
					if(ruta.getListaHomologacion()!=null) {
						for(RutaResponsable responsable: ruta.getListaHomologacion()) {
							responsable.setIdRuta(item.getId());
							responsable.setDisponible(ruta.getDisponible());
							RutaResponsable rutaResponsable = this.registrarRutaColaborador(responsable, usuario);
							listaResponsable.add(rutaResponsable);
						}
					}
					item.setListaHomologacion(listaResponsable);
					
					lista.add(item);
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RutaDAOImpl.guardarRuta";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private RutaResponsable registrarRutaColaborador(RutaResponsable responsable, String usuario) {
		RutaResponsable rutaResponsable = new RutaResponsable();
		Map<String, Object> out = null;
		
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_RUTACOLABORADOR_GUARDAR")
			.declareParameters(
				new SqlParameter("i_nidruta", 		OracleTypes.NUMBER),						
				new SqlParameter("i_nidcola", 		OracleTypes.NUMBER),	
				new SqlParameter("i_nidfase",		OracleTypes.NUMBER),
				new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nidruta",		responsable.getIdRuta())
			.addValue("i_nidcola",		responsable.getIdColaborador())
			.addValue("i_nidfase",		responsable.getIdFase())
			.addValue("i_ndisponible",	responsable.getDisponible())
			.addValue("i_vusuario",		usuario);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				List<RutaResponsable> listaResponsable = new ArrayList<>();		
				List<Map<String, Object>> lista	= (List<Map<String, Object>>)out.get("o_cursor");
				
				for(Map<String, Object> map : lista) {			
					RutaResponsable item = new RutaResponsable();
					item.setIdRuta(((BigDecimal)map.get("N_IDRUTA")).longValue());
					item.setIdColaborador(((BigDecimal)map.get("N_IDCOLA")).longValue());
					item.setIdFase(((BigDecimal)map.get("N_IDFASE")).longValue());
					item.setRuta((String)map.get("V_NOMRUTA"));
					item.setResponsable((String)map.get("V_NOMCOLA"));
					item.setFuncion((String)map.get("V_FUNCOLA"));
					item.setEquipo((String)map.get("V_EQUCOLA"));
					item.setFase((String)map.get("V_NOMFASE"));
					item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISRUTCOL")));		
					listaResponsable.add(item);
					
					if (map.get("RESULT_COUNT")!=null) {
						this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
					}
				}
				rutaResponsable = (RutaResponsable)listaResponsable.get(0);
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RutaDAOImpl.registrarRutaColaborador";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			return null;
		}
		return rutaResponsable;
	}
	
	public Boolean eliminarRutaColaborador(Long codigo, String usuario) {
		Map<String, Object> out = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_RUTACOLABORADOR_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_nidruta",		OracleTypes.NUMBER),
				new SqlParameter("i_nidcola",		OracleTypes.NUMBER),
				new SqlParameter("i_nidfase",		OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nidruta",	codigo)
			.addValue("i_nidcola",	null)
			.addValue("i_nidfase",	null)
			.addValue("i_vusuario",	usuario);

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
			String mensaje			= "Error en RutaDAOImpl.eliminarRutaColaborador";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}

	public Boolean eliminarRuta(Long codigo, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_RUTA_ELIMINAR")
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
				return true;
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
				return false;
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en RutaDAOImpl.eliminarRuta";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}

}