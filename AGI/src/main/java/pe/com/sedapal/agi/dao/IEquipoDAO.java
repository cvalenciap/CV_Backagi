package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Equipo;
import pe.com.sedapal.agi.model.request_objects.EquipoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IEquipoDAO {
	List<Equipo> obtenerEquipo(EquipoRequest equipoRequest);
	Error getError();
}