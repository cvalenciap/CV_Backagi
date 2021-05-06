package pe.com.sedapal.agi.api;

//prueba
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfReader;
import java.io.IOException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;

import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.service.IDocumentoService;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FooterApi {
	
	@Autowired
	Environment env;
	
	@Autowired
	private IDocumentoService service;
      @GetMapping(path = "/footer/{codigo}/{nombre}/{iddocu}/{idrevi}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> create(RevisionRequest revisionRequest,@PathVariable("codigo") Long codigo,@PathVariable("nombre") String nombre,@PathVariable("iddocu") Long iddocu,@PathVariable("idrevi") Long idrevi) throws IOException, DocumentException{		
		System.out.println("Aqui");
		ResponseObject response = new ResponseObject();	
		String rutacopiacontrolada;
        try {			
        	//Traemos la Ruta de la copia controlada
        	Documento objeto = this.service.obtenerDocumentoRevisionDetalle(iddocu,idrevi);
        	response.setResultado(objeto);
        	rutacopiacontrolada = objeto.getRutacopiacontrolada();
        	System.out.print(objeto);
        	System.out.print(rutacopiacontrolada);
        	Document document = new Document();
        	
			//Ruta donde se creara la copia con el footer. 
			java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
			PdfCopy copy = new PdfCopy(document, os);//(new File("D:\\PDF_PRUEBA\\155.pdf")));
			document.open();
			String RutaIpFileServer= env.getProperty("app.config.servidor.fileserver");	//Capturamos ipFileServer
            //archivo donde se requiere el footer.
			PdfReader reader1 = new PdfReader(RutaIpFileServer+rutacopiacontrolada);//("D:\\PDF_PRUEBA\\CGUERRAFINAL.pdf");
			int n1 = reader1.getNumberOfPages();

			PdfImportedPage page;
			PdfCopy.PageStamp stamp;
			//Style del footer
			//Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
			for (int i = 0; i < n1; ) {
			    page = copy.getImportedPage(reader1, ++i);
			    stamp = copy.createPageStamp(page);
			    //Posicion del footer x pag.
			    ColumnText.showTextAligned(stamp.getOverContent(), Element.ALIGN_LEFT,new Phrase(String.format("COPIA"+" "+codigo +"-"+ nombre, i, n1)),345.5f, 28, 0);//385//150
			    stamp.alterContents();
			    copy.addPage(page);
			}
			document.close();
			reader1.close();	
			byte[] bytes = os.toByteArray();			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename="+"reporte"+".pdf");
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            			
	return ResponseEntity.ok().headers(headers).body(bytes);
//			return new ResponseEntity<ResponseObject>(response,HttpStatus.OK);
    }catch(DocumentException de){  
    	throw new ExceptionConverter(de);	
    }catch(FileNotFoundException e){  
    	throw new ExceptionConverter(e);
    }
  }   
}