package pe.com.sedapal.agi.model;

import java.math.BigDecimal;
import java.util.List;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Jerarquia {
	
	private String 			v_descons;
	private Long			id;
	private Long			idTipo;
	private String			tipo;
	//private String			descripcion;
	//private Long			nivel;
	//private Long			orden;
	private Long			idPadre;
	private Jerarquia		padre;
	private EstadoConstante	estado;
	private BigDecimal		disponible;
	private String			ruta;
	private String			indicadorGrabar;
	private String			indicadorAgua;
	private Long			retencionRevision;
	private Long			idFicha;
	private FichaTecnica	ficha;
	private List<Constante>	extension;
	private String			textoNodo;
	private List<String>    listaHijos;
	private Long 			idJerarquiaPadre;
	private Long 			idJerarquia;
	private String			codJera;
	private String			abrJera;
	private Long			idTipoDocu;
	private String detAnterior;
	private String nomAnterior;
	private String descDocumento;
	private Long cantidadDocumentos;
	private Long indicadorDescargas;
	
	
	
	
	
	public Long getIndicadorDescargas() {
		return indicadorDescargas;
	}
	public void setIndicadorDescargas(Long indicadorDescargas) {
		this.indicadorDescargas = indicadorDescargas;
	}
	public Long getIdTipoDocu() {
		return idTipoDocu;
	}
	public void setIdTipoDocu(Long idTipoDocu) {
		this.idTipoDocu = idTipoDocu;
	}
	public String getCodJera() {
		return codJera;
	}
	public void setCodJera(String codJera) {
		this.codJera = codJera;
	}
	public String getAbrJera() {
		return abrJera;
	}
	public void setAbrJera(String abrJera) {
		this.abrJera = abrJera;
	}

	private Long idRequisito;
	//private String orden;
	private Long orden;
	private String descripcion;
	//private String nivel;
	private Long nivel;
	private String detalle;
	private Long idRequisitoPadre;
	private String tipoNorma;
	private Long idNorma;
	private String descripcionNorma;
	
	private Long idproc;
	private Long idalcasgi;
	private Long idgeregnrl;
	
	public String getV_descons() {
		return v_descons;
	}
	public void setV_descons(String v_descons) {
		this.v_descons = v_descons;
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
	public Long getIdRequisito() {
		return idRequisito;
	}
	public void setIdRequisito(Long idRequisito) {
		this.idRequisito = idRequisito;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public Long getIdRequisitoPadre() {
		return idRequisitoPadre;
	}
	public void setIdRequisitoPadre(Long idRequisitoPadre) {
		this.idRequisitoPadre = idRequisitoPadre;
	}
	public String getTipoNorma() {
		return tipoNorma;
	}
	public void setTipoNorma(String tipoNorma) {
		this.tipoNorma = tipoNorma;
	}
	public Long getIdNorma() {
		return idNorma;
	}
	public void setIdNorma(Long idNorma) {
		this.idNorma = idNorma;
	}
	public String getDescripcionNorma() {
		return descripcionNorma;
	}
	public void setDescripcionNorma(String descripcionNorma) {
		this.descripcionNorma = descripcionNorma;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getNivel() {
		return nivel;
	}
	public void setNivel(Long nivel) {
		this.nivel = nivel;
	}
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public Long getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(Long idPadre) {
		this.idPadre = idPadre;
	}
	public Jerarquia getPadre() {
		return padre;
	}
	public void setPadre(Jerarquia padre) {
		this.padre = padre;
	}
	public EstadoConstante getEstado() {
		return estado;
	}
	public void setEstado(EstadoConstante estado) {
		this.estado = estado;
	}
	public BigDecimal getDisponible() {
		return disponible;
	}
	public void setDisponible(BigDecimal disponible) {
		this.disponible = disponible;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getIndicadorGrabar() {
		return indicadorGrabar;
	}
	public void setIndicadorGrabar(String indicadorGrabar) {
		this.indicadorGrabar = indicadorGrabar;
	}
	public String getIndicadorAgua() {
		return indicadorAgua;
	}
	public void setIndicadorAgua(String indicadorAgua) {
		this.indicadorAgua = indicadorAgua;
	}
	public Long getRetencionRevision() {
		return retencionRevision;
	}
	public void setRetencionRevision(Long retencionRevision) {
		this.retencionRevision = retencionRevision;
	}
	public Long getIdFicha() {
		return idFicha;
	}
	public void setIdFicha(Long idFicha) {
		this.idFicha = idFicha;
	}
	public FichaTecnica getFicha() {
		return ficha;
	}
	public void setFicha(FichaTecnica ficha) {
		this.ficha = ficha;
	}
	public List<Constante> getExtension() {
		return extension;
	}
	public void setExtension(List<Constante> extension) {
		this.extension = extension;
	}
	public String getTextoNodo() {
		return textoNodo;
	}
	public void setTextoNodo(String textoNodo) {
		this.textoNodo = textoNodo;
	}
	
	public Long getIdJerarquiaPadre() {
		return idJerarquiaPadre;
	}
	
	public void setIdJerarquiaPadre(Long idJerarquiaPadre) {
		this.idJerarquiaPadre = idJerarquiaPadre;
	}
	
	public Long getIdJerarquia() {
		return idJerarquia;
	}
	public void setIdJerarquia(Long idJerarquia) {
		this.idJerarquia = idJerarquia;
	}
	
	public List<String> getListaHijos() {
		return listaHijos;
	}
	public void setListaHijos(List<String> listaHijos) {
		this.listaHijos = listaHijos;
	}
	
	public String getDetAnterior() {
		return detAnterior;
	}
	public void setDetAnterior(String detAnterior) {
		this.detAnterior = detAnterior;
	}
	
	
	
	public String getDescDocumento() {
		return descDocumento;
	}
	public void setDescDocumento(String descDocumento) {
		this.descDocumento = descDocumento;
	}
	public String getNomAnterior() {
		return nomAnterior;
	}
	public void setNomAnterior(String nomAnterior) {
		this.nomAnterior = nomAnterior;
	}
	
	public Long getCantidadDocumentos() {
		return cantidadDocumentos;
	}
	public void setCantidadDocumentos(Long cantidadDocumentos) {
		this.cantidadDocumentos = cantidadDocumentos;
	}
	public Jerarquia() {
		super();
	}
	
	public Jerarquia(
			Long			id,
			Long			idTipo,
			String			descripcion,
			Long			nivel,
			Jerarquia		padre,
			BigDecimal		disponible
	) {
		super();
		this.id			= id;
		this.idTipo		= idTipo;
		this.descripcion= descripcion;
		//this.nivel		= nivel;
		this.padre		= padre;
		this.disponible	= disponible;
	}

}