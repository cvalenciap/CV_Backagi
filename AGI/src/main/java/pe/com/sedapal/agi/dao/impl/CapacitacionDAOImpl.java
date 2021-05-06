package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.model.CapacitacionColaborador;
import pe.com.sedapal.agi.model.CapacitacionDocumentos;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.ICapacitacionDAO;
import pe.com.sedapal.agi.dao.ICursoDAO;
import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Capacitacion;
import pe.com.sedapal.agi.model.CapacitacionDocInterno;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.UploadResponse;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UConvierteFecha;

@Repository
public class CapacitacionDAOImpl implements ICapacitacionDAO {
	@Autowired
	private JdbcTemplate jdbc;
	private SimpleJdbcCall jdbcCall;
	private Paginacion paginacion;
	private UConvierteFecha util;
	private Error error;
	@Autowired
	private IFileServerService fileServerService;

	@Autowired
	private ICursoDAO iCurso;

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

	private static final Logger LOGGER = Logger.getLogger(CapacitacionDAOImpl.class);	
	
	
	public List<Capacitacion> obtenerCapacitaciones(Capacitacion capacitacion, PageRequest pageRequest) {
		Map<String, Object> out = null;
		List<Capacitacion> lista = new ArrayList<>();

		this.paginacion = new Paginacion();
		this.error = null;
		this.paginacion.setPagina(pageRequest.getPagina());
		this.paginacion.setRegistros(pageRequest.getRegistros());

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("LISTAR_BANDEJA_CAPACITACIONES")
					.declareParameters(new SqlParameter("c_vnomcurs", OracleTypes.VARCHAR),
							new SqlParameter("c_vnominst", OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("c_vnomcurs", capacitacion.getNombreCurso())
					.addValue("c_vnominst", capacitacion.getNombreInstructor())
					.addValue("i_npagina", pageRequest.getPagina())
					.addValue("i_nregistros", pageRequest.getRegistros());

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearCapacitacion(out);
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");
				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.obtenerCapacitaciones";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		
		return lista;
	}

	private List<Capacitacion> mapearCapacitacion(Map<String, Object> resultado) {
		List<Capacitacion> listaCapacitacion = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		Capacitacion item = null;
		for (Map<String, Object> map : lista) {
			item = new Capacitacion();
			item.setIdCapacitacion(((BigDecimal) map.get("n_idcapa")).longValue());
			item.setIdCurso(((BigDecimal) map.get("n_idcurs")).longValue());
			item.setCodigoCurso((String) map.get("v_codcurs"));
			item.setNombreCurso((String) map.get("v_nomcurs"));
			item.setIndEvaluacion(((BigDecimal) map.get("n_indevacapa")).longValue());
			item.setFechaInicio((Date) map.get("fechaInicio"));
			item.setFechaFin((Date) map.get("fechaFin"));
			item.setNombreInstructor((String) map.get("v_nominst"));
			item.setIdInstructor(((BigDecimal) map.get("n_idinst")).longValue());
			item.setCantParticipantes(((BigDecimal) map.get("cantidadParticipantes")).longValue());
			item.setDisponibilidad(((BigDecimal) map.get("n_discapa")).longValue());
			item.setEstadoCapacitacion((String) map.get("v_estacapa"));

			Curso curs = obtenerCursoId(item.getIdCurso());
			item.setCurso(curs);
			List<Sesion> lstSes = obtenerCursoSesion(item.getIdCurso());
			item.setListaSesiones(lstSes);

			for (Sesion ses : lstSes) {
				item.setFechaInicio(ses.getFechaInicio());
				item.setFechaFin(ses.getFechaFin());
			}

			List<Colaborador> lstParticipante = obtenerParticipanteCapac(item.getIdCapacitacion());
			item.setLstColaborador(lstParticipante);

			List<CapacitacionDocInterno> lstDocumentosInternos = obtenerDocuIntCapacitacion(item.getIdCapacitacion());
			item.setListaDocumentos(lstDocumentosInternos);
			
			List<CapacitacionDocumentos> lstDocCapacitacion = obtenerDocumentosCapac(item.getIdCapacitacion());
			item.setListaDocuCapa(lstDocCapacitacion);
			
			listaCapacitacion.add(item);
			if (map.get("RESULT_COUNT") != null) {
				this.paginacion.setTotalRegistros(((BigDecimal) map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaCapacitacion;
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
			String mensaje = "Error en CapacitacionDAOImpl.obtenerCursoSesion";
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
			item.setFechaSesion((Date) map.get("fechaSesion"));
			item.setFechaInicio((Date) map.get("o_fechaInicio"));
			item.setFechaFin((Date) map.get("o_fechaFin"));
			item.setHoraInicio((String) map.get("horaInicio"));
			item.setHoraFin((String) map.get("horaFin"));
			listaSesion.add(item);
		}
		return listaSesion;
	}

	public Curso obtenerCursoId(Long idCurso) {

		Curso curso = null;
		Map<String, Object> out = null;
		this.error = null;

		try {

			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("PRC_OBTENER_CURSO_ID")
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
			String mensaje = "Error en CapacitacionDAOImpl.mapearCursoSesionId";
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

	public Capacitacion registroCapacitacion(Capacitacion capacitacion) {
		Capacitacion regCapa = null;
		try {
			if (capacitacion.getEstadoCapacitacion().equals("R")) {
				capacitacion.setIdCurso(capacitacion.getCurso().getIdCurso());
				//Registrar Datos Capacitacion c/s Sesion
				regCapa = this.registrarCapacitacion(capacitacion);
				
				//Registrar Datos Participante Capacitacion
				if (capacitacion.getLstColaborador().size() > 0) {
					for (Colaborador colaborador : capacitacion.getLstColaborador()) {
						boolean registro = false;
						Long disPartCap = (long) 1;
						CapacitacionColaborador part = new CapacitacionColaborador();
						part.setFichaColaborador(Long.valueOf(colaborador.getNumeroFicha()));
						part.setIdCapacitacion(regCapa.getIdCapacitacion());
						part.setDisponibilidad(disPartCap);
						registro = registrarParticianteCapacitacion(part);
						if (!registro) {
							break;
						}
					}
				}
				
				//Modificar datos Capacitacion Documentos
				if(capacitacion.getListaDocumentos().size()>0) {
					for(CapacitacionDocInterno doc : capacitacion.getListaDocumentos()) {
						boolean regDoc = false;
						capacitacion.setIdDocumento(doc.getIdDocumento());
						//Registrar Capacitacion documento
						if(doc.getDisponibilidad()==0) {
							regDoc = registrarCapacitacionDocumento(capacitacion);
							if(!regDoc) {
								break;
							}
						}
					}
				}
				
				//Modificar Preguntas capacitacion
				if(capacitacion.getLstPreguntas().size() > 0) {
					for(PreguntaCurso pregC : capacitacion.getLstPreguntas()) {
						if(pregC.getDisPregCapa() == 0) {
							
						}
					}
				}
			}

		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.registroCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return regCapa;
	}

	public Capacitacion registrarCapacitacion(Capacitacion capacitacion) {
		Map<String, Object> out = null;
		this.error = null;
		Long idCapa = null;
		boolean registro = false;
		boolean updateSesion = false;
		Capacitacion capaRegistro = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("REGISTRAR_CAPACITACION")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlParameter("i_nidinst", OracleTypes.NUMBER),
							new SqlParameter("i_nindevacapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidexam", OracleTypes.NUMBER),
							new SqlParameter("i_ndiscapa", OracleTypes.NUMBER),
							new SqlParameter("i_vestacapa", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR),
							new SqlOutParameter("o_nidcapa", OracleTypes.NUMBER));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", capacitacion.getIdCapacitacion())
					.addValue("i_nidcurs", capacitacion.getIdCurso())
					.addValue("i_nidinst", capacitacion.getInstructorr().getN_idinst())
					.addValue("i_nindevacapa", capacitacion.getIndEvaluacion())
					.addValue("i_nidexam", capacitacion.getIdExamen())
					.addValue("i_ndiscapa", capacitacion.getDisponibilidad())
					.addValue("i_vestacapa", capacitacion.getEstadoCapacitacion()).addValue("i_avusucre", "AGI");

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				idCapa = ((BigDecimal) out.get("o_nidcapa")).longValue();
				capacitacion.setIdCapacitacion(idCapa);
				//Registrar Datos sesion
				for (Sesion ses : capacitacion.getListaSesiones()) {
					ses.setIdCurso(capacitacion.getIdCurso());
					ses.setIdSesion(ses.getIdSesion());
					if (ses.getHoraFin() != null && ses.getHoraFin() != null) {
						String hInicio = UConvierteFecha.validar(ses.getHoraInicio(), ses.getFechaSesion());
						ses.setHoraInicio(hInicio);
						String hFin = UConvierteFecha.validar(ses.getHoraFin(), ses.getFechaSesion());
						ses.setHoraFin(hFin);
					}
					updateSesion = this.iCurso.registrarSesion(ses);
					if (!updateSesion) {
						break;
					}
				}
				capaRegistro = capacitacion;
				registro = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.registrarCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
			registro = false;
		}
		return capaRegistro;
	}

	public boolean registrarParticianteCapacitacion(CapacitacionColaborador colaborador) {
		boolean registro = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("REGISTRAR_CAPA_COLA")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidcola", OracleTypes.NUMBER),
							new SqlParameter("i_ndiscapcol", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", colaborador.getIdCapacitacion())
					.addValue("i_nidcola", colaborador.getFichaColaborador())
					.addValue("i_ndiscapcol", colaborador.getDisponibilidad()).addValue("i_avusucre", "AGI");

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.registrarParticianteCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return registro;
	}

	public List<Colaborador> obtenerParticipanteCapac(Long idCapacitacion) {
		List<Colaborador> lista = null;
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("OBTENER_PARTICIPANTES_CAPAC")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", idCapacitacion);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearParticipanteCapa(out);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.obtenerParticipanteCapac";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}

	public List<Colaborador> mapearParticipanteCapa(Map<String, Object> resultado) {
		List<Colaborador> listaParticipante = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		Colaborador item = null;
		for (Map<String, Object> map : lista) {
			item = new Colaborador();
			item.setDescripcionEquipo((String) map.get("vdescripcion"));
			item.setNombre((String) map.get("participante"));
			item.setNumFicha(((BigDecimal) map.get("nficha")).longValue());
			item.setDisponible(((BigDecimal) map.get("n_discapcol")).longValue());
			item.setIndEnvio(((BigDecimal) map.get("n_indenvi")).longValue());
			listaParticipante.add(item);
		}
		return listaParticipante;
	}

	@Override
	public boolean eliminarCapacitacion(Capacitacion capacitacion) {
		boolean elimino = false;
		elimino = this.eliminaCapacitacion(capacitacion);
		return elimino;
	}

	private boolean eliminaCapacitacion(Capacitacion capacitacion) {
		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("ELIMINAR_CAPACITACION")
				.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", capacitacion.getIdCapacitacion())
				.addValue("i_vusuario", "AGI");

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
			String mensaje = "Error en CapacitacionDAOImpl.eliminaCapacitacion";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}

	}

	@Override
	public Capacitacion actualizarDatosCapacitacion(Capacitacion capacitacion) {
		Map<String, Object> out = null;
		Capacitacion capacitacionActualizar = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("PRC_MODIFICAR_CAPACITACION")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidcurs", OracleTypes.NUMBER),
							new SqlParameter("i_nidinst", OracleTypes.NUMBER),
							new SqlParameter("i_nindevacapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidexam", OracleTypes.NUMBER),
							new SqlParameter("i_avusucre", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", capacitacion.getIdCapacitacion())
					.addValue("i_nidcurs", capacitacion.getIdCurso())
					.addValue("i_nidinst", capacitacion.getIdInstructor())
					.addValue("i_nindevacapa", capacitacion.getIndEvaluacion())
					.addValue("i_nidexam", capacitacion.getIdExamen()).addValue("i_avusucre", "AGI");

			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				capacitacionActualizar = capacitacion;
			} else {
				String mensaje = (String) out.get("o_mensaje");
				String mensajeInterno = (String) out.get("o_sqlerrm");				
				this.error = new Error(resultado, mensaje, mensajeInterno);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionaDAOImpl.actualizarDatosCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return capacitacionActualizar;
	}

	@Override
	public Capacitacion actualizarCapacitacion(Capacitacion capacitacion) {
		Capacitacion updCapacitacion = null;
		try {
			//Actualizar datos capacitacion
			updCapacitacion = actualizarDatosCapacitacion(capacitacion);
			
			//Modificar Datos Sesion
			if (updCapacitacion != null) {
				if(capacitacion.getListaSesiones().size() > 0) {
					for (Sesion ses : capacitacion.getListaSesiones()) {
						boolean actualizoCap = false;
						actualizoCap = this.iCurso.modificarDatosSesion(ses);
						if (!actualizoCap) {
							break;
						}
					}
				}
			}
			
			//Modificar Datos Colaborador
			if (capacitacion.getLstColaborador().size() > 0) {
				for (Colaborador colaborador : capacitacion.getLstColaborador()) {
					boolean update = false;
					boolean updateAsist = false;
					//Eliminar colaborador de capacitacion
					if (colaborador.getDisponible() == 2) {
						CapacitacionColaborador part = new CapacitacionColaborador();
						part.setFichaColaborador(colaborador.getNumFicha());
						part.setIdCapacitacion(capacitacion.getIdCapacitacion());
						update = eliminarParticipanteCapacitacion(part);
						if(update) {
							//Eliminar colaborador de asistencia
							Asistencia asist = new Asistencia();
							for(Sesion ses : capacitacion.getListaSesiones()) {
								asist.setIdSesion(ses.getIdSesion());
								asist.setIdColaborador(part.getFichaColaborador());
								updateAsist = eliminarParticipanteAsist(asist);
								if(!updateAsist) {
									break;
								}
							}
						}
						if (!update) {
							break;
						}
					} else if (colaborador.getDisponible() == 0) {
						//registrar particpante Capacitacion
						boolean registro = false;
						Long disPartCap = (long) 1;
						CapacitacionColaborador part = new CapacitacionColaborador();
						part.setFichaColaborador(Long.valueOf(colaborador.getNumFicha()));
						part.setIdCapacitacion(capacitacion.getIdCapacitacion());
						part.setDisponibilidad(disPartCap);
						registro = registrarParticianteCapacitacion(part);
						if (!registro) {
							break;
						}
					}
				}
			}
			
			//Modificar datos Capacitacion Documentos
			if(capacitacion.getListaDocumentos().size()>0) {
				for(CapacitacionDocInterno doc : capacitacion.getListaDocumentos()) {
					boolean regDoc = false;
					boolean elimina = false;
					capacitacion.setIdDocumento(doc.getIdDocumento());
					//Registrar Capacitacion documento
					if(doc.getDisponibilidad()==0) {
						regDoc = registrarCapacitacionDocumento(capacitacion);
						if(!regDoc) {
							break;
						}
					}
					//Eliminar Capacitacion documento
					else if(doc.getDisponibilidad() == 2){
						elimina = eliminarDocumentoCapacitacion(doc);
						if(!elimina) {
							break;
						}
					}
				}
			}
			
			//Modificar Preguntas capacitacion
			if(capacitacion.getLstPreguntas().size() > 0) {
				for(PreguntaCurso pregC : capacitacion.getLstPreguntas()) {
					if(pregC.getDisPregCapa() == 0) {
						
					}
					
					else if(pregC.getDisPregCapa() == 2) {
						
					}
				}
			}
			
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionaDAOImpl.actualizarCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return updCapacitacion;
	}

	public boolean eliminarParticipanteCapacitacion(CapacitacionColaborador participante) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("ELIMINAR_CAPACITACION_COLAB")
				.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
						new SqlParameter("i_nidcola", OracleTypes.NUMBER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", participante.getIdCapacitacion())
				.addValue("i_nidcola", participante.getFichaColaborador());

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
			String mensaje = "Error en CapacitacionDAOImpl.eliminarParticipanteCapacitacion()";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	public boolean programarCapacitacion(Asistencia asistencia) {
		boolean registro = false;
		boolean update = false;
		try {
			registro = registrarCapParticipante(asistencia);
			if(registro) {
				Capacitacion cap = new Capacitacion();
				cap.setIdCapacitacion(asistencia.getIdCapacitacion());
				cap.setEstadoCapacitacion(asistencia.getEstCapacitacion());
				update = actualizarEstadoCapacitacion(cap);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.programarCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return registro;
	}
	
	public boolean registrarCapParticipante(Asistencia asistencia) {
		boolean registro = false;
		boolean actIndCap = false;
		try {
			for (Sesion ses : asistencia.getListSesion()) {
				asistencia.setIdSesion(ses.getIdSesion());
				for(Trabajador trab : asistencia.getListTrabajador()) {
					if(trab.getIndEnvio() == 0) {
						asistencia.setIdColaborador(trab.getIdTrabajador());
						registro = enviarCapacitacionPart(asistencia);
						if(registro) {
							CapacitacionColaborador cap = new CapacitacionColaborador();
							cap.setIdCapacitacion(asistencia.getIdCapacitacion());
							cap.setFichaColaborador(trab.getIdTrabajador());
							actIndCap = actualizarCapacitacionColab(cap);
							if(!actIndCap) {
								break;
							}
						}
					}
				}
				if(!registro) {
					break;
				}
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.registrarCapParticipante";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return registro;
	}
	
	public boolean enviarCapacitacionPart(Asistencia asistencia) {
		
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI").withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("REGISTRAR_CAPACITACION_ASIST")
				.declareParameters(
						new SqlParameter("i_n_idsesi", OracleTypes.NUMBER),
						new SqlParameter("i_v_indasis", OracleTypes.NUMBER),
						new SqlParameter("i_v_jusasis", OracleTypes.VARCHAR),
						new SqlParameter("i_n_idcapa", OracleTypes.NUMBER),
						new SqlParameter("i_n_idcola", OracleTypes.NUMBER),
						new SqlParameter("i_n_disasis", OracleTypes.NUMBER),
						new SqlParameter("i_v_rutasust", OracleTypes.NUMBER),
						new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("i_n_idsesi", asistencia.getIdSesion())
				.addValue("i_v_indasis", asistencia.getIndAsistencia())
				.addValue("i_v_jusasis", asistencia.getJustificacion())
				.addValue("i_n_idcapa", asistencia.getIdCapacitacion())
				.addValue("i_n_idcola", asistencia.getIdColaborador())
				.addValue("i_n_disasis", asistencia.getDispAsistencia())
				.addValue("i_a_v_usucre", "AGI")
				.addValue("i_v_rutasust", asistencia.getRuta());

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
			String mensaje = "Error en CapacitacionDAOImpl.enviarCapacitacionPart";
			String mensajeInterno = e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			return false;
		}
	}

	public boolean actualizarCapacitacionColab(CapacitacionColaborador colaborador) {
		boolean registro = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("ACTUALIZAR_INDENV_CAPAC")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidcola", OracleTypes.NUMBER),
							new SqlParameter("i_ndiscapcol", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", colaborador.getIdCapacitacion())
					.addValue("i_nidcola", colaborador.getFichaColaborador()).addValue("i_avusumod", "AGI");

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.actualizarCapacitacionColab";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return registro;
	}
	
	public boolean eliminarParticipanteAsist(Asistencia asistencia) {
		boolean update = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("ELIMINAR_PARTICIPANTE_ASIST")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidcola", OracleTypes.NUMBER),
							new SqlParameter("i_ndiscapcol", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_n_idsesi", asistencia.getIdSesion())
					.addValue("i_n_idcola", asistencia.getIdColaborador());

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				update = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.eliminarParticipanteAsist";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return update;
	}
	
	public boolean actualizarEstadoCapacitacion(Capacitacion capacitacion) {
		boolean actualizo = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("ACTUALIZAR_ESTADO_CAPA")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_estcapa", OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidcapa", capacitacion.getIdCapacitacion())
					.addValue("i_estcapa", capacitacion.getEstadoCapacitacion())
					.addValue("i_avusumod", "AGI");

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				actualizo = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.actualizarEstadoCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return actualizo;
	}
	
	public boolean registrarCapacitacionDocumento(Capacitacion capacitacion) {
		boolean registro = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("REGISTRO_CAPA_DOCUMENTO")
					.declareParameters(
							new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_niddocu", OracleTypes.NUMBER),
							new SqlParameter("i_ndiscapdoc", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource()
					.addValue("i_nidcapa", capacitacion.getIdCapacitacion())
					.addValue("i_niddocu", capacitacion.getIdDocumento())
					.addValue("i_ndiscapdoc", "1")
					.addValue("i_avusucre", "AGI");

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				registro = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.registrarCapacitacionDocumento";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return registro;
	}
	
	public List<CapacitacionDocInterno> obtenerDocuIntCapacitacion(Long idCapacitacion){
		List<CapacitacionDocInterno> lista = null;
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("OBTENER_DOCUMENTOS_CAPA")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", idCapacitacion);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearDocumentoCapa(out);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.obtenerDocuIntCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	public List<CapacitacionDocInterno> mapearDocumentoCapa(Map<String, Object> resultado) {
		List<CapacitacionDocInterno> listaDocumento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		CapacitacionDocInterno item = null;
		for (Map<String, Object> map : lista) {
			item = new CapacitacionDocInterno();
			item.setIdCapacitacion(((BigDecimal) map.get("n_idcapa")).longValue());
			item.setIdDocumento(((BigDecimal) map.get("n_iddocu")).longValue());
			item.setCodigoDocumento((String) map.get("v_coddocu"));
			item.setNombreDocumento((String) map.get("v_desdocu"));
			item.setIdRevision(((BigDecimal) map.get("n_idrevi")).longValue());
			item.setFechaRevisionDocu((Date) map.get("fecRev"));
			item.setDisponibilidad(((BigDecimal) map.get("n_discapdoc")).longValue());
			listaDocumento.add(item);
		}
		return listaDocumento;
	}
	
	public boolean eliminarDocumentoCapacitacion(CapacitacionDocInterno documento) {
		boolean delete = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("ELIMINAR_DOCUMENTO_CAPA")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_nidcola", OracleTypes.NUMBER),
							new SqlParameter("i_ndiscapcol", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", documento.getIdCapacitacion())
					.addValue("i_niddocu", documento.getIdDocumento());

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				delete = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.eliminarDocumentoCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return delete;
	}

	@Override
	public CapacitacionDocumentos cargarDocumentoFS(CapacitacionDocumentos capacitacion, Optional<MultipartFile> file) {
		CapacitacionDocumentos cap = new CapacitacionDocumentos();
		try {
			boolean eliminoFS = false;
			boolean eliminoDB = false;
			if(capacitacion.getDisponibilidad()==2) {
				String nombArch = null;
				nombArch = capacitacion.getRutaDocumento().substring(53);
				eliminoFS = this.fileServerService.eliminarArchivoFileServerCapacitacion(nombArch); 
				if(eliminoFS) {
					eliminoDB = eliminarCapaDocuComp(capacitacion);
				}
			} else if(capacitacion.getDisponibilidad()==0) {
				UploadResponse uploadArchivoDoc = this.fileServerService.cargarArchivoFileServerCapacitacion(file.get().getBytes(), file.get().getOriginalFilename());

				String rutaArchivo = uploadArchivoDoc.getUrl();
				capacitacion.setRutaDocumento(rutaArchivo);
					cap = registrarDocumentoCapa(capacitacion);
			}
			
//			else if(capacitacion.getDisponibilidad()==2) {
//				this.fileServerService.eliminarArchivoFileServerCapacitacion(capacitacion.getNombreDocumento()); 
//			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.cargarDocumentoFS";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return cap;
	}
	
	
	public CapacitacionDocumentos registrarDocumentoCapa(CapacitacionDocumentos capacitacion) {
		this.error = null;
		Map<String, Object> out = null;
		Long idDocu = null;
		CapacitacionDocumentos datoCapaDocu=null;
		try {
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("REGISTRO_CAPA_DOC_COMP")
				.declareParameters(
						new SqlParameter("i_nidcapa",		OracleTypes.NUMBER),
						new SqlParameter("i_avusucre",	OracleTypes.VARCHAR),
						new SqlParameter("i_ndiscadoco",		OracleTypes.NUMBER),
						new SqlParameter("i_vrutdocu",	OracleTypes.VARCHAR),
						new SqlParameter("i_vnomarchi",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
						new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR),
						new SqlOutParameter("o_niddocu", OracleTypes.NUMBER));
	
		
		SqlParameterSource in =
				new MapSqlParameterSource()
					.addValue("i_nidcapa", capacitacion.getIdCapacitacion())
					.addValue("i_avusucre",		"AGI")
					.addValue("i_ndiscadoco", capacitacion.getDisponibilidad())
					.addValue("i_vrutdocu",	capacitacion.getRutaDocumento())
					.addValue("i_vnomarchi", capacitacion.getNombreDocumento());
					
		
		out = this.jdbcCall.execute(in);
		Integer resultado = (Integer)out.get("o_retorno");
		
		if(resultado == 0) {
			idDocu = ((BigDecimal) out.get("o_niddocu")).longValue();
			capacitacion.setIdDocumento(idDocu);
			datoCapaDocu = capacitacion;
			
		} else {
			String mensaje			= (String)out.get("o_mensaje");
			String mensajeInterno	= (String)out.get("o_sqlerrm");
			
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en AsistenciaDaoImpl.registrarDocumentoCapa";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return datoCapaDocu;
	}
	
	public List<CapacitacionDocumentos> obtenerDocumentosCapac(Long idCapacitacion){
		List<CapacitacionDocumentos> lista = null;
		Map<String, Object> out = null;
		this.error = null;

		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("OBTENER_CAPA_DOC_COMP")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor", OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", idCapacitacion);
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				lista = new ArrayList<>();
				lista = mapearDocumentoCapacitacion(out);
			}
		} catch (Exception e) {
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.obtenerDocumentosCapac";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	public List<CapacitacionDocumentos> mapearDocumentoCapacitacion(Map<String, Object> resultado) {
		List<CapacitacionDocumentos> listaDocumento = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultado.get("o_cursor");
		CapacitacionDocumentos item = null;
		for (Map<String, Object> map : lista) {
			item = new CapacitacionDocumentos();
			item.setIdCapacitacion(((BigDecimal) map.get("n_idcapa")).longValue());
			item.setIdDocumento(((BigDecimal) map.get("n_iddocu")).longValue());
			item.setNombreDocumento((String) map.get("v_nomarchi"));
			item.setDisponibilidad(((BigDecimal) map.get("n_discadoco")).longValue());
			item.setRutaDocumento((String) map.get("v_rutdocu"));
			System.out.println("RUTA :"  + item.getRutaDocumento());
			listaDocumento.add(item);
		}
		return listaDocumento;
	}

	@Override
	public List<PreguntaCurso> consultarPreguntaCursoId(PreguntaCurso pregunta, PageRequest paginaRequest) {
		
		Map<String, Object> out	= null;
		List<PreguntaCurso> listaCurso = null;
		this.error = null;
		
		this.paginacion = new Paginacion();
		
		this.paginacion.setPagina(paginaRequest.getPagina());
		this.paginacion.setRegistros(paginaRequest.getRegistros());
		try {
			
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
					.withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO")
					.withProcedureName("OBTENER_CAP_PREG_CURSO")
					.declareParameters(
							new SqlParameter("i_nidcurs", 	OracleTypes.VARCHAR),
							new SqlParameter("i_npagina", 		OracleTypes.NUMBER),		
							new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),		
							new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
			
			SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_nidcurs", pregunta.getIdCurso())		
						.addValue("i_npagina", paginaRequest.getPagina())
						.addValue("i_nregistros", paginaRequest.getRegistros());
		
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");

			if(resultado == 0) {
				listaCurso = new ArrayList<>();
				listaCurso = mapearCurso(out);
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e) {
			Integer resultado		= 1;
			String mensaje			= "Error en PreguntaCursoDaoImpl.consultarPreguntaCursoId";
			String mensajeInterno	= e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado,mensaje,mensajeInterno);
		}		
		return listaCurso;
	}
	
	private List<PreguntaCurso> mapearCurso(Map<String,Object> resultados){
		List<PreguntaCurso> listaCurso = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		PreguntaCurso item = null;
		for(Map<String, Object> map : lista) {	
			item = new PreguntaCurso();
			
			if(map.get("n_idcurs") != null) {
				item.setIdCurso(((BigDecimal)map.get("n_idcurs")).longValue());
			}
			if(map.get("n_idpreg") != null) {
				item.setIdPregunta(((BigDecimal)map.get("n_idpreg")).longValue());
			}
			if(map.get("v_codcurs") != null) {
				item.setCodCurso((String)map.get("v_codcurs"));
			}
			if(map.get("v_nomcurs") != null) {
				item.setNomCurso((String)map.get("v_nomcurs"));
			}
			if(map.get("n_idtipocurs") != null) {
				item.setIdTipoCurso(((BigDecimal)map.get("n_idtipocurs")).longValue());
			}
			if(map.get("nomTipo") != null) {
				item.setNomTipo((String)map.get("nomTipo"));
			}
			if(map.get("v_descpreg") != null) {
				item.setPregunta((String)map.get("v_descpreg"));
			}
			if(map.get("n_pesopreg") != null) {
				item.setPuntaje(((BigDecimal)map.get("n_pesopreg")).longValue());
			}
			if(map.get("n_dispreg") != null) {
				item.setDisponibilidad(((BigDecimal)map.get("n_dispreg")).longValue());
			}
			listaCurso.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}

		}
		
		return listaCurso;
	}
	
	public boolean eliminarCapaDocuComp(CapacitacionDocumentos documento) {
		boolean delete = false;
		Map<String, Object> out = null;
		this.error = null;
		try {
			this.jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName("AGI")
					.withCatalogName("PCK_AGI_MANTENIMIENTO").withProcedureName("ELIMINAR_CAPA_DOC_COMP")
					.declareParameters(new SqlParameter("i_nidcapa", OracleTypes.NUMBER),
							new SqlParameter("i_niddocu", OracleTypes.NUMBER),
							new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("i_nidcapa", documento.getIdCapacitacion())
					.addValue("i_niddocu", documento.getIdDocumento());

			out = this.jdbcCall.execute(in);

			Integer resultado = (Integer) out.get("o_retorno");
			if (resultado == 0) {
				delete = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Integer resultado = 1;
			String mensaje = "Error en CapacitacionDAOImpl.eliminarDocumentoCapacitacion";
			String mensajeInterno = e.getMessage();
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return delete;
	}
	
	
}
