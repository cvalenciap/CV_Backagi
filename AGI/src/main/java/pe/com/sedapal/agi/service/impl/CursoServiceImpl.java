package pe.com.sedapal.agi.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.ICursoDAO;
import pe.com.sedapal.agi.dao.ITrabajadorDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Curso;
import pe.com.sedapal.agi.model.Trabajador;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.ConstanteRequest;
import pe.com.sedapal.agi.model.request_objects.CursoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.ICursoService;
import pe.com.sedapal.agi.service.ITrabajadorService;

@Service
public class CursoServiceImpl implements ICursoService {
	
	@Autowired ICursoDAO dao;

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
	public List<Curso> obtenerCursos(CursoRequest cursoRequest, PageRequest paginaRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerCursos(cursoRequest, paginaRequest);
	}

	@Override
	public List<Constante> listarTipoCursos(ConstanteRequest constanteRequest) {
		// TODO Auto-generated method stub
		return this.dao.listarTipoCursos(constanteRequest);
	}

	@Override
	public Curso registrarCurso(Curso curso) {
		// TODO Auto-generated method stub
//		return this.dao.registrarCurso(curso);
		return this.dao.registrarDatosCurso(curso);
	}
	
	@Override
	public Curso obtenerDatosCurso(Long idCurso) {
		return this.dao.obtenerDatosCursoSesion(idCurso);
	}
	
	@Override
	public Map<String, Object> eliminarCurso(Long codigo) {
		Map<String,Object> respuesta = new TreeMap<String,Object>();			
		Map<String, Object> queryResp = this.dao.eliminarCurso(codigo);			
		BigDecimal bdCast = (BigDecimal)queryResp.get("o_retorno");
        int iRespuesta = bdCast.intValueExact();
		String sRespuesta;
		if (iRespuesta <= 0) {
			sRespuesta = (String)queryResp.get("o_mensaje");
			respuesta.put("estado", 0); 
			respuesta.put("error", sRespuesta);
		}else {
			sRespuesta = (String)queryResp.get("o_mensaje");
			respuesta.put("estado", 1); 
			respuesta.put("error", sRespuesta);
			}
		return respuesta;
	}
	
	@Override
	public Curso actualizarCurso(Curso curso) {
		return this.dao.actualizarDatosCurso(curso);
	}
}
