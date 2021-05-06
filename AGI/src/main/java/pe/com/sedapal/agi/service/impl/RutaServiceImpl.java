package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import pe.com.sedapal.agi.security.config.UserAuth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.dao.IRutaDAO;
import pe.com.sedapal.agi.model.Ruta;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RutaRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IRutaService;

@Service
public class RutaServiceImpl implements IRutaService{
	
	@Autowired
	SessionInfo session;
	
		
	@Autowired
	private IRutaDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Ruta> obtenerRuta(RutaRequest rutaRequest, PageRequest pageRequest){
		List<Ruta> listaRuta = new ArrayList<>();
		listaRuta = this.dao.obtenerRuta(rutaRequest, pageRequest);
		return listaRuta;
	}
	
	@Override
	public Ruta crearRuta(Ruta ruta) {
		List<Ruta> listaRutas = new ArrayList<>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		listaRutas = this.dao.guardarRuta(ruta, null, (((UserAuth)principal).getUsername()));
		return (listaRutas.size()==0)?null:listaRutas.get(0);
	}
	
	@Override
	public Ruta actualizarRuta(Ruta ruta, Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Ruta> listaRutas = new ArrayList<>();	
		listaRutas = this.dao.guardarRuta(ruta, codigo, (((UserAuth)principal).getUsername()));
		return (listaRutas.size()==0)?null:listaRutas.get(0);
	}
	
	@Override
	public Boolean eliminarRuta(Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.eliminarRuta(codigo, (((UserAuth)principal).getUsername()));
	}

}