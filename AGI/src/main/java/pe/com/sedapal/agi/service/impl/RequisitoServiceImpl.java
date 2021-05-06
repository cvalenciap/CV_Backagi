package pe.com.sedapal.agi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.dao.IRequisitoDAO;
import pe.com.sedapal.agi.model.Requisito;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IRequisitoService;

@Service
public class RequisitoServiceImpl implements IRequisitoService{
	
	@Autowired
	private IRequisitoDAO dao;

	@Autowired
	SessionInfo session;

	@Override
	public Error getError() {
		return dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}

	@Override
	public List<Requisito> listaNormaRequerimientos(PageRequest pageRequest) {
		return dao.listaNormaRequerimientos(pageRequest);
	}
	
	/*@Autowired
	private IRequisitoDAO dao;
	
	@Autowired
	private IAuditoriaDAO daoAuditoria;
	
	private static final Logger LOGGER = Logger.getLogger(RequisitoServiceImpl.class);	*/

	/*@Override
	public NodoRequisito obtenerRequisitos(RequisitoRequest parametros) {
		// TODO Auto-generated method stub
		List<Requisito> listaRequisitos = dao.obtenerRequisitos(parametros);
		NodoRequisito nodos = new NodoRequisito();
		nodos = convertirArbol(listaRequisitos);
		return nodos;
	}*/

	/*private NodoRequisito convertirArbol(List<Requisito> listaRequisitos) {
		NodoRequisito nodos = new NodoRequisito();
		try {
			if(listaRequisitos.size() > 0) {
				nodos.setNombre(listaRequisitos.get(0).getDescripcionNorma());
				nodos.setId(listaRequisitos.get(0).getIdNorma());
				
				//Se agregó
				nodos.setIdNorma(listaRequisitos.get(0).getIdNorma());
				nodos.setDescripcionNorma(listaRequisitos.get(0).getDescripcionNorma());
				nodos.setTipo(listaRequisitos.get(0).getTipoNorma());
				nodos.setEstado(1);
				List<NodoRequisitos> hijos = new ArrayList<>();
				List<Requisito> nivelInicial = listaRequisitos.stream().filter(
						obj -> obj.getNivel().equals("1")).collect(Collectors.toList());
				
				for(Requisito requisitoPadre:nivelInicial) {
					NodoRequisitos nodoPadre = new NodoRequisitos();
					nodoPadre.setId(requisitoPadre.getIdRequisito());
					nodoPadre.setNombre(requisitoPadre.getOrden()+" " +requisitoPadre.getDescripcionReq());
					
					//Se agregó
					nodoPadre.setIdRequisito(requisitoPadre.getIdRequisito());
					nodoPadre.setIdNorReq(requisitoPadre.getIdNorReq());
					nodoPadre.setIdNorma(requisitoPadre.getIdNorma());
					nodoPadre.setOrden(requisitoPadre.getOrden());
					nodoPadre.setNivel(requisitoPadre.getNivel());
					nodoPadre.setEstado(1);
					nodoPadre.setDescripcionReq(requisitoPadre.getDescripcionReq());
					nodoPadre.setIdRequisitoPadre(requisitoPadre.getIdRequisitoPadre());
					nodoPadre.setRequisitoRelacionado(requisitoPadre.getRequisitoRelacionado());
					nodoPadre.setRequisitoDocumento(requisitoPadre.getRequisitoDocumento());
					nodoPadre.setVdetreq(requisitoPadre.getVdetreq());
					nodoPadre.setVcuesti(requisitoPadre.getVcuesti());
					//
					
					List<NodoRequisitos> nodosHijos = new ArrayList<>();
					List<Requisito> nivelHijos = listaRequisitos.stream().filter(
							obj -> !obj.getNivel().equals("1")).collect(Collectors.toList());
					
					//nivelHijos.stream().forEach(obj -> System.out.println(obj.getIdRequisitoPadre()));
					List<Requisito> requisitosHijos = nivelHijos.stream().filter(
							obj -> obj.getIdRequisitoPadre().equals(nodoPadre.getId())).collect(Collectors.toList());
					
					for(Requisito requisitoHijo:requisitosHijos) {
						NodoRequisitos nodo = new NodoRequisitos();
						nodo.setId(requisitoHijo.getIdRequisito());
						nodo.setNombre(requisitoHijo.getOrden()+" "+requisitoHijo.getDescripcionReq());

						//Se agregó
						nodo.setIdRequisito(requisitoHijo.getIdRequisito());
						nodo.setIdNorReq(requisitoHijo.getIdNorReq());
						nodo.setIdNorma(requisitoHijo.getIdNorma());
						nodo.setOrden(requisitoHijo.getOrden());
						nodo.setNivel(requisitoHijo.getNivel());
						nodo.setEstado(1);
						nodo.setDescripcionReq(requisitoHijo.getDescripcionReq());
						nodo.setIdRequisitoPadre(requisitoHijo.getIdRequisitoPadre());
						nodo.setRequisitoRelacionado(requisitoHijo.getRequisitoRelacionado());
						nodo.setRequisitoDocumento(requisitoHijo.getRequisitoDocumento());
						nodo.setVdetreq(requisitoHijo.getVdetreq());
						nodo.setVcuesti(requisitoHijo.getVcuesti());
						//
						
						List<NodoRequisitos> nodosHijosRequisito = obtenerNodosHijos(nodo,listaRequisitos);
							if(nodosHijosRequisito.size()>0) {
								nodo.setChildren(nodosHijosRequisito);
							}
							nodosHijos.add(nodo);
						}
						
						if(nodosHijos.size()>0) {
							nodoPadre.setChildren(nodosHijos);
						}
						
						hijos.add(nodoPadre);
						
					}
				
				nodos.setChildren(hijos);
				
				
			}
		}catch(Exception e) {
			String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
			LOGGER.error(error[1], e);
		}
		
		return nodos;
	}
	*/
	/*private List<NodoRequisitos> obtenerNodosHijos(NodoRequisitos nodoPadre,List<Requisito> listaRequisitos){
		List<Requisito> requisitosHijos = new ArrayList<>();*/
		/*List<Requisito> nivelHijos = listaRequisitos.stream().filter(
				obj -> !obj.getNivel().equals("1")).collect(Collectors.toList());
		requisitosHijos = nivelHijos.stream().filter(
				obj -> obj.getIdRequisitoPadre().equals(nodoPadre.getId())).collect(Collectors.toList());*/
		
