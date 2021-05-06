package pe.com.sedapal.agi.security.filters;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import pe.com.sedapal.agi.util.SessionConstants;
import pe.com.sedapal.agi.util.JwtUtil;
import pe.com.sedapal.seguridad.core.bean.Retorno;
import pe.com.sedapal.seguridad.ws.SeguridadClienteWs;
import pe.com.sedapal.agi.util.RedisUtil;

import static java.util.Collections.emptyList;

@Order(Ordered.LOWEST_PRECEDENCE)
public class TokenFilter extends GenericFilterBean {

	@Autowired
	private SeguridadClienteWs stub;
	
	private final RedisUtil redis;
	public TokenFilter(RedisUtil redis, SeguridadClienteWs stub) {
		super();
		this.redis = redis;
		this.stub = stub;
	}
	

	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
			throws IOException, ServletException {
		try {
			
			HttpServletRequest req = (HttpServletRequest) request;
			
			if (!req.getRequestURI().equals(SessionConstants.URL_LOGIN)
//					&& !req.getRequestURI().equals(SessionConstants.URL_PROFILE)
					&& !req.getRequestURI().equals(SessionConstants.URL_LOGOUT)
					&& !req.getRequestURI().equals(SessionConstants.URL_PASSWORD_REQUEST)
					&& !req.getRequestURI().equals(SessionConstants.URL_PASSWORD_RESET)
					&& !req.getRequestURI().equals(SessionConstants.URL_APP_INFO)) {
				
				Authentication authentication = JwtUtil.getAuthentication(req, stub);
				
				String token = redis.getToken(req);
					
				Retorno result = stub.validarTokenWs(token);

				if ("0".equals(result.getCodigo())) { 
					throw new Exception("No se encuentra el token de seguridad"); 
					}

				SecurityContextHolder.getContext().setAuthentication(authentication)						
						//new UsernamePasswordAuthenticationToken(result.getUsuario(), result.getCodigo(), null, emptyList()) //validamos
				;
			}
			
		} catch (Exception e) {
			
			((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			e.printStackTrace();
			LOGGER.debug("Exception " + e.getMessage(), e);
		}
		
		filterchain.doFilter(request, response);
	}
	
}
