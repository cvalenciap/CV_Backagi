package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.ProgramaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IProgramacionDAO {
	public List<Programa> obtenerProgramas(ProgramaRequest programaRequest,PageRequest paginaRequest);
	Boolean eliminarPrograma(Long codigo, String usuario);
	public Paginacion getPaginacion();
	public Error getError();
	
	public void registrarDatosPrograma(Programa programa,List<Auditoria> listaAuditoria);
	
	public Programa registrarPrograma(Programa programa);
	
	public Auditoria registrarAuditoria(Auditoria auditoria);
	
	public Programa obtenerProgramaPorId(Long idPrograma);
	
	public void actualizarDatosPrograma(Programa programa,List<Auditoria> listaAuditoriasEliminadas, List<Auditoria> listaAuditoriasNuevas);
	
	public Programa modificarPrograma(Programa programa);
	
	public boolean eliminarDatosPrograma(Long idPrograma,String usuario, List<Auditoria> listaAuditorias);
}
