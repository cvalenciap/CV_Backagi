package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ISesionService {

	public Error getError();

	public Paginacion getPaginacion();

	public Sesion obtenerDatosSesionCurso(Long idCurso, Long idSesion);
}
