package pe.com.sedapal.agi.security.model;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.com.sedapal.seguridad.core.bean.UsuarioBean;
import pe.com.sedapal.agi.dao.IUsuarioDAO;
import pe.com.sedapal.agi.util.RedisUtil;

@Component
public class SessionInfo {
	
	@Autowired
	HttpServletRequest req;
	
	
	 @Autowired
	    private IUsuarioDAO dao;
	 
	
		Usuario datosUsuario;
		 UsuarioBean usuario = new UsuarioBean();
		 
		public UsuarioBean getUserProfile1(String username) {		
			datosUsuario = this.dao.consultarUsuario(username);
			 usuario.setCodFicha(datosUsuario.getCodFicha());
			return usuario;
		}
		
	public UsuarioBean getUserProfile() {

		 usuario.setCodFicha(datosUsuario.getCodFicha());
		 usuario.setCodUsuario(datosUsuario.getCodUsuario());
	
		
		return usuario;
	}
	
	
	/*public boolean setPerfilUsuario(int id) {
		Perfil perfil;
		String token;
		Usuario usuario = (Usuario) redis.FindByToken(req);
		for(int i=0; i < usuario.getPerfiles().size(); i++) {
			perfil = usuario.getPerfiles().get(i);
			if (perfil.getCodPerfil() == id) {
				token = redis.getToken(req);
				usuario.setCodPerfil(perfil.getCodPerfil());
				usuario.setDescPerfil(perfil.getDescripcion());
				redis.save(token, usuario);
				return true;
			}
		}
		return false;
	}*/
	
	public boolean validarPermiso(String role) {
		boolean hasRole = false;
		List<String> permiso = this.getUserProfile().getPermisos();		
		hasRole = permiso.contains(role);
		return hasRole;
	} 

}

