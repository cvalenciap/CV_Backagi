package pe.com.sedapal.agi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import pe.com.gmd.util.exception.MensajeExceptionUtil;



public class UCorreo implements Serializable {
		
	private static final long serialVersionUID = -8346728051167393825L;	
	
	private static final Logger LOGGER = Logger.getLogger(UCorreo.class);	

	public List<String> separadorCadena(String sCadenaValores, String sSeparador) {		
		List<String> lstCadena = new ArrayList<String>();
		try{			
			int cantidadPalabra = 0;
			int ctaPalabra = 0;
			
			if (sCadenaValores == null){
				return null;
			}
			if (sCadenaValores.trim().equals("")){
				return null;
			}
			
			StringTokenizer cadenaRecortada = new StringTokenizer(sCadenaValores, sSeparador);
			cantidadPalabra = cadenaRecortada.countTokens();
	
			for (ctaPalabra = 0; ctaPalabra < cantidadPalabra; ctaPalabra++){
				lstCadena.add(cadenaRecortada.nextToken());
			}		
		
		}catch(Exception exception){
			String[] error = MensajeExceptionUtil.obtenerMensajeError(exception);
			LOGGER.error(error[1], exception);
		}
		
		return lstCadena;
	}
	
	public String obtenerEstilosCorreoHtml() {		
		String estiloUsado = "";
		try{
		
		/* Estilos para correos con colores */
		StringBuffer estilosColor = new StringBuffer();
		estilosColor
				.append(".tablabonita{\r\n" + 
						"table {     font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\r\n" + 
						"    font-size: 12px;    margin: 45px;     width: 480px; text-align: justify;    border-collapse: collapse;  }\r\n" + 
						"}\r\n" + 
						"th {   \r\n" + 
						"  font-size: 14px;     font-weight: black;     padding: 8px;     background: #b9c9fe;\r\n" + 
						"    border-top: 4px solid #aabcfe;    border-bottom: 1px solid #fff; color: #039; }\r\n" + 
						"\r\n" + 
						"td {    padding: 8px;         border-bottom: 1px solid #fff;\r\n" + 
						"    color: #fffff;    border-top: 1px solid transparent; }\r\n" + 
						"\r\n" + 
						"tr:hover td { background: #d0dafd; color: #339; }\r\n") ;
		
				estiloUsado += estilosColor.toString();
			    
        } catch (Exception exception) {
        	String[] error = MensajeExceptionUtil.obtenerMensajeError(exception);
        	LOGGER.error(error[1], exception);        	
        }
		
		return estiloUsado;
	}
	
	public String toASCII(String html) {		
        try {
            html = html.replaceAll("�", "&aacute;");
            html = html.replaceAll("�", "&eacute;");
            html = html.replaceAll("�", "&iacute;");
            html = html.replaceAll("�", "&oacute;");
            html = html.replaceAll("�", "&uacute;");
            html = html.replaceAll("�", "&Aacute;");
            html = html.replaceAll("�", "&Eacute;");
            html = html.replaceAll("�", "&Iacute;");
            html = html.replaceAll("�", "&Oacute;");
            html = html.replaceAll("�", "&Uacute;");
            html = html.replaceAll("�", "&ntilde;");
            html = html.replaceAll("�", "&Ntilde;");
            html = html.replaceAll("�", "&Uuml;");
            html = html.replaceAll("�", "&uuml;");
            html = html.replaceAll("�", "");
            html = html.replaceAll("�", "");
            html = html.replaceAll("�", "&deg;");
            html = html.replaceAll("�", "&ordm;");
            html = html.replaceAll("�", "&iquest;");
            html = html.replaceAll("á", "&aacute;");
            html = html.replaceAll("é", "&eacute;");
            html = html.replaceAll("®", "&reg;");
            html = html.replaceAll("ì", "&iacute;");
            html = html.replaceAll("�", "&iacute;");
            html = html.replaceAll("ó", "&oacute;");
            html = html.replaceAll("ú", "&uacute;");
            html = html.replaceAll("n~", "&ntilde;");
            html = html.replaceAll("º", "&ordm;");
            html = html.replaceAll("ª", "&ordf;");
            html = html.replaceAll("Ã¡", "&aacute;");
            html = html.replaceAll("ñ", "&ntilde;");
            html = html.replaceAll("Ñ", "&Ntilde;");
            html = html.replaceAll("Ã±", "&ntilde;");
            html = html.replaceAll("n~", "&ntilde;");
            html = html.replaceAll("Ú", "&Uacute;");
            html = html.replaceAll("�", "");
            html = html.replaceAll("�", "");
            html = html.replaceAll("&Acirc;", "");
            html = html.replaceAll("&acirc;", "");
            
        } catch (Exception exception) {
        	String[] error = MensajeExceptionUtil.obtenerMensajeError(exception);
        	LOGGER.error(error[1], exception);        	
        }        
        return html;
    }

}