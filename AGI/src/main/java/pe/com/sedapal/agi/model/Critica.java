package pe.com.sedapal.agi.model;

import java.util.Date;

public class Critica {
    private Long codigoFase;
    private Long codigoColaboradorFase;
    private String nombreColaboradorFase;
    private String observacion;
    private Date fechaFase;
    private Long numeroFicha;
    
    

    public Long getNumeroFicha() {
		return numeroFicha;
	}

	public void setNumeroFicha(Long numeroFicha) {
		this.numeroFicha = numeroFicha;
	}

	public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getCodigoFase() {
        return codigoFase;
    }

    public void setCodigoFase(Long codigoFase) {
        this.codigoFase = codigoFase;
    }

    public Long getCodigoColaboradorFase() {
        return codigoColaboradorFase;
    }

    public void setCodigoColaboradorFase(Long codigoColaboradorFase) {
        this.codigoColaboradorFase = codigoColaboradorFase;
    }

    public String getNombreColaboradorFase() {
        return nombreColaboradorFase;
    }

    public void setNombreColaboradorFase(String nombreColaboradorFase) {
        this.nombreColaboradorFase = nombreColaboradorFase;
    }

    public Date getFechaFase() {
        return fechaFase;
    }

    public void setFechaFase(Date fechaFase) {
        this.fechaFase = fechaFase;
    }

}
