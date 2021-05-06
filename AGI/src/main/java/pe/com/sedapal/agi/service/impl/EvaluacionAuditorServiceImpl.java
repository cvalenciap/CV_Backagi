package pe.com.sedapal.agi.service.impl;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IEvaluacionAuditorDAO;
import pe.com.sedapal.agi.model.EvaluacionAuditor;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.request_objects.EvaluacionAuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IEvaluacionAuditorService;
import pe.com.sedapal.agi.util.UConstante;
import pe.com.sedapal.agi.security.config.UserAuth;
@Service
public class EvaluacionAuditorServiceImpl implements IEvaluacionAuditorService {

	private static final Logger LOGGER = Logger.getLogger(EvaluacionAuditorServiceImpl.class);	
	
	@Autowired
	SessionInfo session;
	
	@Autowired
	private IEvaluacionAuditorDAO dao;
	
	@Override
	public List<EvaluacionAuditor> obtenerListaEvaluacionAuditorGrilla(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return dao.obtenerListaEvaAuditorGrilla(evaluacionAuditorRequest,  pageRequest);
	}   
   
	
	@Override
	public EvaluacionAuditor obtenerEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest) {
		// TODO Auto-generated method stub
		return dao.obtenerEvaluacionAuditor(evaluacionAuditorRequest);
	}     
      
	    
	@Override
	public List<Pregunta> obtenerListaAspectos(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest){
		// TODO Auto-generated method stub
		return dao.obtenerListaAspectos(evaluacionAuditorRequest,  pageRequest);
		
	}
	//Este es para actualizar las respuestas
	@Override
	public List<Pregunta> obtenerListaAspectosSegunIdEva(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest){
		// TODO Auto-generated method stub
		return dao.obtenerListaAspectosSegunIdEva(evaluacionAuditorRequest,  pageRequest);
		
	}
	
	
	@Override
	public Boolean actualizarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta> listaPreguntas) {
		// TODO Auto-generated method stub
		return this.dao.actualizarEvaluacionAuditor(evaluacionAuditor,listaPreguntas);
	}

	
	
	@Override
	public List<Pregunta> buscarResPorCodEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest){
		// TODO Auto-generated method stub
		return dao.buscarResPorCodEvaluacionAuditor(evaluacionAuditorRequest);
		
	}
  
	@Override
	public Boolean eliminarEvaluacionAuditor(Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.eliminarEvaluacionAuditor(codigo, (((UserAuth)principal).getUsername()));
	}
	  
	@Override
	public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor) {
		
		return this.dao.registrarEvaluacionAuditor(evaluacionAuditor);
		
	}
	 
	 
	@Override
	public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta> listaPreguntas) {
		// TODO Auto-generated method stub
		return this.dao.registrarEvaluacionAuditor(evaluacionAuditor,listaPreguntas);
	}

	//para el imprimir
	@Override
	public void imprimirPDF(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest) {
 
        Document documento = new Document();
        File archivoPDF = new File("D:\\PDF_PRUEBA\\ARCHIVO_IMPRIMIR.pdf");

        if (archivoPDF.exists()) {
            archivoPDF.delete();
        }        
        try {        	
        	archivoPDF.createNewFile();
        				
			 FileOutputStream ficheroPdf = null;
			 ficheroPdf = new FileOutputStream(archivoPDF);
				
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
            documento.open();
            List<Pregunta> listaPreguntas = this.dao.obtenerListaAspectosSegunIdEva(evaluacionAuditorRequest,  pageRequest);
 
            System.out.println("listaPreguntas.size()-->"+listaPreguntas.size());
            
                   Font fuente =new Font();
                   fuente.setStyle(Font.BOLD);
                   fuente.setStyle(64);
            
        
		            Paragraph titulo1 = new Paragraph();
		            titulo1.setAlignment(Paragraph.ALIGN_CENTER);
		            titulo1.setFont(FontFactory.getFont("Times New Roman", 16, Font.BOLD));
		            titulo1.add("Evaluación Auditor");
		            documento.add(titulo1);
                    
		            documento.add( Chunk.NEWLINE );

		            // Este codigo genera una tabla de 6 columnas
		            PdfPTable table = new PdfPTable(6);     
		            
		              
		            float[] medidaCeldas = { 2.25f,0.55f ,0.55f, 0.55f,0.55f,0.55f};

		            // ASIGNAS LAS MEDIDAS A LA TABLA (ANCHO)
		             table.setWidths(medidaCeldas);
		             
		             
		             Paragraph tituloAspectos = new Paragraph();
		             tituloAspectos.setAlignment(Paragraph.ALIGN_CENTER);
		             tituloAspectos.setFont(FontFactory.getFont("Times New Roman", 12, Font.BOLD));
		             tituloAspectos.add("ASPECTOS A EVALUAR"); 
		             
		             
		            
		            table.addCell(tituloAspectos);
		            
		            Paragraph tituloN1 = new Paragraph();
		            tituloN1.setAlignment(Paragraph.ALIGN_CENTER);
		            tituloN1.setFont(FontFactory.getFont("Times New Roman", 12, Font.BOLD));
		            tituloN1.add("1"); 	
                    table.addCell(tituloN1);
                    
                    
                    Paragraph tituloN2 = new Paragraph();
		            tituloN2.setAlignment(Paragraph.ALIGN_CENTER);
		            tituloN2.setFont(FontFactory.getFont("Times New Roman", 12, Font.BOLD));
		            tituloN2.add("2"); 	
                    table.addCell(tituloN2);
                    
                    
                    Paragraph tituloN3 = new Paragraph();
		            tituloN3.setAlignment(Paragraph.ALIGN_CENTER);
		            tituloN3.setFont(FontFactory.getFont("Times New Roman", 12, Font.BOLD));
		            tituloN3.add("3"); 	
                    table.addCell(tituloN3);
                    
                    
                    Paragraph tituloN4 = new Paragraph();
		            tituloN4.setAlignment(Paragraph.ALIGN_CENTER);
		            tituloN4.setFont(FontFactory.getFont("Times New Roman", 12, Font.BOLD));
		            tituloN4.add("4"); 	
                    table.addCell(tituloN4);
                  
                    
                    Paragraph tituloN5 = new Paragraph();
		            tituloN5.setAlignment(Paragraph.ALIGN_CENTER);
		            tituloN5.setFont(FontFactory.getFont("Times New Roman", 12, Font.BOLD));
		            tituloN5.add("5"); 	
                    table.addCell(tituloN5);
                
		            //ASPECTOS A EVALUAR
		            
            if (listaPreguntas.size() > 0) {

                for (int i = 0; i < listaPreguntas.size(); i++) {

               
                    table.addCell(listaPreguntas.get(i).getPregunta());
                    
                    int puntaje=Integer.parseInt(listaPreguntas.get(i).getRadioNum());//1

                    switch (puntaje) {
                    case 1:
                    //instrucciones;
                    	 table.addCell("X");
                         table.addCell("");
                         table.addCell("");
                         table.addCell("");
                         table.addCell("");

                    break;
                    case 2:
                    //instrucciones;
                    	 table.addCell("");
                         table.addCell("X");
                         table.addCell("");
                         table.addCell("");
                         table.addCell("");

                    break;                    
                    case 3:
                        //instrucciones;
                    	 table.addCell("");
                         table.addCell("");
                         table.addCell("X");
                         table.addCell("");
                         table.addCell("");

                        break;                        
                    case 4:
                        //instrucciones;
                    	 table.addCell("");
                         table.addCell("");
                         table.addCell("");
                         table.addCell("X");
                         table.addCell("");

                        break;
                    default:
                    //sentencias;
                    	 table.addCell("");
                         table.addCell("");
                         table.addCell("");
                         table.addCell("");
                         table.addCell("X");
  
                    break;
             }
 
       }
                
                
                
                Paragraph tituloPunt = new Paragraph();
                tituloPunt.setAlignment(Paragraph.ALIGN_CENTER);
                tituloPunt.setFont(FontFactory.getFont("Times New Roman", 12, Font.BOLD));
                tituloPunt.add("PUNTAJE TOTAL OBTENIDO");
                
                    
                PdfPCell celdaPuntaje = new PdfPCell(new Paragraph(tituloPunt));
                celdaPuntaje.setColspan(5);
	            table.addCell(celdaPuntaje);	              
	            
	            PdfPCell celdaPuntaje1 = new PdfPCell(new Paragraph(""));
                celdaPuntaje1.setColspan(1);
	            table.addCell(celdaPuntaje1);
	            
	            
	            PdfPCell celdaApellidoNombres = new PdfPCell(new Paragraph("Apellidos y Nombres del Evaluador:"));
	            celdaApellidoNombres.setColspan(6);
	            table.addCell(celdaApellidoNombres);
	            
	          
	            PdfPCell celdaAuditoria= new PdfPCell(new Paragraph("Nombre y Fecha de la Auditoría:"));
	            celdaAuditoria.setColspan(6);
	            table.addCell(celdaAuditoria);
	            
	          
	            PdfPCell celdaActividad = new PdfPCell(new Paragraph("Actividad/ Área Auditada: "));
	            celdaActividad.setColspan(6);
	            table.addCell(celdaActividad);
	            
	        
	            PdfPCell celdaTotalHoras = new PdfPCell(new Paragraph("Total de horas de la Auditoria1:"));
	            celdaTotalHoras.setColspan(6);
	            table.addCell(celdaTotalHoras);
	            
	       
	              
	            
	            documento.add(table);		             
	            documento.close();  
	            
	            //Desktop.getDesktop().open(archivoPDF);
            }
            
            else{           
               documento.add(new Paragraph("No hay nada "));            
            }
            
            documento.close();
          
        } catch (DocumentException ex) {
           
        	String[] error = MensajeExceptionUtil.obtenerMensajeError(ex);
        	LOGGER.error(error[1], ex);
			
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	
	
	

	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}
	
	@Override
	public Error getError() {
		return dao.getError();
	}
}
