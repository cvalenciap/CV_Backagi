package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;
import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.ICursoDAO;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.ConsideracionPlan;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.CursoArea;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.CursoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class CursoDAOImpl implements ICursoDAO {
	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private Error error;

	public JdbcTemplate getJdbc() {
		return jdbc;
	}

	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public SimpleJdbcCall getJdbcCall() {
		return jdbcCall;
	}

	public void setJdbcCall(SimpleJdbcCall jdbcCall) {
		this.jdbcCall = jdbcCall;
	}

	public Paginacion getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(Paginacion paginacion) {
		this.paginacion = paginacion;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	
	private static final Logger LOGGER = Logger.getLogger(CursoDAOImpl.class);	

	public List<Curso> obtenerCursos(CursoRequest cursoRequest, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Curso> lista = new ArrayList<>();

		this.paginacion = new Paginacion();
		this.error = null;
		this.paginacion.setPagina(pageRequest.getPagina());
		this.paginacion.setRegistros(pageRequest.getRegistros());

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_CURSO_OBTENER")
					.declareParameters(new SqlParameter("c_vnomcurs", OracleTypes.VARCHAR),
							new SqlParameter("c_vcodcurs", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("c_vnomcurs", cursoRequest.getNombreCurso())// .addValue("i_ndurcurs",
																														// cursoRequest.getDuracion())
					.addValue("c_vcodcurs", cursoRequest.getCodigoCurso())
					.addValue("i_npagina", pageRequest.getPagina())
					.addValue("i_nregistros", pageRequest.getRegistros());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearCurso(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.obtenerCursos";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return lista;
	}

	private List<Curso> mapearCurso(Map<String, Object> resultado) {
		List<Curso> listaCurso = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		Curso item = null;
		for (Map<String, Object> map : lista) {
			item = new Curso();
			item.setIdCurso(((BigDecimal) map.get("n_idcurs")).longValue());
			item.setCodigoCurso((String) map.get("v_codcurs"));
			item.setNombreCurso((String) map.get("v_nomcurs"));
			item.setTipoCurso(((BigDecimal) map.get("n_idtipocurs")).longValue());
//			item.setTipoCurso((String) map.get("n_idtipocurs"));
			item.setDuracion(((BigDecimal) map.get("n_durcurs")).longValue());
			item.setIndicadorSGI((String) map.get("v_indsgicurs"));
//			item.setDisponibilidad((String) map.get("n_discurs"));
			item.setDisponibilidad(((BigDecimal) map.get("n_discurs")).longValue());
			item.setSesiones(((BigDecimal) map.get("sesiones")).longValue());
			item.setDescDisp((String) map.get("descDisp"));
			item.setDescTipo((String) map.get("descTipo"));
//			item.setSesiones(((BigDecimal)map.get("n_dursesi")).longValue());
			
			List<Sesion> lstSes = obtenerCursoSesion(item.getIdCurso());
			item.setListaSesiones(lstSes);
			listaCurso.add(item);

			if (map.get("RESULT_COUNT") != null) {
				this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaCurso;
	}

	public List<Constante> listarTipoCursos(ConstanteRequest constanteRequest) {
		Map<String, Object> out = null;
		List<Constante> lista = new ArrayList<>();
		this.error=null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_TIPO_CURSO")
					.declareParameters(new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource();
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearTipoCurso(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.listarTipoCursos";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}		
		return lista;
	}

	private List<Constante> mapearTipoCurso(Map<String, Object> resultado) {
		List<Constante> listaTipoCurso = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");

		Constante item = null;
		for (Map<String, Object> map : lista) {
			item = new Constante();
			item.setIdconstante(((BigDecimal) map.get("n_idcons")).longValue());
			item.setV_valcons((String) map.get("v_valcons"));
			listaTipoCurso.add(item);

		}
		return listaTipoCurso;
	}

	public Curso registrarCurso(Curso curso) {
		Map<String, Object> out = null;
		this.error = null;
		boolean registro = false;
		Long idCurso = null;
		Curso cursoregistro = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_REGISTRAR_CURSO")
					.declareParameters(new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlParameter("i_vnomcurs", OracleTypes.VARCHAR),
							new SqlParameter("i_nidtipocurs", OracleTypes.NUMBER),
							new SqlParameter("i_ndurcurs", OracleTypes.NUMBER),
							new SqlParameter("i_vinsgicurs", OracleTypes.VARCHAR),
							new SqlParameter("i_vcodcurso", OracleTypes.VARCHAR),
							new SqlParameter("i_ndiscurs", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_nidcurs", OracleTypes.NUMBER));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidcurs", curso.getIdCurso())
					.addValue("i_vnomcurs", curso.getNombreCurso())
					.addValue("i_nidtipocurs", curso.getTipoCurso())
					.addValue("i_ndurcurs", curso.getDuracion())
					.addValue("i_vinsgicurs", curso.getIndicadorSGI())
					.addValue("i_vcodcurso", curso.getCodigoCurso())
					.addValue("i_ndiscurs", curso.getDisponibilidad())
					.addValue("i_avusucre", "AGI");

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				idCurso = ((BigDecimal) out.get("o_nidcurs")).longValue();
				System.out.println("IDCURSO :" + idCurso);
				curso.setIdCurso(idCurso);
				cursoregistro = curso;
				registro = true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
				registro = false;
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.registrarCurso";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
			registro = false;
		}
		return cursoregistro;
	}

	public boolean registrarSesion(Sesion sesion) {
		boolean registro = false;
		Map<String, Object> out = null;
		this.error = null;
		System.out.println("SESION IDCURSO : " + sesion.getIdCurso());
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("").withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_REGISTRAR_SESION")
					.declareParameters(
							new SqlParameter("i_dfecsesi", OracleTypes.DATE),
							new SqlParameter("i_nidsesi", OracleTypes.NUMBER),
							new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlParameter("i_ndursesi", OracleTypes.NUMBER),
							new SqlParameter("i_ndissesi", OracleTypes.NUMBER),
							new SqlParameter("i_dhorinisesi", OracleTypes.VARCHAR),
							new SqlParameter("i_dhorfinsesi", OracleTypes.VARCHAR),
							new SqlParameter("i_vnomsesi", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_nidsesi", OracleTypes.NUMBER));
			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidsesi", sesion.getIdSesion())
					.addValue("i_dfecsesi", sesion.getFechaSesion())
					.addValue("i_nidcurs", sesion.getIdCurso())
					.addValue("i_ndursesi", sesion.getDuracion())
					.addValue("i_ndissesi", sesion.getDisponibilidad())
					.addValue("i_dhorinisesi", sesion.getHoraInicio())
					.addValue("i_dhorfinsesi", sesion.getHoraFin())
					.addValue("i_vnomsesi", sesion.getNombreSesion())
					.addValue("i_avusucre", "AGI");

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.registrarSesion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registro;
	}

	@Transactional
	public Curso registrarDatosCurso(Curso curso) {
		Curso regCurso = null;

		try {
			regCurso = this.registrarCurso(curso);
			if (regCurso.getListaSesiones().size() > 0) {
				for (Sesion sesion : curso.getListaSesiones()) {
					boolean registro = false;
					sesion.setIdCurso(regCurso.getIdCurso());
					registro = this.registrarSesion(sesion);
					if (!registro) {
						break;
					}
				}
			}
			if(regCurso.getListaAreas().size()>0) {
				for(CursoArea cursoArea : curso.getListaAreas()) {
					boolean registro = false;
					cursoArea.setIdCurso(regCurso.getIdCurso());
					cursoArea.setIdArea(regCurso.getIdArea());
					registro = this.registrarEquipoCurso(cursoArea);
					if (!registro) {
						break;
					} else {
						if(cursoArea.getDisponibilidad()==null || cursoArea.getDisponibilidad() != 1) {
							Long dispo = (long) 1;
							cursoArea.setDisponibilidad(dispo);
							registro = this.registrarEquipoCurso(cursoArea);
						}
						
					}
				}
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.registrarDatosCurso";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return regCurso;
	}

	public Curso obtenerCursoId(Long idCurso) {

		Curso curso = null;
		Map<String, Object> out = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_OBTENER_CURSO_ID")
					.declareParameters(new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcurs", idCurso);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				curso = mapearCursoId(out);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.obtenerCursoSesionId";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return curso;
	}

	public Curso mapearCursoId(Map<String, Object> resultado) {
		Curso curso = null;
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		Curso item = null;
		for (Map<String, Object> map : lista) {
			item = new Curso();
			item.setIdCurso(((BigDecimal) map.get("n_idcurs")).longValue());
			item.setNombreCurso((String) map.get("v_nomcurs"));
			item.setDuracion(((BigDecimal) map.get("n_durcurs")).longValue());
			item.setIndicadorSGI((String) map.get("v_indsgicurs"));
//			item.setDisponibilidad((String) map.get("n_discurs"));
			item.setDisponibilidad(((BigDecimal) map.get("n_discurs")).longValue());
			item.setCodigoCurso((String) map.get("v_codcurs"));
			item.setTipoCurso(((BigDecimal) map.get("n_idtipocurs")).longValue());
//			item.setTipoCurso((String) map.get("n_idtipocurs"));
			curso = item;
		}
		return curso;
	}

	public List<Sesion> obtenerCursoSesion(Long idCurso) {

		List<Sesion> lista = new ArrayList<>();
		Sesion sesion = null;
		Map<String, Object> out = null;
		this.error = null;
		Date fecIni = null;
		Date fecFin = null;
		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_OBTENER_SESION_CURSO")
					.declareParameters(new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_fechaInicio", OracleTypes.DATE),
							new SqlOutParameter("o_fechaFin", OracleTypes.DATE));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcurs", idCurso);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearCursoSesionId(out);
				for (Sesion ses : lista) {
					fecIni = ((Date) out.get("o_fechaInicio"));
					fecFin = ((Date) out.get("o_fechaFin"));
					ses.setFechaInicio(fecIni);
					ses.setFechaFin(fecFin);
				}
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.obtenerCursoSesion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return lista;
	}

	public List<Sesion> mapearCursoSesionId(Map<String, Object> resultado) {
		List<Sesion> listaSesion = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		Sesion item = null;
		for (Map<String, Object> map : lista) {
			item = new Sesion();
			item.setIdCurso(((BigDecimal) map.get("n_idcurs")).longValue());
			item.setIdSesion(((BigDecimal) map.get("n_idsesi")).longValue());
			item.setNombreSesion((String) map.get("v_nomsesi"));
			item.setDuracion(((BigDecimal) map.get("n_dursesi")).longValue());
			item.setDisponibilidad(((BigDecimal) map.get("n_dissesi")).longValue());
			item.setDescDisp((String) map.get("descDisp"));
			item.setFechaSesion((Date)map.get("fechaSesion"));
			item.setFechaInicio((Date)map.get("o_fechaInicio"));
			item.setFechaFin((Date)map.get("o_fechaFin"));
			item.setHoraInicio((String) map.get("horaInicio"));
			item.setHoraFin((String) map.get("horaFin"));
			listaSesion.add(item);
		}
		return listaSesion;
	}

	public Curso obtenerDatosCursoSesion(Long idCurso) {

		Curso curso = null;
		List<Sesion> lstSesion = null;
		List<CursoArea> lstCursoEquipo = null;
		try {
			curso = obtenerCursoId(idCurso);
			lstSesion = obtenerCursoSesion(idCurso);
			if (lstSesion.size() > 0) {
				curso.setListaSesiones(lstSesion);
			}
			lstCursoEquipo=obtenerCursoEquipo(idCurso);
			if(lstCursoEquipo.size()>0) {
				curso.setListaAreas(lstCursoEquipo);
			}
			
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.obtenerDatosCursoSesion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return curso;
	}

	public Map<String, Object> eliminarCurso(Long idCurso) {
		Map<String, Object> out = null;
		this.error=null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_ELIMINAR_CURSO")
					.declareParameters(new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("O_Sqlerrm", OracleTypes.VARCHAR));

			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("i_nidcurs", idCurso);

			out = this.jdbcCall.execute(paraMap);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO PRC_CARGA_CURSOS_OBLIGA_LIBRE");
		}
		return out;
	}

	@Transactional
	public Curso actualizarDatosCurso(Curso curso) {
		Curso regCurso = null;

		try {
			regCurso = this.registrarCurso(curso);
			if (regCurso.getListaSesiones().size() > 0) {
				for (Sesion sesion : curso.getListaSesiones()) {
					boolean registro = false;
					sesion.setIdCurso(regCurso.getIdCurso());
					if(sesion.getEstadoRegistro()==2) {
						registro = this.eliminarSesion(sesion);
						if (!registro) {
							break;
						}
					}else {
						registro = this.registrarSesion(sesion);
					}
				}
			}
			if (regCurso.getListaAreas().size() > 0) {
				for(CursoArea cursoArea : curso.getListaAreas()) {
					boolean registro = false;
					cursoArea.setIdCurso(regCurso.getIdCurso());
					if(cursoArea.getEstadoRegistro() == 2) {
						registro = this.eliminarCursoEquipo(cursoArea);
						if (!registro) {
							break;
						}
					}else {
						if(cursoArea.getDisponibilidad()==null || cursoArea.getDisponibilidad() != 1) {
							Long dispo = (long) 1;
							cursoArea.setDisponibilidad(dispo);
							registro = this.registrarEquipoCurso(cursoArea);
						}
						
					}
				}
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.actualizarDatosCurso";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return regCurso;
	}

	public boolean eliminarSesion(Sesion sesion) {
		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_ELIMINAR_SESION")
				.declareParameters(new SqlParameter("i_estReg", OracleTypes.NUMBER),
						new SqlParameter("i_nidsesi", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("i_estReg", sesion.getEstadoRegistro())
				.addValue("i_nidsesi", sesion.getIdSesion());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {
				
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);

				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.eliminarSesion()";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	
	public boolean registrarEquipoCurso(CursoArea cursoArea) {
		
		boolean registro = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("").withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_REGISTRAR_CURSO_AREA")
					.declareParameters(
							new SqlParameter("i_estadoRegistro", OracleTypes.NUMBER),
							new SqlParameter("i_nidcurso", OracleTypes.NUMBER),
							new SqlParameter("i_ncodarea", OracleTypes.NUMBER),
							new SqlParameter("i_ndiscurequ", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
			SqlParameterSource in = new MapSqlParameterSource().addValue("i_estadoRegistro", cursoArea.getEstadoRegistro())
					.addValue("i_nidcurso", cursoArea.getIdCurso()).addValue("i_ncodarea", cursoArea.getIdArea())
					.addValue("i_ndiscurequ", cursoArea.getDisponibilidad()).addValue("i_avusucre", "AGI");

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.registrarEquipoCurso";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return registro;
		
	}
	
	public boolean eliminarCursoEquipo(CursoArea cursoArea) {
		System.out.println("IDCURSO :" + cursoArea.getIdCurso());
		System.out.println("IDAREA :" + cursoArea.getIdArea());		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_ELIMINAR_CURSO_EQUIPO")
				.declareParameters(new SqlParameter("i_estadoRegistro", OracleTypes.NUMBER),
						new SqlParameter("i_nidcurso", OracleTypes.VARCHAR),
						new SqlParameter("i_ncodarea", OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_estadoRegistro", cursoArea.getEstadoRegistro())
				.addValue("i_nidcurso", cursoArea.getIdCurso())
				.addValue("i_ncodarea", cursoArea.getIdArea());

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {				
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);				
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.eliminarCursoEquipo()";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	
	public List<CursoArea> obtenerCursoEquipo(Long idCurso) {
		List<CursoArea> lista = new ArrayList<>();
		Sesion sesion = null;
		Map<String, Object> out = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_OBTENER_EQUIPO_CURSO")
					.declareParameters(new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcurs", idCurso);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearCursoEquipoId(out);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.obtenerCursoEquipo";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}

		return lista;
	}
	
	public List<CursoArea> mapearCursoEquipoId(Map<String, Object> resultado) {
		List<CursoArea> listaCursoArea = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		CursoArea item = null;
		for (Map<String, Object> map : lista) {
			item = new CursoArea();
			item.setIdCurso(((BigDecimal) map.get("n_idcurs")).longValue());
			item.setIdArea(((BigDecimal) map.get("n_idequi")).longValue());
			item.setCodigoCurso((String) map.get("v_codcurs"));
			item.setDisponibilidad(((BigDecimal) map.get("n_discurequ")).longValue());
			item.setDescripcion((String)map.get("vdescripcion"));
			item.setAbreviatura((String)map.get("vabreviatura"));
			listaCursoArea.add(item);
		}
		return listaCursoArea;
	}
	
	public boolean modificarDatosSesion(Sesion sesion) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_MODIFICAR_SESION")
				.declareParameters(
						new SqlParameter("i_dfecsesi", OracleTypes.DATE),
						new SqlParameter("i_nidsesi", OracleTypes.NUMBER),
						new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
						new SqlParameter("i_ndursesi", OracleTypes.NUMBER),
						new SqlParameter("i_ndissesi", OracleTypes.NUMBER),
						new SqlParameter("i_dhorinisesi", OracleTypes.VARCHAR),
						new SqlParameter("i_dhorfinsesi", OracleTypes.VARCHAR),
						new SqlParameter("i_vnomsesi", OracleTypes.VARCHAR),
						new SqlParameter("i_idaula", OracleTypes.NUMBER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_nidsesi", sesion.getIdSesion())
				.addValue("i_dfecsesi", sesion.getFechaSesion())
				.addValue("i_nidcurs", sesion.getIdCurso())
				.addValue("i_ndursesi", sesion.getDuracion())
				.addValue("i_ndissesi", sesion.getDisponibilidad())
				.addValue("i_dhorinisesi", sesion.getHoraInicio())
				.addValue("i_dhorfinsesi", sesion.getHoraFin())
				.addValue("i_vnomsesi", sesion.getNombreSesion())
				.addValue("i_idaula", sesion.getIdAula())
				.addValue("i_avusumod", "AGI");

		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");

			if (resultado == 0) {				
				return true;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				this.error = new Error(resultado, mensaje, mensajeInterno);				
				return false;
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CursoDAOImpl.eliminarCursoEquipo()";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}
	
}
