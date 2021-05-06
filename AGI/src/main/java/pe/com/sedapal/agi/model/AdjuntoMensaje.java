package pe.com.sedapal.agi.model;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class AdjuntoMensaje {
	private Integer idAdjunto;
	private String nombreAdjunto;
	private String nombreRealAdjunto;
	private String extensionAdjunto;
	private String urlAdjunto;
	private Integer sizeAdjunto;
	private byte[] bytesArray;
	private Long n_estado;
    private String a_v_usucre;
    private String a_v_usumod;    
    
    
	public AdjuntoMensaje() {
		super();
	}



	public AdjuntoMensaje(Integer idAdjunto, String nombreAdjunto, String nombreRealAdjunto, String extensionAdjunto, String urlAdjunto,
						  Integer sizeAdjunto, byte[] bytesArray, Long n_estado, String a_v_usucre, String a_v_usumod) {
		super();
		this.idAdjunto = idAdjunto;
		this.nombreAdjunto = nombreAdjunto;
		this.nombreRealAdjunto = nombreRealAdjunto;
		this.extensionAdjunto = extensionAdjunto;
		this.urlAdjunto = urlAdjunto;
		this.sizeAdjunto = sizeAdjunto;
		this.bytesArray = bytesArray;
		this.n_estado = n_estado;
		this.a_v_usucre = a_v_usucre;
		this.a_v_usumod = a_v_usumod;
		
	}
	
	
	public String getNombreAdjunto() {
		return nombreAdjunto;
	}

	public void setNombreAdjunto(String NombreAdjunto) {
		this.nombreAdjunto = NombreAdjunto;
	}

	public String getNombreRealAdjunto() {
		return nombreRealAdjunto;
	}

	public void setNombreRealAdjunto(String NombreRealAdjunto) {
		this.nombreRealAdjunto = NombreRealAdjunto;
	}

	public Integer getIdAdjunto() {
		return idAdjunto;
	}

	public void setIdAdjunto(Integer idAdjunto) {
		this.idAdjunto = idAdjunto;
	}

	public String getExtensionAdjunto() {
		return extensionAdjunto;
	}

	public void setExtensionAdjunto(String extensionAdjunto) {
		this.extensionAdjunto = extensionAdjunto;
	}

	public String getUrlAdjunto() {
		return urlAdjunto;
	}

	public void setUrlAdjunto(String urlAdjunto) {
		this.urlAdjunto = urlAdjunto;
	}

	public Integer getSizeAdjunto() {
		return sizeAdjunto;
	}

	public void setSizeAdjunto(Integer sizeAdjunto) {
		this.sizeAdjunto = sizeAdjunto;
	}

	public byte[] getBytesArray() {
		return bytesArray;
	}

	public void setBytesArray(byte[] bytesArray) {
		this.bytesArray = bytesArray;
	}	

    public Long getN_estado() {
		return n_estado;
	}

	public void setN_estado(Long n_estado) {
		this.n_estado = n_estado;
	}

	public String getA_v_usucre() {
		return a_v_usucre;
	}

	public void setA_v_usucre(String a_v_usucre) {
		this.a_v_usucre = a_v_usucre;
	}

	public String getA_v_usumod() {
		return a_v_usumod;
	}

	public void setA_v_usumod(String a_v_usumod) {
		this.a_v_usumod = a_v_usumod;
	}	
}
