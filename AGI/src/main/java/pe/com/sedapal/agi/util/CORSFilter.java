package pe.com.sedapal.agi.util;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter{
	
	@Override
	public void destroy() {}

	   //@Value("${url.validate.seguridad}")
	    private String pathValidate = "/auth/login/";
	    
	    
	    @Override
		public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
				throws IOException, ServletException {
			HttpServletResponse response = (HttpServletResponse) res;
			HttpServletRequest request = (HttpServletRequest) req;

			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
			//chain.doFilter(req, res);

			if ("OPTIONS".equalsIgnoreCase(request.getMethod()) ){
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				chain.doFilter(req, res);
			}
		}
	    
	    
	/*@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT");
		httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
		httpServletResponse.setHeader("Access-Control-Allow-Headers",
				"x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if (!httpServletRequest.getMethod().equals("OPTIONS")){
			if (!validatePath(httpServletRequest.getRequestURI())) {
				String token = httpServletRequest.getHeader("Authorization");
				if (token == null) {
					httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
				}
			}
		}
		if (httpServletResponse.getStatus() != HttpStatus.UNAUTHORIZED.value()) {
			chain.doFilter(request, response);
		}
	}
	

	private boolean validatePath(String url) {
		boolean resultado = true;
		String baseUrl = pathValidate;
		if (!baseUrl.isEmpty()) {
			if (baseUrl.length() <= url.length()) {
				for (int indice = 0; indice < baseUrl.length(); indice++){
					if (!baseUrl.substring(indice, indice + 1).equals(url.substring(indice, indice + 1))) {
						resultado = false;
					}
				}
			} else return false;

        }
		return resultado;
	}
	*/
	
	
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
}
