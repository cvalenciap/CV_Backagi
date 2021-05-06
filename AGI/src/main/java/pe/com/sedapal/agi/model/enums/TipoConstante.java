package pe.com.sedapal.agi.model.enums;

import java.io.Serializable;

public enum TipoConstante implements Serializable {
    ETAPA_RUTA("Listado de Etapas de Ruta (Fases del Documento)");
       
    private final String text;

	TipoConstante(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
	
}