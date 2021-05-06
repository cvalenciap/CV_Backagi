package pe.com.sedapal.agi.model;

import java.math.BigDecimal;
import java.util.Date;

public class Revision {

	Long id;
	String codigo;
	String titulo;
	Long numero;
	Date fecha;
	Long idHistorial;
	Constante estado;
	Constante estadofaseact;
	BigDecimal disponible;
	Documento documento;
	Colaborador colaborador;
	//AdjuntoMensaje archivo;
	String    archivo;
	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	Equipo     equipo;
	String descripcion;
	String motivoRevision;
	Date fechaAprobacion;
	Date fechacancelacion;
	Long usuarioDeAprobacion;
	Date fecPlazoAprobacion;
	String motivoRechazoRev;
	//Long idColaborador;
	Long idmotirevi;
    String ruta;
    Long diferenciaPlazo;
    Date fechaPlazoAprob;
    Long idEsta;
    Long idEstaFaseAct;
    //Long plazoDiferencia;
    Date plazoParticipante;
    String idDocGoogleDrive;
    //Long idDocumento;
    
    /* Rutas de documentos*/
   public String rutaDocumentoOriginal;
   public String rutaDocumentoGoogle;
    String rutaDocumentoCopiaNoCont;
    String rutaDocumentoCopiaCont;
    String rutaDocumentoCopiaObso;
    /* Rutas de documentos*/
    Long iteracion;

    Date fechaRegistroSOlicit;
    Long usuarioRevision;

	String rutaDocumt;
	String rutaDocumtNueva;
    
    String	codDocu;
    String	desDocu;
    Long	numRevi;
    Date	fecRevi;
    Long	antiguedadDocu;
    Long	periodoOblig;
    String	responsableEquipo;
    Long	idResponsableEquipo;
    Long	idDocu;
    Long	idTipDoc;    
    Long	gerenparametroid;
    String 	idCorrelativo;
    Boolean	seleccionado;
    Long	idProgramacion;
    Long	idProg;
    Long	idProgExistente;
    Date 	nombreEquipo;
    Long	idEstadoProg;
    String 	fechDistribucion;
    String	descequipo;
    String	idequipo;
    String	desestadoejec;
    Long	idestadoejec;
    String	desestado;
    String	tipobusq;
    Long	estados;
    Long	anio;
    String	idListaVerificacion;
    String	anioantiguedad;
    String	gerenparametrodesc;
    String	tipodocumento;
    String	rutacopianocontrolada;
    String	nestcopi;
    String	observa;
    String	numtipoestasoli;
    String	nmotivo;
    String	resumenCritica;
    String	indicadorestado;
    String	motivo;
    Long	revisionActual;
    String	desta;
    String	proceso;
    String	alcance;
    String	sustento;
    Long	gerencia;
    Long	numerosolicitud;
    String	nidrevision;
    String	tabName;
    Long	codFichaLogueado;
    boolean revisHist;
    
    String	estProgr;
    Date fechaProgramacion;
    Date fechaDistribucion;
    Date fechaEjecucion;
    Long idEquipoProgramacion;
    String desestadoProg;
    String equipoProgramacion;
    String estadoFase;
    
    
    
    public Date getFechacancelacion() {
		return fechacancelacion;
	}

	public void setFechacancelacion(Date fechacancelacion) {
		this.fechacancelacion = fechacancelacion;
	}

	public String getRutaDocumtNueva() {
		return rutaDocumtNueva;
	}

	public void setRutaDocumtNueva(String rutaDocumtNueva) {
		this.rutaDocumtNueva = rutaDocumtNueva;
	}

	public String getEstadoFase() {
		return estadoFase;
	}

	public void setEstadoFase(String estadoFase) {
		this.estadoFase = estadoFase;
	}

	public String getEstProgr() {
		return estProgr;
	}

	public void setEstProgr(String estProg) {
		this.estProgr = estProg;
	}

	public Date getFechaProgramacion() {
		return fechaProgramacion;
	}

	public void setFechaProgramacion(Date fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}

	public Date getFechaDistribucion() {
		return fechaDistribucion;
	}

	public void setFechaDistribucion(Date fechaDistribucion) {
		this.fechaDistribucion = fechaDistribucion;
	}

	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public Long getIdEquipoProgramacion() {
		return idEquipoProgramacion;
	}

	public void setIdEquipoProgramacion(Long idEquipoProgramacion) {
		this.idEquipoProgramacion = idEquipoProgramacion;
	}

