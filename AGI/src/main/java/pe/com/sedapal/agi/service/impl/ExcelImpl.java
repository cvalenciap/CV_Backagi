package pe.com.sedapal.agi.service.impl;

import java.io.File;
import pe.com.sedapal.agi.security.config.UserAuth;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Dashboard;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.FichaTecnica;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IExcel;
import pe.com.sedapal.agi.util.UConstante;
//import pe.com.sedapal.agi.recursos.rutas;

@Service
public class ExcelImpl implements IExcel {
	
	@Autowired
	SessionInfo session;	
	
	@Autowired
	Environment env;
	
	private String endpointServidor; 
	
	/* generar excel - Documento Jerarquia */
	public String escribirExcel(List<Documento> documentos) throws IOException {
		
		String rutaPlazoXlsx = "";
		//System.out.println("Total Docs: " + documentos.size());
		try {
			if(documentos != null && !documentos.isEmpty()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet();
				workbook.setSheetName(0, "Documentos");
				String[] headers = new String[] { "Código", "Titulo", "Revisión", "Fecha de Aprobación", "Estado" };
				

				XSSFFont headerFont = workbook.createFont();
				XSSFFont tituloFont = workbook.createFont();
				tituloFont.setBold(true);
				tituloFont.setFontHeightInPoints((short) 16);

				CellStyle tituloCellStyle = workbook.createCellStyle();
				tituloCellStyle.setFont(tituloFont);

				XSSFRow tituloRow = sheet.createRow(0);
				XSSFCell tituloCell = tituloRow.createCell(0);
				tituloCell.setCellStyle(tituloCellStyle);
				tituloCell.setCellValue("Consulta de Documentos");
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
				headerFont.setBold(true);
				headerFont.setFontHeightInPoints((short) 12);
				CellStyle headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				XSSFRow headerRow = sheet.createRow(2);

				for (int i = 0; i < headers.length; ++i) {
					String header = headers[i];
					XSSFCell cell = headerRow.createCell(i);
					cell.setCellStyle(headerCellStyle);
					cell.setCellValue(header);
				}
				for (int i = 0; i < documentos.size(); ++i) {
					XSSFRow dataRow = sheet.createRow(i + 3);
					dataRow.createCell(0).setCellValue(documentos.get(i).getCodigo());	
					dataRow.createCell(1).setCellValue(documentos.get(i).getDescripcion());
					dataRow.createCell(2).setCellValue(documentos.get(i).getRevision().getNumero());
					if(documentos.get(i).getRevision().getFecha()!=null) {
						CellStyle estilo = workbook.createCellStyle();
						CreationHelper createHelper = workbook.getCreationHelper();
						estilo.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
						
						Cell celda = dataRow.createCell(3);
						celda.setCellValue(documentos.get(i).getRevision().getFecha());
						celda.setCellStyle(estilo);
						
					}
					dataRow.createCell(4).setCellValue(documentos.get(i).getEstado().getV_descons());
				}
				XSSFRow dataRow = sheet.createRow(5 + documentos.size());
				dataRow.createCell(0).setCellValue("Total de Documentos");
				dataRow.createCell(1).setCellValue(documentos.size());
				
				/*cguerra*/
				endpointServidor = env.getProperty("app.config.paths.temp");
				System.out.println(endpointServidor);
				rutaPlazoXlsx = endpointServidor + System.currentTimeMillis() +".xlsx";
				FileOutputStream file = new FileOutputStream(rutaPlazoXlsx);
				/*rutaPlazoXlsx = "C:/Temp/" + System.currentTimeMillis() + ".xlsx";				
                FileOutputStream file = new FileOutputStream(rutaPlazoXlsx);*/
                
                /*cguerra*/
				workbook.write(file);
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rutaPlazoXlsx;
	}
	
	public String formatoExcelDashboard(List<Dashboard> documentos, Long anio, String trimestre) throws IOException {
		String rutaExcel = "";
		//System.out.println("Total Docs: " + documentos.size());
		try {
			if(documentos != null && !documentos.isEmpty()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet();
				workbook.setSheetName(0, "Detalle");
				String[] headers = new String[] { "Código", "Titulo", "Revisión", "Fecha de Aprobación", "Estado",
						"Programacion", "Trimestre", "Equipo", "Fecha Programación"};

				XSSFFont headerFont = workbook.createFont();
				XSSFFont tituloFont = workbook.createFont();
				tituloFont.setBold(true);
				tituloFont.setFontHeightInPoints((short) 16);

				CellStyle tituloCellStyle = workbook.createCellStyle();
				tituloCellStyle.setFont(tituloFont);

				XSSFRow tituloRow = sheet.createRow(0);
				XSSFCell tituloCell = tituloRow.createCell(0);
				tituloCell.setCellStyle(tituloCellStyle);
				tituloCell.setCellValue("Documentos Programados en el Trimestre: " + anio + "-" + trimestre);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
				headerFont.setBold(true);
				headerFont.setFontHeightInPoints((short) 12);
				CellStyle headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				XSSFRow headerRow = sheet.createRow(2);

				for (int i = 0; i < headers.length; ++i) {
					String header = headers[i];
					XSSFCell cell = headerRow.createCell(i);
					cell.setCellStyle(headerCellStyle);
					cell.setCellValue(header);
				}
				for (int i = 0; i < documentos.size(); ++i) {
					XSSFRow dataRow = sheet.createRow(i + 3);
					
					CellStyle estiloFecha = workbook.createCellStyle();
					CreationHelper createHelper = workbook.getCreationHelper();
					estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					
					dataRow.createCell(0).setCellValue(documentos.get(i).getDocumento().getCodigo());	
					dataRow.createCell(1).setCellValue(documentos.get(i).getDocumento().getDescripcion());
					dataRow.createCell(2).setCellValue(documentos.get(i).getDocumento().getRevision().getId());
					if(documentos.get(i).getDocumento().getRevision().getFecha()!=null) {
						Cell celda = dataRow.createCell(3);
						celda.setCellValue(documentos.get(i).getDocumento().getRevision().getFecha());
						celda.setCellStyle(estiloFecha);
					}
					dataRow.createCell(4).setCellValue(documentos.get(i).getDocumento().getEstado().getV_descons());
					
					dataRow.createCell(5).setCellValue(documentos.get(i).getIdProgramacion());
					dataRow.createCell(6).setCellValue(documentos.get(i).getTrimestre().getV_descons());
					dataRow.createCell(7).setCellValue(documentos.get(i).getEquipo().getDescripcion());
					
					if(documentos.get(i).getFechaProgramacion()!=null) {
						Cell celda = dataRow.createCell(8);
						celda.setCellValue(documentos.get(i).getFechaProgramacion());
						celda.setCellStyle(estiloFecha);
					}
				}
				XSSFRow dataRow = sheet.createRow(5 + documentos.size());
				dataRow.createCell(0).setCellValue("Total de Documentos");
				dataRow.createCell(1).setCellValue(documentos.size());
				
				
				rutaExcel = endpointServidor + System.currentTimeMillis() +".xlsx";
				FileOutputStream file = new FileOutputStream(rutaExcel);
				
				/*rutaExcel = "C:/Temp/" + System.currentTimeMillis() + ".xlsx";	
                FileOutputStream file = new FileOutputStream(rutaExcel);*/
				
				
				
				
				workbook.write(file);
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rutaExcel;
	}
	
	@Override
	public byte[] generarPdfDocumento(List<Documento> documentos) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String carpetaJaspers = env.getProperty("app.config.pdf");
		String carpetaImagenes =env.getProperty("app.config.img.pdf");
		try {
			//File file = Paths.get(carpetaResources+carpetaImagenes+"sedapal-logo.png").toFile();
			File file = Paths.get(carpetaImagenes+"sedapal-logo.png").toFile();
			String absolutePath = file.getAbsolutePath();
			Map<String,Object> parametrosPdf = new HashMap<>();
			parametrosPdf.put("rutaImagenLogo", absolutePath);
			parametrosPdf.put("usuario", (((UserAuth)principal).getUsername()));
			Date date = new Date();
			parametrosPdf.put("fechaActual", date);
			//InputStream is = new FileInputStream(carpetaResources+carpetaJaspers+"consultaDocumento.jasper");
			InputStream is = new FileInputStream(carpetaJaspers + "consultaDocumento.jasper");
			JRBeanCollectionDataSource jrb = new JRBeanCollectionDataSource(documentos);
			byte[] bytes = JasperRunManager.runReportToPdf(is, parametrosPdf,jrb);
			is.close();
			return bytes;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (JRException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	
	@Override
	public byte[] generarPdfEvaluacion(List<Trabajador> trabajador) {	
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String carpetaJaspers = env.getProperty("app.config.pdf");
		String carpetaImagenes =env.getProperty("app.config.img.pdf");
		
		try {
			File file = Paths.get(carpetaImagenes+"sedapal-logo.png").toFile();
			String absolutePath = file.getAbsolutePath();
			Map<String,Object> parametrosPdf = new HashMap<>();
			parametrosPdf.put("rutaImagenLogo", absolutePath);
			parametrosPdf.put("usuario", (((UserAuth)principal).getUsername()));
			Date date = new Date();
			parametrosPdf.put("fechaActual", date);
			parametrosPdf.put("nomCurso", trabajador.get(0).getNomCurso());
			InputStream is = new FileInputStream(carpetaJaspers+"consultaEvaluacion.jasper");
			JRBeanCollectionDataSource jrb = new JRBeanCollectionDataSource(trabajador);
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


	/* generar excel - Reporte Cancelacion */
	public String escribirExcelCancelacion(List<Cancelacion> cancelacion) throws IOException {
		String rutaPlazoXlsx = "";
		System.out.println("Total Docs: " + cancelacion.size());
		try {			
			if (cancelacion != null && !cancelacion.isEmpty()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet();
				CellStyle estiloFecha = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				workbook.setSheetName(0, "Bandeja Cancelación");					
				String[] headers = new String[] { "Código", 
												  "Titulo", 
												  "Revisión",
												  "Tipo de Cancelación",
												  "Motivo Cancelación", 
												  "Descripción Cancelación",
												  "Solicitado Por ", 
												  "Fecha de solicitud", 
												  "Aprobado Por", 
												  "Fecha de Aprobación",
												  "Cancelado Por",
												  "Fecha de Cancelación" };

				XSSFFont headerFont = workbook.createFont();
				XSSFFont tituloFont = workbook.createFont();
				tituloFont.setBold(true);
				tituloFont.setFontHeightInPoints((short) 16);

				CellStyle tituloCellStyle = workbook.createCellStyle();
				tituloCellStyle.setFont(tituloFont);
				Calendar fecha = Calendar.getInstance();	
				XSSFRow tituloRow = sheet.createRow(0);
				XSSFCell tituloCell = tituloRow.createCell(0);
				tituloCell.setCellStyle(tituloCellStyle);
				tituloCell.setCellValue("Reporte de Documentos Cancelados  " +fecha.get(Calendar.DAY_OF_MONTH) +"/"+(fecha.get(Calendar.MONTH)+1)+"/"+fecha.get(Calendar.YEAR));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
				headerFont.setBold(true);
				headerFont.setFontHeightInPoints((short) 12);
				CellStyle headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				XSSFRow headerRow = sheet.createRow(2);

				for (int i = 0; i < headers.length; ++i) {
					String header = headers[i];
					XSSFCell cell = headerRow.createCell(i);
					cell.setCellStyle(headerCellStyle);
					cell.setCellValue(header);
				}
				for (int i = 0; i < cancelacion.size(); ++i) {
					XSSFRow dataRow = sheet.createRow(i + 3);
					if(cancelacion.get(i).getCodigoDocumento()!=null) {
						dataRow.createCell(0).setCellValue(cancelacion.get(i).getCodigoDocumento());}	
					if(cancelacion.get(i).getTituloDocumento()!=null) {
					dataRow.createCell(1).setCellValue(cancelacion.get(i).getTituloDocumento());}
					if(cancelacion.get(i).getNumeroRevision()!=null) {
					dataRow.createCell(2).setCellValue(cancelacion.get(i).getNumeroRevision());}
					if(cancelacion.get(i).getTipoCancelacion()!=null) {
					dataRow.createCell(3).setCellValue(cancelacion.get(i).getTipoCancelacion());}
					if(cancelacion.get(i).getMotivoCancelacion()!=null) {
					dataRow.createCell(4).setCellValue(cancelacion.get(i).getMotivoCancelacion());}
					if(cancelacion.get(i).getSustentoSolicitud()!=null) {
					dataRow.createCell(5).setCellValue(cancelacion.get(i).getSustentoSolicitud());}
					if(cancelacion.get(i).getNombreColaborador()!=null || cancelacion.get(i).getApeMatColaborador()!=null || cancelacion.get(i).getApePatColaborador()!=null) {
					dataRow.createCell(6).setCellValue(cancelacion.get(i).getNombreColaborador()+" "+
													   cancelacion.get(i).getApeMatColaborador()+" "+
													   cancelacion.get(i).getApePatColaborador());}
					if(cancelacion.get(i).getFechaSolicitud()!=null) {
						Cell celda = dataRow.createCell(7);
                        celda.setCellValue(cancelacion.get(i).getFechaSolicitud());
                       celda.setCellStyle(estiloFecha);
					}
					if(cancelacion.get(i).getAprobador()!=null) {
					dataRow.createCell(8).setCellValue(cancelacion.get(i).getAprobador());}
					if(cancelacion.get(i).getFechaAprobacion()!=null) {
						Cell celda = dataRow.createCell(9);
                        celda.setCellValue(cancelacion.get(i).getFechaAprobacion());
                       celda.setCellStyle(estiloFecha);					
					}
					if(cancelacion.get(i).getCancelador()!=null) {
					dataRow.createCell(10).setCellValue(cancelacion.get(i).getCancelador());}
					if(cancelacion.get(i).getFechaCancelacion()!=null) {
						Cell celda = dataRow.createCell(11);
                        celda.setCellValue(cancelacion.get(i).getFechaCancelacion());
                       celda.setCellStyle(estiloFecha);					
					 }
				}

				XSSFRow dataRow = sheet.createRow(5 + cancelacion.size());
				dataRow.createCell(0).setCellValue("Total de Documentos");
				dataRow.createCell(2).setCellValue(cancelacion.size());		
				
				//rutaPlazoXlsx = "Reporte_de_Documentos_Cancelados(" + fecha.get(Calendar.YEAR)+"-"+fecha.get(Calendar.MONTH)+"-"+fecha.get(Calendar.DAY_OF_MONTH)+").xlsx";
				rutaPlazoXlsx = endpointServidor + System.currentTimeMillis() + ".xlsx";
				FileOutputStream file = new FileOutputStream(rutaPlazoXlsx);
				/*
				rutaPlazoXlsx = endpointServidor + System.currentTimeMillis() + ".xlsx";
                FileOutputStream file = new FileOutputStream(rutaPlazoXlsx);*/
				workbook.write(file);
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rutaPlazoXlsx;
	}

	
	/* generar excel - Reporte Ficha Tecnica */
	public String escribirExcelFicha(FichaTecnica fichaTecnica) throws IOException {
		String rutaPlazoXlsx = "";
		
		try {			
			if (fichaTecnica != null) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet();
				CellStyle estiloFecha = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				
				workbook.setSheetName(0, "Ficha Tecnica");			

				
				Calendar fecha = Calendar.getInstance();
				//titulo
						//estilo
						XSSFFont tituloFont = workbook.createFont();
						tituloFont.setBold(true);
						tituloFont.setFontHeightInPoints((short) 16);
						CellStyle tituloCellStyle = workbook.createCellStyle();
						tituloCellStyle.setFont(tituloFont);			
						tituloCellStyle.setAlignment(tituloCellStyle.ALIGN_CENTER);
						//estilo fin
				XSSFRow tituloRow = sheet.createRow(0);				
				XSSFCell tituloCell = tituloRow.createCell(0);
				tituloCell.setCellStyle(tituloCellStyle);					
				tituloCell.setCellValue(" FICHA TECNICA DE PROCESO ");
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));
				//fin titulo
				//cabecera identificacion del proceso
						//estilo
						XSSFFont headerIdentificacionFont = workbook.createFont();
						headerIdentificacionFont.setBold(true);
						headerIdentificacionFont.setFontHeightInPoints((short) 14);
						CellStyle headerIdentificacionCellStyle = workbook.createCellStyle();
						headerIdentificacionCellStyle.setFont(headerIdentificacionFont);
						headerIdentificacionCellStyle.setAlignment(headerIdentificacionCellStyle.ALIGN_CENTER);				
						headerIdentificacionCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());												
						headerIdentificacionCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
						//estilo fin						
				XSSFRow headerIdentificacionRow = sheet.createRow(1);
				XSSFCell headerIdentificacionCell = headerIdentificacionRow.createCell(0);
				headerIdentificacionCell.setCellStyle(headerIdentificacionCellStyle);
				headerIdentificacionCell.setCellValue(" Identificación del proceso ");
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 11));
				//cabecera fin
				//cuerpo identificacion del proceso
				//estilo
				XSSFFont headerIdentificacionSubTitulosFont = workbook.createFont();
				headerIdentificacionSubTitulosFont.setBold(true);
				headerIdentificacionSubTitulosFont.setFontHeightInPoints((short) 12);
				CellStyle headerIdentificacionSubTitulosCellStyle = workbook.createCellStyle();
				headerIdentificacionSubTitulosCellStyle.setWrapText(true);
				headerIdentificacionSubTitulosCellStyle.setFont(headerIdentificacionSubTitulosFont);
				headerIdentificacionSubTitulosCellStyle.setVerticalAlignment(headerIdentificacionSubTitulosCellStyle.VERTICAL_CENTER);	
				headerIdentificacionSubTitulosCellStyle.setAlignment(headerIdentificacionSubTitulosCellStyle.ALIGN_CENTER);
				
				XSSFFont headerIdentificacionDatosFont = workbook.createFont();				
				headerIdentificacionDatosFont.setFontHeightInPoints((short) 12);
				CellStyle headerIdentificacionDatosCellStyle = workbook.createCellStyle();
				headerIdentificacionDatosCellStyle.setWrapText(true);
				headerIdentificacionDatosCellStyle.setFont(headerIdentificacionDatosFont);
				headerIdentificacionDatosCellStyle.setVerticalAlignment(headerIdentificacionDatosCellStyle.VERTICAL_CENTER);
				headerIdentificacionDatosCellStyle.setAlignment(headerIdentificacionDatosCellStyle.ALIGN_CENTER);
				//estilo fin						
				XSSFRow headerIdentificacionFirstRow = sheet.createRow(2);
				XSSFCell TipoProcesoCell = headerIdentificacionFirstRow.createCell(0);
				TipoProcesoCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				TipoProcesoCell.setCellValue(" Tipo de Proceso:  ");
				sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
				XSSFCell TipoProcesoDatoCell = headerIdentificacionFirstRow.createCell(3);
				TipoProcesoDatoCell.setCellStyle(headerIdentificacionDatosCellStyle);
				TipoProcesoDatoCell.setCellValue(fichaTecnica.getTipoProceso());				
				sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 5));				
				XSSFCell NivelCell = headerIdentificacionFirstRow.createCell(6);
				NivelCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				NivelCell.setCellValue(" Nivel:  ");
				sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 8));
				XSSFCell NivelDatoCell = headerIdentificacionFirstRow.createCell(9);
				NivelDatoCell.setCellStyle(headerIdentificacionDatosCellStyle);
				NivelDatoCell.setCellValue(fichaTecnica.getNivel());	
				sheet.addMergedRegion(new CellRangeAddress(2, 2, 9, 11));
				
				XSSFRow headerIdentificacionSecondRow = sheet.createRow(3);				
				XSSFCell NombreProcesoCell = headerIdentificacionSecondRow.createCell(0);
				NombreProcesoCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				NombreProcesoCell.setCellValue(" Nombre del Proceso:  ");
				sheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 2));
				XSSFCell NombreProcesoDatoCell = headerIdentificacionSecondRow.createCell(3);
				NombreProcesoDatoCell.setCellStyle(headerIdentificacionDatosCellStyle);
				NombreProcesoDatoCell.setCellValue(fichaTecnica.getProceso());				
				sheet.addMergedRegion(new CellRangeAddress(3, 4, 3, 5));				
				XSSFCell ResponsableProcesoCell = headerIdentificacionSecondRow.createCell(6);
				ResponsableProcesoCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ResponsableProcesoCell.setCellValue(" Responsable del Proceso:  ");
				sheet.addMergedRegion(new CellRangeAddress(3, 4, 6, 8));
				XSSFCell ResponsableProcesoDatoCell = headerIdentificacionSecondRow.createCell(9);
				ResponsableProcesoDatoCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ResponsableProcesoDatoCell.setCellValue(fichaTecnica.getResponsable());	
				sheet.addMergedRegion(new CellRangeAddress(3, 4, 9, 11));
				
				XSSFRow headerIdentificacionThirdRow = sheet.createRow(5);
				XSSFCell ObjetivoCell = headerIdentificacionThirdRow.createCell(0);
				ObjetivoCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ObjetivoCell.setCellValue(" Objetivo:  ");
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));
				XSSFCell ObjetivoDatoCell = headerIdentificacionThirdRow.createCell(3);
				ObjetivoDatoCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ObjetivoDatoCell.setCellValue(fichaTecnica.getObjetivo());				
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 5));				
				XSSFCell AlcanseCell = headerIdentificacionThirdRow.createCell(6);
				AlcanseCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				AlcanseCell.setCellValue(" Alcance:  ");
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 8));
				XSSFCell AlcanseDatoCell = headerIdentificacionThirdRow.createCell(9);
				AlcanseDatoCell.setCellStyle(headerIdentificacionDatosCellStyle);
				AlcanseDatoCell.setCellValue(fichaTecnica.getAlcance());	
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 9, 11));
				
				XSSFRow headerIdentificacionFourRow = sheet.createRow(6);
				XSSFCell ReqLegRegCell = headerIdentificacionFourRow.createCell(0);
				ReqLegRegCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ReqLegRegCell.setCellValue(" Requisitos Legales y Reglamentos:  ");
				sheet.addMergedRegion(new CellRangeAddress(6, 7, 0, 2));
				XSSFCell ReqLegRegDatoCell = headerIdentificacionFourRow.createCell(3);
				ReqLegRegDatoCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ReqLegRegDatoCell.setCellValue(fichaTecnica.getRequisitos());				
				sheet.addMergedRegion(new CellRangeAddress(6, 7, 3, 11));
				
				//cabecera Componentes del proceso del proceso
				//estilo
				XSSFFont headerComponentesProcesoFont = workbook.createFont();
				headerComponentesProcesoFont.setBold(true);
				headerComponentesProcesoFont.setFontHeightInPoints((short) 14);
				CellStyle headerComponentesProcesoCellStyle = workbook.createCellStyle();
				headerComponentesProcesoCellStyle.setFont(headerComponentesProcesoFont);
				headerComponentesProcesoCellStyle.setAlignment(headerComponentesProcesoCellStyle.ALIGN_CENTER);				
				headerComponentesProcesoCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());												
				headerComponentesProcesoCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				//estilo fin						
				XSSFRow headerComponentesProcesoRow = sheet.createRow(8);
				XSSFCell headerComponentesProcesoCell = headerComponentesProcesoRow.createCell(0);
				headerComponentesProcesoCell.setCellStyle(headerComponentesProcesoCellStyle);
				headerComponentesProcesoCell.setCellValue(" Componentes del proceso ");
				sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 11));
				
				XSSFRow headerComponentesProcesoCabeceraRow = sheet.createRow(9);
				XSSFCell ProveedoresCell = headerComponentesProcesoCabeceraRow.createCell(0);
				ProveedoresCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ProveedoresCell.setCellValue(" Proveedores ");
				sheet.addMergedRegion(new CellRangeAddress(9, 10, 0, 2));
				XSSFCell EntradasCell = headerComponentesProcesoCabeceraRow.createCell(3);
				EntradasCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				EntradasCell.setCellValue(" Entradas ");
				sheet.addMergedRegion(new CellRangeAddress(9, 10, 3, 4));
				XSSFCell SubProcesosCell = headerComponentesProcesoCabeceraRow.createCell(5);
				SubProcesosCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				SubProcesosCell.setCellValue(" Subprocesos / Actividades ");
				sheet.addMergedRegion(new CellRangeAddress(9, 10, 5,6));
				XSSFCell SalidasCell = headerComponentesProcesoCabeceraRow.createCell(7);
				SalidasCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				SalidasCell.setCellValue(" Salidas ");
				sheet.addMergedRegion(new CellRangeAddress(9, 10, 7,8));
				XSSFCell ClienteCell = headerComponentesProcesoCabeceraRow.createCell(9);
				ClienteCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ClienteCell.setCellValue(" Cliente / Usuario ");
				sheet.addMergedRegion(new CellRangeAddress(9, 10, 9,11));				
				
				XSSFRow headerComponentesProcesoCuerpoRow = sheet.createRow(11);			
				XSSFCell ProveedoresDatosCell = headerComponentesProcesoCuerpoRow.createCell(0);
				ProveedoresDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ProveedoresDatosCell.setCellValue(fichaTecnica.getProveedores());				
				sheet.addMergedRegion(new CellRangeAddress(11,15, 0, 2));
				XSSFCell EntradasDatosCell = headerComponentesProcesoCuerpoRow.createCell(3);
				EntradasDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				EntradasDatosCell.setCellValue(fichaTecnica.getEntradas());				
				sheet.addMergedRegion(new CellRangeAddress(11,15, 3, 4));
				XSSFCell SubProcesosDatosCell = headerComponentesProcesoCuerpoRow.createCell(5);
				SubProcesosDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				SubProcesosDatosCell.setCellValue(fichaTecnica.getSubProceso());				
				sheet.addMergedRegion(new CellRangeAddress(11,15, 5, 6));
				XSSFCell SalidasDatosCell = headerComponentesProcesoCuerpoRow.createCell(7);
				SalidasDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				SalidasDatosCell.setCellValue(fichaTecnica.getSalidas());				
				sheet.addMergedRegion(new CellRangeAddress(11,15, 7, 8));
				XSSFCell ClienteDatosCell = headerComponentesProcesoCuerpoRow.createCell(9);
				ClienteDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ClienteDatosCell.setCellValue(fichaTecnica.getClientes());				
				sheet.addMergedRegion(new CellRangeAddress(11,15, 9, 11));
				
				//cabecera Componentes del proceso del proceso
				//estilo
				XSSFFont headerIdentificacionRecursosFont = workbook.createFont();
				headerIdentificacionRecursosFont.setBold(true);
				headerIdentificacionRecursosFont.setFontHeightInPoints((short) 14);
				CellStyle headerIdentificacionRecursosCellStyle = workbook.createCellStyle();
				headerIdentificacionRecursosCellStyle.setFont(headerComponentesProcesoFont);
				headerIdentificacionRecursosCellStyle.setAlignment(headerComponentesProcesoCellStyle.ALIGN_CENTER);				
				headerIdentificacionRecursosCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());												
				headerIdentificacionRecursosCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				//estilo fin						
				XSSFRow headerIdentificacionRecursosRow = sheet.createRow(16);
				XSSFCell headerIdentificacionRecursosCell = headerIdentificacionRecursosRow.createCell(0);
				headerIdentificacionRecursosCell.setCellStyle(headerComponentesProcesoCellStyle);
				headerIdentificacionRecursosCell.setCellValue(" Identificación de Recursos Críticos para la Ejecución y Control del Proceso ");
				sheet.addMergedRegion(new CellRangeAddress(16, 16, 0, 11));
				
				
				XSSFRow headerIdentificacionRecursosCabeceraFirstRow = sheet.createRow(17);
				XSSFCell PersonalCell = headerIdentificacionRecursosCabeceraFirstRow.createCell(0);
				PersonalCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				PersonalCell.setCellValue(" Personal ");
				sheet.addMergedRegion(new CellRangeAddress(17, 17, 0, 3));
				XSSFCell EquiHerraCell = headerIdentificacionRecursosCabeceraFirstRow.createCell(4);
				EquiHerraCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				EquiHerraCell.setCellValue(" Equipos y Herramientas ");
				sheet.addMergedRegion(new CellRangeAddress(17, 17, 4, 7));
				XSSFCell MateSumiCell = headerIdentificacionRecursosCabeceraFirstRow.createCell(8);
				MateSumiCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				MateSumiCell.setCellValue(" Materiales y Suministro de Oficina ");
				sheet.addMergedRegion(new CellRangeAddress(17, 17, 8,11));
				
				XSSFRow headerIdentificacionRecursosCuerpoFirstRow = sheet.createRow(18);			
				XSSFCell PersonalDatosCell = headerIdentificacionRecursosCuerpoFirstRow.createCell(0);
				PersonalDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				PersonalDatosCell.setCellValue(fichaTecnica.getPersonal());				
				sheet.addMergedRegion(new CellRangeAddress(18,22, 0, 3));
				XSSFCell EquiHerraDatosCell = headerIdentificacionRecursosCuerpoFirstRow.createCell(4);
				EquiHerraDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				EquiHerraDatosCell.setCellValue(fichaTecnica.getEquipos());				
				sheet.addMergedRegion(new CellRangeAddress(18,22, 4, 7));
				XSSFCell  MateSumiDatosCell = headerIdentificacionRecursosCuerpoFirstRow.createCell(8);
				MateSumiDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				MateSumiDatosCell.setCellValue(fichaTecnica.getMateriales());				
				sheet.addMergedRegion(new CellRangeAddress(18,22, 8, 11));
				
				XSSFRow headerIdentificacionRecursosCabeceraSecondRow = sheet.createRow(23);
				XSSFCell AmbienteCell = headerIdentificacionRecursosCabeceraSecondRow.createCell(0);
				AmbienteCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				AmbienteCell.setCellValue(" Ambiente de Trabajo ");
				sheet.addMergedRegion(new CellRangeAddress(23, 23, 0, 3));
				XSSFCell DocuApliCell = headerIdentificacionRecursosCabeceraSecondRow.createCell(4);
				DocuApliCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				DocuApliCell.setCellValue(" Documentos Aplicados ");
				sheet.addMergedRegion(new CellRangeAddress(23, 23, 4, 7));
				XSSFCell ContInspCell = headerIdentificacionRecursosCabeceraSecondRow.createCell(8);
				ContInspCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ContInspCell.setCellValue(" Controles e Inspecciones ");
				sheet.addMergedRegion(new CellRangeAddress(23, 23, 8,11));
				
				XSSFRow headerIdentificacionRecursosCuerpoSecondRow = sheet.createRow(24);			
				XSSFCell AmbienteDatosCell = headerIdentificacionRecursosCuerpoSecondRow.createCell(0);
				AmbienteDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				AmbienteDatosCell.setCellValue(fichaTecnica.getAmbientes());				
				sheet.addMergedRegion(new CellRangeAddress(24,28, 0, 3));
				XSSFCell DocuApliDatosCell = headerIdentificacionRecursosCuerpoSecondRow.createCell(4);
				DocuApliDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				DocuApliDatosCell.setCellValue(fichaTecnica.getDocumentosAplicado());				
				sheet.addMergedRegion(new CellRangeAddress(24,28, 4, 7));
				XSSFCell  ContInsDatosCell = headerIdentificacionRecursosCuerpoSecondRow.createCell(8);
				ContInsDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ContInsDatosCell.setCellValue(fichaTecnica.getControles());				
				sheet.addMergedRegion(new CellRangeAddress(24,28, 8, 11));
				
				//cabecera Libre
				//estilo				
				CellStyle headerLibreCellStyle = workbook.createCellStyle();							
				headerLibreCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());												
				headerLibreCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				//estilo fin						
				XSSFRow headerLibreRow = sheet.createRow(29);
				XSSFCell headerLibreCell = headerLibreRow.createCell(0);
				headerLibreCell.setCellStyle(headerLibreCellStyle);				
				sheet.addMergedRegion(new CellRangeAddress(29, 29, 0, 11));
				
				XSSFRow headerIdentificacionRecursosCabeceraThridRow = sheet.createRow(30);
				XSSFCell RegistrosCell = headerIdentificacionRecursosCabeceraThridRow.createCell(0);
				RegistrosCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				RegistrosCell.setCellValue(" Registros a Controlar ");
				sheet.addMergedRegion(new CellRangeAddress(30, 30, 0, 5));
				XSSFCell IndicadoresCell = headerIdentificacionRecursosCabeceraThridRow.createCell(6);
				IndicadoresCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				IndicadoresCell.setCellValue(" Indicadores Desempeño ");
				sheet.addMergedRegion(new CellRangeAddress(30, 30, 6, 11));
				
				XSSFRow headerIdentificacionRecursosCuerpoThridRow = sheet.createRow(31);			
				XSSFCell RegistrosDatosCell = headerIdentificacionRecursosCuerpoThridRow.createCell(0);
				RegistrosDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				RegistrosDatosCell.setCellValue(fichaTecnica.getRegistros());				
				sheet.addMergedRegion(new CellRangeAddress(31,35, 0, 5));
				XSSFCell IndicadoresDatosCell = headerIdentificacionRecursosCuerpoThridRow.createCell(6);
				IndicadoresDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				IndicadoresDatosCell.setCellValue(fichaTecnica.getIndicadores());				
				sheet.addMergedRegion(new CellRangeAddress(31,35, 6, 11));
				
				//cabecera Componentes del proceso del proceso
				//estilo
				XSSFFont headerAprobacionFont = workbook.createFont();
				headerAprobacionFont.setBold(true);
				headerAprobacionFont.setFontHeightInPoints((short) 14);
				CellStyle headerAprobacionCellStyle = workbook.createCellStyle();
				headerAprobacionCellStyle.setFont(headerAprobacionFont);
				headerAprobacionCellStyle.setAlignment(headerAprobacionCellStyle.ALIGN_CENTER);				
				headerAprobacionCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());												
				headerAprobacionCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				//estilo fin						
				XSSFRow headerAprobacionRow = sheet.createRow(36);
				XSSFCell headerAprobacionCell = headerAprobacionRow.createCell(0);
				headerAprobacionCell.setCellStyle(headerAprobacionCellStyle);
				headerAprobacionCell.setCellValue(" Aprobación ");
				sheet.addMergedRegion(new CellRangeAddress(36, 36, 0, 11));
				
				XSSFRow headerAprobacionCabeceraRow = sheet.createRow(37);
				XSSFCell ElaboradoCell = headerAprobacionCabeceraRow.createCell(0);
				ElaboradoCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ElaboradoCell.setCellValue(" Elaborado por ");
				sheet.addMergedRegion(new CellRangeAddress(37, 37, 0, 3));
				XSSFCell ConsensadoCell = headerAprobacionCabeceraRow.createCell(4);
				ConsensadoCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				ConsensadoCell.setCellValue(" Consensado por ");
				sheet.addMergedRegion(new CellRangeAddress(37, 37, 4, 7));
				XSSFCell AprobadoCell = headerAprobacionCabeceraRow.createCell(8);
				AprobadoCell.setCellStyle(headerIdentificacionSubTitulosCellStyle);
				AprobadoCell.setCellValue(" Aprobado por ");
				sheet.addMergedRegion(new CellRangeAddress(37, 37, 8,11));
				
				XSSFRow headerAprobacionCuerpoRow = sheet.createRow(38);			
				XSSFCell ElaboradoDatosCell = headerAprobacionCuerpoRow.createCell(0);
				ElaboradoDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ElaboradoDatosCell.setCellValue(fichaTecnica.getElaborado());				
				sheet.addMergedRegion(new CellRangeAddress(38,39, 0, 3));
				XSSFCell ConsensadoDatosCell = headerAprobacionCuerpoRow.createCell(4);
				ConsensadoDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				ConsensadoDatosCell.setCellValue(fichaTecnica.getConsensado());				
				sheet.addMergedRegion(new CellRangeAddress(38,39, 4, 7));
				XSSFCell  AprobadoDatosCell = headerAprobacionCuerpoRow.createCell(8);
				AprobadoDatosCell.setCellStyle(headerIdentificacionDatosCellStyle);
				AprobadoDatosCell.setCellValue(fichaTecnica.getAprobado());				
				sheet.addMergedRegion(new CellRangeAddress(38,39, 8, 11));
				
				//cabecera fin
				//cuerpo fin
				/*
				for (int i = 0; i < fichaTecnica.size(); ++i) {
					XSSFRow dataRow = sheet.createRow(i + 3);
					if(cancelacion.get(i).getCodigoDocumento()!=null) {
						dataRow.createCell(0).setCellValue(cancelacion.get(i).getCodigoDocumento());}	
					if(cancelacion.get(i).getTituloDocumento()!=null) {
					dataRow.createCell(1).setCellValue(cancelacion.get(i).getTituloDocumento());}
					if(cancelacion.get(i).getNumeroRevision()!=null) {
					dataRow.createCell(2).setCellValue(cancelacion.get(i).getNumeroRevision());}
					if(cancelacion.get(i).getMotivoCancelacion()!=null) {
					dataRow.createCell(3).setCellValue(cancelacion.get(i).getMotivoCancelacion());}
					if(cancelacion.get(i).getSustentoSolicitud()!=null) {
					dataRow.createCell(4).setCellValue(cancelacion.get(i).getSustentoSolicitud());}
					if(cancelacion.get(i).getNombreColaborador()!=null || cancelacion.get(i).getApeMatColaborador()!=null || cancelacion.get(i).getApePatColaborador()!=null) {
					dataRow.createCell(5).setCellValue(cancelacion.get(i).getNombreColaborador()+" "+
													   cancelacion.get(i).getApeMatColaborador()+" "+
													   cancelacion.get(i).getApePatColaborador());}
					if(cancelacion.get(i).getFechaSolicitud()!=null) {
						Cell celda = dataRow.createCell(6);
                        celda.setCellValue(cancelacion.get(i).getFechaSolicitud());
                       celda.setCellStyle(estiloFecha);
					}
					if(cancelacion.get(i).getAprobador()!=null) {
					dataRow.createCell(7).setCellValue(cancelacion.get(i).getAprobador());}
					if(cancelacion.get(i).getFechaAprobacion()!=null) {
						Cell celda = dataRow.createCell(8);
                        celda.setCellValue(cancelacion.get(i).getFechaAprobacion());
                       celda.setCellStyle(estiloFecha);					
					}
					if(cancelacion.get(i).getCancelador()!=null) {
					dataRow.createCell(9).setCellValue(cancelacion.get(i).getCancelador());}
					if(cancelacion.get(i).getFechaCancelacion()!=null) {
						Cell celda = dataRow.createCell(10);
                        celda.setCellValue(cancelacion.get(i).getFechaCancelacion());
                       celda.setCellStyle(estiloFecha);					
					 }
				}
*/
				//XSSFRow dataRow = sheet.createRow(5 + cancelacion.size());
				//dataRow.createCell(0).setCellValue("Total de Documentos");
				//dataRow.createCell(2).setCellValue(cancelacion.size());							
				
				//rutaPlazoXlsx = "Ficha_Tecnica(" + fecha.get(Calendar.YEAR)+"-"+fecha.get(Calendar.MONTH)+"-"+fecha.get(Calendar.DAY_OF_MONTH)+").xlsx";
				rutaPlazoXlsx = endpointServidor + System.currentTimeMillis() + ".xlsx";
				FileOutputStream file = new FileOutputStream(rutaPlazoXlsx);
			
				/*rutaPlazoXlsx = endpointServidor + System.currentTimeMillis() + ".xlsx";
                FileOutputStream file = new FileOutputStream(rutaPlazoXlsx);*/
                
                
				workbook.write(file);
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rutaPlazoXlsx;
	}


}