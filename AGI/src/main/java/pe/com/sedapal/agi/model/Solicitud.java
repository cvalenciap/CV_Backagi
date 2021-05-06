package pe.com.sedapal.agi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class Solicitud implements Serializable {

	private static final long serialVersionUID = 7966862308744277335L;
	private Long   codigo;
    private String motivo;
    private String estado;
    private String nroCodigoDocumento;
    private String codigoDocumento;
    private String descripcionDocumento;
    private String estadoDocumento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String sustentoSolicitado;
    private String sustentoAprobado;
    private Date fechaSolicitud;

    @JsonIgnore
    private Long numeroRegistros;

    public Solicitud() {
        super();
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(String estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Long getNumeroRegistros() {
        return numeroRegistros;
    }

    public void setNumeroRegistros(Long numeroRegistros) {
        this.numeroRegistros = numeroRegistros;
    }

    public String getSustentoSolicitado() {
        return sustentoSolicitado;
    }

    public void setSustentoSolicitado(String sustentoSolicitado) {
        this.sustentoSolicitado = sustentoSolicitado;
    }

    public String getSustentoAprobado() {
        return sustentoAprobado;
    }

    public void setSustentoAprobado(String sustentoAprobado) {
        this.sustentoAprobado = sustentoAprobado;
    }

    public String getNroCodigoDocumento() {
        return nroCodigoDocumento;
    }

    public void setNroCodigoDocumento(String nroCodigoDocumento) {
        this.nroCodigoDocumento = nroCodigoDocumento;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}
