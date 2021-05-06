package pe.com.sedapal.agi.model.request_objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.List;

import pe.com.sedapal.agi.model.AdjuntoMensaje;
import pe.com.sedapal.agi.model.Constante;

public class RevisionRequestAdjunto {
	
String tabName;
Number id;
Number number;
Constante estado;
String   idHistorial;
String   nidrevision;
String   documento;
Number   numerosolicitud;
String   numeromotivo;
String  sustentosolicitud;
String susteso;
String colaborador;   
Number   gerencia;
 Number usuarioDeAprobacion;
Date fechaAprobacion;
 String nidrevi;
  String codigo;
  String sustento;
  String motivoR;
  String tipoCopia;
  String idestejec;
  Number usuarioRevision;
  String numerotipocopia;
 // RutaParticipante listaParticipante;
	  String fecha;
	  String alcance;
	  String proceso;
	  String equipo;
	  String desta;
	  Number revisionActual;
	  String titulo;
	  String motivo;
	  String nrum;
	 String numerosol;
	  String estadoSoli;
	  String fechaSolicitud;
	  String solicitantSolicitud;
	  String destinatarioSolicitud;
	  String indicadorestado;
	  String resumenCritica;
	  Number idususoli;
	  String nmotivo;
	  String numtipoestasoli;
	  String observa;
	  String nestcopi;
	  String rutacopianocontrolada;
	  String motivoRevision;
	  String fechaPlazoAprob;
	  String rutaDocumt;
	  String rutaDocumtNueva;
	  String tipodocumento;
	  String gerenparametroid;
	  String gerenparametrodesc;
	  String anioantiguedad;
	  Number iteracion;
	  String rutaDocumentoOriginal;
	  String rutaDocumentoGoogle;
	  String rutaDocumentoCopiaNoCont;
	String	 rutaDocumentoCopiaCont;
	String	rutaDocumentoCopiaObso;
	 Boolean seleccionado;
	 String codDocu;
	 String desDocu;
	 Number numRevi;
	 Date fecRevi;
	Number antiguedadDocu;
	 Number periodoOblig;
	 String responsableEquipo;
	 String idListaVerificacion;
	 String idTipDoc;
	String idResponsableEquipo;
	 String idDocu;
	 Number anio;
	 Number estados;
	 String tipobusq;
	 String desestado;
	  String correlativo;
	  String idProgExistente;
	  Number idestadoejec;
	  String desestadoejec;
	  String idequipo;
	  String descequipo;
	  String fechDistribucion;
	  Number idEstadoProg;
	  Date nombreEquipo;
	  Number cantDocRevisar;
	  Number idTrimestre;
	  Number cantDocu;
	 Number primerTrim;
	 Number segundoTrim;
	 Number tercerTrim;
	 Number cuartoTrim;
	  String responsableEquipoSelecc;
	  Number idResponsableEquipoSelecc;
	 Date fechaActual;
	 String estEjec;
	 AdjuntoMensaje archivo;
//	 		File archivo;
	 String mensajearchivo;
	 String codFichaLogueado;
	Number idmotirevi;
	 String descripcion;
	 Number disponible;
	 Number diferenciaPlazo;
	 String fecPlazoAprobacion;
	 String motivoRechazoRev;
	  String ruta;
	  String idDocGoogleDrive;
	  String idProg;
	  String fecCreProg;
	  String estProg;
	  String usuCreProg;
	  String fechModProg;
	  String desTipoDocuProg;
	  String anioProg;
	  String nomapellidoparterDestina;
	  Date fechaRegistroSOlicit;
	  Date fechaAprobacionDocumento;
		String usuarioAprobacionDocumento;
		Date fechaAprobacionAnterior;
		Number numeroAnterior;
		Date fechaAprobDocu;
	Number numeroMostrar;
	Date  fechaMostrar;
	  String estiloPlazo;
	  String rutaFinal;
	  Boolean revisHist;
String estProgr;
String fechaProgramacion;
String fechaDistribucion;
String fechaEjecucion;
Number idEquipoProgramacion;
	String desestadoProg;
String equipoProgramacion;
String estadoFase;
Number idProgramacion;
public String getTabName() {
	return tabName;
}
public void setTabName(String tabName) {
	this.tabName = tabName;
}
public Number getId() {
	return id;
}
public void setId(Number id) {
	this.id = id;
}
public Number getNumber() {
	return number;
}
public void setNumber(Number number) {
	this.number = number;
}
public Constante getEstado() {
	return estado;
}
public void setEstado(Constante estado) {
	this.estado = estado;
}
public String getIdHistorial() {
	return idHistorial;
}
public void setIdHistorial(String idHistorial) {
	this.idHistorial = idHistorial;
}
public String getNidrevision() {
	return nidrevision;
}
public void setNidrevision(String nidrevision) {
	this.nidrevision = nidrevision;
}
public String getDocumento() {
	return documento;
}
public void setDocumento(String documento) {
	this.documento = documento;
}
public Number getNumerosolicitud() {
	return numerosolicitud;
}
public void setNumerosolicitud(Number numerosolicitud) {
	this.numerosolicitud = numerosolicitud;
}
public String getNumeromotivo() {
	return numeromotivo;
}
public void setNumeromotivo(String numeromotivo) {
	this.numeromotivo = numeromotivo;
}
public String getSustentosolicitud() {
	return sustentosolicitud;
}
public void setSustentosolicitud(String sustentosolicitud) {
	this.sustentosolicitud = sustentosolicitud;
}
public String getSusteso() {
	return susteso;
}
public void setSusteso(String susteso) {
	this.susteso = susteso;
}
public String getColaborador() {
	return colaborador;
}
public void setColaborador(String colaborador) {
	this.colaborador = colaborador;
}
public Number getGerencia() {
	return gerencia;
}
public void setGerencia(Number gerencia) {
	this.gerencia = gerencia;
}
public Number getUsuarioDeAprobacion() {
	return usuarioDeAprobacion;
}
public void setUsuarioDeAprobacion(Number usuarioDeAprobacion) {
	this.usuarioDeAprobacion = usuarioDeAprobacion;
}
public Date getFechaAprobacion() {
	return fechaAprobacion;
}
public void setFechaAprobacion(Date fechaAprobacion) {
	this.fechaAprobacion = fechaAprobacion;
}
public String getNidrevi() {
	return nidrevi;
}
public void setNidrevi(String nidrevi) {
	this.nidrevi = nidrevi;
}
public String getCodigo() {
	return codigo;
}
public void setCodigo(String codigo) {
	this.codigo = codigo;
}
public String getSustento() {
	return sustento;
}
public void setSustento(String sustento) {
	this.sustento = sustento;
}
public String getMotivoR() {
	return motivoR;
}
public void setMotivoR(String motivoR) {
	this.motivoR = motivoR;
}
public String getTipoCopia() {
	return tipoCopia;
}
public void setTipoCopia(String tipoCopia) {
	this.tipoCopia = tipoCopia;
}
public String getIdestejec() {
	return idestejec;
}
public void setIdestejec(String idestejec) {
	this.idestejec = idestejec;
}
public Number getUsuarioRevision() {
	return usuarioRevision;
}
public void setUsuarioRevision(Number usuarioRevision) {
	this.usuarioRevision = usuarioRevision;
}
public String getNumerotipocopia() {
	return numerotipocopia;
}
public void setNumerotipocopia(String numerotipocopia) {
	this.numerotipocopia = numerotipocopia;
}
public String getFecha() {
	return fecha;
}
public void setFecha(String fecha) {
	this.fecha = fecha;
}
public String getAlcance() {
	return alcance;
}
public void setAlcance(String alcance) {
	this.alcance = alcance;
}
public String getProceso() {
	return proceso;
}
public void setProceso(String proceso) {
	this.proceso = proceso;
}
public String getEquipo() {
	return equipo;
}
public void setEquipo(String equipo) {
	this.equipo = equipo;
}
public String getDesta() {
	return desta;
}
public void setDesta(String desta) {
	this.desta = desta;
}
public Number getRevisionActual() {
	return revisionActual;
}
public void setRevisionActual(Number revisionActual) {
	this.revisionActual = revisionActual;
}
public String getTitulo() {
	return titulo;
}
public void setTitulo(String titulo) {
	this.titulo = titulo;
}
public String getMotivo() {
	return motivo;
}
public void setMotivo(String motivo) {
	this.motivo = motivo;
}
public String getNrum() {
	return nrum;
}
public void setNrum(String nrum) {
	this.nrum = nrum;
}
public String getNumerosol() {
	return numerosol;
}
public void setNumerosol(String numerosol) {
	this.numerosol = numerosol;
}
public String getEstadoSoli() {
	return estadoSoli;
}
public void setEstadoSoli(String estadoSoli) {
	this.estadoSoli = estadoSoli;
}
public String getFechaSolicitud() {
	return fechaSolicitud;
}
public void setFechaSolicitud(String fechaSolicitud) {
	this.fechaSolicitud = fechaSolicitud;
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
public String getIndicadorestado() {
	return indicadorestado;
}
public void setIndicadorestado(String indicadorestado) {
	this.indicadorestado = indicadorestado;
}
public String getResumenCritica() {
	return resumenCritica;
}
public void setResumenCritica(String resumenCritica) {
	this.resumenCritica = resumenCritica;
}
public Number getIdususoli() {
	return idususoli;
}
public void setIdususoli(Number idususoli) {
	this.idususoli = idususoli;
}
public String getNmotivo() {
	return nmotivo;
}
public void setNmotivo(String nmotivo) {
	this.nmotivo = nmotivo;
}
public String getNumtipoestasoli() {
	return numtipoestasoli;
}
public void setNumtipoestasoli(String numtipoestasoli) {
	this.numtipoestasoli = numtipoestasoli;
}
public String getObserva() {
	return observa;
}
public void setObserva(String observa) {
	this.observa = observa;
}
public String getNestcopi() {
	return nestcopi;
}
public void setNestcopi(String nestcopi) {
	this.nestcopi = nestcopi;
}
public String getRutacopianocontrolada() {
	return rutacopianocontrolada;
}
public void setRutacopianocontrolada(String rutacopianocontrolada) {
	this.rutacopianocontrolada = rutacopianocontrolada;
}
public String getMotivoRevision() {
	return motivoRevision;
}
public void setMotivoRevision(String motivoRevision) {
	this.motivoRevision = motivoRevision;
}
public String getFechaPlazoAprob() {
	return fechaPlazoAprob;
}
public void setFechaPlazoAprob(String fechaPlazoAprob) {
	this.fechaPlazoAprob = fechaPlazoAprob;
}
public String getRutaDocumt() {
	return rutaDocumt;
}
public void setRutaDocumt(String rutaDocumt) {
	this.rutaDocumt = rutaDocumt;
}
public String getRutaDocumtNueva() {
	return rutaDocumtNueva;
}
public void setRutaDocumtNueva(String rutaDocumtNueva) {
	this.rutaDocumtNueva = rutaDocumtNueva;
}
public String getTipodocumento() {
	return tipodocumento;
}
public void setTipodocumento(String tipodocumento) {
	this.tipodocumento = tipodocumento;
}
public String getGerenparametroid() {
	return gerenparametroid;
}
public void setGerenparametroid(String gerenparametroid) {
	this.gerenparametroid = gerenparametroid;
}
public String getGerenparametrodesc() {
	return gerenparametrodesc;
}
public void setGerenparametrodesc(String gerenparametrodesc) {
	this.gerenparametrodesc = gerenparametrodesc;
}
public String getAnioantiguedad() {
	return anioantiguedad;
}
public void setAnioantiguedad(String anioantiguedad) {
	this.anioantiguedad = anioantiguedad;
}
public Number getIteracion() {
	return iteracion;
}
public void setIteracion(Number iteracion) {
	this.iteracion = iteracion;
}
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
public Boolean getSeleccionado() {
	return seleccionado;
}
public void setSeleccionado(Boolean seleccionado) {
	this.seleccionado = seleccionado;
}
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
public Number getNumRevi() {
	return numRevi;
}
public void setNumRevi(Number numRevi) {
	this.numRevi = numRevi;
}
public Date getFecRevi() {
	return fecRevi;
}
public void setFecRevi(Date fecRevi) {
	this.fecRevi = fecRevi;
}
public Number getAntiguedadDocu() {
	return antiguedadDocu;
}
public void setAntiguedadDocu(Number antiguedadDocu) {
	this.antiguedadDocu = antiguedadDocu;
}
public Number getPeriodoOblig() {
	return periodoOblig;
}
public void setPeriodoOblig(Number periodoOblig) {
	this.periodoOblig = periodoOblig;
}
public String getResponsableEquipo() {
	return responsableEquipo;
}
public void setResponsableEquipo(String responsableEquipo) {
	this.responsableEquipo = responsableEquipo;
}
public String getIdListaVerificacion() {
	return idListaVerificacion;
}
public void setIdListaVerificacion(String idListaVerificacion) {
	this.idListaVerificacion = idListaVerificacion;
}
public String getIdTipDoc() {
	return idTipDoc;
}
public void setIdTipDoc(String idTipDoc) {
	this.idTipDoc = idTipDoc;
}
public String getIdResponsableEquipo() {
	return idResponsableEquipo;
}
public void setIdResponsableEquipo(String idResponsableEquipo) {
	this.idResponsableEquipo = idResponsableEquipo;
}
public String getIdDocu() {
	return idDocu;
}
public void setIdDocu(String idDocu) {
	this.idDocu = idDocu;
}
public Number getAnio() {
	return anio;
}
public void setAnio(Number anio) {
	this.anio = anio;
}
public Number getEstados() {
	return estados;
}
public void setEstados(Number estados) {
	this.estados = estados;
}
public String getTipobusq() {
	return tipobusq;
}
public void setTipobusq(String tipobusq) {
	this.tipobusq = tipobusq;
}
public String getDesestado() {
	return desestado;
}
public void setDesestado(String desestado) {
	this.desestado = desestado;
}
public String getCorrelativo() {
	return correlativo;
}
public void setCorrelativo(String correlativo) {
	this.correlativo = correlativo;
}
public String getIdProgExistente() {
	return idProgExistente;
}
public void setIdProgExistente(String idProgExistente) {
	this.idProgExistente = idProgExistente;
}
public Number getIdestadoejec() {
	return idestadoejec;
}
public void setIdestadoejec(Number idestadoejec) {
	this.idestadoejec = idestadoejec;
}
public String getDesestadoejec() {
	return desestadoejec;
}
public void setDesestadoejec(String desestadoejec) {
	this.desestadoejec = desestadoejec;
}
public String getIdequipo() {
	return idequipo;
}
public void setIdequipo(String idequipo) {
	this.idequipo = idequipo;
}
public String getDescequipo() {
	return descequipo;
}
public void setDescequipo(String descequipo) {
	this.descequipo = descequipo;
}
public String getFechDistribucion() {
	return fechDistribucion;
}
public void setFechDistribucion(String fechDistribucion) {
	this.fechDistribucion = fechDistribucion;
}
public Number getIdEstadoProg() {
	return idEstadoProg;
}
public void setIdEstadoProg(Number idEstadoProg) {
	this.idEstadoProg = idEstadoProg;
}
public Date getNombreEquipo() {
	return nombreEquipo;
}
public void setNombreEquipo(Date nombreEquipo) {
	this.nombreEquipo = nombreEquipo;
}
public Number getCantDocRevisar() {
	return cantDocRevisar;
}
public void setCantDocRevisar(Number cantDocRevisar) {
	this.cantDocRevisar = cantDocRevisar;
}
public Number getIdTrimestre() {
	return idTrimestre;
}
public void setIdTrimestre(Number idTrimestre) {
	this.idTrimestre = idTrimestre;
}
public Number getCantDocu() {
	return cantDocu;
}
public void setCantDocu(Number cantDocu) {
	this.cantDocu = cantDocu;
}
public Number getPrimerTrim() {
	return primerTrim;
}
public void setPrimerTrim(Number primerTrim) {
	this.primerTrim = primerTrim;
}
public Number getSegundoTrim() {
	return segundoTrim;
}
public void setSegundoTrim(Number segundoTrim) {
	this.segundoTrim = segundoTrim;
}
public Number getTercerTrim() {
	return tercerTrim;
}
public void setTercerTrim(Number tercerTrim) {
	this.tercerTrim = tercerTrim;
}
public Number getCuartoTrim() {
	return cuartoTrim;
}
public void setCuartoTrim(Number cuartoTrim) {
	this.cuartoTrim = cuartoTrim;
}
public String getResponsableEquipoSelecc() {
	return responsableEquipoSelecc;
}
public void setResponsableEquipoSelecc(String responsableEquipoSelecc) {
	this.responsableEquipoSelecc = responsableEquipoSelecc;
}
public Number getIdResponsableEquipoSelecc() {
	return idResponsableEquipoSelecc;
}
public void setIdResponsableEquipoSelecc(Number idResponsableEquipoSelecc) {
	this.idResponsableEquipoSelecc = idResponsableEquipoSelecc;
}
public Date getFechaActual() {
	return fechaActual;
}
public void setFechaActual(Date fechaActual) {
	this.fechaActual = fechaActual;
}
public String getEstEjec() {
	return estEjec;
}
public void setEstEjec(String estEjec) {
	this.estEjec = estEjec;
}
public AdjuntoMensaje getArchivo() {
	return archivo;
}
public void setArchivo(AdjuntoMensaje archivo) {
	this.archivo = archivo;
}
public String getMensajearchivo() {
	return mensajearchivo;
}
public void setMensajearchivo(String mensajearchivo) {
	this.mensajearchivo = mensajearchivo;
}
public String getCodFichaLogueado() {
	return codFichaLogueado;
}
public void setCodFichaLogueado(String codFichaLogueado) {
	this.codFichaLogueado = codFichaLogueado;
}
public Number getIdmotirevi() {
	return idmotirevi;
}
public void setIdmotirevi(Number idmotirevi) {
	this.idmotirevi = idmotirevi;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public Number getDisponible() {
	return disponible;
}
public void setDisponible(Number disponible) {
	this.disponible = disponible;
}
public Number getDiferenciaPlazo() {
	return diferenciaPlazo;
}
public void setDiferenciaPlazo(Number diferenciaPlazo) {
	this.diferenciaPlazo = diferenciaPlazo;
}
public String getFecPlazoAprobacion() {
	return fecPlazoAprobacion;
}
public void setFecPlazoAprobacion(String fecPlazoAprobacion) {
	this.fecPlazoAprobacion = fecPlazoAprobacion;
}
public String getMotivoRechazoRev() {
	return motivoRechazoRev;
}
public void setMotivoRechazoRev(String motivoRechazoRev) {
	this.motivoRechazoRev = motivoRechazoRev;
}
public String getRuta() {
	return ruta;
}
public void setRuta(String ruta) {
	this.ruta = ruta;
}
public String getIdDocGoogleDrive() {
	return idDocGoogleDrive;
}
public void setIdDocGoogleDrive(String idDocGoogleDrive) {
	this.idDocGoogleDrive = idDocGoogleDrive;
}
public String getIdProg() {
	return idProg;
}
public void setIdProg(String idProg) {
	this.idProg = idProg;
}
public String getFecCreProg() {
	return fecCreProg;
}
public void setFecCreProg(String fecCreProg) {
	this.fecCreProg = fecCreProg;
}
public String getEstProg() {
	return estProg;
}
public void setEstProg(String estProg) {
	this.estProg = estProg;
}
public String getUsuCreProg() {
	return usuCreProg;
}
public void setUsuCreProg(String usuCreProg) {
	this.usuCreProg = usuCreProg;
}
public String getFechModProg() {
	return fechModProg;
}
public void setFechModProg(String fechModProg) {
	this.fechModProg = fechModProg;
}
public String getDesTipoDocuProg() {
	return desTipoDocuProg;
}
public void setDesTipoDocuProg(String desTipoDocuProg) {
	this.desTipoDocuProg = desTipoDocuProg;
}
public String getAnioProg() {
	return anioProg;
}
public void setAnioProg(String anioProg) {
	this.anioProg = anioProg;
}
public String getNomapellidoparterDestina() {
	return nomapellidoparterDestina;
}
public void setNomapellidoparterDestina(String nomapellidoparterDestina) {
	this.nomapellidoparterDestina = nomapellidoparterDestina;
}
public Date getFechaRegistroSOlicit() {
	return fechaRegistroSOlicit;
}
public void setFechaRegistroSOlicit(Date fechaRegistroSOlicit) {
	this.fechaRegistroSOlicit = fechaRegistroSOlicit;
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
public Date getFechaAprobacionAnterior() {
	return fechaAprobacionAnterior;
}
public void setFechaAprobacionAnterior(Date fechaAprobacionAnterior) {
	this.fechaAprobacionAnterior = fechaAprobacionAnterior;
}
public Number getNumeroAnterior() {
	return numeroAnterior;
}
public void setNumeroAnterior(Number numeroAnterior) {
	this.numeroAnterior = numeroAnterior;
}
public Date getFechaAprobDocu() {
	return fechaAprobDocu;
}
public void setFechaAprobDocu(Date fechaAprobDocu) {
	this.fechaAprobDocu = fechaAprobDocu;
}
public Number getNumeroMostrar() {
	return numeroMostrar;
}
public void setNumeroMostrar(Number numeroMostrar) {
	this.numeroMostrar = numeroMostrar;
}
public Date getFechaMostrar() {
	return fechaMostrar;
}
public void setFechaMostrar(Date fechaMostrar) {
	this.fechaMostrar = fechaMostrar;
}
public String getEstiloPlazo() {
	return estiloPlazo;
}
public void setEstiloPlazo(String estiloPlazo) {
	this.estiloPlazo = estiloPlazo;
}
public String getRutaFinal() {
	return rutaFinal;
}
public void setRutaFinal(String rutaFinal) {
	this.rutaFinal = rutaFinal;
}
public Boolean getRevisHist() {
	return revisHist;
}
public void setRevisHist(Boolean revisHist) {
	this.revisHist = revisHist;
}
public String getEstProgr() {
	return estProgr;
}
public void setEstProgr(String estProgr) {
	this.estProgr = estProgr;
}
public String getFechaProgramacion() {
	return fechaProgramacion;
}
public void setFechaProgramacion(String fechaProgramacion) {
	this.fechaProgramacion = fechaProgramacion;
}
public String getFechaDistribucion() {
	return fechaDistribucion;
}
public void setFechaDistribucion(String fechaDistribucion) {
	this.fechaDistribucion = fechaDistribucion;
}
public String getFechaEjecucion() {
	return fechaEjecucion;
}
public void setFechaEjecucion(String fechaEjecucion) {
	this.fechaEjecucion = fechaEjecucion;
}
public Number getIdEquipoProgramacion() {
	return idEquipoProgramacion;
}
public void setIdEquipoProgramacion(Number idEquipoProgramacion) {
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
public String getEstadoFase() {
	return estadoFase;
}
public void setEstadoFase(String estadoFase) {
	this.estadoFase = estadoFase;
}
public Number getIdProgramacion() {
	return idProgramacion;
}
public void setIdProgramacion(Number idProgramacion) {
	this.idProgramacion = idProgramacion;
}
	  
	


}