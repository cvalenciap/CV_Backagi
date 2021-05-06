package pe.com.sedapal.agi.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Colaborador {
	
	Long			idColaborador;
	Long			numeroFicha;
	String			nombre;
	String			apellidoPaterno;
	String			apellidoMaterno;
	String			nombreCompleto;
	String			funcion;
	Equipo			equipo;
	String          equipoColaborador;
	EstadoConstante	estadoDisponible;
	Constante		tipo;
	String			login;
	String			estado;
	String			tipoDocumentoIdentidad;
	String			numeroDocumentoIdentidad;
	Constante		rolAuditor;
	String			experiencia;
	String			educacion;
	Date			fechaIngreso;
	String			tiempoExperiencia;
	String			evaluacion;
	Long			ciclo;
	String			comentario;
	Long			plazo;
	Date			fechaPlazo;
	Date			fechaLiberacion;
	Long			idHistorial;
	Long			disponible;
	Long            idRolAuditor;
	Documento 		documento;
	String 			dni;

	@JsonIgnore
	private Long numeroRegistros;
	Long			idRevision;
	Long			iteracion;
	
	
	
	//String equipo;
    //String	estado;
    //String funcion;
    Long idFase;
    String nombreFase;
    String idRuta;
    //String plazo;
    Long prioridad;
    String responsable;
    Long indicadorRechazo;
    Date fechaRechazo;
    String estadoSoli;
    Long   idsolicitud;
    Long indicadorTrabajador;
    Long indicadorEquipo;
    Long indicadorFuncion;
    Long indicadorJefe;
    Long indicadorJefeActual;
    Long idTrabajador;
    Boolean estiloBloqueado;
    String textoBloqueado;
    
	public Long getIndicadorJefe() {
		return indicadorJefe;
	}
	public void setIndicadorJefe(Long indicadorJefe) {
		this.indicadorJefe = indicadorJefe;
	}
	public Long getIndicadorJefeActual() {
		return indicadorJefeActual;
	}
	public void setIndicadorJefeActual(Long indicadorJefeActual) {
		this.indicadorJefeActual = indicadorJefeActual;
	}
	public Boolean getEstiloBloqueado() {
		return estiloBloqueado;
	}
	public void setEstiloBloqueado(Boolean estiloBloqueado) {
		this.estiloBloqueado = estiloBloqueado;
	}
	public String getTextoBloqueado() {
		return textoBloqueado;
	}
	public void setTextoBloqueado(String textoBloqueado) {
		this.textoBloqueado = textoBloqueado;
	}
	public Long getIdTrabajador() {
		return idTrabajador;
	}
	public void setIdTrabajador(Long idTrabajador) {
		this.idTrabajador = idTrabajador;
	}
	public Long getIndicadorTrabajador() {
		return indicadorTrabajador;
	}
	public void setIndicadorTrabajador(Long indicadorTrabajador) {
		this.indicadorTrabajador = indicadorTrabajador;
	}
	public Long getIndicadorEquipo() {
		return indicadorEquipo;
	}
	public void setIndicadorEquipo(Long indicadorEquipo) {
		this.indicadorEquipo = indicadorEquipo;
	}
	public Long getIndicadorFuncion() {
		return indicadorFuncion;
	}
	public void setIndicadorFuncion(Long indicadorFuncion) {
		this.indicadorFuncion = indicadorFuncion;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public Long getIdsolicitud() {
		return idsolicitud;
	}
	public void setIdsolicitud(Long idsolicitud) {
		this.idsolicitud = idsolicitud;
	}


	public Long getIndicadorRechazo() {
		return indicadorRechazo;
	}
	public void setIndicadorRechazo(Long indicadorRechazo) {
		this.indicadorRechazo = indicadorRechazo;
	}
	public Date getFechaRechazo() {
		return fechaRechazo;
	}
	public void setFechaRechazo(Date fechaRechazo) {
		this.fechaRechazo = fechaRechazo;
	}
  
	

	public String getEstadoSoli() {
		return estadoSoli;
	}
	public void setEstadoSoli(String estadoSoli) {
		this.estadoSoli = estadoSoli;
	}
	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	public String getEquipoColaborador() {
		return equipoColaborador;
	}
	public void setEquipoColaborador(String equipoColaborador) {
		this.equipoColaborador = equipoColaborador;
	}
	public Long getIdFase() {
		return idFase;
	}
	public void setIdFase(Long idFase) {
		this.idFase = idFase;
	}
	public String getNombreFase() {
		return nombreFase;
	}
	public void setNombreFase(String nombreFase) {
		this.nombreFase = nombreFase;
	}
	public String getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(String idRuta) {
		this.idRuta = idRuta;
	}
	public Long getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(Long prioridad) {
		this.prioridad = prioridad;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public Long getIdRolAuditor() {
		return idRolAuditor;
	}
	public void setIdRolAuditor(Long idRolAuditor) {
		this.idRolAuditor = idRolAuditor;
	}
	public Long getIdColaborador() {
		return idColaborador;
	}
	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
	public Long getNumeroFicha() {
		return numeroFicha;
	}
	public void setNumeroFicha(Long numeroFicha) {
		this.numeroFicha = numeroFicha;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getFuncion() {
		return funcion;
	}
	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
	public Equipo getEquipo() {
		return equipo;
	}
	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
	public EstadoConstante getEstadoDisponible() {
		return estadoDisponible;
	}
	public void setEstadoDisponible(EstadoConstante estadoDisponible) {
		this.estadoDisponible = estadoDisponible;
	}
	public Constante getTipo() {
		return tipo;
	}
	public void setTipo(Constante tipo) {
		this.tipo = tipo;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipoDocumentoIdentidad() {
		return tipoDocumentoIdentidad;
	}
	public void setTipoDocumentoIdentidad(String tipoDocumentoIdentidad) {
		this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
	}
	public String getNumeroDocumentoIdentidad() {
		return numeroDocumentoIdentidad;
	}
	public void setNumeroDocumentoIdentidad(String numeroDocumentoIdentidad) {
		this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
	}
	public Constante getRolAuditor() {
		return rolAuditor;
	}
	public void setRolAuditor(Constante rolAuditor) {
		this.rolAuditor = rolAuditor;
	}
	public String getExperiencia() {
		return experiencia;
	}
	public void setExperiencia(String experiencia) {
		this.experiencia = experiencia;
	}
	public String getEducacion() {
		return educacion;
	}
	public void setEducacion(String educacion) {
		this.educacion = educacion;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getTiempoExperiencia() {
		return tiempoExperiencia;
	}
	public void setTiempoExperiencia(String tiempoExperiencia) {
		this.tiempoExperiencia = tiempoExperiencia;
	}
	public String getEvaluacion() {
		return evaluacion;
	}
	public void setEvaluacion(String evaluacion) {
		this.evaluacion = evaluacion;
	}
	public Long getCiclo() {
		return ciclo;
	}
	public void setCiclo(Long ciclo) {
		this.ciclo = ciclo;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Long getPlazo() {
		return plazo;
	}
	public void setPlazo(Long plazo) {
		this.plazo = plazo;
	}
	public Date getFechaPlazo() {
		return fechaPlazo;
	}
	public void setFechaPlazo(Date fechaPlazo) {
		this.fechaPlazo = fechaPlazo;
	}
	public Date getFechaLiberacion() {
		return fechaLiberacion;
	}
	public void setFechaLiberacion(Date fechaLiberacion) {
		this.fechaLiberacion = fechaLiberacion;
	}
	public Long getIdHistorial() {
		return idHistorial;
	}
	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}
	public Long getIdRevision() {
		return idRevision;
	}
	public void setIdRevision(Long idRevision) {
		this.idRevision = idRevision;
	}
	public Long getIteracion() {
		return iteracion;
	}
	public void setIteracion(Long iteracion) {
		this.iteracion = iteracion;
	}
	public Long getDisponible() {
		return disponible;
	}
	public void setDisponible(Long disponible) {
		this.disponible = disponible;
	}

	public Long getNumeroRegistros() {
		return numeroRegistros;
	}

	public void setNumeroRegistros(Long numeroRegistros) {
		this.numeroRegistros = numeroRegistros;
	}

	public Colaborador() {
		super();
	}
	
	public Colaborador(
		Long			idColaborador,
		String			nombre,
		String			apellidoPaterno,
		String			apellidoMaterno,
		String			funcion,
		Equipo			equipo,
		EstadoConstante	estadoDisponible
	) {
		super();
		this.idColaborador		= idColaborador;
		this.nombre				= nombre;
		this.apellidoPaterno	= apellidoPaterno;
		this.apellidoMaterno	= apellidoMaterno;
		this.funcion			= funcion;
		this.equipo				= equipo;
		this.estadoDisponible	= estadoDisponible;
	}
	
	private String descripcionEquipo;
	private Long itemColumnaPart;
	private Long numFicha;
	private Long indEnvio;

	public String getDescripcionEquipo() {
		return descripcionEquipo;
	}
	public void setDescripcionEquipo(String descripcionEquipo) {
		this.descripcionEquipo = descripcionEquipo;
	}
	public Long getItemColumnaPart() {
		return itemColumnaPart;
	}
	public void setItemColumnaPart(Long itemColumnaPart) {
		this.itemColumnaPart = itemColumnaPart;
	}
	public Long getNumFicha() {
		return numFicha;
	}
	public void setNumFicha(Long numFicha) {
		this.numFicha = numFicha;
	}
	public Long getIndEnvio() {
		return indEnvio;
	}
	public void setIndEnvio(Long indEnvio) {
		this.indEnvio = indEnvio;
	}
	
}