package pe.com.sedapal.agi.model.enums;

import java.io.Serializable;

public enum Fase implements Serializable {
    ELABORACION("Elaboracion"),
    CONSENSO("Consenso"),
    APROBACION("Aprobacion"),
    HOMOLOGACION("Homologacion");
       
    private final String text;

    Fase(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
	
}