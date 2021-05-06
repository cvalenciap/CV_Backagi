package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.agi.dao.ICorreoDatosDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.CorreoDestinatario;
import pe.com.sedapal.agi.model.FormatoHTML;
import pe.com.sedapal.agi.model.enums.CorreoConstante;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.util.UCorreo;

@Repository
public class CorreoDatosDAOImpl implements ICorreoDatosDAO {
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Error			error;
	
	public Error getError() {
		return this.error;
	}
	
	public Error getErrorNull() {
		this.error = null;
		return this.error;
	}
	
	@Override
	public FormatoHTML obtenerFormato(String nombre) throws SQLException {
		Map<String, Object> out	= null;
		FormatoHTML	objeto		= null;
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_CORREO")
			.withProcedureName("PRC_OBTENER_FORMATO")
			.declareParameters(
				new SqlParameter("i_vnombre", 	OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		
		SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vnombre",	nombre);
		
		try {
			out		= this.jdbcCall.execute(in);
			objeto	= this.mapearFormatoHTML(out);
		}catch(Exception e){
			System.out.println(e);
			Integer resultado		= 1;
			String mensaje			= "Error en CorreoDatosDAOImpl.obtenerFormato";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return objeto;
	}
	
	@SuppressWarnings("unchecked")
	private FormatoHTML mapearFormatoHTML(Map<String, Object> resultados) throws SQLException {
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		FormatoHTML item				= null;
		for(Map<String, Object> map : lista) {
			item = new FormatoHTML();
			item.setId(((BigDecimal)map.get("N_IDCORFOR")).longValue());
			item.setDescripcion((String)map.get("V_DESCORFOR"));
			item.setTitulo((String)map.get("V_TITCORFOR"));
			byte[] formato = (byte[]) map.get("B_FORCORFOR");
			item.setArchivo(formato);
			Blob blob = new SerialBlob(formato);
			blob.setBytes(1, formato);
			item.setFormatoHtml(blob);
		}
		return item;
	}
	
	@Override
	public List<String> obtenerVariableFormato(String nombre) throws SQLException {
		Map<String, Object> out	= null;
		List<String> lista		= new ArrayList<>();
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_CORREO")
			.withProcedureName("PRC_OBTENER_VARIABLE")
			.declareParameters(
				new SqlParameter("i_vnombre", 	OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		
		SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vnombre",	nombre);
		
		try {
			out		= this.jdbcCall.execute(in);
			lista	= this.mapearVariable(out);
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en CorreoDatosDAOImpl.obtenerVariableFormato";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	

	@SuppressWarnings("unchecked")
	private List<String> mapearVariable(Map<String, Object> resultados) throws SQLException {
		List<String> listaVariable		= new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		for(Map<String, Object> map : lista) {
			String resultado = (String)map.get("V_CODCORVAR");
			int posicion	 = (map.get("N_POSCORVAR")==null)?0:((BigDecimal)map.get("N_POSCORVAR")).intValue();
			listaVariable.add(resultado);
		}
		return listaVariable;
	}
	
	@Override
	public List<String> obtenerValorFormato(List<Long> listaId, String nombre, String imagenFirma, String imagenPie, String linkSistema)
	throws SQLException {
		Map<String, Object> out	= null;
		List<String> lista		= new ArrayList<>();
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_CORREO")
			.withProcedureName("PRC_OBTENER_VALOR_VARIABLE")
			.declareParameters(
				new SqlParameter("i_vdocumento",OracleTypes.NUMBER),
				new SqlParameter("i_vid", 		OracleTypes.NUMBER),
				new SqlParameter("i_vauxiliar", OracleTypes.NUMBER),
				new SqlParameter("i_vtipo",		OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", OracleTypes.CURSOR));
		
		Long idDocumento= null;
		Long id			= null;
		Long idAuxiliar	= null;
		if(listaId.size() >= 1)	idDocumento	= listaId.get(0);
		if(listaId.size() >= 2)	id			= listaId.get(1);
		if(listaId.size() >= 3)	idAuxiliar	= listaId.get(2);
		
		SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vdocumento",	idDocumento)
				.addValue("i_vid",			id)
				.addValue("i_vauxiliar",	idAuxiliar)
				.addValue("i_vtipo",		nombre);
		
		try {
			out		= this.jdbcCall.execute(in);
			lista	= this.mapearValor(out, nombre, imagenFirma, imagenPie, linkSistema);
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en CorreoDatosDAOImpl.obtenerValorFormato";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> mapearValor(Map<String, Object> resultados, String nombre, String imagenFirma,
			String imagenPie, String linkSistema) throws SQLException {
		List<String> listaVariable = new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>) resultados.get("o_cursor");
		UCorreo utilitario = new UCorreo();
		for (Map<String, Object> map : lista) {
			String estilo = utilitario.obtenerEstilosCorreoHtml();
			String codigo = (String) map.get("V_CODDOCU");
			String titulo = (String) map.get("V_DESDOCU");

			if (nombre.equals(CorreoConstante.NOTI_REGI_SOLI.toString())
					|| nombre.equals(CorreoConstante.NOTI_APRO_SOLI.toString())
					|| nombre.equals(CorreoConstante.NOTI_SEDA.toString())					
					|| nombre.equals(CorreoConstante.NOTI_APRO.toString())) {
				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, titulo);
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, linkSistema);
			}else if (nombre.equals(CorreoConstante.NOTI_CANC_SOLI.toString())
					|| nombre.equals(CorreoConstante.NOTI_CANC_APRO.toString())
					|| nombre.equals(CorreoConstante.NOTI_CANC_EJEC.toString())) {
				String tCancel = (String) map.get("VTIPO");
				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, titulo);
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, tCancel);
				listaVariable.add(6, linkSistema);
			}else if (nombre.equals(CorreoConstante.NOTI_CANC_RECH.toString())) {
				String cRechazo = (String) map.get("V_SUSTRECH");
				String tCancel = (String) map.get("VTIPO");
				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, titulo);
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, cRechazo);
				listaVariable.add(6, tCancel);
				listaVariable.add(7, linkSistema);
			} else if (nombre.equals(CorreoConstante.NOTI_RECH_DOCU.toString())) {
				String vRechazo = (String) map.get("V_RECHREVI");
				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, titulo);
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, vRechazo);
				listaVariable.add(6, linkSistema);
			} else if (nombre.equals(CorreoConstante.NOTI_PLAZ_ATEN.toString())) {
				String vence = (String) map.get("V_DESVENC");
				String plazo = (String) map.get("D_PLAZPART");

				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, titulo);
				listaVariable.add(3, vence);
				listaVariable.add(4, plazo);
				listaVariable.add(5, imagenFirma);
				listaVariable.add(6, imagenPie);
				listaVariable.add(7, linkSistema);
			} else if (nombre.equals(CorreoConstante.NOTI_DOCU_LIBE.toString())) {
				String fase = (String) map.get("V_NOMFASE");

				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, fase);
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, linkSistema);
			} else if (nombre.equals(CorreoConstante.NOTI_COPI_RECH.toString())) {
				Long posicion = (map.get("N_CANCOPI") == null) ? 0 : ((BigDecimal) map.get("N_CANCOPI")).longValue();
				String copia = posicion.toString();
				Long nidCopi = (map.get("N_IDCOPI") == null) ? 0 : ((BigDecimal) map.get("N_IDCOPI")).longValue();
				String ncopia = nidCopi.toString();
				String critica = (String) map.get("DE_CRITICA");

				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, copia);
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, titulo);
				listaVariable.add(6, ncopia);
				listaVariable.add(7, critica);
				listaVariable.add(8, linkSistema);

			} else if (nombre.equals(CorreoConstante.NOTI_COPI_REGI.toString())
					|| nombre.equals(CorreoConstante.NOTI_COPI_IMPR.toString())) {
				Long posicion = (map.get("N_CANCOPI") == null) ? 0 : ((BigDecimal) map.get("N_CANCOPI")).longValue();
				String copia = posicion.toString();
				Long nidCopi = (map.get("N_IDCOPI") == null) ? 0 : ((BigDecimal) map.get("N_IDCOPI")).longValue();
				String ncopia = nidCopi.toString();

				listaVariable.add(0, estilo);
				listaVariable.add(1, codigo);
				listaVariable.add(2, copia);
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, titulo);
				listaVariable.add(6, ncopia);
				listaVariable.add(7, linkSistema);

			} else if (nombre.equals(CorreoConstante.NOTI_PROG_SOLI.toString())
					|| nombre.equals(CorreoConstante.NOTI_PROG_DIST.toString())
					|| nombre.equals(CorreoConstante.NOTI_PROG_EJEC.toString())) {
				
				Long nidProg = (map.get("N_IDPROG") == null) ? 0 : ((BigDecimal) map.get("N_IDPROG")).longValue();
				String nProg = nidProg.toString();
				String html = (String) map.get("V_HTML_MSG");

				listaVariable.add(0, estilo);
				listaVariable.add(1, nProg);
				listaVariable.add(2, html);				
				listaVariable.add(3, imagenFirma);
				listaVariable.add(4, imagenPie);
				listaVariable.add(5, linkSistema);
			}
		}
		return listaVariable;
	}
	
	@Override
	public List<String> obtenerDestinatario(List<Long> listaId, String nombre) throws SQLException {
		Map<String, Object> out	= null;
		List<String> lista		= new ArrayList<>();
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_CORREO")
			.withProcedureName("PRC_OBTENER_DESTINATARIO")
			.declareParameters(
				new SqlParameter("i_vdocumento",	OracleTypes.NUMBER),
				new SqlParameter("i_vid",			OracleTypes.NUMBER),
				new SqlParameter("i_vtipo",			OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR));
		
		Long idDocumento	= null;
		Long id				= null;
		if(listaId.size() >= 1)	idDocumento		= listaId.get(0);
		if(listaId.size() >= 2)	id				= listaId.get(1);
		
		SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vdocumento",	idDocumento)
				.addValue("i_vid",			id)
				.addValue("i_vtipo",		nombre);
		
		try {
			out		= this.jdbcCall.execute(in);
			lista	= this.mapearDestinatario(out, nombre);
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en CorreoDatosDAOImpl.obtenerDestinatario";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> mapearDestinatario(Map<String, Object> resultados, String nombre) throws SQLException {
		List<String> listado			= new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		int posicion					= 0;
		for(Map<String, Object> map : lista) {
			String correo = (String)map.get("VDIRELECTRONICA");
			listado.add(posicion, correo);
			posicion = posicion + 1;
		}
		return listado;
	}
	
	@Override
	public List<CorreoDestinatario> obtenerListaDestinatario(List<Long> listaId, String nombre) throws SQLException {
		Map<String, Object> out			= null;
		List<CorreoDestinatario> lista	= new ArrayList<>();
		this.error						= null;
		this.jdbcCall					=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_CORREO")
			.withProcedureName("PRC_OBTENER_DESTINATARIO")
			.declareParameters(
				new SqlParameter("i_vdocumento",	OracleTypes.NUMBER),
				new SqlParameter("i_vid",			OracleTypes.NUMBER),
				new SqlParameter("i_vtipo",			OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR));
		
		Long idDocumento	= null;
		Long id				= null;
		if(listaId.size() >= 1)	idDocumento		= listaId.get(0);
		if(listaId.size() >= 2)	id				= listaId.get(1);
		
		SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vdocumento",	idDocumento)
				.addValue("i_vid",			id)
				.addValue("i_vtipo",		nombre);
		
		try {
			out		= this.jdbcCall.execute(in);
			lista	= this.mapearListaDestinatario(out, nombre);
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en CorreoDatosDAOImpl.obtenerListaDestinatario";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<CorreoDestinatario> mapearListaDestinatario(Map<String, Object> resultados, String nombre) throws SQLException {
		List<CorreoDestinatario> listado= new ArrayList<>();
		List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
		for(Map<String, Object> map : lista) {
			Long id = (map.get("NCODTRABAJADOR")==null)?null:((BigDecimal)map.get("NCODTRABAJADOR")).longValue();
			Long idEquipo = (map.get("N_IDEQUI")==null)?null:((BigDecimal)map.get("N_IDEQUI")).longValue();
			String correo = (String)map.get("VDIRELECTRONICA");
			CorreoDestinatario destinatario=new CorreoDestinatario();
			destinatario.setIdDestinatario(id);
			destinatario.setCorreo(correo);
			destinatario.setIdEquipo(idEquipo);
			listado.add(destinatario);
		}
		return listado;
	}
	
	@Override
	public List<String> obtenerDestinatarioOtro(List<Long> listaId, String nombre) throws SQLException {
		Map<String, Object> out	= null;
		List<String> lista		= new ArrayList<>();
		this.error				= null;
		this.jdbcCall			=
		new SimpleJdbcCall(this.jdbc)
			.withSchemaName("AGI")
			.withCatalogName("PCK_AGI_CORREO")
			.withProcedureName("PRC_OBTENER_DESTINATARIO_OTRO")
			.declareParameters(
				new SqlParameter("i_vdocumento",	OracleTypes.NUMBER),
				new SqlParameter("i_vid",			OracleTypes.NUMBER),
				new SqlParameter("i_vtipo",			OracleTypes.VARCHAR),
				new SqlOutParameter("o_cursor", 	OracleTypes.CURSOR));
		
		Long idDocumento	= null;
		Long id				= null;
		if(listaId.size() >= 1)	idDocumento		= listaId.get(0);
		if(listaId.size() >= 2)	id				= listaId.get(1);
		
		SqlParameterSource in =
			new MapSqlParameterSource()
				.addValue("i_vdocumento",	idDocumento)
				.addValue("i_vid",			id)
				.addValue("i_vtipo",		nombre);
		
		try {
			out		= this.jdbcCall.execute(in);
			lista	= this.mapearDestinatario(out, nombre);
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en CorreoDatosDAOImpl.obtenerDestinatarioOtro";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}
	
	@Override
	public List<Constante> obtenerDatosCorreo() throws SQLException {
		List<Constante> lista = new ArrayList<>();
		try {
			PageRequest pagina = new PageRequest();
			pagina.setPagina(1);
			pagina.setRegistros(1000);
			
			ConstanteDAOImpl constanteDao = new ConstanteDAOImpl();
			constanteDao.setJdbc(this.jdbc);
			
			ConstanteRequest constante = new ConstanteRequest();
			constante.setPadre(CorreoConstante.CONSTANTE_PARAMETRO_CORREO.toString());
			lista = constanteDao.obtenerConstantes(constante, pagina, null);
			
		}catch(Exception e){
			Integer resultado		= 1;
			String mensaje			= "Error en CorreoDatosDAOImpl.obtenerDatosCorreo";
			String mensajeInterno	= e.getMessage();
			this.error = new Error(resultado, mensaje, mensajeInterno);
		}
		return lista;
	}

}