package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;


public class SolicitudCopia {	      
		private Long tipoCopia;		
		private Long numero;
		private String descripcion;
		private Long gerencia;
		//private String codigo;
		private Date fecha;
		private Long alcance;
		private Long proceso;
		private String sustento;	
		private Long motivoR;
		private List<Colaborador> listaParticipante;    
		private Long id;
		private Long indicadorestado;
		private String resumenCritica;
		
		
		private Long nmotivo;   //sustento
		private Long numtipoestasoli; //tipocopia
		private String observa;     //
		private Long  nestcopi;
		private Long nidrevision;
		private Long idUsuAprobador;
		
		
		
		public Long getNidrevision() {
			return nidrevision;
		}
		public void setNidrevision(Long nidrevision) {
			this.nidrevision = nidrevision;
		}
		public Long getNestcopi() {
			return nestcopi;
		}
		public void setNestcopi(Long nestcopi) {
			this.nestcopi = nestcopi;
		}
		public Long getNmotivo() {
			return nmotivo;
		}
		public void setNmotivo(Long nmotivo) {
			this.nmotivo = nmotivo;
		}
		public Long getNumtipoestasoli() {
			return numtipoestasoli;
		}
		public void setNumtipoestasoli(Long numtipoestasoli) {
			this.numtipoestasoli = numtipoestasoli;
		}
		public String getObserva() {
			return observa;
		}
		public void setObserva(String observa) {
			this.observa = observa;
		}
		public String getResumenCritica() {
			return resumenCritica;
		}
		public void setResumenCritica(String resumenCritica) {
			this.resumenCritica = resumenCritica;
		}
		public Long getIndicadorestado() {
			return indicadorestado;
		}
		public void setIndicadorestado(Long indicadorestado) {
			this.indicadorestado = indicadorestado;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public List<Colaborador> getListaParticipante() {
			return listaParticipante;
		}
		public void setListaParticipante(List<Colaborador> listaParticipante) {
			this.listaParticipante = listaParticipante;
		}
		public Long getTipoCopia() {
			return tipoCopia;
		}
		public void setTipoCopia(Long tipoCopia) {
			this.tipoCopia = tipoCopia;
		}
		public Long getNumero() {
			return numero;
		}
		public void setNumero(Long numero) {
			this.numero = numero;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public Long getGerencia() {
			return gerencia;
		}
		public void setGerencia(Long gerencia) {
			this.gerencia = gerencia;
		}
		public Date getFecha() {
			return fecha;
		}
		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}
		public Long getAlcance() {
			return alcance;
		}
		public void setAlcance(Long alcance) {
			this.alcance = alcance;
		}
		public Long getProceso() {
			return proceso;
		}
		public void setProceso(Long proceso) {
			this.proceso = proceso;
		}
		public String getSustento() {
			return sustento;
		}
		public void setSustento(String sustento) {
			this.sustento = sustento;
		}
		public Long getMotivoR() {
			return motivoR;
		}
		public void setMotivoR(Long motivoR) {
			this.motivoR = motivoR;
		}
		public Long getIdUsuAprobador() {
			return idUsuAprobador;
		}
		public void setIdUsuAprobador(Long idUsuAprobador) {
			this.idUsuAprobador = idUsuAprobador;
		}

		
		
	
}
