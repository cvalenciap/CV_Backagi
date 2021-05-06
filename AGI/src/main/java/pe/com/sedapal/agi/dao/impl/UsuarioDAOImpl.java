package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import oracle.jdbc.OracleTypes;

import pe.com.sedapal.agi.dao.IUsuarioDAO;
import pe.com.sedapal.agi.security.model.Usuario;

@Repository
public class UsuarioDAOImpl implements IUsuarioDAO {

    @Autowired
    private JdbcTemplate jdbc;

    private SimpleJdbcCall jdbcCall;

    public Usuario mapearUsuario (Map<String,Object> resultado) {
        Usuario usuario = null;

        if (resultado != null) {
            usuario = new Usuario();
            usuario.setCodFicha(((BigDecimal)resultado.get("v_ficha")).intValue());
            usuario.setNombUsuario((String)resultado.get("v_nombre"));
            usuario.setCodUsuario((String)resultado.get("v_usuario"));
            usuario.setCodPerfil(((BigDecimal)resultado.get("v_codperfil")).intValue());
            usuario.setDescPerfil((String)resultado.get("v_nombre_perfil"));
            usuario.setCodArea(((BigDecimal)resultado.get("v_cod_area")).intValue());
            usuario.setDescArea((String)resultado.get("v_nombre_area"));
            usuario.setAbrevArea((String)resultado.get("v_area_abrev"));
        }

        return usuario;
    }

    @Override
    public Usuario consultarUsuario(String username) {
        // TODO Auto-generated method stub
        Map<String, Object> out = null;

        this.jdbcCall = new SimpleJdbcCall(this.jdbc)
                .withSchemaName("AGI")
                .withCatalogName("PCK_AGI_SEGURIDAD")
                .withProcedureName("PRC_USUARIO_OBTENER_INFO")
                .declareParameters(
                        new SqlParameter("v_username",OracleTypes.VARCHAR),
                        new SqlOutParameter("v_ficha", OracleTypes.NUMBER),
                        new SqlOutParameter("v_nombre", OracleTypes.VARCHAR),
                        new SqlOutParameter("v_usuario", OracleTypes.VARCHAR),
                        new SqlOutParameter("v_codperfil", OracleTypes.NUMBER),
                        new SqlOutParameter("v_nombre_perfil", OracleTypes.VARCHAR),
                        new SqlOutParameter("v_cod_area", OracleTypes.NUMBER),
                        new SqlOutParameter("v_nombre_area", OracleTypes.VARCHAR),
                        new SqlOutParameter("v_area_abrev", OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("v_username", username);


        out = this.jdbcCall.execute(in);
        Usuario usuario = new Usuario();
        usuario = mapearUsuario(out);

        return usuario;
    }
}
