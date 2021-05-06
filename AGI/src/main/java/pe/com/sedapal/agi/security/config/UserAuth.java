package pe.com.sedapal.agi.security.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserAuth extends User{
	
	private static final long serialVersionUID = 1L;
	
	//private String token;
	//private List<Integer> perfiles;
	private Integer codPerfil;
	
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UserAuth(String username, String password, boolean enabled,
	         boolean accountNonExpired, boolean credentialsNonExpired,
	         boolean accountNonLocked,Collection authorities,Integer codPerfil) {

	             super(username, password, enabled, accountNonExpired,
	                credentialsNonExpired, accountNonLocked, authorities);

	             //this.token = token;
	           //  this.perfiles = perfiles;
	             this.codPerfil= codPerfil;
	}


	public Integer getCodPerfil() {
		return codPerfil;
	}


	public void setCodPerfil(Integer codPerfil) {
		this.codPerfil = codPerfil;
	}

	/*public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}*/

	

	/*public List<Integer> getPerfiles() {
		return perfiles;
	}*/



	/*public void setPerfiles(List<Integer> perfiles) {
		this.perfiles = perfiles;
	}*/
}
