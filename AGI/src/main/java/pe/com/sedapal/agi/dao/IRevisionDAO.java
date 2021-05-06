package pe.com.sedapal.agi.dao;

import java.util.ArrayList;
import java.util.List;

import pe.com.sedapal.agi.model.Conocimiento;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Jefes;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequestAdjunto;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IRevisionDAO {
	List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest);
	Boolean eliminarRevision(Long idRevision);
	Boolean eliminarProgramacion(Long idProgramacion, String iUsuario, Long idUsuario);
	Revision crearRevision(Revision revision, String iUsuario, Long idUsuario);
	List<Revision> grabarPrograma(String idProg,String codFichaLogueado, String idEstProg, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc, String iUsuario, Long idUsuario);
	List<Revision> grabarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc, String iUsuario, Long idUsuario);
	List<Revision> finalizarDistribucion(String codFichaLogueado, List<Revision> revisionSelecc, List<Revision> revisionNoSelecc, String iUsuario, Long idUsuario);
	Revision crearDocumentoGoogleDrive(String idDocGoogle, Long idRevi);
	List<Revision> obtenerRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest, Long iUsuario);
	Paginacion getPaginacion();
	Error getError();
	List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest);
	Revision rechazarDocumento(Long idDocumento, Revision revisionRechazo);
	boolean registrarRutasDocumentoRevision(Revision revision, String iUsuario);
	boolean actualizarConocimientoLectura(Conocimiento conocimiento, String iUsuario);
	List<Revision> consultarProgramacion(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarProgramacionPorIdProg(RevisionRequest revisionRequest);
	List<Jefes> obtenerJefes(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarProgramaciones(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarDetDistribuciones(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<RevisionRequest> consultarListaProgramada(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<RevisionRequest> consultarListaDistribucionEjec(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarListaEjec(RevisionRequest revisionRequest, PageRequest pageRequest);
	List<Revision> consultarProgEquipo(Long idProgramacion);
	List<Revision> consultarProgDistri(Long idProgramacion, Long idEquipo);
	boolean guardarEjecuciones(List<Revision> revisionEjecucion, Long codigo, String usuario, Long idUsuario);
	//boolean guardarEjecucionesProg(Revision revisionEjecucion, Long codigo, String usuario, Long idUsuario);
	boolean finalizarEjecuciones(RevisionRequestAdjunto revisionEjecucion, Long codigo, String usuario, Long idUsuario);
	List<Revision> obtenerRevisionesDoc(RevisionRequest revisionRequest, PageRequest pageRequest, Long idUsuario);
	boolean guardarEjecucionesProg(RevisionRequestAdjunto adjunto, Object codigo, String codUsuario, Long idUsuario);
	boolean guardarEjecucionesProg(RevisionRequestAdjunto revisionEjecucion, Long codigo, String usuario,
			Long idUsuario);
}