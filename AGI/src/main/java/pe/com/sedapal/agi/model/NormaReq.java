package pe.com.sedapal.agi.model;

import java.util.ArrayList;


public class NormaReq {
	
	private Long id;
	private Long idNorReq;
	private Long idNorma;
	private Long idRequisito;
	private Long idRequisitoPadre;
	private String nombre;
	private String orden;
	private String nivel;
	private String descripcionReq;
	private String detReq;
	private ArrayList<NormaReq> normaReq;
	
	private DatosAuditoria datosAuditoria;
	
	private  ArrayList<RequisitoRelacionado> requisitoRelacionado;
	private  ArrayList<RequisitoDocumento> requisitoDocumento;
	
	private String vdetreq;
	private String vcuesti;
	
	
	
	
	public ArrayList<RequisitoDocumento> getRequisitoDocumento() {
		return requisitoDocumento;
	}

	public void setRequisitoDocumento(ArrayList<RequisitoDocumento> requisitoDocumento) {
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

	private String estado;

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

	public Long getIdRequisito() {
		return idRequisito;
	}

	public void setIdRequisito(Long idRequisito) {
		this.idRequisito = idRequisito;
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

	public ArrayList<RequisitoRelacionado> getRequisitoRelacionado() {
		return requisitoRelacionado;
	}

	public void setRequisitoRelacionado(ArrayList<RequisitoRelacionado> requisitoRelacionado) {
		this.requisitoRelacionado = requisitoRelacionado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	public ArrayList<NormaReq> getNormaReq() {
		return normaReq;
	}

	public void setNormaReq(ArrayList<NormaReq> normaReq) {
		this.normaReq = normaReq;
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