	public String getDesestadoProg() {
		return desestadoProg;
	}

	public void setDesestadoProg(String desestadoProg) {
		this.desestadoProg = desestadoProg;
	}

	public String getEquipoProgramacion() {
		return equipoProgramacion;
	}

	public void setEquipoProgramacion(String equipoProgramacion) {
		this.equipoProgramacion = equipoProgramacion;
	}

	public boolean isRevisHist() {
		return revisHist;
	}

	public void setRevisHist(boolean revisHist) {
		this.revisHist = revisHist;
	}

	public Long getCodFichaLogueado() {
		return codFichaLogueado;
	}

	public void setCodFichaLogueado(Long codFichaLogueado) {
		this.codFichaLogueado = codFichaLogueado;
	}

	//cguerra	
    private String tipocopia;
    private String motivoR;
    private String estadoSoli;
    private Date fechaSolicitud;
    private String solicitantSolicitud;
    private String destinatarioSolicitud;
    private Long Idususoli;
    private Long nrum;
    private Long numerosol;
    private Long numerotipocopia;
    private Long numeromotivo;
    private String sustentosolicitud;
    private String susteso;
    private Long n_motivo;
    private Long nidrevi;
    private String nomapellidoparterDestina;
    //cguerra
    Long	idTrimestre;
    Long	correlativo;
    Long	cantDocu;
    Long	primerTrim;
    Long	segundoTrim;
    Long	tercerTrim;
    Long	cuartoTrim;
    String	responsableEquipoSelecc;
    Long	idResponsableEquipoSelecc;
    Long	totalEquipos;
    private Long idUsuarioAprobDocu;
    private Date fechaAprobDocu;
    private Date fechaAprobacionDocumento;
    private String usuarioAprobacionDocumento;
    private Date fechaAprobacionAnterior;
    private Long numeroAnterior;
	private String rutaDocumentoOffice;
	
	public String getRutaDocumentoOffice() {
		return rutaDocumentoOffice;
	}

	public void setRutaDocumentoOffice(String rutaDocumentoOffice) {
		this.rutaDocumentoOffice = rutaDocumentoOffice;
	}

	public Date getFechaAprobacionAnterior() {
		return fechaAprobacionAnterior;
	}
	public void setFechaAprobacionAnterior(Date fechaAprobacionAnterior) {
		this.fechaAprobacionAnterior = fechaAprobacionAnterior;
	}
	public Long getNumeroAnterior() {
		return numeroAnterior;
	}
	public void setNumeroAnterior(Long numeroAnterior) {
		this.numeroAnterior = numeroAnterior;
	}
    Date	fechaActual;
    String	estEjec;
    Long	idestejec;
	public Long getIdUsuarioAprobDocu() {
		return idUsuarioAprobDocu;
	}
	public void setIdUsuarioAprobDocu(Long idUsuarioAprobDocu) {
		this.idUsuarioAprobDocu = idUsuarioAprobDocu;
	}
	public Date getFechaAprobDocu() {
		return fechaAprobDocu;
	}
	public void setFechaAprobDocu(Date fechaAprobDocu) {
		this.fechaAprobDocu = fechaAprobDocu;
	}
	public Date getFechaAprobacionDocumento() {
		return fechaAprobacionDocumento;
	}
	public void setFechaAprobacionDocumento(Date fechaAprobacionDocumento) {
		this.fechaAprobacionDocumento = fechaAprobacionDocumento;
	}
	public String getUsuarioAprobacionDocumento() {
		return usuarioAprobacionDocumento;
	}
	public void setUsuarioAprobacionDocumento(String usuarioAprobacionDocumento) {
		this.usuarioAprobacionDocumento = usuarioAprobacionDocumento;
	}
	
    public Long getIdestejec() {
		return idestejec;
	}

	public void setIdestejec(Long idestejec) {
		this.idestejec = idestejec;
	}
	public String getRutaDocumt() {
		return rutaDocumt;
	}

	public String getNomapellidoparterDestina() {
		return nomapellidoparterDestina;
	}

	public void setNomapellidoparterDestina(String nomapellidoparterDestina) {
		this.nomapellidoparterDestina = nomapellidoparterDestina;
	}

	public Long getNidrevi() {
		return nidrevi;
	}

	public void setNidrevi(Long nidrevi) {
		this.nidrevi = nidrevi;
	}

	public void setRutaDocumt(String rutaDocumt) {
		this.rutaDocumt = rutaDocumt;
	}
	
