package pe.com.sedapal.agi.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import pe.com.sedapal.agi.util.CastUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FichaAudi extends AuditoriaAGI {

	private Integer idFichaAudi;
	private GenericParam<String> tipoAuditor;
	private Integer ficha;
	private String nomAudi;
	private String apePaterno;
	private String apeMaterno;
	private String auditor;
	private String fecIngreso;
	private String annoExp;
	private String nomCargo;
	private String nomEmpre;
	private GenericParam<Integer> equipo;
	private GenericParam<Integer> gerencia;
	private GenericParam<String> rolAuditor;
	private GenericParam<String> tipoEducacion;
	private String evalAudi;
	private String stReg;

	private List<CursoNorma> cursosNorma;
	private List<CursoAuditor> cursosCompletados;
	private List<CursoAuditor> cursosPendientes;

	public Integer getIdFichaAudi() {
		return idFichaAudi;
	}

	public void setIdFichaAudi(Integer idFichaAudi) {
		this.idFichaAudi = idFichaAudi;
	}

	public GenericParam<String> getTipoAuditor() {
		return tipoAuditor;
	}

	public void setTipoAuditor(GenericParam<String> tipoAuditor) {
		this.tipoAuditor = tipoAuditor;
	}

	public Integer getFicha() {
		return ficha;
	}

	public void setFicha(Integer ficha) {
		this.ficha = ficha;
	}

	public String getNomAudi() {
		return nomAudi;
	}

	public void setNomAudi(String nomAudi) {
		this.nomAudi = nomAudi;
	}

	public String getApePaterno() {
		return apePaterno;
	}

	public void setApePaterno(String apePaterno) {
		this.apePaterno = apePaterno;
	}

	public String getApeMaterno() {
		return apeMaterno;
	}

	public void setApeMaterno(String apeMaterno) {
		this.apeMaterno = apeMaterno;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getFecIngreso() {
		return fecIngreso;
	}

	public void setFecIngreso(String fecIngreso) {
		this.fecIngreso = fecIngreso;
	}

	public String getAnnoExp() {
		return annoExp;
	}

	public void setAnnoExp(String annoExp) {
		this.annoExp = annoExp;
	}

	public String getNomCargo() {
		return nomCargo;
	}

	public void setNomCargo(String nomCargo) {
		this.nomCargo = nomCargo;
	}

	public String getNomEmpre() {
		return nomEmpre;
	}

	public void setNomEmpre(String nomEmpre) {
		this.nomEmpre = nomEmpre;
	}

	public GenericParam<Integer> getEquipo() {
		return equipo;
	}

	public void setEquipo(GenericParam<Integer> equipo) {
		this.equipo = equipo;
	}

	public GenericParam<Integer> getGerencia() {
		return gerencia;
	}

	public void setGerencia(GenericParam<Integer> gerencia) {
		this.gerencia = gerencia;
	}

	public GenericParam<String> getRolAuditor() {
		return rolAuditor;
	}

	public void setRolAuditor(GenericParam<String> rolAuditor) {
		this.rolAuditor = rolAuditor;
	}

	public GenericParam<String> getTipoEducacion() {
		return tipoEducacion;
	}

	public void setTipoEducacion(GenericParam<String> tipoEducacion) {
		this.tipoEducacion = tipoEducacion;
	}

	public String getEvalAudi() {
		return evalAudi;
	}

	public void setEvalAudi(String evalAudi) {
		this.evalAudi = evalAudi;
	}

	public String getStReg() {
		return stReg;
	}

	public void setStReg(String stReg) {
		this.stReg = stReg;
	}

	public List<CursoNorma> getCursosNorma() {
		return cursosNorma;
	}

	public void setCursosNorma(List<CursoNorma> cursosNorma) {
		this.cursosNorma = cursosNorma;
	}

	public List<CursoAuditor> getCursosCompletados() {
		return cursosCompletados;
	}

	public void setCursosCompletados(List<CursoAuditor> cursosCompletados) {
		this.cursosCompletados = cursosCompletados;
	}

	public List<CursoAuditor> getCursosPendientes() {
		return cursosPendientes;
	}

	public void setCursosPendientes(List<CursoAuditor> cursosPendientes) {
		this.cursosPendientes = cursosPendientes;
	}

	public static FichaAudi mapper(Map<String, Object> map) {
		FichaAudi fichaAuditor = new FichaAudi();

		fichaAuditor.setIdFichaAudi(CastUtil.leerValorMapInteger(map, "idFichaAudi"));

		GenericParam<String> tipoAuditor = new GenericParam<>();
		tipoAuditor.setCodigo(CastUtil.leerValorMapString(map, "idTipoAudi"));
		tipoAuditor.setDescripcion(CastUtil.leerValorMapString(map, "descTipoAudi"));
		fichaAuditor.setTipoAuditor(tipoAuditor);

		fichaAuditor.setFicha(CastUtil.leerValorMapInteger(map, "ficha"));
		fichaAuditor.setNomAudi(CastUtil.leerValorMapString(map, "nomAudi"));
		fichaAuditor.setApePaterno(CastUtil.leerValorMapString(map, "apePaterno"));
		fichaAuditor.setApeMaterno(CastUtil.leerValorMapString(map, "apeMaterno"));
		fichaAuditor.setAuditor(CastUtil.leerValorMapString(map, "auditor"));
		fichaAuditor.setFecIngreso(CastUtil.leerValorMapString(map, "fecIngreso"));
		fichaAuditor.setAnnoExp(CastUtil.leerValorMapString(map, "annoExp"));
		fichaAuditor.setNomCargo(CastUtil.leerValorMapString(map, "nomCargo"));
		fichaAuditor.setNomEmpre(CastUtil.leerValorMapString(map, "nomEmpre"));

		GenericParam<Integer> equipo = new GenericParam<>();
		equipo.setCodigo(CastUtil.leerValorMapInteger(map, "codEquipo"));
		equipo.setDescripcion(CastUtil.leerValorMapString(map, "nomEquipo"));
		fichaAuditor.setEquipo(equipo);

		GenericParam<Integer> gerencia = new GenericParam<>();
		gerencia.setCodigo(CastUtil.leerValorMapInteger(map, "codGerencia"));
		gerencia.setDescripcion(CastUtil.leerValorMapString(map, "nomGerencia"));
		fichaAuditor.setGerencia(gerencia);

		GenericParam<String> rolAuditor = new GenericParam<>();
		rolAuditor.setCodigo(CastUtil.leerValorMapString(map, "codRol"));
		rolAuditor.setDescripcion(CastUtil.leerValorMapString(map, "descRol"));
		fichaAuditor.setRolAuditor(rolAuditor);

		GenericParam<String> tipoEducacion = new GenericParam<>();
		tipoEducacion.setCodigo(CastUtil.leerValorMapString(map, "codEdu"));
		tipoEducacion.setDescripcion(CastUtil.leerValorMapString(map, "descEdu"));
		fichaAuditor.setTipoEducacion(tipoEducacion);

		fichaAuditor.setEvalAudi(CastUtil.leerValorMapString(map, "evalAudi"));
		fichaAuditor.setStReg(CastUtil.leerValorMapString(map, "stReg"));

		fichaAuditor.setCursosNorma(Arrays.asList(CastUtil.leerArregloCadena(map, "normas", "\r")).stream()
				.map(s -> s.trim().split("\t")).collect(Collectors.toList()).stream().map(CursoNorma::mapperFromArray)
				.collect(Collectors.toList()));
		
		fichaAuditor.setCursosCompletados(Arrays.asList(CastUtil.leerArregloCadena(map, "cursosllevados", "\r"))
				.stream().map(s -> s.trim().split("\t")).collect(Collectors.toList()).stream()
				.map(CursoAuditor::mapperFromArray).collect(Collectors.toList()));
		
		fichaAuditor.setCursosPendientes(Arrays.asList(CastUtil.leerArregloCadena(map, "cursosxllevar", "\r"))
				.stream().map(s -> s.trim().split("\t")).collect(Collectors.toList()).stream()
				.map(CursoAuditor::mapperFromArray).collect(Collectors.toList()));

		return fichaAuditor;
	}

	public static FichaAudi mappertoInfoAudi(Map<String, Object> map) {
		FichaAudi infoAuditor = new FichaAudi();

		infoAuditor.setFicha(CastUtil.leerValorMapInteger(map, "ficha"));
		infoAuditor.setNomAudi(CastUtil.leerValorMapString(map, "nombres"));
		infoAuditor.setApePaterno(CastUtil.leerValorMapString(map, "apePaterno"));
		infoAuditor.setApeMaterno(CastUtil.leerValorMapString(map, "apeMaterno"));
		infoAuditor.setAuditor(CastUtil.leerValorMapString(map, "nombreCompleto"));
		infoAuditor.setFecIngreso(CastUtil.leerValorMapString(map, "fecIngreso"));

		if (CastUtil.leerValorMapInteger(map, "exp") != null) {
			Integer anios = CastUtil.leerValorMapInteger(map, "exp");
			infoAuditor.setAnnoExp(anios > 1 ? anios + " años" : anios + " año");
		}

		GenericParam<Integer> equipo = new GenericParam<>();
		equipo.setCodigo(CastUtil.leerValorMapInteger(map, "codEquipo"));
		equipo.setDescripcion(CastUtil.leerValorMapString(map, "nomEquipo"));
		infoAuditor.setEquipo(equipo);

		GenericParam<Integer> gerencia = new GenericParam<>();
		gerencia.setCodigo(CastUtil.leerValorMapInteger(map, "codGerencia"));
		gerencia.setDescripcion(CastUtil.leerValorMapString(map, "nomGerencia"));
		infoAuditor.setGerencia(gerencia);

		infoAuditor.setNomCargo(CastUtil.leerValorMapString(map, "cargo"));

		return infoAuditor;
	}

}
