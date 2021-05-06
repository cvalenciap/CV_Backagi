package pe.com.sedapal.agi.security.service;
import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import pe.com.sedapal.seguridad.core.bean.PerfilSistemaBean;
import pe.com.sedapal.seguridad.core.bean.Retorno;
import pe.com.sedapal.seguridad.core.bean.SistemaModuloOpcionBean;
import pe.com.sedapal.seguridad.core.bean.UsuarioBean;
import pe.com.sedapal.seguridad.ws.SeguridadClienteWs;
//import pe.com.gmd.util.exception.GmdException;
//import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IUsuarioDAO;
import pe.com.sedapal.agi.util.Constantes;
import pe.com.sedapal.agi.util.RedisUtil;
import pe.com.sedapal.agi.util.RespuestaBean;
import pe.com.sedapal.agi.util.SessionConstants;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.agi.security.model.AuthResponse;
import pe.com.sedapal.agi.security.model.LoginRequest;
import pe.com.sedapal.agi.security.model.Perfil;
import pe.com.sedapal.agi.security.model.ResetPasswordRequest;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.security.model.Usuario;
import static java.text.MessageFormat.format;
import org.apache.log4j.Logger;
@Service
@ImportResource("classpath:pe/com/sedapal/seguridad/ws/config/applicationContext.xml")
public class UsuariosServiceImpl implements IUsuariosService {

    @Autowired
    private SeguridadClienteWs stub;

    @Autowired
    private IUsuarioDAO dao;
    
    @Autowired
	private RedisUtil redis;
    
    //CGUERRA INICIO
    @Autowired
    AuthenticationManager authenticationManager;
    
   // private static final Logger LOGGER = Logger.getLogger(UsuariosServiceImpl.class);
    
  //  RespuestaBean respuesta = new RespuestaBean();
    //CGUERRA FIN
    
	//@Autowired
	//JwtUtil Jwtutil;

	
	/*@Value("${parametro.token.jwtExpiration}")
	private int timeSesion;*/
	
    @Autowired
	private Environment env;
    
    @Autowired
	private HttpServletRequest request;
    
//    @Autowired
//    private SessionInfo session;

    
    AuthResponse auth1 = new AuthResponse();
    
    /* se ejecuta despues de autenticar usuario */
    @Override
    public AuthResponse getUserDetail(String username) {
    	Integer codigoSistema = Integer.parseInt(env.getProperty("app.config.seguridad.codigo-sistema"));
        List <String> permisos;
        List <PerfilSistemaBean> perfiles;
        String token;
        //UsuarioBean usuario;
        PerfilSistemaBean perfil;
        Usuario datosUsuario;
        AuthResponse auth = new AuthResponse();

        try {
	        // Actualizar token obtenido de servicio
	        token = (String)request.getAttribute("token");
	        auth.setToken(token);
	        
	        // Datos del usuario
	        datosUsuario = this.dao.consultarUsuario(username);
           UsuarioBean usuario = new UsuarioBean();
	        
            // Atributos faltantes que no trae el servicio
            usuario.setDescArea(datosUsuario.getDescArea());
            usuario.setNombUsuario(datosUsuario.getNombUsuario());
            usuario.setAbrevArea(datosUsuario.getAbrevArea());
            usuario.setCodUsuario(datosUsuario.getCodUsuario());
            usuario.setCodFicha(datosUsuario.getCodFicha());
            usuario.setCodArea(datosUsuario.getCodArea());
            usuario.setDescripcion(datosUsuario.getNombUsuario());
            usuario.setPass(null);

            // Obtener perfiles
	        perfiles = (List<PerfilSistemaBean>)request.getAttribute("perfiles");
	        usuario.setPerfilesAsociados(perfiles);
	        perfil = perfiles.get(0);
            usuario.setCodPerfil(perfil.getCodPerfil());
            usuario.setPerfilNombre(perfil.getDescripcion());
	        //Permisos/urls
	        permisos = stub.obtenerPermisosActWs(username, codigoSistema, perfil.getCodPerfil());
	        usuario.setPermisos(permisos);
	        auth.setUserProfile(usuario);
            auth.setPerfiles(usuario.getPerfilesAsociados());
            String a = env.getProperty("parametro.token.jwtExpiration");
            int timesession = Integer.parseInt(a);
            System.out.println("TIEMPOOOOOOOOOZZZ");
            System.out.println(timesession);
            auth.setExpiresIn(timesession);    
            auth1= auth;
	        return auth;
	        
        } catch(Exception e) {
        	return null;
        }
    }
      
    /* se ejecuta cuando usuario selecciona su perfil */   
    @Override
	public UsuarioBean getUserDetailPerfil(LoginRequest loginRequest) {
    	Integer codigoSistema = Integer.parseInt(env.getProperty("app.config.seguridad.codigo-sistema"));
		UsuarioBean usuario = new UsuarioBean();		
		List<String> listaPermisos = null;
		List<Perfil> listaPerfil = null;

		String username = loginRequest.getUsername();
		username = username != null ? username.trim().toUpperCase() : null;
		int idProfile = loginRequest.getRoleId();
		listaPermisos = stub.obtenerPermisosActWs(username, codigoSistema, idProfile);

		try {
			usuario.setPermisos(listaPermisos);

		} catch (Exception e) {			
			throw new InternalAuthenticationServiceException(e.getMessage());
		}
		return usuario;
	}
    
    /* se ejecuta cuando usuario solicita nueva contraseña */
    @Override
    public String requestPassword(String username) {
    	Retorno retorno = null;
    	try {
	        retorno = stub.olvidarClaveWs(username);
    	} catch (Exception e) {
    		throw new InternalAuthenticationServiceException(e.getMessage());
		}
        if (retorno == null) {
        	throw new InternalAuthenticationServiceException("No se obtuvo una respuesta del servicio");
        } else if (SessionConstants.FAILURE.equals(retorno.getCodigo())) {
			throw new BadCredentialsException(retorno.getMensaje());
		} else if (SessionConstants.SUCCESS.equals(retorno.getCodigo())) {
			return retorno.getMensaje();
		} else {
			throw new InternalAuthenticationServiceException("No se obtuvo el resultado esperado");
		}
    }
    
    /* se ejecuta cuando usuario establece nueva contraseña */
    @Override
    public String resetPassword(ResetPasswordRequest request) {
    	Retorno retorno = null;
    	try {
	        retorno = stub.actualizarClaveWs(request.getUsername().toUpperCase(), request.getOldPassword(), request.getNewPassword(), request.getNewPasswordCheck());
    	} catch (Exception e) {
    		throw new InternalAuthenticationServiceException(e.getMessage());
		}
        if (retorno == null) {
        	throw new InternalAuthenticationServiceException("No se obtuvo una respuesta del servicio");
        } else if (SessionConstants.FAILURE.equals(retorno.getCodigo())) {
			throw new BadCredentialsException(retorno.getMensaje());
		} else if (SessionConstants.SUCCESS.equals(retorno.getCodigo())) {
			return retorno.getMensaje();
		} else {
			throw new InternalAuthenticationServiceException("No se obtuvo el resultado esperado");
		}
    }

}
