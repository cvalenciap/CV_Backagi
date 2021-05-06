package pe.com.sedapal.agi.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CursoNorma extends AuditoriaAGI {

	private Integer idCursoNorma;
	private Integer idFichaAudi;
	private Integer idNorma;
	private String nomNorma;
	private Integer indObli;
	private String stReg;

	public Integer getIdCursoNorma() {
		return idCursoNorma;
	}

	public void setIdCursoNorma(Integer idCursoNorma) {
		this.idCursoNorma = idCursoNorma;
	}

	public Integer getIdFichaAudi() {
		return idFichaAudi;
	}

	public void setIdFichaAudi(Integer idFichaAudi) {
		this.idFichaAudi = idFichaAudi;
	}

	public Integer getIdNorma() {
		return idNorma;
	}

	public void setIdNorma(Integer idNorma) {
		this.idNorma = idNorma;
	}

	public String getNomNorma() {
		return nomNorma;
	}

	public void setNomNorma(String nomNorma) {
		this.nomNorma = nomNorma;
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

	public static CursoNorma mapperFromArray(String[] array) {
		if (array.length > 0) {
			CursoNorma norma = new CursoNorma();
			norma.setIdCursoNorma(Integer.parseInt(array[0].trim()));
			norma.setIdNorma(Integer.parseInt(array[1].trim()));
			norma.setNomNorma(array[2].trim());
			norma.setIndObli(Integer.parseInt(array[3].trim()));
			return norma;
		} else {
			return null;
		}
	}

}
