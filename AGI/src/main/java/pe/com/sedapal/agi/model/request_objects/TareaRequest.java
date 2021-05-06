package pe.com.sedapal.agi.model.request_objects;

import java.util.Date;

public class TareaRequest {
    private Long codigoSolicitud;
    private String codigoDocumento;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String estado;
    private Long estadoDoc;
    private Long 	idproc;
	private Long 	idalcasgi;
	public Long getIdproc() {
		return idproc;
	}

	public void setIdproc(Long idproc) {
		this.idproc = idproc;
	}

	public Long getIdalcasgi() {
		return idalcasgi;
	}

	public void setIdalcasgi(Long idalcasgi) {
		this.idalcasgi = idalcasgi;
	}

	public Long getIdgeregnrl() {
		return idgeregnrl;
	}

	public void setIdgeregnrl(Long idgeregnrl) {
		this.idgeregnrl = idgeregnrl;
	}

	public Long getIdtipodocu() {
		return idtipodocu;
	}

	public void setIdtipodocu(Long idtipodocu) {
		this.idtipodocu = idtipodocu;
	}

	private Long 	idgeregnrl;
	private Long 	idtipodocu;
	private Date  	fechacandesde;
	private Date	fechacanhasta;
  

	public Date getFechacandesde() {
		return fechacandesde;
	}

	public void setFechacandesde(Date fechacandesde) {
		this.fechacandesde = fechacandesde;
	}

	public Date getFechacanhasta() {
		return fechacanhasta;
	}

	public void setFechacanhasta(Date fechacanhasta) {
		this.fechacanhasta = fechacanhasta;
	}

	private String usuario;
    private Long nroCodigoDocumento;
    private String motivoCancelacion;
	private Long idRevisionSelecc;
    private Long idDocumenSelecc;
    private String  rutaDocuSelecc;
    private String tituloDocumento;

	public Long getIdRevisionSelecc() {
		return idRevisionSelecc;
	}

	public Long getEstadoDoc() {
		return estadoDoc;
	}

	public void setEstadoDoc(Long estadoDoc) {
		this.estadoDoc = estadoDoc;
	}

	public void setIdRevisionSelecc(Long idRevisionSelecc) {
		this.idRevisionSelecc = idRevisionSelecc;
	}

	public Long getIdDocumenSelecc() {
		return idDocumenSelecc;
	}

	public void setIdDocumenSelecc(Long idDocumenSelecc) {
		this.idDocumenSelecc = idDocumenSelecc;
	}

	public String getRutaDocuSelecc() {
		return rutaDocuSelecc;
	}

	public void setRutaDocuSelecc(String rutaDocuSelecc) {
		this.rutaDocuSelecc = rutaDocuSelecc;
	}

    public Long getNroCodigoDocumento() {
        return nroCodigoDocumento;
    }

    public void setNroCodigoDocumento(Long nroCodigoDocumento) {
        this.nroCodigoDocumento = nroCodigoDocumento;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getCodigoSolicitud() {
        return codigoSolicitud;
    }

    public void setCodigoSolicitud(Long codigoSolicitud) {
        this.codigoSolicitud = codigoSolicitud;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

	public String getTituloDocumento() {
		return tituloDocumento;
	}

	public void setTituloDocumento(String tituloDocumento) {
		this.tituloDocumento = tituloDocumento;
	}
    
    

}
