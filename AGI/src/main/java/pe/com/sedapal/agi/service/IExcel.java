package pe.com.sedapal.agi.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Dashboard;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.FichaTecnica;

public interface IExcel {
	String escribirExcel(List<Documento>documento)throws FileNotFoundException, IOException;
	String escribirExcelCancelacion(List<Cancelacion>cancelacion)throws FileNotFoundException, IOException;
	String escribirExcelFicha(FichaTecnica fichaTecnica)throws FileNotFoundException, IOException;
	String formatoExcelDashboard(List<Dashboard> documento, Long anio, String trimnestre) throws FileNotFoundException, IOException;
	byte[] generarPdfDocumento(List<Documento> documentos);
	byte[] generarPdfEvaluacion(List<Trabajador> trabajador);
}
