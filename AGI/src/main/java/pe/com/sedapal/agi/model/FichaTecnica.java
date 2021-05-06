package pe.com.sedapal.agi.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class FichaTecnica {
	
	Long					id;
	Long					idTipoProceso;
	String					tipoProceso;
	Long					idNivel;
	String					nivel;
	String					proceso;
	Long					idResponsable;
	String					responsable;
	String					objetivo;
	String					alcance;
	String					requisitos;
	String					proveedores;
	String					entradas;
	String					subProceso;
	String					salidas;
	String					clientes;
	String					personal;
	String					equipos;
	String					materiales;
	String					ambientes;
	String					documentosAplicado;
	String					controles;
	String					registros;
	String					indicadores;
	Long					idElaborado;
	String					elaborado;
	Long					idAprobado;
	String					aprobado;
	Long					idConsensado;
	String					consensado;
	EstadoConstante			estado;
	BigDecimal				disponible;
	Long					idJera;
	String 					rutaGrafico;
	String 					nombreGrafico;
	public String getRutaGrafico() {
		return rutaGrafico;
	}
	public void setRutaGrafico(String rutaGrafico) {
		this.rutaGrafico = rutaGrafico;
	}
	public String getNombreGrafico() {
		return nombreGrafico;
	}
	public void setNombreGrafico(String nombreGrafico) {
		this.nombreGrafico = nombreGrafico;
	}

	private ArrayList<FichaTecnicaDocumento> fichaTecnicaDocumento;
		
	public ArrayList<FichaTecnicaDocumento> getFichaTecnicaDocumento() {
		return fichaTecnicaDocumento;
	}
	public void setFichaTecnicaDocumento(ArrayList<FichaTecnicaDocumento> fichaTecnicaDocumento) {
		this.fichaTecnicaDocumento = fichaTecnicaDocumento;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdTipoProceso() {
		return idTipoProceso;
	}
	public void setIdTipoProceso(Long idTipoProceso) {
		this.idTipoProceso = idTipoProceso;
	}
	public String getTipoProceso() {
		return tipoProceso;
	}
	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}
	public Long getIdNivel() {
		return idNivel;
	}
	public void setIdNivel(Long idNivel) {
		this.idNivel = idNivel;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	public Long getIdResponsable() {
		return idResponsable;
	}
	public void setIdResponsable(Long idResponsable) {
		this.idResponsable = idResponsable;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public String getRequisitos() {
		return requisitos;
	}
	public void setRequisitos(String requisitos) {
		this.requisitos = requisitos;
	}
	public String getProveedores() {
		return proveedores;
	}
	public void setProveedores(String proveedores) {
		this.proveedores = proveedores;
	}
	public String getEntradas() {
		return entradas;
	}
	public void setEntradas(String entradas) {
		this.entradas = entradas;
	}
	public String getSubProceso() {
		return subProceso;
	}
	public void setSubProceso(String subProceso) {
		this.subProceso = subProceso;
	}
	public String getSalidas() {
		return salidas;
	}
	public void setSalidas(String salidas) {
		this.salidas = salidas;
	}
	public String getClientes() {
		return clientes;
	}
	public void setClientes(String clientes) {
		this.clientes = clientes;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getEquipos() {
		return equipos;
	}
	public void setEquipos(String equipos) {
		this.equipos = equipos;
	}
	public String getMateriales() {
		return materiales;
	}
	public void setMateriales(String materiales) {
		this.materiales = materiales;
	}
	public String getAmbientes() {
		return ambientes;
	}
	public void setAmbientes(String ambientes) {
		this.ambientes = ambientes;
	}
	public String getDocumentosAplicado() {
		return documentosAplicado;
	}
	public void setDocumentosAplicado(String documentosAplicado) {
		this.documentosAplicado = documentosAplicado;
	}
	public String getControles() {
		return controles;
	}
	public void setControles(String controles) {
		this.controles = controles;
	}
	public String getRegistros() {
		return registros;
	}
	public void setRegistros(String registros) {
		this.registros = registros;
	}
	public String getIndicadores() {
		return indicadores;
	}
	public void setIndicadores(String indicadores) {
		this.indicadores = indicadores;
	}
	public Long getIdElaborado() {
		return idElaborado;
	}
	public void setIdElaborado(Long idElaborado) {
		this.idElaborado = idElaborado;
	}
	public String getElaborado() {
		return elaborado;
	}
	public void setElaborado(String elaborado) {
		this.elaborado = elaborado;
	}
	public Long getIdAprobado() {
		return idAprobado;
	}
	public void setIdAprobado(Long idAprobado) {
		this.idAprobado = idAprobado;
	}
	public String getAprobado() {
		return aprobado;
	}
	public void setAprobado(String aprobado) {
		this.aprobado = aprobado;
	}
	public Long getIdConsensado() {
		return idConsensado;
	}
	public void setIdConsensado(Long idConsensado) {
		this.idConsensado = idConsensado;
	}
	public String getConsensado() {
		return consensado;
	}
	public void setConsensado(String consensado) {
		this.consensado = consensado;
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

	public FichaTecnica() {
		super();
	}
	
	public Long getIdJera() {
		return idJera;
	}
	public void setIdJera(Long idJera) {
		this.idJera = idJera;
	}
	
	public FichaTecnica(
			Long		id,
			Long		idTipoProceso,
			Long		idNivel,
			BigDecimal	disponible
	) {
		super();
		this.id				= id;
		this.idTipoProceso	= idTipoProceso;
		this.idNivel		= idNivel;
		this.disponible		= disponible;
	}

}