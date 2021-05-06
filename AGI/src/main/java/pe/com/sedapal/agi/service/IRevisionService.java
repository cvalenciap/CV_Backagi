package pe.com.sedapal.agi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Jefes;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequestAdjunto;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

public interface IRevisionService {
	List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest);
	Boolean eliminarRevision(Long id);
	Boolean eliminarProgramacion(Long id);
	List<Revision> obtenerRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest);
	Error getError();
	Revision crearRevision(Revision revisionRequest);
	List<Revision> grabarPrograma(String idProg, String codFichaLogueado, String idEstProg, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc);
	List<Revision> grabarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc);
	List<Revision> finalizarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc);
	Revision crearDocumentoGoogleDrive(String idDocGoogle, Long idRevi);
	List<Revision> consultarProgramacion(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarProgramacionPorIdProg(RevisionRequest revisionRequest);
	List<Jefes> obtenerJefes(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarProgramaciones(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarDetDistribuciones(RevisionRequest revisionRequest, PageRequest pageRequest);
	Paginacion getPaginacion();
	List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest);
	Revision rechazarDocumento(Long idDocumento, Revision revision);
	List<RevisionRequest> consultarListaProgramada(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<RevisionRequest> consultarListaDistribucionEjec(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarListaEjec(RevisionRequest revisionRequest, PageRequest pageRequest);
	Revision guardarDocumentoRevision(Revision revision);
	Revision guardarDocumentoRevisionAdjunto(Revision revision);	
	boolean registrarDocumentosRevision(Revision revision);
	boolean actualizarConocimientoLectura(Conocimiento conocimiento);
	boolean grabarEjecucion(List<Revision> revisionEjecucion);
	//cguerra
	boolean grabarEjecucionProg(List<RevisionRequestAdjunto> Revision);
	//cguerra
	List<Revision> grabarEjecucionAdjuntar(MultipartFile[] adjuntos);
	
	
	boolean finalizarEjecucion(List<RevisionRequestAdjunto> Revision);
	List<Revision> obtenerRevisionesDoc(RevisionRequest revisionRequest, PageRequest pageRequest);
}