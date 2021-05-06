package pe.com.sedapal.agi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.dao.IAsistenciaDAO;
import pe.com.sedapal.agi.dao.IPreguntaCursoDAO;
import pe.com.sedapal.agi.model.Asistencia;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AsistenciaRequest;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.UploadResponse;
import pe.com.sedapal.agi.service.IAsistenciaService;
import pe.com.sedapal.agi.service.IExcel;
import pe.com.sedapal.agi.service.IFileServerService;
import pe.sedapal.componentes.fs.ClientFS;

@Service
public class AsistenciaServiceImpl implements IAsistenciaService{
	@Autowired IAsistenciaDAO dao;
	@Autowired
	private IFileServerService fileServerService;
	@Autowired
	private IExcel service;	
	
	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}
	
	@Override
	public List<Asistencia> obtenerAsistencia(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest){
		return this.dao.obtenerAsistencia(asistenciaRequest, paginaRequest);
	}
	
	@Override
	public Asistencia crearDocumento(Asistencia asistencia,Optional<MultipartFile> file) {
		
		try {
			if(asistencia.getArchivoAntiguo()!=null) {
				this.fileServerService.eliminarArchivoFileServerPdfAsistencia(asistencia.getArchivoAntiguo());
			}
			ClientFS cliente = new ClientFS();   
    		if(file.isPresent()){
    			UploadResponse uploadArchivoDoc = this.fileServerService.cargarArchivoFileServerPdfAsistencia(file.get().getBytes(), file.get().getOriginalFilename());
    			System.out.println(file.get().getOriginalFilename());
    			
    			String rutaArchivo = uploadArchivoDoc.getUrl();
    			List<Trabajador> traba= new ArrayList<>();
    			for(Trabajador trab:asistencia.getListTrabajador()) {
    				trab.setRutaDocumento(rutaArchivo);
    				traba.add(trab);
    				
    			}
    			
 
    			asistencia.setListTrabajador(traba);
    		}else {
    			
    			List<Trabajador> traba= new ArrayList<>();
    			for(Trabajador trab:asistencia.getListTrabajador()) {
    				if(asistencia.getDescEliminar().equals("2")) {
    					trab.setRutaDocumento(null);
    					trab.setJustificacion(null);
    					trab.setArchivoAntiguo(null);
    					traba.add(trab);
    				}
    				
    			}
    			asistencia.setListTrabajador(traba);
    			
    		}
    		
		}catch(IOException e) {
			e.printStackTrace();
		}
	
		return this.dao.actualizarAsistencia(asistencia);
		
	}
	
	@Override
	public Asistencia actualizarAsistencia(Asistencia asistencia) {
		return this.dao.actualizarAsistencia(asistencia);
	}
	
	@Override
	public List<Asistencia> obtenerEvaluacion(AsistenciaRequest asistenciaRequest, PageRequest paginaRequest){
		return this.dao.obtenerEvaluacion(asistenciaRequest, paginaRequest);
	}
	
	@Override
	public Asistencia actualizarEvaluacion(Asistencia asistencia) {
		return this.dao.actualizarEvaluacion(asistencia);
	}
	
	@Override
	public List<Trabajador> obtenerEmpleadoEvaluacion(Trabajador trabajador,PageRequest paginaRequest){
		return this.dao.obtenerEmpleadoEvaluacion(trabajador, paginaRequest);
	}
	
	@Override
	public byte[] generarPdf(Trabajador trabajador, PageRequest pageRequest) throws IOException {
		List<Trabajador> trab= new ArrayList<>();
		List<Trabajador> empleado= new ArrayList<>();	
		
		trab=this.dao.obtenerEmpleadoEvaluacion(trabajador, pageRequest);
		Integer cont = 0;
		long x = 0;
		Integer not=0;
		
		if(trab.size()>0) {
			for(Trabajador lista:trab) {
				if(lista.getNota()==null) {
					lista.setNota(Long.valueOf(not));
				}
				lista.setNomCurso(trabajador.getNomCurso());
				cont=cont +1;
				x=Long.valueOf(cont);
				lista.setItemColumna(x);
			}
			
			
		}
		
		return service.generarPdfEvaluacion(trab);
	}
	
	public List<Trabajador> obtenerEmpleadoSesion(Sesion sesion){
		Trabajador trabajador = new Trabajador();
		trabajador.setIdSesion(sesion.getIdSesion());
		trabajador.setIdCapacitacion(sesion.getIdCurso());
		
		return dao.obtenerEmpleadoSesion(trabajador);
		
	}
}
