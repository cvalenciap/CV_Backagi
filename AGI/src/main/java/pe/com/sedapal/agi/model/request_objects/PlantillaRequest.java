package pe.com.sedapal.agi.model.request_objects;

import java.util.Date;

public class PlantillaRequest {
	private Long	idplan;
	private String	desplan;
	private String  nomplan;
	private String	rutplan;
	private Long	displan;
	private String  tipoplan;
	public Long getIdplan() {
		return idplan;
	}
	public void setIdplan(Long idplan) {
		this.idplan = idplan;
	}
	public String getDesplan() {
		return desplan;
	}
	public String getTipoplan() {
		return tipoplan;
	}
	public void setTipoplan(String tipoplan) {
		this.tipoplan = tipoplan;
	}
	public void setDesplan(String desplan) {
		this.desplan = desplan;
	}
	public String getNomplan() {
		return nomplan;
	}
	public void setNomplan(String nomplan) {
		this.nomplan = nomplan;
	}
	public String getRutplan() {
		return rutplan;
	}
	public void setRutplan(String rutplan) {
		this.rutplan = rutplan;
	}
	public Long getDisplan() {
		return displan;
	}
	public void setDisplan(Long displan) {
		this.displan = displan;
	}

}