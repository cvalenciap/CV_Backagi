package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Solicitud;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.request_objects.DocumentoRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface ITareaService {

    public List<Cancelacion> obtenerSolicitudesCancelacion(TareaRequest tareaRequest, PageRequest pageRequest);
    public List<Cancelacion> obtenerSolicitudesCancelacionReporte(TareaRequest tareaRequest, PageRequest pageRequest);
    boolean crearActualizarSolicitudCancelacion(Cancelacion cancelacion,Optional<MultipartFile> file);
    String subirDocumentoGoogleDrive(TareaRequest tareaRequest, String idDocGoogle);
    public Error obtenerError();
    public Paginacion obtenerPaginacion();
    public Cancelacion obtenerSolicitudCancelacion(Long idCancelacion);
    public boolean aprobarRechazarSolicitudCancelacion(Cancelacion cancelacion);
    public boolean cancelarDocumento(Cancelacion cancelacion);
    //CGUERRA
    public boolean cancelarDocumentoGoogleDrive(Cancelacion cancelacion);
    //CGUERRA
    
    
    
    
    public String obtenerUltimaRutaCopiaControladaDocumento(Long idDocumento);
    public List<Documento> obtenerDocumentosJerarquicos(Long idDocumentoHijo);
    public Integer obtenerCantidadSolicitudCancelacion(Long idDocumento);
    String generarExcelPlazo(TareaRequest tareaRequest, PageRequest pageRequest) throws IOException;
    public List<Cancelacion> obtenerCancelacionAprobacion(TareaRequest tareaRequest, PageRequest pageRequest);
}
