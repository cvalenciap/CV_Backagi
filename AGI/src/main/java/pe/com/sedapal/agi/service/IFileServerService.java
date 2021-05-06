package pe.com.sedapal.agi.service;
  
import pe.com.sedapal.agi.model.response_objects.UploadResponse;

public interface IFileServerService {
	UploadResponse cargarArchivoFileServer(byte[] bytes,String nombreArchivo,String extension);
	//CGuerra
	UploadResponse cargarArchivoFileServerDoc(byte[] bytes,String nombreArchivo);
	//CGuerra
	UploadResponse cargarArchivoFileServerPdf(byte[] bytes,String nombreArchivo);
	UploadResponse cargarArchivoFileServerPdfAdjunto(byte[] bytes,String nombreArchivo);
	UploadResponse cargarArchivoFileServerPdfAsistencia(byte[] bytes,String nombreArchivo);
	UploadResponse cargarArchivoFileServerCapacitacion(byte[] bytes,String nombreArchivo);
	UploadResponse eliminarArchivoFileServerPdfAsistencia(String nombreArchivo);
	UploadResponse eliminarArchivoFileServerPdfAdjuno(String nombreArchivo);

	void insertarRuta(String ruta);
	boolean eliminarArchivoFileServerCapacitacion(String nombreArchivo);
}
