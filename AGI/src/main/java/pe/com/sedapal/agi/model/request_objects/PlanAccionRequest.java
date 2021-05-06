package pe.com.sedapal.agi.model.request_objects;

public class PlanAccionRequest {
	private Long idNoConformidad;
	private Long idPlanAccion;
	private String tipoNoConformidad;
	
	public Long getIdNoConformidad() {
		return idNoConformidad;
	}
	public void setIdNoConformidad(Long idNoConformidad) {
		this.idNoConformidad = idNoConformidad;
	}
	
	public Long getIdPlanAccion() {
		return idPlanAccion;
	}
	public void setIdPlanAccion(Long idPlanAccion) {
		this.idPlanAccion = idPlanAccion;
	}
	
	public String getTipoNoConformidad() {
		return tipoNoConformidad;
	}
	public void setTipoNoConformidad(String tipoNoConformidad) {
		this.tipoNoConformidad = tipoNoConformidad;
	}
}
