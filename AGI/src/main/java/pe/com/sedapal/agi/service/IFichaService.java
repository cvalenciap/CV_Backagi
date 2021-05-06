package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.FichaTecnica;
import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IFichaService {
	FichaTecnica obtenerFicha(FichaTecnicaRequest FichaRequest);
	Error getError();
	Paginacion getPaginacion();
	FichaTecnica crearFicha(FichaTecnica ficha,Optional<MultipartFile> file);
	FichaTecnica actualizarFicha(FichaTecnica ficha, Long codigo);
	Boolean eliminarFicha(Long codigo);
	String generarExcelPlazo(FichaTecnicaRequest fichaTecnicaRequest) throws IOException;
}