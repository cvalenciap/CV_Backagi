package pe.com.sedapal.agi.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.CorreoDestinatario;
import pe.com.sedapal.agi.model.FormatoHTML;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface ICorreoDatosDAO {
	FormatoHTML obtenerFormato(String nombre) throws SQLException;
	List<String> obtenerVariableFormato(String nombre) throws SQLException;
	List<String> obtenerValorFormato(List<Long> listaId, String nombre, String imagenFirma, String imagenPie, String linkSistema)
	throws SQLException;
	List<String> obtenerDestinatario(List<Long> listaId, String nombre) throws SQLException;
	List<CorreoDestinatario> obtenerListaDestinatario(List<Long> listaId, String nombre) throws SQLException;
	List<String> obtenerDestinatarioOtro(List<Long> listaId, String nombre) throws SQLException;
	List<Constante> obtenerDatosCorreo() throws SQLException;
	Error getError();
	Error getErrorNull();
}