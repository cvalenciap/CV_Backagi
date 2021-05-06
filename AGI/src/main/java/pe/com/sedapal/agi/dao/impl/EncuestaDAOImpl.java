package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.IEncuestaDAO;
import pe.com.sedapal.agi.model.DetEncuesta;
import pe.com.sedapal.agi.model.Encuesta;
import pe.com.sedapal.agi.model.Encuesta;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.request_objects.DetEncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.EncuestaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

@Repository
public class EncuestaDAOImpl implements IEncuestaDAO {
	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

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

	@Override
	public List<Encuesta> obtenerEncuesta(EncuestaRequest constanteRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Encuesta> lista = new ArrayList<>();

		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());

		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_ENCUESTA_OBTENER")
				.declareParameters(new SqlParameter("i_nidcurs ", OracleTypes.NUMBER),
						new SqlParameter("i_nidencu ", OracleTypes.NUMBER),
						new SqlParameter("i_vcodencu", OracleTypes.VARCHAR),
						new SqlParameter("i_vnomencu", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidcurs", constanteRequest.getInidcurs())
				.addValue("i_nidencu", constanteRequest.getInidencu())
				.addValue("i_vcodencu", constanteRequest.getIvcodencu())
				.addValue("i_vnomencu", constanteRequest.getIvnomencu())
				.addValue("i_npagina", pageRequest.getPagina())
				.addValue("i_nregistros", pageRequest.getRegistros());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = mapearEncuestas(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return lista;
	}
	
	@Override
	public Encuesta obtenerListaEncuestaXId(Long idCurso) {
		Map<String, Object> out = null;
		Encuesta lista = new Encuesta();

		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_ENCUESTA_OBTENER_LIST")
				.declareParameters(
						new SqlParameter("i_nidencu ", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidencu", idCurso);

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = mapearEncuestaXId(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return lista;
	}
	
	public List<Encuesta> mapearEncuestas(Map<String, Object> resultados) {

		List<Encuesta> listaEncuesta = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		Encuesta item = null;

		for (Map<String, Object> map : lista) {
			item = new Encuesta();

			if (map.get("N_IDCURS") != null) item.setNidcurs(((BigDecimal) map.get("N_IDCURS")).longValue());
			if (map.get("V_DES_CUR") != null) item.setVdescur((String) map.get("V_DES_CUR"));
			if (map.get("N_IDENCU") != null) item.setNidencu(((BigDecimal) map.get("N_IDENCU")).longValue());
			if (map.get("V_CODENCU") != null) item.setVcodencu((String) map.get("V_CODENCU"));
			if (map.get("V_NOMENCU") != null) item.setVnomencu((String) map.get("V_NOMENCU"));
			if (map.get("A_V_USUCRE") != null) item.setAvusucre((String) map.get("A_V_USUCRE"));
			if (map.get("A_D_FECCRE") != null) item.setAdfeccre((Date) map.get("A_D_FECCRE"));
			if (map.get("A_V_USUMOD") != null) item.setAvusumod((String) map.get("A_V_USUMOD"));
			if (map.get("A_D_FECMOD") != null) item.setAdfecmod((Date) map.get("A_D_FECMOD"));
			if (map.get("A_V_NOMPRG") != null) item.setAvnomprg((String) map.get("A_V_NOMPRG"));
			if (map.get("N_DISENCU") != null) item.setNdisencu(((BigDecimal) map.get("N_DISENCU")).longValue());

			listaEncuesta.add(item);

			if (map.get("RESULT_COUNT") != null) {
				this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
			}
		}

		return listaEncuesta;
	}

	private List<Encuesta> mapearEncuesta(Map<String, Object> resultados) {
		Encuesta item = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		List<Encuesta> listaEncuesta = new ArrayList<>();
		for (Map<String, Object> map : lista) {

			item = new Encuesta();

			if (map.get("N_IDCURS") != null)  item.setNidcurs(((BigDecimal) map.get("N_IDCURS")).longValue());
			if (map.get("V_DES_CUR") != null) item.setVdescur((String) map.get("V_DES_CUR"));
			if (map.get("N_IDENCU") != null)  item.setNidencu(((BigDecimal) map.get("N_IDENCU")).longValue());
			if (map.get("V_CODENCU") != null) item.setVcodencu((String) map.get("V_CODENCU"));
			if (map.get("V_NOMENCU") != null) item.setVnomencu((String) map.get("V_NOMENCU"));
			if (map.get("A_V_USUCRE") != null) item.setAvusucre((String) map.get("A_V_USUCRE"));
			if (map.get("A_D_FECCRE") != null) item.setAdfeccre((Date) map.get("A_D_FECCRE"));
			if (map.get("A_V_USUMOD") != null) item.setAvusumod((String) map.get("A_V_USUMOD"));
			if (map.get("A_D_FECMOD") != null) item.setAdfecmod((Date) map.get("A_D_FECMOD"));
			if (map.get("A_V_NOMPRG") != null) item.setAvnomprg((String) map.get("A_V_NOMPRG"));
			if (map.get("N_DISENCU") != null) item.setNdisencu(((BigDecimal) map.get("N_DISENCU")).longValue());

			listaEncuesta.add(item);

			if (map.get("RESULT_COUNT") != null) {
				this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaEncuesta;
	}
	
	private Encuesta mapearEncuestaXId(Map<String, Object> resultados) {

		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		Encuesta listaEncuesta = new Encuesta();
		for (Map<String, Object> map : lista) {

			if (map.get("N_IDCURS") != null)  listaEncuesta.setNidcurs(((BigDecimal) map.get("N_IDCURS")).longValue());
			if (map.get("V_DES_CUR") != null) listaEncuesta.setVdescur((String) map.get("V_DES_CUR"));
			if (map.get("N_IDENCU") != null)  listaEncuesta.setNidencu(((BigDecimal) map.get("N_IDENCU")).longValue());
			if (map.get("V_CODENCU") != null) listaEncuesta.setVcodencu((String) map.get("V_CODENCU"));
			if (map.get("V_NOMENCU") != null) listaEncuesta.setVnomencu((String) map.get("V_NOMENCU"));
			if (map.get("A_V_USUCRE") != null) listaEncuesta.setAvusucre((String) map.get("A_V_USUCRE"));
			if (map.get("A_D_FECCRE") != null) listaEncuesta.setAdfeccre((Date) map.get("A_D_FECCRE"));
			if (map.get("A_V_USUMOD") != null) listaEncuesta.setAvusumod((String) map.get("A_V_USUMOD"));
			if (map.get("A_D_FECMOD") != null) listaEncuesta.setAdfecmod((Date) map.get("A_D_FECMOD"));
			if (map.get("A_V_NOMPRG") != null) listaEncuesta.setAvnomprg((String) map.get("A_V_NOMPRG"));
			if (map.get("V_COD_CUR") != null) listaEncuesta.setV_cod_cur((String) map.get("V_COD_CUR"));
			if (map.get("N_DISENCU") != null) listaEncuesta.setNdisencu(((BigDecimal) map.get("N_DISENCU")).longValue());

		}
		return listaEncuesta;
	}
	
	@Override
	public List<DetEncuesta> obtenerDetEncuesta(DetEncuestaRequest detEncuestaRequest, PageRequest pageRequest) {

		Map<String, Object> out = null;
		List<DetEncuesta> lista = new ArrayList<>();

		this.paginacion = new Paginacion();
		paginacion.setPagina(pageRequest.getPagina());
		paginacion.setRegistros(pageRequest.getRegistros());
		try {
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_DET_ENCUESTA_OBTENER")
				.declareParameters(
						new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),
						new SqlParameter("i_nidencu", OracleTypes.NUMBER),
						new SqlParameter("i_vdespre", OracleTypes.VARCHAR),
						new SqlParameter("i_vcodetaencu", OracleTypes.VARCHAR),
						new SqlParameter("i_npagina", OracleTypes.NUMBER),
						new SqlParameter("i_nregistros", OracleTypes.NUMBER),
						new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_niddetaencu", detEncuestaRequest.getIniddetaencu())
				.addValue("i_nidencu", detEncuestaRequest.getInidencu())
				.addValue("i_vdespre", detEncuestaRequest.getIvdespre())
				.addValue("i_vcodetaencu", detEncuestaRequest.getIvcodetaencu())
				.addValue("i_npagina", pageRequest.getPagina())
				.addValue("i_nregistros", pageRequest.getRegistros());

		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = mapearDetEncuestas(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return lista;

	}
	
    @Override
	public List<DetEncuesta> obtenerListaEncuestaDetXEncu(Long idEncu) {

		Map<String, Object> out = null;
		List<DetEncuesta> listaDetEncuesta = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_DET_ENCUESTA_OBTENER_LIST")
					.declareParameters(
							new SqlParameter("i_nidencu", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidencu", idEncu);

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				listaDetEncuesta = new ArrayList<>();
				listaDetEncuesta = mapearDetEncuesta(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en EncuestaDAOImpl.obtenerListaEncuestaDetXEncu";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return listaDetEncuesta;

	}

    @Override
	public Encuesta obtenerListaEncuestaAll(Long idCurso) {
		Encuesta listaEncuesta = this.obtenerListaEncuestaXId(idCurso);
		listaEncuesta.setListaDetEncuesta(this.obtenerListaEncuestaDetXEncu(idCurso));
		return listaEncuesta;
	}

	
	public List<DetEncuesta> mapearDetEncuestas(Map<String, Object> resultados) {

		List<DetEncuesta> listaDetEncuesta = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		DetEncuesta item = null;

		for (Map<String, Object> map : lista) {
			item = new DetEncuesta();

			if (map.get("N_IDDETAENCU") != null) item.setNiddetaencu(((BigDecimal) map.get("N_IDDETAENCU")).longValue());
			if (map.get("V_CODETAENCU") != null) item.setVcodetaencu((String) map.get("V_CODETAENCU"));
			if (map.get("V_DESPRE") != null) item.setVdespre((String) map.get("V_DESPRE"));
			if (map.get("N_IDENCU") != null) item.setNidencu(((BigDecimal) map.get("N_IDENCU")).longValue());
			if (map.get("N_DES_CUR") != null) item.setN_des_cur(((BigDecimal) map.get("N_DES_CUR")).longValue());
			if (map.get("N_DISDETENC") != null) item.setNdisdetenc(((BigDecimal) map.get("N_DISDETENC")).longValue());
			if (map.get("A_V_USUCRE") != null) item.setAvusucre((String) map.get("A_V_USUCRE"));
			if (map.get("A_D_FECCRE") != null) item.setAdfeccre((Date) map.get("A_D_FECCRE"));
			if (map.get("A_V_USUMOD") != null) item.setAvusumod((String) map.get("A_V_USUMOD"));
			if (map.get("A_D_FECMOD") != null) item.setAdfecmod((Date) map.get("A_D_FECMOD"));
			if (map.get("A_V_NOMPRG") != null) item.setAv_nomprg((String) map.get("A_V_NOMPRG"));

			listaDetEncuesta.add(item);

			if (map.get("RESULT_COUNT") != null) {
				this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
			}
		}

		return listaDetEncuesta;
	}

	private List<DetEncuesta> mapearDetEncuesta(Map<String, Object> resultados) {
		DetEncuesta item = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		List<DetEncuesta> listaDetEncuesta = new ArrayList<>();
		for (Map<String, Object> map : lista) {

			item = new DetEncuesta();

			if (map.get("N_IDDETAENCU") != null) item.setNiddetaencu(((BigDecimal) map.get("N_IDDETAENCU")).longValue());
			if (map.get("V_CODETAENCU") != null) item.setVcodetaencu((String) map.get("V_CODETAENCU"));
			if (map.get("V_DESPRE") != null) item.setVdespre((String) map.get("V_DESPRE"));
			if (map.get("N_IDENCU") != null) item.setNidencu(((BigDecimal) map.get("N_IDENCU")).longValue());
			if (map.get("N_DES_CUR") != null) item.setN_des_cur(((BigDecimal) map.get("N_DES_CUR")).longValue());
			if (map.get("N_DISDETENC") != null ) item.setNdisdetenc(((BigDecimal) map.get("N_DISDETENC")).longValue());
			if (map.get("A_V_USUCRE") != null) item.setAvusucre((String) map.get("A_V_USUCRE"));
			if (map.get("A_D_FECCRE") != null) item.setAdfeccre((Date) map.get("A_D_FECCRE"));
			if (map.get("A_V_USUMOD") != null) item.setAvusumod((String) map.get("A_V_USUMOD"));
			if (map.get("A_D_FECMOD") != null) item.setAdfecmod((Date) map.get("A_D_FECMOD"));
			if (map.get("A_V_NOMPRG") != null) item.setAv_nomprg((String) map.get("A_V_NOMPRG"));

			listaDetEncuesta.add(item);

			if (map.get("RESULT_COUNT") != null) {
				this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaDetEncuesta;
	}
	
	@Override
	public Encuesta insertarEncuesta(Encuesta encuesta) {
		Encuesta registro = null;
		Map<String, Object> out = null;
		List<Encuesta> lista = new ArrayList<>();
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_ENCUESTA_GUARDAR")
					.declareParameters(
							new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlParameter("i_nidencu", OracleTypes.NUMBER),
							new SqlParameter("i_ndisencu", OracleTypes.NUMBER),
							new SqlParameter("i_vcodencu", OracleTypes.VARCHAR),
							new SqlParameter("i_vnomencu", OracleTypes.VARCHAR),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidcurs", encuesta.getNidcurs())
					.addValue("i_nidencu", null)
					.addValue("i_ndisencu", encuesta.getNdisencu())
					.addValue("i_vcodencu", encuesta.getVcodencu())
					.addValue("i_vnomencu", encuesta.getVnomencu())
					.addValue("i_vusuario", encuesta.getAvusumod())
					.addValue("i_vusuario", "AGI");
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");  
			
			if (resultado == 0) {
				lista = mapearEncuesta(out);
				registro = lista.get(0);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				registro = null;
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en EncuestaDAOImpl.insertarEncuesta";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			System.out.println("Error al guardar la Encuesta" + this.error.getMensaje());
		}

		return registro;

	}
	
	@Override
	public DetEncuesta insertarDetEncuesta(DetEncuesta detEncuesta, Long idEncu) {
		DetEncuesta registro = null;
		Map<String, Object> out = null;
		List<DetEncuesta> lista = new ArrayList<>();
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_DET_ENCUESTA_GUARDAR")
					.declareParameters(
							new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),
							new SqlParameter("i_nidencu", OracleTypes.NUMBER),
							new SqlParameter("i_vdespre", OracleTypes.VARCHAR),
							new SqlParameter("i_vcodetaencu", OracleTypes.VARCHAR),
							new SqlParameter("i_ndisdetenc", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_niddetaencu", null)
					.addValue("i_nidencu", idEncu)
					.addValue("i_vdespre", detEncuesta.getVdespre())
					.addValue("i_vcodetaencu", detEncuesta.getVcodetaencu())
					.addValue("i_ndisdetenc", detEncuesta.getNdisdetenc())
					.addValue("i_vusuario", "AGI");
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");    

			if (resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>) out.get("o_cursor");
				
				for(Map<String, Object> map : listado) {
                	registro = new DetEncuesta();
                    if(map.get("N_IDDETAENCU")!=null) registro.setNiddetaencu(((BigDecimal)map.get("N_IDDETAENCU")).longValue());     
				}

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				registro = null;
			}

		} catch (Exception e) {
            Integer resultado = 1;
            String mensaje = "Error en EncuestaDAOImpl.insertarDetEncuesta";
            String mensajeInterno = e.getMessage();
            this.error                        = new Error(resultado,mensaje,mensajeInterno);
            System.out.println("Error al guardar la Encuesta de Detalle" + this.error.getMensaje());
		}
		return registro;

		}
	
	@Override
	@Transactional
	public Encuesta registrarEncuesta(Encuesta encuesta) {
		Encuesta regEncuesta = new Encuesta();

		try {
			regEncuesta = this.insertarEncuesta(encuesta);
			
			if (regEncuesta==null) {
				return null;
			}
			
			if (encuesta.getListaDetEncuesta() != null) {
				List<DetEncuesta> lista = new ArrayList<>();
				for (DetEncuesta detEncuesta : encuesta.getListaDetEncuesta()) {
					DetEncuesta temporal = this.insertarDetEncuesta(detEncuesta,regEncuesta.getNidencu());
					if (temporal == null) {
						return null;
					}
					
					lista.add(temporal);
				}
				
				regEncuesta.setListaDetEncuesta(lista);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en EncuestaDAOImpl.registrarEncuesta";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return regEncuesta;

	}
	
	@Override
	public Encuesta actualizarEncuesta(Long id, Encuesta encuesta) {
		Encuesta registro = null;
		Map<String, Object> out = null;
		List<Encuesta> lista = new ArrayList<>();
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_ENCUESTA_GUARDAR")
					.declareParameters(
							new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlParameter("i_nidencu", OracleTypes.NUMBER),
							new SqlParameter("i_ndisencu", OracleTypes.NUMBER),
							new SqlParameter("i_vcodencu", OracleTypes.VARCHAR),
							new SqlParameter("i_vnomencu", OracleTypes.VARCHAR),
							new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidcurs", encuesta.getNidcurs())
					.addValue("i_nidencu", id)
					.addValue("i_ndisencu", encuesta.getNdisencu())
					.addValue("i_vcodencu", encuesta.getVcodencu())
					.addValue("i_vnomencu", encuesta.getVnomencu())
					.addValue("i_vusuario", encuesta.getAvusumod())
					.addValue("i_vusuario", "AGI");
			// out = this.jdbcCall.execute(in);
			// lista = mapearEncuesta(out);
			// return lista.get(0);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>) out.get("o_cursor");

				for (Map<String, Object> map : listado) {
					registro = new Encuesta();
					if (map.get("N_IDENCU") != null) registro.setNidencu(((BigDecimal) map.get("N_IDENCU")).longValue());
				}

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				registro = null;
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en EncuestaDAOImpl.actualizarEncuesta";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			System.out.println("Error al actualizar la Encuesta" + this.error.getMensaje());
		}
		return registro;

	}
	
	@Override
	public DetEncuesta actualizarDetEncuesta(Long id, Long idEncu, DetEncuesta detEncuesta) {
		DetEncuesta registro = null;
		Map<String, Object> out = null;
		List<DetEncuesta> lista = new ArrayList<>();
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_DET_ENCUESTA_GUARDAR")
					.declareParameters(
							new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),
							new SqlParameter("i_nidencu", OracleTypes.NUMBER),
							new SqlParameter("i_vdespre", OracleTypes.VARCHAR),
							new SqlParameter("i_vcodetaencu", OracleTypes.VARCHAR),
							new SqlParameter("i_ndisdetenc", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_niddetaencu", id)
					.addValue("i_nidencu", idEncu)
					.addValue("i_vdespre", detEncuesta.getVdespre())
					.addValue("i_vcodetaencu", detEncuesta.getVcodetaencu())
					.addValue("i_ndisdetenc", detEncuesta.getNdisdetenc())
					.addValue("i_vusuario", "AGI");

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				List<Map<String, Object>> listado = (List<Map<String, Object>>) out.get("o_cursor");

				for (Map<String, Object> map : listado) {
					registro = new DetEncuesta();
					if (map.get("N_IDDETAENCU") != null) registro.setNiddetaencu(((BigDecimal) map.get("N_IDDETAENCU")).longValue());
				}

			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				registro = null;
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en EncuestaDAOImpl.actualizarDetEncuesta";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			System.out.println("Error al actualizar la Encuesta de Detalle" + this.error.getMensaje());
		}
		return registro;

	}
	
	@Override
	@Transactional
	public Encuesta actualizarEncuestaAll(Long id,Encuesta encuesta) {
		Encuesta regEncuesta = new Encuesta();

		try {
			regEncuesta = this.actualizarEncuesta(id, encuesta);
			
			if (regEncuesta==null) {
				return null;
			}
			
			if (encuesta.getListaDetEncuesta() != null) {
				List<DetEncuesta> lista = new ArrayList<>();
				for (DetEncuesta detEncuesta : encuesta.getListaDetEncuesta()) {
					DetEncuesta temporal = this.actualizarDetEncuesta(detEncuesta.getNiddetaencu(),regEncuesta.getNidencu(),detEncuesta);
					if (temporal == null) {
						return null;
					}
					
					lista.add(temporal);
				}
				
				regEncuesta.setListaDetEncuesta(lista);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en EncuestaDAOImpl.actualizarEncuestaAll";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return regEncuesta;

	}
	
	

	@Override
	public Boolean eliminarEncuesta(Long id) {
		List<Encuesta> lista = new ArrayList<>();
		Map<String, Object> out = null;
		if (id == null) {
			id = 0L;
		}
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_ENCUESTA_ELIMINAR")
				.declareParameters(new SqlParameter("i_nidencu", OracleTypes.NUMBER),
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidencu", id).addValue("i_vusuario", "AGI");
		out = this.jdbcCall.execute(in);
		Boolean retorno = true;
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				retorno = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				retorno = false;
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
			retorno = false;
		}
		return retorno;
	}

	@Override
	public Boolean eliminarDetEncuesta(Long id) {
		List<DetEncuesta> lista = new ArrayList<>();
		Map<String, Object> out = null;
		if (id == null) {
			id = 0L;
		}
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_DET_ENCUESTA_ELIMINAR")
				.declareParameters(
						new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),
						new SqlParameter("i_nidencu", OracleTypes.NUMBER),						
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_niddetaencu", null)
				.addValue("i_nidencu", id)
				.addValue("i_vusuario","AGI");
		out = this.jdbcCall.execute(in);
		Boolean retorno = true;
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				retorno = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				retorno = false;
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
			retorno = false;
		}
		return retorno;
	}

	@Override
	@Transactional
	public Boolean eliminarEncuestaAll(Long id) {

		Boolean regEncuesta = false;
		Boolean regEncuestaDet = false;

		try {
			regEncuesta = this.eliminarEncuesta(id);
			
			if (regEncuesta==false) {
				return false;
			} else {
				regEncuestaDet = eliminarDetEncuesta(id);
				
				if (regEncuestaDet==false) {
					return false;
				}
			}
			
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en EncuestaDAOImpl.eliminarEncuestaAll";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return regEncuesta;

	}

	@Override
	public Boolean eliminarEncuestaDet(Long id) {
		List<DetEncuesta> lista = new ArrayList<>();
		Map<String, Object> out = null;
		if (id == null) {
			id = 0L;
		}
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_DET_ENCUESTA_ELIMINAR")
				.declareParameters(
						new SqlParameter("i_niddetaencu", OracleTypes.NUMBER),
						new SqlParameter("i_nidencu", OracleTypes.NUMBER),						
						new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_niddetaencu", id)
				.addValue("i_nidencu", null)
				.addValue("i_vusuario","AGI");
		out = this.jdbcCall.execute(in);
		Boolean retorno = true;
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				retorno = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);
				retorno = false;
			}
		} catch (Exception e) {
			Integer resultado = (Integer) out.get("o_retorno");
			String mensaje = (String) out.get("o_mensaje");
			String mensajeInterno = (String) out.get("o_sqlerrm");
			this.error = new Error(resultado, mensaje, mensajeInterno);
			retorno = false;
		}
		return retorno;
	}





}