	public String getEstEjec() {
		return estEjec;
	}

	public void setEstEjec(String estEjec) {
		this.estEjec = estEjec;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public Long getTotalEquipos() {
		return totalEquipos;
	}

	public void setTotalEquipos(Long totalEquipos) {
		this.totalEquipos = totalEquipos;
	}

	public Long getIdResponsableEquipoSelecc() {
		return idResponsableEquipoSelecc;
	}

	public void setIdResponsableEquipoSelecc(Long idResponsableEquipoSelecc) {
		this.idResponsableEquipoSelecc = idResponsableEquipoSelecc;
	}

	public String getResponsableEquipoSelecc() {
		return responsableEquipoSelecc;
	}

	public void setResponsableEquipoSelecc(String responsableEquipoSelecc) {
		this.responsableEquipoSelecc = responsableEquipoSelecc;
	}

	public Long getPrimerTrim() {
		return primerTrim;
	}

	public void setPrimerTrim(Long primerTrim) {
		this.primerTrim = primerTrim;
	}

	public Long getSegundoTrim() {
		return segundoTrim;
	}

	public void setSegundoTrim(Long segundoTrim) {
		this.segundoTrim = segundoTrim;
	}

	public Long getTercerTrim() {
		return tercerTrim;
	}

	public void setTercerTrim(Long tercerTrim) {
		this.tercerTrim = tercerTrim;
	}

	public Long getCuartoTrim() {
		return cuartoTrim;
	}

	public void setCuartoTrim(Long cuartoTrim) {
		this.cuartoTrim = cuartoTrim;
	}

	public Long getCantDocu() {
		return cantDocu;
	}

	public void setCantDocu(Long cantDocu) {
		this.cantDocu = cantDocu;
	}

	public Long getIdTrimestre() {
		return idTrimestre;
	}

	public void setIdTrimestre(Long idTrimestre) {
		this.idTrimestre = idTrimestre;
	}

	public Long getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(Long correlativo) {
		this.correlativo = correlativo;
	}

	public Long getIdProgExistente() {
		return idProgExistente;
	}

	public void setIdProgExistente(Long idProgExistente) {
		this.idProgExistente = idProgExistente;
	}

	public Long getIdProg() {
		return idProg;
	}

	public void setIdProg(Long idProg) {
		this.idProg = idProg;
	}

	public Long getIdProgramacion() {
		return idProgramacion;
	}

	public void setIdProgramacion(Long idProgramacion) {
		this.idProgramacion = idProgramacion;
	}

	public Boolean getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public String getIdCorrelativo() {
		return idCorrelativo;
	}

	public void setIdCorrelativo(String idCorrelativo) {
		this.idCorrelativo = idCorrelativo;
	}

	public Long getGerenparametroid() {
		return gerenparametroid;
	}

	public void setGerenparametroid(Long gerenparametroid) {
		this.gerenparametroid = gerenparametroid;
	}

	public Long getIdTipDoc() {
		return idTipDoc;
	}

	public void setIdTipDoc(Long idTipDoc) {
		this.idTipDoc = idTipDoc;
	}

	public Long getIdDocu() {
		return idDocu;
	}

	public void setIdDocu(Long idDocu) {
		this.idDocu = idDocu;
	}

	public Long getIdResponsableEquipo() {
		return idResponsableEquipo;
	}

	public void setIdResponsableEquipo(Long idResponsableEquipo) {
		this.idResponsableEquipo = idResponsableEquipo;
	}

	public String getCodDocu() {
		return codDocu;
	}

	public Date getFechaRegistroSOlicit() {
		return fechaRegistroSOlicit;
	}

	public Long getN_motivo() {
		return n_motivo;
	}

	public void setN_motivo(Long n_motivo) {
		this.n_motivo = n_motivo;
	}

	public String getSusteso() {
		return susteso;
	}

	public void setSusteso(String susteso) {
		this.susteso = susteso;
	}

	public String getSustentosolicitud() {
		return sustentosolicitud;
	}

	public void setSustentosolicitud(String sustentosolicitud) {
		this.sustentosolicitud = sustentosolicitud;
	}

	public Long getNumeromotivo() {
		return numeromotivo;
	}
	public void setNumeromotivo(Long numeromotivo) {
		this.numeromotivo = numeromotivo;
	}
	public Long getNumerotipocopia() {
		return numerotipocopia;
	}
	public void setNumerotipocopia(Long numerotipocopia) {
		this.numerotipocopia = numerotipocopia;
	}
	public Long getNumerosol() {
		return numerosol;
	}
	public void setNumerosol(Long numerosol) {
		this.numerosol = numerosol;
	}
	public Long getNrum() {
		return nrum;
	}
	public void setNrum(Long nrum) {
		this.nrum = nrum;
	}
	public Long getIdususoli() {
		return Idususoli;
	}
	public void setIdususoli(Long idususoli) {
		Idususoli = idususoli;
	}
	public String getSolicitantSolicitud() {
		return solicitantSolicitud;
	}

	public void setSolicitantSolicitud(String solicitantSolicitud) {
		this.solicitantSolicitud = solicitantSolicitud;
	}

	public String getDestinatarioSolicitud() {
		return destinatarioSolicitud;
	}

	public void setDestinatarioSolicitud(String destinatarioSolicitud) {
		this.destinatarioSolicitud = destinatarioSolicitud;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public String getEstadoSoli() {
		return estadoSoli;
	}

	public void setEstadoSoli(String estadoSoli) {
		this.estadoSoli = estadoSoli;
	}

	public String getMotivoR() {
		return motivoR;
	}
	public void setMotivoR(String motivoR) {
		this.motivoR = motivoR;
	}

	public String getTipocopia() {
		return tipocopia;
	}


	public void setTipocopia(String tipocopia) {
		this.tipocopia = tipocopia;
	}


	public void setFechaRegistroSOlicit(Date fechaRegistroSOlicit) {
		this.fechaRegistroSOlicit = fechaRegistroSOlicit;
	}


	public Long getUsuarioRevision() {
		return usuarioRevision;
	}


	public void setUsuarioRevision(Long usuarioRevision) {
		this.usuarioRevision = usuarioRevision;
	}

	public Revision(Long id, String titulo, BigDecimal disponible) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.disponible = disponible;
	}
	
	
	public Long getUsuarioDeAprobacion() {
		return usuarioDeAprobacion;
	}

	public void setUsuarioDeAprobacion(Long usuarioDeAprobacion) {
		this.usuarioDeAprobacion = usuarioDeAprobacion;
	}

	public Long getIteracion() {
		return iteracion;
	}
	public void setIteracion(Long iteracion) {
		this.iteracion = iteracion;
	}
	public String getIdDocGoogleDrive() {
		return idDocGoogleDrive;
	}

	public void setIdDocGoogleDrive(String idDocGoogleDrive) {
		this.idDocGoogleDrive = idDocGoogleDrive;
	}

	public Constante getEstadofaseact() {
		return estadofaseact;
	}

	public void setEstadofaseact(Constante estadofaseact) {
		this.estadofaseact = estadofaseact;
	}

	/*public Date getFechaPlazoAprob() {
		return fechaPlazoAprob;
	}

	public void setFechaPlazoAprob(Date fechaPlazoAprob) {
		this.fechaPlazoAprob = fechaPlazoAprob;
	}*/

	public Long getIdEsta() {
		return idEsta;
	}

	public void setIdEsta(Long idEsta) {
		this.idEsta = idEsta;
	}

	public Long getIdEstaFaseAct() {
		return idEstaFaseAct;
	}

	public void setIdEstaFaseAct(Long idEstaFaseAct) {
		this.idEstaFaseAct = idEstaFaseAct;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getIdHistorial() {
		return idHistorial;
	}

	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}

	public Constante getEstado() {
		return estado;
	}

	public void setEstado(Constante estado) {
		this.estado = estado;
	}

	public BigDecimal getDisponible() {
		return disponible;
	}

	public void setDisponible(BigDecimal disponible) {
		this.disponible = disponible;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Revision() {
		super();
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMotivoRevision() {
		return motivoRevision;
	}

	public void setMotivoRevision(String motivoRevision) {
		this.motivoRevision = motivoRevision;
	}

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public Date getFecPlazoAprobacion() {
		return fecPlazoAprobacion;
	}

	public void setFecPlazoAprobacion(Date fecPlazoAprobacion) {
		this.fecPlazoAprobacion = fecPlazoAprobacion;
	}

	public String getMotivoRechazoRev() {
		return motivoRechazoRev;
	}

	public void setMotivoRechazoRev(String motivoRechazoRev) {
		this.motivoRechazoRev = motivoRechazoRev;
	}

	public Long getIdmotirevi() {
		return idmotirevi;
	}

	public void setIdmotirevi(Long idmotirevi) {
		this.idmotirevi = idmotirevi;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	@Override
	public String toString() {
		return "Revision [id=" + id + ", codigo=" + codigo + ", titulo=" + titulo + ", numero=" + numero + ", fecha="
				+ fecha + ", idHistorial=" + idHistorial + ", estado=" + estado + ", disponible=" + disponible
				+ ", documento=" + documento + ", colaborador=" + colaborador + ", equipo=" + equipo + ", descripcion="
				+ descripcion + ", motivoRevision=" + motivoRevision + ", fechaAprobacion=" + fechaAprobacion
				+ ", fecPlazoAprobacion=" + fecPlazoAprobacion + ", motivoRechazoRev=" + motivoRechazoRev
				+ ", idmotirevi=" + idmotirevi + ", ruta=" + ruta + "]";
	}

	public Long getDiferenciaPlazo() {
		return diferenciaPlazo;
	}

	public void setDiferenciaPlazo(Long diferenciaPlazo) {
		this.diferenciaPlazo = diferenciaPlazo;
	}

	public Date getPlazoParticipante() {
		return plazoParticipante;
	}

	public void setPlazoParticipante(Date plazoParticipante) {
		this.plazoParticipante = plazoParticipante;
	}

	public Date getFechaPlazoAprob() {
		return fechaPlazoAprob;
	}

	public void setFechaPlazoAprob(Date fechaPlazoAprob) {
		this.fechaPlazoAprob = fechaPlazoAprob;
	}
	
	/* Rutas documento */
	public String getRutaDocumentoOriginal() {
		return rutaDocumentoOriginal;
	}

	public void setRutaDocumentoOriginal(String rutaDocumentoOriginal) {
		this.rutaDocumentoOriginal = rutaDocumentoOriginal;
	}

	public String getRutaDocumentoGoogle() {
		return rutaDocumentoGoogle;
	}

	public void setRutaDocumentoGoogle(String rutaDocumentoGoogle) {
		this.rutaDocumentoGoogle = rutaDocumentoGoogle;
	}

	public String getRutaDocumentoCopiaNoCont() {
		return rutaDocumentoCopiaNoCont;
	}

	public void setRutaDocumentoCopiaNoConf(String rutaDocumentoCopiaNoCont) {
		this.rutaDocumentoCopiaNoCont = rutaDocumentoCopiaNoCont;
	}

	public String getRutaDocumentoCopiaCont() {
		return rutaDocumentoCopiaCont;
	}

	public void setRutaDocumentoCopiaConf(String rutaDocumentoCopiaCont) {
		this.rutaDocumentoCopiaCont = rutaDocumentoCopiaCont;
	}

	public String getRutaDocumentoCopiaObso() {
		return rutaDocumentoCopiaObso;
	}

	public Date getFecRevi() {
		return fecRevi;
	}

	public void setFecRevi(Date fecRevi) {
		this.fecRevi = fecRevi;
	}

	public void setRutaDocumentoCopiaObso(String rutaDocumentoCopiaObso) {
		this.rutaDocumentoCopiaObso = rutaDocumentoCopiaObso;
	}
	/* Rutas documento */

	public Long getAntiguedadDocu() {
		return antiguedadDocu;
	}

	public String getDesDocu() {
		return desDocu;
	}

	public void setDesDocu(String desDocu) {
		this.desDocu = desDocu;
	}

	public void setCodDocu(String codDocu) {
		this.codDocu = codDocu;
	}

	public void setAntiguedadDocu(Long antiguedadDocu) {
		this.antiguedadDocu = antiguedadDocu;
	}

	public Long getNumRevi() {
		return numRevi;
	}

	public void setNumRevi(Long numRevi) {
		this.numRevi = numRevi;
	}

	public Long getPeriodoOblig() {
		return periodoOblig;
	}

	public void setPeriodoOblig(Long periodoOblig) {
		this.periodoOblig = periodoOblig;
	}

	public String getResponsableEquipo() {
		return responsableEquipo;
	}

	public void setResponsableEquipo(String responsableEquipo) {
		this.responsableEquipo = responsableEquipo;
	}

	public void setRutaDocumentoCopiaNoCont(String rutaDocumentoCopiaNoCont) {
		this.rutaDocumentoCopiaNoCont = rutaDocumentoCopiaNoCont;
	}

	public void setRutaDocumentoCopiaCont(String rutaDocumentoCopiaCont) {
		this.rutaDocumentoCopiaCont = rutaDocumentoCopiaCont;
	}

	public Date getNombreEquipo() {
		return nombreEquipo;
	}

	public void setNombreEquipo(Date nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}

	public Long getIdEstadoProg() {
		return idEstadoProg;
	}

	public void setIdEstadoProg(Long idEstadoProg) {
		this.idEstadoProg = idEstadoProg;
	}

	public String getFechDistribucion() {
		return fechDistribucion;
	}

	public void setFechDistribucion(String fechDistribucion) {
		this.fechDistribucion = fechDistribucion;
	}

	public String getDescequipo() {
		return descequipo;
	}

	public void setDescequipo(String descequipo) {
		this.descequipo = descequipo;
	}

	public String getIdequipo() {
		return idequipo;
	}

	public void setIdequipo(String idequipo) {
		this.idequipo = idequipo;
	}

	public String getDesestadoejec() {
		return desestadoejec;
	}

	public void setDesestadoejec(String desestadoejec) {
		this.desestadoejec = desestadoejec;
	}

	public Long getIdestadoejec() {
		return idestadoejec;
	}

	public void setIdestadoejec(Long idestadoejec) {
		this.idestadoejec = idestadoejec;
	}

	public String getDesestado() {
		return desestado;
	}

	public void setDesestado(String desestado) {
		this.desestado = desestado;
	}

	public String getTipobusq() {
		return tipobusq;
	}

	public void setTipobusq(String tipobusq) {
		this.tipobusq = tipobusq;
	}

	public Long getEstados() {
		return estados;
	}

	public void setEstados(Long estados) {
		this.estados = estados;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public String getIdListaVerificacion() {
		return idListaVerificacion;
	}

	public void setIdListaVerificacion(String idListaVerificacion) {
		this.idListaVerificacion = idListaVerificacion;
	}

	public String getAnioantiguedad() {
		return anioantiguedad;
	}

	public void setAnioantiguedad(String anioantiguedad) {
		this.anioantiguedad = anioantiguedad;
	}

	public String getGerenparametrodesc() {
		return gerenparametrodesc;
	}

	public void setGerenparametrodesc(String gerenparametrodesc) {
		this.gerenparametrodesc = gerenparametrodesc;
	}

	public String getTipodocumento() {
		return tipodocumento;
	}

	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}

	public String getRutacopianocontrolada() {
		return rutacopianocontrolada;
	}

	public void setRutacopianocontrolada(String rutacopianocontrolada) {
		this.rutacopianocontrolada = rutacopianocontrolada;
	}

	public String getNestcopi() {
		return nestcopi;
	}

	public void setNestcopi(String nestcopi) {
		this.nestcopi = nestcopi;
	}

	public String getObserva() {
		return observa;
	}

	public void setObserva(String observa) {
		this.observa = observa;
	}

	public String getNumtipoestasoli() {
		return numtipoestasoli;
	}

	public void setNumtipoestasoli(String numtipoestasoli) {
		this.numtipoestasoli = numtipoestasoli;
	}

	public String getNmotivo() {
		return nmotivo;
	}

	public void setNmotivo(String nmotivo) {
		this.nmotivo = nmotivo;
	}

	public String getResumenCritica() {
		return resumenCritica;
	}

	public void setResumenCritica(String resumenCritica) {
		this.resumenCritica = resumenCritica;
	}

	public String getIndicadorestado() {
		return indicadorestado;
	}

	public void setIndicadorestado(String indicadorestado) {
		this.indicadorestado = indicadorestado;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Long getRevisionActual() {
		return revisionActual;
	}

	public void setRevisionActual(Long revisionActual) {
		this.revisionActual = revisionActual;
	}

	public String getDesta() {
		return desta;
	}

	public void setDesta(String desta) {
		this.desta = desta;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getAlcance() {
		return alcance;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

	public String getSustento() {
		return sustento;
	}

	public void setSustento(String sustento) {
		this.sustento = sustento;
	}

	public Long getGerencia() {
		return gerencia;
	}

	public void setGerencia(Long gerencia) {
		this.gerencia = gerencia;
	}

	public Long getNumerosolicitud() {
		return numerosolicitud;
	}

	public void setNumerosolicitud(Long numerosolicitud) {
		this.numerosolicitud = numerosolicitud;
	}

	public String getNidrevision() {
		return nidrevision;
	}

	public void setNidrevision(String nidrevision) {
		this.nidrevision = nidrevision;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

}