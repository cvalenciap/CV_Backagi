package pe.com.sedapal.agi.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.dao.IPlantillaDAO;
import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Plantilla;
import pe.com.sedapal.agi.model.request_objects.ConocimientoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PlantillaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IPlantillaService;
import pe.com.sedapal.agi.util.UArchivo;
import pe.com.sedapal.agi.util.UConstante;
import pe.sedapal.componentes.fs.ClientFS;
import pe.com.sedapal.agi.security.config.UserAuth;
@Service
public class PlantillaServiceImpl implements IPlantillaService{
	
	@Autowired
	SessionInfo session;	
	@Autowired
	Environment env;	
	
	private String endpointServidor;	
	@Autowired
	private IPlantillaDAO dao;
	
	private String UArchivocarpetaResources;
	private String videoplantilla;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}
		@Override
	public List<Plantilla> obtenerPlantilla(PlantillaRequest plantillaRequest, PageRequest pageRequest){
		return this.dao.obtenerPlantilla(plantillaRequest, pageRequest);
	}
		@Override
		public Plantilla agregarPlantilla(Plantilla plantilla,Optional<MultipartFile> file){
			Plantilla respuesta= null;
			try {
				endpointServidor = env.getProperty("app.config.servidor.fileserver");
				UArchivocarpetaResources =env.getProperty("app.config.paths.toke.directory");
				videoplantilla =env.getProperty("app.config.servidor.fileserver.agi");
				ClientFS cliente = new ClientFS();
	    		MultipartFile archivo = null;
	    		UArchivo.crearCarpeta(UArchivocarpetaResources+"carpetagenerador");	    		
	    		File archivoObtenido = UArchivo.convert(file.get(),UArchivocarpetaResources+"carpetagenerador/");	    		
	    		System.out.println("Va a subir Archivo");
        		String rutaSaliente = videoplantilla;
        		String rutaFileServer = cliente.subirArchivo(endpointServidor+"/"+rutaSaliente, archivoObtenido);
        		System.out.println("Subio Archivo");
        		rutaFileServer = rutaFileServer.replace(endpointServidor,"");
        		plantilla.setRutplan(rutaFileServer);
        		System.out.println(plantilla.getNomplan());
        		System.out.println(file.get().getOriginalFilename());
        		plantilla.setNomplan(file.get().getOriginalFilename());
        		UArchivo.eliminarCarpeta(UArchivocarpetaResources+"carpetagenerador");
        		
        		DatosAuditoria datosAuditoria = new DatosAuditoria();
        		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            	datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
            	datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
            	plantilla.setIdColaborador(new Long(((UserAuth)principal).getCodPerfil()));
            	plantilla.setDatosAuditoria(datosAuditoria);
            	return this.dao.agregarPlantilla(plantilla);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return respuesta;
		}
		
		@Override
		public List<Conocimiento> obtenerConocimiento(ConocimientoRequest conocimientoRequest, PageRequest pageRequest){
			return this.dao.obtenerConocimiento(conocimientoRequest, pageRequest);
		}
		
		@Override
		public Boolean eliminarPlantilla(Plantilla plantilla){	
			Boolean respuesta= null;
    			try {
    				ClientFS cliente = new ClientFS();
    	    		MultipartFile archivo = null;     	    		
    	        		if(plantilla.getRutplan() != null) {
    	        			System.out.println("Va a eliminar Archivo");
    	        			cliente.eliminarArchivo(plantilla.getRutplan());
    	        			System.out.println("Elimino Archivo");
    	        		}	
    	        		return this.dao.eliminarPlantilla(plantilla);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			return respuesta;			
		}
		
		@Override
		public Boolean eliminarConocimiento(Conocimiento conocimiento){	    		  					
    	        		return this.dao.eliminarConocimiento(conocimiento);
    				
		}
		
		
		
}