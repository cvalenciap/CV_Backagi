package pe.com.sedapal.agi.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

public class UArchivo {
	@Autowired
	Environment env;
	
	public  String carpetaResources = env.getProperty("app.config.paths.toke.directory");
	public static OutputStream convertirInputStreamAOutputStream(InputStream entrada) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int nRead;
	    byte[] data = new byte[1024];
	    while ((nRead = entrada.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, nRead);
	    }
	 
	    buffer.flush();
	   // byte[] byteArray = buffer.toByteArray();
		return buffer;
	}
	
	public static File convert(MultipartFile file,String rutaCarpeta) throws IOException {    
	    File convFile = new File(rutaCarpeta+file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    
	    return convFile;
	}
	
	public static void crearCarpeta(String ruta) {
		File carpet = new File(ruta);
    	carpet.mkdir();
	}
	
	public static void eliminarCarpeta(String ruta) throws IOException {
		Path path = Paths.get(ruta);
		eliminarDirectorio(path);
	}
	
	private static void eliminarDirectorio(Path path) throws IOException {
	  if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
	    try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
	      for (Path entry : entries) {
	    	eliminarDirectorio(entry);
	      }
	    }
	  }
	  Files.delete(path);
	}
	
}