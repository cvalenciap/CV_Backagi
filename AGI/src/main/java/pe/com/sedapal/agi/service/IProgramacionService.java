package pe.com.sedapal.agi.service;

import java.util.List;

import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ProgramaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IProgramacionService {
	List<Programa> obtenerProgramas(ProgramaRequest programaRequest, PageRequest paginaRequest);
	public Boolean eliminarPrograma(Long codigo);
	public Paginacion getPaginacion();
	public Error getError();
	
	public void registrarDatosPrograma(Programa programa,List<Auditoria> listaAuditorias);
	
	public Programa obtenerProgramaPorId(Long idPrograma);
	
	public void actualizarDatosPrograma(Programa programa, List<Auditoria> listaAuditoriasEliminadas,
			List<Auditoria> listaAuditoriasNuevas);
	
	
	
}
