package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IRelacionCoordinadorDAO;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.Jerarquia;
import pe.com.sedapal.agi.model.NodoJerarquia;
import pe.com.sedapal.agi.model.RelacionCoordinador;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IRelacionCoordinadorService;
import pe.com.sedapal.agi.security.config.UserAuth;
@Service
public class RelacionCoordinadorServiceImpl implements IRelacionCoordinadorService{
	
	@Autowired
	SessionInfo session;
	
	
	@Autowired
	private IRelacionCoordinadorDAO daoRelacion;
	
	public Paginacion getPaginacion() {
		return this.daoRelacion.getPaginacion();
	}
	
	public Error getError() {
		return this.daoRelacion.getError();
	}
	
	@Override
	public List<NodoJerarquia> obtenerArbolJerarquiaPorTipo(Long idJerarquia){
		List<Jerarquia> listaJerarquia = new ArrayList<>();
		List<NodoJerarquia> nodosJerarquia = new ArrayList<NodoJerarquia>();
		listaJerarquia = this.daoRelacion.obtenerArbolJerarquiaPorTipo(idJerarquia);
		nodosJerarquia = convertirArbol(listaJerarquia);
		return nodosJerarquia;
	}
	
	@Override
	public List<RelacionCoordinador> obtenerRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador, PageRequest pageRequest){
		List<RelacionCoordinador> listaRelacion = new ArrayList<>();
		listaRelacion = this.daoRelacion.obtenerRelacionGerenciaAlcance(relacionCoordinador, pageRequest);
		return listaRelacion;
	}
	
	@Override
	public RelacionCoordinador guardarRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador) {
		RelacionCoordinador relacion = new RelacionCoordinador();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		relacionCoordinador.setDatosAuditoria(datosAuditoria);
		relacion = this.daoRelacion.guardarRelacionGerenciaAlcance(relacionCoordinador);
		return relacion;
	}
	
	@Override
	public RelacionCoordinador actualizarRelacionGerenciaAlcance(Long idRelacion, RelacionCoordinador relacionCoordinador) {
		RelacionCoordinador relacion = new RelacionCoordinador();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
		relacionCoordinador.setDatosAuditoria(datosAuditoria);
		relacion = this.daoRelacion.actualizarRelacionGerenciaAlcance(idRelacion, relacionCoordinador);
		return relacion;
	}
	
	@Override
	public boolean eliminarRelacionGerenciaAlcance(RelacionCoordinador relacionCoordinador) {
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
		relacionCoordinador.setDatosAuditoria(datosAuditoria);
		return this.daoRelacion.eliminarRelacionGerenciaAlcance(relacionCoordinador);
	}
	
	@Override
	public List<RelacionCoordinador> obtenerDatosCoordinador(Long idGerencia, Long idAlcance){
		List<RelacionCoordinador> listaRelacion = new ArrayList<>();
		listaRelacion = this.daoRelacion.obtenerDatosCoordinador(idGerencia, idAlcance);
		return listaRelacion;
	}
	
	private List<NodoJerarquia> convertirArbol(List<Jerarquia> listaJerarquia){
		List<NodoJerarquia> hijos = new ArrayList<>();
		try {
			List<Jerarquia> nivelInicial = listaJerarquia.stream().filter(obj -> obj.getNivel().equals(new Long (1))).collect(Collectors.toList());
			for(Jerarquia requisitoPadre:nivelInicial) {
				NodoJerarquia nodoPadre = new NodoJerarquia();
				nodoPadre.setId(requisitoPadre.getId());
				nodoPadre.setIdTipoDocu(requisitoPadre.getIdTipoDocu());
				nodoPadre.setRuta(requisitoPadre.getRuta());
				nodoPadre.setNombre(requisitoPadre.getDescripcion());
				List<NodoJerarquia> nodosHijos = new ArrayList<>();
				List<Jerarquia> nivelHijos = listaJerarquia.stream().filter(obj -> !obj.getNivel().equals(new Long (1))).collect(Collectors.toList());
				List<Jerarquia> requisitosHijos = nivelHijos.stream().filter(obj -> obj.getIdPadre().equals(nodoPadre.getId())).collect(Collectors.toList());
				
				for(Jerarquia requisitoHijo:requisitosHijos) {
					NodoJerarquia nodo = new NodoJerarquia();
					nodo.setId(requisitoHijo.getId());
					nodo.setIdTipoDocu(requisitoHijo.getIdTipoDocu());
					nodo.setRuta(requisitoHijo.getRuta());
					nodo.setNombre(requisitoHijo.getDescripcion());
					
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
		return hijos;
	}
	
	private List<NodoJerarquia> obtenerNodosHijos(NodoJerarquia nodoPadre,List<Jerarquia> listaJerarquia){
		List<Jerarquia> jerarquiaHijos = new ArrayList<>();
		List<Jerarquia> nivelHijos = listaJerarquia.stream().filter(obj -> !obj.getNivel().equals(new Long (1))).collect(Collectors.toList());
		jerarquiaHijos = nivelHijos.stream().filter(obj -> obj.getIdPadre().equals(nodoPadre.getId())).collect(Collectors.toList());
		List<NodoJerarquia> nodosHijos = new ArrayList();
		
		if(jerarquiaHijos.size()>0) {
			for(Jerarquia jerarquiaHijo:jerarquiaHijos) {
				NodoJerarquia nodo = new NodoJerarquia();
				nodo.setId(jerarquiaHijo.getId());
				nodo.setIdTipoDocu(jerarquiaHijo.getIdTipoDocu());
				nodo.setNombre(jerarquiaHijo.getDescripcion());
				nodo.setRuta(jerarquiaHijo.getRuta());
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
	public Long obtenerDatosJefeEquipo(Long idArea) {
		Long nFicha = this.daoRelacion.obtenerDatosJefeEquipo(idArea);
		return nFicha;

	}
}
