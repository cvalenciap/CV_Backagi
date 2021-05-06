package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.EvaluacionAuditor;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.Programa;
import pe.com.sedapal.agi.model.request_objects.EvaluacionAuditorRequest;
import pe.com.sedapal.agi.model.request_objects.NormaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IEvaluacionAuditorDAO {
	
	//lista para la grilla
	public List<EvaluacionAuditor> obtenerListaEvaAuditorGrilla(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest);
	
	//obtener uno
	public EvaluacionAuditor obtenerEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest);
	
 
	public List<Pregunta> obtenerListaAspectos(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest);
	
	//obtener la lista de aspectos segun idrol y idEvalaucion 
	public List<Pregunta> obtenerListaAspectosSegunIdEva(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest);
	  
	//12022019
	public List<Pregunta> buscarResPorCodEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest);
	
	
	//Eliminar una evaluacion Auditor
	Boolean eliminarEvaluacionAuditor(Long codigo, String usuario);
	
	
	//Guardar Evalaucion Auditor
	public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor);
	
	
	//Guardar Evalaucion Auditor, con la lista
	public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta>listaPreguntas);
	 
	 
	public Boolean actualizarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta> listaPreguntas);
	
	public Paginacion getPaginacion();
	public Error getError();  
	
	

}
