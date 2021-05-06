package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Ruta;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RutaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IRutaDAO {
	List<Ruta> obtenerRuta(RutaRequest rutaRequest, PageRequest pageRequest);
	Paginacion getPaginacion();
	Error getError();
	List<Ruta> guardarRuta(Ruta ruta, Long codigo, String usuario);
	Boolean eliminarRuta(Long codigo, String usuario);
}