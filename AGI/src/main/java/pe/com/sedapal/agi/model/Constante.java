package pe.com.sedapal.agi.model;


import java.util.List;

import pe.com.sedapal.agi.model.enums.EstadoConstante;

public class Constante {
	Long idconstante;
	Long idconstantefaseact;
	Long idconstantesuper;
	String v_nomconssupe;
	String v_nomcons;
	String v_descons;
	String v_desconsfaseact;
	String v_valcons;
	String v_abrecons;
	String v_campcons1;
	String v_campcons2;
	String v_campcons3;
	String v_campcons4;
	String v_campcons5;
	String v_campcons6;
	String v_campcons7;
	String v_campcons8;
	Long n_discons;
	Long itemColumna;
	String dispEstado;
	List<Constante> listaDetalle;
	//String tipo;
	//Long rnum;
	//Long result_count;	
	
	/*public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}*/

	String v_nomprg;
	Long n_idconssupe;
	

	
	public String getV_nomprg() {
		return v_nomprg;
	}
	public void setV_nomprg(String v_nomprg) {
		this.v_nomprg = v_nomprg;
	}
	
	public Long getIdconstantefaseact() {
		return idconstantefaseact;
	}
	public void setIdconstantefaseact(Long idconstantefaseact) {
		this.idconstantefaseact = idconstantefaseact;
	}
	public String getV_desconsfaseact() {
		return v_desconsfaseact;
	}
	public void setV_desconsfaseact(String v_desconsfaseact) {
		this.v_desconsfaseact = v_desconsfaseact;
	}
	public Long getN_idconssupe() {
		return n_idconssupe;
	}
	public void setN_idconssupe(Long n_idconssupe) {
		this.n_idconssupe = n_idconssupe;
	}

	Long n_idcons;
	public Long getN_idcons() {
		return n_idcons;
	}
	public void setN_idcons(Long n_idcons) {
		this.n_idcons = n_idcons;
	}

	private EstadoConstante estadoConstante;
	
		
	public EstadoConstante getEstadoConstante() {
		return estadoConstante;
	}
	public void setEstadoConstante(EstadoConstante estadoconstante) {
		this.estadoConstante = estadoconstante;
	}

	public Long getIdconstante() {
		return idconstante;
	}

	public void setIdconstante(Long idconstante) {
		this.idconstante = idconstante;
	}

	public Long getIdconstantesuper() {
		return idconstantesuper;
	}

	public void setIdconstantesuper(Long idconstantesuper) {
		this.idconstantesuper = idconstantesuper;
	}

	public String getV_nomconssupe() {
		return v_nomconssupe;
	}

	public void setV_nomconssupe(String v_nomconssupe) {
		this.v_nomconssupe = v_nomconssupe;
	}

	public String getV_nomcons() {
		return v_nomcons;
	}

	public void setV_nomcons(String v_nomcons) {
		this.v_nomcons = v_nomcons;
	}

	public String getV_descons() {
		return v_descons;
	}

	public void setV_descons(String v_descons) {
		this.v_descons = v_descons;
	}

	public String getV_valcons() {
		return v_valcons;
	}

	public void setV_valcons(String v_valcons) {
		this.v_valcons = v_valcons;
	}

	public String getV_abrecons() {
		return v_abrecons;
	}

	public void setV_abrecons(String v_abrecons) {
		this.v_abrecons = v_abrecons;
	}

	public String getV_campcons1() {
		return v_campcons1;
	}

	public void setV_campcons1(String v_campcons1) {
		this.v_campcons1 = v_campcons1;
	}

	public String getV_campcons2() {
		return v_campcons2;
	}

	public void setV_campcons2(String v_campcons2) {
		this.v_campcons2 = v_campcons2;
	}
	
	public String getV_campcons3() {
		return v_campcons3;
	}

	public void setV_campcons3(String v_campcons3) {
		this.v_campcons3 = v_campcons3;
	}

	public String getV_campcons4() {
		return v_campcons4;
	}

	public void setV_campcons4(String v_campcons4) {
		this.v_campcons4 = v_campcons4;
	}

	public String getV_campcons5() {
		return v_campcons5;
	}

	public void setV_campcons5(String v_campcons5) {
		this.v_campcons5 = v_campcons5;
	}

	public String getV_campcons6() {
		return v_campcons6;
	}

	public void setV_campcons6(String v_campcons6) {
		this.v_campcons6 = v_campcons6;
	}

	public String getV_campcons7() {
		return v_campcons7;
	}

	public void setV_campcons7(String v_campcons7) {
		this.v_campcons7 = v_campcons7;
	}

	public String getV_campcons8() {
		return v_campcons8;
	}

	public void setV_campcons8(String v_campcons8) {
		this.v_campcons8 = v_campcons8;
	}

	public Long getN_discons() {
		return n_discons;
	}

	public void setN_discons(Long n_discons) {
		this.n_discons = n_discons;
	}
	
	
	/*public Long getRnum() {
		return rnum;
	}

	public void setRnum(Long rnum) {
		this.rnum = rnum;
	}

	public Long getResult_count() {
		return result_count;
	}

	public void setResult_count(Long result_count) {
		this.result_count = result_count;
	}*/

	public Long getItemColumna() {
		return itemColumna;
	}
	public void setItemColumna(Long itemColumna) {
		this.itemColumna = itemColumna;
	}
	
	
	public List<Constante> getListaDetalle() {
		return listaDetalle;
	}
	public void setListaDetalle(List<Constante> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}
	
	public String getDispEstado() {
		return dispEstado;
	}
	public void setDispEstado(String dispEstado) {
		this.dispEstado = dispEstado;
	}
	public Constante() {
		super();
	}
	
	public Constante(Long idconstante,Long idconstantesuper,  String v_nomconssupe,	String v_nomcons,	String v_descons, String v_valcons,String v_abrecons,	String v_campcons1,	String v_campcons2,	String v_campcons3,	String v_campcons4,	String v_campcons5,	String v_campcons6,	String v_campcons7,	String v_campcons8,	Long n_discons, String v_nomprg, Long n_idconssupe, Long n_idcons, Long itemColumna, List<Constante> listaDetalle,String dispEstado/*,Long rnum, Long result_count*/ 
) {
		super();
		this.idconstante = idconstante;
		this.idconstantesuper=idconstantesuper; 
		this.v_nomconssupe=v_nomconssupe;       
		this.v_nomcons=v_nomcons;             
		this.v_descons=	v_descons;              
		this.v_valcons=v_valcons;             
		this.v_abrecons=v_abrecons;           
		this.v_campcons1=v_campcons1;         
		this.v_campcons2=v_campcons2;         
		this.v_campcons3=v_campcons3;         
		this.v_campcons4=v_campcons4;         
		this.v_campcons5=v_campcons5;         
		this.v_campcons6=v_campcons6;         
		this.v_campcons7=v_campcons7;         
		this.v_campcons8=v_campcons8;         
		this.n_discons=n_discons;
		this.v_nomprg=v_nomprg;
		this.n_idconssupe=n_idconssupe;
		this.n_idcons=n_idcons;
		this.itemColumna=itemColumna;
		this.listaDetalle=listaDetalle;
		this.dispEstado=dispEstado;
	}
}
