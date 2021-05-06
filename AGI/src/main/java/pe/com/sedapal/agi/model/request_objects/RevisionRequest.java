package pe.com.sedapal.agi.model.request_objects;

import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.List;

import pe.com.sedapal.agi.model.AdjuntoMensaje;

public class RevisionRequest {
	Long id;
	Long idDocumento;
	Long numero;
	Long disponible;
	String estado;
	String fechaRevision;
	String fechaInicio;
	String fechaFinal;
	String nombreCompleto;
	Long codigo;
	String codigoDoc;
	String codigorevision;
	String nombre;
	String motivoRevision;
	Date fechaAprobacion;
	String ruta;
	Date fecPlazoAprobacion;
	String motivoRechazoRev;
	String descripcion;
	Long idHistorial;
	Long idColaborador;
	Long idmotirevi;
	String tituloDoc;
	String titulo;
	Long idFase;
	Long idUsuario;
	Long tipodocumento;
	String tipodoc;
	Long gerenparametroid;
	String gerenId;
	String gerenparametrodesc;
	Long anioantiguedad;
	Long numeroIteracion;
	
	

	//String archivo  ; 
	AdjuntoMensaje archivo;// = new ArrayList<>();
	
	Long idProg;
	Date fecCreProg;
	String estProg;
	String usuCreProg;
	Date fechModProg;
	String desTipoDocuProg;
	Long anioProg;
	Long idEstProg;
	String equipoProg;
	Long idEstEjecProg;
	String nombreEquipo;
	Long cantDocRevisar;
	Date fechDistribucion;
	String IdEstadoProg;
	// Long idEquipoProg;
	Long idususoli;
	Long btipocopi;
	String bnombredestina;
	String bapematerno;
	String bapepaterno;
	String bequipo;
	String titulodocumento;
	String codigoDocumento;
	/*
	 * Long idTipDoc; Long idResponsableEquipo; Long antiguedadDocu; Long idDocu;
	 */
	String estEjec;
	Long fichaTrabajador;
	Long areaTrabajador;
	Long nroFicha;
	Long codArea;
	Long idusuresp;
	String nombres;
	String apellidoPaterno;
	String apellidoMaterno;
	Long estCono;
		
	public String getGerenId() {
		return gerenId;
	}

	public void setGerenId(String gerenId) {
		this.gerenId = gerenId;
	}

	public String getTipodoc() {
		return tipodoc;
	}

	public void setTipodoc(String tipodoc) {
		this.tipodoc = tipodoc;
	}

	

	

	

	public Long getEstCono() {
		return estCono;
	}

	public void setEstCono(Long estCono) {
		this.estCono = estCono;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public Long getIdusuresp() {
		return idusuresp;
	}

	public void setIdusuresp(Long idusuresp) {
		this.idusuresp = idusuresp;
	}

	public Long getNroFicha() {
		return nroFicha;
	}

	public void setNroFicha(Long nroFicha) {
		this.nroFicha = nroFicha;
	}

	public Long getCodArea() {
		return codArea;
	}

	public void setCodArea(Long codArea) {
		this.codArea = codArea;
	}

	public Long getAreaTrabajador() {
		return areaTrabajador;
	}

	public void setAreaTrabajador(Long areaTrabajador) {
		this.areaTrabajador = areaTrabajador;
	}

	public Long getFichaTrabajador() {
		return fichaTrabajador;
	}

	public void setFichaTrabajador(Long fichaTrabajador) {
		this.fichaTrabajador = fichaTrabajador;
	}

	public String getEstEjec() {
		return estEjec;
	}

	public void setEstEjec(String estEjec) {
		this.estEjec = estEjec;
	}

	public String getEquipoProg() {
		return equipoProg;
	}

	public String getIdEstadoProg() {
		return IdEstadoProg;
	}

	public void setIdEstadoProg(String idEstadoProg) {
		IdEstadoProg = idEstadoProg;
	}

	/*
	 * public Long getIdEquipoProg() { return idEquipoProg; } public void
	 * setIdEquipoProg(Long idEquipoProg) { this.idEquipoProg = idEquipoProg; }
	 */
	public Date getFechDistribucion() {
		return fechDistribucion;
	}

	public void setFechDistribucion(Date fechDistribucion) {
		this.fechDistribucion = fechDistribucion;
	}

	public Long getCantDocRevisar() {
		return cantDocRevisar;
	}

	public void setCantDocRevisar(Long cantDocRevisar) {
		this.cantDocRevisar = cantDocRevisar;
	}

	public String getNombreEquipo() {
		return nombreEquipo;
	}

	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}

