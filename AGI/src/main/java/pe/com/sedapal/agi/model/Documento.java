package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Documento {
	private  Long 				id;                                  
	private  Documento			padre;                             
	private  String				codigo;                            
	private  String				descripcion;                      
	private  Long			    version;                           
	private  Constante          estado;                            
	private  Long			    retencionRevision;                  
	//Arbol Id Jerarquia
	private  Long 				alcanceSGI;;
	private  Jerarquia          jalcanceSGI;
	private  Long 				proceso;
	private  Jerarquia          jproceso;
	//private  Constante          proceso;                           
	//private  Constante          alcanceSGI; 
	private  Long 				gerencia;
	private  Jerarquia          jgerencia;
	private  Constante          ctipoDocumento;
	private  Long               tipoDocumento; 	
    private  Long			    periodo;                           
    private  Codigo		        codigoAnterior;                    
    private  String		        justificacion;                     
    private  Constante          motivoRevision;                    
    private  Colaborador		emisor;
    private  Constante          fase;
    private  String		        nombreArchivo;                     
    private  String		        rutaArchivo;                       
    private  Revision	        revision;                          
    private  Long			    disponible;		                     
    private  Jerarquia          nodo;                              
    private  String		        indicadorAvance;
    private  String		        estadoDercarga;
    private  String		        raiz;
    private  Copia			    copia;
    private  List<Codigo>		listaCodigo;
    private  List<Colaborador>	participanteElaboracion;
    private  List<Colaborador>	participanteConsenso;
    private  List<Colaborador>	participanteAprobacion;
    private  List<Colaborador>	participanteHomologacion;
    private  Flujo				bitacora;
    private  Flujo				faseElaboracion;
    private  Flujo				faseConsenso;
    private  Flujo				faseAprobacion;
    private  Flujo				faseHomologacion;
    private  List<Documento>	listaComplementario;
    private List<Equipo>		listaEquipo;
    private List<Revision>		listaRevision;
    private Long			    idHistorial;
    private Constante		    tipoComplementario;
    private EstadoConstante     estadoDisponible;
    private Long                revisonobligatoria;
    String responsable;
    String idrevision;
    private  Colaborador		coordinador;
	private Constante           estadoFaseActual;
    private String rutaDocumento;
    private Critica critica;
    private Long idcolaboracioncreaciondoc;
    private Date fechaCreaDoc;

	private Long indicadorDigital;
    private String equipo;    
    private String funcion;
    private String idFase;
    private String idRuta;
    private String plazo;
    private String prioridad;   
    private  Long idrevi;
   
    private String indAprobacionSoli;
    private String indicadorSolicitudRevision;
    private String indicadorFase;
    private SolicitudCopia  criticaporDocumento;
	private String indicadorAprobado;
    private List<Colaborador> listaDestinatario;
    private List<Colaborador> listaParticipante;     
    private Long idrevisionR;
    private String vcoddocu;
    private String vdesdocu;
    private Long idjerageneral;
    private Long idjeralc;
    private Long iproce;
    private Long nrevision;
    private Long nestrevision;
    private Long nidocu;
    private Long nidrevi;
    private Date fechaApro;    
    private Documento   RevisionDocumento;
    private String rutagerencia;
    private String rutalcance;
    private String rutaproceso;
    private String rutacopiacontrolada;
			 String rutaDocumentoOriginal;	
			 String rutaDocumentoCopiaNoCont;
			 String rutaDocumentoCopiaCont;
			 String rutaDocumentoCopiaObso;
	 private String rutaDocumentoOffice;
 	 private String v_id_googleDrive;
 
	 private Long idRevision;
	 private Long idUsuaEsta;
	 private Long codigoGerencia; 
	 private Long codigoTipoDocumento; 
	 private String codigoAntiguo;   
 
	


	public String getRutaDocumentoOffice() {
		return rutaDocumentoOffice;
	}
	public void setRutaDocumentoOffice(String rutaDocumentoOffice) {
		this.rutaDocumentoOffice = rutaDocumentoOffice;
	}
	public String getV_id_googleDrive() {
	return v_id_googleDrive;
}
public void setV_id_googleDrive(String v_id_googleDrive) {
	this.v_id_googleDrive = v_id_googleDrive;
}
	public Long getIdUsuaEsta() {
		return idUsuaEsta;
	}
	public void setIdUsuaEsta(Long idUsuaEsta) {
		this.idUsuaEsta = idUsuaEsta;
	}
	public String getRutacopiacontrolada() {
		return rutacopiacontrolada;
	}
	public String getRutaDocumentoOriginal() {
		return rutaDocumentoOriginal;
	}
	public void setRutaDocumentoOriginal(String rutaDocumentoOriginal) {
		this.rutaDocumentoOriginal = rutaDocumentoOriginal;
	}
	public String getRutaDocumentoCopiaNoCont() {
		return rutaDocumentoCopiaNoCont;
	}
	public void setRutaDocumentoCopiaNoCont(String rutaDocumentoCopiaNoCont) {
		this.rutaDocumentoCopiaNoCont = rutaDocumentoCopiaNoCont;
	}
	public String getRutaDocumentoCopiaCont() {
		return rutaDocumentoCopiaCont;
	}
	public void setRutaDocumentoCopiaCont(String rutaDocumentoCopiaCont) {
		this.rutaDocumentoCopiaCont = rutaDocumentoCopiaCont;
	}
	public String getRutaDocumentoCopiaObso() {
		return rutaDocumentoCopiaObso;
	}
	public void setRutaDocumentoCopiaObso(String rutaDocumentoCopiaObso) {
		this.rutaDocumentoCopiaObso = rutaDocumentoCopiaObso;
	}
	public void setRutacopiacontrolada(String rutacopiacontrolada) {
		this.rutacopiacontrolada = rutacopiacontrolada;
	}
	public String getRutagerencia() {
		return rutagerencia;
	}	
	public void setRutagerencia(String rutagerencia) {
		this.rutagerencia = rutagerencia;
	}
	public String getRutalcance() {
		return rutalcance;
	}
	public void setRutalcance(String rutalcance) {
		this.rutalcance = rutalcance;
	}
	public String getRutaproceso() {
		return rutaproceso;
	}
	public void setRutaproceso(String rutaproceso) {
		this.rutaproceso = rutaproceso;
	}
	public Documento getRevisionDocumento() {
		return RevisionDocumento;
	}
	public Date getFechaApro() {
		return fechaApro;
	}
	public void setFechaApro(Date fechaApro) {
		this.fechaApro = fechaApro;
	}
	public void setRevisionDocumento(Documento revisionDocumento) {
		RevisionDocumento = revisionDocumento;
	}
	public String getVcoddocu() {
		return vcoddocu;
	}
	public void setVcoddocu(String vcoddocu) {
		this.vcoddocu = vcoddocu;
	}
	public String getVdesdocu() {
		return vdesdocu;
	}
	public void setVdesdocu(String vdesdocu) {
		this.vdesdocu = vdesdocu;
	}
	public Long getIdjerageneral() {
		return idjerageneral;
	}
	public void setIdjerageneral(Long idjerageneral) {
		this.idjerageneral = idjerageneral;
	}
	public Long getIdjeralc() {
		return idjeralc;
	}
	public void setIdjeralc(Long idjeralc) {
		this.idjeralc = idjeralc;
	}
	public Long getIproce() {
		return iproce;
	}
	public void setIproce(Long iproce) {
		this.iproce = iproce;
	}
	public Long getNrevision() {
		return nrevision;
	}
	public void setNrevision(Long nrevision) {
		this.nrevision = nrevision;
	}
	public Long getNestrevision() {
		return nestrevision;
	}
	public void setNestrevision(Long nestrevision) {
		this.nestrevision = nestrevision;
	}
	public Long getNidocu() {
		return nidocu;
	}
	public void setNidocu(Long nidocu) {
		this.nidocu = nidocu;
	}
	public Long getNidrevi() {
		return nidrevi;
	}
	public void setNidrevi(Long nidrevi) {
		this.nidrevi = nidrevi;
	}
	public Long getIdrevisionR() {
		return idrevisionR;
	}
	public void setIdrevisionR(Long idrevisionR) {
		this.idrevisionR = idrevisionR;
	}
	public SolicitudCopia getCriticaporDocumento() {
		return criticaporDocumento;
	}
	public void setCriticaporDocumento(SolicitudCopia criticaporDocumento) {
		this.criticaporDocumento = criticaporDocumento;
	}
	public List<Colaborador> getListaDestinatario() {
		return listaDestinatario;
	}
	public void setListaDestinatario(List<Colaborador> listaDestinatario) {
		this.listaDestinatario = listaDestinatario;
	}
	public List<Colaborador> getListaParticipante() {
		return listaParticipante;
	}
	public void setListaParticipante(List<Colaborador> listaParticipante) {
		this.listaParticipante = listaParticipante;
	}
	public Flujo getFaseElaboracion() {
		return faseElaboracion;
	}
	public void setFaseElaboracion(Flujo faseElaboracion) {
		this.faseElaboracion = faseElaboracion;
	}
	public Flujo getFaseConsenso() {
		return faseConsenso;
	}
	public void setFaseConsenso(Flujo faseConsenso) {
		this.faseConsenso = faseConsenso;
	}
	public Flujo getFaseAprobacion() {
		return faseAprobacion;
	}
	public void setFaseAprobacion(Flujo faseAprobacion) {
		this.faseAprobacion = faseAprobacion;
	}
	public Flujo getFaseHomologacion() {
		return faseHomologacion;
	}
	public void setFaseHomologacion(Flujo faseHomologacion) {
		this.faseHomologacion = faseHomologacion;
	}
	public String getIndicadorFase() {
		return indicadorFase;
	}
	public void setIndicadorFase(String indicadorFase) {
		this.indicadorFase = indicadorFase;
	}
	public String getIndicadorSolicitudRevision() {
		return indicadorSolicitudRevision;
	}
	public void setIndicadorSolicitudRevision(String indicadorSolicitudRevision) {
		this.indicadorSolicitudRevision = indicadorSolicitudRevision;
	}
	public String getIndAprobacionSoli() {
		return indAprobacionSoli;
	}
	public void setIndAprobacionSoli(String indAprobacionSoli) {
		this.indAprobacionSoli = indAprobacionSoli;
	}
    public Long getIndicadorDigital() {
		return indicadorDigital;
	}
	public void setIndicadorDigital(Long indicadorDigital) {
		this.indicadorDigital = indicadorDigital;
	}
	public Long getIdcolaboracioncreaciondoc() {
		return idcolaboracioncreaciondoc;
	}
	public void setIdcolaboracioncreaciondoc(Long idcolaboracioncreaciondoc) {
		this.idcolaboracioncreaciondoc = idcolaboracioncreaciondoc;
	}
	public String getIndicadorAprobado() {
		return indicadorAprobado;
	}
	public void setIndicadorAprobado(String indicadorAprobado) {
		this.indicadorAprobado = indicadorAprobado;
	}
	public Date getFechaCreaDoc() {
		return fechaCreaDoc;
	}
	public void setFechaCreaDoc(Date fechaCreaDoc) {
		this.fechaCreaDoc = fechaCreaDoc;
	}
	public Long getIdrevi() {
		return idrevi;
	}
	public void setIdrevi(Long idrevi) {
		this.idrevi = idrevi;
	}
	public Long getRevisonobligatoria() {
		return revisonobligatoria;
	}
	public String getRutaDocumento() {
		return rutaDocumento;
	}
	public void setRutaDocumento(String rutaDocumento) {
		this.rutaDocumento = rutaDocumento;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	public String getFuncion() {
		return funcion;
	}
	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
	public String getIdFase() {
		return idFase;
	}
	public void setIdFase(String idFase) {
		this.idFase = idFase;
	}
	public String getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(String idRuta) {
		this.idRuta = idRuta;
	}
	public String getPlazo() {
		return plazo;
	}
	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}
	public String getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
	public void setRevisonobligatoria(Long revisonobligatoria) {
		this.revisonobligatoria = revisonobligatoria;
	}
	public Constante getCtipoDocumento() {
		return ctipoDocumento;
	}
	public void setCtipoDocumento(Constante ctipoDocumento) {
		this.ctipoDocumento = ctipoDocumento;
	}
	public Long getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(Long tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public Long getAlcanceSGI() {
		return alcanceSGI;
	}
	public void setAlcanceSGI(Long alcanceSGI) {
		this.alcanceSGI = alcanceSGI;
	}
	public Long getProceso() {
		return proceso;
	}
	public void setProceso(Long proceso) {
		this.proceso = proceso;
	}
	public Jerarquia getJalcanceSGI() {
		return jalcanceSGI;
	}
	public void setJalcanceSGI(Jerarquia jalcanceSGI) {
		this.jalcanceSGI = jalcanceSGI;
	}
	public Jerarquia getJproceso() {
		return jproceso;
	}
	public void setJproceso(Jerarquia jproceso) {
		this.jproceso = jproceso;
	}
	public Long getGerencia() {
		return gerencia;
	}
	public void setGerencia(Long gerencia) {
		this.gerencia = gerencia;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Documento getPadre() {
		return padre;
	}
	public void setPadre(Documento padre) {
		this.padre = padre;
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
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public Constante getEstado() {
		return estado;
	}
	public void setEstado(Constante estado) {
		this.estado = estado;
	}
	public Long getRetencionRevision() {
		return retencionRevision;
	}
	public void setRetencionRevision(Long retencionRevision) {
		this.retencionRevision = retencionRevision;
	}
	
	/*
	public Constante getProceso() {
		return proceso;
	}
	public void setProceso(Constante proceso) {
		this.proceso = proceso;
	}
	public Constante getAlcanceSGI() {
		return alcanceSGI;
	}
	public void setAlcanceSGI(Constante alcanceSGI) {
		this.alcanceSGI = alcanceSGI;
	}*/
	
	
	public Jerarquia getJgerencia() {
		return jgerencia;
	}
	public void setJgerencia(Jerarquia jgerencia) {
		this.jgerencia = jgerencia;
	}/*	
	public Constante getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(Constante tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}*/
	public Long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}
	public Codigo getCodigoAnterior() {
		return codigoAnterior;
	}
	public void setCodigoAnterior(Codigo codigoAnterior) {
		this.codigoAnterior = codigoAnterior;
	}
	public String getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}
	public Constante getMotivoRevision() {
		return motivoRevision;
	}
	public void setMotivoRevision(Constante motivoRevision) {
		this.motivoRevision = motivoRevision;
	}
	public Colaborador getEmisor() {
		return emisor;
	}
	public void setEmisor(Colaborador emisor) {
		this.emisor = emisor;
	}
	public Constante getFase() {
		return fase;
	}
	public void setFase(Constante fase) {
		this.fase = fase;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getRutaArchivo() {
		return rutaArchivo;
	}
	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}
	public Revision getRevision() {
		return revision;
	}
	public void setRevision(Revision revision) {
		this.revision = revision;
	}
	public Long getDisponible() {
		return disponible;
	}
	public void setDisponible(Long disponible) {
		this.disponible = disponible;
	}
	public Jerarquia getNodo() {
		return nodo;
	}
	public void setNodo(Jerarquia nodo) {
		this.nodo = nodo;
	}
	public String getIndicadorAvance() {
		return indicadorAvance;
	}
	public void setIndicadorAvance(String indicadorAvance) {
		this.indicadorAvance = indicadorAvance;
	}
	public String getEstadoDercarga() {
		return estadoDercarga;
	}
	public void setEstadoDercarga(String estadoDercarga) {
		this.estadoDercarga = estadoDercarga;
	}
	public String getRaiz() {
		return raiz;
	}
	public void setRaiz(String raiz) {
		this.raiz = raiz;
	}
	public Copia getCopia() {
		return copia;
	}
	public void setCopia(Copia copia) {
		this.copia = copia;
	}
	public List<Codigo> getListaCodigo() {
		return listaCodigo;
	}
	public void setListaCodigo(List<Codigo> listaCodigo) {
		this.listaCodigo = listaCodigo;
	}
	public List<Colaborador> getParticipanteElaboracion() {
		return participanteElaboracion;
	}
	public void setParticipanteElaboracion(List<Colaborador> participanteElaboracion) {
		this.participanteElaboracion = participanteElaboracion;
	}
	public List<Colaborador> getParticipanteConsenso() {
		return participanteConsenso;
	}
	public void setParticipanteConsenso(List<Colaborador> participanteConsenso) {
		this.participanteConsenso = participanteConsenso;
	}
	public List<Colaborador> getParticipanteAprobacion() {
		return participanteAprobacion;
	}
	public void setParticipanteAprobacion(List<Colaborador> participanteAprobacion) {
		this.participanteAprobacion = participanteAprobacion;
	}
	public List<Colaborador> getParticipanteHomologacion() {
		return participanteHomologacion;
	}
	public void setParticipanteHomologacion(List<Colaborador> participanteHomologacion) {
		this.participanteHomologacion = participanteHomologacion;
	}
	public Flujo getBitacora() {
		return bitacora;
	}
	public void setBitacora(Flujo bitacora) {
		this.bitacora = bitacora;
	}
	public List<Documento> getListaComplementario() {
		return listaComplementario;
	}
	public void setListaComplementario(List<Documento> listaComplementario) {
		this.listaComplementario = listaComplementario;
	}
	public List<Equipo> getListaEquipo() {
		return listaEquipo;
	}
	public void setListaEquipo(List<Equipo> listaEquipo) {
		this.listaEquipo = listaEquipo;
	}
	public List<Revision> getListaRevision() {
		return listaRevision;
	}
	public void setListaRevision(List<Revision> listaRevision) {
		this.listaRevision = listaRevision;
	}
	public Long getIdHistorial() {
		return idHistorial;
	}
	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}
	public Constante getTipoComplementario() {
		return tipoComplementario;
	}
	public void setTipoComplementario(Constante tipoComplementario) {
		this.tipoComplementario = tipoComplementario;
	}
	public EstadoConstante getEstadoDisponible() {
		return estadoDisponible;
	}
	public void setEstadoDisponible(EstadoConstante estadoDisponible) {
		this.estadoDisponible = estadoDisponible;
	}

	public Documento() {
		super();
	}
	
	public Documento(Long id, Long disponible) {
		super();
		this.id			= id;
		this.disponible	= disponible;
	}
	@Override
	public String toString() {
		return "Documento [id=" + id + ", padre=" + padre + ", codigo=" + codigo + ", descripcion=" + descripcion
				+ ", version=" + version + ", estado=" + estado + ", retencionRevision=" + retencionRevision
				+ ", alcanceSGI=" + alcanceSGI + ", jalcanceSGI=" + jalcanceSGI + ", proceso=" + proceso + ", jproceso="
				+ jproceso + ", gerencia=" + gerencia + ", jgerencia=" + jgerencia + ", ctipoDocumento="
				+ ctipoDocumento + ", tipoDocumento=" + tipoDocumento + ", periodo=" + periodo + ", codigoAnterior="
				+ codigoAnterior + ", justificacion=" + justificacion + ", motivoRevision=" + motivoRevision
				+ ", emisor=" + emisor + ", fase=" + fase + ", nombreArchivo=" + nombreArchivo + ", rutaArchivo="
				+ rutaArchivo + ", revision=" + revision + ", disponible=" + disponible + ", nodo=" + nodo
				+ ", indicadorAvance=" + indicadorAvance + ", estadoDercarga=" + estadoDercarga + ", raiz=" + raiz
				+ ", copia=" + copia + ", listaCodigo=" + listaCodigo + ", participanteElaboracion="
				+ participanteElaboracion + ", participanteConsenso=" + participanteConsenso
				+ ", participanteAprobacion=" + participanteAprobacion + ", participanteHomologacion="
				+ participanteHomologacion + ", listaComplementario=" + listaComplementario + ", listaEquipo="
				+ listaEquipo + ", listaRevision=" + listaRevision + ", idHistorial=" + idHistorial
				+ ", tipoComplementario=" + tipoComplementario + ", estadoDisponible=" + estadoDisponible + "]";
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getIdrevision() {
		return idrevision;
	}
	public void setIdrevision(String idrevision) {
		this.idrevision = idrevision;
	}
	public Colaborador getCoordinador() {
		return coordinador;
	}
	public void setCoordinador(Colaborador coordinador) {
		this.coordinador = coordinador;
	}
    public Constante getEstadoFaseActual() {
		return estadoFaseActual;
	}
	public void setEstadoFaseActual(Constante estadoFaseActual) {
		this.estadoFaseActual = estadoFaseActual;
	}
	public Critica getCritica() {
		return critica;
	}
	public void setCritica(Critica critica) {
		this.critica = critica;
	}
	public Long getIdRevision() {
		return idRevision;
	}
	public void setIdRevision(Long idRevision) {
		this.idRevision = idRevision;
	}
	public Long getCodigoGerencia() {
		return codigoGerencia;
	}
	public void setCodigoGerencia(Long codigoGerencia) {
		this.codigoGerencia = codigoGerencia;
	}
	public Long getCodigoTipoDocumento() {
		return codigoTipoDocumento;
	}
	public void setCodigoTipoDocumento(Long codigoTipoDocumento) {
		this.codigoTipoDocumento = codigoTipoDocumento;
	}
	public String getCodigoAntiguo() {
		return codigoAntiguo;
	}
	public void setCodigoAntiguo(String codigoAntiguo) {
		this.codigoAntiguo = codigoAntiguo;
	}

}