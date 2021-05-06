package pe.com.sedapal.agi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class UMarcaAgua {
	
	public static OutputStream agregarTextoMarcaAguaPDF(InputStream pdfEntrada,String textoMarcaAgua) {
    	// read existing pdf
		OutputStream os = new ByteArrayOutputStream();
    	try {
    		PdfReader reader = new PdfReader(pdfEntrada);
            PdfStamper stamper = new PdfStamper(reader, os);

            // text watermark
            //Font FONT = new Font(Font.FontFamily.HELVETICA, 34, Font.BOLD, new GrayColor(0.5f));
            Font font = new Font(Font.FontFamily.HELVETICA, 60, Font.BOLD, BaseColor.BLUE);
            Phrase p = new Phrase(textoMarcaAgua, font);


            // properties
            PdfContentByte over;
            Rectangle pagesize;
            float x, y;

            // loop over every page
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {

                // get page size and position
                pagesize = reader.getPageSizeWithRotation(i);
                x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                over = stamper.getOverContent(i);
                over.saveState();

                // set transparency
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.2f);
                over.setGState(state);

                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 45);
                
                over.restoreState();
            }
            stamper.close();
            reader.close();
            
            
    	}catch(IOException e) {
    		e.printStackTrace();
    	}catch(DocumentException doc) {
    		doc.printStackTrace();
    	}
    	
        return os;
    }
	
	public static OutputStream agregarImagenMarcaAguaPDF(InputStream pdfEntrada,URL urlImagen) {
    	// read existing pdf
		OutputStream os = new ByteArrayOutputStream();
    	try {
    		PdfReader reader = new PdfReader(pdfEntrada);
            PdfStamper stamper = new PdfStamper(reader, os);

            // image watermark
            Image img = Image.getInstance(urlImagen);
            float w = img.getScaledWidth();
            float h = img.getScaledHeight();
            
            // properties
            PdfContentByte over;
            Rectangle pagesize;
            float x, y;

            // loop over every page
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {

                // get page size and position
                pagesize = reader.getPageSizeWithRotation(i);
                x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                over = stamper.getOverContent(i);
                over.saveState();

                // set transparency
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.2f);
                over.setGState(state);
                
                over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
                
                over.restoreState();
            }
            stamper.close();
            reader.close();
            
            
    	}catch(IOException e) {
    		e.printStackTrace();
    	}catch(DocumentException doc) {
    		doc.printStackTrace();
    	}
    	
        return os;
    }
}
