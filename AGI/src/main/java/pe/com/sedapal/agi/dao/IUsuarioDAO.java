package pe.com.sedapal.agi.dao;

import pe.com.sedapal.agi.security.model.Usuario;

public interface IUsuarioDAO {
	Usuario consultarUsuario(String username);
}
