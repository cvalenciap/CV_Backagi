package pe.com.sedapal.agi.model;

import java.util.Date;
import java.util.List;

public class Encuesta {
	
	private Long nidcurs;
	private Long nidencu;
	private String avusucre;
	private Date adfeccre;
	private String avusumod;
	private Date adfecmod;
	private String avnomprg;
	private Long ndisencu;
	private String vcodencu;
	private String vnomencu;
	
	private List<DetEncuesta> listaDetEncuesta;
	private String vdescur;
	private String v_cod_cur;
	
	
	
	public String getV_cod_cur() {
		return v_cod_cur;
	}
	public void setV_cod_cur(String v_cod_cur) {
		this.v_cod_cur = v_cod_cur;
	}
	public List<DetEncuesta> getListaDetEncuesta() {
		return listaDetEncuesta;
	}
	public void setListaDetEncuesta(List<DetEncuesta> listaDetEncuesta) {
		this.listaDetEncuesta = listaDetEncuesta;
	}
	public String getVdescur() {
		return vdescur;
	}
	public void setVdescur(String vdescur) {
		this.vdescur = vdescur;
	}
	public Long getNidcurs() {
		return nidcurs;
	}
	public void setNidcurs(Long nidcurs) {
		this.nidcurs = nidcurs;
	}
	public Long getNidencu() {
		return nidencu;
	}
	public void setNidencu(Long nidencu) {
		this.nidencu = nidencu;
	}
	public String getAvusucre() {
		return avusucre;
	}
	public void setAvusucre(String avusucre) {
		this.avusucre = avusucre;
	}
	public Date getAdfeccre() {
		return adfeccre;
	}
	public void setAdfeccre(Date adfeccre) {
		this.adfeccre = adfeccre;
	}
	public String getAvusumod() {
		return avusumod;
	}
	public void setAvusumod(String avusumod) {
		this.avusumod = avusumod;
	}
	public Date getAdfecmod() {
		return adfecmod;
	}
	public void setAdfecmod(Date adfecmod) {
		this.adfecmod = adfecmod;
	}
	public String getAvnomprg() {
		return avnomprg;
	}
	public void setAvnomprg(String avnomprg) {
		this.avnomprg = avnomprg;
	}
	public Long getNdisencu() {
		return ndisencu;
	}
	public void setNdisencu(Long ndisencu) {
		this.ndisencu = ndisencu;
	}
	public String getVcodencu() {
		return vcodencu;
	}
	public void setVcodencu(String vcodencu) {
		this.vcodencu = vcodencu;
	}
	public String getVnomencu() {
		return vnomencu;
	}
	public void setVnomencu(String vnomencu) {
		this.vnomencu = vnomencu;
	}
	
	
	
}
