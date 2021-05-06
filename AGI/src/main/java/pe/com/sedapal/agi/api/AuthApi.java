package pe.com.sedapal.agi.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.sedapal.agi.model.response_objects.ResponseObject;
import pe.com.sedapal.agi.security.config.UserAuth;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import pe.com.sedapal.agi.security.model.AuthResponse;
import pe.com.sedapal.agi.security.model.LoginRequest;
import pe.com.sedapal.agi.security.model.ResetPasswordRequest;
import pe.com.sedapal.agi.security.model.Usuario;
import pe.com.sedapal.agi.security.service.IUsuariosService;
import pe.com.gmd.util.exception.GmdException;
//import pe.com.gmd.util.exception.GmdException;
import pe.com.sedapal.agi.model.response_objects.Estado;
import pe.com.sedapal.seguridad.core.bean.Retorno;
import pe.com.sedapal.seguridad.core.bean.UsuarioBean;
import pe.com.sedapal.seguridad.ws.SeguridadClienteWs;
import pe.com.sedapal.agi.util.RespuestaBean;

@RestController
@RequestMapping("/auth")
public class AuthApi {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private IUsuariosService service;
	
	@Autowired
	private SeguridadClienteWs stub;	
	
	private AuthResponse auth;

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> autenticarUsuario(@Valid @RequestBody LoginRequest loginRequest,HttpServletRequest req){
        ResponseObject response = new ResponseObject();
        Map<String, Object> authParameter = new HashMap<String, Object>();
        auth = new AuthResponse();
        try {
        	//AGI
            req.setAttribute("password", loginRequest.getPassword());            
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                       
            if (userDetails != null) {
                auth = this.service.getUserDetail(userDetails.getUsername());
                
                if (auth == null) {
                    response.setEstado(Estado.ERROR);
                    response.setError(404, "No se pudo obtener la información del usuario", null);
                    return new ResponseEntity<ResponseObject>(response, HttpStatus.NOT_FOUND);
                } else {
                    response.setResultado(auth);
                    response.setEstado(Estado.OK);
                    //servicio autenticacion completa
                    return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
                }
            } else {
                response.setEstado(Estado.ERROR);
                response.setError(404, "No se encuentra el registro", null);
                return new ResponseEntity<ResponseObject>(response, HttpStatus.NOT_FOUND);
            }

        } catch (InternalAuthenticationServiceException e) {
            response.setError(401, "El nombre de usuario o la contraseña son inválidos", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.setError(1, "Error Interno", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    /*Ingresa al metodo cuando el usuario elige el perfil*/
    @PostMapping(path = "/login/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> getPermissions(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletRequest req) throws GmdException{
		ResponseObject response = new ResponseObject();
		try {
			UsuarioBean usuario = service.getUserDetailPerfil(loginRequest);
			if (usuario == null) {
				response.setEstado(Estado.ERROR);
				response.setError(404, "No se obtiene los parametros", null);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				response.setEstado(Estado.OK);
				response.setResultado(usuario);
				return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
			}
		} catch (InternalAuthenticationServiceException e) {
			response.setError(401, "El nombre de usuario o la contraseña son inválidos", e.getMessage());
			response.setEstado(Estado.ERROR);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			response.setError(1, "Error Interno", e.getMessage());
			response.setEstado(Estado.ERROR);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
    
    @PostMapping(path = "/login/password/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> solicitarPassword(@Valid @RequestBody ResetPasswordRequest passwordRequest, HttpServletRequest req)throws GmdException {
    	ResponseObject response = new ResponseObject();
    	
    	try {
    		String result = this.service.requestPassword(passwordRequest.getUsername());
			response.setEstado(Estado.OK);
			response.setResultado(result);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
    	} catch (BadCredentialsException e) {
    		response.setError(404, "El usuario ingresado no es válido", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.NOT_FOUND);
    	} catch (InternalAuthenticationServiceException e) {
    		response.setError(2, "Error en el servicio de seguridad", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    	} catch (Exception e) {
            response.setError(1, "Error Interno", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(path = "/login/password/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> cambiarPassword(@Valid @RequestBody ResetPasswordRequest passwordRequest, HttpServletRequest req)throws GmdException {
    	ResponseObject response = new ResponseObject();
    	
    	try {
    		String result = this.service.resetPassword(passwordRequest);
			response.setEstado(Estado.OK);
			response.setResultado(result);
			return new ResponseEntity<ResponseObject>(response, HttpStatus.OK);
    	} catch (BadCredentialsException e) {
    		response.setError(401, "El usuario o una de las contraseñas ingresadas no son válidas", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.UNAUTHORIZED);
    	} catch (InternalAuthenticationServiceException e) {
    		response.setError(2, "Error en el servicio de seguridad", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    	} catch (Exception e) {
            response.setError(1, "Error Interno", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
   
    //logout
    @PostMapping(path= {"/login/logout"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> logoutPrrivce(@RequestBody Map<String, String> requestParm, HttpServletRequest req) throws GmdException{
    	ResponseObject response = new ResponseObject();
    	try {    		
    		ResponseObject respuesta = new ResponseObject();
    		String token = requestParm.get("token");
    		if(!token.equals("") && !StringUtils.isEmpty(token)) {
    			Retorno retorno = stub.logoutWs(token);
    			if(retorno.getCodigo().equals(0)) {
    				respuesta.setEstado(Estado.OK);
    				respuesta.setResultado(retorno.getMensaje());
    			}
    		}    		
			return new ResponseEntity<ResponseObject>(respuesta, HttpStatus.OK);
			
    	} catch (BadCredentialsException e) {
    		response.setError(401, "El usuario o una de las contraseñas ingresadas no son válidas", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.UNAUTHORIZED);
    	} catch (InternalAuthenticationServiceException e) {
    		response.setError(2, "Error en el servicio de seguridad", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    	} catch (Exception e) {
            response.setError(1, "Error Interno", e.getMessage());
            response.setEstado(Estado.ERROR);
            return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
}
