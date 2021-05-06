package pe.com.sedapal.agi.service.impl;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.dao.ICorreoDatosDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Correo;
import pe.com.sedapal.agi.model.CorreoCabecera;
import pe.com.sedapal.agi.model.CorreoDestinatario;
import pe.com.sedapal.agi.model.CorreoVariable;
import pe.com.sedapal.agi.model.FormatoHTML;
import pe.com.sedapal.agi.model.enums.CorreoConstante;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.service.ICorreoDatosService;
import pe.com.sedapal.agi.util.UConstante;
import org.springframework.core.env.Environment;

@Service
public class CorreoDatosServiceImpl implements ICorreoDatosService{
	
	private static final Logger LOGGER = Logger.getLogger(CorreoDatosServiceImpl.class);	
	

	
	@Autowired
	private ICorreoDatosDAO dao;
	
	@Autowired
	Environment env;
	
	public Error getError() {
		return this.dao.getError();
	}
	
	public Error getErrorNull() {
		return this.dao.getErrorNull();
	}
	


	@Override
	public Correo obtenerCorreo(List<Long> listaId, String tipoCorreo) throws Exception {
		this.getErrorNull();
		Correo objeto = new Correo();
		String linkSistema =env.getProperty("app.config.link.correo");
		//Obtener Configuracion del Correo
		List<Constante> listaParametros = this.dao.obtenerDatosCorreo();
		if(this.getError()!=null)	return null;
		String correoRemitente	= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_CORREO_REMITENTE.toString());
		String nombreRemitente	= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_USUARIO_REMITENTE.toString());
		String claveRemitente	= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_PASSWORD_REMITENTE.toString());
		String rutaImagen		= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_RUTA_IMAGEN.toString());
		String imagenFirma		= rutaImagen + this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_IMAGEN_FIRMA.toString());
		String imagenPie		= rutaImagen + this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_IMAGEN_PIE.toString());
		
		//Obtener Variables del Mensaje y sus Valores
		CorreoVariable variable = new CorreoVariable();
		variable.setNombreVariable(this.dao.obtenerVariableFormato(tipoCorreo));
		if(this.getError()!=null)	return null;
		variable.setValorVariable(this.dao.obtenerValorFormato(listaId, tipoCorreo, imagenFirma, imagenPie, linkSistema));
		if(this.getError()!=null)	return null;
		objeto.setVariable(variable);
		
		//Obtener Plantilla del Mensaje y Asunto
		FormatoHTML formato = this.dao.obtenerFormato(tipoCorreo);
		if(this.getError()!=null)	return null;
		if(formato!=null) {
			Blob plantilla = formato.getFormatoHtml();
			String mensaje = new String(plantilla.getBytes(1l, (int)plantilla.length()));
			objeto.setMensaje(mensaje);
			objeto.setAsunto(formato.getTitulo());
		}
		
		//Obtener Destinatario
		CorreoCabecera cabecera = new CorreoCabecera();
		cabecera.setCorreoDestino(this.dao.obtenerDestinatario(listaId, tipoCorreo));
		if(this.getError()!=null)	return null;
		
		//Obtener Remitente
		cabecera.setCorreoRemitente(correoRemitente);
		cabecera.setNombreRemiente(nombreRemitente);
		cabecera.setClaveRemitente(claveRemitente);
		objeto.setCorreoCabecera(cabecera);
		
		return objeto;
	}
	
	@Override
	public List<Correo> obtenerListaCorreo(List<Long> listaId, String tipoCorreo) throws Exception {
		this.getErrorNull();
		List<Correo> lista =  new ArrayList<>();
		String linkSistema =env.getProperty("app.config.link.correo");
		//Obtener Configuracion del Correo
		List<Constante> listaParametros = this.dao.obtenerDatosCorreo();
		if(this.getError()!=null)	return null;
		String correoRemitente	= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_CORREO_REMITENTE.toString());
		String nombreRemitente	= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_USUARIO_REMITENTE.toString());
		String claveRemitente	= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_PASSWORD_REMITENTE.toString());
		String rutaImagen		= this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_RUTA_IMAGEN.toString());
		String imagenFirma		= rutaImagen + this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_IMAGEN_FIRMA.toString());
		String imagenPie		= rutaImagen + this.obtenerValorConstante(listaParametros, CorreoConstante.CONSTANTE_IMAGEN_PIE.toString());
		
		//Obtener Variables del Mensaje
		CorreoVariable variable = new CorreoVariable();
		variable.setNombreVariable(this.dao.obtenerVariableFormato(tipoCorreo));
		if(this.getError()!=null)	return null;
		
		//Obtener Plantilla del Mensaje y Asunto
		FormatoHTML formato = this.dao.obtenerFormato(tipoCorreo);
		if(this.getError()!=null)	return null;
		String mensaje = "";
		if(formato!=null) {
			Blob plantilla = formato.getFormatoHtml();
			mensaje = new String(plantilla.getBytes(1l, (int)plantilla.length()));
		}
				
		//Obtener Destinatario
		List<CorreoDestinatario> listaDestinatario = this.dao.obtenerListaDestinatario(listaId, tipoCorreo);
		if(this.getError()!=null)	return null;
		for(CorreoDestinatario parametro : listaDestinatario) {
			CorreoCabecera cabecera = new CorreoCabecera();
			Correo objeto = new Correo();
			if(formato!=null) {
				objeto.setMensaje(mensaje);
				objeto.setAsunto(formato.getTitulo());
			}
			
			List<String> listaCorreo = new ArrayList<>();
			List<String> listaOtro   = this.dao.obtenerDestinatarioOtro(listaId, tipoCorreo);
			if(listaOtro.size()==0) {
				listaCorreo.add(parametro.getCorreo());	
			} else {
				if(!listaOtro.contains(parametro.getCorreo()))	listaOtro.add(parametro.getCorreo());
				listaCorreo = listaOtro;
			}
			cabecera.setCorreoDestino(listaCorreo);
			
			//Obtener Valores del Mensaje por Destinatario
			if(parametro.getIdDestinatario()!=null) {
				if(listaId.size()==1)	listaId.add(null);
				listaId.add(parametro.getIdDestinatario());
			} else if(parametro.getIdEquipo()!=null) {
				if(listaId.size()==1)	listaId.add(null);
				listaId.add(parametro.getIdEquipo());
			}
			
			CorreoVariable valorVariable = new CorreoVariable();
			valorVariable.setNombreVariable(variable.getNombreVariable());
			valorVariable.setValorVariable(this.dao.obtenerValorFormato(listaId, tipoCorreo, imagenFirma, imagenPie, linkSistema));
			if(this.getError()!=null)	return null;
			objeto.setVariable(valorVariable);
			
			//Obtener Remitente
			cabecera.setCorreoRemitente(correoRemitente);
			cabecera.setNombreRemiente(nombreRemitente);
			cabecera.setClaveRemitente(claveRemitente);
			objeto.setCorreoCabecera(cabecera);
			lista.add(objeto);
		}
		return lista;
	}
	
	private String obtenerValorConstante(List<Constante> lista, String nombre) {
		for(Constante item: lista) {
			if(item.getV_descons().toUpperCase().equals(nombre.toUpperCase())) {
				return item.getV_valcons();
			}
		}
		return "";
	}

}