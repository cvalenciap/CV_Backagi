package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.dao.IJerarquiaDAO;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.request_objects.JerarquiaRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IJerarquiaService;
import pe.com.sedapal.agi.model.NodoJerarquia;
import pe.com.sedapal.agi.security.config.UserAuth;
@Service
public class JerarquiaServiceImpl implements IJerarquiaService{
	
	@Autowired
	SessionInfo session;
	
	
	@Autowired
	private IJerarquiaDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}
	
	private List<NodoJerarquia> convertirArbol(List<Jerarquia> listaJerarquia)  {
		List<NodoJerarquia> hijos = new ArrayList<>();
		try {
			
			List<Jerarquia> nivelInicial = listaJerarquia.stream().filter(
					obj -> obj.getNivel().equals(new Long (1))).collect(Collectors.toList());
			for(Jerarquia requisitoPadre:nivelInicial) {
				NodoJerarquia nodoPadre = new NodoJerarquia();
				nodoPadre.setId(requisitoPadre.getId());
				nodoPadre.setIdTipoDocu(requisitoPadre.getIdTipoDocu());
				nodoPadre.setRuta(requisitoPadre.getRuta());
				nodoPadre.setEstado(requisitoPadre.getEstado());				
				nodoPadre.setNombre(requisitoPadre.getDescripcion());
				nodoPadre.setAbrJera(requisitoPadre.getAbrJera());
				// YPM - INICIO
				nodoPadre.setNivelJerarquia(requisitoPadre.getNivel());
				// YPM - FIN
				//cguerra
				nodoPadre.setIndicadorDescargas(requisitoPadre.getIndicadorDescargas());
				//cguerra
				
				List<NodoJerarquia> nodosHijos = new ArrayList<>();
				List<Jerarquia> nivelHijos = listaJerarquia.stream().filter(
						obj -> !obj.getNivel().equals(new Long (1))).collect(Collectors.toList());

				List<Jerarquia> requisitosHijos = nivelHijos.stream().filter(
						obj -> obj.getIdPadre().equals(nodoPadre.getId())).collect(Collectors.toList());
				
				for(Jerarquia requisitoHijo:requisitosHijos) {
					NodoJerarquia nodo = new NodoJerarquia();
					nodo.setId(requisitoHijo.getId());
					nodo.setIdTipoDocu(requisitoHijo.getIdTipoDocu());
					nodo.setRuta(requisitoHijo.getRuta());				
					nodo.setEstado(requisitoHijo.getEstado());
					nodo.setNombre(requisitoHijo.getDescripcion());
					nodo.setAbrJera(requisitoHijo.getAbrJera());					
					// YPM - INICIO
					nodo.setNivelJerarquia(requisitoHijo.getNivel());
					// YPM - FIN
					//CGUERRA
					nodo.setIndicadorDescargas(requisitoHijo.getIndicadorDescargas());
					//CGUERRA
					
					nodo.setIdPadre(requisitoHijo.getIdPadre());
					
					List<NodoJerarquia> nodosHijosJerarquia = obtenerNodosHijos(nodo,listaJerarquia);
					if(nodosHijosJerarquia.size()>0) {						
						nodo.setChildren(nodosHijosJerarquia);
					}
					nodosHijos.add(nodo);
				}
				
				if(nodosHijos.size()>0) {
					nodoPadre.setChildren(nodosHijos);
				}
				hijos.add(nodoPadre);
			}	
		} catch (Exception e) {
			System.out.println("Error: "+ e);
		}
			
		//nodos.setChildren(hijos);
		return hijos;
	}
	
	private List<NodoJerarquia> obtenerNodosHijos(NodoJerarquia nodoPadre,List<Jerarquia> listaJerarquia){
		List<Jerarquia> jerarquiaHijos = new ArrayList<>();
		List<Jerarquia> nivelHijos = listaJerarquia.stream().filter(
				obj -> !obj.getNivel().equals(new Long (1))).collect(Collectors.toList());
		jerarquiaHijos = nivelHijos.stream().filter(
				obj -> obj.getIdPadre().equals(nodoPadre.getId())).collect(Collectors.toList());
		
		List<NodoJerarquia> nodosHijos = new ArrayList<>();
		
		if(jerarquiaHijos.size()>0) {
			for(Jerarquia jerarquiaHijo:jerarquiaHijos) {
				NodoJerarquia nodo = new NodoJerarquia();
				nodo.setId(jerarquiaHijo.getId());
				nodo.setIdTipoDocu(jerarquiaHijo.getIdTipoDocu());
				nodo.setNombre(jerarquiaHijo.getDescripcion());
				nodo.setRuta(jerarquiaHijo.getRuta());
				nodo.setEstado(jerarquiaHijo.getEstado());
				nodo.setAbrJera(jerarquiaHijo.getAbrJera());				
				/*if (jerarquiaHijo.getAbrJera()!=null) {
					nodo.setNombre(jerarquiaHijo.getAbrJera());
				} else {
					if (jerarquiaHijo.getCodJera()!=null) {
						nodo.setNombre(jerarquiaHijo.getCodJera()+" "+jerarquiaHijo.getDescripcion());
					}else {
						nodo.setNombre(jerarquiaHijo.getDescripcion());
					}
				}*/
				// YPM - INICIO
				nodo.setNivelJerarquia(jerarquiaHijo.getNivel());
				// YPM - FIN
				//cguerra
				nodo.setIndicadorDescargas(jerarquiaHijo.getIndicadorDescargas());
				//cguerra
				nodo.setIdPadre(jerarquiaHijo.getIdPadre());
				
				List<NodoJerarquia> nodosHijosJerarquia = obtenerNodosHijos(nodo,listaJerarquia);
				if(nodosHijosJerarquia.size()>0) {
					nodo.setChildren(nodosHijosJerarquia);
				}
				nodosHijos.add(nodo);
			}
		}
		return nodosHijos;
	}
	
	@Override
	public List<NodoJerarquia> obtenerJerarquia(JerarquiaRequest parametros) {
		// TODO Auto-generated method stub
		List<Jerarquia> listaJerarquia = dao.obtenerJerarquia(parametros);
		
		List<NodoJerarquia> nodos = new ArrayList<NodoJerarquia>();
		nodos = convertirArbol(listaJerarquia);
		
		return nodos;
	}
	
	@Override
	public List<Jerarquia> obtenerJerarquiaTipoDocumento(JerarquiaRequest parametros, PageRequest pageRequest) {
		List<Jerarquia> listaJerarquia = dao.obtenerJerarquiaTipoDocumento(parametros, pageRequest);
		return listaJerarquia;
	}
	
	@Override
	public List<Jerarquia> obtenerJerarquiaIdPadre(JerarquiaRequest parametros) {
		// TODO Auto-generated method stub
		List<Jerarquia> listaJerarquia = dao.obtenerJerarquia(parametros);
		return listaJerarquia;
	}
	/*public List<Jerarquia> obtenerJerarquia(JerarquiaRequest jerarquiaRequest, PageRequest pageRequest){
		List<Jerarquia> listaJerarquia = new ArrayList<>();
		listaJerarquia = this.dao.obtenerJerarquia(jerarquiaRequest, pageRequest);
		List<Jerarquia> listaHijos = listaJerarquia.stream().filter(obj -> !obj.getNivel().equals(new Long("1"))).collect(Collectors.toList());
		
		for(Jerarquia jerarquia:listaJerarquia) {
			List<Jerarquia> nodosHijos = listaHijos.stream().filter(obj -> obj.getIdPadre().equals(jerarquia.getId())).collect(Collectors.toList());
			List<String> listaHijosJerarquia = nodosHijos.stream().map(obj -> obj.getId() +":"+ obj.getDescripcion()).collect(Collectors.toList());
			jerarquia.setListaHijos(listaHijosJerarquia);
		}
		return listaJerarquia;
	}*/

	@Override
	public Jerarquia crearJerarquia(Jerarquia jerarquia) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Jerarquia> listaJerarquias = new ArrayList<>();		
		listaJerarquias = this.dao.guardarJerarquia(jerarquia, null, (((UserAuth)principal).getUsername()));
		return (listaJerarquias.size()==0)?null:listaJerarquias.get(0);
	}
	
	@Override
	public Jerarquia actualizarJerarquia(Jerarquia jerarquia, Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Jerarquia> listaJerarquias = new ArrayList<>();	
		listaJerarquias = this.dao.guardarJerarquia(jerarquia, codigo, (((UserAuth)principal).getUsername()));
		return (listaJerarquias.size()==0)?null:listaJerarquias.get(0);
	}
	
	@Override
	public Jerarquia actualizarPermiso(Jerarquia jerarquia, Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.actualizarPermiso(jerarquia, codigo, (((UserAuth)principal).getUsername()));
	}
	
	@Override
	public Boolean eliminarJerarquia(Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.dao.eliminarJerarquia(codigo, (((UserAuth)principal).getUsername()));
	}
	
	@Override
	public Jerarquia actualizarHijosJerarquia(Jerarquia jerarquia,Long codigo) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Jerarquia> listaJerarquias = new ArrayList<>();	
		listaJerarquias = this.dao.actualizarJerarquia(jerarquia, codigo, (((UserAuth)principal).getUsername()));
		return (listaJerarquias.size()==0)?null:listaJerarquias.get(0);
	}
	
	@Override
	public  List<Jerarquia> obtenerHijoJerarquia(Jerarquia jerarquia){
		return this.dao.obtenerHijoJerarquia(jerarquia);
	}
	@Override
	public  List<Jerarquia> obtenerDocJerarquia(Jerarquia jerarquia){
		return this.dao.obtenerDocJerarquia(jerarquia);
		
	}

}