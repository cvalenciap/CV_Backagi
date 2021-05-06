package pe.com.sedapal.agi.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.EvaluacionAuditor;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.request_objects.EvaluacionAuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IEvaluacionAuditorService {

	public List<EvaluacionAuditor> obtenerListaEvaluacionAuditorGrilla(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest);
	
	public EvaluacionAuditor obtenerEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest);
	
	public List<Pregunta> obtenerListaAspectos(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest);
	
	public List<Pregunta> obtenerListaAspectosSegunIdEva(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest);
	
	public List<Pregunta> buscarResPorCodEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest);
		
	public Boolean eliminarEvaluacionAuditor(Long codigo);
	   	  
	public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor);
		 
	public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta> listaPreguntas);
		
	public Boolean actualizarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta> listaPreguntas);
	
	public void imprimirPDF(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest);

	
	
	Paginacion getPaginacion();
		 
	public Error getError();
	
	
	
}
