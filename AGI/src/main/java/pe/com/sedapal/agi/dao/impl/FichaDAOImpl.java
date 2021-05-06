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
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.IFichaDAO;
import pe.com.sedapal.agi.model.FichaTecnica;
import pe.com.sedapal.agi.model.FichaTecnicaDocumento;
import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.enums.EstadoConstante;

@Repository
public class FichaDAOImpl implements IFichaDAO {
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
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public FichaTecnica obtenerFicha(FichaTecnicaRequest fichaRequest) {
		Map<String, Object> out	= null;
		List<FichaTecnica> lista= new ArrayList<>();
		FichaTecnica objeto		= null;
		this.error				= null;
		
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_FICHA_OBTENER")
			.declareParameters(
				new SqlParameter("i_nid", 			OracleTypes.NUMBER),					
				new SqlParameter("i_ndisponible",	OracleTypes.NUMBER),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			fichaRequest.getId())
			.addValue("i_ndisponible",	fichaRequest.getEstado());
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				lista = this.mapearFicha(out);
				if(lista.size() > 0) {
					objeto = (FichaTecnica)lista.get(0);
					
					// Inicio Obtengo relación Ficha Tecnica - Documento
					objeto.setFichaTecnicaDocumento(obtenerFichaDocumento(objeto));
					// Fin
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en FichaDAOImpl.obtenerFicha";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			return null;
		}
		return objeto;
	}
	
