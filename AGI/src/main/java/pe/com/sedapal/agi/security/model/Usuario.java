package pe.com.sedapal.agi.security.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 8320778498372937044L;
	
	private String codUsuario;
	private String nombUsuario;
	private Integer codArea;
	private String descArea;
	private Integer codFicha;
	private Integer codPerfil; // perfil activo
	private String descPerfil; // perfil activo
	private String abrevArea;
	private List<Perfil> perfiles;
	private List<String> permisos;

	
	public Usuario() {
		super();
		this.perfiles = new ArrayList<Perfil>();
		this.permisos = new ArrayList<String>();
	}
	public Usuario(String codUsuario, String nombreUsuario, Integer codArea, String descArea, Integer codFicha, String abrevArea) {
		super();
		this.codUsuario = codUsuario;
		this.nombUsuario = nombreUsuario;
		this.codArea = codArea;
		this.descArea = descArea;
		this.codFicha = codFicha;
		this.abrevArea = abrevArea;
		this.perfiles = new ArrayList<Perfil>();
		this.permisos = new ArrayList<String>();
	}
	public Usuario(String codUsuario, String nombreUsuario, Integer codArea, String descArea, Integer codFicha,
			Integer codPerfil, String descPerfil, String abrevArea) {
		super();
		this.codUsuario = codUsuario;
		this.nombUsuario = nombreUsuario;
		this.codArea = codArea;
		this.descArea = descArea;
		this.codFicha = codFicha;
		this.codPerfil = codPerfil;
		this.descPerfil = descPerfil;
		this.abrevArea = abrevArea;
		this.perfiles = new ArrayList<Perfil>();
		this.permisos = new ArrayList<String>();
	}
	
	public String getCodUsuario() {
		return codUsuario;
	}
	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}
	
	public String getNombUsuario() {
		return nombUsuario;
	}
	public void setNombUsuario(String nombUsuario) {
		this.nombUsuario = nombUsuario;
	}
	
	public Integer getCodArea() {
		return codArea;
	}
	public void setCodArea(Integer codArea) {
		this.codArea = codArea;
	}
	
	public String getDescArea() {
		return descArea;
	}
	public void setDescArea(String descArea) {
		this.descArea = descArea;
	}
	
	public Integer getCodFicha() {
		return codFicha;
	}
	public void setCodFicha(Integer codFicha) {
		this.codFicha = codFicha;
	}

	public Integer getCodPerfil() {
		return codPerfil;
	}
	public void setCodPerfil(Integer codPerfil) {
		this.codPerfil = codPerfil;
	}

	public String getDescPerfil() {
		return descPerfil;
	}
	public void setDescPerfil(String descPerfil) {
		this.descPerfil = descPerfil;
	}
	
	public String getAbrevArea() {
		return abrevArea;
	}
	public void setAbrevArea(String abrevArea) {
		this.abrevArea = abrevArea;
	}
	
	public List<Perfil> getPerfiles() {
		return perfiles;
	}
	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}
	public void addPerfil(Perfil perfil) {
		this.perfiles.add(perfil);
	}
	public List<String> getPermisos() {
		return permisos;
	}
	public void setPermisos(List<String> permisos) {
		this.permisos = permisos;
	}
	
}
