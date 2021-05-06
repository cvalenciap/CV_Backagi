package pe.com.sedapal.agi.security.model;

import pe.com.sedapal.seguridad.core.bean.PerfilSistemaBean;
import pe.com.sedapal.seguridad.core.bean.UsuarioBean;

import java.util.List;

public class AuthResponse {

private int expiresIn;
	
	private String token;
	
	private List<PerfilSistemaBean> perfiles;
	
	private UsuarioBean userProfile;

	public AuthResponse() {
		super();
	}

	public AuthResponse(int expiresIn, String token, UsuarioBean userProfile) {
		super();
		this.expiresIn = expiresIn;
		this.token = token;
		this.userProfile = userProfile;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public List<PerfilSistemaBean> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<PerfilSistemaBean> perfiles) {
		this.perfiles = perfiles;
	}

	public UsuarioBean getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UsuarioBean userProfile) {
		this.userProfile = userProfile;
	}
	
}