	private ArrayList<FichaTecnicaDocumento> obtenerFichaDocumento(FichaTecnica ficha) {
		ArrayList<FichaTecnicaDocumento> ListaFichaDocumento = new ArrayList<>();;
		Map<String, Object> out	= null;
		this.error				= null;
		
		this.jdbcCall = new SimpleJdbcCall(this.jdbc)
							.withSchemaName("AGI")
							.withCatalogName("PCK_AGI_MANTENIMIENTO")
							.withProcedureName("PRC_DOCUMENTOFICHA_OBTENER")
							.declareParameters(
								new SqlParameter("i_ndocumento", 	OracleTypes.NUMBER),
								new SqlParameter("i_nficha", 		OracleTypes.NUMBER),	
								new SqlParameter("i_ndisponible", 	OracleTypes.NUMBER),	
								new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR),
								new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
								new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
								new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));		
		
		SqlParameterSource in = new MapSqlParameterSource()
									.addValue("i_ndocumento", null)
									.addValue("i_nficha", ficha.getId())
									.addValue("i_ndisponible", ficha.getDisponible());		
		
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");			
			if(resultado == 0) {
				// Mapeo resultados
				FichaTecnicaDocumento item = null;
				List<Map<String, Object>> lista	= (List<Map<String, Object>>)out.get("o_cursor");
				
				for(Map<String, Object> map : lista) {
					item = new FichaTecnicaDocumento();				
					item.setIdFich(((BigDecimal)map.get("N_IDFICH")).longValue());
					item.setIdDocu(((BigDecimal)map.get("N_IDDOCU")).longValue());								
					item.setDisFichDoc(((BigDecimal)map.get("n_disficdoc")).longValue());
					item.setDesDocu((String)map.get("v_desdocu"));
					item.setCodDocu((String)map.get("v_coddocu"));
					ListaFichaDocumento.add(item);
				}
				
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en FichaDAOImpl.obtenerFicha";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado,mensaje,mensajeInterno);
			return null;
		}		
		return ListaFichaDocumento;
	}

	@SuppressWarnings("unchecked")
	private List<FichaTecnica> mapearFicha(Map<String, Object> resultados) {
		List<FichaTecnica> listaFicha	= new ArrayList<>();		
		List<Map<String, Object>> lista	= (List<Map<String, Object>>)resultados.get("o_cursor");
		FichaTecnica item				= null;
		
		for(Map<String, Object> map : lista) {			
			item = new FichaTecnica();
			item.setId(((BigDecimal)map.get("N_IDFICH")).longValue());
			item.setIdTipoProceso(((BigDecimal)map.get("N_IDTIPOPROC")).longValue());
			item.setTipoProceso((String)map.get("V_NOTIPOPROC"));
			item.setIdNivel(((BigDecimal)map.get("N_NIVEFICH")).longValue());
			item.setNivel((String)map.get("V_VANIVEFICH"));
			item.setProceso((String)map.get("V_NOPROC"));
			item.setObjetivo((String)map.get("V_OBJEFICH"));
			item.setAlcance((String)map.get("V_ALCAFICH"));
			item.setRequisitos((String)map.get("V_REQUFICH"));
			item.setProveedores((String)map.get("V_PROVFICH"));
			item.setEntradas((String)map.get("V_ENTRFICH"));
			item.setSalidas((String)map.get("V_SALIFICH"));
			item.setSubProceso((String)map.get("V_ACTIFICH"));
			item.setClientes((String)map.get("V_CLIEFICH"));
			item.setPersonal((String)map.get("V_PERSFICH"));
			item.setEquipos((String)map.get("V_EQUIFICH"));
			item.setMateriales((String)map.get("V_MATEFICH"));
			item.setAmbientes((String)map.get("V_AMBIFICH"));
			item.setControles((String)map.get("V_CONTFICH"));
			item.setRegistros((String)map.get("V_REGIFICH"));
			item.setIndicadores((String)map.get("V_DESEFICH"));
			item.setIdElaborado((map.get("N_ELABFICH")==null)?null:((BigDecimal)map.get("N_ELABFICH")).longValue());
			item.setElaborado((String)map.get("V_NOELABFICH"));
			item.setIdConsensado((map.get("N_CONSFICH")==null)?null:((BigDecimal)map.get("N_CONSFICH")).longValue());
			item.setConsensado((String)map.get("V_NOCONSFICH"));
			item.setIdAprobado((map.get("N_APROFICH")==null)?null:((BigDecimal)map.get("N_APROFICH")).longValue());
			item.setAprobado((String)map.get("V_NOAPROFICH"));
			item.setIdResponsable((map.get("N_RESPPROC")==null)?null:((BigDecimal)map.get("N_RESPPROC")).longValue());
			item.setResponsable((String)map.get("V_NORESPPROC"));
			item.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISFICH")));		
			item.setIdJera(((BigDecimal)map.get("N_IDJERA")).longValue());
			item.setRutaGrafico((String)map.get("V_RUTAGRAF"));
			item.setNombreGrafico((String)map.get("V_NOMGRAF"));
			listaFicha.add(item);
			
			if (map.get("RESULT_COUNT")!=null) {
				this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
			}
		}
		return listaFicha;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FichaTecnica guardarFicha(FichaTecnica ficha, Long codigo, String usuario) {
		FichaTecnica objeto		= null;
		Map<String, Object> out = null;
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_FICHA_GUARDAR")
			.declareParameters(
			    new SqlParameter("i_nidfich",		OracleTypes.NUMBER),
			    new SqlParameter("i_nidtipoproc",	OracleTypes.NUMBER),
			    new SqlParameter("i_nnivefich",		OracleTypes.NUMBER),
			    new SqlParameter("i_vnoproc",		OracleTypes.VARCHAR),
			    new SqlParameter("i_nrespproc",		OracleTypes.NUMBER),
			    new SqlParameter("i_vobjefich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_valcafich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vrequfich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vprovfich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_ventrfich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vactifich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vsalifich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vcliefich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vpersfich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vequifich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vmatefich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vambifich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vcontfich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vregifich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_vdesefich",		OracleTypes.VARCHAR),
			    new SqlParameter("i_nelabfich",		OracleTypes.NUMBER),
			    new SqlParameter("i_nconsfich",		OracleTypes.NUMBER),
			    new SqlParameter("i_naprofich",		OracleTypes.NUMBER),
			    new SqlParameter("i_ndisfich",		OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
				new SqlParameter("i_n_idjera",		OracleTypes.NUMBER),
				new SqlParameter("i_vrutagraf",		OracleTypes.VARCHAR),
				new SqlParameter("i_vnomgraf",		OracleTypes.VARCHAR),	
				new SqlOutParameter("o_cursor",		OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
//			.addValue("i_nidfich",		codigo)
			.addValue("i_nidfich",		ficha.getId())
			.addValue("i_nidtipoproc",	ficha.getIdTipoProceso())
			.addValue("i_nnivefich",	ficha.getIdNivel())
			.addValue("i_vnoproc",		ficha.getProceso())
			.addValue("i_nrespproc",	ficha.getIdResponsable())
			.addValue("i_vobjefich",	ficha.getObjetivo())
			.addValue("i_valcafich",	ficha.getAlcance())
			.addValue("i_vrequfich",	ficha.getRequisitos())
			.addValue("i_vprovfich",	ficha.getProveedores())
			.addValue("i_ventrfich",	ficha.getEntradas())
			.addValue("i_vactifich",	ficha.getSubProceso())
			.addValue("i_vsalifich",	ficha.getSalidas())
			.addValue("i_vcliefich",	ficha.getClientes())
			.addValue("i_vpersfich",	ficha.getPersonal())
			.addValue("i_vequifich",	ficha.getEquipos())
			.addValue("i_vmatefich",	ficha.getMateriales())
			.addValue("i_vambifich",	ficha.getAmbientes())
			.addValue("i_vcontfich",	ficha.getControles())
			.addValue("i_vregifich",	ficha.getRegistros())
			.addValue("i_vdesefich",	ficha.getIndicadores())
			.addValue("i_nelabfich",	ficha.getIdElaborado())
			.addValue("i_nconsfich",	ficha.getIdConsensado())
			.addValue("i_naprofich",	ficha.getIdAprobado())
			.addValue("i_vrutagraf",	ficha.getRutaGrafico())
			.addValue("i_vnomgraf", 	ficha.getNombreGrafico())
			.addValue("i_ndisfich",		1)
			.addValue("i_vusuario",		usuario)
			.addValue("i_n_idjera",		ficha.getIdJera());
		
		try {
			out = this.jdbcCall.execute(in);
			Integer resultado = (Integer)out.get("o_retorno");
			if(resultado == 0) {				
				/* Inicio : Grabo relación Ficha Tecnica - Documento */
				guardarFichaDocumento(ficha.getFichaTecnicaDocumento(), usuario);								
				/* Fin*/				
				List<Map<String, Object>> listado = (List<Map<String, Object>>)out.get("o_cursor");
				if(listado!=null) {
					for(Map<String, Object> map : listado) {
						objeto = new FichaTecnica();
						objeto.setId(((BigDecimal)map.get("N_IDFICH")).longValue());
						objeto.setIdTipoProceso(((BigDecimal)map.get("N_IDTIPOPROC")).longValue());
						objeto.setTipoProceso((String)map.get("V_NOTIPOPROC"));
						objeto.setIdNivel(((BigDecimal)map.get("N_NIVEFICH")).longValue());
						objeto.setNivel((String)map.get("V_VANIVEFICH"));
						objeto.setProceso((String)map.get("V_NOPROC"));
						objeto.setIdResponsable((map.get("N_RESPPROC")==null)?null:((BigDecimal)map.get("N_RESPPROC")).longValue());
						objeto.setResponsable((String)map.get("V_NORESPPROC"));
						objeto.setObjetivo((String)map.get("V_OBJEFICH"));
						objeto.setAlcance((String)map.get("V_ALCAFICH"));
						objeto.setRequisitos((String)map.get("V_REQUFICH"));
						objeto.setProveedores((String)map.get("V_PROVFICH"));
						objeto.setEntradas((String)map.get("V_ENTRFICH"));
						objeto.setSubProceso((String)map.get("V_ACTIFICH"));
						objeto.setSalidas((String)map.get("V_SALIFICH"));
						objeto.setClientes((String)map.get("V_CLIEFICH"));
						objeto.setPersonal((String)map.get("V_PERSFICH"));
						objeto.setEquipos((String)map.get("V_EQUIFICH"));
						objeto.setMateriales((String)map.get("V_MATEFICH"));
						objeto.setAmbientes((String)map.get("V_AMBIFICH"));
						objeto.setControles((String)map.get("V_CONTFICH"));
						objeto.setRegistros((String)map.get("V_REGIFICH"));
						objeto.setIndicadores((String)map.get("V_DESEFICH"));
						objeto.setIdElaborado((map.get("N_ELABFICH")==null)?null:((BigDecimal)map.get("N_ELABFICH")).longValue());
						objeto.setElaborado((String)map.get("V_NOELABFICH"));
						objeto.setIdConsensado((map.get("N_CONSFICH")==null)?null:((BigDecimal)map.get("N_CONSFICH")).longValue());
						objeto.setConsensado((String)map.get("V_NOCONSFICH"));
						objeto.setIdAprobado((map.get("N_APROFICH")==null)?null:((BigDecimal)map.get("N_APROFICH")).longValue());
						objeto.setAprobado((String)map.get("V_NOAPROFICH"));
						objeto.setEstado(EstadoConstante.setEstado((BigDecimal)map.get("N_DISFICH")));
						objeto.setRutaGrafico((String)map.get("V_RUTAGRAF"));
						objeto.setClientes((String)map.get("V_NOMGRAF"));				           
					}
				}
			} else {
				String mensaje			= (String)out.get("o_mensaje");
				String mensajeInterno	= (String)out.get("o_sqlerrm");
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en FichaDAOImpl.guardarFicha";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
		}
		return objeto; 
	}
	
	@Transactional
	public boolean guardarFichaDocumento(ArrayList<FichaTecnicaDocumento> ListaFichaDocumento, String usuario) {
		Map<String, Object> out = null;
		this.error				= null;
		boolean existeError = false;
		this.jdbcCall			= 
				new SimpleJdbcCall(this.jdbc)
				.withSchemaName("AGI")
				.withCatalogName("PCK_AGI_MANTENIMIENTO")
				.withProcedureName("PRC_DOCUMENTOFICHA_GUARDAR")
				.declareParameters(
				    new SqlParameter("i_ndisficdoc",OracleTypes.NUMBER),
				    new SqlParameter("i_nidfich",	 OracleTypes.NUMBER),
				    new SqlParameter("i_niddocu",	 OracleTypes.NUMBER),
				    new SqlParameter("i_vusuario", OracleTypes.VARCHAR),
					new SqlOutParameter("o_retorno", OracleTypes.INTEGER),
					new SqlOutParameter("o_mensaje", OracleTypes.VARCHAR),
					new SqlOutParameter("o_sqlerrm", OracleTypes.VARCHAR));
		
		for(FichaTecnicaDocumento obj : ListaFichaDocumento) {
			
			SqlParameterSource in = new MapSqlParameterSource()
										.addValue("i_ndisficdoc", obj.getDisFichDoc())
										.addValue("i_nidfich",	obj.getIdFich())
										.addValue("i_niddocu",	obj.getIdDocu())
										.addValue("i_vusuario",		usuario);
			
			
			try {
				out = this.jdbcCall.execute(in);
				Integer resultado = (Integer)out.get("o_retorno");
				if(resultado == 0) {
					existeError = false;
				}else {
					String mensaje			= (String)out.get("o_mensaje");
					String mensajeInterno	= (String)out.get("o_sqlerrm");
					this.error				= new Error(resultado,mensaje,mensajeInterno);
					return true;
				}					
															
			}catch(Exception e){
				Integer resultado		= 1;
				String mensaje			= "Error en FichaDAOImpl.guardarFicha";
				String mensajeInterno	= e.getMessage();
				this.error				= new Error(resultado,mensaje,mensajeInterno);
			}
		}
		return existeError;
	}

	public Boolean eliminarFicha(Long ficha, Long jerarquia, String usuario) {
		Map<String, Object> out = null;
		this.error = null;
		this.jdbcCall =
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_MANTENIMIENTO")
			.withProcedureName("PRC_FICHA_ELIMINAR")
			.declareParameters(
				new SqlParameter("i_nid",		OracleTypes.NUMBER),
				new SqlParameter("i_njerarquia",OracleTypes.NUMBER),
				new SqlParameter("i_vusuario",	OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),
				new SqlOutParameter("o_retorno",OracleTypes.INTEGER),
				new SqlOutParameter("o_mensaje",OracleTypes.VARCHAR),
				new SqlOutParameter("o_sqlerrm",OracleTypes.VARCHAR));
		
		SqlParameterSource in =
		new MapSqlParameterSource()
			.addValue("i_nid",			ficha)
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
			String mensaje			= "Error en FichaDAOImpl.eliminarFicha";
			String mensajeInterno	= e.getMessage();
			this.error				= new Error(resultado,mensaje,mensajeInterno);
			return false;
		}
	}

}