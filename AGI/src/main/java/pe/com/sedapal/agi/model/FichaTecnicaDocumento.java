package pe.com.sedapal.agi.model;

public class FichaTecnicaDocumento {

	  private Long idFichDoc;
	  private Long idFich;
	  private Long idDocu;
	  private String codDocu;
	  private String desDocu;
	  private Long disFichDoc;
	  
	public String getCodDocu() {
		return codDocu;
	}
	public void setCodDocu(String codDocu) {
		this.codDocu = codDocu;
	}
	public String getDesDocu() {
		return desDocu;
	}
	public void setDesDocu(String desDocu) {
		this.desDocu = desDocu;
	}
	public Long getIdFichDoc() {
		return idFichDoc;
	}
	public void setIdFichDoc(Long idFichDoc) {
		this.idFichDoc = idFichDoc;
	}
	public Long getIdFich() {
		return idFich;
	}
	public void setIdFich(Long idFich) {
		this.idFich = idFich;
	}
	public Long getIdDocu() {
		return idDocu;
	}
	public void setIdDocu(Long idDocu) {
		this.idDocu = idDocu;
	}
	public Long getDisFichDoc() {
		return disFichDoc;
	}
	public void setDisFichDoc(Long disFichDoc) {
		this.disFichDoc = disFichDoc;
	}	  	 
	
}
