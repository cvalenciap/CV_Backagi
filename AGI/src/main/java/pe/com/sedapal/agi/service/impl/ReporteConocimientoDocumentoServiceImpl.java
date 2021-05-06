package pe.com.sedapal.agi.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import pe.com.sedapal.agi.dao.IReporteConocimientoDocumentoDAO;
import pe.com.sedapal.agi.model.ConocimientoRevisionDocumento;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IReporteConocimientoDocumentoService;

@Service
public class ReporteConocimientoDocumentoServiceImpl implements IReporteConocimientoDocumentoService{
	
	@Autowired
	SessionInfo session;
	
	@Autowired
	Environment env;	
	
	@Autowired 
	private IReporteConocimientoDocumentoDAO dao;

	@Override
	public byte[] generarReporteConocimientoDocumento(RevisionRequest request) {
			String carpetaJaspers = env.getProperty("app.config.pdf");
			String carpetaImagenes =env.getProperty("app.config.img.pdf");
		try {
			List<Trabajador> listaPersonas = this.dao.consultarPersonasDesconocenDocumento(request);
			File file = Paths.get(carpetaImagenes+"sedapal-logo.png").toFile();
			String absolutePath = file.getAbsolutePath();
			Map<String,Object> parametrosPdf = new HashMap<>();
			parametrosPdf.put("rutaImagenLogo", absolutePath);
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			parametrosPdf.put("usuario", (((UserAuth)principal).getUsername()));		
			Date date = new Date();
			parametrosPdf.put("fechaActual", date);
			parametrosPdf.put("codigo", request.getCodigoDoc());
			parametrosPdf.put("titulo", request.getTitulo());
			InputStream is = new FileInputStream(carpetaJaspers+"reporteDocDesconocimiento.jasper");			
			JRBeanCollectionDataSource jrb = new JRBeanCollectionDataSource(listaPersonas);
			byte[] bytes = JasperRunManager.runReportToPdf(is, parametrosPdf,jrb);			
			is.close();
			return bytes;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {		
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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
	public List<ConocimientoRevisionDocumento> consultarDocumentosRevision(DocumentoRequest documentoRequest, PageRequest paginaRequest) {
		// TODO Auto-generated method stub
		return this.dao.consultarDocumentosRevision(documentoRequest, paginaRequest);
	}

}
