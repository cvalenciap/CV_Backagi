package pe.com.sedapal.agi.dao;

import pe.com.sedapal.agi.model.FichaTecnica;
import pe.com.sedapal.agi.model.request_objects.FichaTecnicaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IFichaDAO {
	FichaTecnica obtenerFicha(FichaTecnicaRequest fichaRequest);
	Paginacion getPaginacion();
	Error getError();
	FichaTecnica guardarFicha(FichaTecnica ficha, Long codigo, String usuario);
	Boolean eliminarFicha(Long ficha, Long jerarquia, String usuario);
}