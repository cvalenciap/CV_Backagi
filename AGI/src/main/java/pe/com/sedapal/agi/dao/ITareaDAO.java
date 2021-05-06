package pe.com.sedapal.agi.dao;

import pe.com.sedapal.agi.model.Cancelacion;
import pe.com.sedapal.agi.model.Documento;
import pe.com.sedapal.agi.model.Solicitud;
import pe.com.sedapal.agi.model.SolicitudDocumentoComplementario;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.TareaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;

import java.util.List;

public interface ITareaDAO {

    public List<Cancelacion> obtenerSolicitudesCancelacion(TareaRequest tareaRequest, PageRequest pageRequest, Long iUsuario);
    public List<Cancelacion> obtenerSolicitudesCancelacionReporte(TareaRequest tareaRequest, PageRequest pageRequest, Long iUsuario);
    public Cancelacion crearActualizarSolicitudCancelacion(Cancelacion cancelacion);
    String subirDocumentoGoogleDrive(TareaRequest tareaRequest, String idDocGoogle, String iUsuario);
    public Error obtenerError();
    public Paginacion obtenerPaginacion();
    public Cancelacion obtenerSolicitudCancelacion(Long idCancelacion);
    
    boolean crearActualizarSolicitudCancDocComplementario(SolicitudDocumentoComplementario solicitudDocumentoComplementario);
    public boolean crearActualizarDatosSolicitudCancelacion(Cancelacion cancelacion);
    public List<SolicitudDocumentoComplementario> obtenerSolicitudesDocumentoComplementario(Long idCancelacion);
    public boolean aprobarRechazarSolicitudCancelacion(Cancelacion cancelacion);
    public Cancelacion obtenerSolicitudCancelacionActivaDeDocumento(Long idDocumento);
    public boolean cancelarDocumento(Cancelacion cancelacion);
    
    //cguerra
    
    public boolean cancelarDocumentoGoogleDrive(Cancelacion cancelacion);
    
    //cguerra
    
    
    
    public String obtenerUltimaRutaCopiaControladaDocumento(Long idDocumento);
    public List<Documento> obtenerDocumentosJerarquicos(Long idDocumentoHijo);
    public Integer obtenerCantidadSolicitudCancelacion(Long idDocumento);
    public List<Cancelacion> obtenerCancelacionAprobacion(TareaRequest tareaRequest, PageRequest pageRequest, Long iUsuario);
}
