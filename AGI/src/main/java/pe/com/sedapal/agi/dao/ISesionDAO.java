package pe.com.sedapal.agi.dao;

import pe.com.sedapal.agi.model.Sesion;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface ISesionDAO {

	public Error getError();
	
	public Paginacion getPaginacion();
	
	public Sesion obtenerDatosSesionCurso(Long idCurso, Long idSesion);
}
