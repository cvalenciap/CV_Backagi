package pe.com.sedapal.agi.model;

import java.util.List;

public class AreaParametros {

	private List<AreaAuditoria> lstAreaAuditoria;
	private List<AreaAlcanceAuditoria> lstAreaAlcanceAuditoria;
	private List<AreaCargoAuditoria> lstAreaCargoAuditoria;
	private List<CargoSig> lstCargoSig;
	private List<AreaAlcance> lstAlcances;
	private List<Norma> lstNormas;
	private List<AreaNormaAuditoria> lstAreaNormaAuditoria;
	private List<AreaNormaDet> lstAreaNormaDet;

	public List<AreaAuditoria> getLstAreaAuditoria() {
		return lstAreaAuditoria;
	}

	public void setLstAreaAuditoria(List<AreaAuditoria> lstAreaAuditoria) {
		this.lstAreaAuditoria = lstAreaAuditoria;
	}

	public List<AreaAlcanceAuditoria> getLstAreaAlcanceAuditoria() {
		return lstAreaAlcanceAuditoria;
	}

	public void setLstAreaAlcanceAuditoria(List<AreaAlcanceAuditoria> lstAreaAlcanceAuditoria) {
		this.lstAreaAlcanceAuditoria = lstAreaAlcanceAuditoria;
	}

	public List<AreaCargoAuditoria> getLstAreaCargoAuditoria() {
		return lstAreaCargoAuditoria;
	}

	public void setLstAreaCargoAuditoria(List<AreaCargoAuditoria> lstAreaCargoAuditoria) {
		this.lstAreaCargoAuditoria = lstAreaCargoAuditoria;
	}

	public List<CargoSig> getLstCargoSig() {
		return lstCargoSig;
	}

	public void setLstCargoSig(List<CargoSig> lstCargoSig) {
		this.lstCargoSig = lstCargoSig;
	}

	public List<AreaAlcance> getLstAlcances() {
		return lstAlcances;
	}

	public void setLstAlcances(List<AreaAlcance> lstAlcances) {
		this.lstAlcances = lstAlcances;
	}

	public List<Norma> getLstNormas() {
		return lstNormas;
	}

	public void setLstNormas(List<Norma> lstNormas) {
		this.lstNormas = lstNormas;
	}

	public List<AreaNormaAuditoria> getLstAreaNormaAuditoria() {
		return lstAreaNormaAuditoria;
	}

	public void setLstAreaNormaAuditoria(List<AreaNormaAuditoria> lstAreaNormaAuditoria) {
		this.lstAreaNormaAuditoria = lstAreaNormaAuditoria;
	}

	public List<AreaNormaDet> getLstAreaNormaDet() {
		return lstAreaNormaDet;
	}

	public void setLstAreaNormaDet(List<AreaNormaDet> lstAreaNormaDet) {
		this.lstAreaNormaDet = lstAreaNormaDet;
	}

}