	public Long getIdEstEjecProg() {
		return idEstEjecProg;
	}

	public void setIdEstEjecProg(Long idEstEjecProg) {
		this.idEstEjecProg = idEstEjecProg;
	}

	public void setEquipoProg(String equipoProg) {
		this.equipoProg = equipoProg;
	}

	public Long getTipodocumento() {
		return tipodocumento;
	}

	public void setTipodocumento(Long tipodocumento) {
		this.tipodocumento = tipodocumento;
	}

	public Long getGerenparametroid() {
		return gerenparametroid;
	}

	public void setGerenparametroid(Long gerenparametroid) {
		this.gerenparametroid = gerenparametroid;
	}

	public String getGerenparametrodesc() {
		return gerenparametrodesc;
	}

	public void setGerenparametrodesc(String gerenparametrodesc) {
		this.gerenparametrodesc = gerenparametrodesc;
	}

	public Long getAnioantiguedad() {
		return anioantiguedad;
	}

	public void setAnioantiguedad(Long anioantiguedad) {
		this.anioantiguedad = anioantiguedad;
	}

	public String getTitulodocumento() {
		return titulodocumento;
	}

	public void setTitulodocumento(String titulodocumento) {
		this.titulodocumento = titulodocumento;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public Long getBtipocopi() {
		return btipocopi;
	}

	public void setBtipocopi(Long btipocopi) {
		this.btipocopi = btipocopi;
	}

	public String getBnombredestina() {
		return bnombredestina;
	}

	public void setBnombredestina(String bnombredestina) {
		this.bnombredestina = bnombredestina;
	}

	public String getBapematerno() {
		return bapematerno;
	}

	public void setBapematerno(String bapematerno) {
		this.bapematerno = bapematerno;
	}

	public String getBapepaterno() {
		return bapepaterno;
	}

	public void setBapepaterno(String bapepaterno) {
		this.bapepaterno = bapepaterno;
	}

	public String getBequipo() {
		return bequipo;
	}

	public void setBequipo(String bequipo) {
		this.bequipo = bequipo;
	}

	public Long getIdususoli() {
		return idususoli;
	}

	public void setIdususoli(Long idususoli) {
		this.idususoli = idususoli;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCodigorevision() {
		return codigorevision;
	}

	public void setCodigorevision(String codigorevision) {
		this.codigorevision = codigorevision;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Long getDisponible() {
		return disponible;
	}

	public void setDisponible(Long disponible) {
		this.disponible = disponible;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(String fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getIdHistorial() {
		return idHistorial;
	}

	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}

	public Long getIdColaborador() {
		return idColaborador;
	}

	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}

	public Long getIdmotirevi() {
		return idmotirevi;
	}

	public void setIdmotirevi(Long idmotirevi) {
		this.idmotirevi = idmotirevi;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getTituloDoc() {
		return tituloDoc;
	}

	public void setTituloDoc(String tituloDoc) {
		this.tituloDoc = tituloDoc;
	}

	public String getCodigoDoc() {
		return codigoDoc;
	}

	public void setCodigoDoc(String codigoDoc) {
		this.codigoDoc = codigoDoc;
	}

	public Long getIdFase() {
		return idFase;
	}

	public void setIdFase(Long idFase) {
		this.idFase = idFase;
	}

	public Long getIdProg() {
		return idProg;
	}

	public void setIdProg(Long idProg) {
		this.idProg = idProg;
	}

	public Date getFecCreProg() {
		return fecCreProg;
	}

	public void setFecCreProg(Date fecCreProg) {
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

	public Date getFechModProg() {
		return fechModProg;
	}

	public void setFechModProg(Date fechModProg) {
		this.fechModProg = fechModProg;
	}

	public String getDesTipoDocuProg() {
		return desTipoDocuProg;
	}

	public void setDesTipoDocuProg(String desTipoDocuProg) {
		this.desTipoDocuProg = desTipoDocuProg;
	}

	public Long getAnioProg() {
		return anioProg;
	}

	public void setAnioProg(Long anioProg) {
		this.anioProg = anioProg;
	}

	public Long getIdEstProg() {
		return idEstProg;
	}

	public void setIdEstProg(Long idEstProg) {
		this.idEstProg = idEstProg;
	}

	public Long getNumeroIteracion() {
		return numeroIteracion;
	}

	public void setNumeroIteracion(Long numeroIteracion) {
		this.numeroIteracion = numeroIteracion;
	}

}