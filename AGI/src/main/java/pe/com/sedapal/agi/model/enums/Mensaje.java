package pe.com.sedapal.agi.model.enums;

import java.io.Serializable;

public enum Mensaje implements Serializable {
    MENS_TRAB_BAJA("Participante se le dio de Baja"),
	MENS_EQUI_BAJA("Participante tiene otro Equipo"),
	MENS_CARG_BAJA("Participante tiene otro Cargo");
       
    private final String text;

    Mensaje(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
	
}