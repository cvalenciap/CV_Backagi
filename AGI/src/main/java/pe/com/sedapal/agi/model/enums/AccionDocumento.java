package pe.com.sedapal.agi.model.enums;

public enum AccionDocumento {
    ACTIVO("1"),
    INACTIVO("2");
    
    private final String text;

    AccionDocumento(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}