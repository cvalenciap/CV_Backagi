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
import pe.com.sedapal.agi.dao.IRelacionCoordinadorDAO;
import pe.com.sedapal.agi.model.FichaTecnica;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.RelacionCoordinador;
import pe.com.sedapal.agi.model.enums.EstadoConstante;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class RelacionCoordinadorDAOImpl implements IRelacionCoordinadorDAO {
	@Autowired
	private JdbcTemplate jdbc;	
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;
	
	public Paginacion getPaginacion() {
		return this.paginacion;
	}
	public Error getError() {
		return this.error;
	}
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	private static final Logger LOGGER = Logger.getLogger(RelacionCoordinadorDAOImpl.class);	

	@Override
	public List<Jerarquia> obtenerArbolJerarquiaPorTipo(Long idJerarquia) {		
		Map<String, Object> out = null;
		List<Jerarquia> listaJerarquia = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_RELA_GERE_ALCA")
				.withProcedureName("PRC_RELA_ARBOL_OBTENER")
				.declareParameters(
						new SqlParameter("i_n_idtipo", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idtipo", idJerarquia);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				listaJerarquia = mapearArbolJerarquia(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return listaJerarquia;
	}
	
	private List<Jerarquia> mapearArbolJerarquia(Map<String, Object> resultados) {
		List<Jerarquia> listaJerarquia 	= new ArrayList<>();		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		Jerarquia item = null;
		for(Map<String, Object> map : lista) {
			item = new Jerarquia();
			if (map.get("N_IDJERA") != null) {
				item.setId(((BigDecimal)map.get("N_IDJERA")).longValue());
			}
			if (map.get("N_IDTIPODOCU") != null) {
				item.setIdTipoDocu(((BigDecimal)map.get("N_IDTIPODOCU")).longValue());
			}
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
			listaJerarquia.add(item);
		}
		return listaJerarquia;
	}

	@Override
	public List<RelacionCoordinador> obtenerRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador, PageRequest page) {		
		Map<String, Object> out = null;
		List<RelacionCoordinador> listaRelacion = new ArrayList<>();
		
		this.paginacion = new Paginacion();
		paginacion.setPagina(page.getPagina());
		paginacion.setRegistros(page.getRegistros());
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_RELA_GERE_ALCA")
				.withProcedureName("PRC_RELA_GERE_ALCA_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidgeregnrl", OracleTypes.NUMBER),
						new SqlParameter("i_nidalcasgi", OracleTypes.NUMBER),
						new SqlParameter("i_nidcola", OracleTypes.NUMBER),
						new SqlParameter("i_nindsinalc", OracleTypes.NUMBER),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidgeregnrl", relacionCoordinador.getIdGerencia())
				.addValue("i_nidalcasgi", relacionCoordinador.getIdAlcance())
				.addValue("i_nidcola", relacionCoordinador.getIdCoordinador())
				.addValue("i_nindsinalc", (relacionCoordinador.getIndicadorSinAlcance()!= null?relacionCoordinador.getIndicadorSinAlcance():0))
				.addValue("i_npagina", page.getPagina())
				.addValue("i_nregistros", page.getRegistros());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				listaRelacion = mapearRelacionCoordinador(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return listaRelacion;
	}
	
	public List<RelacionCoordinador> mapearRelacionCoordinador(Map<String, Object> resultados){
		List<RelacionCoordinador> listaRelacion = new ArrayList<>();
		List<Map<String, Object>> listaResultado = (List<Map<String, Object>>)resultados.get("o_cursor");
		RelacionCoordinador relacion = null;
		for(Map<String, Object> map: listaResultado) {
			relacion = new RelacionCoordinador();
			if(map.get("n_idrelacoord") != null) {
				relacion.setIdRelacion(((BigDecimal)map.get("n_idrelacoord")).longValue());
			}
			if(map.get("n_idgeregnrl") != null) {
				relacion.setIdGerencia(((BigDecimal)map.get("n_idgeregnrl")).longValue());
			}
			if(map.get("v_desgeregnrl") != null) {
				relacion.setDescripcionGerencia((String)map.get("v_desgeregnrl"));
			}
			if(map.get("n_idalcasgi") != null) {
				relacion.setIdAlcance(((BigDecimal)map.get("n_idalcasgi")).longValue());
			}
			if(map.get("v_desalcasgi") != null) {
				relacion.setDescripcionAlcance((String)map.get("v_desalcasgi"));
			}
			if(map.get("n_idcola") != null) {
				relacion.setIdCoordinador(((BigDecimal)map.get("n_idcola")).longValue());
			}
			if(map.get("v_nroficha") != null) {
				relacion.setNroFicha(((BigDecimal)map.get("v_nroficha")).longValue());
			}
			if(map.get("v_nomcola") != null) {
				relacion.setNombreCompletoCoordinador((String)map.get("v_nomcola"));
			}
			if(map.get("n_inddocdig") != null) {
				relacion.setIndicadorDocumento(((BigDecimal)map.get("n_inddocdig")).longValue());
			}
			if(map.get("v_estaccpro") != null) {
				relacion.setEstadoRegistro((String)map.get("v_estaccpro"));
			}
			if(map.get("n_indsinalc") != null) {
				relacion.setIndicadorSinAlcance(((BigDecimal)map.get("n_indsinalc")).longValue());
			}
			listaRelacion.add(relacion);
			
			if(map.get("RESULT_COUNT")!= null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaRelacion;
	}
	
	@Override
	public RelacionCoordinador guardarRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador) {		
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_RELA_GERE_ALCA")
				.withProcedureName("PRC_RELA_GERE_ALCA_GUARDAR")
				.declareParameters(
						new SqlParameter("i_n_idgeregnrl", OracleTypes.NUMBER),
						new SqlParameter("i_n_idalcasgi", OracleTypes.NUMBER),
						new SqlParameter("i_n_idcola", OracleTypes.NUMBER),
						new SqlParameter("i_n_inddocdig", OracleTypes.NUMBER),
						new SqlParameter("i_a_v_usucre", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idgeregnrl", relacionCoordinador.getIdGerencia())
				.addValue("i_n_idalcasgi", relacionCoordinador.getIdAlcance())
				.addValue("i_n_idcola", relacionCoordinador.getIdCoordinador())
				.addValue("i_n_inddocdig", relacionCoordinador.getIndicadorDocumento())
				.addValue("i_a_v_usucre", relacionCoordinador.getDatosAuditoria().getUsuarioCreacion());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado != 0) {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return relacionCoordinador;
	}
	
	@Override
	public RelacionCoordinador actualizarRelacionGerenciaAlcance(Long idRelacion, RelacionCoordinador relacionCoordinador) {		
		Map<String, Object> out = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_RELA_GERE_ALCA")
				.withProcedureName("PRC_RELA_GERE_ALCA_UPDATE")
				.declareParameters(
						new SqlParameter("i_n_idrelacoord", OracleTypes.NUMBER),
						new SqlParameter("i_n_idcola", OracleTypes.NUMBER),
						new SqlParameter("i_n_inddocdig", OracleTypes.NUMBER),
						new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idrelacoord", idRelacion)
				.addValue("i_n_idcola", relacionCoordinador.getIdCoordinador())
				.addValue("i_n_inddocdig", relacionCoordinador.getIndicadorDocumento())
				.addValue("i_a_v_usumod", relacionCoordinador.getDatosAuditoria().getUsuarioModificacion());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado != 0) {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return relacionCoordinador;
	}
	
	@Override
	public boolean eliminarRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador) {		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_RELA_GERE_ALCA")
				.withProcedureName("PRC_RELA_GERE_ALCA_ELIMINAR")
				.declareParameters(
						new SqlParameter("i_n_idrelacoord",	OracleTypes.NUMBER),
						new SqlParameter("i_a_v_usumod", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idrelacoord", relacionCoordinador.getIdRelacion())
				.addValue("i_a_v_usumod", relacionCoordinador.getDatosAuditoria().getUsuarioModificacion());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			
			if(resultado == 0) {				
				return true;
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");
				this.error = new Error(resultado,mensaje,mensajeInterno);				
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en RelacionCoordinadorDAOImpl.eliminarRelacionGerenciaAlcance";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	
	@Override
	public List<RelacionCoordinador> obtenerDatosCoordinador(Long idGerencia, Long idAlcance) {		
		Map<String, Object> out = null;
		List<RelacionCoordinador> listaRelacion = new ArrayList<>();
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_RELA_GERE_ALCA")
				.withProcedureName("PRC_RELA_COORDINADOR_OBTENER")
				.declareParameters(
						new SqlParameter("i_nidgeregnrl", OracleTypes.NUMBER),
						new SqlParameter("i_nidalcasgi", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidgeregnrl", idGerencia)
				.addValue("i_nidalcasgi", idAlcance != 0 ? idAlcance : null);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				listaRelacion = mapearRelacionCoordinador(out);
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return listaRelacion;
	}
	@Override
	public Long obtenerDatosJefeEquipo(Long idArea) {		
		Map<String, Object> out = null;
		Integer numeroFicha = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_RELA_GERE_ALCA")
				.withProcedureName("PRC_JEFE_EQUIPO_OBTENER")
				.declareParameters(
						new SqlParameter("i_ncodarea", OracleTypes.NUMBER),
						new SqlOutParameter("o_nficha", OracleTypes.INTEGER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_ncodarea", idArea);
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {
				numeroFicha = (Integer) out.get("o_nficha");
			} else {
				String mensaje = (String)out.get("o_mensaje");
				String mensajeInterno = (String)out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer)out.get("o_retorno");
			String mensaje = (String)out.get("o_mensaje");
			String mensajeInterno = (String)out.get("o_sqlerrm");
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return numeroFicha.longValue();
	
	}
}
