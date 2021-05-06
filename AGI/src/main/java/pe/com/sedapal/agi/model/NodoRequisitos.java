package pe.com.sedapal.agi.model;

import java.util.List;

public class NodoRequisitos {
	private Long id;
	private String nombre;
	private List<NodoRequisitos> children;
	
	///Se agrega por mantenimiento de Norma
	private Long idRequisito;
	private Long idNorReq;
	private Long idNorma;
	private Long idRequisitoPadre;
	private String orden;
	private String nivel;
	private String descripcionReq;
	private String detReq;
	private int estado;
	private DatosAuditoria datosAuditoria;
	private List<RequisitoRelacionado> requisitoRelacionado;
	private List<RequisitoDocumento> requisitoDocumento;
	
	private String vdetreq;
	private String vcuesti;
	
	public List<RequisitoDocumento> getRequisitoDocumento() {
		return requisitoDocumento;
	}
	public void setRequisitoDocumento(List<RequisitoDocumento> requisitoDocumento) {
		this.requisitoDocumento = requisitoDocumento;
	}
	public String getVdetreq() {
		return vdetreq;
	}
	public void setVdetreq(String vdetreq) {
		this.vdetreq = vdetreq;
	}
	public String getVcuesti() {
		return vcuesti;
	}
	public void setVcuesti(String vcuesti) {
		this.vcuesti = vcuesti;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<NodoRequisitos> getChildren() {
		return children;
	}
	public void setChildren(List<NodoRequisitos> children) {
		this.children = children;
	}
	public List<RequisitoRelacionado> getRequisitoRelacionado() {
		return requisitoRelacionado;
	}
	public void setRequisitoRelacionado(List<RequisitoRelacionado> requisitoRelacionado) {
		this.requisitoRelacionado = requisitoRelacionado;
	}
	public Long getIdRequisito() {
		return idRequisito;
	}
	public void setIdRequisito(Long idRequisito) {
		this.idRequisito = idRequisito;
	}
	public Long getIdNorReq() {
		return idNorReq;
	}
	public void setIdNorReq(Long idNorReq) {
		this.idNorReq = idNorReq;
	}
	public Long getIdNorma() {
		return idNorma;
	}
	public void setIdNorma(Long idNorma) {
		this.idNorma = idNorma;
	}
	public Long getIdRequisitoPadre() {
		return idRequisitoPadre;
	}
	public void setIdRequisitoPadre(Long idRequisitoPadre) {
		this.idRequisitoPadre = idRequisitoPadre;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getDescripcionReq() {
		return descripcionReq;
	}
	public void setDescripcionReq(String descripcionReq) {
		this.descripcionReq = descripcionReq;
	}
	public String getDetReq() {
		return detReq;
	}
	public void setDetReq(String detReq) {
		this.detReq = detReq;
	}
	public DatosAuditoria getDatosAuditoria() {
		return datosAuditoria;
	}
	public void setDatosAuditoria(DatosAuditoria datosAuditoria) {
		this.datosAuditoria = datosAuditoria;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	
}
