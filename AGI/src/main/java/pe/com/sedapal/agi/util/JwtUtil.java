package pe.com.sedapal.agi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pe.com.sedapal.agi.security.config.UserAuth;
import pe.com.sedapal.seguridad.core.bean.Retorno;
import pe.com.sedapal.seguridad.core.bean.TrabajadorBean;
import pe.com.sedapal.seguridad.ws.SeguridadClienteWs;

@Component
public class JwtUtil {
	
	/*@Value("${parametro.token.jwtExpiration}")
	private int jwtExpiration;

	private static String jwtSecret;
	
	@Value("${parametro.token.jwtSecret}")
	public void setJwtSecret(String jwtSecret) {
		JwtUtil.jwtSecret = jwtSecret;
	}*/
	
	private static final Logger LOGGER = Logger.getLogger(JwtUtil.class);

	

	public static Authentication getAuthentication(HttpServletRequest request, SeguridadClienteWs seguridadClienteWs) {
		TrabajadorBean userData = null;
		String token =  request.getHeader("Authorization");
	    if (token != null && token.startsWith("Bearer ")) {
	        	token= token.replace("Bearer ","");
	    }
	    if(token!= null) {
	    	Retorno retorno = validaJwtToken(token, seguridadClienteWs);
	    	if(retorno.getCodigo().equals(Constantes.SUCCESS)) {
	    		userData = seguridadClienteWs.obtenerTrabajadorWs(retorno.getUsuario());
	    	}
	    	if(retorno != null && userData != null) { 
	    		return new UsernamePasswordAuthenticationToken((new UserAuth(retorno.getUsuario(),"",true,true,true, true, new ArrayList<GrantedAuthority>(),userData.getnFicha())), null, emptyList());
	    	}	    	
	    }
		return null;
	}
	
	
	public static Retorno validaJwtToken(String token, SeguridadClienteWs seguridadClienteWs) {		
		try {
			Retorno retorno = seguridadClienteWs.validarTokenWs(token);
			if(retorno.getCodigo().equals(Constantes.SUCCESS)) {
				return retorno;
			}
		} catch (ExpiredJwtException e) {
			LOGGER.info("Expired token: "+token);
		} catch (Exception e) {
			LOGGER.error("JWT Validation Error: " + e.getMessage(), e);
		}
		return null;
		
	}	
	
	private static Collection<? extends GrantedAuthority> emptyList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*add unimplement methods*/
	public static String getRedisKey(final String userId) {
		return Constantes.REDIS_PREFIX_USERS + Constantes.REDIS_KEYS_SEPARATOR + userId;
	}
	
	public static String getToken(HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.replace("Bearer ", "");
		}
		return token;
	}
	
}
