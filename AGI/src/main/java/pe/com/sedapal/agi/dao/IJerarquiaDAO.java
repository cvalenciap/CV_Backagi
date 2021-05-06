package pe.com.sedapal.agi.dao;

import java.util.List;

import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.JerarquiaRequest;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.model.response_objects.Error;

public interface IJerarquiaDAO {
	//List<Jerarquia> obtenerJerarquia(JerarquiaRequest jerarquiaRequest, PageRequest pageRequest);
	List<Jerarquia> obtenerJerarquia(JerarquiaRequest jerarquiaRequest);
	List<Jerarquia> obtenerJerarquiaTipoDocumento(JerarquiaRequest jerarquiaRequest, PageRequest pageRequest);
	Paginacion getPaginacion();
	Error getError();
	List<Jerarquia> guardarJerarquia(Jerarquia jerarquia, Long codigo, String usuario);
	Jerarquia actualizarPermiso(Jerarquia jerarquia, Long codigo, String usuario);
	Boolean eliminarJerarquia(Long codigo, String usuario);
	public List<Jerarquia> actualizarJerarquia(Jerarquia jerarquia, Long codigo, String usuario); 
	public  List<Jerarquia> obtenerHijoJerarquia(Jerarquia jerarquia);
	public  List<Jerarquia> obtenerDocJerarquia(Jerarquia jerarquia);

}