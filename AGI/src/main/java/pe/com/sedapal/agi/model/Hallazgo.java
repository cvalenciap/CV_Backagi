package pe.com.sedapal.agi.model;

public class Hallazgo {
	private Number item;
	private Number idDeteccionHallazgo;
	private Number idAmbito;
	private String ambito;//
	private Number idorigenDeteccion;
	private String origenDeteccion;//	
	private Number idTipoNoConformidad;//
	private String TipoNoConformidad;
	private String  fechaIngreso;
	private String descripHallazgo;
	private Number idDetector;
	private String detector;
	private String estado;
	private String fechaInicioPlan;
	private String fechaFinPlan;
	
	public Hallazgo(Number idDeteccionHallazgo, Number idAmbito, String ambito, Number idorigenDeteccion,
			String origenDeteccion, Number idTipoNoConformidad, String tipoNoConformidad, String fechaIngreso,
			String descripHallazgo, Number idDetector, String detector, String estado, String fechaInicioPlan,
			String fechaFinPlan, Number item) {
		super();
		this.idDeteccionHallazgo = idDeteccionHallazgo;
		this.idAmbito = idAmbito;
		this.ambito = ambito;
		this.idorigenDeteccion = idorigenDeteccion;
		this.origenDeteccion = origenDeteccion;
		this.idTipoNoConformidad = idTipoNoConformidad;
		this.TipoNoConformidad = tipoNoConformidad;
		this.fechaIngreso = fechaIngreso;
		this.descripHallazgo = descripHallazgo;
		this.idDetector = idDetector;
		this.detector = detector;
		this.estado = estado;
		this.fechaInicioPlan = fechaInicioPlan;
		this.fechaFinPlan = fechaFinPlan;
		this.item=item;
	}

	public Number getItem() {
		return item;
	}

	public void setItem(Number item) {
		this.item = item;
	}

	public Hallazgo() {
		super();
	}

	public Number getIdDeteccionHallazgo() {
		return idDeteccionHallazgo;
	}

	public void setIdDeteccionHallazgo(Number id) {
		this.idDeteccionHallazgo = id;
	}

	public Number getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Number idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public Number getIdorigenDeteccion() {
		return idorigenDeteccion;
	}

	public void setIdorigenDeteccion(Number idorigenDeteccion) {
		this.idorigenDeteccion = idorigenDeteccion;
	}

	public String getOrigenDeteccion() {
		return origenDeteccion;
	}

	public void setOrigenDeteccion(String origenDeteccion) {
		this.origenDeteccion = origenDeteccion;
	}

	public Number getIdTipoNoConformidad() {
		return idTipoNoConformidad;
	}

	public void setIdTipoNoConformidad(Number idTipoNoConformidad) {
		this.idTipoNoConformidad = idTipoNoConformidad;
	}

	public String getTipoNoConformidad() {
		return TipoNoConformidad;
	}

	public void setTipoNoConformidad(String tipoNoConformidad) {
		TipoNoConformidad = tipoNoConformidad;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getDescripHallazgo() {
		return descripHallazgo;
	}

	public void setDescripHallazgo(String descripHallazgo) {
		this.descripHallazgo = descripHallazgo;
	}

	public Number getIdDetector() {
		return idDetector;
	}

	public void setIdDetector(Number idDetector) {
		this.idDetector = idDetector;
	}

	public String getDetector() {
		return detector;
	}

	public void setDetector(String detector) {
		this.detector = detector;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaInicioPlan() {
		return fechaInicioPlan;
	}

	public void setFechaInicioPlan(String fechaInicioPlan) {
		this.fechaInicioPlan = fechaInicioPlan;
	}

	public String getFechaFinPlan() {
		return fechaFinPlan;
	}

	public void setFechaFinPlan(String fechaFinPlan) {
		this.fechaFinPlan = fechaFinPlan;
	}
	
}
