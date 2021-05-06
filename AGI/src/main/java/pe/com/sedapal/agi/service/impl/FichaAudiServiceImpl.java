package pe.com.sedapal.agi.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IFichaAudiDAO;
import pe.com.sedapal.agi.model.CursoAuditor;
import pe.com.sedapal.agi.model.CursoNorma;
import pe.com.sedapal.agi.model.FichaAudi;
import pe.com.sedapal.agi.model.request_objects.FichaAudiRequest;
import pe.com.sedapal.agi.model.request_objects.InfoAuditorRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.ListaPaginada;
import pe.com.sedapal.agi.service.IFichaAudiService;

@Service
public class FichaAudiServiceImpl implements IFichaAudiService {

	@Autowired
	IFichaAudiDAO dao;

	@Override
	public Error getError() {
		return dao.getError();
	}

	@Override
	public Map<String, List<Object>> obtenerParametros() {
		return dao.obtenerParametros();
	}

	@Override
	public List<FichaAudi> obtenerListaAuditores(FichaAudiRequest request) {
		return dao.obtenerListaAuditores(request);
	}

	@Override
	public ListaPaginada<FichaAudi> obtenerInfoAuditor(InfoAuditorRequest request, Integer pagina, Integer registros) {
		return dao.obtenerInfoAuditores(request, pagina, registros);
	}

	@Override
	public Integer eliminarFichaAuditor(Integer idFicha, String usuario) {
		return dao.eliminarFichaAuditor(idFicha, usuario);
	}

	@Override
	public FichaAudi registrarDatosFichaAuditor(FichaAudi fichaAuditor) {
		return dao.registrarDatosFichaAuditor(fichaAuditor);
	}

	@Override
	public FichaAudi guardarFichaAuditor(FichaAudi fichaAuditor) {
		return dao.guardarFichaAuditor(fichaAuditor);
	}

	@Override
	public Integer guardarCursosPendientes(CursoAuditor curso) {
		return dao.guardarCursosPendientes(curso);
	}

	@Override
	public Integer guardarCursosCompletados(CursoAuditor curso) {
		return dao.guardarCursosCompletados(curso);
	}

	@Override
	public Integer guardarCursosNorma(CursoNorma curso) {
		return dao.guardarCursosNorma(curso);
	}

	@Override
	public Integer registrarCambiosFichaAuditor(FichaAudi fichaAuditor) {
		return dao.registrarCambiosFichaAuditor(fichaAuditor);
	}

	@Override
	public Integer actualizarFichaAudi(FichaAudi fichaAuditor) {
		return dao.actualizarFichaAudi(fichaAuditor);
	}

	@Override
	public Integer actualizarCursosCompletados(CursoAuditor curso) {
		return dao.actualizarCursosCompletados(curso);
	}

	@Override
	public Integer actualizarCursosPendientes(CursoAuditor curso) {
		return dao.actualizarCursosPendientes(curso);
	}

	@Override
	public Integer actualizarCursoNorma(CursoNorma curso) {
		return dao.actualizarCursoNorma(curso);
	}

}
