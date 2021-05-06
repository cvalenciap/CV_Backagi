package pe.com.sedapal.agi.util;

import java.text.ParseException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			String stringHora = "10:20";
//			DateFormat hora = new SimpleDateFormat("HH:mm:ss");
//			Date convertido = hora.parse(stringHora);
//			System.out.println(convertido);
			String fecha = "2019-04-02 05:00:00.000 Z";
			SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z"); 
			Date fechaConvertida = null;
			fechaConvertida = formateador.parse(fecha);
		
	        System.out.println(fechaConvertida);
		} catch (ParseException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
