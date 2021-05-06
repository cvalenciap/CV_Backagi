package pe.com.sedapal.agi.service;

import java.util.List;
import java.util.Map;

import pe.com.sedapal.agi.model.CursoAuditor;
import pe.com.sedapal.agi.model.CursoNorma;
import pe.com.sedapal.agi.model.FichaAudi;
import pe.com.sedapal.agi.model.request_objects.FichaAudiRequest;
import pe.com.sedapal.agi.model.request_objects.InfoAuditorRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.ListaPaginada;

public interface IFichaAudiService {

	Error getError();
	
	Map<String, List<Object>> obtenerParametros();

	List<FichaAudi> obtenerListaAuditores(FichaAudiRequest request);
	
	ListaPaginada<FichaAudi> obtenerInfoAuditor(InfoAuditorRequest request, Integer pagina, Integer registros);

	Integer eliminarFichaAuditor(Integer idFicha, String usuario);

	FichaAudi registrarDatosFichaAuditor(FichaAudi fichaAuditor);

	FichaAudi guardarFichaAuditor(FichaAudi fichaAuditor);

	Integer guardarCursosPendientes(CursoAuditor curso);

	Integer guardarCursosCompletados(CursoAuditor curso);

	Integer guardarCursosNorma(CursoNorma curso);
	
	Integer registrarCambiosFichaAuditor(FichaAudi fichaAuditor);

	Integer actualizarFichaAudi(FichaAudi fichaAuditor);

	Integer actualizarCursosCompletados(CursoAuditor curso);

	Integer actualizarCursosPendientes(CursoAuditor curso);

	Integer actualizarCursoNorma(CursoNorma curso);

}
