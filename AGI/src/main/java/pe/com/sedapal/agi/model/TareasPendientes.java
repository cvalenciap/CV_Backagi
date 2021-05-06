package pe.com.sedapal.agi.model;

public class TareasPendientes {
	private Long idColaborador;
	//Solicitudes
	private int cantidadParaRevision;
	private int cantidadParaCancelar;
	private int cantidadParaImprimir;
	//Documentos en Revisión
	private int cantidadElaboracion;
	private int cantidadConsenso;
	private int cantidadAprobacion;
	private int cantidadHomologacion;
	private int cantidadCancelacion;
	private int cantidadCambioPersonal;
	//Por Conocimiento
	private int cantidadRealizarRevision;

	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
	public int getCantidadParaRevision() {
		return cantidadParaRevision;
	}
	public void setCantidadParaRevision(int cantidadParaRevision) {
		this.cantidadParaRevision = cantidadParaRevision;
	}
	public int getCantidadParaCancelar() {
		return cantidadParaCancelar;
	}
	public void setCantidadParaCancelar(int cantidadParaCancelar) {
		this.cantidadParaCancelar = cantidadParaCancelar;
	}
	public int getCantidadParaImprimir() {
		return cantidadParaImprimir;
	}
	public void setCantidadParaImprimir(int cantidadParaImprimir) {
		this.cantidadParaImprimir = cantidadParaImprimir;
	}
	public int getCantidadElaboracion() {
		return cantidadElaboracion;
	}
	public void setCantidadElaboracion(int cantidadElaboracion) {
		this.cantidadElaboracion = cantidadElaboracion;
	}
	public int getCantidadConsenso() {
		return cantidadConsenso;
	}
	public void setCantidadConsenso(int cantidadConsenso) {
		this.cantidadConsenso = cantidadConsenso;
	}
	public int getCantidadAprobacion() {
		return cantidadAprobacion;
	}
	public void setCantidadAprobacion(int cantidadAprobacion) {
		this.cantidadAprobacion = cantidadAprobacion;
	}
	public int getCantidadHomologacion() {
		return cantidadHomologacion;
	}
	public void setCantidadHomologacion(int cantidadHomologacion) {
		this.cantidadHomologacion = cantidadHomologacion;
	}
	public int getCantidadCancelacion() {
		return cantidadCancelacion;
	}
	public void setCantidadCancelacion(int cantidadCancelacion) {
		this.cantidadCancelacion = cantidadCancelacion;
	}
	public int getCantidadCambioPersonal() {
		return cantidadCambioPersonal;
	}
	public void setCantidadCambioPersonal(int cantidadCambioPersonal) {
		this.cantidadCambioPersonal = cantidadCambioPersonal;
	}
	public int getCantidadRealizarRevision() {
		return cantidadRealizarRevision;
	}
	public void setCantidadRealizarRevision(int cantidadRealizarRevision) {
		this.cantidadRealizarRevision = cantidadRealizarRevision;
	}
}
