package pe.com.sedapal.agi.model;

public class Plantilla {
	private Long	idplan;
	private String	desplan;
	private String  nomplan;
	private String	rutplan;
	private Long	displan;
	private String  tipoplan;
	private Long idColaborador;
	private DatosAuditoria datosAuditoria;
	public String getTipoplan() {
		return tipoplan;
	}
	public void setTipoplan(String tipoplan) {
		this.tipoplan = tipoplan;
	}
	public Long getIdplan() {
		return idplan;
	}
	public void setIdplan(Long idplan) {
		this.idplan = idplan;
	}
	public String getDesplan() {
		return desplan;
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
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
}