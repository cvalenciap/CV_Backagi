package pe.com.sedapal.agi.model.request_objects;

import java.util.Date;

public class DocumentoRequest {
	private Long	id;
	private String	codigo;
	private String  titulo;
	private String	descripcion;
	private Long	estado;
	private Long	disponible;
	private Long    idjerarquia;
	private Long    v_descons;
	private Long 	idproc;
	private Long 	idalcasgi;
	private Long 	idgeregnrl;
	private Long 	idtipodocu;
	//private Long	estdoc;
	private String	estdoc;
	private Date  	fecharevdesde;
	private Date	fecharevhasta;
	private Date	fechaaprobdesde;
	private Date	fechaaprobhasta;
	private int		tipodocumento;
	private int  	periodooblig;
	private int		motirevision;
	private int 	numrevi;
	private int		idarea;
	private int		idparticipante;
	private int		idfaseact;
	private int		idfaseestadoact;
	private Long    periodo;
	private Long    revisonobligatoria;
	private String  rutaDocumento;
	private String	listaArea;
	
	private Long idrevison;
	
	/* cguerra */ 
	private Long idtipocopia;
	/* cguerra */
		
	private String	vmodal;
	private String	vmodalava;
	
	

	public String getVmodalava() {
		return vmodalava;
	}
	public void setVmodalava(String vmodalava) {
		this.vmodalava = vmodalava;
	}
	public String getVmodal() {
		return vmodal;
	}
	public void setVmodal(String vmodal) {
		this.vmodal = vmodal;
	}
	public Long getIdrevison() {
		return idrevison;
	}
	public Long getIdtipocopia() {
		return idtipocopia;
	}
	public void setIdtipocopia(Long idtipocopia) {
		this.idtipocopia = idtipocopia;
	}
	public void setIdrevison(Long idrevison) {
		this.idrevison = idrevison;
	}
	public String getListaArea() {
		return listaArea;
	}
	public void setListaArea(String listaArea) {
		this.listaArea = listaArea;
	}
	public Long getRevisonobligatoria() {
		return revisonobligatoria;
	}
	public void setRevisonobligatoria(Long revisonobligatoria) {
		this.revisonobligatoria = revisonobligatoria;
	}
	public String getRutaDocumento() {
		return rutaDocumento;
	}
	public void setRutaDocumento(String rutaDocumento) {
		this.rutaDocumento = rutaDocumento;
	}
	public int getIdparticipante() {
		return idparticipante;
	}
	public Long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}
	public int getIdfaseact() {
		return idfaseact;
	}
	public void setIdfaseact(int idfaseact) {
		this.idfaseact = idfaseact;
	}
	public int getIdfaseestadoact() {
		return idfaseestadoact;
	}
	public void setIdfaseestadoact(int idfaseestadoact) {
		this.idfaseestadoact = idfaseestadoact;
	}
	public void setIdparticipante(int idparticipante) {
		this.idparticipante = idparticipante;
	}
	public int getIdarea() {
		return idarea;
	}
	public void setIdarea(int idarea) {
		this.idarea = idarea;
	}
	/*
	public int getProcesoparametroid() {
		return procesoparametroid;
	}
	public void setProcesoparametroid(int procesoparametroid) {
		this.procesoparametroid = procesoparametroid;
	}
	public int getSgiparametroid() {
		return sgiparametroid;
	}
	public void setSgiparametroid(int sgiparametroid) {
		this.sgiparametroid = sgiparametroid;
	}
	public int getGerenparametroid() {
		return gerenparametroid;
	}
	public void setGerenparametroid(int gerenparametroid) {
		this.gerenparametroid = gerenparametroid;
	}*/
	public int getNumrevi() {
		return numrevi;
	}
	public void setNumrevi(int numrevi) {
		this.numrevi = numrevi;
	}
	public int getMotirevision() {
		return motirevision;
	}
	public void setMotirevision(int motirevision) {
		this.motirevision = motirevision;
	}
	public int getPeriodooblig() {
		return periodooblig;
	}
	public void setPeriodooblig(int periodooblig) {
		this.periodooblig = periodooblig;
	}
	public int getTipodocumento() {
		return tipodocumento;
	}
	public void setTipodocumento(int tipodocumento) {
		this.tipodocumento = tipodocumento;
	}
	public Date getFechaaprobdesde() {
		return fechaaprobdesde;
	}
	public void setFechaaprobdesde(Date fechaaprobdesde) {
		this.fechaaprobdesde = fechaaprobdesde;
	}
	public Date getFechaaprobhasta() {
		return fechaaprobhasta;
	}
	public void setFechaaprobhasta(Date fechaaprobhasta) {
		this.fechaaprobhasta = fechaaprobhasta;
	}
	public Date getFecharevdesde() {
		return fecharevdesde;
	}
	public void setFecharevdesde(Date fecharevdesde) {
		this.fecharevdesde = fecharevdesde;
	}
	public Date getFecharevhasta() {
		return fecharevhasta;
	}
	public void setFecharevhasta(Date fecharevhasta) {
		this.fecharevhasta = fecharevhasta;
	}
	public String getEstdoc() {
		return estdoc;
	}
	public void setEstdoc(String estdoc) {
		this.estdoc = estdoc;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Long getIdtipodocu() {
		return idtipodocu;
	}
	public void setIdtipodocu(Long idtipodocu) {
		this.idtipodocu = idtipodocu;
	}
	public Long getIdproc() {
		return idproc;
	}
	public void setIdproc(Long idproc) {
		this.idproc = idproc;
	}
	public Long getIdalcasgi() {
		return idalcasgi;
	}
	public void setIdalcasgi(Long idalcasgi) {
		this.idalcasgi = idalcasgi;
	}
	public Long getIdgeregnrl() {
		return idgeregnrl;
	}
	public void setIdgeregnrl(Long idgeregnrl) {
		this.idgeregnrl = idgeregnrl;
	}
	public Long getV_descons() {
		return v_descons;
	}
	public void setV_descons(Long v_descons) {
		this.v_descons = v_descons;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}
	public Long getDisponible() {
		return disponible;
	}
	public void setDisponible(Long disponible) {
		this.disponible = disponible;
	}
	public Long getIdjerarquia() {
		return idjerarquia;
	}
	public void setIdjerarquia(Long idjerarquia) {
		this.idjerarquia = idjerarquia;
	}

}