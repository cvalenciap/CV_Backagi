package pe.com.sedapal.agi.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Constantes {
	// private static final Logger logger =
	// LoggerFactory.getLogger(DocumentoServicioImpl.class);

	public static final Map<Integer, String> MESSAGE_ERROR;
	static {
		MESSAGE_ERROR = new HashMap<>();
		MESSAGE_ERROR.put(1000, "No se encontro el usuario de autenticación");
		MESSAGE_ERROR.put(1001, "El valor del campo [{valor}] no puede estar vacío.");
		MESSAGE_ERROR.put(1002, "El valor del campo [{valor}] no puede superar [{maximo}] carácteres de longitud.");
		MESSAGE_ERROR.put(1004, "El valor del campo [{valor}] debe contener entre [1] a [{maximo}] caracteres.");
		MESSAGE_ERROR.put(1003, "El tipo de Actividad de la Carga de Trabajo no esta definida.");
		MESSAGE_ERROR.put(9001,
				"El objeto [{valor}] devuelto por servicio de seguridad, no se ha procesado correctamente.");
		MESSAGE_ERROR.put(9002, "No se pudo crear el directorio donde se almacenarán los archivos cargados");
		MESSAGE_ERROR.put(9003, "El nombre del archivo [{valor}] contiene una secuencia de ruta no válida");
		MESSAGE_ERROR.put(9004, "No se pudo almacenar el archivo [{valor}], por favor volver a intentar");
		MESSAGE_ERROR.put(9999, "Error en la ejecución de la transacción.");
		MESSAGE_ERROR.put(8000, "El valor del campo [{valor}] no cumple con el formato correcto");
	}

	public static final Map<Integer, String> MESSAGE_SUCCESS;
	static {
		MESSAGE_SUCCESS = new HashMap<>();
		MESSAGE_SUCCESS.put(2000, "Autenticación exitosa");
	}

	public static final Map<Integer, String> MESSAGE_CARGA_ARCHIVO_EJECUCION;
	static {
		MESSAGE_CARGA_ARCHIVO_EJECUCION = new HashMap<>();
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1001,
				"La cabecera del Archivo de Ejecución no coincide con el formato definido para la carga. Debe seguir la siguiente estructura:");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1002, "La carga del Archivo de Ejecución se realizó correctamente.");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1003,
				"Se realizó la carga del Archivo de Ejecución. Los siguientes registros no pudieron ser procesados:");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1004, "El fichero Zip debe contener un archivo de texto (.txt)");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1005, "El fichero Zip no debe contener carpeta(s).");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1006, "El fichero Zip no contiene ningún archivo de carga.");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1007, "El fichero Zip debe contener solo un archivo de carga.");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1008, "Se produjo un error durante la carga del Archivo de Ejecución.");
		MESSAGE_CARGA_ARCHIVO_EJECUCION.put(1009, "El Archivo de Ejecución no tiene contenido para el procesamiento.");
	}

	public static final Map<Integer, String> MESSAGE_VISOR_PDF;
	static {
		MESSAGE_VISOR_PDF = new HashMap<>();
		MESSAGE_VISOR_PDF.put(1001, "Ocurrió un error al consultar los archivos adjuntos.");
		MESSAGE_VISOR_PDF.put(1002, "Ocurrió un error en el File Server.");
	}

	public static String CORREO_FROM = "agestioncomercial@sedapal.com.pe";
	public static String CORREO_ASUNTO = "Carga de Trabajo Enviada por Sedapal";
	public static String[] cambioTexto = { "{valor}", "{maximo}" };

	public static final String ACT_DIST_AVISO_COBRANZA = "DA";
	public static final String ACT_DIST_COMUNIC = "DC";
	public static final String ACT_INSP_COMERCIAL = "IC";
	public static final String ACT_MEDIDORES = "ME";
	public static final String ACT_TOMA_ESTADO = "TE";

	public static final String PERFIL_ADMINISTRADOR = "1";
	public static final String PERFIL_ANALISTA_INTERNO = "2";
	public static final String PERFIL_RESPONSABLE = "3";
	public static final String PERFIL_ANALISTA_EXTERNO = "4";
	public static final String PERFIL_SUPERVISOR_EXTERNO = "5";

	public static final Integer TIPO_ESTADOS_CONTRATISTA = 5;
	public static final Integer TIPO_ESTADOS_TODOS = 1;

	public static final Integer MAX_REGISTROS_DETALLE_CARGA_TRABAJO = 60000;

	/*
	 * public static Error obtenerError(Integer codigoError, String textoRemplazar)
	 * { String message = MESSAGE_ERROR.get(codigoError); if(textoRemplazar != null)
	 * { String[] valores = textoRemplazar.split(","); for (int indice = 0; indice <
	 * valores.length; indice++){ message = StringUtils.replace(message,
	 * cambioTexto[indice], valores[indice]); } return new Error(codigoError,
	 * message); } else { return new Error(codigoError, message); } }
	 */

	/*
	 * public static Error validarRespuestaOracle(Map<String, Object>
	 * ejecucionOracle) { Error error = null; BigDecimal codigoEjecucion =
	 * (BigDecimal) ejecucionOracle.get("N_RESP"); if (codigoEjecucion.intValue() !=
	 * 1) { String mensaje = (String)ejecucionOracle.get("V_EJEC"); BigDecimal
	 * mensajeInterno = (BigDecimal) ejecucionOracle.get("N_EJEC"); error = new
	 * Error(9999, mensaje, mensajeInterno.toString()); } return error; }
	 */

	public static File convertMultipartFileToFile(MultipartFile multipartFile) {
		try {
			File file = new File(multipartFile.getOriginalFilename());
			file.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(multipartFile.getBytes());
			fileOutputStream.close();
			return file;
		} catch (IOException ioException) {
			// logger.error("[AGC: Constantes - convertMultipartFileToFile()] -
			// "+ioException.getMessage());
			return null;
		}
	}

	/*
	 * public static ResponseObject putAllParameters(Estado estado, Error error,
	 * Object resultado){ ResponseObject responseObject = new ResponseObject();
	 * responseObject.setEstado(estado); responseObject.setError(error);
	 * responseObject.setResultado(resultado); return responseObject; }
	 */

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
		file.transferTo(convFile);

		return convFile;
	}

	public static byte[] toByteArray(InputStream in) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len;

		// read bytes from the input stream and store them in buffer
		while ((len = in.read(buffer)) != -1) {
			// write bytes from the buffer into output stream
			os.write(buffer, 0, len);
		}

		return os.toByteArray();
	}

	// PARA FECHAS
	public static final String FORMATO_24_HORAS = "FORMATO 24 HORAS";
	public static final String FORMATO_12_HORAS = "FORMATO 12 HORAS";
	public static final String FORMATO_DD_MM_YYYY = "FORMATO DD/MM/YYYY";

	public static final String FORMATO_24_HORAS_DATE = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMATO_12_HORAS_DATE = "dd/MM/yyyy hh:mm aa";
	public static final String FORMATO_DD_MM_YYYY_DATE = "dd/MM/yyyy";
	public static final String FORMATO_24_HORAS_STRING = "HH:mm";

	/* navigator */
	public static final String ERROR = "ERROR";
	public static final String INFO = "INFO";
	public static final String REGRESAR = "REGRESAR";

	/* LOGGER */
	public static final String RESULTADO_OK = "OK";
	public static final String RESULTADO_ERROR = "ERROR";
	public static final String MENSAJE_ERROR_LOG = "Ocurrió un error inesperado con ID {0}, informar al área correspondiente.";

	/* Client Seguridad */
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";

	public static final int ESTADO_ACTIVO = 1;
	public static final int ESTADO_INACTIVO = 0;

	/* audit COLUMNS BD */
	public static final String ST_REGI_INACTIVO = "0";
	public static final String ST_REGI_ACTIVO = "1";

	/* persistence session */
	public static final String URL_LOGIN = "/api/credenciales/login";
	public static final String URL_LOGOUT = "/auth/logout";
	public static final String URL_APP_INFO = "/api/";

