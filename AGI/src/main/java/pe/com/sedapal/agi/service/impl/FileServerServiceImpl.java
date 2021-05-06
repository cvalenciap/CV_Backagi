package pe.com.sedapal.agi.service.impl;
 
import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.model.response_objects.UploadResponse;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.util.UJson;

@Service
public class FileServerServiceImpl implements IFileServerService{
	private static final Logger LOGGER = Logger.getLogger(FileServerServiceImpl.class);	
	static String endpointServidor;	
	static String rutaAgi ;
	
	@Autowired
	Environment env;
	
	@Override
	public UploadResponse cargarArchivoFileServer(byte[] bytes, String nombreArchivo,String extension) {
		   UploadResponse upload = null;
		try {			
			endpointServidor = env.getProperty("app.config.servidor.fileserver");
			rutaAgi = env.getProperty("app.config.paths.informaciondocumentada");
			HttpClient client = HttpClientBuilder.create().build();	    	
	    	HttpPut put = new HttpPut(endpointServidor+"/"+rutaAgi);
	    	
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    	
	    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
	    	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	    	builder.addBinaryBody
	    	  ("file", bytes, ContentType.DEFAULT_BINARY, nombreArchivo+"."+extension);
	    	// 
	    	HttpEntity entity = builder.build();
	    	
	    	put.setEntity(entity);
	    	HttpResponse responseHttp = client.execute(put);
	    	String responseJSON = EntityUtils.toString(responseHttp.getEntity());
	    	upload = UJson.convertirJsonATipo(responseJSON, UploadResponse.class);
    	
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	
    	return upload;
	}
	 
	
	//CGuerra Inicio
	@Override
	public UploadResponse cargarArchivoFileServerDoc(byte[] bytes, String nombreArchivo) {
		UploadResponse upload = null;
		try {									
			endpointServidor = env.getProperty("app.config.servidor.fileserver");
			rutaAgi 		 = env.getProperty("app.config.paths.informaciondocumentada");
			HttpClient client = HttpClientBuilder.create().build();
	    	
	    	HttpPut put = new HttpPut(endpointServidor+"/"+rutaAgi);
	    	
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    	
	    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
	    	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	    	builder.addBinaryBody
	    	  ("file", bytes, ContentType.DEFAULT_BINARY, nombreArchivo);
	    	HttpEntity entity = builder.build();	    	
	    	put.setEntity(entity);
	    	HttpResponse responseHttp = client.execute(put);
	    	String responseJSON = EntityUtils.toString(responseHttp.getEntity());
	    	upload = UJson.convertirJsonATipo(responseJSON, UploadResponse.class);
    	
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	
    	return upload;
	}
	
	
	
	//CGuerra Fin
	
	@Override
	public UploadResponse cargarArchivoFileServerPdf(byte[] bytes, String nombreArchivo) {
		UploadResponse upload = null;
		try {
			endpointServidor = env.getProperty("app.config.servidor.fileserver");
			rutaAgi = env.getProperty("app.config.servidor.fileserver.agi");
			String informaciondocumentada = env.getProperty("app.config.paths.informaciondocumentada");
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpPut put = new HttpPut(endpointServidor+"/"+informaciondocumentada);
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
	    	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	    	builder.addBinaryBody
	    	  ("file", bytes, ContentType.DEFAULT_BINARY, nombreArchivo);
	    	// 
	    	HttpEntity entity = builder.build();
	    	
	    	put.setEntity(entity);
	    	HttpResponse responseHttp = client.execute(put);
	    	String responseJSON = EntityUtils.toString(responseHttp.getEntity());
	    	upload = UJson.convertirJsonATipo(responseJSON, UploadResponse.class);
    	
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	return upload;
	}
	
	@Override
	public UploadResponse cargarArchivoFileServerPdfAdjunto(byte[] bytes, String nombreArchivo) {
		UploadResponse upload = null;
		try {
			endpointServidor = env.getProperty("app.config.servidor.fileserver");
			rutaAgi = env.getProperty("app.config.servidor.fileserver.agi");
		
			HttpClient client = HttpClientBuilder.create().build();
			HttpPut put = new HttpPut(endpointServidor+"/"+rutaAgi);
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
	    	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	    	builder.addBinaryBody
	    	  ("file", bytes, ContentType.DEFAULT_BINARY, nombreArchivo);
	    	// 
	    	HttpEntity entity = builder.build();
	    	
	    	put.setEntity(entity);
	    	HttpResponse responseHttp = client.execute(put);
	    	String responseJSON = EntityUtils.toString(responseHttp.getEntity());
	    	upload = UJson.convertirJsonATipo(responseJSON, UploadResponse.class);
    	
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	return upload;
	}
	

	@Override
	public UploadResponse cargarArchivoFileServerPdfAsistencia(byte[] bytes, String nombreArchivo) {
		UploadResponse upload = null;
			try {
				endpointServidor = env.getProperty("app.config.servidor.fileserver");
				rutaAgi = env.getProperty("app.config.servidor.fileserver.agi");
				String informaciondocumentada = env.getProperty("app.config.paths.informaciondocumentada");
				HttpClient client = HttpClientBuilder.create().build();
		    	
		    	HttpPut put = new HttpPut(endpointServidor+"/"+informaciondocumentada);
		    	
		    	HttpHeaders headers = new HttpHeaders();
		    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		    	
		    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
		    	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		    	builder.addBinaryBody
		    	  ("file", bytes, ContentType.DEFAULT_BINARY, nombreArchivo);
		    	HttpEntity entity = builder.build();
		    	
		    	put.setEntity(entity);
		    	
		    	HttpResponse responseHttp = client.execute(put);
		    	String responseJSON = EntityUtils.toString(responseHttp.getEntity());
		    	upload = UJson.convertirJsonATipo(responseJSON, UploadResponse.class);
    	
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	return upload;
	}


	@Override
	public UploadResponse eliminarArchivoFileServerPdfAsistencia(String nombreArchivo) {
		UploadResponse upload = null;
			try {
				endpointServidor = env.getProperty("app.config.servidor.fileserver");
				rutaAgi = env.getProperty("app.config.servidor.fileserver.agi");
				String informaciondocumentada = env.getProperty("app.config.paths.informaciondocumentada");				
		    	HttpDelete delete = new HttpDelete(endpointServidor+"/"+informaciondocumentada+nombreArchivo );
		    	delete.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		    	delete.addHeader("Content-type","application/json; charset=UTF-8");
		    	delete.addHeader("Accept","application/json; charset=UTF-8");
		    	delete.addHeader("Accept-Encoding","gzip, deflate");
		        delete.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
		        delete.addHeader("Accept-Language","en-US,en;q=0.5");
		        delete.addHeader("Cookie","JSESSIONID=DC83EEF14C3B1F309ADF125B92A62629; _ga=GA1.2.1119685758.1496394454; _gid=GA1.2.682909819.1496644441");
		        delete.addHeader("Connection", "keep-alive");
		        delete.addHeader("X-CSRF-Token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBR0kxNDI0NSIsImlhdCI6MTU1NTk2MjY2MywiZXhwIjoxNTU1OTYyNjgzfQ.GbjI4UXAvsBk0Ll_lWhxjsw1vUUXmW-1l9OfTo0Fl0MlBC8gPpRmyAasU1uDeSZX_PAaXz2JsxVwBagCQQQ2yA");
		        delete.addHeader("Access-Control-Allow-Origin", "*");
		        delete.addHeader("Access-Control-Allow-Methods", "*");
		        delete.addHeader("Access-Control-Allow-Headers", "*");
		        delete.addHeader("Cache-Control", "no-cache");
		    	
		    	HttpClient client = HttpClients.custom().build();		    			
		    	HttpResponse response = client.execute(delete);		    	
		    	int responseCode = response.getStatusLine().getStatusCode();
		    	String statusPhrase = response.getStatusLine().getReasonPhrase();
		    	response.getEntity().getContent().close();
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	return upload;
	}
	
	@Override
	public void insertarRuta(String ruta) {
		this.rutaAgi = ruta;
		
	}
	
	@Override
	public UploadResponse cargarArchivoFileServerCapacitacion(byte[] bytes, String nombreArchivo) {
		UploadResponse upload = null;
			try {
				endpointServidor = env.getProperty("app.config.servidor.fileserver");
				rutaAgi = env.getProperty("app.config.servidor.fileserver.agi");
				String informaciondocumentada = env.getProperty("app.config.paths.informaciondocumentada");
				HttpClient client = HttpClientBuilder.create().build();		    	
		    	HttpPut put = new HttpPut(endpointServidor+"/"+informaciondocumentada);		    	
		    	HttpHeaders headers = new HttpHeaders();
		    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);		    	
		    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
		    	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		    	builder.addBinaryBody
		    	  ("file", bytes, ContentType.DEFAULT_BINARY, nombreArchivo);
		    	// 
		    	HttpEntity entity = builder.build();
		    	
		    	put.setEntity(entity);
		    	
		    	HttpResponse responseHttp = client.execute(put);
		    	String responseJSON = EntityUtils.toString(responseHttp.getEntity());
		    	upload = UJson.convertirJsonATipo(responseJSON, UploadResponse.class);
    	
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	return upload;
	}

	@Override
	public boolean eliminarArchivoFileServerCapacitacion(String nombreArchivo) {
		boolean elimino = false;
		UploadResponse upload = null;
		
		
			try {
				endpointServidor = env.getProperty("app.config.servidor.fileserver");
				rutaAgi = env.getProperty("app.config.servidor.fileserver.agi");
				String informaciondocumentada = env.getProperty("app.config.paths.informaciondocumentada");
				
		    	HttpDelete delete = new HttpDelete(endpointServidor+informaciondocumentada+nombreArchivo);
		    	delete.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		        delete.addHeader("Content-type","application/json; charset=UTF-8");
		        delete.addHeader("Accept","application/json; charset=UTF-8");
		        delete.addHeader("Accept-Encoding","gzip, deflate");
	            delete.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
	            delete.addHeader("Accept-Language","en-US,en;q=0.5");
	            delete.addHeader("Cookie","JSESSIONID=DC83EEF14C3B1F309ADF125B92A62629; _ga=GA1.2.1119685758.1496394454; _gid=GA1.2.682909819.1496644441");
	            delete.addHeader("Connection", "keep-alive");
	            delete.addHeader("X-CSRF-Token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBR0kxNDI0NSIsImlhdCI6MTU1NTk2MjY2MywiZXhwIjoxNTU1OTYyNjgzfQ.GbjI4UXAvsBk0Ll_lWhxjsw1vUUXmW-1l9OfTo0Fl0MlBC8gPpRmyAasU1uDeSZX_PAaXz2JsxVwBagCQQQ2yA");
	            delete.addHeader("Access-Control-Allow-Origin", "*");
	            delete.addHeader("Access-Control-Allow-Methods", "*");
	            delete.addHeader("Access-Control-Allow-Headers", "*");
	            delete.addHeader("Cache-Control", "no-cache");
		    

		    	HttpClient client = HttpClients.custom().build();
		    			
		    	HttpResponse response = client.execute(delete);

		    	int responseCode = response.getStatusLine().getStatusCode();
		    	
		    	if(responseCode == 200) {
		    		String statusPhrase = response.getStatusLine().getReasonPhrase();
		    		response.getEntity().getContent().close();	
		    		elimino = true;
		    	} 
		    	
		    	
    	
		} catch (ClientProtocolException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (IOException e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		} catch (Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
    	return elimino;
	}


	@Override
	public UploadResponse eliminarArchivoFileServerPdfAdjuno(String nombreArchivo) {
		UploadResponse upload = null;
		try {
			endpointServidor = env.getProperty("app.config.servidor.fileserver");
			rutaAgi = env.getProperty("app.config.servidor.fileserver.agi");
	    	HttpDelete delete = new HttpDelete(endpointServidor+nombreArchivo);
	    	delete.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	    	delete.addHeader("Content-type","application/json; charset=UTF-8");
	    	delete.addHeader("Accept","application/json; charset=UTF-8");
	    	delete.addHeader("Accept-Encoding","gzip, deflate");
	        delete.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
	        delete.addHeader("Accept-Language","en-US,en;q=0.5");
	        delete.addHeader("Cookie","JSESSIONID=DC83EEF14C3B1F309ADF125B92A62629; _ga=GA1.2.1119685758.1496394454; _gid=GA1.2.682909819.1496644441");
	        delete.addHeader("Connection", "keep-alive");
	        delete.addHeader("X-CSRF-Token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBR0kxNDI0NSIsImlhdCI6MTU1NTk2MjY2MywiZXhwIjoxNTU1OTYyNjgzfQ.GbjI4UXAvsBk0Ll_lWhxjsw1vUUXmW-1l9OfTo0Fl0MlBC8gPpRmyAasU1uDeSZX_PAaXz2JsxVwBagCQQQ2yA");
	        delete.addHeader("Access-Control-Allow-Origin", "*");
	        delete.addHeader("Access-Control-Allow-Methods", "*");
	        delete.addHeader("Access-Control-Allow-Headers", "*");
	        delete.addHeader("Cache-Control", "no-cache");
	    	HttpClient client = HttpClients.custom().build();		    			
	    	HttpResponse response = client.execute(delete);
	    	int responseCode = response.getStatusLine().getStatusCode();
	    	String statusPhrase = response.getStatusLine().getReasonPhrase();
	    	response.getEntity().getContent().close();
	} catch (ClientProtocolException e) {
		String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
		LOGGER.error(error[1], e);
	} catch (IOException e) {
		String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
		LOGGER.error(error[1], e);
	} catch (Exception e) {
		String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
		LOGGER.error(error[1], e);
	}
	return upload;
}

}
