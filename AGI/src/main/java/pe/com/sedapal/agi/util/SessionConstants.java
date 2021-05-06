package pe.com.sedapal.agi.util;

public class SessionConstants {
	
	/*Constantes de Seguridad*/
	
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";
	
	public static final int ESTADO_ACTIVO = 1;
	public static final int ESTADO_INACTIVO = 0;
	
	/* url */
	public static final String URL_LOGIN = "/auth/login";
	public static final String URL_PROFILE = "/auth/login/profile";
	public static final String URL_PASSWORD_REQUEST = "/auth/login/password/request";
	public static final String URL_PASSWORD_RESET = "/auth/login/password/reset";
	public static final String URL_LOGOUT = "/auth/logout";
	public static final String URL_APP_INFO = "/api/";
	
	/* redis */
	
	public static final String REDIS_PREFIX_USERS = "TOKEN";
	public static final String REDIS_KEYS_SEPARATOR = ":";

}
