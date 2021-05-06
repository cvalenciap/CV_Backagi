package pe.com.sedapal.agi.security.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.security.model.AuthResponse;
import pe.com.sedapal.agi.security.model.LoginRequest;
import pe.com.sedapal.agi.security.model.ResetPasswordRequest;
import pe.com.sedapal.agi.util.RespuestaBean;
import pe.com.sedapal.seguridad.core.bean.UsuarioBean;

public interface IUsuariosService {
	AuthResponse getUserDetail(String username);		
	UsuarioBean getUserDetailPerfil(LoginRequest loginRequest);
	String requestPassword(String username);
	String resetPassword(ResetPasswordRequest request);
	
	
}
