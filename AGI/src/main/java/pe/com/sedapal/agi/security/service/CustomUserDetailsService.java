package pe.com.sedapal.agi.security.service;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.com.sedapal.seguridad.core.bean.Retorno;
import pe.com.sedapal.seguridad.core.bean.TrabajadorBean;
import pe.com.sedapal.seguridad.ws.SeguridadClienteWs;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.util.SessionConstants;

@Service("customUserDetailsService")
@ImportResource("classpath:pe/com/sedapal/seguridad/ws/config/applicationContext.xml")
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private SeguridadClienteWs stub;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private Environment env;
	
	@Override
	public UserDetails loadUserByUsername(String username){
		Integer codigo_sistema = Integer.parseInt(env.getProperty("app.config.seguridad.codigo-sistema"));
		 
		Retorno retorno = null;
		List<String> permisos = null;
		List<GrantedAuthority> authorities = null;
		String password = null;
		
		try {
			password = (String) request.getAttribute("password");
			
			// consulta a webservice
			retorno = stub.autenticacionUsuarioActWs(username.toUpperCase(), codigo_sistema, password);
						
			if (SessionConstants.FAILURE.equals(retorno.getCodigo())) {
				throw new BadCredentialsException(retorno.getMensaje());
			}
			
			//permisos orientado a rutas de menu
			permisos = new ArrayList<>();
			if (retorno.getPerfilesAct().size() == 1) {
				permisos = stub.obtenerPermisosWs(username);
			}
			permisos.add("/cambiarClave");
			authorities = buildUserAuthority(permisos);
			
			request.removeAttribute("password");
			
			// actualizar token
			request.setAttribute("token", retorno.getToken());
			request.setAttribute("perfiles", retorno.getPerfilesAct());
			
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new AccessDeniedException(e.getMessage());
		}
	
		
		//pincipal
		return buildUserForAuthentication(retorno.getUsuario(), retorno.getCodigoTrabajador() , password, authorities);
	}
	
	private List<GrantedAuthority> buildUserAuthority(List<String> permisos) {

		Set<GrantedAuthority> setAuths = new HashSet<>();

		for (String permiso : permisos) {
			setAuths.add(new SimpleGrantedAuthority(permiso));
		}

		return new ArrayList<>(setAuths);
	}
	
	

	private User buildUserForAuthentication(String usuario,Integer usercodigo, String clave, List<GrantedAuthority> authorities) {
		return new UserAuth(usuario, clave,true,true,true, true,  authorities,usercodigo);
	}

}
