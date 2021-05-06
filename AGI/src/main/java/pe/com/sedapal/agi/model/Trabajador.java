package pe.com.sedapal.agi.model;

import java.util.List;

import com.google.api.services.drive.model.File;

public class Trabajador {
	
	private Integer item;
	private Long idTrabajador;
	private Long nroFicha;
	private String nombreTrabajador;
	private String apePaternoTrabajador;
	private String apeMaternoTrabajador;
	private String cargoTrabajador;
	private Long idCapacitacion;
	private Long idEquipo;
	private Long idSesion;
	private Long idCurso;
	private String nomEquipo;
	private String idEstadoAsistencia;
	private String descripAsistencia;
	private String justificacion;
	private Long  itemColumna;
	private String rutaDocumento;
	private String nombreArchivo;
	private String archivoAntiguo;
	private Long nota;
	private String tipoEvaluacion;
	private String estadoEvaluacion;
	private Long idExamen;
	private Long indEnvio;
	private List<Long> listaEmpl;
	private String nomCurso;
	
	public Integer getItem() {
		return item;
	}
	public void setItem(Integer item) {
		this.item = item;
	}
	public Long getIdTrabajador() {
		return idTrabajador;
	}
	public void setIdTrabajador(Long idTrabajador) {
		this.idTrabajador = idTrabajador;
	}
	public Long getNroFicha() {
		return nroFicha;
	}
	public void setNroFicha(Long nroFicha) {
		this.nroFicha = nroFicha;
	}
	public String getNombreTrabajador() {
		return nombreTrabajador;
	}
	public void setNombreTrabajador(String nombreTrabajador) {
		this.nombreTrabajador = nombreTrabajador;
	}
	public String getApePaternoTrabajador() {
		return apePaternoTrabajador;
	}
	public void setApePaternoTrabajador(String apePaternoTrabajador) {
		this.apePaternoTrabajador = apePaternoTrabajador;
	}
	
	public String getApeMaternoTrabajador() {
		return apeMaternoTrabajador;
	}
	public void setApeMaternoTrabajador(String apeMaternoTrabajador) {
		this.apeMaternoTrabajador = apeMaternoTrabajador;
	}
	public String getCargoTrabajador() {
		return cargoTrabajador;
	}
	public void setCargoTrabajador(String cargoTrabajador) {
		this.cargoTrabajador = cargoTrabajador;
	}
	public String getNomEquipo() {
		return nomEquipo;
	}
	public void setNomEquipo(String nomEquipo) {
		this.nomEquipo = nomEquipo;
	}
	
	public String getIdEstadoAsistencia() {
		return idEstadoAsistencia;
	}
	public void setIdEstadoAsistencia(String idEstadoAsistencia) {
		this.idEstadoAsistencia = idEstadoAsistencia;
	}
	public String getDescripAsistencia() {
		return descripAsistencia;
	}
	public void setDescripAsistencia(String descripAsistencia) {
		this.descripAsistencia = descripAsistencia;
	}
	public String getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}
	public Long getIdCapacitacion() {
		return idCapacitacion;
	}
	public void setIdCapacitacion(Long idCapacitacion) {
		this.idCapacitacion = idCapacitacion;
	}
	public Long getIdEquipo() {
		return idEquipo;
	}
	public void setIdEquipo(Long idEquipo) {
		this.idEquipo = idEquipo;
	}
	public Long getIdSesion() {
		return idSesion;
	}
	public void setIdSesion(Long idSesion) {
		this.idSesion = idSesion;
	}
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public Long getItemColumna() {
		return itemColumna;
	}
	public void setItemColumna(Long itemColumna) {
		this.itemColumna = itemColumna;
	}
	public String getRutaDocumento() {
		return rutaDocumento;
	}
	public void setRutaDocumento(String rutaDocumento) {
		this.rutaDocumento = rutaDocumento;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getArchivoAntiguo() {
		return archivoAntiguo;
	}
	public void setArchivoAntiguo(String archivoAntiguo) {
		this.archivoAntiguo = archivoAntiguo;
	}
	public Long getNota() {
		return nota;
	}
	public void setNota(Long nota) {
		this.nota = nota;
	}
	public String getTipoEvaluacion() {
		return tipoEvaluacion;
	}
	public void setTipoEvaluacion(String tipoEvaluacion) {
		this.tipoEvaluacion = tipoEvaluacion;
	}
	public String getEstadoEvaluacion() {
		return estadoEvaluacion;
	}
	public void setEstadoEvaluacion(String estadoEvaluacion) {
		this.estadoEvaluacion = estadoEvaluacion;
	}
	public Long getIdExamen() {
		return idExamen;
	}
	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}
	public Long getIndEnvio() {
		return indEnvio;
	}
	public void setIndEnvio(Long indEnvio) {
		this.indEnvio = indEnvio;
	}
	public List<Long> getListaEmpl() {
		return listaEmpl;
	}
	public void setListaEmpl(List<Long> listaEmpl) {
		this.listaEmpl = listaEmpl;
	}
	public String getNomCurso() {
		return nomCurso;
	}
	public void setNomCurso(String nomCurso) {
		this.nomCurso = nomCurso;
	}
}
