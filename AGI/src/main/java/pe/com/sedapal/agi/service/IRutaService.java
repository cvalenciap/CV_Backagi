package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.response_objects.Error;

import java.util.List;

import pe.com.sedapal.agi.model.Ruta;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RutaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IRutaService {
	List<Ruta> obtenerRuta(RutaRequest rutaRequest, PageRequest pageRequest);
	Error getError();
	Paginacion getPaginacion();
	Ruta crearRuta(Ruta ruta);
	Ruta actualizarRuta(Ruta ruta, Long codigo);
	Boolean eliminarRuta(Long codigo);
}