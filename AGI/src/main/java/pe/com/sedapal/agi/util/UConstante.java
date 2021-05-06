/**
 * Resumen.
 * Objeto                   : UConstante.java
 * Descripcion              : Clase para las constantes
 * Fecha de Creacion        : 14/12/2018
 * PE de Creacion           : 36571
 * Autor                    : Werner Mamani Colan
 * ---------------------------------------------------------------------------------------------------------------------------
 * Modificaciones
 * Motivo                   Fecha             Nombre                  Descripcion
 * ---------------------------------------------------------------------------------------------------------------------------
 */
package pe.com.sedapal.agi.util;

import java.io.Serializable;

/**
 * Clase para las constantes
 */
public class UConstante implements Serializable{
		
	/** Variable autogenerada */
	private static final long serialVersionUID = -2083917677128950397L;	
	/** Variable para almacenar el paquete  para la configuracion de los logs */
	public static final String LOG_CONFIG_FILE = "pe.com.sedapal.agi.recursos";	
	public static final String NOMBRE_PARAMETRO_ESTADOS_CANCELACION = "Listado de estados de cancelacion";	
	public static final String ESTADO_CANCELACION_REGISTRADO = "Registrado";
	public static final String ESTADO_CANCELACION_EN_REVISION= "En revision";
	public static final String ESTADO_CANCELACION_APROBADO = "Aprobado";
	public static final String ESTADO_CANCELACION_RECHAZADO = "Rechazado";
	public static final String ESTADO_CANCELACION_EJECUTADO = "Ejecutado";
	
}
