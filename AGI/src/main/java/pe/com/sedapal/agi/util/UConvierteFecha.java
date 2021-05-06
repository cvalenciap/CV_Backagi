package pe.com.sedapal.agi.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UConvierteFecha {

	public static String formatearFecha(Date fecha) {
		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		String fechaConvertida = formatoFecha.format(fecha);
		return fechaConvertida;
	}

	public static Date convertirStringADate(String fecha) throws Exception {

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaConvertida = null;
		try {
			fechaConvertida = formateador.parse(fecha);
		} catch (ParseException excepcion) {
			throw new Exception(excepcion);
		}
		return fechaConvertida;
	}

	public static String validar(String hora, Date fecFront) throws Exception {
		try {
			DateFormat hour = new SimpleDateFormat("HH:mm");
			fecFront = hour.parse(hora);
			String devfec = convertirStringAFecha(fecFront);
			DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
//			String fechaConvertida = formatoFecha.format(devfec);
//			System.out.println("FORMAT :" + fechaConvertida);
			return devfec;
		} catch (ParseException excepcion) {
			throw new Exception(excepcion);
		}
	}

	public static Date convertirHoraADate(String hora, Date fecFront) throws Exception {
		DateFormat hour = new SimpleDateFormat("HH:mm");
		try {
			fecFront = hour.parse(hora);
			String devfec = convertirStringAFecha(fecFront);
		} catch (ParseException excepcion) {
			throw new Exception(excepcion);
		}
		System.out.println("HORA DATE :" + fecFront);
		return fecFront;

	}

	public static String convertirStringAFecha(Date fecha) throws Exception {

		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
		String fechaConvertida = formatoFecha.format(fecha);
		System.out.println("FORMAT :" + fechaConvertida);
		return fechaConvertida;
	}

}
