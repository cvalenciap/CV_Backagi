package pe.com.sedapal.agi.model.enums;

import java.io.Serializable;
import java.math.BigDecimal;

public enum EstadoConstante implements Serializable {
    ACTIVO("1"),
    INACTIVO("0"),
	ESTADO_SOLI_EMITIDO("Emitido"),
    ESTADO_SOLI_RECHAZADO("Rechazado"),
    ESTADO_SOLI_APROBADO("Aprobado"),
    ESTADO_DOCU_EMISION("Emision"),
    ESTADO_DOCU_CANCELADO("Cancelado"),
    ESTADO_DOCU_EN_REVISION("En Revision"),
    ESTADO_DOCU_APROBADO("Aprobado");
       
    private final String text;

    EstadoConstante(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
    
    public String getEstadoConstante() {
        return this.name();
    }
    
    static public EstadoConstante setEstado(BigDecimal valor) {
    	EstadoConstante result = ACTIVO; 
       
       switch ((valor.toString()).trim()) {
             case "1":
                    result = EstadoConstante.ACTIVO;
                    break;
             case "0":
                    result = EstadoConstante.INACTIVO;
                    break;            
       }
       
       if((valor.toString()).trim()==EstadoConstante.ACTIVO.text) {
             result = EstadoConstante.ACTIVO;
       }
       
       return result;
    }


	
}

