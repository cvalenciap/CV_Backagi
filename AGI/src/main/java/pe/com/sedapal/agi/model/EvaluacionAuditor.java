package pe.com.sedapal.agi.model;


public class EvaluacionAuditor {
	
	//Ítem	Nro Auditoría	Fecha Auditoría	Ficha Auditor	Auditor Evaluado	Rol
	
	    private Long idEvaluacionAuditor ;
	    private Auditoria auditoria;
	    private Colaborador colaborador;
	    	   
		private String fechaCreacion;
		private String fechaModificacion;
		private String usuarioCreador;
		private String usuarioModificador;	
		private String descripcionPregunta;			
		private String v_nomprg;				
			
		private Long idEvaluadi;
		private Long idPregAudi;
		private String respuestaNivelPreg;		
		private String estado;
	    
		Pregunta pregunta;
		
		private Long totalHoras;
		

		public Pregunta getPregunta() {
			return pregunta;
		}
		public void setPregunta(Pregunta pregunta) {
			this.pregunta = pregunta;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
		public Long getIdPregAudi() {
			return idPregAudi;
		}
		public void setIdPregAudi(Long idPregAudi) {
			this.idPregAudi = idPregAudi;
		}
		public Long getIdEvaluadi() {
			return idEvaluadi;
		}
		public void setIdEvaluadi(Long idEvaluadi) {
			this.idEvaluadi = idEvaluadi;
		}
		public String getRespuestaNivelPreg() {
			return respuestaNivelPreg;
		}
		public void setRespuestaNivelPreg(String respuestaNivelPreg) {
			this.respuestaNivelPreg = respuestaNivelPreg;
		}
		
		

		public String getV_nomprg() {
			return v_nomprg;
		}
		public void setV_nomprg(String v_nomprg) {
			this.v_nomprg = v_nomprg;
		}
		public String getDescripcionPregunta() {
			return descripcionPregunta;
		}
		public void setDescripcionPregunta(String descripcionPregunta) {
			this.descripcionPregunta = descripcionPregunta;
		}
		public Long getTotalHoras() {
			return totalHoras;
		}
		public void setTotalHoras(Long totalHoras) {
			this.totalHoras = totalHoras;
		}
		public Long getIdEvaluacionAuditor() {
			return idEvaluacionAuditor;
		}
		public void setIdEvaluacionAuditor(Long idEvaluacionAuditor) {
			this.idEvaluacionAuditor = idEvaluacionAuditor;
		}
		public Auditoria getAuditoria() {
			return auditoria;
		}
		public void setAuditoria(Auditoria auditoria) {
			this.auditoria = auditoria;
		}
		public Colaborador getColaborador() {
			return colaborador;
		}
		public void setColaborador(Colaborador colaborador) {
			this.colaborador = colaborador;
		}
		public String getFechaCreacion() {
			return fechaCreacion;
		}
		public void setFechaCreacion(String fechaCreacion) {
			this.fechaCreacion = fechaCreacion;
		}
		
		
		
		public String getFechaModificacion() {
			return fechaModificacion;
		}
		public void setFechaModificacion(String fechaModificacion) {
			this.fechaModificacion = fechaModificacion;
		}
		public String getUsuarioCreador() {
			return usuarioCreador;
		}
		public void setUsuarioCreador(String usuarioCreador) {
			this.usuarioCreador = usuarioCreador;
		}
		public String getUsuarioModificador() {
			return usuarioModificador;
		}
		public void setUsuarioModificador(String usuarioModificador) {
			this.usuarioModificador = usuarioModificador;
		}
	 
	 
	 

}
