package pe.com.sedapal.agi.util;

import java.util.Map;

import org.springframework.util.StringUtils;

public class CastUtil {
	
	public static String leerValorMapString(Map<String, Object> map, String key) {
		return !StringUtils.isEmpty(map.get(key)) ? String.valueOf(map.get(key)) : null;
	}
	
	public static Integer leerValorMapInteger(Map<String, Object> map, String key) {
		return map.get(key) != null ? Integer.parseInt(String.valueOf(map.get(key))) : null;
	}
	
	public static Double leerValorMapDouble(Map<String, Object> map, String key) {
		return map.get(key) != null ? Double.parseDouble(String.valueOf(map.get(key))) : null;
	}
	
	public static String[] leerArregloCadena(Map<String, Object> map, String key, String separador) {
		String cadena = leerValorMapString(map, key);
		String[] array = cadena == null ? new String[0] : cadena.trim().split(separador);
		return array;
	}

}
