package pe.com.sedapal.agi.model.request_objects;

import java.util.ArrayList;

public class NormaRequest {
	
	private String tipo;
	private	String descripcionNorma;
	
	//para la lista
	
	private Long idNorma;
	private Long idRequisito;
	private int orden;
	private int nivel;
	private String requisito;
	private String idRequisitoPadre;
	private String idNormaReq;
	private ArrayList<NormaRequest> normaRelaciona;
	
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
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public String getRequisito() {
		return requisito;
	}
	public void setRequisito(String requisito) {
		this.requisito = requisito;
	}
	public String getIdRequisitoPadre() {
		return idRequisitoPadre;
	}
	public void setIdRequisitoPadre(String idRequisitoPadre) {
		this.idRequisitoPadre = idRequisitoPadre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getIdNormaReq() {
		return idNormaReq;
	}
	public void setIdNormaReq(String idNormaReq) {
		this.idNormaReq = idNormaReq;
	}
	public ArrayList<NormaRequest> getNormaRelaciona() {
		return normaRelaciona;
	}
	public void setNormaRelaciona(ArrayList<NormaRequest> normaRelaciona) {
		this.normaRelaciona = normaRelaciona;
	}
	public String getDescripcionNorma() {
		return descripcionNorma;
	}
	public void setDescripcionNorma(String descripcionNorma) {
		this.descripcionNorma = descripcionNorma;
	}
	
	
	
	
	
	
	
}