//	session constants
	public static final String REDIS_PREFIX_USERS = "TOKEN";
	public static final String REDIS_KEYS_SEPARATOR = ":";

	/* Constantes Servicio SUNARP - rramirez */
	public static final Integer ID_SERVICIO_SUNARP = 1;
	public static final String DES_SERVICIO_SUNARP = "Consulta SUNARP";

	public static final Integer ID_CONSULTA_TITULARIDAD = 1;
	public static final String DES_CONSULTA_TITULARIDAD = "Consulta de titularidad";
	public static final Integer ID_CONSULTA_PERSONA_JURIDICA = 2;
	public static final String DES_CONSULTA_PERSONA_JURIDICA = "Consulta de pesona jurídica";
	public static final Integer ID_CONSULTA_AERONAVE = 3;
	public static final String DES_CONSULTA_AERONAVE = "Consulta de aeronave";
	public static final Integer ID_CONSULTA_OFICINAS = 4;
	public static final String DES_CONSULTA_OFICINAS = "Consulta de oficinas";
	public static final Integer ID_CONSULTA_ASIENTOS = 5;
	public static final String DES_CONSULTA_ASIENTOS = "Consulta de asientos";
	public static final Integer ID_CONSULTA_DETALLE_RPV = 6;
	public static final String DES_CONSULTA_DETALLE_RPV = "Consulta de detalle RPV";
	public static final Integer ID_CONSULTA_IMG_ASIENTO = 7;
	public static final String DES_CONSULTA_IMG_ASIENTO = "Consulta de img asiento";

	/**/

	/* Constantes Servicio INPE - rramirez */
	public static final Integer ID_SERVICIO_INPE = 2;
	public static final String DES_SERVICIO_INPE = "Consulta INPE";

	public static final Integer ID_CONSULTA_ANTECEDENTE_JUDICIAL = 1;
	public static final String DES_CONSULTA_ANTECEDENTE_JUDICIAL = "Consulta de antecedente judicial";

	/**/

	/* Constantes Servicio PNP - rramirez */
	public static final Integer ID_SERVICIO_PNP = 3;
	public static final String DES_SERVICIO_PNP = "Consulta PNP";

	public static final Integer ID_CONSULTA_DNI_GENERAL = 1;
	public static final String DES_CONSULTA_DNI_GENERAL = "Consulta de dni general";
	public static final Integer ID_CONSULTA_NOMBRE_GENERAL = 2;
	public static final String DES_CONSULTA_NOMBRE_GENERAL = "Consulta de nombre general";

	/**/

	/* Constantes Servicio RENIEC - rramirez */
	public static final Integer ID_SERVICIO_RENIEC = 4;
	public static final String DES_SERVICIO_RENIEC = "Consulta RENIEC";

	public static final Integer ID_CONSULTA_DNI_RENIEC = 1;
	public static final String DES_CONSULTA_DNI_RENIEC = "Consulta de dni RENIEC";
	public static final Integer ID_ACTUALIZA_CREDENCIAL = 2;
	public static final String DES_ACTUALIZA_CREDENCIAL = "Actualización de credencial";

	/**/

	/* Constantes Servicio SUNEDU - rramirez */
	public static final Integer ID_SERVICIO_SUNEDU = 5;
	public static final String DES_SERVICIO_SUNEDU = "Consulta SUNEDU";

	public static final Integer ID_CONSULTA_RNGT = 1;
	public static final String DES_CONSULTA_RNGT = "Consulta de rngt";

	/**/

	/* Constantes Servicio SUNAT - rramirez */
	public static final Integer ID_SERVICIO_SUNAT = 6;
	public static final String DES_SERVICIO_SUNAT = "Consulta SUNAT";

	public static final Integer ID_CONSULTA_DATOS_PRINCIPALES = 1;
	public static final String DES_CONSULTA_DATOS_PRINCIPALES = "Consulta de datos principales";
	public static final Integer ID_CONSULTA_DATOS_SECUNDARIOS = 2;
	public static final String DES_CONSULTA_DATOS_SECUNDARIOS = "Consulta de datos secundarios";
	public static final Integer ID_CONSULTA_DATOST1144 = 3;
	public static final String DES_CONSULTA_DATOST1144 = "Consulta de datost1144";
	public static final Integer ID_CONSULTA_DATOST362 = 4;
	public static final String DES_CONSULTA_DATOST362 = "Consulta de datost362";
	public static final Integer ID_CONSULTA_DOMICILIO_LEGAL = 5;
	public static final String DES_CONSULTA_DOMICILIO_LEGAL = "Consulta de domicilio legal";
	public static final Integer ID_CONSULTA_ESTABLECIMIENTOS_ANEXOS = 6;
	public static final String DES_CONSULTA_ESTABLECIMIENTOS_ANEXOS = "Consulta de establecimientos anexos";
	public static final Integer ID_CONSULTA_ESTANEXOS_T1150 = 7;
	public static final String DES_CONSULTA_ESTANEXOS_T1150 = "Consulta de establecimientos anexos T1150";
	public static final Integer ID_CONSULTA_REPLEGALES = 8;
	public static final String DES_CONSULTA_REPLEGALES = "Consulta de relegales";
	public static final Integer ID_CONSULTA_SUNAT_RAZON_SOCIAL = 9;
	public static final String DES_CONSULTA_SUNAT_RAZON_SOCIAL = "Consulta de sunat razon social";

	/**/

	/* Inicio cambios mcortegana */
	public static class Mensajes {
		public static final String ERROR_OPERACION = "Ocurrio un error en la operacion";
	}
	
	public static class MantFichaAuditor {
		public static final String TIPO_AUD_INTERNO = "1";
		public static final String TIPO_AUD_EXTERNO = "2";
		public static final String ROL_AUD_EXTERNO = "5";
	}
	/* Fin cambios mcortegana */

}