	/*	List<NodoRequisitos> nodosHijos = new ArrayList();
		
		if(requisitosHijos.size()>0) {
			for(Requisito requisitoHijo:requisitosHijos) {
				NodoRequisitos nodo = new NodoRequisitos();
				nodo.setId(requisitoHijo.getIdRequisito());
				nodo.setNombre(requisitoHijo.getOrden()+" " + requisitoHijo.getDescripcionReq());
				
				//Se agregó
				nodo.setIdRequisito(requisitoHijo.getIdRequisito());
				nodo.setIdNorReq(requisitoHijo.getIdNorReq());
				nodo.setIdNorma(requisitoHijo.getIdNorma());
				nodo.setOrden(requisitoHijo.getOrden());
				nodo.setNivel(requisitoHijo.getNivel());
				nodo.setEstado(1);
				nodo.setDescripcionReq(requisitoHijo.getDescripcionReq());
				nodo.setIdRequisitoPadre(requisitoHijo.getIdRequisitoPadre());
				nodo.setRequisitoRelacionado(requisitoHijo.getRequisitoRelacionado());
				nodo.setVdetreq(requisitoHijo.getVdetreq());
				nodo.setVcuesti(requisitoHijo.getVcuesti());
				//
				
				List<NodoRequisitos> nodosHijosRequisito = obtenerNodosHijos(nodo,listaRequisitos);
				if(nodosHijosRequisito.size()>0) {
					nodo.setChildren(nodosHijosRequisito);
				}
				
				nodosHijos.add(nodo);
			}
		}
		
		
		
		
		return nodosHijos;
	}*/

	/*@Override
	public List<NodoRequisito> obtenerListaRequisitos(RequisitoRequest parametros) {
		List<Norma> listaNormas = this.daoAuditoria.obtenerNormasAuditoria(parametros.getIdAuditoria());
		
		//listaNormas.stream().forEach(obj -> System.out.println(obj.getIdNorma()));
		RequisitoRequest request = null;
		List<NodoRequisito> listaRequisitos = new ArrayList<>();
		for(Norma norma: listaNormas) {
			request = new RequisitoRequest();
			request.setIdNorma(norma.getIdNorma());
			request.setTipoNorma("1");
			NodoRequisito requisito = this.obtenerRequisitos(request);
			listaRequisitos.add(requisito);
		}
		return listaRequisitos;
	}*/

}
