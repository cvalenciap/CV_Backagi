package pe.com.sedapal.agi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IPreguntaCursoDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.PreguntaCurso;
import pe.com.sedapal.agi.model.PreguntaDetalle;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.PreguntaCursoRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IPreguntaCursoService;

@Service
public class PreguntaCursoServiceImpl implements IPreguntaCursoService{
	
	@Autowired IPreguntaCursoDAO dao;
	
	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}
	
	@Override
	public List<PreguntaCurso> obtenerPregunta(PreguntaCursoRequest preguntaRequest, PageRequest paginaRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerPregunta(preguntaRequest, paginaRequest);
	}
	
	@Override
	public PreguntaCurso registrarPregunta(PreguntaCurso preguntaCurso) {
		return this.dao.guardarPregunta(preguntaCurso);
	}
	
	@Override
	public Boolean eliminarPregunta(Long id) {
		return this.dao.eliminarPregunta(id);
	}
	
	@Override
	public List<ConstanteRequest> obtenerTipoPregunta(){
		return this.dao.obtenerTipoPregunta();
	}
	
	@Override
	public List<PreguntaCurso> obtenerCurso(PreguntaCursoRequest preguntaRequest,PageRequest paginaRequest){
		return this.dao.obtenerCurso(preguntaRequest, paginaRequest);
	}
	
	@Override
	public PreguntaCurso actualizarPregunta(PreguntaCurso preguntaCurso) {
		return this.dao.guardarDetalle(preguntaCurso);
	}
	
}
