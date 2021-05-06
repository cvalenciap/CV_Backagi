package pe.com.sedapal.agi.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CursoAuditor extends AuditoriaAGI {

	private Integer idCurso;
	private Integer idFichaAudi;
	private String nomCurso;
	private Integer indObli;
	private String stReg;

	public Integer getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(Integer idCurso) {
		this.idCurso = idCurso;
	}

	public Integer getIdFichaAudi() {
		return idFichaAudi;
	}

	public void setIdFichaAudi(Integer idFichaAudi) {
		this.idFichaAudi = idFichaAudi;
	}

	public String getNomCurso() {
		return nomCurso;
	}

	public void setNomCurso(String nomCurso) {
		this.nomCurso = nomCurso;
	}

	public Integer getIndObli() {
		return indObli;
	}

	public void setIndObli(Integer indObli) {
		this.indObli = indObli;
	}

	public String getStReg() {
		return stReg;
	}

	public void setStReg(String stReg) {
		this.stReg = stReg;
	}
	
	public static CursoAuditor mapperFromArray(String[] array) {
		if (array.length > 0) {
			CursoAuditor curso = new CursoAuditor();
			curso.setIdCurso(Integer.parseInt(array[0].trim()));
			curso.setNomCurso(array[1].trim());
			curso.setIndObli(Integer.parseInt(array[2].trim()));
			return curso;
		} else {
			return null;
		}
		
	}

}
