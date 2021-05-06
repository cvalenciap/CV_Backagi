package pe.com.sedapal.agi.model;

public class ListaVerificacionRequisito {
	private Long idLVRequisito;
	private Long idListaVerificacion;
	private Long idRequisito;
	private Long idDetalleRequisito;
	private String detalleRequisito;
	private String cuestionarioRequisito;
	private Long idNorma;
	private String descripcionRequisito;
	private String descripcionNorma;
	private String valorCalificacion;
	private String descripcionCalificacion;
	private HallazgoRequisito hallazgo;
	private DatosAuditoria datosAuditoria;
	
	public Long getIdLVRequisito() {
		return idLVRequisito;
	}
	public void setIdLVRequisito(Long idLVRequisito) {
		this.idLVRequisito = idLVRequisito;
	}
	public Long getIdListaVerificacion() {
		return idListaVerificacion;
	}
	public void setIdListaVerificacion(Long idListaVerificacion) {
		this.idListaVerificacion = idListaVerificacion;
	}
	public Long getIdRequisito() {
		return idRequisito;
	}
	public void setIdRequisito(Long idRequisito) {
		this.idRequisito = idRequisito;
	}
	public Long getIdDetalleRequisito() {
		return idDetalleRequisito;
	}
	public void setIdDetalleRequisito(Long idDetalleRequisito) {
		this.idDetalleRequisito = idDetalleRequisito;
	}
	public String getDetalleRequisito() {
		return detalleRequisito;
	}
	public void setDetalleRequisito(String detalleRequisito) {
		this.detalleRequisito = detalleRequisito;
	}
	public String getCuestionarioRequisito() {
		return cuestionarioRequisito;
	}
	public void setCuestionarioRequisito(String cuestionarioRequisito) {
		this.cuestionarioRequisito = cuestionarioRequisito;
	}
	public Long getIdNorma() {
		return idNorma;
	}
	public void setIdNorma(Long idNorma) {
		this.idNorma = idNorma;
	}
	
	public String getDescripcionRequisito() {
		return descripcionRequisito;
	}
	public void setDescripcionRequisito(String descripcionRequisito) {
		this.descripcionRequisito = descripcionRequisito;
	}
	public String getDescripcionNorma() {
		return descripcionNorma;
	}
	public void setDescripcionNorma(String descripcionNorma) {
		this.descripcionNorma = descripcionNorma;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public String getValorCalificacion() {
		return valorCalificacion;
	}
	public void setValorCalificacion(String valorCalificacion) {
		this.valorCalificacion = valorCalificacion;
	}
	public String getDescripcionCalificacion() {
		return descripcionCalificacion;
	}
	public void setDescripcionCalificacion(String descripcionCalificacion) {
		this.descripcionCalificacion = descripcionCalificacion;
	}
	public HallazgoRequisito getHallazgo() {
		return hallazgo;
	}
	public void setHallazgo(HallazgoRequisito hallazgo) {
		this.hallazgo = hallazgo;
	}
	
	
	
	
	
}
