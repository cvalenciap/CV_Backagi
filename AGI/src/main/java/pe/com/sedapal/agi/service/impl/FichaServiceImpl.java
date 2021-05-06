package pe.com.sedapal.agi.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.dao.IFichaDAO;
import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.FichaTecnica;
import pe.com.sedapal.agi.model.Plantilla;
import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IExcel;
import pe.com.sedapal.agi.service.IFichaService;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.com.sedapal.agi.util.UArchivo;
import pe.com.sedapal.agi.util.UConstante;
import pe.sedapal.componentes.fs.ClientFS;

@Service
public class FichaServiceImpl implements IFichaService{
	
	@Autowired
	SessionInfo session;
	@Autowired
	Environment env;
	@Autowired
	private IExcel service;	
	private String endpointServidor;
	@Autowired
	private IFileServerService fileServerService;
	@Autowired
	private IFichaDAO dao;
	
	private String UArchivocarpetaResources;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public FichaTecnica obtenerFicha(FichaTecnicaRequest fichaRequest){
		FichaTecnica ficha = this.dao.obtenerFicha(fichaRequest);
		return ficha;
	}
	
	@Override
	public FichaTecnica crearFicha(FichaTecnica ficha,Optional<MultipartFile> file) {		
		try {
			endpointServidor = env.getProperty("app.config.servidor.fileserver");
			UArchivocarpetaResources = env.getProperty("app.config.paths.toke.directory");
			ClientFS cliente = new ClientFS();
    		MultipartFile archivo = null;
    		UArchivo.crearCarpeta(UArchivocarpetaResources+"carpetagenerador");
    		
    		if(file.isPresent()) {
			//Eliminamos Archivo
				if (ficha.getRutaGrafico() != null) {
					this.fileServerService.eliminarArchivoFileServerPdfAdjuno(ficha.getRutaGrafico());
				}
    		File archivoObtenido = UArchivo.convert(file.get(),UArchivocarpetaResources+"carpetagenerador/");
    		
    		System.out.println("Va a subir Archivo");
    		String rutaSaliente = env.getProperty("app.config.paths.informaciondocumentada"); 
    		String rutaFileServer = cliente.subirArchivo(endpointServidor+"/"+rutaSaliente, archivoObtenido);
    		System.out.println("Subio Archivo");
    		String RutaFInal = rutaFileServer.replace(endpointServidor, "");
    		ficha.setRutaGrafico(RutaFInal);
    		System.out.println(ficha.getNombreGrafico());
    		System.out.println(file.get().getOriginalFilename());
    		ficha.setNombreGrafico(file.get().getOriginalFilename());
    		UArchivo.eliminarCarpeta(UArchivocarpetaResources+"carpetagenerador");
    		}
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FichaTecnica resultado = this.dao.guardarFicha(ficha, null, (((UserAuth)principal).getUsername()));
    	return resultado;
		}
	
	@Override
	public FichaTecnica actualizarFicha(FichaTecnica ficha, Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FichaTecnica resultado = this.dao.guardarFicha(ficha, codigo, (((UserAuth)principal).getUsername()));
		return resultado;
	}
	
	@Override
	public Boolean eliminarFicha(Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.eliminarFicha(codigo, null, (((UserAuth)principal).getUsername()));
	}
	
	/*Excel Inicio*/	
	public String generarExcelPlazo(FichaTecnicaRequest fichaTecnicaRequest) throws IOException {
		FichaTecnica Ficha = new FichaTecnica();	
		Ficha = this.dao.obtenerFicha(fichaTecnicaRequest);					
		return service.escribirExcelFicha(Ficha);
	}
	/*Excel Fin*/


}